package com.dlb.chess.pgn.parser;

import java.util.List;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.fen.FenParserAdvanced;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.pgn.parser.model.Tag;
import com.dlb.chess.utility.TagUtility;

public abstract class AbstractPgnParser {

  public static List<MoveSpecification> calculateMoveSpecificationList(PgnFile pgnFile) {
    final Fen startFen = pgnFile.startFen();

    final ApiBoard board = new Board(startFen);

    for (final PgnHalfMove pgnHalfMove : pgnFile.halfMoveList()) {
      board.performMove(pgnHalfMove.san());
    }
    return board.getPerformedMoveSpecificationList();
  }

  private static String calculateStartFenStr(List<Tag> tagList, boolean isStartFromPosition) {
    if (!isStartFromPosition) {
      return FenConstants.FEN_INITIAL_STR;
    }
    return TagUtility.readFen(tagList);
  }

  static Fen calculateStartFen(List<Tag> tagList, boolean isStartFromPosition) {
    final String startFenStr = calculateStartFenStr(tagList, isStartFromPosition);
    return FenParserAdvanced.parseFenAdvanced(startFenStr);
  }

  static void removeFenIfInitial(List<Tag> tagList, Fen startFen) {
    if (startFen.equals(FenConstants.FEN_INITIAL)) {
      if (TagUtility.hasFen(tagList)) {
        TagUtility.removeFenTag(tagList);
      }
      if (TagUtility.hasSetUp(tagList)) {
        TagUtility.removeSetUpTag(tagList);
      }
    }
  }

}
