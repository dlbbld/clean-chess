package com.dlb.chess.common.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.analysis.enums.CheckmateOrStalemate;
import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.reader.PgnReader;
import com.dlb.chess.pgn.reader.model.PgnFile;

public class GeneralUtility {

  public static CheckmateOrStalemate calculateLastPositionEvaluation(ApiBoard board) {
    // order is crucial
    if (board.isCheckmate()) {
      return CheckmateOrStalemate.CHECKMATE;
    }
    if (board.isStalemate()) {
      return CheckmateOrStalemate.STALEMATE;
    }
    return CheckmateOrStalemate.NA;
  }

  public static boolean calculateIsWhite(Side havingMove) {
    switch (havingMove) {
      case BLACK:
        return false;
      case WHITE:
        return true;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static void logLines(Logger logger, List<String> list) {
    for (final String line : list) {
      logger.info(line);
    }
  }

  public static Board calculateBoard(PgnFile pgnFile) {

    final Board board = new Board(pgnFile.startFen());

    for (final PgnHalfMove halfMove : pgnFile.halfMoveList()) {
      final String san = halfMove.san();
      board.performMove(san);
    }

    return board;
  }

  public static ApiBoard calculateBoard(String folderPath, String pgnFileName) throws Exception {

    final PgnFile pgnFile = PgnReader.readPgn(folderPath, pgnFileName);

    return calculateBoard(pgnFile);
  }

  public static ApiBoard calculateBoard(List<HalfMove> halfMoveList) {

    final var board = new Board();

    for (final HalfMove halfMove : halfMoveList) {
      board.performMove(halfMove.moveSpecification());
    }

    return board;
  }

  public static String calculateSquareList(Set<Square> squareSet) {
    final List<String> squareList = new ArrayList<>();
    for (final Square square : squareSet) {
      squareList.add(square.getName());
    }
    return BasicUtility.calculateCommaSeparatedList(squareList);
  }
}
