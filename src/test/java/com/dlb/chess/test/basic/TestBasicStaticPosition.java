package com.dlb.chess.test.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.fen.FenParser;
import com.dlb.chess.fen.constants.FenConstants;

class TestBasicStaticPosition implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void testClearNonEmpty() throws Exception {
    StaticPosition workingPosition = StaticPosition.INITIAL_POSITION;
    for (final Square square : Square.BOARD_SQUARE_LIST) {
      if (!workingPosition.isEmpty(square)) {
        workingPosition = workingPosition.createChangedPosition(square);
      }
    }

    assertEquals(StaticPosition.EMPTY_POSITION, workingPosition);
  }

  @SuppressWarnings("static-method")
  @Test
  void testInitialPosition() throws Exception {
    final StaticPosition staticInitialPositionActual = FenParser.parseAdvancedFen(FenConstants.FEN_INITIAL_STR)
        .staticPosition();

    assertEquals(StaticPosition.INITIAL_POSITION, staticInitialPositionActual);
  }

  @SuppressWarnings("static-method")
  @Test
  void testFill() throws Exception {
    StaticPosition workingPosition = StaticPosition.EMPTY_POSITION;
    for (final Square square : Square.BOARD_SQUARE_LIST) {
      workingPosition = workingPosition.createChangedPosition(square, WHITE_PAWN);
    }
    final StaticPosition staticAllPawnPositionActual = new StaticPosition(WHITE_PAWN, WHITE_PAWN, WHITE_PAWN,
        WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN,
        WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN,
        WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN,
        WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN,
        WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN,
        WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN,
        WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN);

    assertEquals(workingPosition, staticAllPawnPositionActual);
  }

  @SuppressWarnings("static-method")
  @Test
  void testBoardUseNoneSquareForClearMethod() {
    final StaticPosition emptyPosition = StaticPosition.EMPTY_POSITION;
    var isCorrectException = false;
    try {
      emptyPosition.createChangedPosition(Square.NONE);
    } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
      isCorrectException = true;
    }
    assertTrue(isCorrectException);
  }

  @SuppressWarnings("static-method")
  @Test
  void testBoardUseNoneSquareForPutMethod() {
    final StaticPosition emptyPosition = StaticPosition.EMPTY_POSITION;

    for (final Piece piece : Piece.values()) {
      if (piece == Piece.NONE) {
        continue;
      }
      var isCorrectException = false;
      try {
        emptyPosition.createChangedPosition(Square.NONE, piece);
      } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
        isCorrectException = true;
      }
      assertTrue(isCorrectException);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testBoardUseNonePieceForPutMethod() {
    final StaticPosition emptyPosition = StaticPosition.EMPTY_POSITION;

    for (final Square square : Square.values()) {
      if (square == Square.NONE) {
        continue;
      }
      var isCorrectException = false;
      try {
        emptyPosition.createChangedPosition(square, Piece.NONE);
      } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
        isCorrectException = true;
      }
      assertTrue(isCorrectException);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testBoardPuttingPieces() {
    StaticPosition workingPosition = StaticPosition.EMPTY_POSITION;

    for (final Square square : Square.values()) {
      if (square == Square.NONE) {
        continue;
      }
      for (final Piece piece : Piece.values()) {
        if (piece == Piece.NONE) {
          continue;
        }
        assertEquals(Piece.NONE, workingPosition.get(square));
        workingPosition = workingPosition.createChangedPosition(square, piece);
        assertEquals(piece, workingPosition.get(square));
        workingPosition = workingPosition.createChangedPosition(square);
        assertEquals(Piece.NONE, workingPosition.get(square));
        workingPosition = workingPosition.createChangedPosition(square, piece);
        assertEquals(piece, workingPosition.get(square));
        workingPosition = workingPosition.createChangedPosition(square);
        assertEquals(StaticPosition.EMPTY_POSITION, workingPosition);
      }
    }
  }

}
