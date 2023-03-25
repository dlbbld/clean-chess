package com.dlb.chess.illegal.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.common.model.MoveSpecification;

@SuppressWarnings("null")
public record DetectEnPassantCapture(boolean isDetected, @NonNull MoveSpecification moveSpecification) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (DetectEnPassantCapture) obj;
    return isDetected == other.isDetected && Objects.equals(moveSpecification, other.moveSpecification);
  }

}
