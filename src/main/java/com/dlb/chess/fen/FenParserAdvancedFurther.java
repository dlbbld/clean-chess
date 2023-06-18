package com.dlb.chess.fen;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.enums.FenAdvancedFurtherValidationProblem;
import com.dlb.chess.common.exceptions.FenAdvancedFurtherValidationException;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.fen.model.Fen;

/**
 * Class for advanced validation which FEN's from a real chess game must fulfill. However for practicality it makes
 * sense to usually not require this for FEN's. Not currently used, but there might be potential usage, so left for now.
 */
public class FenParserAdvancedFurther implements EnumConstants {

  private FenParserAdvancedFurther() {
  }

  public static void parseFenAdvancedFurther(Fen fen) throws FenAdvancedFurtherValidationException {
    final var maximumPossibleHalfMoveClock = calculateMaximumPossibleHalfMoveClock(fen.fullMoveNumber(),
        fen.havingMove());
    if (fen.halfMoveClock() > maximumPossibleHalfMoveClock) {
      throw new FenAdvancedFurtherValidationException(
          FenAdvancedFurtherValidationProblem.INVALID_HALF_MOVE_CLOCK_TOO_BIG_RELATIVE_TO_FULL_MOVE_NUMBER,
          "the half-move clock \"" + fen.halfMoveClock() + "\" is greater than the maximum possible half-move clock \""
              + maximumPossibleHalfMoveClock + "\" for the specified full-move counter of " + fen.fullMoveNumber());
    }

    if (fen.fullMoveNumber() == 1) {
      switch (fen.havingMove()) {
        case BLACK:
          if (!FenConstants.POSSIBLE_FEN_AFTER_FIRST_HALF_MOVE.contains(fen.fen())) {
            throw new FenAdvancedFurtherValidationException(
                FenAdvancedFurtherValidationProblem.INVALID_FULL_MOVE_NUMBER_ONE_BLACK_IN_NON_POSSIBLE_POSITION,
                "Black can only make the first move, "
                    + "(full move counter of one), if it's any of the 20 possible positions after White's first move");
          }
          break;
        case WHITE:
          if (!FenConstants.FEN_INITIAL_STR.equals(fen.fen())) {
            throw new FenAdvancedFurtherValidationException(
                FenAdvancedFurtherValidationProblem.INVALID_FULL_MOVE_NUMBER_ONE_WHITE_IN_NON_STARTING_POSITION,
                "White can only make the first move, " + "(full move counter of one), in the starting position");
          }
          break;
        case NONE:
        default:
          throw new IllegalArgumentException();
      }
    }
  }

  private static int calculateMaximumPossibleHalfMoveClock(int fullMoveNumber, Side havingMove) {

    final var baseValue = 2 * (fullMoveNumber - 1);
    return switch (havingMove) {
      case BLACK -> baseValue + 1;
      case WHITE -> baseValue;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };

  }
}
