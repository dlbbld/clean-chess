package com.dlb.chess.test.apicarlos.bugs;

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

  private static List<PgnTest> PGN_TEST_LIST = NonNullWrapperCommon.asList(PgnTest.LONG, PgnTest.LONGEST_POSSIBLE);

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    runTestDetailed();
    runTestNonDetailed();
  }

  private static void runTestDetailed() throws Exception {
    for (final PgnTest pgnTest : PGN_TEST_LIST) {
      final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(pgnTest);
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        final String pgnFileName = testCase.pgnFileName();
        System.out.printf("Processing %s%n", pgnFileName);
        final PgnHolder pgn = new PgnHolder(FileUtility.calculateFilePath(pgnTest.getFolderPath(), pgnFileName));

        var millisecondsBefore = System.currentTimeMillis();
        pgn.loadPgn();
        double millisecondDuration = System.currentTimeMillis() - millisecondsBefore;
        System.out.printf("loadPgn duration seconds: %f%n", millisecondDuration / 1000);

        final var game = NonNullWrapperCommon.getFirst(NonNullWrapperApiCarlos.getGames(pgn));
        millisecondsBefore = System.currentTimeMillis();
        game.loadMoveText();
        millisecondDuration = System.currentTimeMillis() - millisecondsBefore;
        System.out.printf("loadMoveText duration seconds: %f%n", millisecondDuration / 1000);

        final var moves = game.getHalfMoves();
        final var halfMoves = moves.size();
        System.out.printf("Half-moves to perform: %d%n", halfMoves);
        final Board board = new Board();
        millisecondsBefore = System.currentTimeMillis();
        for (final Move move : moves) {
          board.doMove(move);
        }
        millisecondDuration = System.currentTimeMillis() - millisecondsBefore;
        System.out.printf("Milliseconds per half-move: %f%n", millisecondDuration / halfMoves);
        System.out.println();
      }
    }

  }

  private static void runTestNonDetailed() throws Exception {
    for (final PgnTest pgnTest : PGN_TEST_LIST) {
      final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(pgnTest);
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        final String pgnFileName = testCase.pgnFileName();

        System.out.printf("Processing %s%n", pgnFileName);
        final PgnHolder pgn = new PgnHolder(FileUtility.calculateFilePath(pgnTest.getFolderPath(), pgnFileName));

        final var millisecondsBefore = System.currentTimeMillis();
        pgn.loadPgn();
        final var game = NonNullWrapperCommon.getFirst(NonNullWrapperApiCarlos.getGames(pgn));
        game.loadMoveText();
        final var millisecondsDuration = System.currentTimeMillis() - millisecondsBefore;
        System.out.printf("Loading duration seconds: %f%n", millisecondsDuration / 1000.0);
        System.out.printf("Half-moves: %s%n", game.getHalfMoves().size());
        System.out.println();
      }
    }
  }
}
