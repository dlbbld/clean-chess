package com.dlb.chess.test.board;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.fen.constants.FenConstants;

class TestBoardCopyCurrentPositionWithoutHistory {

  @SuppressWarnings("static-method")
  @Test
  void testMatchesFenRoundtripAfterPlayedLines() {
    checkAfterMoves("e4", "e5", "Nf3", "Nc6", "Bb5", "a6", "O-O");
    checkAfterMoves("e4", "Nf6", "e5", "d5");
    checkAfterMoves("Nc3", "Nc6", "Nf3", "Nf6", "e4", "e5", "a3", "a6", "Ke2", "Rg8");
  }

  @SuppressWarnings("static-method")
  @Test
  void testMatchesFenConstructedBoardAndIsIdempotent() {
    checkFromFen(FenConstants.FEN_INITIAL_STR);
    checkFromFen("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 37 42");
    checkFromFen("4k3/8/8/3pP3/8/8/8/4K3 w - d6 0 2");
  }

  private static void checkAfterMoves(String... sanMoves) {
    final Board source = new Board(false);
    source.movesStrict(sanMoves);

    final Board expected = new Board(source.getFen(), false);
    final Board actual = source.copyCurrentPositionWithoutHistory(false);

    assertEquivalentHistorylessBoard(expected, actual);
  }

  private static void checkFromFen(String fen) {
    final Board source = new Board(fen, false);
    final Board copy = source.copyCurrentPositionWithoutHistory(false);
    final Board copyOfCopy = copy.copyCurrentPositionWithoutHistory(false);

    assertEquivalentHistorylessBoard(source, copy);
    assertEquivalentHistorylessBoard(source, copyOfCopy);
  }

  private static void assertEquivalentHistorylessBoard(Board expected, Board actual) {
    assertEquals(0, actual.getPerformedHalfMoveCount());
    assertEquals(0, actual.getPerformedLegalMoveList().size());
    assertEquals(0, actual.getHalfMoveList().size());

    assertEquals(expected.getFen(), actual.getFen());
    assertEquals(expected.getInitialFen(), actual.getInitialFen());
    assertEquals(expected.getStaticPosition(), actual.getStaticPosition());
    assertEquals(expected.getHavingMove(), actual.getHavingMove());
    assertEquals(expected.getCastlingRightWhite(), actual.getCastlingRightWhite());
    assertEquals(expected.getCastlingRightBlack(), actual.getCastlingRightBlack());
    assertEquals(expected.getEnPassantCaptureTargetSquare(), actual.getEnPassantCaptureTargetSquare());
    assertEquals(expected.getHalfMoveClock(), actual.getHalfMoveClock());
    assertEquals(expected.getFullMoveNumberForNextHalfMove(), actual.getFullMoveNumberForNextHalfMove());
    assertEquals(expected.getLegalMoves(), actual.getLegalMoves());
    assertEquals(expected.isCheck(), actual.isCheck());
    assertEquals(expected.isCheckmate(), actual.isCheckmate());
    assertEquals(expected.isStalemate(), actual.isStalemate());
    assertEquals(expected.isInsufficientMaterial(), actual.isInsufficientMaterial());
    assertEquals(expected.isDeadPositionUnwinnableQuick(), actual.isDeadPositionUnwinnableQuick());
  }
}
