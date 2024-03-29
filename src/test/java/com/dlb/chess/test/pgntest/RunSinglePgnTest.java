package com.dlb.chess.test.pgntest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.analysis.model.SingleOutput;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.test.analysis.output.BasicOutput;
import com.dlb.chess.test.pgntest.enums.PgnTest;

class RunSinglePgnTest extends AbstractSinglePgnTest {

  private static final String PGN_FILE_NAME = "03_claim_for_own_move_incorrect_castling_right_lost_for_king_move.pgn";

  private static final Logger logger = NonNullWrapperCommon.getLogger(RunSinglePgnTest.class);

  @SuppressWarnings("static-method")
  @Test
  void testPgnFile() throws Exception {

    logger.info(PGN_FILE_NAME);

    final PgnTest pgnTest = PgnExpectedValue.findPgnFileBelongingPgnTestHavingTestValuesAlready(PGN_FILE_NAME);
    final SingleOutput testResult = BasicOutput.calculateTestResult(pgnTest.getFolderPath(), PGN_FILE_NAME);

    GeneralUtility.logLines(logger, testResult.output());

    assertTrue(runTestCase(PGN_FILE_NAME, testResult.analysis()));
  }

}
