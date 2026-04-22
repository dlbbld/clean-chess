package com.dlb.chess.pgn.parser.sequential;

/**
 * A single lexical unit produced by a PGN tokenizer. The {@code text} field carries the logical payload — for tag
 * names, tag values, SAN, comments and so on, this is the semantic content (values unquoted, comments without braces).
 * Structural tokens like {@link PgnTokenType#TAG_BRACKET_OPEN} or {@link PgnTokenType#NEWLINE} carry the literal text
 * of the character(s) consumed.
 *
 * <p>Line and column are one-based and refer to the first character of the token in the source.
 */
public record PgnToken(PgnTokenType type, String text, int line, int column) {
}
