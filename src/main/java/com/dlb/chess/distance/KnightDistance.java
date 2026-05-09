package com.dlb.chess.distance;

import java.util.ArrayDeque;
import java.util.Queue;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;

public final class KnightDistance {

  private static final int BOARD_SIZE = 8;
  private static final int SQUARE_COUNT = BOARD_SIZE * BOARD_SIZE;

  private static final int[] FILE_OFFSETS = { -2, -1, 1, 2, -2, -1, 1, 2 };
  private static final int[] RANK_OFFSETS = { -1, -2, -2, -1, 1, 2, 2, 1 };

  private static final int[][] DISTANCE_BY_SQUARE_INDEX = createDistanceTable();

  private KnightDistance() {
  }

  public static int distance(Square fromSquare, Square toSquare) {
    return DISTANCE_BY_SQUARE_INDEX[toIndex(fromSquare)][toIndex(toSquare)];
  }

  private static int[][] createDistanceTable() {
    final var result = new int[SQUARE_COUNT][SQUARE_COUNT];
    for (final Square fromSquare : Square.REAL) {
      result[toIndex(fromSquare)] = calculateDistancesFrom(fromSquare);
    }
    return result;
  }

  private static int[] calculateDistancesFrom(Square fromSquare) {
    final var distances = new int[SQUARE_COUNT];
    for (var i = 0; i < distances.length; i++) {
      distances[i] = -1;
    }

    final Queue<Square> queue = new ArrayDeque<>();
    distances[toIndex(fromSquare)] = 0;
    queue.add(fromSquare);

    while (!queue.isEmpty()) {
      final Square current = NonNullWrapperCommon.remove(queue);
      final var nextDistance = distances[toIndex(current)] + 1;
      for (var i = 0; i < FILE_OFFSETS.length; i++) {
        final int fileNumber = current.getFile().getNumber() + FILE_OFFSETS[i];
        final int rankNumber = current.getRank().getNumber() + RANK_OFFSETS[i];
        if (!isBoardSquare(fileNumber, rankNumber)) {
          continue;
        }
        final Square next = Square.calculate(fileNumber, rankNumber);
        final int nextIndex = toIndex(next);
        if (distances[nextIndex] != -1) {
          continue;
        }
        distances[nextIndex] = nextDistance;
        queue.add(next);
      }
    }

    return distances;
  }

  private static boolean isBoardSquare(int fileNumber, int rankNumber) {
    return fileNumber >= 1 && fileNumber <= BOARD_SIZE && rankNumber >= 1 && rankNumber <= BOARD_SIZE;
  }

  private static int toIndex(Square square) {
    return (square.getRank().getNumber() - 1) * BOARD_SIZE + square.getFile().getNumber() - 1;
  }
}
