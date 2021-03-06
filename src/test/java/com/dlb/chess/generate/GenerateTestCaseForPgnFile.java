package com.dlb.chess.generate;

import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;

public class GenerateTestCaseForPgnFile extends AbstractGenerateTestCaseForPgn {

  // we assume for convenience the file is in one of the provided folders
  private static final String PGN_FILE_NAME = "threefold_10_1_multiple_threefolds_end_with_last.pgn";

  public static void main(String[] args) throws Exception {
    generateTestCaseForPgnFile(PGN_FILE_NAME);
  }

  private static void generateTestCaseForPgnFile(String pgnFileName) throws Exception {
    final PgnTest pgnTest = PgnExpectedValue.findPgnFileBelongingPgnTestNotHavingTestValuesAlready(pgnFileName);
    final String testCaseValues = generate(pgnTest.getFolderPath(), pgnFileName);
    System.out.println(testCaseValues);
  }
}
