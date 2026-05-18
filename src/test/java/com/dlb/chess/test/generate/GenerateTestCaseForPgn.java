package com.dlb.chess.test.generate;

import com.dlb.chess.test.pgn.setup.PgnTestCaseCatalog;
import com.dlb.chess.test.pgntest.enums.PgnTest;

public class GenerateTestCaseForPgn extends AbstractGenerateTestCaseForPgn {

  // we assume for convenience the file is in one of the provided folders
  private static final String ABC_XYZ_PGN_TTT_NAME = "01_m1_white_to_move.pgn";

  public static void main(String[] args) throws Exception {
    generateTestCaseForPgn(ABC_XYZ_PGN_TTT_NAME);
  }

  private static void generateTestCaseForPgn(String pgnName) throws Exception {
    final PgnTest pgnTest = PgnTestCaseCatalog.findPgnTestPgnNotListed(pgnName);
    final String testCaseValues = generate(pgnTest.getFolderPath(), pgnName);
    System.out.println(testCaseValues);
  }
}
