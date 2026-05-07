package com.dlb.chess.pgn.parser;

/**
 * Normalises any line-ending convention in a PGN source string to a single canonical form: LF (`\n`).
 *
 * <p>CRLF and lone CR are both rewritten to LF before tokenization. The library's invariant — applied uniformly to
 * tag values, movetext, and {@code {...}} commentary content — is that no `\r` ever reaches the model.
 */
public final class NewlineNormalization {

  private NewlineNormalization() {
    // utility class
  }

  /**
   * Returns {@code source} with every CRLF and every standalone CR replaced by LF. Inputs that already use only LF
   * are returned unchanged (and short-circuited so the common case allocates nothing).
   */
  public static String toLf(String source) {
    if (source.indexOf('\r') < 0) {
      return source;
    }
    final StringBuilder result = new StringBuilder(source.length());
    final int len = source.length();
    for (int i = 0; i < len; i++) {
      final char c = source.charAt(i);
      if (c == '\r') {
        result.append('\n');
        // Skip the LF half of a CRLF pair.
        if (i + 1 < len && source.charAt(i + 1) == '\n') {
          i++;
        }
      } else {
        result.append(c);
      }
    }
    return result.toString();
  }
}
