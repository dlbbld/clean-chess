package com.dlb.chess.test.winnable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.test.winnable.enums.Winnable;
import com.google.common.collect.ImmutableSet;

public class WinnableEvaluateGameState {

  private static final ImmutableSet<GameStatus> GAME_DRAW_SET;
  private static final ImmutableSet<GameStatus> GAME_MADE_THE_MOVE_UNWINNABLE_SET;
  static final ImmutableSet<GameStatus> GAME_NOT_MADE_THE_MOVE_UNWINNABLE_SET;

  static {
    {
      final EnumSet<GameStatus> set = NonNullWrapperCommon.newEnumSet(new ArrayList<>(), GameStatus.class);

      set.add(GameStatus.STALEMATE);
      set.add(GameStatus.INSUFFICIENT_MATERIAL_BOTH);
      set.add(GameStatus.FIVE_FOLD_REPETITION_RULE);
      set.add(GameStatus.SEVENTY_FIVE_MOVE_RULE);
      GAME_DRAW_SET = NonNullWrapperCommon.copyOfSet(set);
    }

    {
      final EnumSet<GameStatus> set = NonNullWrapperCommon.newEnumSet(new ArrayList<>(), GameStatus.class);

      set.addAll(GAME_DRAW_SET);
      set.add(GameStatus.INSUFFICIENT_MATERIAL_MADE_THE_MOVE_ONLY);
      GAME_MADE_THE_MOVE_UNWINNABLE_SET = NonNullWrapperCommon.copyOfSet(set);
    }

    {
      final EnumSet<GameStatus> set = NonNullWrapperCommon.newEnumSet(new ArrayList<>(), GameStatus.class);

      set.addAll(GAME_DRAW_SET);
      set.add(GameStatus.CHECKMATE);
      set.add(GameStatus.INSUFFICIENT_MATERIAL_NOT_MADE_THE_MOVE_ONLY);
      GAME_NOT_MADE_THE_MOVE_UNWINNABLE_SET = NonNullWrapperCommon.copyOfSet(set);
    }
  }

  static Winnable calculateWinnableMadeTheMove(Set<GameStatus> gameStatusFirstHalfMove) {

    if (gameStatusFirstHalfMove.size() == 1) {
      final GameStatus singleGameStatus = BasicUtility.getFirstElement(gameStatusFirstHalfMove);

      switch (singleGameStatus) {
        case CHECKMATE:
          return Winnable.YES;
        case STALEMATE:
          return Winnable.NO;
        case INSUFFICIENT_MATERIAL_BOTH:
          return Winnable.NO;
        case INSUFFICIENT_MATERIAL_MADE_THE_MOVE_ONLY:
          return Winnable.NO;
        case FIVE_FOLD_REPETITION_RULE:
          return Winnable.NO;
        case SEVENTY_FIVE_MOVE_RULE:
          return Winnable.NO;
        case INSUFFICIENT_MATERIAL_NOT_MADE_THE_MOVE_ONLY:
        case OTHER:
          return Winnable.UNKNOWN;
        default:
          throw new IllegalArgumentException();
      }
    }
    if (gameStatusFirstHalfMove.contains(GameStatus.CHECKMATE)) {
      return Winnable.YES;
    }
    if (GAME_DRAW_SET.containsAll(gameStatusFirstHalfMove)
        || GAME_MADE_THE_MOVE_UNWINNABLE_SET.containsAll(gameStatusFirstHalfMove)) {
      return Winnable.NO;
    }
    return Winnable.UNKNOWN;
  }

  static Winnable calculateWinnableNotMadeTheMove(Set<GameStatus> gameStatusFirstHalfMove) {

    if (GAME_DRAW_SET.containsAll(gameStatusFirstHalfMove)
        || GAME_NOT_MADE_THE_MOVE_UNWINNABLE_SET.containsAll(gameStatusFirstHalfMove)) {
      return Winnable.NO;
    }
    return Winnable.UNKNOWN;
  }

}
