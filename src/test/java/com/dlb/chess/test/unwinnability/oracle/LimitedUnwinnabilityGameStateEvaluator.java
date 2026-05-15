package com.dlb.chess.test.unwinnability.oracle;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.test.unwinnability.oracle.enums.LimitedUnwinnabilityVerdict;
import com.google.common.collect.ImmutableSet;

public class LimitedUnwinnabilityGameStateEvaluator {

  private static final ImmutableSet<GameStatus> GAME_DRAW_SET;
  private static final ImmutableSet<GameStatus> GAME_MADE_THE_MOVE_UNWINNABLE_SET;
  static final ImmutableSet<GameStatus> GAME_NOT_MADE_THE_MOVE_UNWINNABLE_SET;

  static {
    {
      final EnumSet<GameStatus> set = Nulls.newEnumSet(new ArrayList<>(), GameStatus.class);

      set.add(GameStatus.STALEMATE);
      set.add(GameStatus.INSUFFICIENT_MATERIAL_BOTH);
      set.add(GameStatus.FIVE_FOLD_REPETITION_RULE);
      set.add(GameStatus.SEVENTY_FIVE_MOVE_RULE);
      GAME_DRAW_SET = Nulls.copyOfSet(set);
    }

    {
      final EnumSet<GameStatus> set = Nulls.newEnumSet(new ArrayList<>(), GameStatus.class);

      set.addAll(GAME_DRAW_SET);
      set.add(GameStatus.INSUFFICIENT_MATERIAL_MADE_THE_MOVE_ONLY);
      GAME_MADE_THE_MOVE_UNWINNABLE_SET = Nulls.copyOfSet(set);
    }

    {
      final EnumSet<GameStatus> set = Nulls.newEnumSet(new ArrayList<>(), GameStatus.class);

      set.addAll(GAME_DRAW_SET);
      set.add(GameStatus.CHECKMATE);
      set.add(GameStatus.INSUFFICIENT_MATERIAL_NOT_MADE_THE_MOVE_ONLY);
      GAME_NOT_MADE_THE_MOVE_UNWINNABLE_SET = Nulls.copyOfSet(set);
    }
  }

  static LimitedUnwinnabilityVerdict calculateUnwinnabilityMadeTheMove(Set<GameStatus> gameStatusFirstHalfMove) {

    if (gameStatusFirstHalfMove.size() == 1) {
      final GameStatus singleGameStatus = BasicUtility.calculateOnlyElement(gameStatusFirstHalfMove);

      return switch (singleGameStatus) {
        case CHECKMATE -> LimitedUnwinnabilityVerdict.WINNABLE;
        case STALEMATE -> LimitedUnwinnabilityVerdict.UNWINNABLE;
        case INSUFFICIENT_MATERIAL_BOTH -> LimitedUnwinnabilityVerdict.UNWINNABLE;
        case INSUFFICIENT_MATERIAL_MADE_THE_MOVE_ONLY -> LimitedUnwinnabilityVerdict.UNWINNABLE;
        case FIVE_FOLD_REPETITION_RULE -> LimitedUnwinnabilityVerdict.UNWINNABLE;
        case SEVENTY_FIVE_MOVE_RULE -> LimitedUnwinnabilityVerdict.UNWINNABLE;
        case INSUFFICIENT_MATERIAL_NOT_MADE_THE_MOVE_ONLY, ONGOING -> LimitedUnwinnabilityVerdict.UNKNOWN;
        default -> throw new IllegalArgumentException();
      };
    }
    if (gameStatusFirstHalfMove.contains(GameStatus.CHECKMATE)) {
      return LimitedUnwinnabilityVerdict.WINNABLE;
    }
    if (GAME_DRAW_SET.containsAll(gameStatusFirstHalfMove)
        || GAME_MADE_THE_MOVE_UNWINNABLE_SET.containsAll(gameStatusFirstHalfMove)) {
      return LimitedUnwinnabilityVerdict.UNWINNABLE;
    }
    return LimitedUnwinnabilityVerdict.UNKNOWN;
  }

  static LimitedUnwinnabilityVerdict calculateUnwinnabilityNotMadeTheMove(Set<GameStatus> gameStatusFirstHalfMove) {

    if (GAME_DRAW_SET.containsAll(gameStatusFirstHalfMove)
        || GAME_NOT_MADE_THE_MOVE_UNWINNABLE_SET.containsAll(gameStatusFirstHalfMove)) {
      return LimitedUnwinnabilityVerdict.UNWINNABLE;
    }
    return LimitedUnwinnabilityVerdict.UNKNOWN;
  }

}
