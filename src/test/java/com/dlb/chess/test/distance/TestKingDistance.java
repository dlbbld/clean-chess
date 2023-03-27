package com.dlb.chess.test.distance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.distance.KingDistance;

class TestKingDistance implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void test() {
    assertEquals(0, KingDistance.distance(A1, A1));
    assertEquals(1, KingDistance.distance(A1, A2));

    assertEquals(7, KingDistance.distance(A1, A8));
    assertEquals(7, KingDistance.distance(A1, B8));
    assertEquals(7, KingDistance.distance(A1, C8));
    assertEquals(7, KingDistance.distance(A1, D8));
    assertEquals(7, KingDistance.distance(A1, E8));
    assertEquals(7, KingDistance.distance(A1, F8));
    assertEquals(7, KingDistance.distance(A1, G8));
    assertEquals(7, KingDistance.distance(A1, H8));

    assertEquals(3, KingDistance.distance(A1, D4));
    assertEquals(5, KingDistance.distance(A1, D6));
    assertEquals(5, KingDistance.distance(A1, F2));

  }
}
