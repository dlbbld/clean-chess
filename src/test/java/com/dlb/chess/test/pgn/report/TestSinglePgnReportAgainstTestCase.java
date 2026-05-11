package com.dlb.chess.test.pgn.report;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.report.Reporter;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.report.representation.BasicRepresentation;

class TestSinglePgnReportAgainstTestCase extends AbstractPgnReportTest {

  private static final String PGN_FILE_NAME = "various_pranav_savic_2021_incomplete_speculative_from_last_capture.pgn";

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestSinglePgnReportAgainstTestCase.class);

  @SuppressWarnings("static-method")
  @Test
  void testPgnFile() throws Exception {

    logger.info(PGN_FILE_NAME);

    final PgnTest pgnTest = CreatePgnTestCases.findPgnTestPgnNotListed(PGN_FILE_NAME);
    final var expectedReports = Reporter.calculateReport(pgnTest.getFolderPath(), PGN_FILE_NAME);
    final List<String> visualIndication = BasicRepresentation.calculateRepresentation(expectedReports, PGN_FILE_NAME);

    for (final String line : visualIndication) {
      logger.info(line);
    }

    testReportAgainstTestCase(PGN_FILE_NAME, expectedReports);
  }

}
