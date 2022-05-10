package com.dlb.chess.test.common.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.common.utility.RandomUtility;

class TestBasicUtility {

  @SuppressWarnings("static-method")
  @Test
  void test() {

    check("dfdujh", "[1-9]+", "dfdujh");

    check("1dfdujh", "[1-9]+", "1dfdujh");

    check("dfdujh2", "[1-9]+", "dfdujh", "2");

    check("5ab1c2d", "[1-9]+", "5ab", "1c", "2d");

    check("5ab1c2d3", "[1-9]+", "5ab", "1c", "2d", "3");

    check("gt5ab1c2d", "[1-9]+", "gt", "5ab", "1c", "2d");

    check("gt5ab1c2d9", "[1-9]+", "gt", "5ab", "1c", "2d", "9");

    final var regExpMoveNumber = "[1-9]{1}[0-9]*\\. ";

    check("1. e2", regExpMoveNumber, "1. e2");
    check("1. e2 ", regExpMoveNumber, "1. e2 ");
    check("1. e2 e4", regExpMoveNumber, "1. e2 e4");
    check("1. e2 e4 ", regExpMoveNumber, "1. e2 e4 ");
    check("1. e2 e4 2. d4", regExpMoveNumber, "1. e2 e4 ", "2. d4");
    check("1. e2 e4 2. d4 ", regExpMoveNumber, "1. e2 e4 ", "2. d4 ");
    check("1. e2 e4 2. d4 d5", regExpMoveNumber, "1. e2 e4 ", "2. d4 d5");
    check("1. e2 e4 2. d4 d5 ", regExpMoveNumber, "1. e2 e4 ", "2. d4 d5 ");

    check("1. e2 e4 2. d4 d5 3. Sc3", regExpMoveNumber, "1. e2 e4 ", "2. d4 d5 ", "3. Sc3");
    check("1. e2 e4 2. d4 d5 3. Sc3 Sc6", regExpMoveNumber, "1. e2 e4 ", "2. d4 d5 ", "3. Sc3 Sc6");

  }

  private static void check(String string, String regExp, String... expectedListArray) {
    @SuppressWarnings("null") final List<String> expectedList = Arrays.asList(expectedListArray);
    final List<String> actualList = BasicUtility.splitStringRetainingDelimiter(string, regExp);
    assertEquals(expectedList, actualList);
  }

  @SuppressWarnings("static-method")
  @Test
  void testRandom() {
    checkIntervalException(0, -1);
    checkIntervalException(1, 0);
    checkIntervalException(2, 0);
    checkIntervalException(2, -1);
    checkIntervalException(99, 97);
    checkIntervalException(99, 0);
    checkIntervalException(99, -97);

    // do this 1000 times
    for (var i = 1; i <= 1000; i++) {
      checkInterval(0, 0);
      checkInterval(0, 1);
      checkInterval(1, 1);
      checkInterval(-1, 0);
      checkInterval(1, 1);
      checkInterval(1, 2);
      checkInterval(1, 3);
      checkInterval(1, 10);
      checkInterval(1, 99);
      checkInterval(1, 100);
      checkInterval(0, 1);
      checkInterval(0, 2);
      checkInterval(0, 3);
      checkInterval(0, 10);
      checkInterval(0, 99);
      checkInterval(0, 100);
    }

    // check we hit all number from 0 to 10
    // with assumption it happens before Integer.MAX_VALUE
    for (var i = 0; i <= 10; i++) {
      var isFound = false;
      for (var repeat = 1; repeat <= Integer.MAX_VALUE; repeat++) {
        final var generated = RandomUtility.calculateRandomNumber(0, 10);
        if (generated == i) {
          isFound = true;
          break;
        }
      }
      assertTrue(isFound);
    }
  }

  private static void checkInterval(int leftBoundInclusive, int rightBoundInclusive) {
    final var value = RandomUtility.calculateRandomNumber(leftBoundInclusive, rightBoundInclusive);
    assertTrue(leftBoundInclusive <= value);
    assertTrue(value <= rightBoundInclusive);
  }

  private static void checkIntervalException(int leftBoundInclusive, int rightBoundInclusive) {
    var isException = false;
    try {
      RandomUtility.calculateRandomNumber(leftBoundInclusive, rightBoundInclusive);
    } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
      isException = true;
    }
    assertTrue(isException);
  }

  @SuppressWarnings("static-method")
  @Test
  void testWrappedLines() {
    checkWrappedLines("a", 1, "a");
    checkWrappedLines("a", 2, "a");
    checkWrappedLines("a", 10, "a");

    checkWrappedLines("aaa", 1, "aaa");
    checkWrappedLines("aaa", 2, "aaa");
    checkWrappedLines("aaaaaaaaaaaaaaaaaaaaaaa", 10, "aaaaaaaaaaaaaaaaaaaaaaa");

    checkWrappedLines("1", 3, "1");
    checkWrappedLines("12", 3, "12");
    checkWrappedLines("123", 3, "123");
    checkWrappedLines("123 4", 3, "123", "4");
    checkWrappedLines("123 45", 3, "123", "45");
    checkWrappedLines("123 456", 3, "123", "456");
    checkWrappedLines("123 456 7", 3, "123", "456", "7");

    checkWrappedLines("aa", 5, "aa");
    checkWrappedLines("aa aa", 5, "aa aa");
    checkWrappedLines("aa aa aa", 5, "aa aa", "aa");
    checkWrappedLines("aa aa aa a", 5, "aa aa", "aa a");
    checkWrappedLines("aa aa aa aa", 5, "aa aa", "aa aa");
    checkWrappedLines("aa aa aa aa aa", 5, "aa aa", "aa aa", "aa");

  }

  private static void checkWrappedLines(String line, int lineLength, String... expectedLineArray) {
    @SuppressWarnings("null") final List<String> expectedResult = Arrays.asList(expectedLineArray);
    final List<String> actualResult = BasicUtility.calculateWrappedLines(line, lineLength);
    assertEquals(expectedResult, actualResult);
  }
}