package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dlb.chess.test.winnable.enums.Winnable;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

public class CheckFull {

  public static void check(Winnable winnable, UnwinnableFull unwinnableFull) {
    switch (winnable) {
      case NO:
        assertEquals(UnwinnableFull.UNWINNABLE, unwinnableFull);
        break;
      case YES:
        assertNotEquals(UnwinnableFull.WINNABLE, unwinnableFull);
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
      case UNDETERMINED:
        assertEquals(Winnable.UNKNOWN, winnable);
        break;
      default:
        throw new IllegalArgumentException();
    }
  }
}
