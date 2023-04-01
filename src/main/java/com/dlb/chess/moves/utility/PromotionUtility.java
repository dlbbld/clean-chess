package com.dlb.chess.moves.utility;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.model.UpdateSquare;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.model.LegalMove;

public abstract class PromotionUtility implements EnumConstants {

  public static boolean calculateIsPromotionNewMove(MoveSpecification moveSpecification) {
    return moveSpecification.promotionPieceType() != PromotionPieceType.NONE;
  }

  public static boolean calculateIsPromotionLastMove(ApiBoard board) {
    final LegalMove lastMove = board.getLastMove();
    return lastMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE;
  }

  public static boolean calculateIsPromotion(MoveSpecification move) {
    return move.promotionPieceType() != PromotionPieceType.NONE;
  }

  public static List<UpdateSquare> performPromotionMovements(MoveSpecification moveSpecification) {

    final List<UpdateSquare> result = new ArrayList<>();

    result.add(new UpdateSquare(moveSpecification.fromSquare()));
    final Piece promotionPiece = PromotionPieceType.calculate(moveSpecification.havingMove(),
        moveSpecification.promotionPieceType());
    result.add(new UpdateSquare(moveSpecification.toSquare(), promotionPiece));

    return result;
  }

  public static List<UpdateSquare> performPromotionUndoMovements(LegalMove lastMove) {

    final List<UpdateSquare> result = new ArrayList<>();

    if (lastMove.pieceCaptured() != Piece.NONE) {
      result.add(new UpdateSquare(lastMove.moveSpecification().toSquare(), lastMove.pieceCaptured()));
    } else {
      result.add(new UpdateSquare(lastMove.moveSpecification().toSquare()));
    }
    result.add(new UpdateSquare(lastMove.moveSpecification().fromSquare(), lastMove.movingPiece()));

    return result;
  }
}
