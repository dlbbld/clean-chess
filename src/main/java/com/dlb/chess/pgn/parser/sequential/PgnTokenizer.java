package com.dlb.chess.pgn.parser.sequential;

/**
 * Pull-based PGN tokenizer. The tokenizer is deliberately lenient in what it recognizes — it emits tokens for every
 * construct the strict-mode grammar cares about, without enforcing strict-specific inter-token constraints (exact
 * single spacing, required move numbers, termination marker presence). Those checks belong to the parser, which has
 * full contextual awareness.
 *
 * <p>The tokenizer operates as a finite automaton driven by the next character in the stream. It never looks further
 * than the next few characters and never retains the whole source, making it usable over a future streaming source.
 */
public final class PgnTokenizer {

  private static final int CHAR_EOF = PgnCharStream.EOF;

  private final PgnCharStream stream;

  private PgnToken peekedAt0;
  private PgnToken peekedAt1;

  public PgnTokenizer(PgnCharStream stream) {
    this.stream = stream;
  }

  public PgnToken peek() {
    if (peekedAt0 == null) {
      peekedAt0 = readNext();
    }
    return peekedAt0;
  }

  /** One-token lookahead past {@link #peek()}. */
  public PgnToken peekNext() {
    if (peekedAt0 == null) {
      peekedAt0 = readNext();
    }
    if (peekedAt1 == null) {
      peekedAt1 = readNext();
    }
    return peekedAt1;
  }

  public PgnToken next() {
    if (peekedAt0 != null) {
      final PgnToken token = peekedAt0;
      peekedAt0 = peekedAt1;
      peekedAt1 = null;
      return token;
    }
    return readNext();
  }

  private PgnToken readNext() {
    if (stream.isEof()) {
      return new PgnToken(PgnTokenType.EOF, "", stream.line(), stream.column());
    }

    final int line = stream.line();
    final int column = stream.column();
    final int c = stream.peek();

    if (c == '\r' || c == '\n') {
      return readNewline(line, column);
    }
    if (c == ' ' || c == '\t') {
      return readSpaces(line, column);
    }
    if (c == '[') {
      stream.read();
      return new PgnToken(PgnTokenType.TAG_BRACKET_OPEN, "[", line, column);
    }
    if (c == ']') {
      stream.read();
      return new PgnToken(PgnTokenType.TAG_BRACKET_CLOSE, "]", line, column);
    }
    if (c == '"') {
      return readTagValueString(line, column);
    }
    if (c == '{') {
      return readBraceComment(line, column);
    }
    if (c == '!' || c == '?') {
      return readMoveSuffixAnnotation(line, column);
    }
    if (c == '*') {
      stream.read();
      return new PgnToken(PgnTokenType.TERMINATION_MARKER, "*", line, column);
    }
    if (isAsciiDigit(c)) {
      return readDigitStarted(line, column);
    }
    return readSymbol(line, column);
  }

  private PgnToken readNewline(int line, int column) {
    final int first = stream.read();
    final StringBuilder text = new StringBuilder();
    text.append((char) first);
    if (first == '\r' && stream.peek() == '\n') {
      text.append((char) stream.read());
    }
    return new PgnToken(PgnTokenType.NEWLINE, text.toString(), line, column);
  }

  private PgnToken readSpaces(int line, int column) {
    final StringBuilder text = new StringBuilder();
    while (stream.peek() == ' ' || stream.peek() == '\t') {
      text.append((char) stream.read());
    }
    return new PgnToken(PgnTokenType.SPACES, text.toString(), line, column);
  }

  private PgnToken readTagValueString(int line, int column) {
    stream.read(); // opening quote
    final StringBuilder text = new StringBuilder();
    boolean terminated = false;
    while (true) {
      final int c = stream.peek();
      if (c == CHAR_EOF || c == '\n' || c == '\r') {
        break;
      }
      if (c == '"') {
        stream.read();
        terminated = true;
        break;
      }
      if (c == '\\') {
        stream.read();
        final int escaped = stream.peek();
        if (escaped == '\\' || escaped == '"') {
          text.append((char) stream.read());
        } else {
          text.append('\\');
        }
        continue;
      }
      text.append((char) stream.read());
    }
    return new PgnToken(terminated ? PgnTokenType.TAG_VALUE_STRING : PgnTokenType.TAG_VALUE_STRING_UNTERMINATED,
        text.toString(), line, column);
  }

  private PgnToken readBraceComment(int line, int column) {
    stream.read(); // opening brace
    final StringBuilder text = new StringBuilder();
    boolean closed = false;
    while (true) {
      final int c = stream.peek();
      if (c == CHAR_EOF) {
        break;
      }
      if (c == '}') {
        stream.read();
        closed = true;
        break;
      }
      text.append((char) stream.read());
    }
    return new PgnToken(closed ? PgnTokenType.BRACE_COMMENT : PgnTokenType.BRACE_COMMENT_UNCLOSED, text.toString(),
        line, column);
  }

  private PgnToken readMoveSuffixAnnotation(int line, int column) {
    final StringBuilder text = new StringBuilder();
    text.append((char) stream.read());
    final int next = stream.peek();
    if (next == '!' || next == '?') {
      text.append((char) stream.read());
    }
    return new PgnToken(PgnTokenType.MOVE_SUFFIX_ANNOTATION, text.toString(), line, column);
  }

  private PgnToken readDigitStarted(int line, int column) {
    final StringBuilder text = new StringBuilder();
    while (isAsciiDigit(stream.peek())) {
      text.append((char) stream.read());
    }
    final int next = stream.peek();
    if (next == '.') {
      return readMoveNumberDots(line, column, text);
    }
    if (next == '-' || next == '/') {
      return readTerminationAfterDigits(line, column, text);
    }
    return new PgnToken(PgnTokenType.SYMBOL, text.toString(), line, column);
  }

  private PgnToken readMoveNumberDots(int line, int column, StringBuilder digits) {
    final StringBuilder text = new StringBuilder(digits);
    int dotCount = 0;
    while (stream.peek() == '.') {
      text.append((char) stream.read());
      dotCount++;
    }
    if (dotCount >= 3) {
      return new PgnToken(PgnTokenType.MOVE_NUMBER_BLACK, text.toString(), line, column);
    }
    return new PgnToken(PgnTokenType.MOVE_NUMBER_WHITE, text.toString(), line, column);
  }

  private PgnToken readTerminationAfterDigits(int line, int column, StringBuilder digits) {
    final StringBuilder text = new StringBuilder(digits);
    while (true) {
      final int c = stream.peek();
      if (c == '-' || c == '/' || isAsciiDigit(c)) {
        text.append((char) stream.read());
      } else {
        break;
      }
    }
    return new PgnToken(PgnTokenType.TERMINATION_MARKER, text.toString(), line, column);
  }

  private PgnToken readSymbol(int line, int column) {
    final StringBuilder text = new StringBuilder();
    while (true) {
      final int c = stream.peek();
      if (c == CHAR_EOF || isWordBreak(c)) {
        break;
      }
      text.append((char) stream.read());
    }
    if (text.length() == 0) {
      // Unrecognized single character — consume it so we always make progress. The parser will reject this as a
      // structural error when it encounters the resulting symbol.
      text.append((char) stream.read());
    }
    return new PgnToken(PgnTokenType.SYMBOL, text.toString(), line, column);
  }

  private static boolean isWordBreak(int c) {
    return c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '[' || c == ']' || c == '{' || c == '}'
        || c == '"' || c == '!' || c == '?';
  }

  private static boolean isAsciiDigit(int c) {
    return c >= '0' && c <= '9';
  }
}
