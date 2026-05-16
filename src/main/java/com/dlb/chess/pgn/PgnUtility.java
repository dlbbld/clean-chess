package com.dlb.chess.pgn;

import java.nio.file.Path;

import com.dlb.chess.board.Board;
import com.dlb.chess.model.PgnHalfMove;

public abstract class PgnUtility {

  /**
   * Replays the half-moves of {@code pgnFile} on a fresh board and returns the resulting state. The {@code
   * detectDeadPositionUnwinnable} flag is forwarded to the underlying {@link Board} constructor — set it to
   * {@code false} when the PGN is allowed to pass through a position the quick unwinnability analyzer would classify as
   * dead (for example, recorded games used as test fixtures whose final position is intentionally dead). The mechanical
   * insufficient-material termination is unaffected.
   */
  public static Board calculateBoard(PgnGame pgnGame, boolean detectDeadPositionUnwinnable) {

    final Board board = new Board(pgnGame.startFen(), detectDeadPositionUnwinnable);

    for (final PgnHalfMove halfMove : pgnGame.halfMoveList()) {
      final String san = halfMove.san();
      board.moveStrict(san);
    }

    return board;
  }

  public static Board calculateBoard(Path folderPath, String pgnFileName, boolean detectDeadPositionUnwinnable) {
    final PgnGame pgnGame = LenientPgnParser.parse(folderPath, pgnFileName);
    return calculateBoard(pgnGame, detectDeadPositionUnwinnable);
  }

}
