package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.san.SanValidation;

class TestPerformMoveForSan implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void testLastChar() {

    var isException = false;

    try {
      final ApiBoard board = new Board();

      // we require checks in SAN to be real checks
      // we do not allow fake checks white
      board.performMove("e4");
      // we do not allow fake checks black
      board.performMove("e5");
      board.performMove("Bc4");
      board.performMove("Bc5");
      // real check white
      board.performMove("Bxf7+");
      board.performMove("Ke7");
      board.performMove("a3");
      // real check black
      board.performMove("Bxf2+");
      board.performMove("Ke2");

      // we require checkmate in SAN to be real checkmate
      // we do not allow fake checkmate white
      board.performMove("a6");
      board.performMove("d3");
      // we do not allow fake checkmate black
      board.performMove("Bc5");
      board.performMove("Bc4");
      board.performMove("Ke8");
      board.performMove("Ke1");
      board.performMove("a5");
      board.performMove("Qf3");
      board.performMove("a4");
      // real checkmate white
      board.performMove("Qf7#");

      // real checkmate black
      {
        final ApiBoard boardCheckmateBlack = new Board();

        boardCheckmateBlack.performMove("f3");
        boardCheckmateBlack.performMove("e5");
        boardCheckmateBlack.performMove("g4");
        // real checkmate black
        boardCheckmateBlack.performMove("Qh4#");
      }
    } catch (@SuppressWarnings("unused") final Exception e) {
      isException = true;
    }

