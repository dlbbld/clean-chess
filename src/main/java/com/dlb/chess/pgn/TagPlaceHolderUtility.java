package com.dlb.chess.pgn;

abstract class TagPlaceHolderUtility {

  static Tag getPlaceHolderTag(StandardTag standardTag) {
    return new Tag(standardTag.getName(), "?");
  }

}
