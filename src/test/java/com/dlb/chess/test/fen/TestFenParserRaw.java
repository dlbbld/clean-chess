package com.dlb.chess.test.fen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.exceptions.FenRawValidationException;
import com.dlb.chess.fen.FenParserRaw;
import com.dlb.chess.fen.constants.FenConstants;

class TestFenParserRaw {
  @SuppressWarnings("static-method")
  @Test
  void testSuccessParseFields() {

    // 1. e4
    // 1... c6
    // 2. Nf3
    // 3... Nf6
    // 4. Rg1
    final var halfMove0 = FenConstants.FEN_INITIAL_STR;
    final var halfMove1 = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
    final var halfMove2 = "rnbqkbnr/pp1ppppp/2p5/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2";
    final var halfMove3 = "rnbqkbnr/pp1ppppp/2p5/8/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2";
    final var halfMove4 = "rnbqkb1r/pp1ppppp/2p2n2/8/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 2 3";
    final var halfMove5 = "rnbqkb1r/pp1ppppp/2p2n2/8/4P3/5N2/PPPP1PPP/RNBQKBR1 b Qkq - 3 3";

    assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", FenParserRaw.parsePiecePlacement(halfMove0));
    assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR", FenParserRaw.parsePiecePlacement(halfMove1));
    assertEquals("rnbqkbnr/pp1ppppp/2p5/8/4P3/8/PPPP1PPP/RNBQKBNR", FenParserRaw.parsePiecePlacement(halfMove2));
    assertEquals("rnbqkbnr/pp1ppppp/2p5/8/4P3/5N2/PPPP1PPP/RNBQKB1R", FenParserRaw.parsePiecePlacement(halfMove3));
    assertEquals("rnbqkb1r/pp1ppppp/2p2n2/8/4P3/5N2/PPPP1PPP/RNBQKB1R", FenParserRaw.parsePiecePlacement(halfMove4));
    assertEquals("rnbqkb1r/pp1ppppp/2p2n2/8/4P3/5N2/PPPP1PPP/RNBQKBR1", FenParserRaw.parsePiecePlacement(halfMove5));

    assertEquals("w", FenParserRaw.parseHavingMove(halfMove0));
    assertEquals("b", FenParserRaw.parseHavingMove(halfMove1));
    assertEquals("w", FenParserRaw.parseHavingMove(halfMove2));
    assertEquals("b", FenParserRaw.parseHavingMove(halfMove3));
    assertEquals("w", FenParserRaw.parseHavingMove(halfMove4));
    assertEquals("b", FenParserRaw.parseHavingMove(halfMove5));

    assertEquals("KQkq", FenParserRaw.parseCastlingRight(halfMove0));
    assertEquals("KQkq", FenParserRaw.parseCastlingRight(halfMove1));
    assertEquals("KQkq", FenParserRaw.parseCastlingRight(halfMove2));
    assertEquals("KQkq", FenParserRaw.parseCastlingRight(halfMove3));
    assertEquals("KQkq", FenParserRaw.parseCastlingRight(halfMove4));
    assertEquals("Qkq", FenParserRaw.parseCastlingRight(halfMove5));

    assertEquals("-", FenParserRaw.parseEnPassantCaptureTargetSquare(halfMove0));
    assertEquals("e3", FenParserRaw.parseEnPassantCaptureTargetSquare(halfMove1));
    assertEquals("-", FenParserRaw.parseEnPassantCaptureTargetSquare(halfMove2));
    assertEquals("-", FenParserRaw.parseEnPassantCaptureTargetSquare(halfMove3));
    assertEquals("-", FenParserRaw.parseEnPassantCaptureTargetSquare(halfMove4));
    assertEquals("-", FenParserRaw.parseEnPassantCaptureTargetSquare(halfMove5));

    assertEquals("0", FenParserRaw.parseHalfMoveClock(halfMove0));
    assertEquals("0", FenParserRaw.parseHalfMoveClock(halfMove1));
    assertEquals("0", FenParserRaw.parseHalfMoveClock(halfMove2));
    assertEquals("1", FenParserRaw.parseHalfMoveClock(halfMove3));
    assertEquals("2", FenParserRaw.parseHalfMoveClock(halfMove4));
    assertEquals("3", FenParserRaw.parseHalfMoveClock(halfMove5));

    assertEquals("1", FenParserRaw.parseFullMoveNumber(halfMove0));
    assertEquals("1", FenParserRaw.parseFullMoveNumber(halfMove1));
    assertEquals("2", FenParserRaw.parseFullMoveNumber(halfMove2));
    assertEquals("2", FenParserRaw.parseFullMoveNumber(halfMove3));
    assertEquals("3", FenParserRaw.parseFullMoveNumber(halfMove4));
    assertEquals("3", FenParserRaw.parseFullMoveNumber(halfMove5));
  }

  @SuppressWarnings("static-method")
  @Test
  void testException() {
    checkException(" rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    checkException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR  w KQkq - 0 1");
    checkException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w  KQkq - 0 1");
    checkException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq  - 0 1");
    checkException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -  0 1");
    checkException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0  1");
    checkException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 ");

    checkException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNRw KQkq - 0 1");
    checkException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR wKQkq - 0 1");
    checkException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq- 0 1");
    checkException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -0 1");
    checkException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 01");
  }

  private static void checkException(String fen) {
    var isException = false;
    try {
      FenParserRaw.parseFenRaw(fen);
    } catch (@SuppressWarnings("unused") final FenRawValidationException e) {
      isException = true;
    }
    assertTrue(isException);

  }
}
