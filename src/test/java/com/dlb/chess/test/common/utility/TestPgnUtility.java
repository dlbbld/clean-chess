package com.dlb.chess.test.common.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.utility.PgnUtility;

class TestPgnUtility {

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
    final List<String> actualResult = PgnUtility.calculateWrappedLines(line, lineLength);
    assertEquals(expectedResult, actualResult);
  }

}