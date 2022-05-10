package com.dlb.chess.test.custom.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

public record UciMoveTest(String san, String uciMoveStr) {

  public String san() {
    return san;
  }

  public String uciMoveStr() {
    return uciMoveStr;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (UciMoveTest) obj;
    return Objects.equals(san, other.san) && Objects.equals(uciMoveStr, other.uciMoveStr);
  }
}
