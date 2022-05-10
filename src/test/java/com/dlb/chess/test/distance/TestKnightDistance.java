package com.dlb.chess.test.distance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.distance.KnightDistance;

public class TestKnightDistance implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void test() {
    assertEquals(0, KnightDistance.distance(A1, A1));

    assertEquals(1, KnightDistance.distance(A1, B3));
    assertEquals(2, KnightDistance.distance(A1, C5));
    assertEquals(3, KnightDistance.distance(A1, D7));
    assertEquals(4, KnightDistance.distance(A1, F8));

    assertEquals(5, KnightDistance.distance(A1, H1));
    assertEquals(6, KnightDistance.distance(A1, H8));
  }
}
