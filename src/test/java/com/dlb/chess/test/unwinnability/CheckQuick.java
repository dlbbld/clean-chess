package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.dlb.chess.test.winnable.enums.Winnable;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public class CheckQuick {

  static void check(Winnable winnable, UnwinnableQuick unwinnableQuick) {
    switch (winnable) {
      case NO:
        assertEquals(UnwinnableQuick.UNWINNABLE, unwinnableQuick);
        break;
      case YES:
        assertNotEquals(UnwinnableQuick.UNWINNABLE, unwinnableQuick);
        break;
      case UNKNOWN:
        break;
      default:
        throw new IllegalArgumentException();
    }

    switch (unwinnableQuick) {
      case WINNABLE:
        assertNotEquals(Winnable.NO, winnable);
        break;
      case UNWINNABLE:
        assertNotEquals(Winnable.YES, winnable);
        break;
      case POSSIBLY_WINNABLE:
        break;
      default:
        throw new IllegalArgumentException();
    }
  }

}