    assertFalse(isException);
  }

  @SuppressWarnings("static-method")
  @Test
  void testCalculateMoveSpecificationForSan() {
    var isException = false;

    try {
      final ApiBoard board = new Board();

      checkMoveSpecificationTest(board, "e4", new MoveSpecification(WHITE, E2, E4));
      checkMoveSpecificationTest(board, "d5", new MoveSpecification(BLACK, D7, D5));
      checkMoveSpecificationTest(board, "exd5", new MoveSpecification(WHITE, E4, D5));
      checkMoveSpecificationTest(board, "e5", new MoveSpecification(BLACK, E7, E5));
      checkMoveSpecificationTest(board, "dxe6", new MoveSpecification(WHITE, D5, E6));
      checkMoveSpecificationTest(board, "h6", new MoveSpecification(BLACK, H7, H6));
      checkMoveSpecificationTest(board, "exf7+", new MoveSpecification(WHITE, E6, F7));
      checkMoveSpecificationTest(board, "Ke7", new MoveSpecification(BLACK, E8, E7));
      checkMoveSpecificationTest(board, "fxg8=Q", new MoveSpecification(WHITE, F7, G8, PromotionPieceType.QUEEN));
      checkMoveSpecificationTest(board, "h5", new MoveSpecification(BLACK, H6, H5));
      checkMoveSpecificationTest(board, "Qxh8", new MoveSpecification(WHITE, G8, H8));
      checkMoveSpecificationTest(board, "h4", new MoveSpecification(BLACK, H5, H4));
      checkMoveSpecificationTest(board, "g4", new MoveSpecification(WHITE, G2, G4));
      checkMoveSpecificationTest(board, "hxg3", new MoveSpecification(BLACK, H4, G3));
      checkMoveSpecificationTest(board, "a4", new MoveSpecification(WHITE, A2, A4));
      checkMoveSpecificationTest(board, "gxh2", new MoveSpecification(BLACK, G3, H2));
      checkMoveSpecificationTest(board, "a5", new MoveSpecification(WHITE, A4, A5));
      checkMoveSpecificationTest(board, "hxg1=Q", new MoveSpecification(BLACK, H2, G1, PromotionPieceType.QUEEN));
      checkMoveSpecificationTest(board, "a6", new MoveSpecification(WHITE, A5, A6));
      checkMoveSpecificationTest(board, "Qxh1", new MoveSpecification(BLACK, G1, H1));
    } catch (@SuppressWarnings("unused") final Exception e) {
      isException = true;
    }

    assertFalse(isException);
  }

  @SuppressWarnings("static-method")
  @Test
  void testFileRankSpecificationValid() {

    // rook
    {
      final ApiBoard board = new Board();

      board.performMoves("a4", "a5", "h4", "h5", "Ra3", "Ra6");
      // valid file specification white
      board.performMoves("Rhh3");
      // valid file specification black
      board.performMoves("Rhh6");
      // valid file specification white
      board.performMoves("Rhe3");
      // valid file specification black
      board.performMoves("Rhe6");
      // valid file specification white
      board.performMoves("Rad3");
      // valid file specification black
      board.performMoves("Rad6");
      board.performMoves("Rd4");
      board.performMoves("Rd5");
      // valid file specification white
      board.performMoves("Rde4");
      // valid file specification black
      board.performMoves("Red6");
      board.performMoves("Re5");
      board.performMoves("Rd4");
      // valid rank specification white
      board.performMoves("R3e4");
      board.performMoves("Rd3");
      board.performMoves("Re6");
      // valid rank specification white
      board.performMoves("R6d5");
    }

    // knight
    // "Nc3", "Nc6", "Nh3", "Nh6", "Nf4", "Nf5", "Nfd5", "Nfd4", "Nb4", "Nb5", "Nd3", "Nd6", "Nc5", "Nc4", "N5e4",
    // "N4e5", "h4", "a5", "h5", "a4", "h6", "a3", "hxg7", "axb2", "gxf8=N", "bxc1=N", "Ng5", "Ng4", "Nfe6", "Nb3",
    // "Nc5", "Nba5", "Nc5e4", "Nc4", "Nc5", "Nc4e5"
    {
      final ApiBoard board = new Board();

      board.performMoves("Nc3", "Nc6", "Nh3", "Nh6", "Nf4", "Nf5");
      // valid file specification white
      board.performMoves("Nfd5");
      // valid file specification black
      board.performMoves("Nfd4");
      board.performMoves("Nb4", "Nb5", "Nd3", "Nd6", "Nc5", "Nc4");
      // valid rank specification white
      board.performMoves("N5e4");
      // valid rank specification black
      board.performMoves("N4e5");
      board.performMoves("h4", "a5", "h5", "a4", "h6", "a3", "hxg7", "axb2", "gxf8=N", "bxc1=N", "Ng5", "Ng4", "Nfe6",
          "Nb3");
      board.performMoves("Nc5", "Nba5");
      // valid square specification white
      board.performMoves("Nc5e4");
      board.performMoves("Nc4");
      board.performMoves("Nc5");
      // valid square specification black
      board.performMoves("Nc4e5");
    }

    // bishop
    // "d4", "e5", "d5", "e4", "f3", "c6", "dxc6", "exf3", "cxb7", "fxg2", "bxa8=B", "gxh1=B", "e3", "d6", "Bd5", "Bf3",
    // "Bb5+", "Ke7", "Bd2", "Bh3",
    // "Bbc6", "Bfg4", "Bc4", "Bg2", "B4d5", "B2h3", "e4", "a5", "e5", "a4", "e6", "a3",
    // "exf7", "axb2", "fxg8=B", "Qc8", "Na3", "b1=B", "Bc4", "Bxc2", "Bgd5", "Be4", "Be6", "Bf1", "Bc6d5", "Bfe2",
    // "Nb1", "Be4f3
    {
      final ApiBoard board = new Board();

      board.performMoves("d4", "e5", "d5", "e4", "f3", "c6", "dxc6", "exf3", "cxb7", "fxg2", "bxa8=B", "gxh1=B", "e3",
          "d6", "Bd5", "Bf3");
      board.performMoves("Bb5+", "Ke7", "Bd2", "Bh3");
      // valid file specification white
      board.performMoves("Bbc6");
      // valid file specification black
      board.performMoves("Bfg4");
      board.performMoves("Bc4", "Bg2");
      // valid rank specification white
      board.performMoves("B4d5");
      // valid rank specification black
      board.performMoves("B2h3");
      board.performMoves("e4", "a5", "e5", "a4", "e6", "a3");
      board.performMoves("exf7", "axb2", "fxg8=B", "Qc8", "Na3", "b1=B", "Bc4", "Bxc2", "Bgd5", "Be4", "Be6", "Bf1");
      // valid square specification white
      board.performMoves("Bc6d5");
      board.performMoves("Bfe2");
      board.performMoves("Nb1");
      // valid square specification black
      board.performMoves("Be4f3");

    }

    // queen
    // "c4", "f5", "c5", "f4", "c6", "f3", "cxb7", "fxg2", "bxa8=Q", "gxh1=Q", "Qb3", "e6", "Qad5", "Qg5", "Qdc4",
    // "Qe4", "Qcc3", "Qgf4", "Qcb4", "Qfe5", "Q3c4", "Q5f4", "d4", "g5", "d5", "g4", "d6", "g3", "dxc7", "gxh2",
    // "cxb8=Q", "hxg1=Q", "Q8b5", "Qgg5", "Qb4c5", "Qef5", "Qc5b4", "Qf5g4"
    {
      final ApiBoard board = new Board();

      board.performMoves("c4", "f5", "c5", "f4", "c6", "f3", "cxb7", "fxg2", "bxa8=Q", "gxh1=Q", "Qb3", "e6");
      // valid file specification white
      board.performMoves("Qad5");
      board.performMoves("Qg5");
      board.performMoves("Qdc4");
      board.performMoves("Qe4");
      // valid file specification black
      board.performMoves("Qcc3");
      board.performMoves("Qgf4", "Qcb4", "Qfe5");
      // valid rank specification white
      board.performMoves("Q3c4");
      // valid rank specification black
      board.performMoves("Q5f4");
      board.performMoves("d4", "g5", "d5", "g4", "d6", "g3", "dxc7", "gxh2");
      board.performMoves("cxb8=Q", "hxg1=Q", "Q8b5", "Qgg5");
      // valid square specification white
      board.performMoves("Qb4c5");
      board.performMoves("Qef5");
      board.performMoves("Qc5b4");
      // valid square specification black
      board.performMoves("Qf5g4");

    }

  }

  private static void checkMoveSpecificationTest(ApiBoard board, String san, MoveSpecification expectedMove) {
    final MoveSpecification parsedMoveAsIs = SanValidation.calculateMoveSpecificationForSan(san, board);
    assertEquals(expectedMove, parsedMoveAsIs);

    board.performMove(expectedMove);

  }

}