package com.dlb.chess.test.apicarlos.bugs.fixed;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.test.apicarlos.NonNullWrapperApiCarlos;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.pgn.PgnHolder;

class TestApiCarlosPerformance {

  private static final double LOAD_PGN_DURATION_MAX_MILLISECONDS = 1000.0;

  private static final double LOAD_MOVE_TEXT_DURATION_MAX_MILLISECONDS = 500.0;

  private static final double PER_HALF_MOVE_MAX_MILLISECONDS = 0.5;

  private static List<PgnTest> PGN_TEST_LIST = NonNullWrapperCommon.asList(PgnTest.LONG, PgnTest.LONGEST_POSSIBLE);

  @SuppressWarnings("static-method")
  @Test
  void testPerformance() throws Exception {
    for (final PgnTest pgnTest : PGN_TEST_LIST) {
      final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(pgnTest);
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        final String pgnFileName = testCase.pgnFileName();
        System.out.printf("Processing %s%n", pgnFileName);
        final PgnHolder pgn = new PgnHolder(FileUtility.calculateFilePath(pgnTest.getFolderPath(), pgnFileName));

        final var millisecondsBeforeLoadPgn = System.currentTimeMillis();
        pgn.loadPgn();
        final double millisecondDurationLoadPgn = System.currentTimeMillis() - millisecondsBeforeLoadPgn;

        assertTrue(millisecondDurationLoadPgn < LOAD_PGN_DURATION_MAX_MILLISECONDS);

        System.out.printf("loadPgn duration seconds: %f%n", millisecondDurationLoadPgn / 1000);

        final var game = NonNullWrapperCommon.getFirst(NonNullWrapperApiCarlos.getGames(pgn));
        final var millisecondsBeforeLoadMoveText = System.currentTimeMillis();
        game.loadMoveText();
        final var millisecondDurationLoadMoveText = System.currentTimeMillis() - millisecondsBeforeLoadMoveText;
        assertTrue(millisecondDurationLoadMoveText < LOAD_MOVE_TEXT_DURATION_MAX_MILLISECONDS);

        System.out.printf("loadMoveText duration seconds: %f%n", millisecondDurationLoadPgn / 1000);

        final var moves = game.getHalfMoves();
        final var halfMoves = moves.size();
        System.out.printf("Half-moves to perform: %d%n", halfMoves);
        final Board board = new Board();
        final var millisecondsBeforePlayingMoves = System.currentTimeMillis();
        for (final Move move : moves) {
          board.doMove(move);
        }
        final var millisecondDurationPlayingMoves = System.currentTimeMillis() - millisecondsBeforePlayingMoves;

        final double perHalfMoveMilliseconds = millisecondDurationPlayingMoves / halfMoves;

        assertTrue(perHalfMoveMilliseconds < PER_HALF_MOVE_MAX_MILLISECONDS);
        System.out.printf("Milliseconds per half-move: %f%n", millisecondDurationLoadPgn / halfMoves);
        System.out.println();
      }
    }

  }

}
