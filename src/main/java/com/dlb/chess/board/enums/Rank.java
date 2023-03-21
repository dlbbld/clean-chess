package com.dlb.chess.board.enums;

import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.exceptions.NonePointerException;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;

public enum Rank {
  RANK_1(ChessConstants.RANK_1_NUMBER),
  RANK_2(ChessConstants.RANK_2_NUMBER),
  RANK_3(ChessConstants.RANK_3_NUMBER),
  RANK_4(ChessConstants.RANK_4_NUMBER),
  RANK_5(ChessConstants.RANK_5_NUMBER),
  RANK_6(ChessConstants.RANK_6_NUMBER),
  RANK_7(ChessConstants.RANK_7_NUMBER),
  RANK_8(ChessConstants.RANK_8_NUMBER),
  NONE(0);

  private final int number;

  Rank(int number) {
    this.number = number;
  }

  public int getNumber() {
    check();
    return number;
  }

  public static boolean exists(int number) {
    for (final Rank rank : values()) {
      if (rank == NONE) {
        continue;
      }
      if (rank.getNumber() == number) {
        return true;
      }
    }
    return false;
  }

  public static Rank calculateRank(int number) {
    if (!exists(number)) {
      throw new IllegalArgumentException("For this number no corresponding non dummy Rank exists");
    }
    for (final Rank rank : values()) {
      if (rank == NONE) {
        continue;
      }
      if (rank.getNumber() == number) {
        return rank;
      }
    }
    throw new ProgrammingMistakeException("The code for calculating the rank is wrong");
  }

