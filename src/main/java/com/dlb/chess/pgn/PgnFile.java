package com.dlb.chess.pgn;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.common.Nulls;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.PgnHalfMove;
import com.google.common.collect.ImmutableList;

@SuppressWarnings("null")
public record PgnFile(@NonNull ImmutableList<@NonNull Tag> tagList, @NonNull Fen startFen,
    @NonNull PgnCommentary pregameCommentary, @NonNull ImmutableList<@NonNull PgnHalfMove> halfMoveList) {

  public PgnFile {
    tagList = Nulls.copyOfList(tagList);
    halfMoveList = Nulls.copyOfList(halfMoveList);
  }
}
