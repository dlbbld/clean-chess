package com.dlb.chess.test.model;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.test.pgntest.enums.PgnTest;

public record PgnFileTestCaseList(PgnTest pgnTest, List<PgnFileTestCase> list) {

  public PgnTest pgnTest() {
    return pgnTest;
  }

  public List<PgnFileTestCase> list() {
    return list;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (PgnFileTestCaseList) obj;
    return Objects.equals(pgnTest, other.pgnTest) && Objects.equals(list, other.list);
  }
}
