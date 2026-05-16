package com.dlb.chess.test.pgn.export;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.pgn.PgnCreate;
import com.dlb.chess.pgn.PgnFile;
import com.dlb.chess.pgn.PgnUtility;
import com.dlb.chess.pgn.ResultTagValue;

/**
 * Verifies that {@link PgnCreate#createPgnFile(Board)} produces the minimal honest model — no STR fabrication — and
 * that the model round-trips through the parser back to the source board. STR fabrication is exercised separately in
 * {@code TestPgnExportBoardArchival} (the archival-mode path that does fill the Seven Tag Roster).
 */
class TestPgnExportBoard {

  @SuppressWarnings("static-method")
  @Test
  void test() {

    check(ResultTagValue.ONGOING);
    check(ResultTagValue.ONGOING, "e4");

    check(ResultTagValue.WHITE_WON, "e4", "e5", "Bc4", "Nc6", "Qh5", "h6", "Qxf7#");
    check(ResultTagValue.BLACK_WON, "f3", "e5", "g4", "Qh4#");

  }

  private static void check(ResultTagValue resultTagValue, String... sanArray) {

    final Board boardExpected = new Board(false);
    for (final String san : sanArray) {
      @SuppressWarnings("null") @NonNull final String sanIsNotNull = san;
      boardExpected.moveStrict(sanIsNotNull);
    }

    final PgnFile boardExpectedPgnFile = PgnCreate.createPgnFile(boardExpected);

    checkNoFabrication(resultTagValue, boardExpectedPgnFile);

    checkBoardReplay(boardExpected, boardExpectedPgnFile);
  }

  /**
   * The minimal honest model: no caller supplied tags and the board started from the initial position, so the tag list
   * is empty. The termination marker carries the board-state-derived result; the Result tag itself is not fabricated
   * (it would be added on the archival-write path, not into the parse-model).
   */
  private static void checkNoFabrication(ResultTagValue resultTagValue, PgnFile pgnFile) {
    assertTrue(pgnFile.tagList().isEmpty());
    assertEquals(resultTagValue, pgnFile.terminationMarker());
  }

  private static void checkBoardReplay(Board boardExpected, PgnFile boardExpectedPgnFile) {
    final Board boardActual = PgnUtility.calculateBoard(boardExpectedPgnFile, false);
    assertEquals(boardExpected, boardActual);
  }

}
