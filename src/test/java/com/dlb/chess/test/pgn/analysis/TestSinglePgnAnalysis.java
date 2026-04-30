package com.dlb.chess.test.pgn.analysis;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.analysis.Analyzer;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.test.analysis.output.BasicOutput;
import com.dlb.chess.test.pgnall.AbstractPgnTest;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;

class TestSinglePgnAnalysis extends AbstractPgnTest {

  private static final String PGN_FILE_NAME = "03_claim_for_own_move_incorrect_castling_right_lost_for_king_move.pgn";

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestSinglePgnAnalysis.class);

  @SuppressWarnings("static-method")
  @Test
  void testPgnFile() throws Exception {

    logger.info(PGN_FILE_NAME);

    final PgnTest pgnTest = PgnExpectedValue.findPgnTestPgnNotListed(PGN_FILE_NAME);
    final var expectedAnalysis = Analyzer.calculateAnalysis(pgnTest.getFolderPath(), PGN_FILE_NAME);
    final List<String> visualIndication = BasicOutput.calculateVisualIndication(expectedAnalysis, PGN_FILE_NAME);

    GeneralUtility.logLines(logger, visualIndication);

    testAnalysisAgainstTestCase(PGN_FILE_NAME, expectedAnalysis);
  }

}
