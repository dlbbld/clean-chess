package com.dlb.chess.test.common;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;

class TestNonNullWrapperCommon {

  @SuppressWarnings("static-method")
  @Test
  void test() {

    final var pgn = """
        line 1

        line 3
        """;

    final String[] expected = { "line 1", "", "line 3", "" };
    final String[] actual = NonNullWrapperCommon.split(pgn, "\\n");

    assertArrayEquals(expected, actual);
  }

}