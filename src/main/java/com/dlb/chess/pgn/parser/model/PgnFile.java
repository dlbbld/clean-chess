package com.dlb.chess.pgn.parser.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.PgnHalfMove;
import com.google.common.collect.ImmutableList;

@SuppressWarnings("null")
public record PgnFile(@NonNull ImmutableList<@NonNull Tag> tagList, @NonNull Fen startFen,
    @NonNull PgnCommentary pregameCommentary, @NonNull ImmutableList<@NonNull PgnHalfMove> halfMoveList) {

  public PgnFile {
    tagList = NonNullWrapperCommon.copyOfList(tagList);
    halfMoveList = NonNullWrapperCommon.copyOfList(halfMoveList);
  }
}
