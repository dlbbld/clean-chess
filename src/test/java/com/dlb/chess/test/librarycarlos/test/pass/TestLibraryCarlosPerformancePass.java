package com.dlb.chess.test.librarycarlos.test.pass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.test.librarycarlos.NonNullWrapperLibraryCarlos;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.pgn.PgnHolder;

class TestLibraryCarlosPerformancePass {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestLibraryCarlosPerformancePass.class);

  private static final double LOAD_PGN_DURATION_MAX_MILLISECONDS = 1000.0;

  private static final double LOAD_MOVE_TEXT_DURATION_MAX_MILLISECONDS = 500.0;

  private static final double PER_HALF_MOVE_MAX_MILLISECONDS = 0.5;

  private static List<PgnTest> PGN_TEST_LIST = NonNullWrapperCommon.asList(PgnTest.MAX_MOVES);

  @SuppressWarnings("static-method")
  @Test
  void testPerformance() throws Exception {
    for (final PgnTest pgnTest : PGN_TEST_LIST) {
      final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(pgnTest);
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        final String pgnFileName = testCase.pgnFileName();
        logger.info(pgnFileName);
        final Path filePath = NonNullWrapperCommon.pathResolve(pgnTest.getFolderPath(), pgnFileName);
        final var pgn = new PgnHolder(filePath.toAbsolutePath().toString());

        final var millisecondsBeforeLoadPgn = System.currentTimeMillis();
        pgn.loadPgn();
        final double millisecondDurationLoadPgn = System.currentTimeMillis() - millisecondsBeforeLoadPgn;

        assertTrue(millisecondDurationLoadPgn < LOAD_PGN_DURATION_MAX_MILLISECONDS);

        logger.info("loadPgn duration seconds: {}", millisecondDurationLoadPgn / 1000);

        final var game = NonNullWrapperCommon.getFirst(NonNullWrapperLibraryCarlos.getGames(pgn));
        final var millisecondsBeforeLoadMoveText = System.currentTimeMillis();
        game.loadMoveText();
        final var millisecondDurationLoadMoveText = System.currentTimeMillis() - millisecondsBeforeLoadMoveText;
        assertTrue(millisecondDurationLoadMoveText < LOAD_MOVE_TEXT_DURATION_MAX_MILLISECONDS);

        logger.info("loadMoveText duration seconds: {}", millisecondDurationLoadMoveText / 1000);

        final var moves = game.getHalfMoves();
        final var halfMoves = moves.size();
        logger.info("Half-moves to perform: {}", halfMoves);
        final Board board = new Board();
        final var millisecondsBeforePlayingMoves = System.currentTimeMillis();
        for (final Move move : moves) {
          board.doMove(move);
        }
        final var millisecondDurationPlayingMoves = System.currentTimeMillis() - millisecondsBeforePlayingMoves;

        final double perHalfMoveMilliseconds = millisecondDurationPlayingMoves / halfMoves;

        assertTrue(perHalfMoveMilliseconds < PER_HALF_MOVE_MAX_MILLISECONDS);
        logger.info("Milliseconds per half-move: {}", perHalfMoveMilliseconds);
        logger.info("");
      }
    }

  }

}
