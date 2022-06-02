package com.dlb.chess.test.unwinnability.againstcha.model;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

public record ChaBothRead(List<ChaFullRead> fullResultList, List<ChaQuickRead> quickResultList) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (ChaBothRead) obj;
    return Objects.equals(fullResultList, other.fullResultList)
        && Objects.equals(quickResultList, other.quickResultList);
  }

  public List<ChaFullRead> fullResultList() {
    return fullResultList;
  }

  public List<ChaQuickRead> quickResultList() {
    return quickResultList;
  }
}
