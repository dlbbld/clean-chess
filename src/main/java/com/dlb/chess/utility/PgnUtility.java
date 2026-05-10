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
      board.moveStrict(pgnHalfMove.san());
    }
    return board;
  }

  /**
   * Wraps {@code line} into ≤ {@code lineLength}-char lines, splitting on spaces — except inside {@code {...}}
   * brace comments, which are atomic. A brace region longer than {@code lineLength} is emitted on its own line
   * rather than broken: spaces inside commentary are content, not wrap candidates. The 79-char export-format
   * guideline is a soft target, matching python-chess.
   */
  public static List<String> calculateWrappedLines(String line, int lineLength) {
    final List<String> atoms = splitIntoAtoms(line);
    if (atoms.isEmpty()) {
      throw new ProgrammingMistakeException("As the passed text cannot be null the array cannot be empty");
    }
    final List<String> result = new ArrayList<>();
    StringBuilder wrappedLine = new StringBuilder();
    wrappedLine.append(NonNullWrapperCommon.get(atoms, 0));
    for (var i = 1; i < atoms.size(); i++) {
      final String atom = NonNullWrapperCommon.get(atoms, i);
      if (wrappedLine.length() + 1 + atom.length() <= lineLength) {
        wrappedLine.append(" ").append(atom);
      } else {
        result.add(NonNullWrapperCommon.toString(wrappedLine));
        wrappedLine = new StringBuilder();
        wrappedLine.append(atom);
      }
    }
    result.add(NonNullWrapperCommon.toString(wrappedLine));
    return result;
  }

  /**
   * Splits {@code line} into space-separated atoms; each {@code {...}} region is one indivisible atom. Spaces and
   * other characters inside a brace region are content, not separators. Per PGN spec §8.2.5 an inner {@code {} is
   * content too, so only {@code }} ends a region.
   */
  private static List<String> splitIntoAtoms(String line) {
    final List<String> atoms = new ArrayList<>();
    final int len = line.length();
    var i = 0;
    while (i < len) {
      final char c = line.charAt(i);
      if (c == ' ') {
        i++;
        continue;
      }
      if (c == '{') {
        var j = i + 1;
        while (j < len && line.charAt(j) != '}') {
          j++;
        }
        if (j < len) {
          atoms.add(NonNullWrapperCommon.substring(line, i, j + 1));
          i = j + 1;
        } else {
          // Defensive — PgnCreate emits balanced braces, so unreachable in practice.
          atoms.add(NonNullWrapperCommon.substring(line, i));
          i = len;
        }
        continue;
      }
      var j = i;
      while (j < len && line.charAt(j) != ' ' && line.charAt(j) != '{') {
        j++;
      }
      atoms.add(NonNullWrapperCommon.substring(line, i, j));
      i = j;
    }
    return atoms;
  }

}
