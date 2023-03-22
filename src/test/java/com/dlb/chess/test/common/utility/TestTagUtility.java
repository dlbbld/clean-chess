package com.dlb.chess.test.common.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.pgn.reader.enums.ResultTagValue;
import com.dlb.chess.pgn.reader.enums.StandardTag;
import com.dlb.chess.pgn.reader.model.Tag;
import com.dlb.chess.utility.TagUtility;

class TestTagUtility implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void test() {

    final List<Tag> tagList = new ArrayList<>();

    tagList.add(new Tag(StandardTag.RESULT.getName(), ResultTagValue.WHITE_WON.getValue()));
    tagList.add(new Tag(StandardTag.FEN.getName(), FenConstants.FEN_INITIAL_STR));

    assertTrue(TagUtility.hasResult(tagList));
    assertTrue(TagUtility.hasFen(tagList));

    assertEquals(ResultTagValue.WHITE_WON.getValue(), TagUtility.readResult(tagList));
    assertEquals(FenConstants.FEN_INITIAL_STR, TagUtility.readFen(tagList));

    assertFalse(TagUtility.hasEvent(tagList));
    assertFalse(TagUtility.hasSetUp(tagList));
  }
}