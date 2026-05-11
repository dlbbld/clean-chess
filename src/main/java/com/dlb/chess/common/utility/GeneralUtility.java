package com.dlb.chess.common.utility;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.LenientPgnParser;
import com.dlb.chess.pgn.PgnFile;
import com.dlb.chess.report.CheckmateOrStalemate;

public abstract class GeneralUtility {

  public static CheckmateOrStalemate calculateLastPositionEvaluation(Board board) {
    // order is crucial
    if (board.isCheckmate()) {
      return CheckmateOrStalemate.CHECKMATE;
    }
    if (board.isStalemate()) {
      return CheckmateOrStalemate.STALEMATE;
    }
    return CheckmateOrStalemate.NA;
  }

  public static Board calculateBoard(PgnFile pgnFile) {

    final Board board = new Board(pgnFile.startFen());

    for (final PgnHalfMove halfMove : pgnFile.halfMoveList()) {
      final String san = halfMove.san();
      board.moveStrict(san);
    }

    return board;
  }

  public static Board calculateBoard(Path folderPath, String pgnFileName) {

    final PgnFile pgnFile = LenientPgnParser.parse(folderPath, pgnFileName);

    return calculateBoard(pgnFile);
  }

  public static String calculateSquareList(Set<Square> squareSet) {
    final List<String> squareList = new ArrayList<>();
    for (final Square square : squareSet) {
      squareList.add(square.getName());
    }
    return BasicUtility.calculateCommaSeparatedList(squareList);
  }

}
