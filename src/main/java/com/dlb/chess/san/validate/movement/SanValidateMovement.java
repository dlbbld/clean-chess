package com.dlb.chess.san.validate.movement;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.enums.SanFormat;
import com.dlb.chess.san.model.SanParse;

public abstract class SanValidateMovement extends AbstractSan implements EnumConstants {

  public static void validateMovement(SanParse sanParse, Side havingMove) {
    final var sanType = sanParse.sanType();
    final var sanConversion = sanParse.sanConversion();
    final SanFormat sanFormat = sanType.getSanFormat();

    if (sanType.getMovingPieceType() == PieceType.PAWN) {
      SanValidateMovementPawn.validatePawnMovement(havingMove, sanFormat, sanConversion);
      return;
    }

    if (sanType.getMovingPieceType() != PieceType.KING) {
      SanValidateMovementRnbq.validateRnbqMovement(sanParse);
    }

  }

}
