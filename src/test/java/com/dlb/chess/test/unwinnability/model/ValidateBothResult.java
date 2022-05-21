package com.dlb.chess.test.unwinnability.model;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

public record ValidateBothResult(List<ValidateFullResult> fullResultList, List<ValidateQuickResult> quickResultList) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (ValidateBothResult) obj;
    return Objects.equals(fullResultList, other.fullResultList)
        && Objects.equals(quickResultList, other.quickResultList);
  }

  public List<ValidateFullResult> fullResultList() {
    return fullResultList;
  }

  public List<ValidateQuickResult> quickResultList() {
    return quickResultList;
  }
}
