package com.dlb.chess.test.illegal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.fen.FenParser;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.illegal.IllegalMoveSan;
import com.dlb.chess.illegal.model.DetectMovement;
import com.dlb.chess.illegal.model.GuessMove;

class TestIllegalMoveSan {

  @SuppressWarnings("static-method")
  @Test
  void testCastling() throws Exception {

    // legal castling
    check(true, "O-O", "4k3/8/8/8/8/8/8/4K2R w K - 0 100", "4k3/8/8/8/8/8/8/5RK1");
    check(true, "O-O-O", "4k3/8/8/8/8/8/8/R3K3 w Q - 0 100", "4k3/8/8/8/8/8/8/2KR4");
    check(true, "O-O", "4k2r/8/8/8/8/8/8/4K3 b k - 0 100", "5rk1/8/8/8/8/8/8/4K3");
    check(true, "O-O-O", "r3k3/8/8/8/8/8/8/4K3 b q - 0 100", "2kr4/8/8/8/8/8/8/4K3");

    // illegal castling due to missing castling rights
    check(false, "O-O", "4k3/8/8/8/8/8/8/4K2R w - - 0 100", "4k3/8/8/8/8/8/8/5RK1");
    check(false, "O-O-O", "4k3/8/8/8/8/8/8/R3K3 w - - 0 100", "4k3/8/8/8/8/8/8/2KR4");
    check(false, "O-O", "4k2r/8/8/8/8/8/8/4K3 b - - 0 100", "5rk1/8/8/8/8/8/8/4K3");
    check(false, "O-O-O", "r3k3/8/8/8/8/8/8/4K3 b - - 0 100", "2kr4/8/8/8/8/8/8/4K3");

    // illegal castling due to king in check
    check(false, "O-O", "4k3/4r3/8/8/8/8/8/4K2R w K - 0 100", "4k3/4r3/8/8/8/8/8/5RK1");
    check(false, "O-O-O", "4k3/4r3/8/8/8/8/8/R3K3 w Q - 0 100", "4k3/4r3/8/8/8/8/8/2KR4");
    check(false, "O-O", "4k2r/8/8/8/8/8/4R3/4K3 b k - 0 100", "5rk1/8/8/8/8/8/4R3/4K3");
    check(false, "O-O-O", "r3k3/8/8/8/8/8/4R3/4K3 b q - 0 100", "2kr4/8/8/8/8/8/4R3/4K3");

    // illegal castling due to king traveling through check
    check(false, "O-O", "4k3/5r2/8/8/8/8/8/4K2R w K - 0 100", "4k3/5r2/8/8/8/8/8/5RK1");
    check(false, "O-O-O", "4k3/3r4/8/8/8/8/8/R3K3 w Q - 0 100", "4k3/3r4/8/8/8/8/8/2KR4");
    check(false, "O-O", "4k2r/8/8/8/8/8/5R2/4K3 b k - 0 100", "5rk1/8/8/8/8/8/5R2/4K3");
    check(false, "O-O-O", "r3k3/8/8/8/8/8/3R4/4K3 b q - 0 100", "2kr4/8/8/8/8/8/3R4/4K3");

    // illegal castling due to king landing in check
    check(false, "O-O", "4k3/6r1/8/8/8/8/8/4K2R w K - 0 100", "4k3/6r1/8/8/8/8/8/5RK1");
    check(false, "O-O-O", "4k3/2r5/8/8/8/8/8/R3K3 w Q - 0 100", "4k3/2r5/8/8/8/8/8/2KR4");
    check(false, "O-O", "4k2r/8/8/8/8/8/6R1/4K3 b k - 0 100", "5rk1/8/8/8/8/8/6R1/4K3");
    check(false, "O-O-O", "r3k3/8/8/8/8/8/2R5/4K3 b q - 0 100", "2kr4/8/8/8/8/8/2R5/4K3");

    // illegal castling due to piece inbetween
    check(false, "O-O-O", "4k3/8/8/8/8/8/8/RN2K3 w Q - 0 100", "4k3/8/8/8/8/8/8/1NKR4");
    check(false, "O-O-O", "rn2k3/8/8/8/8/8/8/4K3 b q - 0 100", "1nkr4/8/8/8/8/8/8/4K3");
  }

