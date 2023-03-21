package com.dlb.chess.utility;

import com.dlb.chess.pgn.reader.enums.StandardTag;
import com.dlb.chess.pgn.reader.model.Tag;

public class TagPlaceHolderUtility {

  private static final Tag TAG_EVENT = new Tag(StandardTag.EVENT.getName(), "?");
  private static final Tag TAG_SITE = new Tag(StandardTag.SITE.getName(), "?");
  private static final Tag TAG_DATE = new Tag(StandardTag.DATE.getName(), "?");
  private static final Tag TAG_ROUND = new Tag(StandardTag.ROUND.getName(), "?");
  private static final Tag TAG_WHITE = new Tag(StandardTag.WHITE.getName(), "?");
  private static final Tag TAG_BLACK = new Tag(StandardTag.BLACK.getName(), "?");

  public static Tag getPlaceHolderTag(StandardTag standardTag) {
    switch (standardTag) {
      case EVENT:
        return TAG_EVENT;
      case SITE:
        return TAG_SITE;
      case DATE:
        return TAG_DATE;
      case ROUND:
        return TAG_ROUND;
      case WHITE:
        return TAG_WHITE;
      case BLACK:
        return TAG_BLACK;
      // $CASES-OMITTED$
      default:
        throw new IllegalArgumentException(
            "Place holder tags only exists for the sevent tag roster tags excluding the result");

    }

  }

}
