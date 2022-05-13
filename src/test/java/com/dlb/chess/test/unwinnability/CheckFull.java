package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dlb.chess.test.pgntest.enums.UnwinnableFullResultTest;
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

}
