package com.dlb.chess.common.utility;

import java.util.Random;

public class RandomUtility {

  // recommended by SonarLint to reuse
  private static final Random random = new Random();

  public static int calculateRandomNumber(int minimumInclusive, int maximumInclusive) {
    if (minimumInclusive > maximumInclusive) {
      throw new IllegalArgumentException();
    }
    final var maximumExclusive = maximumInclusive + 1;
    return random.nextInt(maximumExclusive - minimumInclusive) + minimumInclusive;
  }

}
