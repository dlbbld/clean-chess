package com.dlb.chess.san.validate;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.enums.SanFormat;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanParse;

public abstract class SanValidateNonMovement extends AbstractSan {

  public static void validateNonMovement(SanParse sanParse) {
    final SanFormat sanFormat = sanParse.sanType().getSanFormat();
    switch (sanFormat) {
      case PIECE_NON_CAPTURING_SQUARE:
      case PIECE_CAPTURING_SQUARE: {
        final SanConversion sanConversion = sanParse.sanConversion();
        final Square fromSquare = AbstractSan.calculateFromSquare(sanConversion);
        final Square toSquare = sanConversion.toSquare();
        if (fromSquare == toSquare) {
          throw new SanValidationException(
              SanValidationProblem.NON_MOVEMENT_RNBQ_SOURCE_SQUARE_EQUALS_DESTINATION_SQUARE,
              Message.getString("validation.san.nonMovement.sourceSquareEqualsDestinationSquare.rnbq",
                  fromSquare.getName(), toSquare.getName()));
        }
        break;
      }
      default:
    }
  }

}
