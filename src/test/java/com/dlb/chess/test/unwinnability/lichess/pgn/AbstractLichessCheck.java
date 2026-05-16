package com.dlb.chess.test.unwinnability.lichess.pgn;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.pgn.PgnFile;
import com.dlb.chess.pgn.PgnUtility;
import com.dlb.chess.pgn.ResultTagValue;
import com.dlb.chess.pgn.TagUtility;
import com.dlb.chess.test.unwinnability.oracle.LimitedUnwinnabilityOracle;
import com.dlb.chess.test.unwinnability.oracle.enums.LimitedUnwinnabilityVerdict;

public abstract class AbstractLichessCheck {

  static boolean calculateIsTimeForfeitCandidate(PgnFile pgnFile) {
    final String terminationValue = TagUtility.calculateTagValue(pgnFile, "Termination");
    if (!"Time forfeit".equals(terminationValue)) {
      return false;
    }
    final ResultTagValue resultTagValue = TagUtility.readResultTagValue(pgnFile);
    return resultTagValue == ResultTagValue.WHITE_WON || resultTagValue == ResultTagValue.BLACK_WON;
  }

  static boolean calculateIsIncorrectResult(PgnFile pgnFile) {

    final Board boardPerLastMove = PgnUtility.calculateBoard(pgnFile, false);
    final ResultTagValue resultTagValue = TagUtility.readResultTagValue(pgnFile);

    if (boardPerLastMove.getHavingMove() == Side.WHITE && resultTagValue == ResultTagValue.WHITE_WON
        || boardPerLastMove.getHavingMove() == Side.BLACK && resultTagValue == ResultTagValue.BLACK_WON) {
      throw new ProgrammingMistakeException("Should not happen");
    }

    final LimitedUnwinnabilityVerdict verdict = LimitedUnwinnabilityOracle.calculateUnwinnability(boardPerLastMove,
        boardPerLastMove.getHavingMove().getOppositeSide());
    return verdict == LimitedUnwinnabilityVerdict.UNWINNABLE;
  }
}
