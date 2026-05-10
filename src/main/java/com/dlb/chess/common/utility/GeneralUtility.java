package com.dlb.chess.common.utility;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.interfaces.ChessBoard;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.model.UciMove;
import com.dlb.chess.pgn.parser.LenientPgnParser;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.report.enums.CheckmateOrStalemate;

public abstract class GeneralUtility {

  public static CheckmateOrStalemate calculateLastPositionEvaluation(ChessBoard board) {
    // order is crucial
    if (board.isCheckmate()) {
      return CheckmateOrStalemate.CHECKMATE;
    }
    if (board.isStalemate()) {
      return CheckmateOrStalemate.STALEMATE;
    }
    return CheckmateOrStalemate.NA;
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
      board.moveStrict(san);
    }

    return board;
  }

  public static ChessBoard calculateBoard(Path folderPath, String pgnFileName) {

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

  public static String composeCheckmateLine(List<UciMove> uciMoveList) {
    final List<String> uciMoveStrList = new ArrayList<>();

    for (final UciMove uciMove : uciMoveList) {
      uciMoveStrList.add(uciMove.text());
    }

    return BasicUtility.calculateSpaceSeparatedList(uciMoveStrList);
  }
}
