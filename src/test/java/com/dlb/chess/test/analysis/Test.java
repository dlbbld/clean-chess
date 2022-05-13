package com.dlb.chess.test.analysis;

import com.dlb.chess.analysis.Analyzer;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;

public class Test {

  private static final String PGN_FILE_NAME = "Ob5ozxgG.pgn";

  public static void main(String[] args) throws Exception {
    printAnalysis(PGN_FILE_NAME);
  }

  private static void printAnalysis(String pgnFileName) throws Exception {
    final PgnTest pgnTest = PgnExpectedValue.findPgnFileBelongingPgnTestNotHavingTestValuesAlready(pgnFileName);
    Analyzer.printAnalysis(pgnTest.getFolderPath(), pgnFileName);
  }

}
