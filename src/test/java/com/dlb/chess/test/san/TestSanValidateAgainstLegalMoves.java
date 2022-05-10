package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.SanValidation;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;

class TestSanValidateAgainstLegalMoves {

  @SuppressWarnings("static-method")
  @Test
  void testKingCastling() {
    final ApiBoard board = new Board();

    // whitecheck(board, "O-O-O",
    // SanValidationPositionRelatedProblem.KING_CASTLING_QUEEN_SIDE_NOT_POSSIBLE);check(board, "O-O",
    // SanValidationPositionRelatedProblem.KING_CASTLING_KING_SIDE_NOT_POSSIBLE);

    // black
    board.performMoves("e4");
    checkException(board, "O-O-O", SanValidationProblem.KING_CASTLING_QUEEN_SIDE_NOT_POSSIBLE);
    checkException(board, "O-O", SanValidationProblem.KING_CASTLING_KING_SIDE_NOT_POSSIBLE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testKingNonCastling() {
    final ApiBoard board = new Board();

    // whitecheck(board, "Ke2", SanValidationPositionRelatedProblem.KING_NON_CASTLING_NO_LEGAL_MOVE);check(board,
    // "Kxa8", SanValidationPositionRelatedProblem.KING_NON_CASTLING_NO_LEGAL_MOVE);

    // black
    board.performMoves("e4");
    checkException(board, "Ke2", SanValidationProblem.KING_NON_CASTLING_NO_LEGAL_MOVE);
    checkException(board, "Kxa1", SanValidationProblem.KING_NON_CASTLING_NO_LEGAL_MOVE);

  }

  @SuppressWarnings("static-method")
  @Test
  void testPawnNonPromotion() {
    final ApiBoard board = new Board();

    // whitecheck(board, "e5", SanValidationPositionRelatedProblem.PAWN_NON_PROMOTION_NO_LEGAL_MOVE);check(board,
    // "exd7", SanValidationPositionRelatedProblem.PAWN_NON_PROMOTION_NO_LEGAL_MOVE);

    // black
    board.performMoves("e4");
    checkException(board, "d4", SanValidationProblem.PAWN_NON_PROMOTION_NO_LEGAL_MOVE);
    checkException(board, "exd2", SanValidationProblem.PAWN_NON_PROMOTION_NO_LEGAL_MOVE);

  }

  @SuppressWarnings("static-method")
  @Test
  void testPawnPromotion() {
    // black
    {
      final ApiBoard board = new Board("8/8/8/5k2/5p2/8/2P5/1K6 b - - 0 100");
      checkException(board, "f1=Q", SanValidationProblem.PAWN_PROMOTION_NO_LEGAL_MOVE);
    }
    {
      final ApiBoard board = new Board("8/8/8/5k2/5p2/8/2P5/1K3N2 b - - 0 100");
      checkException(board, "exf1=Q", SanValidationProblem.PAWN_PROMOTION_NO_LEGAL_MOVE);
    }

  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceNeither1NoLegalMove() {

    final ApiBoard board = new Board();

    // white
    checkException(board, "Ra3", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);
    checkException(board, "Nb4", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);
    checkException(board, "Be3", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);
    checkException(board, "Qd5", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);
    checkException(board, "Rxa8", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);
    checkException(board, "Nxb8", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);
    checkException(board, "Bxc8", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);
    checkException(board, "Qxd8", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);

    // black
    board.performMoves("e4");
    checkException(board, "Ra6", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);
    checkException(board, "Ne5", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);
    checkException(board, "Bg4", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);
    checkException(board, "Qd6", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);
    checkException(board, "Rxa2", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);
    checkException(board, "Nxb2", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);
    checkException(board, "Bxc2", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);
    checkException(board, "Qxd2", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);

  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceNeither2MultipleLegalMoves() {

    final ApiBoard board = new Board();

    // white
    // white rook
    board.performMoves("a4", "a5", "h4", "h5", "Ra3", "Ra6");
    checkException(board, "Rh3", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("Rah3");
    // black rook
    checkException(board, "Rh6", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("Rah6");

    // white knight
    board.performMoves("Nc3", "Nc6", "Nf3", "Nf6", "Nd4", "Nd5");
    checkException(board, "Nb5", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("Ncb5");
    // black knight
    checkException(board, "Nb4", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("Ncb4");

    // white bishop
    board.performMoves("f4", "c5", "f5", "c4", "f6", "c3", "fxg7", "cxb2", "gxh8=B", "e6", "d3", "Bd6", "Bd2", "Ke7",
        "Qa1", "bxa1=B", "Bxh6", "Bxd4");
    checkException(board, "Bg7", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("B6g7");
    // black bishop
    checkException(board, "Be5", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("B4e5");

    // white queen
    board.performMoves("c4", "f5", "cxd5", "f4", "dxe6", "f3", "exd7", "fxg2", "dxc8=Q", "gxh1=Q", "d4", "Qe4", "dxe5",
        "Kf7", "exd6");
    checkException(board, "Qxh4+", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("Qdxh4+");
    board.performMoves("Kd1", "Na2", "d7", "Nb4", "d8=Q", "Nc2", "Na7", "Kg6", "Nb5", "Kh7", "Qdc7");
    // black queen
    checkException(board, "Qf4", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("Qhf4");
    // white queen
    checkException(board, "Qd7", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("Q8d7");
    board.performMoves("Qfe3", "Qf7");
    // black queen
    checkException(board, "Qf4", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("Q4f4");
  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceFile1NoLegalMove() {
    final ApiBoard board = new Board();

    // white
    // rook
    checkException(board, "Raa3", SanValidationProblem.PIECE_FILE_NO_LEGAL_MOVE);

    // knight
    checkException(board, "Nac3", SanValidationProblem.PIECE_FILE_NO_LEGAL_MOVE);

    // bishop
    checkException(board, "Bcb3", SanValidationProblem.PIECE_FILE_NO_LEGAL_MOVE);

    // queen
    checkException(board, "Qdd3", SanValidationProblem.PIECE_FILE_NO_LEGAL_MOVE);

    // black
    board.performMoves("e4");
    // rook
    checkException(board, "Raa6", SanValidationProblem.PIECE_FILE_NO_LEGAL_MOVE);

    // knight
    checkException(board, "Nac6", SanValidationProblem.PIECE_FILE_NO_LEGAL_MOVE);

    // bishop
    checkException(board, "Bcb6", SanValidationProblem.PIECE_FILE_NO_LEGAL_MOVE);

    // queen
    checkException(board, "Qdd4", SanValidationProblem.PIECE_FILE_NO_LEGAL_MOVE);

  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceFile2OnlyOneLegalMove() {
    final ApiBoard board = new Board();

    // white
    // rook
    board.performMoves("a4");
    board.performMoves("a5");
    checkException(board, "Raa2", SanValidationProblem.PIECE_FILE_ONLY_ONE_LEGAL_MOVE);

    // knight
    checkException(board, "Nbc3", SanValidationProblem.PIECE_FILE_ONLY_ONE_LEGAL_MOVE);

    // bishop
    board.performMoves("b3");
    board.performMoves("b6");
    checkException(board, "Bcb2", SanValidationProblem.PIECE_FILE_ONLY_ONE_LEGAL_MOVE);

    // queen
    board.performMoves("d4");
    board.performMoves("d5");
    checkException(board, "Qdd2", SanValidationProblem.PIECE_FILE_ONLY_ONE_LEGAL_MOVE);

    // black
    board.performMoves("e4");
    // rook
    checkException(board, "Raa7", SanValidationProblem.PIECE_FILE_ONLY_ONE_LEGAL_MOVE);

    // knight
    checkException(board, "Nbc6", SanValidationProblem.PIECE_FILE_ONLY_ONE_LEGAL_MOVE);

    // bishop
    checkException(board, "Bcb7", SanValidationProblem.PIECE_FILE_ONLY_ONE_LEGAL_MOVE);

    // queen
    checkException(board, "Qdd7", SanValidationProblem.PIECE_FILE_ONLY_ONE_LEGAL_MOVE);

  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceFile3MustUseRank() {
    {
      final ApiBoard board = new Board();

      board.performMoves("a4", "a5", "h4", "h5", "Ra3", "Ra6", "Rhh3", "Rhh6", "Rad3", "Rac6", "Rd4", "Rc5", "Rhd3",
          "Rhc6", "Rd5", "Rc4");
      // two white rooks

      checkException(board, "Rdd4", SanValidationProblem.PIECE_FILE_MUST_USE_RANK);

      board.performMove("R5d4");
      // two black rooks

      checkException(board, "Rcc5", SanValidationProblem.PIECE_FILE_MUST_USE_RANK);

      board.performMove("R4c5");

      board.performMoves("Nf3", "Na6", "Nc3", "Rb6", "Ne4", "Nf6", "Ng3", "Nd5", "Rc4", "c6", "Nf5", "Ndc7", "N5d4",
          "Na8", "Nf5", "N8c7");
      // two white knights

      checkException(board, "Nfd4", SanValidationProblem.PIECE_FILE_MUST_USE_RANK);

      board.performMove("N5d4");
      // two black knights
      board.performMoves("Na8", "Nf5");

      checkException(board, "Nac7", SanValidationProblem.PIECE_FILE_MUST_USE_RANK);

      board.performMove("N8c7");

      board.performMoves("g4", "Rb4", "gxh5", "Rxa4", "Bg2", "g6", "hxg6", "Rb4", "g7", "a4", "g8=B", "a3", "Bh7", "a2",
          "Ne3", "a1=B", "Bf5", "Bxb2", "Bg4", "Bf6");
      // two white bishops

      checkException(board, "Bgh3", SanValidationProblem.PIECE_FILE_MUST_USE_RANK);

      board.performMove("B4h3");
      // two black bishops

      checkException(board, "Bfg7", SanValidationProblem.PIECE_FILE_MUST_USE_RANK);

      board.performMove("B6g7");

      board.performMoves("h5", "b5", "h6", "Ra4", "h7", "b4", "h8=Q", "b3", "Rdd4", "bxc2", "Qh7", "Bb7", "Rb4", "Qb8",
          "Rb1", "cxb1=Q", "Qd3", "Qb6");
      // two white queens

      checkException(board, "Qdc2", SanValidationProblem.PIECE_FILE_MUST_USE_RANK);

      board.performMove("Q3c2");
      // two black queens
      board.performMoves("Ne6", "Qd3");

      checkException(board, "Qbc7", SanValidationProblem.PIECE_FILE_MUST_USE_RANK);

      board.performMove("Q6c7");
    }

    {
      final ApiBoard board = new Board();

      board.performMoves("e4", "e5", "Bc4", "Bc5", "Nf3", "Nf6", "O-O", "O-O", "Nxe5", "Nxe4", "Qg4", "Qg5", "Na3",
          "Na6", "d4", "d5", "Be3", "Be6", "Bb5", "Bb4", "c4", "c5", "cxd5", "cxd4", "d6", "d3", "d7", "d2", "d8=R",
          "d1=R", "Rd3", "Rc1", "Rfd1", "Rc6", "Rac1", "Rac8", "R1d2", "Rfd8", "Rdc3", "Rd7", "R1c2", "h5", "h4",
          "hxg4", "hxg5", "g6", "g3", "f6", "f3", "gxf3", "gxf6", "f2+", "Kh2", "f1=R", "f7+", "Kh7", "f8=R", "Rff7",
          "Rdd3", "Rfe7", "Rff2", "Rdd6", "Rc1", "b6", "R3c2", "Rdd7", "Rcd1", "Rb7");

      // three white rooks

      checkException(board, "Rdd2", SanValidationProblem.PIECE_FILE_MUST_USE_RANK);

      board.performMove("R1d2");
      // three black rooks

      checkException(board, "Rcc7", SanValidationProblem.PIECE_FILE_MUST_USE_RANK);

      board.performMove("R8c7");
    }

  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceFile4MustUseRankOrSquare() {
    {
      final ApiBoard board = new Board();

      board.performMoves("c4", "f5", "c5", "f4", "c6", "f3", "cxb7", "fxg2", "bxa8=N", "gxh1=N", "Nc3", "Nf6", "Nf3",
          "Nc6", "Nb6", "Ng3", "Nba4", "Ngh5", "Nc5", "Nf4", "Ng5", "Nb4", "Nge4", "Nbd5", "Ng5", "Nb4");

      // three white knights

      checkException(board, "Nce4", SanValidationProblem.PIECE_FILE_MUST_USE_RANK_OR_SQUARE);

      board.performMove("N3e4");
      // three black knights

      checkException(board, "Nfd5", SanValidationProblem.PIECE_FILE_MUST_USE_RANK_OR_SQUARE);

      board.performMove("N6d5");
    }

    {
      final ApiBoard board = new Board();

      board.performMoves("c4", "f5", "c5", "f4", "c6", "f3", "cxb7", "fxg2", "bxa8=N", "gxh1=N", "Nc3", "Nf6", "Nf3",
          "Nc6", "Nb6", "Ng3", "Nba4", "Ngh5", "Nc5", "Nf4", "Ng5", "Nb4", "Nge4", "Nbd5", "Ng5", "Nb4", "a4", "h5",
          "a5", "h4", "a6", "h3", "Bg2", "hxg2", "Rb1", "g1=N", "Ra1", "Bb7", "axb7", "Ngh3", "b8=N", "Nxf2", "Nc6",
          "N2e4", "Ne5", "Nd6", "Ned3", "Nc8", "Nf2", "Nb6", "Nh1", "Qc8", "Ng3", "Qb8");
      // four white knights

      checkException(board, "Nce4", SanValidationProblem.PIECE_FILE_MUST_USE_RANK_OR_SQUARE);

      board.performMove("Nc3e4");
      // four black knights

      checkException(board, "Nbd5", SanValidationProblem.PIECE_FILE_MUST_USE_RANK_OR_SQUARE);

      board.performMove("Nb6d5");
    }

    {
      final ApiBoard board = new Board();

      board.performMoves("c4", "f5", "c5", "f4", "c6", "f3", "cxb7", "fxg2", "bxa8=B", "gxh1=B", "e4", "d5", "e5", "d4",
          "e6", "d3", "Qc2", "Nh6", "Bh3", "dxc2", "Bg4", "cxb1=B", "Bgf3", "Nf7", "exf7+", "Kd7", "Be2", "Rg8",
          "fxg8=B", "Bg6", "Bgd5", "Bg2", "Bh5", "Bh3", "Bb3", "Be6", "Bad5", "Be4", "Bbd1", "Ba6", "Bde2", "Bed3",
          "Be4", "Bdc4", "Bf5", "Bxa2", "Bef3", "Qe8");
      // three white bishops

      checkException(board, "Bfg4", SanValidationProblem.PIECE_FILE_MUST_USE_RANK_OR_SQUARE);

      board.performMove("Bf5g4");
      board.performMoves("Kd8", "Kd1"); // unpin black bishop
      // three black bishops

      checkException(board, "Bac4", SanValidationProblem.PIECE_FILE_MUST_USE_RANK_OR_SQUARE);

      board.performMove("Ba6c4");

      board.performMoves("b4", "g5", "Bh3", "g4", "b5", "g3", "b6", "g2", "b7", "Nc6", "Ne2", "Nb4", "Ng3", "Nd5",
          "Nf1", "gxf1=B", "Bfg4", "Nb6", "Bf5", "Na8", "bxa8=B", "Bb5", "Baf3", "Bc6", "Ke1", "Bd3", "Kd1", "Bac4",
          "Ke1", "Bde4");
      // four white bishops

      checkException(board, "Bfg4", SanValidationProblem.PIECE_FILE_MUST_USE_RANK_OR_SQUARE);

      board.performMove("Bf3g4");
      // four black bishops

      checkException(board, "Bed5", SanValidationProblem.PIECE_FILE_MUST_USE_RANK_OR_SQUARE);

      board.performMove("Be4d5");
    }

    {
      final ApiBoard board = new Board();

      board.performMoves("f4", "c5", "f5", "c4", "f6", "c3", "fxg7", "cxb2", "gxh8=Q", "bxa1=Q", "c4", "f5", "c5", "f4",
          "c6", "f3", "cxb7", "fxg2", "bxa8=Q", "gxh1=Q", "Qb3", "Qf3", "Qab7", "Qe5", "Qf6", "Qeh5", "Qd6", "e6",
          "Q3b4", "Qdf6", "Q7b6", "Q6f5");
      // three white queens

      checkException(board, "Qbc5", SanValidationProblem.PIECE_FILE_MUST_USE_RANK_OR_SQUARE);

      board.performMove("Qb6c5");
      // three black queens

      checkException(board, "Qfg4", SanValidationProblem.PIECE_FILE_MUST_USE_RANK_OR_SQUARE);

      board.performMove("Qf5g4");

      board.performMoves("a4", "Qxh2", "Qxa7", "h5", "a5", "h4", "a6", "Qhg3+", "Kd1", "h3", "Qab6", "h2", "a7", "h1=Q",
          "a8=Q", "Qgf5", "Qad5", "Qhh5", "Qdd4", "Qgh3");
      // four white queens

      checkException(board, "Qbc5", SanValidationProblem.PIECE_FILE_MUST_USE_RANK_OR_SQUARE);

      board.performMove("Qb4c5");
      // four black queens

      checkException(board, "Qfg4", SanValidationProblem.PIECE_FILE_MUST_USE_RANK_OR_SQUARE);

      board.performMove("Qf3g4");

      board.performMoves("Qcb4", "Qgf3", "d3", "Be7", "Kc2", "Bf8", "Kb2", "Be7", "Ka1", "Qxe2", "Qa7", "Kf8");
      // four white queens

      checkException(board, "Qdc5", SanValidationProblem.PIECE_FILE_MUST_USE_RANK_OR_SQUARE);

      board.performMove("Qd4c5");
      // four black queens

      checkException(board, "Qhg4", SanValidationProblem.PIECE_FILE_MUST_USE_RANK_OR_SQUARE);

      board.performMove("Qh5g4");
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceRank1NoLegalMove() {
    final ApiBoard board = new Board();

    // white
    // rook
    checkException(board, "R2a4", SanValidationProblem.PIECE_RANK_NO_LEGAL_MOVE);

    // knight
    checkException(board, "N1b3", SanValidationProblem.PIECE_RANK_NO_LEGAL_MOVE);

    // bishop
    checkException(board, "B1d4", SanValidationProblem.PIECE_RANK_NO_LEGAL_MOVE);

    // queen
    checkException(board, "Q2d3", SanValidationProblem.PIECE_RANK_NO_LEGAL_MOVE);

    // black
    board.performMoves("e4");
    // rook
    checkException(board, "R7a5", SanValidationProblem.PIECE_RANK_NO_LEGAL_MOVE);

    // knight
    checkException(board, "N5c6", SanValidationProblem.PIECE_RANK_NO_LEGAL_MOVE);

    // bishop
    checkException(board, "B3d5", SanValidationProblem.PIECE_RANK_NO_LEGAL_MOVE);

    // queen
    checkException(board, "Q1d6", SanValidationProblem.PIECE_RANK_NO_LEGAL_MOVE);

  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceRank2OnlyOneLegalMove() {
    final ApiBoard board = new Board();

    // white
    // rook
    board.performMoves("a4");
    board.performMoves("a5");
    checkException(board, "R1a2", SanValidationProblem.PIECE_RANK_ONLY_ONE_LEGAL_MOVE);

    // knight
    checkException(board, "N1c3", SanValidationProblem.PIECE_RANK_ONLY_ONE_LEGAL_MOVE);

    // bishop
    board.performMoves("b3");
    board.performMoves("b6");
    checkException(board, "B1b2", SanValidationProblem.PIECE_RANK_ONLY_ONE_LEGAL_MOVE);

    // queen
    board.performMoves("d4");
    board.performMoves("d5");
    checkException(board, "Q1d2", SanValidationProblem.PIECE_RANK_ONLY_ONE_LEGAL_MOVE);

    // black
    board.performMoves("e4");
    // rook
    checkException(board, "R8a7", SanValidationProblem.PIECE_RANK_ONLY_ONE_LEGAL_MOVE);

    // knight
    checkException(board, "N8c6", SanValidationProblem.PIECE_RANK_ONLY_ONE_LEGAL_MOVE);

    // bishop
    checkException(board, "B8b7", SanValidationProblem.PIECE_RANK_ONLY_ONE_LEGAL_MOVE);

    // queen
    checkException(board, "Q8d7", SanValidationProblem.PIECE_RANK_ONLY_ONE_LEGAL_MOVE);

  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceRank3MustUseFile() {
    {
      final ApiBoard board = new Board();

      board.performMoves("a4", "a5", "h4", "h5", "Ra3", "Ra6", "Rhh3", "Rhh6", "Rab3", "Rad6", "Rhd3", "Rd5", "Rd4",
          "Rg6");
      // two white rooks

      checkException(board, "R4b4", SanValidationProblem.PIECE_RANK_MUST_USE_FILE);

      board.performMove("Rdb4");
      // two black rooks

      checkException(board, "R5g5", SanValidationProblem.PIECE_RANK_MUST_USE_FILE);

      board.performMove("Rdg5");

      board.performMoves("Nf3", "Nf6", "Nc3", "Nc6", "Nd4", "Nd5");
      // two white knights

      checkException(board, "N3b5", SanValidationProblem.PIECE_RANK_MUST_USE_FILE);

      board.performMove("Ncb5");
      // two black knights

      checkException(board, "N5xb4", SanValidationProblem.PIECE_RANK_MUST_USE_FILE);

      board.performMove("Ndxb4");

      board.performMoves("e4", "f5", "e5", "d6", "exd6", "f4", "dxc7", "f3", "d3", "fxg2", "Bd2", "g1=B", "Bc1", "Nb8",
          "cxb8=B", "Bxf2+", "Kd2", "e6", "Bf4", "Bxd4", "Ke1", "Rh6", "Bxg5", "g6", "Bxh6", "Bfg7");

      // two white bishops

      checkException(board, "B6f4", SanValidationProblem.PIECE_RANK_MUST_USE_FILE);

      board.performMove("Bhf4");
      // two black bishops

      checkException(board, "B4e5", SanValidationProblem.PIECE_RANK_MUST_USE_FILE);

      board.performMove("Bde5");

      board.performMoves("c4", "g5", "c5", "g4", "c6", "g3", "cxb7", "g2", "bxc8=Q", "gxf1=Q+", "Kd2", "Kf7", "Qxh5+",
          "Kf6", "Qxe5+", "Kg6", "Qd4", "Qf6", "Qdc4", "Qf2+", "Kd1", "Na2", "Na3", "Nb4", "Q4a6", "Q2d4");

      // two white queens

      checkException(board, "Q6c6", SanValidationProblem.PIECE_RANK_MUST_USE_FILE);

      board.performMove("Qac6");
      // two black queens

      checkException(board, "Q4xf4", SanValidationProblem.PIECE_RANK_MUST_USE_FILE);

      board.performMove("Qdxf4");

    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceRank4MustUseFileOrSquare() {
    {
      final ApiBoard board = new Board();

      board.performMoves("c4", "f5", "c5", "f4", "c6", "f3", "cxb7", "fxg2", "bxa8=N", "gxh1=N", "Nc3", "Nf6", "Nf3",
          "Nc6", "Nb6", "Ng3", "Nba4", "Ngh5", "Nc5", "Nf4", "Ng5", "Nb4", "Nge4", "Nbd5", "Ng5", "Nb4");

      // three white knights

      checkException(board, "N5e4", SanValidationProblem.PIECE_RANK_MUST_USE_FILE_OR_SQUARE);

      board.performMove("Nc5e4");
      // three black knights

      checkException(board, "N4d5", SanValidationProblem.PIECE_RANK_MUST_USE_FILE_OR_SQUARE);

      board.performMove("Nf4d5");
    }

    {
      final ApiBoard board = new Board();

      board.performMoves("c4", "f5", "c5", "f4", "c6", "f3", "cxb7", "fxg2", "bxa8=N", "gxh1=N", "Nc3", "Nf6", "Nf3",
          "Nc6", "Nb6", "Ng3", "Nba4", "Ngh5", "Nc5", "Nf4", "Ng5", "Nb4", "Nge4", "Nbd5", "Ng5", "Nb4", "a4", "h5",
          "a5", "h4", "a6", "h3", "Bg2", "hxg2", "Rb1", "g1=N", "Ra1", "Bb7", "axb7", "Ngh3", "b8=N", "Nxf2", "Nc6",
          "N2e4", "Ne5", "Nd6", "Ned3", "Nc8", "Nf2", "Nb6", "Nh1", "Qc8", "Ng3", "Qb8");
      // four white knights

      checkException(board, "N3e4", SanValidationProblem.PIECE_RANK_MUST_USE_FILE_OR_SQUARE);

      board.performMove("Nc3e4");
      // four black knights

      checkException(board, "N6d5", SanValidationProblem.PIECE_RANK_MUST_USE_FILE_OR_SQUARE);

      board.performMove("Nb6d5");
    }

    {
      final ApiBoard board = new Board();

      board.performMoves("c4", "f5", "c5", "f4", "c6", "f3", "cxb7", "fxg2", "bxa8=B", "gxh1=B", "e4", "d5", "e5", "d4",
          "e6", "d3", "Qc2", "Nh6", "Bh3", "dxc2", "Bg4", "cxb1=B", "Bgf3", "Nf7", "exf7+", "Kd7", "Be2", "Rg8",
          "fxg8=B", "Bg6", "Bgd5", "Bg2", "Bh5", "Bh3", "Bb3", "Be6", "Bad5", "Be4", "Bbd1", "Ba6", "Bde2", "Bed3",
          "Be4", "Bdc4", "Bf5", "Bxa2", "Bef3", "Qe8");
      // three white bishops

      checkException(board, "B5g4", SanValidationProblem.PIECE_RANK_MUST_USE_FILE_OR_SQUARE);

      board.performMove("Bf5g4");
      board.performMoves("Kd8", "Kd1"); // unpin black bishop
      // three black bishops

      checkException(board, "B6c4", SanValidationProblem.PIECE_RANK_MUST_USE_FILE_OR_SQUARE);

      board.performMove("Ba6c4");

      board.performMoves("b4", "g5", "Bh3", "g4", "b5", "g3", "b6", "g2", "b7", "Nc6", "Ne2", "Nb4", "Ng3", "Nd5",
          "Nf1", "gxf1=B", "Bfg4", "Nb6", "Bf5", "Na8", "bxa8=B", "Bb5", "Baf3", "Bc6", "Ke1", "Bd3", "Kd1", "Bac4",
          "Ke1", "Bde4");
      // four white bishops

      checkException(board, "B3g4", SanValidationProblem.PIECE_RANK_MUST_USE_FILE_OR_SQUARE);

      board.performMove("Bf3g4");
      // four black bishops

      checkException(board, "B4d5", SanValidationProblem.PIECE_RANK_MUST_USE_FILE_OR_SQUARE);

      board.performMove("Be4d5");
    }

    {
      final ApiBoard board = new Board();

      board.performMoves("f4", "c5", "f5", "c4", "f6", "c3", "fxg7", "cxb2", "gxh8=Q", "bxa1=Q", "c4", "f5", "c5", "f4",
          "c6", "f3", "cxb7", "fxg2", "bxa8=Q", "gxh1=Q", "Qb3", "Qf3", "Qab7", "Qe5", "Qf6", "Qeh5", "Qd6", "e6",
          "Q3b4", "Qdf6", "Q7b6", "Q6f5");
      // three white queens

      checkException(board, "Q6c5", SanValidationProblem.PIECE_RANK_MUST_USE_FILE_OR_SQUARE);

      board.performMove("Qb6c5");
      // three black queens

      checkException(board, "Q5g4", SanValidationProblem.PIECE_RANK_MUST_USE_FILE_OR_SQUARE);

      board.performMove("Qf5g4");

      board.performMoves("a4", "Qxh2", "Qxa7", "h5", "a5", "h4", "a6", "Qhg3+", "Kd1", "h3", "Qab6", "h2", "a7", "h1=Q",
          "a8=Q", "Qgf5", "Qad5", "Qhh5", "Qdd4", "Qgh3");
      // four white queens

      checkException(board, "Q4c5", SanValidationProblem.PIECE_RANK_MUST_USE_FILE_OR_SQUARE);

      board.performMove("Qb4c5");
      // four black queens

      checkException(board, "Q3g4", SanValidationProblem.PIECE_RANK_MUST_USE_FILE_OR_SQUARE);

      board.performMove("Qf3g4");

      board.performMoves("Qcb4", "Qgf3", "d3", "Be7", "Kc2", "Bf8", "Kb2", "Be7", "Ka1", "Qxe2", "Qa7", "Kf8");
      // four white queens

      checkException(board, "Q4c5", SanValidationProblem.PIECE_RANK_MUST_USE_FILE_OR_SQUARE);

      board.performMove("Qd4c5");
      // four black queens

      checkException(board, "Q5g4", SanValidationProblem.PIECE_RANK_MUST_USE_FILE_OR_SQUARE);

      board.performMove("Qh5g4");
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceSquare1NoLegalMove() {
    final ApiBoard board = new Board();

    // white
    // rook no square specification allowed
    // knight
    checkException(board, "Na1b3", SanValidationProblem.PIECE_SQUARE_NO_LEGAL_MOVE);

    // bishop
    checkException(board, "Bc1a3", SanValidationProblem.PIECE_SQUARE_NO_LEGAL_MOVE);

    // queen
    checkException(board, "Qd1d4", SanValidationProblem.PIECE_SQUARE_NO_LEGAL_MOVE);

    // black
    board.performMoves("e4");
    // rook no square specification allowed
    // knight
    checkException(board, "Nc6d4", SanValidationProblem.PIECE_SQUARE_NO_LEGAL_MOVE);

    // bishop
    checkException(board, "Bf8h6", SanValidationProblem.PIECE_SQUARE_NO_LEGAL_MOVE);

    // queen
    checkException(board, "Qd8d5", SanValidationProblem.PIECE_SQUARE_NO_LEGAL_MOVE);

  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceSquare2OnlyOneLegalMove() {
    final ApiBoard board = new Board();

    // white
    // rook no square specification allowed
    // knight
    checkException(board, "Nb1c3", SanValidationProblem.PIECE_SQUARE_ONLY_ONE_LEGAL_MOVE);

    // bishop
    board.performMoves("b3");
    board.performMoves("b6");
    checkException(board, "Bc1b2", SanValidationProblem.PIECE_SQUARE_ONLY_ONE_LEGAL_MOVE);

    // queen
    board.performMoves("d4");
    board.performMoves("d5");
    checkException(board, "Qd1d2", SanValidationProblem.PIECE_SQUARE_ONLY_ONE_LEGAL_MOVE);

    // black
    board.performMoves("e4");
    // rook no square specification allowed
    // knight
    checkException(board, "Nb8c6", SanValidationProblem.PIECE_SQUARE_ONLY_ONE_LEGAL_MOVE);

    // bishop
    checkException(board, "Bc8b7", SanValidationProblem.PIECE_SQUARE_ONLY_ONE_LEGAL_MOVE);

    // queen
    checkException(board, "Qd8d7", SanValidationProblem.PIECE_SQUARE_ONLY_ONE_LEGAL_MOVE);

  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceSquare3OnlyOnPieceOnFile() {
    {
      final ApiBoard board = new Board();

      board.performMoves("a4", "a5", "h4", "h5", "Ra3", "Ra6", "Rhh3", "Rhh6", "Rab3", "Rad6", "Rhd3", "Rd5", "Rd4",
          "Rg6");
      // two white rooks
      // rook no square specification allowed
      board.performMove("Rdb4");
      // two black rooks
      // rook no square specification allowed
      board.performMove("Rdg5");

      board.performMoves("Nf3", "Nf6", "Nc3", "Nc6", "Nd4", "Nd5");
      // two white knights

      checkException(board, "Nc3b5", SanValidationProblem.PIECE_SQUARE_ONLY_ONE_PIECE_ON_FILE);

      board.performMove("Ncb5");
      // two black knights

      checkException(board, "Nd5xb4", SanValidationProblem.PIECE_SQUARE_ONLY_ONE_PIECE_ON_FILE);

      board.performMove("Ndxb4");

      board.performMoves("e4", "f5", "e5", "d6", "exd6", "f4", "dxc7", "f3", "d3", "fxg2", "Bd2", "g1=B", "Bc1", "Nb8",
          "cxb8=B", "Bxf2+", "Kd2", "e6", "Bf4", "Bxd4", "Ke1", "Rh6", "Bxg5", "g6", "Bxh6", "Bfg7");

      // two white bishops

      checkException(board, "Bh6f4", SanValidationProblem.PIECE_SQUARE_ONLY_ONE_PIECE_ON_FILE);

      board.performMove("Bhf4");
      // two black bishops

      checkException(board, "Bd4e5", SanValidationProblem.PIECE_SQUARE_ONLY_ONE_PIECE_ON_FILE);

      board.performMove("Bde5");

      board.performMoves("c4", "g5", "c5", "g4", "c6", "g3", "cxb7", "g2", "bxc8=Q", "gxf1=Q+", "Kd2", "Kf7", "Qxh5+",
          "Kf6", "Qxe5+", "Kg6", "Qd4", "Qf6", "Qdc4", "Qf2+", "Kd1", "Na2", "Na3", "Nb4", "Q4a6", "Q2d4");

      // two white queens

      checkException(board, "Qa6c6", SanValidationProblem.PIECE_SQUARE_ONLY_ONE_PIECE_ON_FILE);

      board.performMove("Qac6");
      // two black queens

      checkException(board, "Qd4xf4", SanValidationProblem.PIECE_SQUARE_ONLY_ONE_PIECE_ON_FILE);

      board.performMove("Qdxf4");

    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceSquare4OnlyOnPieceOnRank() {
    {
      final ApiBoard board = new Board();

      board.performMoves("a4", "a5", "h4", "h5", "Ra3", "Ra6", "Rhh3", "Rhh6", "Rad3", "Rac6", "Rd4", "Rc5", "Rhd3",
          "Rhc6", "Rd5", "Rc4");
      // two white rooks
      // rook no square specification allowed
      board.performMove("R5d4");
      // two black rooks
      // rook no square specification allowed
      board.performMove("R4c5");

      board.performMoves("Nf3", "Na6", "Nc3", "Rb6", "Ne4", "Nf6", "Ng3", "Nd5", "Rc4", "c6", "Nf5", "Ndc7", "N5d4",
          "Na8", "Nf5", "N8c7");
      // two white knights

      checkException(board, "Nf5d4", SanValidationProblem.PIECE_SQUARE_ONLY_ONE_PIECE_ON_RANK);

      board.performMove("N5d4");
      // two black knights
      board.performMoves("Na8", "Nf5");

      checkException(board, "Na8c7", SanValidationProblem.PIECE_SQUARE_ONLY_ONE_PIECE_ON_RANK);

      board.performMove("N8c7");

      board.performMoves("g4", "Rb4", "gxh5", "Rxa4", "Bg2", "g6", "hxg6", "Rb4", "g7", "a4", "g8=B", "a3", "Bh7", "a2",
          "Ne3", "a1=B", "Bf5", "Bxb2", "Bg4", "Bf6");
      // two white bishops

      checkException(board, "Bg4h3", SanValidationProblem.PIECE_SQUARE_ONLY_ONE_PIECE_ON_RANK);

      board.performMove("B4h3");
      // two black bishops

      checkException(board, "Bf6g7", SanValidationProblem.PIECE_SQUARE_ONLY_ONE_PIECE_ON_RANK);

      board.performMove("B6g7");

      board.performMoves("h5", "b5", "h6", "Ra4", "h7", "b4", "h8=Q", "b3", "Rdd4", "bxc2", "Qh7", "Bb7", "Rb4", "Qb8",
          "Rb1", "cxb1=Q", "Qd3", "Qb6");
      // two white queens

      checkException(board, "Qd3c2", SanValidationProblem.PIECE_SQUARE_ONLY_ONE_PIECE_ON_RANK);

      board.performMove("Q3c2");
      // two black queens
      board.performMoves("Ne6", "Qd3");

      checkException(board, "Qb6c7", SanValidationProblem.PIECE_SQUARE_ONLY_ONE_PIECE_ON_RANK);

      board.performMove("Q6c7");
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testOverspecification() {

    final ApiBoard board = new Board();
    board.performMoves("e4", "e5", "d3", "Bb4+", "Nc3", "Nf6");
    checkException(board, "Nge2", SanValidationProblem.PIECE_FILE_ONLY_ONE_LEGAL_MOVE);
  }

  private static void checkException(ApiBoard board, String san, SanValidationProblem expectedValidation) {
    boolean isException;
    try {
      SanValidation.calculateMoveSpecificationForSan(san, board);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(expectedValidation, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }

}