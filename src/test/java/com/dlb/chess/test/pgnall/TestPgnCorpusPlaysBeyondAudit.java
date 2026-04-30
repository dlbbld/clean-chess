package com.dlb.chess.test.pgnall;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.parser.PgnCacheForStrictPgnParserTestCases;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;

/**
 * Audits the full PGN test corpus and reports every file whose recorded halfmove sequence cannot
 * be fully replayed under the strict-game invariant (i.e., it continues past a FIDE-automatic
 * termination, or its FEN tag's halfmove clock is above the 75-move threshold).
 *
 * <h2>Audit mode (current)</h2>
 *
 * The test does NOT fail when the corpus contains plays-beyond files. It logs each offender as
 * a {@code WARN} so the output can be used to drive the corpus cleanup task — relocating these
 * files out of the regular corpus into a dedicated plays-beyond fixtures folder.
 *
 * <h2>Sharpening (planned)</h2>
 *
 * Once the cleanup is complete, this test should be sharpened: replace the warning log with a
 * {@code fail()} on non-empty findings. Any future PGN added to the regular corpus that cannot
 * be replayed under the strict-game invariant will then surface immediately rather than
 * silently degrading coverage of other tests that use the corpus.
 *
 * <h2>Scope</h2>
 *
 * Iterates every category in {@link PgnTest} (including {@code LONGEST_POSSIBLE}) — the full
 * corpus, not the restricted/smoke subsets, because corpus debt anywhere is corpus debt.
 */
class TestPgnCorpusPlaysBeyondAudit {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestPgnCorpusPlaysBeyondAudit.class);

  @SuppressWarnings("static-method")
  @Test
  void auditCorpus() {
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
