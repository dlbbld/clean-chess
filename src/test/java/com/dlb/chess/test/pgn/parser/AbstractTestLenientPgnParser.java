package com.dlb.chess.test.pgn.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dlb.chess.pgn.parser.model.PgnFile;

public abstract class AbstractTestLenientPgnParser {

  static void assertEqualsButTagList(PgnFile expected, PgnFile actual) {
    assertEquals(expected.halfMoveList(), actual.halfMoveList());
    assertEquals(expected.leadingCommentary(), actual.leadingCommentary());
    // assertEquals(expected.tagList(), actual.tagList());
    assertEquals(expected.startFen(), actual.startFen());
  }

  static void assertEqualsButTagListAndResult(PgnFile expected, PgnFile actual) {
    // assertEquals(expected.resultTagValue(), actual.resultTagValue());
    assertEquals(expected.halfMoveList(), actual.halfMoveList());
    assertEquals(expected.leadingCommentary(), actual.leadingCommentary());
    // assertEquals(expected.tagList(), actual.tagList());
    assertEquals(expected.startFen(), actual.startFen());
  }

}
