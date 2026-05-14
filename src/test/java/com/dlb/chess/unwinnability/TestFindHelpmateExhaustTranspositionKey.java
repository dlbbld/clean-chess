package com.dlb.chess.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;

/**
 * Verifies the helpmate-search transposition-key behaviour now exposed via {@link Board#getDynamicPosition()}.
 *
 * <p>
 * The helpmate search keys its visited-position map on {@code DynamicPosition}. The properties tested here are the
 * ones that distinguish {@code DynamicPosition} from a raw FEN-equality test: the halfmove and fullmove counters are
 * not part of position identity, and the en-passant target square is normalised away when no opposing pawn can
 * actually capture there.
 */
class TestFindHelpmateExhaustTranspositionKey {

  @SuppressWarnings("static-method")
  @Test
  void testIgnoresMoveCounters() {
    final var boardFirst = new Board("8/8/8/8/8/8/8/K6k w - - 0 1");
    final var boardSecond = new Board("8/8/8/8/8/8/8/K6k w - - 10 9");

    assertEquals(boardFirst.getDynamicPosition(), boardSecond.getDynamicPosition());
  }

  @SuppressWarnings("static-method")
  @Test
  void testIgnoresUncapturableEnPassantTarget() {
    final var boardWithTarget = new Board("8/8/8/8/4P3/8/8/K6k b - e3 0 1");
    final var boardWithoutTarget = new Board("8/8/8/8/4P3/8/8/K6k b - - 0 1");

    assertEquals(boardWithTarget.getDynamicPosition(), boardWithoutTarget.getDynamicPosition());
  }

  @SuppressWarnings("static-method")
  @Test
  void testKeepsCapturableEnPassantTarget() {
    final var boardWithTarget = new Board("8/8/8/8/3pP3/8/8/K6k b - e3 0 1");
    final var boardWithoutTarget = new Board("8/8/8/8/3pP3/8/8/K6k b - - 0 1");

    assertNotEquals(boardWithTarget.getDynamicPosition(), boardWithoutTarget.getDynamicPosition());
  }
}
