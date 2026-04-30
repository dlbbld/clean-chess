package com.dlb.chess.test.pgnall;

import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.test.RestrictTestConstants;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.parser.PgnCacheForStrictPgnParserTestCases;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;

/**
 * Asserts the regular PGN test corpus contains no fixtures that play past a FIDE-automatic
 * termination — i.e. every file replays cleanly under the strict-game invariant. The class name
 * states the expected outcome ("not plays beyond"); the test reports any leftover.
 *
 * <h2>Audit mode (current)</h2>
 *
 * The test does NOT fail when the corpus still contains plays-beyond files. Each offender is
 * logged as a {@code WARN} so the output can be used to drive the corpus cleanup task —
 * relocating those files into the dedicated plays-beyond fixtures folder.
 *
 * <h2>Sharpening (planned)</h2>
 *
 * Once the cleanup is complete this test should be sharpened: replace the warning log with a
 * {@code fail()} on non-empty findings. Any future PGN added to the regular corpus that cannot
 * be replayed under the strict-game invariant will then surface immediately rather than
 * silently degrading coverage of other tests that use the corpus.
 *
 * <h2>Scope and runtime</h2>
 *
 * Iterates every category in {@link PgnTest} (including {@code LONGEST_POSSIBLE}) — the full
 * corpus, not the restricted/smoke subsets, because corpus debt anywhere is corpus debt. The
 * full sweep takes a few minutes; the test is gated behind
 * {@link RestrictTestConstants#IS_EXCLUDE_LONG_RUNNING_PGN_CORPUS_NOT_PLAYS_BEYOND_AUDIT} and
 * skipped via {@code assumeFalse} during routine runs. Flip the flag to {@code false} locally
 * to run.
 */
class TestPgnCorpusNotPlaysBeyondAudit {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestPgnCorpusNotPlaysBeyondAudit.class);

  @SuppressWarnings("static-method")
  @Test
  void auditCorpus() {
    assumeFalse(RestrictTestConstants.IS_EXCLUDE_LONG_RUNNING_PGN_CORPUS_NOT_PLAYS_BEYOND_AUDIT,
        "Long-running corpus audit excluded by IS_EXCLUDE_LONG_RUNNING_PGN_CORPUS_NOT_PLAYS_BEYOND_AUDIT");

    final List<String> playsBeyondFiles = new ArrayList<>();
    var totalFiles = 0;

    for (final PgnTest pgnTest : PgnTest.values()) {
      final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(pgnTest);
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        totalFiles++;
        final String pgnFileName = testCase.pgnFileName();
        try {
          PgnCacheForStrictPgnParserTestCases.getPgn(pgnTest.getFolderPath(), pgnFileName);
        } catch (final RuntimeException e) {
          // The strict parser surfaces both move-past-termination
          // (StrictPgnParserValidationException) and FEN-rejection
          // (FenAdvancedValidationException, wrapped) cases as runtime exceptions; collect
          // both. Catching the broader RuntimeException keeps the audit robust against future
          // exception subtypes that also indicate the same "cannot replay" outcome.
          playsBeyondFiles.add(pgnTest.name() + " / " + pgnFileName + "  —  " + e.getMessage());
        }
      }
    }

    if (playsBeyondFiles.isEmpty()) {
      logger.info("Corpus audit: all {} PGN files replay cleanly under the strict-game invariant.", totalFiles);
      return;
    }

    logger.warn("Corpus audit: {} of {} PGN files cannot be fully replayed under the strict-game invariant.",
        playsBeyondFiles.size(), totalFiles);
    logger.warn("These files should be relocated out of the regular corpus (see open task list).");
    for (final String entry : playsBeyondFiles) {
      logger.warn("  {}", entry);
    }
  }
}
