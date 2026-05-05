package com.dlb.chess.utility;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.parser.model.PgnFile;

public abstract class PgnUtility {

  public static Board calculateBoardPerLastMove(PgnFile pgnFile) {
    final Board board = new Board(pgnFile.startFen());
    for (final PgnHalfMove pgnHalfMove : pgnFile.halfMoveList()) {
      board.performMove(pgnHalfMove.san());
    }
    return board;
  }

  public static List<String> calculateWrappedLines(String line, int lineLength) {
    final List<String> result = new ArrayList<>();
    final var blockArray = line.split(" ");

    if (blockArray.length == 0) {
      throw new ProgrammingMistakeException("As the passed text cannot be null the array cannot be empty");
    }
    final var firstBlock = blockArray[0];
    StringBuilder wrappedLine = new StringBuilder();
    // we add the first block unconditionally without leading space
    // if checking and then first adding line as for other elements that would produce an empty line
    wrappedLine.append(firstBlock);

    // we add the remaining blocks with a leading space
    if (blockArray.length >= 2) {
      for (var i = 1; i < blockArray.length; i++) {
        // +1 for the space we also need to append
        final var block = blockArray[i];
        if (wrappedLine.length() + 1 + block.length() <= lineLength) {
          wrappedLine.append(" ").append(block);
        } else {
          result.add(NonNullWrapperCommon.toString(wrappedLine));
          wrappedLine = new StringBuilder();
          wrappedLine.append(block);
        }
      }
    }
    result.add(NonNullWrapperCommon.toString(wrappedLine));
    return result;
  }

}
