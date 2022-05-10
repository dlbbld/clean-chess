package com.dlb.chess.analysis.model;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.common.model.HalfMove;

public record RepeatingSequence(List<List<HalfMove>> sequence, int sequenceLength, int repetitions) {

  public List<List<HalfMove>> sequence() {
    return sequence;
  }

  public int sequenceLength() {
    return sequenceLength;
  }

  public int repetitions() {
    return repetitions;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (RepeatingSequence) obj;
    return repetitions == other.repetitions && Objects.equals(sequence, other.sequence)
        && sequenceLength == other.sequenceLength;
  }

}
