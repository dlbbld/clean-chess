package com.dlb.chess.distance.model;

import org.eclipse.jdt.annotation.Nullable;

//Class for storing a cell's data
public record Cell(int x, int y, int dis) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (Cell) obj;
    return dis == other.dis && x == other.x && y == other.y;
  }

}
