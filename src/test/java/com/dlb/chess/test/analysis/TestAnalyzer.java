package com.dlb.chess.test.analysis;

import com.dlb.chess.analysis.Analyzer;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;

public class TestAnalyzer {

  public static void main(String[] args) throws Exception {
    printAnalysis("05_claim_for_own_move_correct_but_makes_move_on_board.pgn");

  }

  private static void printAnalysis(String pgnFileName) throws Exception {
    final PgnTest pgnTest = PgnExpectedValue.findPgnFileBelongingPgnTestNotHavingTestValuesAlready(pgnFileName);
    System.out.println(pgnFileName);
    Analyzer.printAnalysis(pgnTest.getFolderPath(), pgnFileName);
  }

}
