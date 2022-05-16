package com.dlb.chess.unwinnability.findhelpmate.exhaust;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.SquareType;
import com.dlb.chess.common.utility.MaterialUtility;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.moves.utility.PromotionUtility;
import com.dlb.chess.unwinnability.findhelpmate.exhaust.enums.Goal;
import com.dlb.chess.unwinnability.findhelpmate.exhaust.enums.ScoreResult;

// Figure 12 Score routine used in Figure 5. Algorithm Going-to-corner is defined in Figure 13.
public class Score {

  // Inputs: position, legal move in the position
  // Output: Normal, Reward, or Punish (variation score)
  public static ScoreResult score(Side color, Side havingMove, StaticPosition staticPosition, LegalMove legalMove) {
    // 1: if it is the intended winner’s turn in pos then
    if (havingMove == color) {
      // 2: if m is a capture or m is a pawn push or Going-to-corner(pos, m, Win) then
      // 3: return Reward
      if (CastlingUtility.calculateIsCastlingMove(legalMove.moveSpecification())) {
        if (GoingToCorner.goingToCorner(color, staticPosition, legalMove, Goal.WIN)) {
          return ScoreResult.REWARD;
        }
      } else if (legalMove.pieceCaptured() != Piece.NONE || legalMove.movingPiece().getPieceType() == PieceType.PAWN
          || GoingToCorner.goingToCorner(color, staticPosition, legalMove, Goal.WIN)) {

        return ScoreResult.REWARD;

      }
      // 4: else ( -> It is the intended loser’s turn in pos)
    } else {

      // 5: if the intended winner has just a knight and the intended loser has just pawns
      // and/or queens or the intended winner has just bishops of the same square color and
      // the intended loser does not have knights or bishops of the opposite color then ( -> The
      // conditions of Lemma 5 or Lemma 6 apply (ignoring the pawn-freeness condition))
      //

      // if the intended winner has just a knight and the intended loser has just pawns
      // and/or queens
      final var isFirstCondition = MaterialUtility.calculateHasKingAndKnightOnly(color, staticPosition)
          && MaterialUtility.calculateHasKingAndPawnsOrQueensOnly(color.getOppositeSide(), staticPosition);

      // or the intended winner has just bishops of the same square color and
      // the intended loser does not have knights or bishops of the opposite color
      final var isSecondConditionDarkSquare = MaterialUtility.calculateHasKingAndBishopsOnly(color, staticPosition,
          SquareType.DARK_SQUARE) && MaterialUtility.calculateHasNoKnights(color.getOppositeSide(), staticPosition)
          && MaterialUtility.calculateHasNoLightSquareBishops(color.getOppositeSide(), staticPosition);

      final var isSecondConditionLightSquare = MaterialUtility.calculateHasKingAndBishopsOnly(color, staticPosition,
          SquareType.LIGHT_SQUARE) && MaterialUtility.calculateHasNoKnights(color.getOppositeSide(), staticPosition)
          && MaterialUtility.calculateHasNoDarkSquareBishops(color.getOppositeSide(), staticPosition);

      if (isFirstCondition || isSecondConditionDarkSquare || isSecondConditionLightSquare) {
        // 6: if m is a promotion to a queen or rook then return Punish
        if (PromotionUtility.calculateIsPromotion(legalMove.moveSpecification())
            && (legalMove.moveSpecification().promotionPieceType() == PromotionPieceType.QUEEN
                || legalMove.moveSpecification().promotionPieceType() == PromotionPieceType.ROOK)) {
          return ScoreResult.PUNISH;
          // 7: else if m is a pawn move then return Reward
        }
        if (legalMove.movingPiece().getPieceType() == PieceType.PAWN) {
          return ScoreResult.REWARD;
        }
      }

      // 8: if Going-to-corner(pos, m, Lose) then return Reward
      if (GoingToCorner.goingToCorner(color, staticPosition, legalMove, Goal.LOSE)) {
        return ScoreResult.REWARD;
      }

      // 9: if m is a capture then return Punish
      if (legalMove.pieceCaptured() != Piece.NONE) {
        return ScoreResult.PUNISH;
      }
    }

    // 10: return Normal ( -> The default output if none of the above conditions hold)
    return ScoreResult.NORMAL;
  }

}