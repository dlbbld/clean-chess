package com.dlb.chess.pgn.parser.sequential;

/**
 * Forward-only character stream over a PGN source. Tracks one-based line and column numbers so tokenizers and parsers
 * can report positions in error messages. The source text is held verbatim — no line-ending normalization is performed
 * here, leaving that decision to the tokenizer.
 */
public final class PgnCharStream {

  public static final int EOF = -1;

  private static final char LINE_FEED = '\n';
  private static final char CARRIAGE_RETURN = '\r';

  private final String source;

  private int index;
  private int line = 1;
  private int column = 1;

  public PgnCharStream(String source) {
    this.source = source;
  }

  public int peek() {
    return peek(0);
  }

  public int peek(int offset) {
    final int position = index + offset;
    if (position >= source.length()) {
      return EOF;
    }
    return source.charAt(position);
  }

  public int read() {
    if (index >= source.length()) {
      return EOF;
    }
    final char c = source.charAt(index);
    index++;
    if (c == LINE_FEED) {
      line++;
      column = 1;
    } else if (c == CARRIAGE_RETURN) {
      // column stays; a following \n handles the line increment. A lone \r still opens a new line.
      if (peek() != LINE_FEED) {
        line++;
        column = 1;
      }
    } else {
      column++;
    }
    return c;
  }

  public boolean isEof() {
    return index >= source.length();
  }

  public boolean atLineStart() {
    return column == 1;
  }

  public int line() {
    return line;
  }

  public int column() {
    return column;
  }

  public int index() {
    return index;
  }
}
