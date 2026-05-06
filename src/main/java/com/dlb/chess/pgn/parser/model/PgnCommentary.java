package com.dlb.chess.pgn.parser.model;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.PgnCommentaryValidationException;

/**
 * Value object holding a PGN commentary string. Construction validates the contract; once a {@code PgnCommentary}
 * exists, its content satisfies the contract by typing — downstream code does not need to validate again.
 *
 * <h2>Contract</h2> The wrapped {@link String} must contain none of:
 * <ul>
 * <li>tab character ({@code \t}, U+0009),</li>
 * <li>newline characters ({@code \n}, U+000A; {@code \r}, U+000D),</li>
 * <li>any other ASCII control character (U+0000–U+001F, U+007F).</li>
 * </ul>
 * Spaces are permitted, including consecutive spaces.
 *
 * <h2>Sources of values</h2>
 * <ul>
 * <li>Strict-parser path: parsed brace content goes directly through the constructor; forbidden characters surface as a
 * strict-parser validation exception via the parser's wrapping logic.</li>
 * <li>Lenient-parser path: parsed brace content goes through {@link #fromLenientImport(String)} which substitutes
 * tab / newline / carriage return / CRLF with single spaces, then validates via the constructor — remaining control
 * characters surface as a lenient-parser validation exception via the parser's wrapping logic.</li>
 * <li>Programmatic API: any caller invoking the constructor directly sees a
 * {@link PgnCommentaryValidationException} on contract violation.</li>
 * </ul>
 *
 * <h2>Whitespace policy (deliberate)</h2> Multiple consecutive spaces are <strong>preserved</strong>, never collapsed.
 * This keeps strict and lenient semantics aligned — strict accepts the source as-is, lenient only substitutes the
 * forbidden whitespace classes (tab/newline/CR) with single spaces. Two parses of the same content always produce equal
 * {@code PgnCommentary} values.
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

  /**
   * Builds a {@code PgnCommentary} from raw text extracted by the lenient parser.
   *
   * <p>
   * Substitutes tab, newline, carriage return, and CRLF with single spaces. CRLF is recognised as a unit and replaced
   * with a single space; loose CR or LF each become a space. Tab becomes a space. After substitution the constructor
   * validates the result; if any non-tab/newline/CR control character remains, the constructor throws.
   */
  public static PgnCommentary fromLenientImport(String raw) {
    final String s1 = NonNullWrapperCommon.replace(raw, "\r\n", " ");
    final String s2 = NonNullWrapperCommon.replace(s1, "\r", " ");
    final String s3 = NonNullWrapperCommon.replace(s2, "\n", " ");
    final String substituted = NonNullWrapperCommon.replace(s3, "\t", " ");
    return new PgnCommentary(substituted);
  }

  private static void validate(String value) {
    for (int i = 0; i < value.length(); i++) {
      final char c = value.charAt(i);
      if (isForbidden(c)) {
        throw new PgnCommentaryValidationException(formatProblem(value, i, c));
      }
    }
  }

  private static boolean isForbidden(char c) {
    // ASCII control characters (C0): U+0000–U+001F. DEL: U+007F.
    return c < 0x20 || c == 0x7F;
  }

  private static String formatProblem(String value, int index, char c) {
    final String charLabel = switch (c) {
      case '\t' -> "tab";
      case '\n' -> "newline (LF)";
      case '\r' -> "carriage return (CR)";
      default -> NonNullWrapperCommon.format("control character U+%04X", (int) c);
    };
    return "PGN commentary must not contain " + charLabel + " (at index " + index + " of: \""
        + summarizeForError(value) + "\").";
  }

  private static String summarizeForError(String value) {
    if (value.length() <= 80) {
      return value;
    }
    return NonNullWrapperCommon.substring(value, 0, 77) + "...";
  }

}