  @SuppressWarnings("static-method")
  @Test
  void testEnPassantCapture() throws Exception {

    // white
    // legal en passant capture
    check(true, "axb6", "4k3/8/8/Pp6/8/8/8/4K3 w - b6 0 100", "4k3/8/1P6/8/8/8/8/4K3");
    check(true, "bxc6", "4k3/8/8/1Pp5/8/8/8/4K3 w - c6 0 100", "4k3/8/2P5/8/8/8/8/4K3");
    check(true, "cxd6", "4k3/8/8/2Pp4/8/8/8/4K3 w - d6 0 100", "4k3/8/3P4/8/8/8/8/4K3");
    check(true, "dxe6", "4k3/8/8/3Pp3/8/8/8/4K3 w - e6 0 100", "4k3/8/4P3/8/8/8/8/4K3");
    check(true, "exf6", "4k3/8/8/4Pp2/8/8/8/4K3 w - f6 0 100", "4k3/8/5P2/8/8/8/8/4K3");
    check(true, "fxg6", "4k3/8/8/5Pp1/8/8/8/4K3 w - g6 0 100", "4k3/8/6P1/8/8/8/8/4K3");
    check(true, "gxh6", "4k3/8/8/6Pp/8/8/8/4K3 w - h6 0 100", "4k3/8/7P/8/8/8/8/4K3");

    check(true, "bxa6", "4k3/8/8/pP6/8/8/8/4K3 w - a6 0 100", "4k3/8/P7/8/8/8/8/4K3");
    check(true, "cxb6", "4k3/8/8/1pP5/8/8/8/4K3 w - b6 0 100", "4k3/8/1P6/8/8/8/8/4K3");
    check(true, "dxc6", "4k3/8/8/2pP4/8/8/8/4K3 w - c6 0 100", "4k3/8/2P5/8/8/8/8/4K3");
    check(true, "exd6", "4k3/8/8/3pP3/8/8/8/4K3 w - d6 0 100", "4k3/8/3P4/8/8/8/8/4K3");
    check(true, "fxe6", "4k3/8/8/4pP2/8/8/8/4K3 w - e6 0 100", "4k3/8/4P3/8/8/8/8/4K3");
    check(true, "gxf6", "4k3/8/8/5pP1/8/8/8/4K3 w - f6 0 100", "4k3/8/5P2/8/8/8/8/4K3");
    check(true, "hxg6", "4k3/8/8/6pP/8/8/8/4K3 w - g6 0 100", "4k3/8/6P1/8/8/8/8/4K3");

    // illegal en passant capture
    check(false, "a5xb6", "4k3/8/8/Pp6/8/8/8/4K3 w - - 0 100", "4k3/8/1P6/8/8/8/8/4K3");
    check(false, "b5xc6", "4k3/8/8/1Pp5/8/8/8/4K3 w - - 0 100", "4k3/8/2P5/8/8/8/8/4K3");
    check(false, "c5xd6", "4k3/8/8/2Pp4/8/8/8/4K3 w - - 0 100", "4k3/8/3P4/8/8/8/8/4K3");
    check(false, "d5xe6", "4k3/8/8/3Pp3/8/8/8/4K3 w - - 0 100", "4k3/8/4P3/8/8/8/8/4K3");
    check(false, "e5xf6", "4k3/8/8/4Pp2/8/8/8/4K3 w - - 0 100", "4k3/8/5P2/8/8/8/8/4K3");
    check(false, "f5xg6", "4k3/8/8/5Pp1/8/8/8/4K3 w - - 0 100", "4k3/8/6P1/8/8/8/8/4K3");
    check(false, "g5xh6", "4k3/8/8/6Pp/8/8/8/4K3 w - - 0 100", "4k3/8/7P/8/8/8/8/4K3");

    check(false, "b5xa6", "4k3/8/8/pP6/8/8/8/4K3 w - - 0 100", "4k3/8/P7/8/8/8/8/4K3");
    check(false, "c5xb6", "4k3/8/8/1pP5/8/8/8/4K3 w - - 0 100", "4k3/8/1P6/8/8/8/8/4K3");
    check(false, "d5xc6", "4k3/8/8/2pP4/8/8/8/4K3 w - - 0 100", "4k3/8/2P5/8/8/8/8/4K3");
    check(false, "e5xd6", "4k3/8/8/3pP3/8/8/8/4K3 w - - 0 100", "4k3/8/3P4/8/8/8/8/4K3");
    check(false, "f5xe6", "4k3/8/8/4pP2/8/8/8/4K3 w - - 0 100", "4k3/8/4P3/8/8/8/8/4K3");
    check(false, "g5xf6", "4k3/8/8/5pP1/8/8/8/4K3 w - - 0 100", "4k3/8/5P2/8/8/8/8/4K3");
    check(false, "h5xg6", "4k3/8/8/6pP/8/8/8/4K3 w - - 0 100", "4k3/8/6P1/8/8/8/8/4K3");

    // black
    // legal en passant capture
    check(true, "hxg3", "4k3/8/8/8/6Pp/8/8/4K3 b - g3 0 100", "4k3/8/8/8/8/6p1/8/4K3");
    check(true, "gxf3", "4k3/8/8/8/5Pp1/8/8/4K3 b - f3 0 100", "4k3/8/8/8/8/5p2/8/4K3");
    check(true, "fxe3", "4k3/8/8/8/4Pp2/8/8/4K3 b - e3 0 100", "4k3/8/8/8/8/4p3/8/4K3");
    check(true, "exd3", "4k3/8/8/8/3Pp3/8/8/4K3 b - d3 0 100", "4k3/8/8/8/8/3p4/8/4K3");
    check(true, "dxc3", "4k3/8/8/8/2Pp4/8/8/4K3 b - c3 0 100", "4k3/8/8/8/8/2p5/8/4K3");
    check(true, "cxb3", "4k3/8/8/8/1Pp5/8/8/4K3 b - b3 0 100", "4k3/8/8/8/8/1p6/8/4K3");
    check(true, "bxa3", "4k3/8/8/8/Pp6/8/8/4K3 b - a3 0 100", "4k3/8/8/8/8/p7/8/4K3");

    check(true, "gxh3", "4k3/8/8/8/6pP/8/8/4K3 b - h3 0 100", "4k3/8/8/8/8/7p/8/4K3");
    check(true, "fxg3", "4k3/8/8/8/5pP1/8/8/4K3 b - g3 0 100", "4k3/8/8/8/8/6p1/8/4K3");
    check(true, "exf3", "4k3/8/8/8/4pP2/8/8/4K3 b - f3 0 100", "4k3/8/8/8/8/5p2/8/4K3");
    check(true, "dxe3", "4k3/8/8/8/3pP3/8/8/4K3 b - e3 0 100", "4k3/8/8/8/8/4p3/8/4K3");
    check(true, "cxd3", "4k3/8/8/8/2pP4/8/8/4K3 b - d3 0 100", "4k3/8/8/8/8/3p4/8/4K3");
    check(true, "bxc3", "4k3/8/8/8/1pP5/8/8/4K3 b - c3 0 100", "4k3/8/8/8/8/2p5/8/4K3");
    check(true, "axb3", "4k3/8/8/8/pP6/8/8/4K3 b - b3 0 100", "4k3/8/8/8/8/1p6/8/4K3");

    // illegal en passant capture
    check(false, "h4xg3", "4k3/8/8/8/6Pp/8/8/4K3 b - - 0 100", "4k3/8/8/8/8/6p1/8/4K3");
    check(false, "g4xf3", "4k3/8/8/8/5Pp1/8/8/4K3 b - - 0 100", "4k3/8/8/8/8/5p2/8/4K3");
    check(false, "f4xe3", "4k3/8/8/8/4Pp2/8/8/4K3 b - - 0 100", "4k3/8/8/8/8/4p3/8/4K3");
    check(false, "e4xd3", "4k3/8/8/8/3Pp3/8/8/4K3 b - - 0 100", "4k3/8/8/8/8/3p4/8/4K3");
    check(false, "d4xc3", "4k3/8/8/8/2Pp4/8/8/4K3 b - - 0 100", "4k3/8/8/8/8/2p5/8/4K3");
    check(false, "c4xb3", "4k3/8/8/8/1Pp5/8/8/4K3 b - - 0 100", "4k3/8/8/8/8/1p6/8/4K3");
    check(false, "b4xa3", "4k3/8/8/8/Pp6/8/8/4K3 b - - 0 100", "4k3/8/8/8/8/p7/8/4K3");

    check(false, "g4xh3", "4k3/8/8/8/6pP/8/8/4K3 b - - 0 100", "4k3/8/8/8/8/7p/8/4K3");
    check(false, "f4xg3", "4k3/8/8/8/5pP1/8/8/4K3 b - - 0 100", "4k3/8/8/8/8/6p1/8/4K3");
    check(false, "e4xf3", "4k3/8/8/8/4pP2/8/8/4K3 b - - 0 100", "4k3/8/8/8/8/5p2/8/4K3");
    check(false, "d4xe3", "4k3/8/8/8/3pP3/8/8/4K3 b - - 0 100", "4k3/8/8/8/8/4p3/8/4K3");
    check(false, "c4xd3", "4k3/8/8/8/2pP4/8/8/4K3 b - - 0 100", "4k3/8/8/8/8/3p4/8/4K3");
    check(false, "b4xc3", "4k3/8/8/8/1pP5/8/8/4K3 b - - 0 100", "4k3/8/8/8/8/2p5/8/4K3");
    check(false, "a4xb3", "4k3/8/8/8/pP6/8/8/4K3 b - - 0 100", "4k3/8/8/8/8/1p6/8/4K3");

  }

