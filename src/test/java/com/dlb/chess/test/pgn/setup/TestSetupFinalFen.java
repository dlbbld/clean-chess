package com.dlb.chess.test.pgn.setup;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.enums.PgnTest;

/**
 * On-demand audit that verifies every {@link PgnFileTestCase}'s cached {@code fen} matches the position actually
 * reached by replaying its PGN. Disabled by default because it replays the full corpus.
 *
 * <p>
 * Run with:
 *
 * <pre>{@code
 * mvn -q test -Dtest=TestFenAudit -Dfen.audit=true
 * }</pre>
 *
 * <p>
 * The audit replays each PGN with dead-position auto-detection disabled (the same mode used when the fixture was
 * originally captured) so fixtures whose final position is intentionally dead still produce a deterministic FEN. Every
 * mismatch is reported on the test failure message: PGN filename, the {@link PgnTest} it belongs to, the expected
 * (cached) FEN, and the actual (replayed) FEN. Apply fixes by editing the matching literal in
 * {@code CreatePgnTestCases}.
 */
class TestSetupFinalFen {

  private static final Logger logger = Nulls.getLogger(TestSetupFinalFen.class);

  @SuppressWarnings("static-method")
  @Test
  @EnabledIfSystemProperty(named = "fen.audit", matches = "true")
  void auditCachedFenAgainstPgnReplay() {
    final List<String> mismatches = new ArrayList<>();
    final List<String> errors = new ArrayList<>();
    var totalFixtures = 0;

    for (final PgnTest pgnTest : PgnTest.values()) {
      final PgnFileTestCaseList testCaseList = CreatePgnTestCases.getTestList(pgnTest);
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        totalFixtures++;
        try {
          final Board board = testCase.game(pgnTest);
          final String actualFen = board.getFen();
          final String expectedFen = testCase.finalFen();
          if (!actualFen.equals(expectedFen)) {
            mismatches.add(String.format("%s (%s)%n  expected: %s%n  actual:   %s", testCase.pgnFileName(), pgnTest,
                expectedFen, actualFen));
          }
        } catch (RuntimeException e) {
          // PGN missing on disk, parse error, or illegal move: surface alongside the FEN mismatches so the
          // operator gets the full picture in one pass instead of crashing on the first bad fixture.
          errors.add(String.format("%s (%s): %s", testCase.pgnFileName(), pgnTest, e.getMessage()));
        }
      }
    }

    logger.info("Audited {} fixtures across {} PgnTest folders; {} mismatches; {} replay errors.", totalFixtures,
        PgnTest.values().length, mismatches.size(), errors.size());

    if (!mismatches.isEmpty() || !errors.isEmpty()) {
      final StringBuilder sb = new StringBuilder();
      if (!mismatches.isEmpty()) {
        sb.append(String.format("%d fixture(s) have a stale cached FEN:%n%n", mismatches.size()));
        sb.append(String.join("\n\n", mismatches));
      }
      if (!errors.isEmpty()) {
        if (sb.length() > 0) {
          sb.append("\n\n");
        }
        sb.append(String.format("%d fixture(s) could not be replayed:%n%n", errors.size()));
        sb.append(String.join("\n", errors));
      }
      fail(sb.toString());
    }
  }
}
