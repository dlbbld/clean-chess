package com.dlb.chess.test.lan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ChessBoard;

class TestLanCalculation implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void test() {
    final ChessBoard board = new Board();

    board.moveStrict("e4");
    assertEquals("e2e4", board.getLan());

    board.moveStrict("d5");
    assertEquals("d7d5", board.getLan());

    board.moveStrict("exd5");
    assertEquals("e4xd5", board.getLan());

    board.moveStrict("e5");
    assertEquals("e7e5", board.getLan());

    board.moveStrict("dxe6");
    assertEquals("d5xe6", board.getLan());

    board.moveStrict("h6");
    assertEquals("h7h6", board.getLan());

    board.moveStrict("exf7+");
    assertEquals("e6xf7+", board.getLan());

    board.moveStrict("Ke7");
    assertEquals("Ke8e7", board.getLan());

    board.moveStrict("fxg8=Q");
    assertEquals("f7xg8=Q", board.getLan());

    board.moveStrict("h5");
    assertEquals("h6h5", board.getLan());

    board.moveStrict("Qxh8");
    assertEquals("Qg8xh8", board.getLan());

    board.moveStrict("h4");
    assertEquals("h5h4", board.getLan());

    board.moveStrict("g4");
    assertEquals("g2g4", board.getLan());

    board.moveStrict("hxg3");
    assertEquals("h4xg3", board.getLan());

    board.moveStrict("a4");
    assertEquals("a2a4", board.getLan());

    board.moveStrict("gxh2");
    assertEquals("g3xh2", board.getLan());

    board.moveStrict("a5");
    assertEquals("a4a5", board.getLan());

    board.moveStrict("hxg1=Q");
    assertEquals("h2xg1=Q", board.getLan());

    board.moveStrict("a6");
    assertEquals("a5a6", board.getLan());

    board.moveStrict("Qxh1");
    assertEquals("Qg1xh1", board.getLan());

  }

}