package com.dlb.chess.test.pgntest;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.analysis.model.SingleOutput;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.test.analysis.output.BasicOutput;
import com.dlb.chess.test.pgntest.enums.PgnTest;

public class TestSinglePgn extends AbstractSinglePgnTest {

  private static final String PGN_FILE_NAME = "grischuk_mamedyarov_2017.pgn";

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestSinglePgn.class);

  @SuppressWarnings("static-method")
  @Test
  void testPgnFiles() throws Exception {

    final PgnTest pgnTest = PgnExpectedValue.findPgnFileBelongingPgnTestHavingTestValuesAlready(PGN_FILE_NAME);
    final SingleOutput testResult = BasicOutput.calculateTestResult(pgnTest.getFolderPath(), PGN_FILE_NAME);

    GeneralUtility.logLines(logger, testResult.output());

    runTestCase(PGN_FILE_NAME, testResult.analysis());
  }

}
