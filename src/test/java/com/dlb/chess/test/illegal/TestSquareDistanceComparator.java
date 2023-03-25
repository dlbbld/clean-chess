package com.dlb.chess.test.illegal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.illegal.model.SquareDistance;

class TestSquareDistanceComparator {

  @SuppressWarnings("static-method")
  @Test
  void testDistance() throws Exception {

    checkDistance(0.0, Square.A1, Square.A1);
    checkDistance(1.0, Square.A1, Square.A2);
    checkDistance(1.0, Square.A1, Square.B1);
    checkDistance(2.0, Square.A1, Square.A3);
    checkDistance(2.0, Square.A1, Square.C1);
    checkDistance(7.0, Square.A1, Square.A8);
    checkDistance(7.0, Square.A1, Square.H1);

    checkDistance(Math.sqrt(2.0), Square.A1, Square.B2);
    checkDistance(Math.sqrt(8.0), Square.A1, Square.C3);
    checkDistance(Math.sqrt(18.0), Square.A1, Square.D4);
    checkDistance(Math.sqrt(32.0), Square.A1, Square.E5);
    checkDistance(Math.sqrt(50.0), Square.A1, Square.F6);
    checkDistance(Math.sqrt(72.0), Square.A1, Square.G7);
    checkDistance(Math.sqrt(98.0), Square.A1, Square.H8);

    checkDistance(Math.sqrt(5.0), Square.A1, Square.C2);
    checkDistance(Math.sqrt(10.0), Square.A1, Square.D2);

    checkDistance(Math.sqrt(5.0), Square.D4, Square.F5);
    checkDistance(Math.sqrt(10.0), Square.D4, Square.G5);

  }

  private static void checkDistance(double expected, Square fromSquare, Square toSquare) {
    final var actual1 = SquareDistance.calculateDistance(fromSquare, toSquare);
    assertEquals(expected, actual1);

    final var actual2 = SquareDistance.calculateDistance(toSquare, fromSquare);
    assertEquals(expected, actual2);
  }

  @SuppressWarnings({ "static-method", "null" })
  @Test
  void testSorting() throws Exception {
    {
      final List<Square> expectedList = Arrays.asList(Square.C3, Square.A1, Square.D8);
      final List<Square> actualList = Arrays.asList(Square.A1, Square.C3, Square.D8);
      checkSorting(Square.C3, expectedList, actualList);
    }

    {
      final List<Square> expectedList = Arrays.asList(Square.C3, Square.D8, Square.A1);
      final List<Square> actualList = Arrays.asList(Square.A1, Square.C3, Square.D8);
      checkSorting(Square.D4, expectedList, actualList);
    }

    {
      final List<Square> expectedList = Arrays.asList(Square.D8, Square.C3, Square.A1);
      final List<Square> actualList = Arrays.asList(Square.A1, Square.C3, Square.D8);
      checkSorting(Square.D7, expectedList, actualList);
    }
  }

  private static void checkSorting(Square fromSquare, List<Square> expectedList, List<Square> actualList) {
    final List<Square> actualListSorted = new ArrayList<>(actualList);
    Collections.sort(actualListSorted, new SquareDistance(fromSquare));

    assertEquals(expectedList, actualListSorted);
  }
}
