package com.dlb.chess.pgn.parser.model;

import com.dlb.chess.common.exceptions.PgnCommentaryValidationException;

/**
 * Value object holding a PGN commentary string. Construction validates the contract; once a {@code PgnCommentary}
 * exists, its content satisfies the contract by typing — downstream code does not need to validate again.
 *
 * <h2>Contract</h2> The wrapped {@link String} must contain none of:
 * <ul>
 * <li>{@code {} — opening brace, ambiguous on export (would be re-parsed as a comment opener).</li>
 * <li>{@code }} — closing brace, terminates the {@code {...}} grammar on export.</li>
 * </ul>
 * Everything else is permitted, including:
 * <ul>
 * <li>tab ({@code \t}, U+0009),</li>
 * <li>line breaks ({@code \n} U+000A, {@code \r} U+000D, including the {@code \r\n} sequence),</li>
 * <li>extended-ASCII / multi-byte printable characters,</li>
 * <li>other ASCII control characters (bell, escape, DEL, …).</li>
 * </ul>
 *
 * <h2>Why this contract</h2>
 * <p>
 * The PGN standard restricts non-printing characters from <em>string tokens</em> (tag values), but is silent on
 * non-printing characters inside {@code {...}} commentary. Multiple chess tools — python-chess, Lichess — preserve
 * tabs and line breaks in commentary verbatim through the parse / export round-trip. We follow that convention: the
 * commentary content is whatever bytes lived between the braces in the source, modulo the grammar-mandated brace
 * exclusions.
 *
 * <h2>Sources of values</h2>
 * <ul>
 * <li>Strict and lenient parsers: parsed brace content goes directly through the constructor. Both parsers preserve
 * source bytes verbatim. The previous lenient-side substitution of tab / newline / CR with single space has been
 * removed — that was over-strict.</li>
 * <li>Programmatic API: any caller invoking the constructor directly sees a
 * {@link PgnCommentaryValidationException} on contract violation.</li>
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
    for (int i = 0; i < value.length(); i++) {
      final char c = value.charAt(i);
      if (c == '{' || c == '}') {
        throw new PgnCommentaryValidationException(formatProblem(value, i, c));
      }
    }
  }

  private static String formatProblem(String value, int index, char c) {
    final String charLabel = switch (c) {
      case '{' -> "opening brace";
      case '}' -> "closing brace";
      default -> "character '" + c + "'";
    };
    return "PGN commentary must not contain " + charLabel + " (at index " + index + " of: \""
        + summarizeForError(value) + "\").";
  }

  private static String summarizeForError(String value) {
    if (value.length() <= 80) {
      return value;
    }
    return value.substring(0, 77) + "...";
  }

}
