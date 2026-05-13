package com.dlb.chess.pgn;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.common.Nulls;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.PgnHalfMove;
import com.google.common.collect.ImmutableList;

/**
 * Parsed PGN model. Reflects what the source actually contained — tag presence/absence and tag order are preserved by
 * the parsers. The {@code terminationMarker} is the movetext game-termination marker (`1-0`, `0-1`, `1/2-1/2`, `*`) and
 * is independent of any {@code Result} tag in {@link #tagList()}: both, either, or neither may be present in
 * lenient-parsed input, while strict-parsed input always has both (and they match).
 */
@SuppressWarnings("null")
public record PgnFile(@NonNull ImmutableList<@NonNull Tag> tagList, @NonNull Fen startFen,
    @NonNull PgnCommentary pregameCommentary, @NonNull ImmutableList<@NonNull PgnHalfMove> halfMoveList,
    @Nullable ResultTagValue terminationMarker) {

  public PgnFile {
    tagList = Nulls.copyOfList(tagList);
    halfMoveList = Nulls.copyOfList(halfMoveList);
  }
}
