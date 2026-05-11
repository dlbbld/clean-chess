package com.dlb.chess.moves;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.model.UpdateSquare;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.model.MoveSpecification;

public abstract class PromotionUtility implements EnumConstants {

  public static boolean calculateIsPromotionNewMove(MoveSpecification moveSpecification) {
    return moveSpecification.promotionPieceType() != PromotionPieceType.NONE;
  }

  public static boolean calculateIsPromotion(MoveSpecification move) {
    return move.promotionPieceType() != PromotionPieceType.NONE;
  }

  public static List<UpdateSquare> performPromotionMovements(Side havingMove, MoveSpecification moveSpecification) {

    final List<UpdateSquare> result = new ArrayList<>();

    result.add(new UpdateSquare(moveSpecification.fromSquare()));
    final Piece promotionPiece = PromotionPieceType.calculate(havingMove, moveSpecification.promotionPieceType());
    result.add(new UpdateSquare(moveSpecification.toSquare(), promotionPiece));

    return result;
  }

}
