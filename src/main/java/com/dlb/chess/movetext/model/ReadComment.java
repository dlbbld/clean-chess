package com.dlb.chess.movetext.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

public record ReadComment(String comment, boolean isExhausted, String remainingValue) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (ReadComment) obj;
    return Objects.equals(comment, other.comment) && isExhausted == other.isExhausted
        && Objects.equals(remainingValue, other.remainingValue);
  }

  public String comment() {
    return comment;
  }

  public boolean isExhausted() {
    return isExhausted;
  }

  public String remainingValue() {
    return remainingValue;
  }

}
