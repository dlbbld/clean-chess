package com.dlb.chess;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.model.PossibleMove;
import com.google.common.collect.ImmutableMap;

//where the underlying API is checked to return non null
public class NonNullWrapper {
  private NonNullWrapper() {
  }

  private static <E> E checkResult(@Nullable E result) {
    if (result == null) {
      throw new ProgrammingMistakeException("Assumed value is not null");
    }
    return result;
  }

  public static PossibleMove getPossibleMove(
      ImmutableMap<Side, ImmutableMap<Square, ImmutableMap<Square, PossibleMove>>> mapMapMap, Side side,
      Square fromSquare, Square toSquare) {

    final ImmutableMap<Square, ImmutableMap<Square, PossibleMove>> stepOne = checkResult(mapMapMap.get(side));
    final ImmutableMap<Square, PossibleMove> stepTwo = checkResult(stepOne.get(fromSquare));
    return checkResult(stepTwo.get(toSquare));
  }

}