  @SuppressWarnings("static-method")
  @Test
  void testMovingPiece() throws Exception {

    // legal
    // white all pieces
    check(true, "Ra2", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/R3PPPP/1NBQKBNR");
    check(true, "Na3", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/pppp4/PPPP4/N7/4PPPP/R1BQKBNR");
    check(true, "Bb2", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/1B2PPPP/RN1QKBNR");
    check(true, "Qd3", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/pppp4/PPPP4/3Q4/4PPPP/RNB1KBNR");
    check(true, "Kd2", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/3KPPPP/RNBQ1BNR");
    check(true, "e4", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/pppp4/PPPPP3/8/5PPP/RNBQKBNR");

    // black all pieces
    check(true, "Ra7", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR b KQkq - 0 10",
        "1nbqkbnr/r3pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR");
    check(true, "Na6", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR b KQkq - 0 10",
        "r1bqkbnr/4pppp/n7/pppp4/PPPP4/8/4PPPP/RNBQKBNR");
    check(true, "Bb7", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR b KQkq - 0 10",
        "rn1qkbnr/1b2pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR");
    check(true, "Qd6", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR b KQkq - 0 10",
        "rnb1kbnr/4pppp/3q4/pppp4/PPPP4/8/4PPPP/RNBQKBNR");
    check(true, "Kd7", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR b KQkq - 0 10",
        "rnbq1bnr/3kpppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR");
    check(true, "e5", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR b KQkq - 0 10",
        "rnbqkbnr/5ppp/8/ppppp3/PPPP4/8/4PPPP/RNBQKBNR");

    // illegal
    // white to empty field - impossible movement
    check(false, "Ra1b2", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/1R2PPPP/1NBQKBNR");
    check(false, "Nb1b3", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/pppp4/PPPP4/1N6/4PPPP/R1BQKBNR");
    check(false, "Bc1c2", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/2B1PPPP/RN1QKBNR");
    check(false, "Qd1c3", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/pppp4/PPPP4/2Q5/4PPPP/RNB1KBNR");
    check(false, "Ke1c3", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/pppp4/PPPP4/2K5/4PPPP/RNBQ1BNR");
    check(false, "e2f3", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/pppp4/PPPP4/5P2/5PPP/RNBQKBNR");
    check(false, "e2e5", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/ppppP3/PPPP4/8/5PPP/RNBQKBNR");
    check(false, "e3e5", "rnbqkbnr/4pppp/8/pppp4/PPPP4/4P3/5PPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/ppppP3/PPPP4/8/5PPP/RNBQKBNR");

    // white captures opponent piece - impossible movement
    check(false, "Ra1xb8", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rRbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/1NBQKBNR");
    check(false, "Nb1xf7", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pNpp/8/pppp4/PPPP4/8/4PPPP/R1BQKBNR");
    check(false, "Bc1xc5", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/ppBp4/PPPP4/8/4PPPP/RN1QKBNR");
    check(false, "Qd1xg8", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbQr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNB1KBNR");
    check(false, "Ke1xc5", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/ppKp4/PPPP4/8/4PPPP/RNBQ1BNR");
    check(false, "a4xa5", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/Pppp4/1PPP4/8/4PPPP/RNBQKBNR");
    check(false, "a4xc5", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/ppPp4/1PPP4/8/4PPPP/RNBQKBNR");
    check(false, "e2xe4", "rnb1kbnr/4pppp/8/pppp4/PPPPq3/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnb1kbnr/4pppp/8/pppp4/PPPPP3/8/5PPP/RNBQKBNR");

    // white captures opponent piece - possible movement but piece not visible
    // rook
    check(false, "Ra1xa5", "rnbqkbnr/4pppp/8/pppp4/PPPP4/8/4PPPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkbnr/4pppp/8/Rppp4/PPPP4/8/4PPPP/1NBQKBNR");
    // bishop
    check(false, "Bc1xg5", "rnbqkb1r/4pppp/8/pppp2n1/PPPP4/4P3/5PPP/RNBQKBNR w KQkq - 0 10",
        "rnbqkb1r/4pppp/8/pppp2B1/PPPP4/4P3/5PPP/RN1QKBNR");
    // queen
    check(false, "Qd1xd5", "rnbqkb1r/4pppp/8/pppp2B1/PPPP4/4P3/5PPP/RN1QKBNR w KQkq - 0 10",
        "rnbqkb1r/4pppp/8/pppQ2B1/PPPP4/4P3/5PPP/RN2KBNR");

    // white captures own piece - possible movement
    check(false, "Ra1xb1", FenConstants.FEN_INITIAL_STR, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/1RBQKBNR");
    check(false, "Nb1xd2", FenConstants.FEN_INITIAL_STR, "rnbqkbnr/pppppppp/8/8/8/8/PPPNPPPP/R1BQKBNR");
    check(false, "Bc1xb2", FenConstants.FEN_INITIAL_STR, "rnbqkbnr/pppppppp/8/8/8/8/PBPPPPPP/RN1QKBNR");
    check(false, "Qd1xc1", FenConstants.FEN_INITIAL_STR, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNQ1KBNR");
    check(false, "Ke1xd1", FenConstants.FEN_INITIAL_STR, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBK1BNR");
    check(false, "a2xa3", "rnbqkbnr/pppppppp/8/8/8/N7/PPPPPPPP/R1BQKBNR w KQkq - 0 10",
        "rnbqkbnr/pppppppp/8/8/8/P7/1PPPPPPP/R1BQKBNR");
    check(false, "a2xa4", "rnbqkbnr/pppppppp/8/8/N7/8/PPPPPPPP/R1BQKBNR w KQkq - 0 10",
        "rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/R1BQKBNR");
    check(false, "a2xb3", "rnbqkbnr/pppppppp/8/8/8/1N6/PPPPPPPP/R1BQKBNR w KQkq - 0 10",
        "rnbqkbnr/pppppppp/8/8/8/1P6/1PPPPPPP/R1BQKBNR");

    // white leaves own king in check - possible movement
    check(false, "Ra1a2", "rnbqk1nr/pppp1ppp/8/4p3/2B1P2P/P5b1/1PPP2P1/RNBQK1NR w KQkq - 0 10",
        "rnbqk1nr/pppp1ppp/8/4p3/2B1P2P/P5b1/RPPP2P1/1NBQK1NR");
    check(false, "Nb1c3", "rnbqk1nr/pppp1ppp/8/4p3/2B1P2P/P5b1/1PPP2P1/RNBQK1NR w KQkq - 0 10",
        "rnbqk1nr/pppp1ppp/8/4p3/2B1P2P/P1N3b1/1PPP2P1/R1BQK1NR");
    check(false, "Bc4b3", "rnbqk1nr/pppp1ppp/8/4p3/2B1P2P/P5b1/1PPP2P1/RNBQK1NR w KQkq - 0 10",
        "rnbqk1nr/pppp1ppp/8/4p3/4P2P/PB4b1/1PPP2P1/RNBQK1NR");
    check(false, "Qd1f3", "rnbqk1nr/pppp1ppp/8/4p3/2B1P2P/P5b1/1PPP2P1/RNBQK1NR w KQkq - 0 10",
        "rnbqk1nr/pppp1ppp/8/4p3/2B1P2P/P4Qb1/1PPP2P1/RNB1K1NR");
    check(false, "Ke1f2", "rnbqk1nr/pppp1ppp/8/4p3/2B1P2P/P5b1/1PPP2P1/RNBQK1NR w KQkq - 0 10",
        "rnbqk1nr/pppp1ppp/8/4p3/2B1P2P/P5b1/1PPP1KP1/RNBQ2NR");
    check(false, "b2b3", "rnbqk1nr/pppp1ppp/8/4p3/2B1P2P/P5b1/1PPP2P1/RNBQK1NR w KQkq - 0 10",
        "rnbqk1nr/pppp1ppp/8/4p3/2B1P2P/PP4b1/2PP2P1/RNBQK1NR");
    check(false, "b2b4", "rnbqk1nr/pppp1ppp/8/4p3/2B1P2P/P5b1/1PPP2P1/RNBQK1NR w KQkq - 0 10",
        "rnbqk1nr/pppp1ppp/8/4p3/1PB1P2P/P5b1/2PP2P1/RNBQK1NR");

    // white exposes own king in check
    check(false, "Ke1f2", "rnbqk2r/pppp1ppp/8/4p3/2B1PbnP/PP6/2PP2P1/RNBQK1NR w KQkq - 0 10",
        "rnbqk2r/pppp1ppp/8/4p3/2B1PbnP/PP6/2PP1KP1/RNBQ2NR");

    // black to empty field - impossible movement
    check(false, "Ra8b6", "rnbqkbnr/1ppp1ppp/8/p3p3/4P3/P4N2/1PPP1PPP/RNBQKB1R b KQkq - 0 10",
        "1nbqkbnr/1ppp1ppp/1r6/p3p3/4P3/P4N2/1PPP1PPP/RNBQKB1R");
    check(false, "Nb8b6", "rnbqkbnr/1ppp1ppp/8/p3p3/4P3/P4N2/1PPP1PPP/RNBQKB1R b KQkq - 0 10",
        "r1bqkbnr/1ppp1ppp/1n6/p3p3/4P3/P4N2/1PPP1PPP/RNBQKB1R");
    check(false, "Bf8c4", "rnbqkbnr/1ppp1ppp/8/p3p3/4P3/P4N2/1PPP1PPP/RNBQKB1R b KQkq - 0 10",
        "rnbqk1nr/1ppp1ppp/8/p3p3/2b1P3/P4N2/1PPP1PPP/RNBQKB1R");
    check(false, "Qd8e6", "rnbqkbnr/1ppp1ppp/8/p3p3/4P3/P4N2/1PPP1PPP/RNBQKB1R b KQkq - 0 10",
        "rnb1kbnr/1ppp1ppp/4q3/p3p3/4P3/P4N2/1PPP1PPP/RNBQKB1R");
    check(false, "Ke8e6", "rnbqkbnr/1ppp1ppp/8/p3p3/4P3/P4N2/1PPP1PPP/RNBQKB1R b KQkq - 0 10",
        "rnbq1bnr/1ppp1ppp/4k3/p3p3/4P3/P4N2/1PPP1PPP/RNBQKB1R");
    check(false, "h7g6", "rnbqkbnr/1ppp1ppp/8/p3p3/4P3/P4N2/1PPP1PPP/RNBQKB1R b KQkq - 0 10",
        "rnbqkbnr/1ppp1pp1/6p1/p3p3/4P3/P4N2/1PPP1PPP/RNBQKB1R");
    check(false, "h7h4", "rnbqkbnr/1ppp1ppp/8/p3p3/4P3/P4N2/1PPP1PPP/RNBQKB1R b KQkq - 0 10",
        "rnbqkbnr/1ppp1pp1/8/p3p3/4P2p/P4N2/1PPP1PPP/RNBQKB1R");
    check(false, "h6h4", "rnbqkbnr/1ppp1pp1/7p/p3p3/4P3/P4N2/1PPP1PPP/RNBQKB1R b KQkq - 0 10",
        "rnbqkbnr/1ppp1pp1/8/p3p3/4P2p/P4N2/1PPP1PPP/RNBQKB1R");

    // black captures opponent piece - impossible movement
    check(false, "Rf8xe5", "r4rk1/pp1bbppp/1qn5/2ppPp2/3P1B2/PQP2N2/1P3PPP/RN2R1K1 b - - 0 100",
        "r5k1/pp1bbppp/1qn5/2pprp2/3P1B2/PQP2N2/1P3PPP/RN2R1K1");
    check(false, "Nc6xc3", "r4rk1/pp1bbppp/1qn5/2ppPp2/3P1B2/PQP2N2/1P3PPP/RN2R1K1 b - - 0 100",
        "r4rk1/pp1bbppp/1q6/2ppPp2/3P1B2/PQn2N2/1P3PPP/RN2R1K1");
    check(false, "Bd7xa3", "r4rk1/pp1bbppp/1qn5/2ppPp2/3P1B2/PQP2N2/1P3PPP/RN2R1K1 b - - 0 100",
        "r4rk1/pp2bppp/1qn5/2ppPp2/3P1B2/bQP2N2/1P3PPP/RN2R1K1");
    check(false, "Qb6xc3", "r4rk1/pp1bbppp/1qn5/2ppPp2/3P1B2/PQP2N2/1P3PPP/RN2R1K1 b - - 0 100",
        "r4rk1/pp1bbppp/2n5/2ppPp2/3P1B2/PQq2N2/1P3PPP/RN2R1K1");
    check(false, "Kg8xf4", "r4rk1/pp1bbppp/1qn5/2ppPp2/3P1B2/PQP2N2/1P3PPP/RN2R1K1 b - - 0 100",
        "r4r2/pp1bbppp/1qn5/2ppPp2/3P1k2/PQP2N2/1P3PPP/RN2R1K1");
    check(false, "f5xd4", "r4rk1/pp1bbppp/1qn5/2ppPp2/3P1B2/PQP2N2/1P3PPP/RN2R1K1 b - - 0 100",
        "r4rk1/pp1bbppp/1qn5/2ppP3/3p1B2/PQP2N2/1P3PPP/RN2R1K1");
    check(false, "h7xh2", "r4rk1/pp1bbppp/1qn5/2ppPp2/3P1B2/PQP2N2/1P3PPP/RN2R1K1 b - - 0 100",
        "r4rk1/pp1bbpp1/1qn5/2ppPp2/3P1B2/PQP2N2/1P3PPp/RN2R1K1");
    check(false, "h7xf3", "r4rk1/pp1bbppp/1qn5/2ppPp2/3P1B2/PQP2N2/1P3PPP/RN2R1K1 b - - 0 100",
        "r4rk1/pp1bbpp1/1qn5/2ppPp2/3P1B2/PQP2p2/1P3PPP/RN2R1K1");

    // black captures opponent piece - possible movement but piece not visible
    // rook
    check(false, "Rf8xf4", "r4rk1/pp1bbppp/1qn5/2ppPp2/3P1B2/PQP2N2/1P3PPP/RN2R1K1 b - - 0 100",
        "r5k1/pp1bbppp/1qn5/2ppPp2/3P1r2/PQP2N2/1P3PPP/RN2R1K1");
    // bishop
    check(false, "Be7xa3", "r4rk1/pp1bbppp/1qn5/2ppPp2/3P1B2/PQP2N2/1P3PPP/RN2R1K1 b - - 0 100",
        "r4rk1/pp1b1ppp/1qn5/2ppPp2/3P1B2/bQP2N2/1P3PPP/RN2R1K1");
    // queen
    check(false, "Qb6xb2", "r4rk1/pp1bbppp/1qn5/2ppPp2/3P1B2/PQP2N2/1P3PPP/RN2R1K1 b - - 0 100",
        "r4rk1/pp1bbppp/2n5/2ppPp2/3P1B2/PQP2N2/1q3PPP/RN2R1K1");

    // black captures own piece - possible movement
    check(false, "Rh8xh7", "r2qkb1r/pbp2ppp/np2p3/1N6/3P1Q2/P4N2/1PP1BPPP/R3K2R b KQkq - 0 100",
        "r2qkb2/pbp2ppr/np2p3/1N6/3P1Q2/P4N2/1PP1BPPP/R3K2R");
    check(false, "Na6xc7", "r2qkb1r/pbp2ppp/np2p3/1N6/3P1Q2/P4N2/1PP1BPPP/R3K2R b KQkq - 0 100",
        "r2qkb1r/pbn2ppp/1p2p3/1N6/3P1Q2/P4N2/1PP1BPPP/R3K2R");
    check(false, "Bb7xa6", "r2qkb1r/pbp2ppp/np2p3/1N6/3P1Q2/P4N2/1PP1BPPP/R3K2R b KQkq - 0 100",
        "r2qkb1r/p1p2ppp/bp2p3/1N6/3P1Q2/P4N2/1PP1BPPP/R3K2R");
    check(false, "Qd8xa8", "r2qkb1r/pbp2ppp/np2p3/1N6/3P1Q2/P4N2/1PP1BPPP/R3K2R b KQkq - 0 100",
        "q3kb1r/pbp2ppp/np2p3/1N6/3P1Q2/P4N2/1PP1BPPP/R3K2R");
    check(false, "Ke8xd8", "r2qkb1r/pbp2ppp/np2p3/1N6/3P1Q2/P4N2/1PP1BPPP/R3K2R b KQkq - 0 100",
        "r2k1b1r/pbp2ppp/np2p3/1N6/3P1Q2/P4N2/1PP1BPPP/R3K2R");
    check(false, "f7xe6", "r2qkb1r/p1p2ppp/np2b3/1N6/3P1Q2/P4N2/1PP1BPPP/R3K2R b KQkq - 0 100",
        "r2qkb1r/p1p3pp/np2p3/1N6/3P1Q2/P4N2/1PP1BPPP/R3K2R");
    check(false, "a7xa6", "r2qkb1r/pbp2ppp/np2p3/1N6/3P1Q2/P4N2/1PP1BPPP/R3K2R b KQkq - 0 100",
        "r2qkb1r/1bp2ppp/pp2p3/1N6/3P1Q2/P4N2/1PP1BPPP/R3K2R");
    check(false, "h7xh5", "r2qk2r/pbp2ppp/np2p3/1N5b/3P1Q2/P4N2/1PP1BPPP/R3K2R b KQkq - 0 100",
        "r2qk2r/pbp2pp1/np2p3/1N5p/3P1Q2/P4N2/1PP1BPPP/R3K2R");

    // black leaves own king in check - possible movement
    check(false, "Rh8xh2", "rnbqk1nr/pp2bp2/4p3/1B2P1p1/8/5NP1/P2B3P/1R1QK2R b Kkq - 0 100",
        "rnbqk1n1/pp2bp2/4p3/1B2P1p1/8/5NP1/P2B3r/1R1QK2R");
    check(false, "Ng8f6", "rnbqk1nr/pp2bp2/4p3/1B2P1p1/8/5NP1/P2B3P/1R1QK2R b Kkq - 0 100",
        "rnbqk2r/pp2bp2/4pn2/1B2P1p1/8/5NP1/P2B3P/1R1QK2R");
    check(false, "Be7a3", "rnbqk1nr/pp2bp2/4p3/1B2P1p1/8/5NP1/P2B3P/1R1QK2R b Kkq - 0 100",
        "rnbqk1nr/pp3p2/4p3/1B2P1p1/8/b4NP1/P2B3P/1R1QK2R");
    check(false, "Qd8xd2", "rnbqk1nr/pp2bp2/4p3/1B2P1p1/8/5NP1/P2B3P/1R1QK2R b Kkq - 0 100",
        "rnb1k1nr/pp2bp2/4p3/1B2P1p1/8/5NP1/P2q3P/1R1QK2R");
    check(false, "Ke8d7", "rnbqk1nr/pp2bp2/4p3/1B2P1p1/8/5NP1/P2B3P/1R1QK2R b Kkq - 0 100",
        "rnbq2nr/pp1kbp2/4p3/1B2P1p1/8/5NP1/P2B3P/1R1QK2R");
    check(false, "g5g4", "rnbqk1nr/pp2bp2/4p3/1B2P1p1/8/5NP1/P2B3P/1R1QK2R b Kkq - 0 100",
        "rnbqk1nr/pp2bp2/4p3/1B2P3/6p1/5NP1/P2B3P/1R1QK2R");
    check(false, "a7a5", "rnbqk1nr/pp2bp2/4p3/1B2P1p1/8/5NP1/P2B3P/1R1QK2R b Kkq - 0 100",
        "rnbqk1nr/1p2bp2/4p3/pB2P1p1/8/5NP1/P2B3P/1R1QK2R");

    // black exposes own king in check
    check(false, "Kg8f8", "2rq2kr/pp1bppB1/3p1np1/7p/8/2N2N2/PPP2PPP/R2QK2R b KQ - 0 100",
        "2rq1k1r/pp1bppB1/3p1np1/7p/8/2N2N2/PPP2PPP/R2QK2R");

    // cannot detect movement
    // white legal moves
    check(false, "NA", "rn1qk1nr/1p1bbp2/4p3/pB2P1p1/8/5NP1/P2B3P/1R1QK2R w Kkq - 0 100",
        "rn1qk1nr/1p1bbp2/4p3/pB2P1p1/8/2B2NP1/P1Q4P/1R2K2R");
    check(false, "NA", "rn1qk1nr/1p1bbp2/4p3/pB2P1p1/8/5NP1/P2B3P/1R1QK2R w Kkq - 0 100",
        "rn1qk1nr/1p1bbp2/4p3/pB2P1N1/8/P5P1/3B3P/1R1QK2R");
    check(false, "NA", "rn1qk1nr/1p1bbp2/4p3/pB2P1p1/8/5NP1/P2B3P/1R1QK2R w Kkq - 0 100",
        "rn1qk1nr/1p1bbp2/4p3/pB2P1p1/8/5NP1/P2B3P/1R1Q1KR1");

    // black legal moves
    check(false, "NA", "2r2k1r/pp1bpp2/3p2p1/3q3p/8/8/PRPN1PPP/3QK2R b K - 0 100",
        "2r2k1r/p2bpp2/1p1p2p1/7p/3q4/8/PRPN1PPP/3QK2R");
    check(false, "NA", "2r2k1r/pp1bpp2/3p2p1/3q3p/8/8/PRPN1PPP/3QK2R b K - 0 100",
        "2r2kr1/pp1bp3/3p1pp1/3q3p/8/8/PRPN1PPP/3QK2R");
    check(false, "NA", "2r2k1r/pp1bpp2/3p2p1/3q3p/8/8/PRPN1PPP/3QK2R b K - 0 100",
        "2r1bk1r/pp2pp2/3p4/3q2pp/8/8/PRPN1PPP/3QK2R");

    // white non possible movements
    check(false, "NA", "rn1qk1nr/1p1bbp2/4p3/pB2P1p1/8/5NP1/P2B3P/1R1QK2R w Kkq - 0 100",
        "rn1qk1nr/1p1bbp2/4p3/pB2P1p1/8/3B1NP1/P6P/1R1Q2KR");
    check(false, "NA", "rn1qk1nr/1p1bbp2/4p3/pB2P1p1/8/5NP1/P2B3P/1R1QK2R w Kkq - 0 100",
        "rn1qk1nr/1p1bbp2/4p3/pB2P1p1/8/2R2NP1/PQ1B3P/4K2R");
    check(false, "NA", "rn1qk1nr/1p1bbp2/4p3/pB2P1p1/8/5NP1/P2B3P/1R1QK2R w Kkq - 0 100",
        "rn1qk1nr/1p1bbp2/4p3/pB2P1p1/8/1P4P1/3B1N1P/1R1QK2R");

    // black non possible movements
    check(false, "NA", "2r2k1r/pp1bpp2/3p2p1/3q3p/8/8/PRPN1PPP/3QK2R b K - 0 100",
        "2rk3r/pp1bpp2/3p2p1/7p/5q2/8/PRPN1PPP/3QK2R");
    check(false, "NA", "2r2k1r/pp1bpp2/3p2p1/3q3p/8/8/PRPN1PPP/3QK2R b K - 0 100",
        "5k1r/ppb1pp2/1r1p2p1/3q3p/8/8/PRPN1PPP/3QK2R");
    check(false, "NA", "2r2k1r/pp1bpp2/3p2p1/3q3p/8/8/PRPN1PPP/3QK2R b K - 0 100",
        "2r4r/p2bpp1k/p2p2p1/3q3p/8/8/PRPN1PPP/3QK2R");

    // mixed legal movements
    check(false, "NA", "rn1qk1nr/1p1bbp2/4p3/pB2P1p1/8/5NP1/P2B3P/1R1QK2R w Kkq - 0 100",
        "rn1qk1nr/3bbp2/1p2p3/pB2P1p1/P7/5NP1/3B3P/1R1QK2R");
    check(false, "NA", "rn1qk1nr/1p1bbp2/4p3/pB2P1p1/8/5NP1/P2B3P/1R1QK2R w Kkq - 0 100",
        "r2qk1nr/1p1bbp2/2n1p3/pB2P1p1/3N4/6P1/P2B3P/1R1QK2R");
    check(false, "NA", "rn1qk1nr/1p1bbp2/4p3/pB2P1p1/8/5NP1/P2B3P/1R1QK2R w Kkq - 0 100",
        "rn1q1knr/1p1bbp2/4p3/pB2P1p1/8/5NP1/P2B1K1P/1R1Q3R");

    // unrelated positions
    check(false, "NA", "rn1q1knr/1p1bbp2/4p3/pB2P1p1/8/5NP1/P2B1K1P/1R1Q3R w - - 0 100", "8/7Q/8/6p1/5p1k/5K2/8/8");
    check(false, "NA", "rn1q1knr/1p1bbp2/4p3/pB2P1p1/8/5NP1/P2B1K1P/1R1Q3R w - - 0 100",
        "1r2kb1r/p1pq1ppp/2B2n2/4p3/Q7/2PpP3/PP1P1P1P/RNB1K1N1");
    check(false, "NA", "rn1q1knr/1p1bbp2/4p3/pB2P1p1/8/5NP1/P2B1K1P/1R1Q3R w - - 0 100",
        "Qn1qkb1r/p3nppp/2p1b3/3pp3/8/2P1P3/PP1P1PPP/RNB1KBNR");

  }

  private static void check(boolean isLegal, String san, String previousFen, String currentPiecePlacement) {
    final ApiBoard previousPosition = new Board(previousFen);
    final StaticPosition currentPosition = FenParser.validatePiecePlacement(currentPiecePlacement);

    final GuessMove model = IllegalMoveSan.guessMove(previousPosition, currentPosition);
    assertEquals(isLegal, model.isLegal());

    if (isLegal) {
      assertEquals(san, model.san());
    } else {
      final DetectMovement detectMovement = IllegalMoveSan.detectMovement(previousPosition, currentPosition);
      final String lan = IllegalMoveSan.calculateLan(detectMovement);
      assertEquals(san, lan);
    }
  }
}
