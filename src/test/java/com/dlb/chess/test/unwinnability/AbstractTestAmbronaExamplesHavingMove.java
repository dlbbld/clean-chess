package com.dlb.chess.test.unwinnability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dlb.chess.test.pgntest.basic.AbstractTestBasic;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.pgntest.enums.UnwinnableFullResultTest;

abstract class AbstractTestAmbronaExamplesHavingMove extends AbstractTestBasic {

  static final Map<String, UnwinnableFullResultTest> map = new HashMap<>();

  static {

    map.put("ae_01.pgn", UnwinnableFullResultTest.UNWINNABLE);
    map.put("ae_02.pgn", UnwinnableFullResultTest.UNWINNABLE);
    map.put("ae_03.pgn", UnwinnableFullResultTest.UNWINNABLE);
    map.put("ae_04.pgn", UnwinnableFullResultTest.UNWINNABLE);
    map.put("ae_05.pgn", UnwinnableFullResultTest.UNWINNABLE);
    map.put("ae_06.pgn", UnwinnableFullResultTest.UNWINNABLE);
    map.put("ae_07.pgn", UnwinnableFullResultTest.UNWINNABLE);
    map.put("ae_08.pgn", UnwinnableFullResultTest.UNWINNABLE);
    map.put("ae_10.pgn", UnwinnableFullResultTest.UNWINNABLE);
    map.put("ae_11_FKr42ZRT.pgn", UnwinnableFullResultTest.UNWINNABLE);
    map.put("ae_12_bKHPqNEw.pgn", UnwinnableFullResultTest.UNWINNABLE);
    map.put("ae_13_OawUhnkq.pgn", UnwinnableFullResultTest.UNWINNABLE);
    map.put("ae_14.pgn", UnwinnableFullResultTest.UNWINNABLE);
    map.put("ae_15_QRvIMh3z.pgn", UnwinnableFullResultTest.UNWINNABLE);
    map.put("ae_16.pgn", UnwinnableFullResultTest.UNWINNABLE);
    map.put("ae_17.pgn", UnwinnableFullResultTest.UNWINNABLE);

    final List<String> pgnFileNameList = new ArrayList<>(map.keySet());

    checkTestFolder(pgnFileNameList, PgnTest.UNFAIR_AMBRONA_EXAMPLES);
  }

}
