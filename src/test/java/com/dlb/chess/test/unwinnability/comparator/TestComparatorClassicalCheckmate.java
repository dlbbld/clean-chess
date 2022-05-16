package com.dlb.chess.test.unwinnability.comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.exceptions.TestSetupException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.unwinnability.findhelpmate.comparator.ComparatorClassicalCheckmate;

public class TestComparatorClassicalCheckmate implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void testHavingMoveCapture() {
    final Board board = new Board("8/8/3pk3/8/8/8/3RK3/8 w - - 0 100");

    final ComparatorClassicalCheckmate comparator = new ComparatorClassicalCheckmate(WHITE, WHITE,
        board.getStaticPosition());

    {
      final LegalMove firstLegalMove = calculateLegalMove(WHITE, D2, D6, WHITE_ROOK, BLACK_PAWN, board);
      final LegalMove secondLegalMove = calculateLegalMove(WHITE, D2, D5, WHITE_ROOK, PIECE_NONE, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testHavingMoveSacrifice() {
    final Board board = new Board("8/8/3pk3/8/8/3R4/2R1K3/8 w - - 0 100");

    final ComparatorClassicalCheckmate comparator = new ComparatorClassicalCheckmate(WHITE, WHITE,
        board.getStaticPosition());

    {
      final LegalMove firstLegalMove = calculateLegalMove(WHITE, D3, D5, WHITE_ROOK, PIECE_NONE, board);
      final LegalMove secondLegalMove = calculateLegalMove(WHITE, D3, D4, WHITE_ROOK, PIECE_NONE, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
    {
      final LegalMove firstLegalMove = calculateLegalMove(WHITE, D3, D5, WHITE_ROOK, PIECE_NONE, board);
      final LegalMove secondLegalMove = calculateLegalMove(WHITE, E2, D1, WHITE_KING, PIECE_NONE, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
    {
      final LegalMove firstLegalMove = calculateLegalMove(WHITE, D3, D6, WHITE_ROOK, BLACK_PAWN, board);
      final LegalMove secondLegalMove = calculateLegalMove(WHITE, C2, C5, WHITE_ROOK, PIECE_NONE, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testHavingMovePawnPush() {
    final Board board = new Board("8/8/4k3/1p6/8/1p1P4/3NK3/8 w - - 0 100");

    final ComparatorClassicalCheckmate comparator = new ComparatorClassicalCheckmate(WHITE, WHITE,
        board.getStaticPosition());

    {
      final LegalMove firstLegalMove = calculateLegalMove(WHITE, D3, D4, WHITE_PAWN, PIECE_NONE, board);
      final LegalMove secondLegalMove = calculateLegalMove(WHITE, D2, C4, WHITE_KNIGHT, PIECE_NONE, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
    {
      final LegalMove firstLegalMove = calculateLegalMove(WHITE, D3, D4, WHITE_PAWN, PIECE_NONE, board);
      final LegalMove secondLegalMove = calculateLegalMove(WHITE, D2, B3, WHITE_KNIGHT, BLACK_PAWN, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
    {
      final LegalMove firstLegalMove = calculateLegalMove(WHITE, D3, D4, WHITE_PAWN, PIECE_NONE, board);
      final LegalMove secondLegalMove = calculateLegalMove(WHITE, E2, E3, WHITE_KING, PIECE_NONE, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testNotHavingMoveCaptureClassicalCheckmate() {
    // KQ
    {
      final Board board = new Board("8/1r1r4/8/4k3/8/1B1Q4/3NK3/8 b - - 0 100");

      final ComparatorClassicalCheckmate comparator = new ComparatorClassicalCheckmate(WHITE, BLACK,
          board.getStaticPosition());

      final LegalMove firstLegalMove = calculateLegalMove(BLACK, B7, B3, BLACK_ROOK, WHITE_BISHOP, board);
      final LegalMove secondLegalMove = calculateLegalMove(BLACK, D7, D3, BLACK_ROOK, WHITE_QUEEN, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
    // KR
    {
      final Board board = new Board("2q3R1/1r1r4/8/4k3/8/1B6/2N1K3/8 b - - 0 100");

      final ComparatorClassicalCheckmate comparator = new ComparatorClassicalCheckmate(WHITE, BLACK,
          board.getStaticPosition());

      final LegalMove firstLegalMove = calculateLegalMove(BLACK, C8, C2, BLACK_QUEEN, WHITE_KNIGHT, board);
      final LegalMove secondLegalMove = calculateLegalMove(BLACK, C8, G8, BLACK_QUEEN, WHITE_ROOK, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
    // KBB
    {
      final Board board = new Board("2q3B1/1r6/8/4k3/6r1/6P1/4K3/2B5 b - - 0 100");

      final ComparatorClassicalCheckmate comparator = new ComparatorClassicalCheckmate(WHITE, BLACK,
          board.getStaticPosition());

      final LegalMove firstLegalMove = calculateLegalMove(BLACK, G4, G3, BLACK_ROOK, WHITE_PAWN, board);
      final LegalMove secondLegalMove = calculateLegalMove(BLACK, G4, G8, BLACK_ROOK, WHITE_BISHOP, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
    // KBN
    {
      final Board board = new Board("8/1r1r4/8/4k3/8/1B1P4/3NK3/8 b - - 0 100");

      final ComparatorClassicalCheckmate comparator = new ComparatorClassicalCheckmate(WHITE, BLACK,
          board.getStaticPosition());

      final LegalMove firstLegalMove = calculateLegalMove(BLACK, D7, D3, BLACK_ROOK, WHITE_PAWN, board);
      final LegalMove secondLegalMove = calculateLegalMove(BLACK, B7, B3, BLACK_ROOK, WHITE_BISHOP, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testNotHavingMoveNoCaptureIfClassicalCheckmateMaterial() {
    // KQ
    {
      final Board board = new Board("8/8/2Qk4/8/8/8/3K4/8 b - - 0 100");

      final ComparatorClassicalCheckmate comparator = new ComparatorClassicalCheckmate(WHITE, BLACK,
          board.getStaticPosition());

      final LegalMove firstLegalMove = calculateLegalMove(BLACK, D6, E7, BLACK_KING, PIECE_NONE, board);
      final LegalMove secondLegalMove = calculateLegalMove(BLACK, D6, C6, BLACK_KING, WHITE_QUEEN, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
    // KR
    {
      final Board board = new Board("8/8/8/5k2/4R3/8/3K4/8 b - - 0 100");

      final ComparatorClassicalCheckmate comparator = new ComparatorClassicalCheckmate(WHITE, BLACK,
          board.getStaticPosition());

      final LegalMove firstLegalMove = calculateLegalMove(BLACK, F5, F6, BLACK_KING, PIECE_NONE, board);
      final LegalMove secondLegalMove = calculateLegalMove(BLACK, F5, E4, BLACK_KING, WHITE_ROOK, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
    // KBB
    {
      final Board board = new Board("8/8/5k2/4BB2/8/2K5/8/8 b - - 0 100");

      final ComparatorClassicalCheckmate comparator = new ComparatorClassicalCheckmate(WHITE, BLACK,
          board.getStaticPosition());

      final LegalMove firstLegalMove = calculateLegalMove(BLACK, F6, F7, BLACK_KING, PIECE_NONE, board);
      final LegalMove secondLegalMove = calculateLegalMove(BLACK, F6, E5, BLACK_KING, WHITE_BISHOP, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
    // KBN
    {
      final Board board = new Board("8/8/8/4k3/3NB3/8/3K4/8 b - - 0 100");

      final ComparatorClassicalCheckmate comparator = new ComparatorClassicalCheckmate(WHITE, BLACK,
          board.getStaticPosition());

      final LegalMove firstLegalMove = calculateLegalMove(BLACK, E5, D6, BLACK_KING, PIECE_NONE, board);
      final LegalMove secondLegalMove = calculateLegalMove(BLACK, E5, E4, BLACK_KING, WHITE_BISHOP, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testNotHavingMoveCaptureRightPieceAboveClassicalCheckmateMaterial() {
    // KQ
    {
      final Board board = new Board("1k6/8/1q3R2/8/8/4Q3/3K4/8 b - - 0 100");

      final ComparatorClassicalCheckmate comparator = new ComparatorClassicalCheckmate(WHITE, BLACK,
          board.getStaticPosition());

      final LegalMove firstLegalMove = calculateLegalMove(BLACK, B6, F6, BLACK_QUEEN, WHITE_ROOK, board);
      final LegalMove secondLegalMove = calculateLegalMove(BLACK, B6, E3, BLACK_QUEEN, WHITE_QUEEN, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
    // KR
    {
      final Board board = new Board("1k6/8/1q3P2/8/8/4R3/3K4/8 b - - 0 100");

      final ComparatorClassicalCheckmate comparator = new ComparatorClassicalCheckmate(WHITE, BLACK,
          board.getStaticPosition());

      final LegalMove firstLegalMove = calculateLegalMove(BLACK, B6, F6, BLACK_QUEEN, WHITE_PAWN, board);
      final LegalMove secondLegalMove = calculateLegalMove(BLACK, B6, E3, BLACK_QUEEN, WHITE_ROOK, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
    // KBB
    {
      final Board board = new Board("1k6/8/1q2B3/5B2/8/4B3/3K4/8 b - - 0 100");

      final ComparatorClassicalCheckmate comparator = new ComparatorClassicalCheckmate(WHITE, BLACK,
          board.getStaticPosition());

      final LegalMove firstLegalMove = calculateLegalMove(BLACK, B6, E6, BLACK_QUEEN, WHITE_BISHOP, board);
      final LegalMove secondLegalMove = calculateLegalMove(BLACK, B6, E3, BLACK_QUEEN, WHITE_BISHOP, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
    // KBN
    {
      final Board board = new Board("1k6/8/1q2P3/5B2/8/4N3/3K4/8 b - - 0 100");

      final ComparatorClassicalCheckmate comparator = new ComparatorClassicalCheckmate(WHITE, BLACK,
          board.getStaticPosition());

      final LegalMove firstLegalMove = calculateLegalMove(BLACK, B6, E6, BLACK_QUEEN, WHITE_PAWN, board);
      final LegalMove secondLegalMove = calculateLegalMove(BLACK, B6, E3, BLACK_QUEEN, WHITE_KNIGHT, board);

      assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testHavingMoveSacrificeThenCapture() {
    {
      final Board board = new Board("1k6/1r4P1/q3P3/5B2/8/4N3/3K4/8 b - - 0 100");

      final ComparatorClassicalCheckmate comparator = new ComparatorClassicalCheckmate(WHITE, BLACK,
          board.getStaticPosition());

      {
        final LegalMove firstLegalMove = calculateLegalMove(BLACK, B7, D7, BLACK_ROOK, PIECE_NONE, board);
        final LegalMove secondLegalMove = calculateLegalMove(BLACK, B7, C7, BLACK_ROOK, PIECE_NONE, board);

        assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
      }
      {
        final LegalMove firstLegalMove = calculateLegalMove(BLACK, B7, D7, BLACK_ROOK, PIECE_NONE, board);
        final LegalMove secondLegalMove = calculateLegalMove(BLACK, B7, G7, BLACK_ROOK, WHITE_PAWN, board);

        assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
      }
      {
        final LegalMove firstLegalMove = calculateLegalMove(BLACK, B7, G7, BLACK_ROOK, WHITE_PAWN, board);
        final LegalMove secondLegalMove = calculateLegalMove(BLACK, B7, C7, BLACK_ROOK, PIECE_NONE, board);

        assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
      }

      {
        final LegalMove firstLegalMove = calculateLegalMove(BLACK, A6, D3, BLACK_QUEEN, PIECE_NONE, board);
        final LegalMove secondLegalMove = calculateLegalMove(BLACK, A6, A2, BLACK_QUEEN, PIECE_NONE, board);

        assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
      }
      {
        final LegalMove firstLegalMove = calculateLegalMove(BLACK, A6, D3, BLACK_QUEEN, PIECE_NONE, board);
        final LegalMove secondLegalMove = calculateLegalMove(BLACK, A6, E6, BLACK_QUEEN, WHITE_PAWN, board);

        assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
      }
      {
        final LegalMove firstLegalMove = calculateLegalMove(BLACK, A6, E6, BLACK_QUEEN, WHITE_PAWN, board);
        final LegalMove secondLegalMove = calculateLegalMove(BLACK, A6, D6, BLACK_QUEEN, PIECE_NONE, board);

        assertEquals(-1, comparator.compare(firstLegalMove, secondLegalMove));
      }
    }

  }

  private static LegalMove calculateLegalMove(Side havingMove, Square fromSquare, Square toSquare, Piece movingPiece,
      Piece pieceCaptured, ApiBoard board) {
    final var moveSpecification = new MoveSpecification(havingMove, fromSquare, toSquare);
    final LegalMove legalMove = new LegalMove(moveSpecification, movingPiece, pieceCaptured);

    // check if specified correctly
    board.performMove(legalMove.moveSpecification());

    final LegalMove lastLegalMove = board.getLastMove();
    board.unperformMove();

    if (!legalMove.equals(lastLegalMove)) {
      throw new TestSetupException();
    }

    return legalMove;

  }
}
