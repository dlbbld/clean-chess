package com.dlb.chess.test.pgn.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;

/**
 * Shared body for the strict and lenient FEN-initialization parser tests.
 *
 * <p>Iterates every PGN under the three {@code PARSER_FROM_FEN*} buckets and asserts:
 *
 * <ol>
 * <li>the parser accepts the file (no exception),</li>
 * <li>replaying the parsed halfmove sequence on a {@link Board} starting from the parsed
 * {@code startFen} reaches the FEN recorded as the registered test case's {@code fen()}.</li>
 * </ol>
 *
 * <p>The second step is the actual parser-mechanics check: the registered FEN encodes the
 * expected final halfmove clock and full-move number, so divergence pinpoints either a bad
 * FEN-tag → board-state initialization or a bad clock progression through subsequent moves.
 *
 * <p>Subclasses inject the parser by passing
 * {@code (folder, fileName) -> StrictPgnParser.parse(folder, fileName)} (or the lenient
 * variant) to {@link #runForBuckets}.
 *
 * <p>Logs each fixture's name as it runs so a failure mid-iteration shows progress.
 */
abstract class AbstractTestPgnParserHalfMoveClockFromFen {

  private static final List<PgnTest> BUCKETS = List.of(PgnTest.PARSER_FROM_FEN, PgnTest.PARSER_FROM_FEN_YAWN_WHITE,
      PgnTest.PARSER_FROM_FEN_YAWN_BLACK);

  protected static void runForBuckets(BiFunction<java.nio.file.Path, String, PgnFile> parse, Logger logger) {
    final List<String> failures = new ArrayList<>();
    var totalFixtures = 0;

    for (final PgnTest bucket : BUCKETS) {
      final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(bucket);
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        totalFixtures++;
        final String pgnFileName = testCase.pgnFileName();
        logger.info(pgnFileName);

        final PgnFile pgnFile = parse.apply(bucket.getFolderPath(), pgnFileName);

        final ApiBoard board = new Board(pgnFile.startFen());
        for (final PgnHalfMove halfMove : pgnFile.halfMoveList()) {
          board.performMove(halfMove.san());
        }

        try {
          assertEquals(testCase.fen(), board.getFen(),
              () -> bucket + " / " + pgnFileName + " — final FEN mismatch (halfmove-clock or move-number drift)");
        } catch (final AssertionError e) {
          failures.add(e.getMessage());
        }
      }
    }

    if (totalFixtures == 0) {
      fail("No fixtures iterated — bucket wiring is broken");
    }
    if (!failures.isEmpty()) {
      final StringBuilder report = new StringBuilder().append(failures.size()).append(" of ").append(totalFixtures)
          .append(" PARSER_FROM_FEN* fixtures produced an unexpected final FEN:\n");
      for (final String f : failures) {
        report.append("  ").append(f).append('\n');
      }
      fail(report.toString());
    }
  }
}
