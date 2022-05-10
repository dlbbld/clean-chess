package com.dlb.chess.analysis.model;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

public record SingleOutput(Analysis analysis, List<String> output) {

  public Analysis analysis() {
    return analysis;
  }

  public List<String> output() {
    return output;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (SingleOutput) obj;
    return Objects.equals(analysis, other.analysis) && Objects.equals(output, other.output);
  }
}
