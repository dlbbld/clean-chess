package com.dlb.chess.pgn.reader.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

public record FirstDuplicateTag(boolean hasDuplicateTag, String duplicateTagName) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (FirstDuplicateTag) obj;
    return hasDuplicateTag == other.hasDuplicateTag && Objects.equals(duplicateTagName, other.duplicateTagName);
  }

}
