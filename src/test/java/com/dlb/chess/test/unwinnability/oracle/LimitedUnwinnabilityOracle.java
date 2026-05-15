package com.dlb.chess.test.unwinnability.oracle;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.test.unwinnability.oracle.enums.LimitedUnwinnabilityVerdict;

public class LimitedUnwinnabilityOracle {

  public static LimitedUnwinnabilityVerdict calculateUnwinnability(Board board, Side side) {
    final LimitedUnwinnabilityVerdict verdict = ShallowTerminationOracle.calculateUnwinnability(board, side);
    if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
      return verdict;
    }

    if (PawnWallGeometricAnalyzer.calculate(board) == PawnWallVerdict.YES) {
      return LimitedUnwinnabilityVerdict.UNWINNABLE;
    }

    return LimitedUnwinnabilityVerdict.UNKNOWN;
  }

}
