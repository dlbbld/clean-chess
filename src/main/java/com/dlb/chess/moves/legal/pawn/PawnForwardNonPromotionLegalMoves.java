package com.dlb.chess.moves.legal.pawn;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.utility.StaticPositionUtility;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.squares.to.potential.PawnPotentialToSquares;

class PawnForwardNonPromotionLegalMoves extends PawnLegalMoves {

  public static Set<LegalMove> calculateLegalMoves(StaticPosition staticPosition, Side havingMove, Square fromSquare) {

    final Piece movingPiece = staticPosition.get(fromSquare);
    checkPiece(havingMove, movingPiece, PAWN);

    final Set<LegalMove> legalMoveSet = new TreeSet<>();

    final Set<Square> pawnPotentialToSquareSet = PawnPotentialToSquares
        .calculatePawnPotentialAdvanceToSquares(staticPosition, fromSquare, havingMove);

    for (final Square toSquare : pawnPotentialToSquareSet) {
      if (!Rank.calculateIsPromotionRank(havingMove, toSquare.getRank())) {
        final MoveSpecification moveSpecification = new MoveSpecification(havingMove, fromSquare, toSquare);
        if (!StaticPositionUtility.calculateIsEvaluateAttackingKing(staticPosition, moveSpecification)) {
          final Piece pieceCaptured = staticPosition.get(toSquare);
          final LegalMove legalMove = new LegalMove(moveSpecification, movingPiece, pieceCaptured);
          legalMoveSet.add(legalMove);
        }
      }
    }

    return legalMoveSet;
  }
}
