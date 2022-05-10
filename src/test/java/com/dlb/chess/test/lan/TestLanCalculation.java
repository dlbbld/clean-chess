package com.dlb.chess.test.lan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ApiBoard;

class TestLanCalculation implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void test() {
    final ApiBoard board = new Board();

    board.performMove("e4");
    assertEquals("e2e4", board.getLan());

    board.performMove("d5");
    assertEquals("d7d5", board.getLan());

    board.performMove("exd5");
    assertEquals("e4xd5", board.getLan());

    board.performMove("e5");
    assertEquals("e7e5", board.getLan());

    board.performMove("dxe6");
    assertEquals("d5xe6", board.getLan());

    board.performMove("h6");
    assertEquals("h7h6", board.getLan());

    board.performMove("exf7+");
    assertEquals("e6xf7+", board.getLan());

    board.performMove("Ke7");
    assertEquals("Ke8e7", board.getLan());

    board.performMove("fxg8=Q");
    assertEquals("f7xg8=Q", board.getLan());

    board.performMove("h5");
    assertEquals("h6h5", board.getLan());

    board.performMove("Qxh8");
    assertEquals("Qg8xh8", board.getLan());

    board.performMove("h4");
    assertEquals("h5h4", board.getLan());

    board.performMove("g4");
    assertEquals("g2g4", board.getLan());

    board.performMove("hxg3");
    assertEquals("h4xg3", board.getLan());

    board.performMove("a4");
    assertEquals("a2a4", board.getLan());

    board.performMove("gxh2");
    assertEquals("g3xh2", board.getLan());

    board.performMove("a5");
    assertEquals("a4a5", board.getLan());

    board.performMove("hxg1=Q");
    assertEquals("h2xg1=Q", board.getLan());

    board.performMove("a6");
    assertEquals("a5a6", board.getLan());

    board.performMove("Qxh1");
    assertEquals("Qg1xh1", board.getLan());

  }

}