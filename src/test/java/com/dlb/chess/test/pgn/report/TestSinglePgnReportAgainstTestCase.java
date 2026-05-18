package com.dlb.chess.test.pgn.report;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.Nulls;
import com.dlb.chess.report.Reporter;
import com.dlb.chess.test.pgn.setup.PgnTestCaseCatalog;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.report.representation.BasicRepresentation;

class TestSinglePgnReportAgainstTestCase extends AbstractPgnReportTest {

  private static final String ABC_XYZ_PGN_TTT_NAME = "various_pranav_savic_2021_incomplete_speculative_from_last_capture.pgn";

  private static final Logger logger = Nulls.getLogger(TestSinglePgnReportAgainstTestCase.class);

  @SuppressWarnings("static-method")
  @Test
  void testPgn() throws Exception {

    logger.info(ABC_XYZ_PGN_TTT_NAME);

    final PgnTest pgnTest = PgnTestCaseCatalog.findPgnTestPgnNotListed(ABC_XYZ_PGN_TTT_NAME);
    final var expectedReports = Reporter.calculateReport(pgnTest.getFolderPath(), ABC_XYZ_PGN_TTT_NAME);
    final List<String> visualIndication = BasicRepresentation.calculateRepresentation(expectedReports, ABC_XYZ_PGN_TTT_NAME);

    for (final String line : visualIndication) {
      logger.info(line);
    }

    testReportAgainstTestCase(ABC_XYZ_PGN_TTT_NAME, expectedReports);

  }

}
