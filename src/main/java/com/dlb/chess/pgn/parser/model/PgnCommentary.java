package com.dlb.chess.pgn.parser.model;

import com.dlb.chess.common.exceptions.PgnCommentaryValidationException;

/**
 * Value object holding a PGN commentary string. Construction validates the contract; once a {@code PgnCommentary}
 * exists, its content satisfies the contract by typing — downstream code does not need to validate again.
 *
 * <h2>Contract</h2>
 *
 * <p>
 * <b>Forbidden — grammar:</b>
 * <ul>
 * <li>{@code }} — closing brace, terminates the {@code {...}} grammar on export.</li>
 * </ul>
 *
 * <p>
 * Note: {@code {} (opening brace) is <em>allowed</em> in commentary content per the PGN spec — "a left brace
 * character appearing in a brace comment loses its special meaning and is ignored" (§8.2.5). Both parsers consume
 * inner {@code {} as a literal content character, and the exporter writes it verbatim; the inner {@code {} round-trips
 * unchanged because only {@code }} can close a brace comment.
 *
 * <p>
 * <b>Forbidden — Unicode categories:</b>
 * <ul>
 * <li>{@link Character#CONTROL} (Cc) — except for the three explicitly permitted control characters
 *     {@code \t} (U+0009), {@code \n} (U+000A), and {@code \r} (U+000D), which are valid commentary content
 *     per the PGN standard's distinction between string tokens and brace commentary.</li>
 * <li>{@link Character#SURROGATE} (Cs) — lone high or low surrogate code points; these are UTF-16 encoding
 *     artefacts, not real characters.</li>
 * <li>{@link Character#UNASSIGNED} (Cn) — code points not assigned by the Unicode Standard.</li>
 * <li>{@link Character#PRIVATE_USE} (Co) — code points reserved for private agreement; chess commentary should
 *     not depend on private fonts or in-house symbols.</li>
 * </ul>
 *
 * <p>
 * <b>Allowed:</b> all other Unicode categories — letters, marks, numbers, punctuation, symbols, separators,
 * format characters (Cf — zero-width joiner, BOM, bidi marks, soft hyphen). Supplementary characters above
 * U+FFFF (e.g. emoji, rare scripts encoded as surrogate pairs) round-trip correctly because the validator
 * iterates by code point, not by char.
 *
 * <h2>Why this contract</h2>
 *
 * <p>
 * The PGN standard restricts non-printing characters from <em>string tokens</em> (tag values), but is silent on
 * non-printing characters inside {@code {...}} commentary. Multiple chess tools — python-chess, Lichess — preserve
 * tabs and line breaks in commentary verbatim through the parse / export round-trip. We follow that convention,
 * extended with sanity checks against malformed Unicode (lone surrogates, unassigned code points) and obviously
 * inappropriate content (other control characters, private-use code points). The result is permissive enough
 * for legitimate text and strict enough to surface data corruption at the model boundary.
 *
 * <h2>Sources of values</h2>
 * <ul>
 * <li>Strict and lenient parsers: parsed brace content goes directly through the constructor. Both parsers preserve
 *     source bytes verbatim.</li>
 * <li>Programmatic API: any caller invoking the constructor directly sees a
 *     {@link PgnCommentaryValidationException} on contract violation, with the offending character's index and
 *     Unicode category named in the message.</li>
 * </ul>
 */
public record PgnCommentary(String value) {

  /**
   * Compact constructor — validates the contract. Throws {@link PgnCommentaryValidationException} on violation.
   */
  public PgnCommentary {
    validate(value);
  }

  /** The empty commentary. Convenience for the very common "no commentary" case. */
  public static final PgnCommentary EMPTY = new PgnCommentary("");

  private static void validate(String value) {
    for (int i = 0; i < value.length();) {
      final int cp = value.codePointAt(i);

      // Grammar prohibition takes priority — applies regardless of Unicode category. Only the closing brace `}`
      // is forbidden, since `}` would terminate the {...} grammar on export. The opening brace `{` is allowed
      // in content per the PGN spec (§8.2.5: "a left brace character appearing in a brace comment loses its
      // special meaning"); it round-trips unchanged.
      if (cp == '}') {
        throw new PgnCommentaryValidationException(formatBraceProblem(value, i));
      }

      // The three control characters explicitly permitted in commentary.
      if (cp != '\t' && cp != '\n' && cp != '\r') {
        final int type = Character.getType(cp);
        if (type == Character.CONTROL
            || type == Character.SURROGATE
            || type == Character.UNASSIGNED
            || type == Character.PRIVATE_USE) {
          throw new PgnCommentaryValidationException(formatTypeProblem(value, i, cp, type));
        }
      }

      i += Character.charCount(cp);
    }
  }

  private static String formatBraceProblem(String value, int index) {
    return "PGN commentary must not contain closing brace (at index " + index + " of: \""
        + summarizeForError(value) + "\").";
  }

  private static String formatTypeProblem(String value, int index, int cp, int type) {
    final String typeLabel = switch (type) {
      case Character.CONTROL -> "a control character";
      case Character.SURROGATE -> "a lone surrogate code point";
      case Character.UNASSIGNED -> "an unassigned code point";
      case Character.PRIVATE_USE -> "a private-use code point";
      default -> "an invalid character";
    };
    return "PGN commentary must not contain " + typeLabel
        + " (U+" + String.format("%04X", cp) + " at index " + index + " of: \""
        + summarizeForError(value) + "\").";
  }

  private static String summarizeForError(String value) {
    if (value.length() <= 80) {
      return value;
    }
    return value.substring(0, 77) + "...";
  }

}
