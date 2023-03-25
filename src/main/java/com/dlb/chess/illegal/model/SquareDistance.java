package com.dlb.chess.illegal.model;

import java.util.Comparator;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Square;

@SuppressWarnings("null")
public record SquareDistance(@NonNull Square fromSquare) implements Comparator<Square> {

  @Override
  public int compare(Square a, Square b) {
    return Double.compare(calculateDistance(fromSquare, a), calculateDistance(fromSquare, b));
  }

  public static double calculateDistance(Square fromSquare, Square toSquare) {
    final var cathetusHorizontal = Math.abs(toSquare.getFile().getNumber() - fromSquare.getFile().getNumber());

    final var cathetusVertical = Math.abs(toSquare.getRank().getNumber() - fromSquare.getRank().getNumber());

    return Math.sqrt(Math.pow(cathetusHorizontal, 2) + Math.pow(cathetusVertical, 2));
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (SquareDistance) obj;
    return fromSquare == other.fromSquare;
  }

}
