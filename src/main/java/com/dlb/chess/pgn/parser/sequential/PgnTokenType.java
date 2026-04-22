package com.dlb.chess.pgn.parser.sequential;

/**
 * Lexical categories the sequential PGN tokenizer can emit. The set is intentionally narrow — only the constructs the
 * strict-mode parser needs. Additional categories (NAG, variations, semicolon comments, line-start escape) are added
 * when the lenient parser requires them.
 */
public enum PgnTokenType {

  /**
   * The {@code [} character introducing a tag pair on its own line.
   */
  TAG_BRACKET_OPEN,

  /**
   * The {@code ]} character terminating a tag pair.
   */
  TAG_BRACKET_CLOSE,

  /** Tag value — the unescaped content between the quotes, without the surrounding quotes. */
  TAG_VALUE_STRING,

  /**
   * Tag value that started with {@code "} but was not terminated before end-of-line or end-of-input.
   */
  TAG_VALUE_STRING_UNTERMINATED,

  /** Run of space and tab characters inside a line. */
  SPACES,

  /**
   * Single {@code \n} or {@code \r\n} line terminator.
   */
  NEWLINE,

  /**
   * Move number indicator for the side to move, e.g. {@code 1.} or {@code 12.}.
   */
  MOVE_NUMBER_WHITE,

  /**
   * Move number indicator for the continuation of black's move, e.g. {@code 1...} or {@code 12...}.
   */
  MOVE_NUMBER_BLACK,

  /**
   * Generic PGN symbol token — letters, digits, and the continuation punctuation set. The parser decides by context
   * whether a given symbol is a tag name, a SAN half-move, or something else.
   */
  SYMBOL,

  /**
   * Move suffix annotation — {@code !}, {@code !!}, {@code ?}, {@code ??}, {@code !?}, {@code ?!}.
   */
  MOVE_SUFFIX_ANNOTATION,

  /**
   * Brace-delimited commentary {@code {...}}. The token text is the commentary content without the braces.
   */
  BRACE_COMMENT,

  /**
   * A brace commentary whose closing brace was not found before end of input. The token text contains everything read
   * after the opening brace. The parser uses this to produce the specific "start brace not followed by end brace"
   * validation error rather than a generic structural error.
   */
  BRACE_COMMENT_UNCLOSED,

  /**
   * Game termination marker — {@code 1-0}, {@code 0-1}, {@code 1/2-1/2}, {@code *}.
   */
  TERMINATION_MARKER,

  /** End of the character source. */
  EOF
}
