package com.dlb.chess.san.validate.movement;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.enums.SanFormat;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;

public abstract class SanValidateMovementPawn extends AbstractSan implements EnumConstants {

  public static void validatePawnMovement(Side havingMove, SanFormat sanFormat, SanConversion sanConversion) {

    switch (sanFormat) {
      case KING_CASTLING_KING_SIDE:
      case KING_CASTLING_QUEEN_SIDE:
      case KING_NON_CASTLING_CAPTURING:
      case KING_NON_CASTLING_NON_CAPTURING:
        throw new IllegalArgumentException();
      case PAWN_NON_CAPTURING_NON_PROMOTION:
      case PAWN_NON_CAPTURING_PROMOTION: {
        validatePawnDestinationRank(havingMove, sanConversion.toSquare().getRank());
        break;
      }
      case PAWN_CAPTURING_NON_PROMOTION:
      case PAWN_CAPTURING_PROMOTION: {
        validatePawnDestinationRank(havingMove, sanConversion.toSquare().getRank());
        validatePawnCapturingDiagonal(havingMove, sanConversion.fromFile(), sanConversion.toSquare().getFile());
        break;
      }
      case RNBQ_CAPTURING_NEITHER:
      case RNBQ_NON_CAPTURING_NEITHER:
      case RNBQ_CAPTURING_FILE:
      case RNBQ_NON_CAPTURING_FILE:
      case RNBQ_CAPTURING_RANK:
      case RNBQ_NON_CAPTURING_RANK:
      case RNBQ_CAPTURING_SQUARE:
      case RNBQ_NON_CAPTURING_SQUARE:
      default:
        throw new IllegalArgumentException();
    }
  }

  private static void validatePawnDestinationRank(Side havingMove, Rank destinationRank) {
    final var isInvalid = !Rank.calculateIsValidRank(havingMove, destinationRank);
    if (isInvalid) {
      final var messageKey = switch (havingMove) {
        case WHITE -> "validation.san.movement.pawn.forward.backwards.white";
        case BLACK -> "validation.san.movement.pawn.forward.backwards.black";
        case NONE -> throw new IllegalArgumentException();
        default -> throw new IllegalArgumentException();
      };
      throw new SanValidationException(SanValidationProblem.MOVEMENT_PAWN_FORWARD_BACKWARDS,
          Message.getString(messageKey, havingMove.getName()));
    }
  }

  private static void validatePawnCapturingDiagonal(Side havingMove, File fromFile, File toFile) {
    final var isAdjacentLeft = File.calculateHasLeftFile(havingMove, fromFile)
        && File.calculateLeftFile(havingMove, fromFile) == toFile;
    final var isAdjacentRight = File.calculateHasRightFile(havingMove, fromFile)
        && File.calculateRightFile(havingMove, fromFile) == toFile;

    if (!isAdjacentLeft && !isAdjacentRight) {
      throw new SanValidationException(SanValidationProblem.MOVEMENT_PAWN_CAPTURE_NON_ADJACENT_FILE,
          Message.getString("validation.san.movement.pawn.capture.nonAdjacentFile"));
    }
  }

}
