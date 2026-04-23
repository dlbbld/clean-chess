package com.dlb.chess.pgn.parser.sequential;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.common.NonNullWrapperCommon;

/**
 * Pull-based PGN tokenizer. The tokenizer is deliberately lenient in what it recognizes — it emits tokens for every
 * construct the strict-mode grammar cares about, without enforcing strict-specific inter-token constraints (exact
 * single spacing, required move numbers, termination marker presence). Those checks belong to the parser, which has
 * full contextual awareness.
 *
 * <p>
 * The tokenizer operates as a finite automaton driven by the next character in the stream. It never looks further than
 * the next few characters and never retains the whole source, making it usable over a future streaming source.
 */
public final class PgnTokenizer {

  private static final int CHAR_EOF = PgnCharStream.EOF;

  private final PgnCharStream stream;

  // The peek slots are lazily filled — null means "not yet read". Captured into locals before use so that JDT's
  // flow analysis can narrow them back to @NonNull before they escape as return values.
  private @Nullable PgnToken peekedAt0;
  private @Nullable PgnToken peekedAt1;

  public PgnTokenizer(PgnCharStream stream) {
    this.stream = stream;
  }

  public PgnToken peek() {
    PgnToken result = peekedAt0;
    if (result == null) {
      result = readNext();
      peekedAt0 = result;
    }
    return result;
  }

  /**
   * One-token lookahead past {@link #peek()}.
   */
  public PgnToken peekNext() {
    if (peekedAt0 == null) {
      peekedAt0 = readNext();
    }
    PgnToken result = peekedAt1;
    if (result == null) {
      result = readNext();
      peekedAt1 = result;
    }
    return result;
  }

  public PgnToken next() {
    final PgnToken front = peekedAt0;
    if (front != null) {
      peekedAt0 = peekedAt1;
      peekedAt1 = null;
      return front;
    }
    return readNext();
  }

  private PgnToken readNext() {
    if (stream.isEof()) {
      return new PgnToken(PgnTokenType.EOF, "", stream.line(), stream.column());
    }

    final var line = stream.line();
    final var column = stream.column();
    final var c = stream.peek();

    switch (c) {
      case '\r':
      case '\n':
        return readNewline(line, column);
      case ' ':
      case '\t':
        return readSpaces(line, column);
      case '[':
        stream.read();
        return new PgnToken(PgnTokenType.TAG_BRACKET_OPEN, "[", line, column);
      case ']':
        stream.read();
        return new PgnToken(PgnTokenType.TAG_BRACKET_CLOSE, "]", line, column);
      case '"':
        return readTagValueString(line, column);
      case '{':
        return readBraceComment(line, column);
      case '}':
        // Stray closing brace outside of any open commentary — always an error; the parser surfaces it.
        stream.read();
        return new PgnToken(PgnTokenType.BRACE_STRAY_CLOSE, "}", line, column);
      case '!':
      case '?':
        return readMoveSuffixAnnotation(line, column);
      case '*':
        stream.read();
        return new PgnToken(PgnTokenType.TERMINATION_MARKER, "*", line, column);
      default:
        break;
    }
    if (isAsciiDigit(c)) {
      return readDigitStarted(line, column);
    }
    return readSymbol(line, column);
  }

  private PgnToken readNewline(int line, int column) {
    final var first = stream.read();
    final StringBuilder text = new StringBuilder();
    text.append((char) first);
    if (first == '\r' && stream.peek() == '\n') {
      text.append((char) stream.read());
    }
    return new PgnToken(PgnTokenType.NEWLINE, NonNullWrapperCommon.toString(text), line, column);
  }

  private PgnToken readSpaces(int line, int column) {
    final StringBuilder text = new StringBuilder();
    while (stream.peek() == ' ' || stream.peek() == '\t') {
      text.append((char) stream.read());
    }
    return new PgnToken(PgnTokenType.SPACES, NonNullWrapperCommon.toString(text), line, column);
  }

