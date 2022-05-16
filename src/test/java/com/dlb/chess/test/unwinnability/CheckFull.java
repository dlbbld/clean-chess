package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dlb.chess.test.pgntest.enums.UnwinnableFullResultTest;
import com.dlb.chess.test.winnable.enums.Winnable;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

public class CheckFull {

  static void check(UnwinnableFullResultTest unwinnableFullResultTest, UnwinnableFull unwinnableFull) {
    switch (unwinnableFullResultTest) {
      case UNWINNABLE:
      case UNWINNABLE_NOT_QUICK:
        assertEquals(UnwinnableFull.UNWINNABLE, unwinnableFull);
        break;
      case WINNABLE:
        assertEquals(UnwinnableFull.WINNABLE, unwinnableFull);
        break;
      default:
        throw new IllegalArgumentException();
    }
  }

  static void check(Winnable winnable, UnwinnableFull unwinnableFull) {
    switch (winnable) {
      case NO:
        assertEquals(UnwinnableFull.UNWINNABLE, unwinnableFull);
        break;
      case YES:
        assertNotEquals(UnwinnableFull.UNWINNABLE, unwinnableFull);
        break;
      case UNKNOWN:
        break;
      default:
        throw new IllegalArgumentException();
    }

    switch (unwinnableFull) {
      case WINNABLE:
        assertNotEquals(Winnable.NO, winnable);
        break;
      case UNWINNABLE:
        final var isIncomplete = winnable == Winnable.NO || winnable == Winnable.UNKNOWN;
        assertTrue(isIncomplete);
        break;
      default:
        throw new IllegalArgumentException();
    }
  }
}
