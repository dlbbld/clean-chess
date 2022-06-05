package com.dlb.chess.distance;

import java.util.Vector;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.distance.model.DistanceToCell;

//This code contributed by Rajput-Ji

//Java program to find minimum steps to reach to
//specific cell in minimum moves by Knight
public class KnightDistance {

  // TODO unwinnability cache
  private static final int BOARD_SIZE = 8;

  // Utility method returns true if (x, y) lies
  // inside Board
  private static boolean isInside(int x, int y) {
    if (x >= 1 && x <= BOARD_SIZE && y >= 1 && y <= BOARD_SIZE) {
      return true;
    }
    return false;
  }

  public static int distance(Square fromSquare, Square toSquare) {
    final int[] knightPos = { fromSquare.getFile().getNumber(), fromSquare.getRank().getNumber() };
    final int[] targetPos = { toSquare.getFile().getNumber(), toSquare.getRank().getNumber() };

    final var distance = minStepToReachTarget(knightPos, targetPos);
    return distance;
  }

  // Method returns minimum step
  // to reach target position
  private static int minStepToReachTarget(int[] knightPos, int[] targetPos) {
    // x and y direction, where a knight can move
    final int[] dx = { -2, -1, 1, 2, -2, -1, 1, 2 };
    final int[] dy = { -1, -2, -2, -1, 1, 2, 2, 1 };

    // queue for storing states of knight in board
    final Vector<DistanceToCell> q = new Vector<>();

    // push starting position of knight with 0 distance
    q.add(new DistanceToCell(knightPos[0], knightPos[1], 0));

    DistanceToCell t;
    int x;
    int y;
    final var visit = new boolean[BOARD_SIZE + 1][BOARD_SIZE + 1];

    // make all cell unvisited
    for (var i = 1; i <= BOARD_SIZE; i++) {
      for (var j = 1; j <= BOARD_SIZE; j++) {
        visit[i][j] = false;
      }
    }

    // visit starting state
    visit[knightPos[0]][knightPos[1]] = true;

    // loop until we have one element in queue
    while (!q.isEmpty()) {
      @SuppressWarnings("null") final var firstElement = q.firstElement();
      t = firstElement;
      q.remove(0);

      // if current cell is equal to target cell,
      // return its distance
      if (t.x() == targetPos[0] && t.y() == targetPos[1]) {
        return t.dis();
      }

      // loop for all reachable states
      for (var i = 0; i < 8; i++) {
        x = t.x() + dx[i];
        y = t.y() + dy[i];

        // If reachable state is not yet visited and
        // inside board, push that state into queue
        if (isInside(x, y) && !visit[x][y]) {
          visit[x][y] = true;
          q.add(new DistanceToCell(x, y, t.dis() + 1));
        }
      }
    }
    return Integer.MAX_VALUE;
  }

  // Driver code
  public static void main(String[] args) {
    final int[] knightPos = { 1, 1 };
    final int[] targetPos = { 2, 3 };
    System.out.println(minStepToReachTarget(knightPos, targetPos));
  }
}
