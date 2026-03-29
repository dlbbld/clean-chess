package com.dlb.chess.utility;

import com.dlb.chess.pgn.parser.enums.StandardTag;
import com.dlb.chess.pgn.parser.model.Tag;

public abstract class TagPlaceHolderUtility {

  public static Tag getPlaceHolderTag(StandardTag standardTag) {
    return new Tag(standardTag.getName(), "?");
  }

}
