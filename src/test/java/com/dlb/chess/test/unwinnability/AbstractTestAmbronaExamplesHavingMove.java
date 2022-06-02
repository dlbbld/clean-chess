package com.dlb.chess.test.unwinnability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dlb.chess.test.pgntest.basic.AbstractTestBasic;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

abstract class AbstractTestAmbronaExamplesHavingMove extends AbstractTestBasic {

  static final Map<String, UnwinnableFull> map = new HashMap<>();

  static {

    map.put("ae_01.pgn", UnwinnableFull.UNWINNABLE);
    map.put("ae_02.pgn", UnwinnableFull.UNWINNABLE);
    map.put("ae_03.pgn", UnwinnableFull.UNWINNABLE);
    map.put("ae_04.pgn", UnwinnableFull.UNWINNABLE);
    map.put("ae_05.pgn", UnwinnableFull.UNWINNABLE);
    map.put("ae_06.pgn", UnwinnableFull.UNWINNABLE);
    map.put("ae_07.pgn", UnwinnableFull.UNWINNABLE);
    map.put("ae_08.pgn", UnwinnableFull.UNWINNABLE);
    map.put("ae_10.pgn", UnwinnableFull.UNWINNABLE);
    map.put("ae_11_FKr42ZRT.pgn", UnwinnableFull.UNWINNABLE);
    map.put("ae_12_bKHPqNEw.pgn", UnwinnableFull.UNWINNABLE);
    map.put("ae_13_OawUhnkq.pgn", UnwinnableFull.UNWINNABLE);
    map.put("ae_14.pgn", UnwinnableFull.UNWINNABLE);
    map.put("ae_15_QRvIMh3z.pgn", UnwinnableFull.UNWINNABLE);
    map.put("ae_16.pgn", UnwinnableFull.UNWINNABLE);
    map.put("ae_17.pgn", UnwinnableFull.UNWINNABLE);

    final List<String> pgnFileNameList = new ArrayList<>(map.keySet());

    checkTestFolder(pgnFileNameList, PgnTest.UNFAIR_AMBRONA_EXAMPLES);
  }

}