  private PgnToken readTagValueString(int line, int column) {
    stream.read(); // opening quote
    final StringBuilder text = new StringBuilder();
    var terminated = false;
    while (true) {
      final var c = stream.peek();
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
        final var escaped = stream.peek();
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
        NonNullWrapperCommon.toString(text), line, column);
  }

  private PgnToken readBraceComment(int line, int column) {
    stream.read(); // opening brace
    final StringBuilder text = new StringBuilder();
    while (true) {
      final var c = stream.peek();
      if (c == CHAR_EOF) {
        // EOF before matching } — unclosed commentary.
        return new PgnToken(PgnTokenType.BRACE_COMMENT_UNCLOSED, NonNullWrapperCommon.toString(text), line, column);
      }
      if (c == '{') {
        // A second { before the first } — commentary cannot nest. Leave the inner { unconsumed; the parser throws
        // on the nested-token, so what follows in the stream does not matter for correctness.
        return new PgnToken(PgnTokenType.BRACE_COMMENT_NESTED, NonNullWrapperCommon.toString(text), line, column);
      }
      if (c == '}') {
        stream.read();
        return new PgnToken(PgnTokenType.BRACE_COMMENT, NonNullWrapperCommon.toString(text), line, column);
      }
      text.append((char) stream.read());
    }
  }

  private PgnToken readMoveSuffixAnnotation(int line, int column) {
    final StringBuilder text = new StringBuilder();
    text.append((char) stream.read());
    final var next = stream.peek();
    if (next == '!' || next == '?') {
      text.append((char) stream.read());
    }
    return new PgnToken(PgnTokenType.MOVE_SUFFIX_ANNOTATION, NonNullWrapperCommon.toString(text), line, column);
  }

  private PgnToken readDigitStarted(int line, int column) {
    final StringBuilder text = new StringBuilder();
    while (isAsciiDigit(stream.peek())) {
      text.append((char) stream.read());
    }
    final var next = stream.peek();
    if (next == '.') {
      return readMoveNumberDots(line, column, text);
    }
    if (next == '-' || next == '/') {
      return readTerminationAfterDigits(line, column, text);
    }
    return new PgnToken(PgnTokenType.SYMBOL, NonNullWrapperCommon.toString(text), line, column);
  }

  private PgnToken readMoveNumberDots(int line, int column, StringBuilder digits) {
    final StringBuilder text = new StringBuilder(digits);
    var dotCount = 0;
    while (stream.peek() == '.') {
      text.append((char) stream.read());
      dotCount++;
    }
    if (dotCount >= 3) {
      return new PgnToken(PgnTokenType.MOVE_NUMBER_BLACK, NonNullWrapperCommon.toString(text), line, column);
    }
    return new PgnToken(PgnTokenType.MOVE_NUMBER_WHITE, NonNullWrapperCommon.toString(text), line, column);
  }

  private PgnToken readTerminationAfterDigits(int line, int column, StringBuilder digits) {
    final StringBuilder text = new StringBuilder(digits);
    while (true) {
      final var c = stream.peek();
      if (c != '-' && c != '/' && !isAsciiDigit(c)) {
        break;
      }
      text.append((char) stream.read());
    }
    return new PgnToken(PgnTokenType.TERMINATION_MARKER, NonNullWrapperCommon.toString(text), line, column);
  }

  private PgnToken readSymbol(int line, int column) {
    final StringBuilder text = new StringBuilder();
    while (true) {
      final var c = stream.peek();
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
    return new PgnToken(PgnTokenType.SYMBOL, NonNullWrapperCommon.toString(text), line, column);
  }

  private static boolean isWordBreak(int c) {
    return c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '[' || c == ']' || c == '{' || c == '}' || c == '"'
        || c == '!' || c == '?';
  }

  private static boolean isAsciiDigit(int c) {
    return c >= '0' && c <= '9';
  }
}
