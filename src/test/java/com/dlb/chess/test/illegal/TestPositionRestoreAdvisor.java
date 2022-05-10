package com.dlb.chess.test.illegal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.fen.FenParser;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.illegal.PositionRestoreAdvisor;
import com.dlb.chess.illegal.model.RestorePositionAdvice;
import com.dlb.chess.illegal.model.RestorePositionAdviceEmptySquarePutPieceFromOtherSquare;
import com.dlb.chess.illegal.model.RestorePositionAdviceEmptySquarePutSparePiece;
import com.dlb.chess.illegal.model.RestorePositionAdviceNonEmptySquareMoveToOtherSquare;
import com.dlb.chess.illegal.model.RestorePositionAdviceNonEmptySquareRemoveFromBoard;
import com.dlb.chess.illegal.model.RestorePositionAdviceNonEmptySquareSwapPiece;
import com.dlb.chess.illegal.model.RestorePositionAdviceNone;

class TestPositionRestoreAdvisor {

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {

    // position equals
    {
      final RestorePositionAdvice model = calculatePositionAdvice(FenConstants.PIECE_PLACEMENT_INITIAL,
          FenConstants.PIECE_PLACEMENT_INITIAL);
      final var isCorrectType = model instanceof RestorePositionAdviceNone;
      assertTrue(isCorrectType);
    }

    // empty square - put piece from other square - one option
    {
      final RestorePositionAdvice model = calculatePositionAdvice(FenConstants.PIECE_PLACEMENT_INITIAL,
          FenConstants.PIECE_PLACEMENT_AFTER_E4);
      final var isCorrectType = model instanceof RestorePositionAdviceEmptySquarePutPieceFromOtherSquare;
      assertTrue(isCorrectType);
      final var advice = (RestorePositionAdviceEmptySquarePutPieceFromOtherSquare) model;
      assertEquals(Square.E2, advice.square());
      assertEquals(Arrays.asList(Square.E4), advice.orderedFromSquareList());
    }

    // empty square - put piece from other square - two options
    {
      final RestorePositionAdvice model = calculatePositionAdvice("rnbqkbnr/pppppppp/8/8/8/8/PPPPPP2/RNBQKBNR",
          "rnbqkbnr/pppppppp/8/8/8/8/1PPPPPPP/RNBQKBNR");
      final var isCorrectType = model instanceof RestorePositionAdviceEmptySquarePutPieceFromOtherSquare;
      assertTrue(isCorrectType);
      final var advice = (RestorePositionAdviceEmptySquarePutPieceFromOtherSquare) model;
      assertEquals(Square.A2, advice.square());
      assertEquals(Arrays.asList(Square.G2, Square.H2), advice.orderedFromSquareList());
    }

    // empty square - put piece from other square - more than two options
    {
      final RestorePositionAdvice model = calculatePositionAdvice("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR",
          "rnbqkbnr/pppppppp/8/8/8/PPPPPPPP/8/RNBQKBNR");
      final var isCorrectType = model instanceof RestorePositionAdviceEmptySquarePutPieceFromOtherSquare;
      assertTrue(isCorrectType);
      final var advice = (RestorePositionAdviceEmptySquarePutPieceFromOtherSquare) model;
      assertEquals(Square.A2, advice.square());
      assertEquals(
          Arrays.asList(Square.A3, Square.B3, Square.C3, Square.D3, Square.E3, Square.F3, Square.G3, Square.H3),
          advice.orderedFromSquareList());
    }

    // empty square - put spare piece
    {
      final RestorePositionAdvice model = calculatePositionAdvice(FenConstants.PIECE_PLACEMENT_INITIAL,
          "r1bqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
      final var isCorrectType = model instanceof RestorePositionAdviceEmptySquarePutSparePiece;
      assertTrue(isCorrectType);
      final var advice = (RestorePositionAdviceEmptySquarePutSparePiece) model;
      assertEquals(Square.B8, advice.square());
    }

    // non-empty square - swap piece - one option
    {
      final RestorePositionAdvice model = calculatePositionAdvice("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR",
          "rbnqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
      final var isCorrectType = model instanceof RestorePositionAdviceNonEmptySquareSwapPiece;
      assertTrue(isCorrectType);
      final var advice = (RestorePositionAdviceNonEmptySquareSwapPiece) model;
      assertEquals(Square.B8, advice.square());
      assertEquals(Arrays.asList(Square.C8), advice.orderedSwapSquareList());
    }

    // non-empty square - swap piece - two options
    {
      final RestorePositionAdvice model = calculatePositionAdvice("rnbqkbnr/pppppppp/8/8/8/B7/PPPPPPPP/RNBQK1NR",
          "rbnqkbnr/pppppppp/8/8/8/N7/PPPPPPPP/RBNQKB1R");
      final var isCorrectType = model instanceof RestorePositionAdviceNonEmptySquareSwapPiece;
      assertTrue(isCorrectType);
      final var advice = (RestorePositionAdviceNonEmptySquareSwapPiece) model;
      assertEquals(Square.B1, advice.square());
      assertEquals(Arrays.asList(Square.C1, Square.A3), advice.orderedSwapSquareList());
    }

    // non-empty square - swap piece - more than two options
    {
      final RestorePositionAdvice model = calculatePositionAdvice("rnbqkbnr/pppppppp/R7/8/3R4/8/8/RNBQKBNR",
          "rnbqkbnr/pppppppp/B7/8/3B4/8/8/RNBQKRNB");
      final var isCorrectType = model instanceof RestorePositionAdviceNonEmptySquareSwapPiece;
      assertTrue(isCorrectType);
      final var advice = (RestorePositionAdviceNonEmptySquareSwapPiece) model;
      assertEquals(Square.F1, advice.square());
      assertEquals(Arrays.asList(Square.H1, Square.D4, Square.A6), advice.orderedSwapSquareList());
    }

    // non-empty square - move to other square - one option
    {
      final RestorePositionAdvice model = calculatePositionAdvice(FenConstants.PIECE_PLACEMENT_AFTER_E4,
          FenConstants.PIECE_PLACEMENT_INITIAL);
      final var isCorrectType = model instanceof RestorePositionAdviceNonEmptySquareMoveToOtherSquare;
      assertTrue(isCorrectType);
      final var advice = (RestorePositionAdviceNonEmptySquareMoveToOtherSquare) model;
      assertEquals(Square.E2, advice.square());
      assertEquals(Arrays.asList(Square.E4), advice.orderedToSquareList());
    }

    // non-empty square - move to other square - two options
    {
      final RestorePositionAdvice model = calculatePositionAdvice("rnbqkbnr/pppppppp/8/8/3PP3/8/PPP2PPP/RNBQKBNR",
          "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
      final var isCorrectType = model instanceof RestorePositionAdviceNonEmptySquareMoveToOtherSquare;
      assertTrue(isCorrectType);
      final var advice = (RestorePositionAdviceNonEmptySquareMoveToOtherSquare) model;
      assertEquals(Square.D2, advice.square());
      assertEquals(Arrays.asList(Square.D4, Square.E4), advice.orderedToSquareList());
    }

    // non-empty square - move from board
    {
      final RestorePositionAdvice model = calculatePositionAdvice("r1bqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR",
          FenConstants.PIECE_PLACEMENT_INITIAL);
      final var isCorrectType = model instanceof RestorePositionAdviceNonEmptySquareRemoveFromBoard;
      assertTrue(isCorrectType);
      final var advice = (RestorePositionAdviceNonEmptySquareRemoveFromBoard) model;
      assertEquals(Square.B8, advice.square());
    }

    // non-empty square - move from board
    {
      final RestorePositionAdvice model = calculatePositionAdvice("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNB1KB1R",
          FenConstants.PIECE_PLACEMENT_INITIAL);
      final var isCorrectType = model instanceof RestorePositionAdviceNonEmptySquareRemoveFromBoard;
      assertTrue(isCorrectType);
      final var advice = (RestorePositionAdviceNonEmptySquareRemoveFromBoard) model;
      assertEquals(Square.D1, advice.square());
    }

  }

  private static RestorePositionAdvice calculatePositionAdvice(String correctPiecePlacement,
      String incorrectPiecePlacement) {
    final StaticPosition correctPosition = FenParser.validatePiecePlacement(correctPiecePlacement);
    final StaticPosition incorrectPosition = FenParser.validatePiecePlacement(incorrectPiecePlacement);

    return PositionRestoreAdvisor.calculatePositionAdvice(correctPosition, incorrectPosition);
  }
}
