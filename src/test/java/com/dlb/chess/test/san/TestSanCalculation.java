package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ApiBoard;

class TestSanCalculation implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void test() {
    final ApiBoard board = new Board();

    board.performMove("e4");
    assertEquals("e4", board.getSan());

    board.performMove("d5");
    assertEquals("d5", board.getSan());

    board.performMove("exd5");
    assertEquals("exd5", board.getSan());

    board.performMove("e5");
    assertEquals("e5", board.getSan());

    board.performMove("dxe6");
    assertEquals("dxe6", board.getSan());

    board.performMove("h6");
    assertEquals("h6", board.getSan());

    board.performMove("exf7+");
    assertEquals("exf7+", board.getSan());

    board.performMove("Ke7");
    assertEquals("Ke7", board.getSan());

    board.performMove("fxg8=Q");
    assertEquals("fxg8=Q", board.getSan());

    board.performMove("h5");
    assertEquals("h5", board.getSan());

    board.performMove("Qxh8");
    assertEquals("Qxh8", board.getSan());

    board.performMove("h4");
    assertEquals("h4", board.getSan());

    board.performMove("g4");
    assertEquals("g4", board.getSan());

    board.performMove("hxg3");
    assertEquals("hxg3", board.getSan());

    board.performMove("a4");
    assertEquals("a4", board.getSan());

    board.performMove("gxh2");
    assertEquals("gxh2", board.getSan());

    board.performMove("a5");
    assertEquals("a5", board.getSan());

    board.performMove("hxg1=Q");
    assertEquals("hxg1=Q", board.getSan());

    board.performMove("a6");
    assertEquals("a6", board.getSan());

    board.performMove("Qxh1");
    assertEquals("Qxh1", board.getSan());

  }

}