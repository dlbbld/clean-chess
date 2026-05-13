package com.dlb.chess.test.pgn.export;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.pgn.PgnCreate;
import com.dlb.chess.pgn.PgnFile;
import com.dlb.chess.pgn.ResultTagValue;
import com.dlb.chess.pgn.StandardTag;
import com.dlb.chess.pgn.Tag;
import com.dlb.chess.pgn.TagUtility;
import com.dlb.chess.pgn.WriteMode;

/**
 * The {@link PgnCreate#createPgnFile(Board)} path produces a minimal honest model — no STR fabrication. Archival
 * export ({@link WriteMode#ARCHIVAL}) is the opt-in path that fills the Seven Tag Roster, synthesises a Result tag
 * from the termination marker, and emits PGN spec section 8.1.1-conformant output. This test exercises that
 * archival path on Board-derived inputs.
 */
class TestPgnExportBoardArchival {

  @SuppressWarnings("static-method")
  @Test
  void test() {
    check(ResultTagValue.ONGOING);
    check(ResultTagValue.ONGOING, "e4");
    check(ResultTagValue.WHITE_WON, "e4", "e5", "Bc4", "Nc6", "Qh5", "h6", "Qxf7#");
    check(ResultTagValue.BLACK_WON, "f3", "e5", "g4", "Qh4#");
  }

  private static void check(ResultTagValue expectedResult, String... sanArray) {

    final Board board = new Board();
    for (final String san : sanArray) {
      @SuppressWarnings("null") @NonNull final String sanIsNotNull = san;
      board.moveStrict(sanIsNotNull);
    }

    final PgnFile boardPgnFile = PgnCreate.createPgnFile(board);

    // Sanity precondition: the Board path itself does not fabricate STR. Archival output is the only place STR
    // gets filled.
    assertTrue(boardPgnFile.tagList().isEmpty());

    final String archivalOutput = PgnCreate.createPgnFileString(boardPgnFile, WriteMode.ARCHIVAL);

    assertContainsAllStrPlaceholders(archivalOutput);
    assertResultTagMatchesExpected(archivalOutput, expectedResult);
    assertNoSetUpOrFenTag(archivalOutput);
  }

  private static void assertContainsAllStrPlaceholders(String pgn) {
    // PGN spec section 8.1.1: archival storage requires the full Seven Tag Roster. Date uses "????.??.??" per
    // 8.1.1.3; the other STR entries use "?" placeholders.
    assertTrue(pgn.contains("[" + StandardTag.EVENT.getName() + " \"?\"]"));
    assertTrue(pgn.contains("[" + StandardTag.SITE.getName() + " \"?\"]"));
    assertTrue(pgn.contains("[" + StandardTag.DATE.getName() + " \"????.??.??\"]"));
    assertTrue(pgn.contains("[" + StandardTag.ROUND.getName() + " \"?\"]"));
    assertTrue(pgn.contains("[" + StandardTag.WHITE.getName() + " \"?\"]"));
    assertTrue(pgn.contains("[" + StandardTag.BLACK.getName() + " \"?\"]"));
  }

  private static void assertResultTagMatchesExpected(String pgn, ResultTagValue expectedResult) {
    assertTrue(pgn.contains("[" + StandardTag.RESULT.getName() + " \"" + expectedResult.getValue() + "\"]"));
  }

  private static void assertNoSetUpOrFenTag(String pgn) {
    // The boards in this test all start from the initial position. Archival mode drops the redundant SetUp/FEN
    // signals (per PGN spec section 8.1.1's archival convention; the initial position is the implicit default).
    assertFalse(pgn.contains("[" + StandardTag.SET_UP.getName() + " "));
    assertFalse(pgn.contains("[" + StandardTag.FEN.getName() + " "));
  }

  @SuppressWarnings("static-method")
  @Test
  void boardFromNonInitialPosition_archivalEmitsSetUpAndFen() {
    // Caller passes a tagList already containing FEN+SetUp for a custom starting position, then makes moves on
    // the board. Archival export must preserve the position-encoding tags.
    final String customFen = "r1b2r2/pp1pk1pp/8/7q/3pP1n1/5N1P/PPQ2PP1/3R1RK1 w - - 0 17";
    final Board board = new Board(customFen);
    board.moveStrict("Qa4");

    final List<Tag> tagList = new java.util.ArrayList<>();
    tagList.add(new Tag(StandardTag.SET_UP.getName(), "1"));
    tagList.add(new Tag(StandardTag.FEN.getName(), customFen));

    final PgnFile boardPgnFile = PgnCreate.createPgnFile(board, tagList);
    final String archivalOutput = PgnCreate.createPgnFileString(boardPgnFile, WriteMode.ARCHIVAL);

    assertTrue(archivalOutput.contains("[" + StandardTag.SET_UP.getName() + " \"1\"]"));
    assertTrue(archivalOutput.contains("[" + StandardTag.FEN.getName() + " \"" + customFen + "\"]"));
    assertContainsAllStrPlaceholders(archivalOutput);
    assertResultTagMatchesExpected(archivalOutput, ResultTagValue.ONGOING);
  }

  @SuppressWarnings("static-method")
  @Test
  void archivalTagOrderIsCanonical() {
    // Tags emitted in canonical sort order (STR first by sortOrder, then supplemental tags). Verify by checking
    // that Event appears before Result in the output.
    final Board board = new Board();
    final PgnFile pgnFile = PgnCreate.createPgnFile(board);

    final String archivalOutput = PgnCreate.createPgnFileString(pgnFile, WriteMode.ARCHIVAL);

    final int eventIdx = archivalOutput.indexOf("[" + StandardTag.EVENT.getName() + " ");
    final int resultIdx = archivalOutput.indexOf("[" + StandardTag.RESULT.getName() + " ");

    assertTrue(eventIdx >= 0);
    assertTrue(resultIdx > eventIdx);
  }

  @SuppressWarnings("static-method")
  @Test
  void semanticOutputForInitialBoardIsTagless() {
    // Counterpart to the archival path: semantic mode of an initial-position Board emits a tag-less PGN — just
    // the movetext and termination marker. Honest preservation of "no caller-provided tags."
    final Board board = new Board();
    final PgnFile pgnFile = PgnCreate.createPgnFile(board);

    final String semanticOutput = PgnCreate.createPgnFileString(pgnFile, WriteMode.SEMANTIC);

    // Tag section is empty — no '[' appears in the output, no STR placeholders.
    assertFalse(semanticOutput.contains("[" + StandardTag.EVENT.getName() + " "));
    assertFalse(semanticOutput.contains("[" + StandardTag.RESULT.getName() + " "));
    // Termination marker is still emitted (carried by the parse model's terminationMarker field).
    assertTrue(semanticOutput.contains("*"));

    // And the tagList check that no fabrication slipped into the model on this path.
    assertTrue(TagUtility.calculateIsContainsAllSevenTagRosterTags(pgnFile.tagList()) == false);
  }
}
