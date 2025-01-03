package com.dlb.chess.test.pgn.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.pgn.reader.enums.StandardTag;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.common.utility.AbstractTestMovetextUtility;
import com.dlb.chess.test.pgntest.PgnTestConstants;
import com.dlb.chess.utility.TagUtility;

class TestPgnReaderStrictReadTag extends AbstractTestMovetextUtility {
  private static final Path PGN_TEST_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.PGN_READER_STRICT_TEST_ROOT_FOLDER_PATH, "tag");

  @SuppressWarnings("static-method")
  @Test
  void test() {
    {
      final PgnFile pgnFile = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "01_example_white_last_move.pgn");

      assertEquals("Groningen", TagUtility.calculateTagValue(pgnFile, StandardTag.EVENT));
      assertEquals("Groningen NED", TagUtility.calculateTagValue(pgnFile, StandardTag.SITE));
      assertEquals("1997.??.??", TagUtility.calculateTagValue(pgnFile, StandardTag.DATE));
      assertEquals("9", TagUtility.calculateTagValue(pgnFile, StandardTag.ROUND));
      assertEquals("Pavel Blatny", TagUtility.calculateTagValue(pgnFile, StandardTag.WHITE));
      assertEquals("Frank Holzke", TagUtility.calculateTagValue(pgnFile, StandardTag.BLACK));
      assertEquals("1/2-1/2", TagUtility.calculateTagValue(pgnFile, StandardTag.RESULT));
      assertEquals("A15", TagUtility.calculateTagValue(pgnFile, "ECO"));
      assertEquals("1997.??.??", TagUtility.calculateTagValue(pgnFile, "EventDate"));

      assertEquals(FenConstants.FEN_INITIAL, pgnFile.startFen());
    }

    {
      final PgnFile pgnFile = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "02_example_black_last_move.pgn");

      assertEquals(FenConstants.FEN_INITIAL, pgnFile.startFen());
    }

    {
      final PgnFile pgnFile = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH,
          "03_example_white_last_move_short.pgn");
      final List<String> halfMoveList = new ArrayList<>();
      halfMoveList.add("Nf3");
      halfMoveList.add("Nf6");
      halfMoveList.add("c4");
      assertEquals(halfMoveList, calculateSanList(pgnFile.halfMoveList()));

      assertEquals(FenConstants.FEN_INITIAL, pgnFile.startFen());
    }

    {
      final PgnFile pgnFile = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH,
          "04_example_black_last_move_short.pgn");
      final List<String> halfMoveList = new ArrayList<>();
      halfMoveList.add("Nf3");
      halfMoveList.add("Nf6");
      halfMoveList.add("c4");
      halfMoveList.add("c5");
      assertEquals(halfMoveList, calculateSanList(pgnFile.halfMoveList()));

      assertEquals(FenConstants.FEN_INITIAL, pgnFile.startFen());
    }

    {
      // we want to check that the initial position - the easiest case - is read properly
      final PgnFile pgnFile = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH,
          "05_set_up_value_1_fen_initial_position.pgn");
      assertEquals(FenConstants.FEN_INITIAL, pgnFile.startFen());
    }

    {
      // the FEN must be there for this case
      final PgnFile pgnFile = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH,
          "06_set_up_value_1_fen_non_initial_position.pgn");
      assertEquals("3k4/8/8/8/3K4/3R4/8/8 w - - 0 100", pgnFile.startFen().fen());
    }

    {
      // the FEN must not be there for this case
      final PgnFile pgnFile = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH,
          "07_set_up_value_0_fen_not_set.pgn");
      assertEquals(FenConstants.FEN_INITIAL_STR, pgnFile.startFen().fen());
    }

  }

}
