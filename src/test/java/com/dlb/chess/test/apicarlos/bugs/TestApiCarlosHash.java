package com.dlb.chess.test.apicarlos.bugs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.FileSystemAccessException;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.exceptions.TestSetupException;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.game.Game;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.pgn.PgnHolder;

class TestApiCarlosHash {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestApiCarlosHash.class);

  private static final int SHOW_COMPLETION_PROGRESS_NUMBER_OF_PROCESSED_FILES = 1;

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    processFileList(PgnTest.RANDOM_NO_REPETITION);
  }

  private static void processFileList(PgnTest pgnTest) throws Exception {
    final File folder = new File(pgnTest.getFolderPath());
    if (!folder.isDirectory()) {
      throw new TestSetupException("\"" + pgnTest.getFolderPath() + "\" is not a directory");
    }

    final var filesList = folder.listFiles();
    if (filesList == null) {
      throw new FileSystemAccessException("The files in directory " + pgnTest.getFolderPath() + " could not be read");
    }

    logger.info("Completion progress will be displayed every " + SHOW_COMPLETION_PROGRESS_NUMBER_OF_PROCESSED_FILES
        + " processed files");

    var numberOfFilesProcessed = 0;
    final var numberOfFilesToProcess = filesList.length;
    logger.info("*** Total " + numberOfFilesToProcess + " files to process ***");

    for (final File file : filesList) {
      if (file == null) {
        throw new ProgrammingMistakeException("Wrong assumption about API behaviour");
      }
      try {
        final String absolutePath = NonNullWrapperCommon.getAbsolutePath(file);
        testBoardHashKeyConsistency(absolutePath);
      } finally {
        numberOfFilesProcessed++;
        if (numberOfFilesProcessed % SHOW_COMPLETION_PROGRESS_NUMBER_OF_PROCESSED_FILES == 0) {
          logger.info("*** Processed " + numberOfFilesProcessed + " games ***");
        }
      }

    }
    logger.info("End processing");
  }

  // from PgnHolderTest.java in API carlos API
  @SuppressWarnings("null")
  private static void testBoardHashKeyConsistency(String path) throws Exception {

    final PgnHolder pgn = new PgnHolder(path);
    pgn.loadPgn();

    var numberOfInconsistencies = 0;

    for (final Game game : pgn.getGames()) {
      game.loadMoveText();
      final StringBuilder s = new StringBuilder();
      final Map<Long, Integer> map = new TreeMap<>();
      final Map<String, Integer> map2 = new TreeMap<>();
      final Map<String, Long> map3 = new TreeMap<>();
      final Board board = new Board();
      var i = 0;
      for (final Move move : game.getHalfMoves()) {
        board.doMove(move);
        assertEquals(board.getIncrementalHashKey(), board.getZobristKey());
        s.append(i);
        i++;
        s.append(" -> ");
        s.append(board.getFen());
        s.append(" -> ");
        s.append(move);
        s.append(" -> ");
        s.append(board.getIncrementalHashKey());
        s.append("\n");
        map.compute(board.getIncrementalHashKey(), (a, b) -> b == null ? 1 : b + 1);
        final var key = board.getPositionId();
        map2.compute(key, (a, b) -> b == null ? 1 : b + 1);
        map3.put(key, board.getIncrementalHashKey());
      }

      for (final Map.Entry<String, Long> entry : map3.entrySet()) {
        if (map.get(entry.getValue()).intValue() != map2.get(entry.getKey()).intValue()) {
          logger.error("----------------");
          logger.error(entry.getKey() + ":" + entry.getValue() + " [" + map2.get(entry.getKey()) + " - "
              + map.get(entry.getValue()) + "]");
          logger.error(s.toString());
          numberOfInconsistencies++;
        }
      }
    }
    assertEquals(0, numberOfInconsistencies);
  }

}
