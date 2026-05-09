package com.dlb.chess.test.pgn.analysis;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.report.Reporter;
import com.dlb.chess.test.pgnall.AbstractPgnTest;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.report.representation.BasicRepresentation;

class TestSinglePgnAnalysisAgainstTestCase extends AbstractPgnTest {

  private static final String PGN_FILE_NAME = "various_pranav_savic_2021_incomplete_speculative_from_last_capture.pgn";

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestSinglePgnAnalysisAgainstTestCase.class);

  @SuppressWarnings("static-method")
  @Test
  void testPgnFile() throws Exception {

    logger.info(PGN_FILE_NAME);

    final PgnTest pgnTest = PgnExpectedValue.findPgnTestPgnNotListed(PGN_FILE_NAME);
    final var expectedAnalysis = Reporter.calculateReport(pgnTest.getFolderPath(), PGN_FILE_NAME);
    final List<String> visualIndication = BasicRepresentation.calculateRepresentation(expectedAnalysis, PGN_FILE_NAME);

    GeneralUtility.logLines(logger, visualIndication);

    testAnalysisAgainstTestCase(PGN_FILE_NAME, expectedAnalysis);
  }

}
