package com.dlb.chess.test.analysis;

import com.dlb.chess.analysis.Analyzer;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;

public class TestAnalyzer {

  public static void main(String[] args) throws Exception {
    printAnalysis("various_gundavaa_tari_2022.pgn");

    printAnalysis("wikipedia_threefold_2_5_korchnoi_portisch_1970_game_4.pgn");
    printAnalysis("wikipedia_threefold_2_3_capablanca_lasker_1921.pgn");
    printAnalysis("wikipedia_fifty_move_2_2_karpov_kasparov_1991.pgn");

  }

  private static void printAnalysis(String pgnFileName) throws Exception {
    final PgnTest pgnTest = PgnExpectedValue.findPgnFileBelongingPgnTestNotHavingTestValuesAlready(pgnFileName);
    System.out.println(pgnFileName);
    Analyzer.printAnalysis(pgnTest.getFolderPath(), pgnFileName);
  }

}
