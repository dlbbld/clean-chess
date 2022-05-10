package com.dlb.chess.test.pgn.export;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.pgn.create.PgnCreate;
import com.dlb.chess.pgn.reader.enums.ResultTagValue;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.pgn.reader.model.Tag;
import com.dlb.chess.utility.PgnUtility;
import com.dlb.chess.utility.TagUtility;

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

    final Board boardExpected = new Board();
    for (final String san : sanArray) {
      @SuppressWarnings("null") @NonNull final String sanIsNotNull = san;
      boardExpected.performMove(sanIsNotNull);
    }

    final PgnFile boardExpectedPgnFile = PgnCreate.createPgnFile(boardExpected);

    checkTags(resultTagValue, boardExpectedPgnFile.tagList());

    checkBoardReplay(boardExpected, boardExpectedPgnFile);
  }

  private static void checkTags(ResultTagValue resultTagValue, List<Tag> tagList) {
    // check that the STR tags are there
    assertTrue(TagUtility.calculateIsContainsAllSevenTagRosterTags(tagList));

    // check that there are no additional tags
    assertEquals(TagUtility.SEVEN_TAG_ROSTER_TAG_LIST.size(), tagList.size());

    // check the date
    assertEquals(BasicUtility.calculateTodayDate(), TagUtility.readDate(tagList));

    // check the result
    assertEquals(resultTagValue.getValue(), TagUtility.readResult(tagList));
  }

  private static void checkBoardReplay(Board boardExpected, PgnFile boardExpectedPgnFile) {
    final Board boardActual = PgnUtility.calculateBoardPerLastMove(boardExpectedPgnFile);
    assertTrue(boardExpected.equals(boardActual));
  }

}
