package com.dlb.chess.test.lichess;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.utility.BasicChessUtility;
import com.dlb.chess.pgn.reader.enums.ResultTagValue;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.winnable.WinnableCalculator;
import com.dlb.chess.test.winnable.enums.Winnable;
import com.dlb.chess.utility.PgnUtility;
import com.dlb.chess.utility.TagUtility;

public abstract class AbstractLichessCheck {

  static boolean calculateIsTimeForfeitCandidate(PgnFile pgnFile) {
    final String terminationValue = BasicChessUtility.calculateTagValue(pgnFile, "Termination");
    if (!"Time forfeit".equals(terminationValue)) {
      return false;
    }
    final ResultTagValue resultTagValue = TagUtility.readResultTagValue(pgnFile);
    return resultTagValue == ResultTagValue.WHITE_WON || resultTagValue == ResultTagValue.BLACK_WON;
  }

  static boolean calculateIsIncorrectResult(PgnFile pgnFile) {

    final Board boardPerLastMove = PgnUtility.calculateBoardPerLastMove(pgnFile);
    final ResultTagValue resultTagValue = TagUtility.readResultTagValue(pgnFile);

    if (boardPerLastMove.getHavingMove() == Side.WHITE && resultTagValue == ResultTagValue.WHITE_WON
        || boardPerLastMove.getHavingMove() == Side.BLACK && resultTagValue == ResultTagValue.BLACK_WON) {
      throw new ProgrammingMistakeException("Should not happen");
    }

    final Winnable winnable = WinnableCalculator.calculateWinnable(boardPerLastMove,
        boardPerLastMove.getHavingMove().getOppositeSide());
    return winnable == Winnable.NO;
  }
}
