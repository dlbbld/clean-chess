package com.dlb.chess.test.pgn.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.PgnFile;
import com.dlb.chess.pgn.StrictPgnParser;

class TestStrictPgnParserMovetextWithoutCommentary {

  @SuppressWarnings("static-method")
  @Test
  void testInitialWithoutCommentary() {
    checkInitialWithoutCommentary("1. e4", NonNullWrapperCommon.asList("e4"));
    checkInitialWithoutCommentary("1. e4 e5", NonNullWrapperCommon.asList("e4", "e5"));
    checkInitialWithoutCommentary("1. e4 e5 2. d4", NonNullWrapperCommon.asList("e4", "e5", "d4"));
    checkInitialWithoutCommentary("1. e4 e5 2. d4 d5", NonNullWrapperCommon.asList("e4", "e5", "d4", "d5"));
    checkInitialWithoutCommentary("1. e4 e5 2. d4 d5 3. Nc3",
        NonNullWrapperCommon.asList("e4", "e5", "d4", "d5", "Nc3"));
    checkInitialWithoutCommentary("1. e4 e5 2. d4 d5 3. Nc3 Nc6",
        NonNullWrapperCommon.asList("e4", "e5", "d4", "d5", "Nc3", "Nc6"));
    checkInitialWithoutCommentary("1. e4 e5 2. d4 d5 3. Nc3 Nc6 4. a4",
        NonNullWrapperCommon.asList("e4", "e5", "d4", "d5", "Nc3", "Nc6", "a4"));
    checkInitialWithoutCommentary("1. e4 e5 2. d4 d5 3. Nc3 Nc6 4. a4 h5",
        NonNullWrapperCommon.asList("e4", "e5", "d4", "d5", "Nc3", "Nc6", "a4", "h5"));
    checkInitialWithoutCommentary("1. e4 e5 2. d4 d5 3. Nc3 Nc6 4. a4 h5 5. Ra2",
        NonNullWrapperCommon.asList("e4", "e5", "d4", "d5", "Nc3", "Nc6", "a4", "h5", "Ra2"));
    checkInitialWithoutCommentary("1. e4 e5 2. d4 d5 3. Nc3 Nc6 4. a4 h5 5. Ra2 Rh7",
        NonNullWrapperCommon.asList("e4", "e5", "d4", "d5", "Nc3", "Nc6", "a4", "h5", "Ra2", "Rh7"));

    checkInitialWithoutCommentary(
        "1. e4 e5 2. d4 d5 3. Nc3 Nc6 4. a4 h5 5. Ra2 Rh7 6. a5 h4 7. Ra3 Rh6 8. a6 h3 9. Ra4 Rh5"
            + " 10. Ra5 Rh4 11. Ra1 Rh8 12. exd5 exd4",
        NonNullWrapperCommon.asList("e4", "e5", "d4", "d5", "Nc3", "Nc6", "a4", "h5", "Ra2", "Rh7", "a5", "h4", "Ra3",
            "Rh6", "a6", "h3", "Ra4", "Rh5", "Ra5", "Rh4", "Ra1", "Rh8", "exd5", "exd4"));
  }

  /**
   * Verifies that the given movetext body, wrapped in a minimal seven-tag-roster PGN with a trailing termination
   * marker, produces the expected ordered half-move SAN list and leaves every half-move's commentary empty.
   */
  private static void checkInitialWithoutCommentary(String movetextPart, List<String> expectedSanList) {
    final PgnFile file = StrictPgnParser.parseText(header() + movetextPart + " *\n\n");
    assertEquals("", file.pregameCommentary().value());
    assertEquals(expectedSanList, calculateSanList(file.halfMoveList()));
    for (final com.dlb.chess.model.PgnHalfMove halfMove : file.halfMoveList()) {
      assertEquals("", halfMove.commentary().value(), "Expected no commentary on " + halfMove.san());
    }
  }

  /** Minimal strict-format seven-tag-roster header ending with the blank-line tag/movetext separator. */
  private static String header() {
    return """
        [Event "?"]
        [Site "?"]
        [Date "?"]
        [Round "?"]
        [White "?"]
        [Black "?"]
        [Result "*"]

        """;
  }

  private static List<String> calculateSanList(List<PgnHalfMove> halfMoveList) {
    final List<String> sanList = new ArrayList<>();
    for (final PgnHalfMove halfMove : halfMoveList) {
      sanList.add(halfMove.san());
    }
    return sanList;
  }

}