  public static Rank calculatePreviousRank(Side havingMove, Rank rank) {
    if (havingMove == Side.NONE || rank == NONE || !calculateHasPreviousRank(havingMove, rank)) {
      throw new IllegalArgumentException();
    }
    return switch (havingMove) {
      case BLACK -> calculateNextRankWhiteView(rank);
      case WHITE -> calculatePreviousRankWhiteView(rank);
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static Rank calculatePreviousPreviousRank(Side havingMove, Rank rank) {
    if (havingMove == Side.NONE || rank == NONE || !calculateHasPreviousPreviousRank(havingMove, rank)) {
      throw new IllegalArgumentException();
    }
    final Rank previousRank = calculatePreviousRank(havingMove, rank);

    return calculatePreviousRank(havingMove, previousRank);
  }

  public static Rank calculateNextRank(Side havingMove, Rank rank) {
    if (havingMove == Side.NONE || rank == NONE || !calculateHasNextRank(havingMove, rank)) {
      throw new IllegalArgumentException();
    }

    return switch (havingMove) {
      case BLACK -> calculatePreviousRankWhiteView(rank);
      case WHITE -> calculateNextRankWhiteView(rank);
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static Rank calculateNextNextRank(Side havingMove, Rank rank) {
    if (havingMove == Side.NONE || rank == NONE || !calculateHasNextNextRank(havingMove, rank)) {
      throw new IllegalArgumentException();
    }
    final Rank nextRank = calculateNextRank(havingMove, rank);

    return calculateNextRank(havingMove, nextRank);
  }

  public static boolean calculateHasPreviousRank(Side havingMove, Rank rank) {
    if (havingMove == Side.NONE || rank == NONE) {
      throw new IllegalArgumentException();
    }

    return switch (havingMove) {
      case BLACK -> rank != RANK_8;
      case WHITE -> rank != RANK_1;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static boolean calculateHasPreviousPreviousRank(Side havingMove, Rank rank) {
    if (havingMove == Side.NONE || rank == NONE) {
      throw new IllegalArgumentException();
    }

    if (!calculateHasPreviousRank(havingMove, rank)) {
      return false;
    }
    final Rank previousRank = calculatePreviousRank(havingMove, rank);

    return calculateHasPreviousRank(havingMove, previousRank);
  }

  public static boolean calculateHasNextRank(Side havingMove, Rank rank) {
    if (havingMove == Side.NONE || rank == NONE) {
      throw new IllegalArgumentException();
    }
    return switch (havingMove) {
      case BLACK -> rank != RANK_1;
      case WHITE -> rank != RANK_8;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static boolean calculateHasNextNextRank(Side havingMove, Rank rank) {
    if (havingMove == Side.NONE || rank == NONE) {
      throw new IllegalArgumentException();
    }

    if (!calculateHasNextRank(havingMove, rank)) {
      return false;
    }
    final Rank nextRank = calculateNextRank(havingMove, rank);

    return calculateHasNextRank(havingMove, nextRank);
  }

  private static Rank calculatePreviousRankWhiteView(Rank rank) {

    return switch (rank) {
      case RANK_1 -> throw new IllegalArgumentException();
      case RANK_2 -> RANK_1;
      case RANK_3 -> RANK_2;
      case RANK_4 -> RANK_3;
      case RANK_5 -> RANK_4;
      case RANK_6 -> RANK_5;
      case RANK_7 -> RANK_6;
      case RANK_8 -> RANK_7;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  private static Rank calculateNextRankWhiteView(Rank rank) {
    return switch (rank) {
      case RANK_1 -> RANK_2;
      case RANK_2 -> RANK_3;
      case RANK_3 -> RANK_4;
      case RANK_4 -> RANK_5;
      case RANK_5 -> RANK_6;
      case RANK_6 -> RANK_7;
      case RANK_7 -> RANK_8;
      case RANK_8 -> throw new IllegalArgumentException();
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static boolean calculateIsGroundRank(Side havingMove, Rank rank) {
    if (havingMove == Side.NONE || rank == NONE) {
      throw new IllegalArgumentException();
    }
    return rank == calculateGroundRank(havingMove);
  }

  public static Rank calculateGroundRank(Side havingMove) {
    if (havingMove == Side.NONE) {
      throw new IllegalArgumentException();
    }

    return calculatePromotionRank(havingMove.getOppositeSide());
  }

  public static boolean calculateIsPromotionRank(Side havingMove, Rank rank) {
    if (havingMove == Side.NONE || rank == NONE) {
      throw new IllegalArgumentException();
    }

    return rank == calculatePromotionRank(havingMove);
  }

  public static Rank calculatePromotionRank(Side havingMove) {
    return switch (havingMove) {
      case WHITE -> RANK_8;
      case BLACK -> RANK_1;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static boolean calculateIsPawnInititalRank(Side havingMove, Rank rank) {
    if (havingMove == Side.NONE || rank == NONE) {
      throw new IllegalArgumentException();
    }

    return rank == calculatePawnInitialRank(havingMove);
  }

  public static Rank calculatePawnInitialRank(Side havingMove) {
    return switch (havingMove) {
      case BLACK -> RANK_7;
      case WHITE -> RANK_2;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static boolean calculateIsPawnTwoSquareAdvanceRank(Side havingMove, Rank rank) {
    if (havingMove == Side.NONE || rank == NONE) {
      throw new IllegalArgumentException();
    }

    return rank == calculatePawnTwoSquareAdvanceRank(havingMove);
  }

  public static Rank calculatePawnTwoSquareAdvanceRank(Side havingMove) {
    return switch (havingMove) {
      case BLACK -> RANK_5;
      case WHITE -> RANK_4;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static Rank calculateEnPassantCaptureToRank(Side havingMove) {
    return switch (havingMove) {
      case BLACK -> RANK_3;
      case WHITE -> RANK_6;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static Rank calculateEnPassantCaptureFromRank(Side havingMove) {
    return switch (havingMove) {
      case BLACK -> RANK_4;
      case WHITE -> RANK_5;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static boolean calculateIsPawnEnPassantCaptureToRank(Side havingMove, Rank rank) {
    if (havingMove == Side.NONE || rank == NONE) {
      throw new IllegalArgumentException();
    }

    return rank == calculateEnPassantCaptureToRank(havingMove);
  }

  private void check() {
    if (this == NONE) {
      throw new NonePointerException();
    }
  }
}
