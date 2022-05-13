package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dlb.chess.test.pgntest.enums.UnwinnableFullResultTest;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public class CheckQuick {

  static void check(UnwinnableFullResultTest unwinnableFullResultTest, UnwinnableQuick unwinnableQuick) {
    switch (unwinnableFullResultTest) {
      case UNWINNABLE:
        assertEquals(UnwinnableQuick.UNWINNABLE, unwinnableQuick);
        break;
      case UNWINNABLE_NOT_QUICK:
        assertEquals(UnwinnableQuick.POSSIBLY_WINNABLE, unwinnableQuick);
        break;
      case WINNABLE:
        final var isIncomplete = unwinnableQuick == UnwinnableQuick.WINNABLE
            || unwinnableQuick == UnwinnableQuick.POSSIBLY_WINNABLE;
        assertTrue(isIncomplete);
        break;
      default:
        throw new IllegalArgumentException();
    }
  }

}
