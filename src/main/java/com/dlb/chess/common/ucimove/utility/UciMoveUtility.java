package com.dlb.chess.common.ucimove.utility;

import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.model.UciMove;
import com.dlb.chess.moves.utility.CastlingUtility;

public class UciMoveUtility {

  public static UciMove convertMoveSpecificationToUci(MoveSpecification moveSpecification) {
    Square fromSquare;
    Square toSquare;
    PromotionPieceType promotionPieceType;
    if (CastlingUtility.calculateIsCastlingMove(moveSpecification)) {
      fromSquare = CastlingUtility.calculateKingCastlingFrom(moveSpecification);
      toSquare = CastlingUtility.calculateKingCastlingTo(moveSpecification);
      promotionPieceType = PromotionPieceType.NONE;
    } else {
      fromSquare = moveSpecification.fromSquare();
      toSquare = moveSpecification.toSquare();
      promotionPieceType = moveSpecification.promotionPieceType();
    }

    final String uciMoveStr = UciMoveValidationUtility.calculateUciMoveStr(fromSquare, toSquare, promotionPieceType);

    return UciMoveValidationUtility.lookup(uciMoveStr);
  }

  public static MoveSpecification convertUciMoveToMoveSpecification(ApiBoard board, UciMove uciMove) {
    // we need the board to identify the castling move

    final Square fromSquare = uciMove.fromSquare();
    final Square toSquare = uciMove.toSquare();

    if (uciMove.isPromotion()) {
      return new MoveSpecification(board.getHavingMove(), fromSquare, toSquare, uciMove.promotionPieceType());
    }

    if (board.getStaticPosition().get(fromSquare).getPieceType() == PieceType.KING) {
      final CastlingMove potentialCastlingMove = calculatePotentialCastlingMove(fromSquare, toSquare);
      switch (potentialCastlingMove) {
        case KING_SIDE:
        case QUEEN_SIDE:
          return new MoveSpecification(board.getHavingMove(), potentialCastlingMove);
        case NONE:
          break;
        default:
          throw new IllegalArgumentException();
      }
    }

    return new MoveSpecification(board.getHavingMove(), fromSquare, toSquare);
  }

  public static String convertUciMoveToSan(ApiBoard board, UciMove uciMove) {
    final MoveSpecification moveSpecification = convertUciMoveToMoveSpecification(board, uciMove);
    board.performMove(moveSpecification);
    final String san = board.getSan();
    board.unperformMove();
    return san;
  }

  private static CastlingMove calculatePotentialCastlingMove(Square firstSquare, Square secondSquare) {
    if (firstSquare == CastlingUtility.WHITE_KING_FROM
        && secondSquare == CastlingUtility.WHITE_KING_KING_SIDE_CASTLING_TO) {
      return CastlingMove.KING_SIDE;
    }
    if (firstSquare == CastlingUtility.WHITE_KING_FROM
        && secondSquare == CastlingUtility.WHITE_KING_QUEEN_SIDE_CASTLING_TO) {
      return CastlingMove.QUEEN_SIDE;
    }
    if (firstSquare == CastlingUtility.BLACK_KING_FROM
        && secondSquare == CastlingUtility.BLACK_KING_KING_SIDE_CASTLING_TO) {
      return CastlingMove.KING_SIDE;
    }
    if (firstSquare == CastlingUtility.BLACK_KING_FROM
        && secondSquare == CastlingUtility.BLACK_KING_QUEEN_SIDE_CASTLING_TO) {
      return CastlingMove.QUEEN_SIDE;
    }
    return CastlingMove.NONE;
  }

}
