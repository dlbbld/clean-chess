package com.dlb.chess.unwinnability.findhelpmate.exhaust;

import java.util.Set;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.moves.utility.PromotionUtility;
import com.dlb.chess.squares.to.threaten.AbstractThreatenSquares;
import com.dlb.chess.unwinnability.findhelpmate.exhaust.enums.Goal;
import com.dlb.chess.unwinnability.findhelpmate.exhaust.enums.ScoreResult;

// Figure 12 Score routine used in Figure 5. Algorithm Going-to-corner is defined in Figure 13.
public class Score {

  public static ScoreResult scoreClassicalCheckmate(Side havingMove, StaticPosition staticPosition,
      LegalMove legalMove) {
    if (legalMove.pieceCaptured() != Piece.NONE) {
      return ScoreResult.REWARD;
    }
    final Set<Square> squaresAttackedByNotHavingMove = AbstractThreatenSquares
        .calculateThreatenedSquares(staticPosition, havingMove.getOppositeSide());

    if (squaresAttackedByNotHavingMove.contains(legalMove.moveSpecification().toSquare())) {
      return ScoreResult.REWARD;
    }

    return ScoreResult.NORMAL;

  }

  // Inputs: position, legal move in the position
  // Output: Normal, Reward, or Punish (variation score)
  public static ScoreResult score(Side color, Side havingMove, StaticPosition staticPosition, LegalMove legalMove) {
    var variation = ScoreResult.NORMAL;

    // 1: if it is the intended winner’s turn in pos then
    if (havingMove == color) {
      // 2: if m is a capture or m is a pawn push or Going-to-corner(pos, m, Win) then
      // 3: return Reward
      if (calculateIsCapture(legalMove) || calculateIsAdvancedPawnPush(legalMove)
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

      // Note: We are not immediately returning the value when evaluated as in the PDF, but also evaluating the
      // later condition as in the code. Must be checked what is todot.
      final var isNeedLoserPromotion = FindHelpmateExhaust.calculateIsNeedLoserPromotion(color, staticPosition);
      if (isNeedLoserPromotion) {
        // 6: if m is a promotion to a queen or rook then return Punish
        // Note: we implement the code with differences to the PDF for this case
        final var isHeavyPromotion = calculateIsPromotionToHeavyPiece(legalMove);
        final var isPawnMove = calculateIsPawnMove(legalMove);
        // 7: else if m is a pawn move then return Reward
        // Note: the code does not uphelp this
        variation = isPawnMove && !isHeavyPromotion ? ScoreResult.REWARD : ScoreResult.PUNISH;
      }

      // 8: if Going-to-corner(pos, m, Lose) then return Reward
      if (GoingToCorner.goingToCorner(color, staticPosition, legalMove, Goal.LOSE)) {
        variation = ScoreResult.REWARD;
      }

      // 9: if m is a capture then return Punish
      if (calculateIsCapture(legalMove)) {
        variation = ScoreResult.PUNISH;
      }
    }

    // 10: return Normal ( -> The default output if none of the above conditions hold)
    // Note: Not what the code is doing
    return variation;
  }

  private static boolean calculateIsCapture(LegalMove legalMove) {
    return legalMove.pieceCaptured() != Piece.NONE;
  }

  private static boolean calculateIsAdvancedPawnPush(LegalMove legalMove) {

    if (!calculateIsPawnMove(legalMove)) {
      return false;
    }

    return calculateIsAdvancedRank(legalMove.moveSpecification().havingMove(),
        legalMove.moveSpecification().toSquare().getRank());
  }

  private static boolean calculateIsPawnMove(LegalMove legalMove) {
    // in the castling we don't set the king as the moving piece. querying the
    // moving piece type would trigger an error as the moving piece is not set.
    // so we must treat it separately, otherwise there is a runtime exception.
    if (CastlingUtility.calculateIsCastlingMove(legalMove.moveSpecification())) {
      return false;
    }
    return legalMove.movingPiece().getPieceType() == PieceType.PAWN;
  }

  private static boolean calculateIsAdvancedRank(Side side, Rank rank) {
    switch (side) {
      case WHITE:
        return switch (rank) {
          case RANK_1, RANK_2, RANK_3, RANK_4, RANK_5 -> false;
          case RANK_6, RANK_7, RANK_8 -> true;
          case NONE -> throw new IllegalArgumentException();
          default -> throw new IllegalArgumentException();
        };
      case BLACK:
        return switch (rank) {
          case RANK_1, RANK_2, RANK_3 -> true;
          case RANK_4, RANK_5, RANK_6, RANK_7, RANK_8 -> false;
          case NONE -> throw new IllegalArgumentException();
          default -> throw new IllegalArgumentException();
        };
      case NONE:
      default:
        throw new IllegalArgumentException();

    }
  }

  private static boolean calculateIsPromotionToHeavyPiece(LegalMove legalMove) {
    if (PromotionUtility.calculateIsPromotion(legalMove.moveSpecification())
        && (legalMove.moveSpecification().promotionPieceType() == PromotionPieceType.QUEEN
            || legalMove.moveSpecification().promotionPieceType() == PromotionPieceType.ROOK)) {
      return true;
    }
    return false;
  }

}
