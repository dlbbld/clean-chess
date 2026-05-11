package com.dlb.chess.common.exceptions;

/**
 * Thrown when a PGN commentary string violates the model contract: must contain no tab, newline, carriage return, or
 * other non-printing/control character.
 *
 * <p>
 * Surfaces at the point of {@link com.dlb.chess.pgn.model.PgnCommentary} construction, which is invoked by:
 * <ul>
 * <li>programmatic API callers building a {@code PgnFile} or {@code PgnHalfMove} directly,</li>
 * <li>{@code StrictPgnParser} on commentary content extracted between {@code {} and {@code }},</li>
 * <li>{@code LenientPgnParser} after applying its tab/newline/CR-to-space substitution, if remaining control characters
 * are present.</li>
 * </ul>
 *
 * <p>
 * The two parsers wrap this into their own validation exception types so that callers see the parser-specific failure.
 * Programmatic API callers see this exception directly.
 */
public class PgnCommentaryValidationException extends UsageException {

  public PgnCommentaryValidationException(String message) {
    super(message);
  }

}
