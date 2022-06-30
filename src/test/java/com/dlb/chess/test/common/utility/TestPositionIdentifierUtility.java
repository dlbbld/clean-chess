package com.dlb.chess.test.common.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.utility.PositionIdentifierUtility;

public class TestPositionIdentifierUtility {

  @Test
  @SuppressWarnings("static-method")
  void testRepresentation() {
    checkRepresentation(0, 10, 0);
    checkRepresentation(1, 10, 1);
    checkRepresentation(10, 10, 1, 0);
    checkRepresentation(11, 10, 1, 1);
    checkRepresentation(111, 10, 1, 1, 1);
    checkRepresentation(1111, 10, 1, 1, 1, 1);

    checkRepresentation(0, 5, 0);
    checkRepresentation(1, 5, 1);
    checkRepresentation(5, 5, 1, 0);
    checkRepresentation(6, 5, 1, 1);
    checkRepresentation(10, 5, 2, 0);
    checkRepresentation(11, 5, 2, 1);
    checkRepresentation(37, 5, 1, 2, 2);

    checkRepresentation(0, 25, 0);
    checkRepresentation(1, 25, 1);
    checkRepresentation(2, 25, 2);

    checkRepresentation(23, 25, 23);
    checkRepresentation(24, 25, 24);
    checkRepresentation(25, 25, 1, 0);
    checkRepresentation(26, 25, 1, 1);
    checkRepresentation(27, 25, 1, 2);

    checkRepresentation(0, 26, 0);
    checkRepresentation(1, 26, 1);
    checkRepresentation(2, 26, 2);
    checkRepresentation(25, 26, 25);

    checkRepresentation(26, 26, 1, 0);
    checkRepresentation(27, 26, 1, 1);
    checkRepresentation(28, 26, 1, 2);

    checkRepresentation(49, 26, 1, 23);
    checkRepresentation(50, 26, 1, 24);
    checkRepresentation(51, 26, 1, 25);

    checkRepresentation(52, 26, 2, 0);
    checkRepresentation(53, 26, 2, 1);
    checkRepresentation(54, 26, 2, 2);

    checkRepresentation(1, 27, 1);
    checkRepresentation(2, 27, 2);
    checkRepresentation(25, 27, 25);

    checkRepresentation(26, 27, 26);
    checkRepresentation(27, 27, 1, 0);
    checkRepresentation(28, 27, 1, 1);

    checkRepresentation(53, 27, 1, 26);

  }

  private static void checkRepresentation(int number, int base, int... expectedArray) {

    final List<Integer> expected = new ArrayList<>();
    for (final int entry : expectedArray) {
      expected.add(entry);
    }
    final List<Integer> actual = PositionIdentifierUtility.calculateRepresentation(number, base);

    assertEquals(expected, actual);
  }

  @Test
  @SuppressWarnings("static-method")
  void testIdentifer() {
    checkIdentifer("A", 1);
    checkIdentifer("B", 2);
    checkIdentifer("C", 3);
    checkIdentifer("X", 24);
    checkIdentifer("Y", 25);
    checkIdentifer("Z", 26);

    checkIdentifer("AA", 27);
    checkIdentifer("AB", 28);
    checkIdentifer("AC", 29);
    checkIdentifer("AX", 50);
    checkIdentifer("AY", 51);
    checkIdentifer("AZ", 52);

    checkIdentifer("BA", 53);

  }

  private static void checkIdentifer(String expected, int number) {
    assertEquals(expected, PositionIdentifierUtility.calculateIdentifier(number));
  }
}
