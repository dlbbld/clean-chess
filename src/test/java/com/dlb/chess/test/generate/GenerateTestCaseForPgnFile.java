package com.dlb.chess.test.generate;

import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;

public class GenerateTestCaseForPgnFile extends AbstractGenerateTestCaseForPgn {

  // we assume for convenience the file is in one of the provided folders
  private static final String PGN_FILE_NAME = "test_lichess_V7eJ1RR9_helpmate.pgn";

  public static void main(String[] args) throws Exception {
    generateTestCaseForPgnFile(PGN_FILE_NAME);
  }

  private static void generateTestCaseForPgnFile(String pgnFileName) throws Exception {
    final PgnTest pgnTest = CreatePgnTestCases.findPgnTestPgnNotListed(pgnFileName);
    final String testCaseValues = generate(pgnTest.getFolderPath(), pgnFileName);
    System.out.println(testCaseValues);
  }
}
