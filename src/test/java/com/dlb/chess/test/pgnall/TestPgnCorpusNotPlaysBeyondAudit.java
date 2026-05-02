package com.dlb.chess.test.pgnall;

import static org.junit.jupiter.api.Assertions.fail;
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
 * Asserts the regular PGN test corpus contains no fixtures that play past a FIDE-automatic termination — i.e. every
 * file replays cleanly under the strict-game invariant. The class name states the expected outcome ("not plays
 * beyond"); the test fails if any leftover is found.
 *
 * <h2>Scope and runtime</h2>
 *
 * <p>
 * Iterates every category in {@link PgnTest} (including {@code LONGEST_POSSIBLE}) — the full corpus, not the
 * restricted/smoke subsets, because corpus debt anywhere is corpus debt. The full sweep takes a few minutes; the test
 * is gated behind {@link RestrictTestConstants#IS_EXCLUDE_LONG_RUNNING_PGN_CORPUS_NOT_PLAYS_BEYOND_AUDIT} and skipped
 * via {@code assumeFalse} during routine runs. Flip the flag to {@code false} locally to run.
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

    final var report = new StringBuilder().append("Corpus audit: ").append(playsBeyondFiles.size())
        .append(" of ").append(totalFiles).append(" PGN files cannot be fully replayed under the strict-game ")
        .append("invariant. They must be relocated out of the regular corpus into ")
        .append("pgnParser/common/beyond/legacy/:\n");
    for (final String entry : playsBeyondFiles) {
      report.append("  ").append(entry).append('\n');
    }
    fail(report.toString());
  }
}
