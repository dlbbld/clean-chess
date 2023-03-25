package com.dlb.chess.movetext.model;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.common.model.Movetext;
import com.dlb.chess.model.PgnHalfMove;

@SuppressWarnings("null")
public record MovetextParseResult(@NonNull Movetext movetext, @NonNull List<PgnHalfMove> halfMoveParseList,
    @NonNull String leadingCommentary) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (MovetextParseResult) obj;
    return Objects.equals(halfMoveParseList, other.halfMoveParseList)
        && Objects.equals(leadingCommentary, other.leadingCommentary) && Objects.equals(movetext, other.movetext);
  }

}
