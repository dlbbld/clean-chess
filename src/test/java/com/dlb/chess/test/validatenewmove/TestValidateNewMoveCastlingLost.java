package com.dlb.chess.test.validatenewmove;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.CastlingRightLoss;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.model.CastlingRightBoth;

class TestValidateNewMoveCastlingLost implements EnumConstants {

  // --- King moved ---

  @SuppressWarnings("static-method")
  @Test
  void testWhiteKingMoved() {
    final ApiBoard board = new Board();
    board.performMoves("e4", "e5", "Ke2");

    final CastlingRightBoth crb = board.getCastlingRightBoth();
    assertEquals(CastlingRight.NONE, crb.castlingRightWhite());
    assertEquals(CastlingRightLoss.KING_MOVED, crb.whiteKingSideLoss());
    assertEquals(CastlingRightLoss.KING_MOVED, crb.whiteQueenSideLoss());
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlackKingMoved() {
    final ApiBoard board = new Board();
    board.performMoves("e4", "e5", "Nf3", "Ke7");

    final CastlingRightBoth crb = board.getCastlingRightBoth();
    assertEquals(CastlingRight.NONE, crb.castlingRightBlack());
    assertEquals(CastlingRightLoss.KING_MOVED, crb.blackKingSideLoss());
    assertEquals(CastlingRightLoss.KING_MOVED, crb.blackQueenSideLoss());
  }

  // --- King-side rook moved ---

  @SuppressWarnings("static-method")
  @Test
  void testWhiteKingSideRookMoved() {
    final ApiBoard board = new Board();
    board.performMoves("h4", "e5", "Rh3");

    final CastlingRightBoth crb = board.getCastlingRightBoth();
    assertEquals(CastlingRight.QUEEN_SIDE, crb.castlingRightWhite());
    assertEquals(CastlingRightLoss.ROOK_MOVED, crb.whiteKingSideLoss());
    assertEquals(CastlingRightLoss.NONE, crb.whiteQueenSideLoss());
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlackKingSideRookMoved() {
    final ApiBoard board = new Board();
    board.performMoves("e4", "h5", "d4", "Rh6");

    final CastlingRightBoth crb = board.getCastlingRightBoth();
    assertEquals(CastlingRight.QUEEN_SIDE, crb.castlingRightBlack());
    assertEquals(CastlingRightLoss.ROOK_MOVED, crb.blackKingSideLoss());
    assertEquals(CastlingRightLoss.NONE, crb.blackQueenSideLoss());
  }

  // --- Queen-side rook moved ---

  @SuppressWarnings("static-method")
  @Test
  void testWhiteQueenSideRookMoved() {
    final ApiBoard board = new Board();
    board.performMoves("a4", "e5", "Ra3");

    final CastlingRightBoth crb = board.getCastlingRightBoth();
    assertEquals(CastlingRight.KING_SIDE, crb.castlingRightWhite());
    assertEquals(CastlingRightLoss.NONE, crb.whiteKingSideLoss());
    assertEquals(CastlingRightLoss.ROOK_MOVED, crb.whiteQueenSideLoss());
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlackQueenSideRookMoved() {
    final ApiBoard board = new Board();
    board.performMoves("e4", "a5", "d4", "Ra6");

    final CastlingRightBoth crb = board.getCastlingRightBoth();
    assertEquals(CastlingRight.KING_SIDE, crb.castlingRightBlack());
    assertEquals(CastlingRightLoss.NONE, crb.blackKingSideLoss());
    assertEquals(CastlingRightLoss.ROOK_MOVED, crb.blackQueenSideLoss());
  }

  // --- Castled king-side ---

  @SuppressWarnings("static-method")
  @Test
  void testWhiteCastledKingSide() {
    final ApiBoard board = new Board();
    board.performMoves("e4", "e5", "Nf3", "Nc6", "Bc4", "Bc5", "O-O");

    final CastlingRightBoth crb = board.getCastlingRightBoth();
    assertEquals(CastlingRight.NONE, crb.castlingRightWhite());
    assertEquals(CastlingRightLoss.CASTLED, crb.whiteKingSideLoss());
    assertEquals(CastlingRightLoss.CASTLED, crb.whiteQueenSideLoss());
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlackCastledKingSide() {
    final ApiBoard board = new Board();
    board.performMoves("e4", "e5", "Nf3", "Nf6", "Nc3", "Bc5", "Bc4", "O-O");

    final CastlingRightBoth crb = board.getCastlingRightBoth();
    assertEquals(CastlingRight.NONE, crb.castlingRightBlack());
    assertEquals(CastlingRightLoss.CASTLED, crb.blackKingSideLoss());
    assertEquals(CastlingRightLoss.CASTLED, crb.blackQueenSideLoss());
  }

  // --- Castled queen-side ---

  @SuppressWarnings("static-method")
  @Test
  void testWhiteCastledQueenSide() {
    final ApiBoard board = new Board();
    board.performMoves("d4", "d5", "Nc3", "Nc6", "Bf4", "Bf5", "Qd2", "Qd7", "O-O-O");

    final CastlingRightBoth crb = board.getCastlingRightBoth();
    assertEquals(CastlingRight.NONE, crb.castlingRightWhite());
    assertEquals(CastlingRightLoss.CASTLED, crb.whiteKingSideLoss());
    assertEquals(CastlingRightLoss.CASTLED, crb.whiteQueenSideLoss());
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlackCastledQueenSide() {
    final ApiBoard board = new Board();
    board.performMoves("e4", "d5", "d4", "Nc6", "Nf3", "Bf5", "Nc3", "Qd7", "Bc4", "O-O-O");

    final CastlingRightBoth crb = board.getCastlingRightBoth();
    assertEquals(CastlingRight.NONE, crb.castlingRightBlack());
    assertEquals(CastlingRightLoss.CASTLED, crb.blackKingSideLoss());
    assertEquals(CastlingRightLoss.CASTLED, crb.blackQueenSideLoss());
  }

  // --- Unknown FEN import ---

  @SuppressWarnings("static-method")
  @Test
  void testFenImportNoRights() {
    // FEN with no castling rights at all
    final ApiBoard board = new Board("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w - - 0 1");

    final CastlingRightBoth crb = board.getCastlingRightBoth();
    assertEquals(CastlingRight.NONE, crb.castlingRightWhite());
    assertEquals(CastlingRight.NONE, crb.castlingRightBlack());
    assertEquals(CastlingRightLoss.UNKNOWN_FEN_IMPORT, crb.whiteKingSideLoss());
    assertEquals(CastlingRightLoss.UNKNOWN_FEN_IMPORT, crb.whiteQueenSideLoss());
    assertEquals(CastlingRightLoss.UNKNOWN_FEN_IMPORT, crb.blackKingSideLoss());
    assertEquals(CastlingRightLoss.UNKNOWN_FEN_IMPORT, crb.blackQueenSideLoss());
  }

  @SuppressWarnings("static-method")
  @Test
  void testFenImportPartialRights() {
    // FEN with only white king-side and black queen-side castling rights
    final ApiBoard board = new Board("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w Kq - 0 1");

    final CastlingRightBoth crb = board.getCastlingRightBoth();
    assertEquals(CastlingRight.KING_SIDE, crb.castlingRightWhite());
    assertEquals(CastlingRight.QUEEN_SIDE, crb.castlingRightBlack());
    assertEquals(CastlingRightLoss.NONE, crb.whiteKingSideLoss());
    assertEquals(CastlingRightLoss.UNKNOWN_FEN_IMPORT, crb.whiteQueenSideLoss());
    assertEquals(CastlingRightLoss.UNKNOWN_FEN_IMPORT, crb.blackKingSideLoss());
    assertEquals(CastlingRightLoss.NONE, crb.blackQueenSideLoss());
  }

  @SuppressWarnings("static-method")
  @Test
  void testFenImportAllRights() {
    // FEN with all castling rights — no losses
    final ApiBoard board = new Board("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1");

    final CastlingRightBoth crb = board.getCastlingRightBoth();
    assertEquals(CastlingRight.KING_AND_QUEEN_SIDE, crb.castlingRightWhite());
    assertEquals(CastlingRight.KING_AND_QUEEN_SIDE, crb.castlingRightBlack());
    assertEquals(CastlingRightLoss.NONE, crb.whiteKingSideLoss());
    assertEquals(CastlingRightLoss.NONE, crb.whiteQueenSideLoss());
    assertEquals(CastlingRightLoss.NONE, crb.blackKingSideLoss());
    assertEquals(CastlingRightLoss.NONE, crb.blackQueenSideLoss());
  }

  // --- Rook captured (opponent captures the rook on its original square, king never moved) ---

  @SuppressWarnings("static-method")
  @Test
  void testBlackKingSideRookCapturedByWhite() {
    // White bishop goes Bb2-xg7-xh8, capturing black's h8 rook. Black king never moves.
    final ApiBoard board = new Board();
    board.performMoves("b3", "a5", "Bb2", "a4", "Bxg7", "a3", "Bxh8");

    final CastlingRightBoth crb = board.getCastlingRightBoth();
    assertEquals(CastlingRight.QUEEN_SIDE, crb.castlingRightBlack());
    assertEquals(CastlingRightLoss.ROOK_CAPTURED, crb.blackKingSideLoss());
    assertEquals(CastlingRightLoss.NONE, crb.blackQueenSideLoss());
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlackQueenSideRookCapturedByWhite() {
    // White pawn marches a-file and promotes on a8 capturing the rook.
    final ApiBoard board = new Board();
    board.performMoves("a4", "b5", "axb5", "c5", "b6", "c4", "b7", "c3", "bxa8=Q");

    final CastlingRightBoth crb = board.getCastlingRightBoth();
    assertEquals(CastlingRight.KING_SIDE, crb.castlingRightBlack());
    assertEquals(CastlingRightLoss.NONE, crb.blackKingSideLoss());
    assertEquals(CastlingRightLoss.ROOK_CAPTURED, crb.blackQueenSideLoss());
  }

  @SuppressWarnings("static-method")
  @Test
  void testWhiteKingSideRookCapturedByBlack() {
    // Mirror of black king-side test: black bishop goes Bb7-xg2-xh1.
    final ApiBoard board = new Board();
    board.performMoves("a3", "b6", "a4", "Bb7", "a5", "Bxg2", "a6", "Bxh1");

    final CastlingRightBoth crb = board.getCastlingRightBoth();
    assertEquals(CastlingRight.QUEEN_SIDE, crb.castlingRightWhite());
    assertEquals(CastlingRightLoss.ROOK_CAPTURED, crb.whiteKingSideLoss());
    assertEquals(CastlingRightLoss.NONE, crb.whiteQueenSideLoss());
  }

  @SuppressWarnings("static-method")
  @Test
  void testWhiteQueenSideRookCapturedByBlack() {
    // Mirror of black queen-side test: black pawn marches b-file and promotes on b1 capturing a1 is complex.
    // Instead: black bishop goes via Bb4-xa3... no. Simpler: use Bc3-xa1 path.
    // Black bishop goes Bf8-b4-a3...no. Let me use: black queen captures a1 rook.
    final ApiBoard board = new Board();
    board.performMoves("h3", "a5", "h4", "a4", "h5", "a3", "h6", "axb2", "hxg7", "bxa1=Q");

    final CastlingRightBoth crb = board.getCastlingRightBoth();
    assertEquals(CastlingRight.KING_SIDE, crb.castlingRightWhite());
    assertEquals(CastlingRightLoss.NONE, crb.whiteKingSideLoss());
    assertEquals(CastlingRightLoss.ROOK_CAPTURED, crb.whiteQueenSideLoss());
  }

}
