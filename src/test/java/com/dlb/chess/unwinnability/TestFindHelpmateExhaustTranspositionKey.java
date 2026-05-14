package com.dlb.chess.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;

class TestFindHelpmateExhaustTranspositionKey {

  @SuppressWarnings("static-method")
  @Test
  void testIgnoresMoveCounters() {
    final var boardFirst = new Board("8/8/8/8/8/8/8/K6k w - - 0 1");
    final var boardSecond = new Board("8/8/8/8/8/8/8/K6k w - - 10 9");

    assertEquals(FindHelpmateExhaust.calculateTranspositionKey(boardFirst),
        FindHelpmateExhaust.calculateTranspositionKey(boardSecond));
  }

  @SuppressWarnings("static-method")
  @Test
  void testIgnoresUncapturableEnPassantTarget() {
    final var boardWithTarget = new Board("8/8/8/8/4P3/8/8/K6k b - e3 0 1");
    final var boardWithoutTarget = new Board("8/8/8/8/4P3/8/8/K6k b - - 0 1");

    assertEquals(FindHelpmateExhaust.calculateTranspositionKey(boardWithTarget),
        FindHelpmateExhaust.calculateTranspositionKey(boardWithoutTarget));
  }

  @SuppressWarnings("static-method")
  @Test
  void testKeepsCapturableEnPassantTarget() {
    final var boardWithTarget = new Board("8/8/8/8/3pP3/8/8/K6k b - e3 0 1");
    final var boardWithoutTarget = new Board("8/8/8/8/3pP3/8/8/K6k b - - 0 1");

    assertNotEquals(FindHelpmateExhaust.calculateTranspositionKey(boardWithTarget),
        FindHelpmateExhaust.calculateTranspositionKey(boardWithoutTarget));
  }
}
