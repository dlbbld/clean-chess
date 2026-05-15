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
    return calculateBoard(pgnFile, true);
  }

  /**
   * Replays the half-moves of {@code pgnFile} on a fresh board and returns the resulting state. The {@code
   * detectDeadPositionUnwinnable} flag is forwarded to the underlying {@link Board} constructor — set it to
   * {@code false} when the PGN is allowed to pass through a position the quick unwinnability analyzer would
   * classify as dead (for example, recorded games used as test fixtures whose final position is intentionally
   * dead). The mechanical insufficient-material termination is unaffected.
   */
  public static Board calculateBoard(PgnFile pgnFile, boolean detectDeadPositionUnwinnable) {

    final Board board = new Board(pgnFile.startFen(), detectDeadPositionUnwinnable);

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
