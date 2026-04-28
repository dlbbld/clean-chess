package com.dlb.chess.test.pgntest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.analysis.Analyzer;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.FenAdvancedValidationException;
import com.dlb.chess.exceptions.InvalidMoveException;
import com.dlb.chess.pgn.parser.exceptions.LenientPgnParserValidationException;
import com.dlb.chess.pgn.parser.exceptions.StrictPgnParserValidationException;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;

/**
 * Repurposes the carefully crafted "plays beyond" PGN test corpus (files identified by
 * {@link PgnPlaysBeyondTermination#playsBeyondAutomaticTermination}) to verify the new role
 * those PGNs play under the strict-game invariant: importing them must fail with a clear
 * GAME_ALREADY_ENDED diagnostic, because the recorded halfmove sequence continues past a
 * FIDE-automatic termination (fivefold repetition, 75-move rule, mutual insufficient material,
 * or a FEN start position whose halfmove clock is already at the 75-move threshold).
 *
 * <p>The test does not assert the specific halfmove or termination reason for each PGN —
 * those vary across the corpus — only that strict-pipeline import refuses to fully replay any
 * of them. The full-replay tests (TestPgnExpectedAnalysis et al.) skip these same PGNs.
 */
class TestPgnPlaysBeyondTerminationRejection {

  private static final Logger logger = NonNullWrapperCommon
      .getLogger(TestPgnPlaysBeyondTerminationRejection.class);

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    var seenAtLeastOne = false;

    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getRestrictedTestListList()) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        final String pgnFileName = testCase.pgnFileName();
        if (!PgnPlaysBeyondTermination.playsBeyondAutomaticTermination(pgnFileName)) {
          continue;
        }
        seenAtLeastOne = true;
        logger.info(pgnFileName);

        var rejected = false;
        Throwable rejection = null;
        try {
          Analyzer.calculateAnalysis(testCaseList.pgnTest().getFolderPath(), pgnFileName);
        } catch (final InvalidMoveException | LenientPgnParserValidationException
            | StrictPgnParserValidationException | FenAdvancedValidationException e) {
          // InvalidMoveException / LenientPgnParserValidationException /
          // StrictPgnParserValidationException — strict-pipeline rejected a move past the
          // automatic termination during PGN replay.
          // FenAdvancedValidationException — the PGN's FEN tag itself has a halfmove clock
          // above the 75-move threshold and is rejected at parse time.
          rejected = true;
          rejection = e;
        }

        if (!rejected) {
          fail("PGN " + pgnFileName + " was expected to be rejected by strict-pipeline import "
              + "(it is marked as plays-beyond-automatic-termination) but the analyzer succeeded");
        }
        assertNotNull(rejection);
      }
    }

    if (!seenAtLeastOne) {
      fail("No plays-beyond PGNs were exercised — the test or the marker class is mis-configured");
    }
  }
}
