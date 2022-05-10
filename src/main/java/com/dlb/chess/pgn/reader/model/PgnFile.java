package com.dlb.chess.pgn.reader.model;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.PgnHalfMove;

public record PgnFile(List<Tag> tagList, Fen startFen, String leadingCommentary, List<PgnHalfMove> halfMoveList) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (PgnFile) obj;
    return Objects.equals(halfMoveList, other.halfMoveList)
        && Objects.equals(leadingCommentary, other.leadingCommentary) && Objects.equals(startFen, other.startFen)
        && Objects.equals(tagList, other.tagList);
  }

  public List<Tag> tagList() {
    return tagList;
  }

  public List<PgnHalfMove> halfMoveList() {
    return halfMoveList;
  }

  public Fen startFen() {
    return startFen;
  }

  public String leadingCommentary() {
    return leadingCommentary;
  }

}
