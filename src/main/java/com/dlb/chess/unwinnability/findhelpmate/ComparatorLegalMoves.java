package com.dlb.chess.unwinnability.findhelpmate;

import java.util.Comparator;
import java.util.Set;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.unwinnability.findhelpmate.enums.ScoreResult;

public class ComparatorLegalMoves implements Comparator<LegalMove> {

  private final Side color;
  private final Side havingMove;
  private final StaticPosition staticPosition;
  private final Set<Square> attackedSquaresHavingMove;

  public ComparatorLegalMoves(Side color, Side havingMove, StaticPosition staticPosition,
      Set<Square> attackedSquaresHavingMove) {
    this.color = color;
    this.havingMove = havingMove;
    this.staticPosition = staticPosition;
    this.attackedSquaresHavingMove = attackedSquaresHavingMove;
  }

  @Override
  public int compare(LegalMove firstLegalMove, LegalMove secondLegalMove) {
    final ScoreResult firstScore = Score.score(color, havingMove, staticPosition, firstLegalMove);
    final ScoreResult secondScore = Score.score(color, havingMove, staticPosition, secondLegalMove);
    // preferred reward - normal - punish
    final var compareScore = Integer.compare(-firstScore.getIncrement(), -secondScore.getIncrement());

    if (compareScore != 0) {
      return compareScore;
    }

    // intended winner is moving
    if (color == havingMove) {

      // captures - preferred - and if both are capturing, capturing a higher value piece preferred
      if (firstLegalMove.pieceCaptured() != Piece.NONE && secondLegalMove.pieceCaptured() == Piece.NONE) {
        return -1;
      }
      if (firstLegalMove.pieceCaptured() == Piece.NONE && secondLegalMove.pieceCaptured() != Piece.NONE) {
        return 1;
      }
      if (firstLegalMove.pieceCaptured() != Piece.NONE && secondLegalMove.pieceCaptured() != Piece.NONE) {
        final PieceType firstPieceTypeCapture = firstLegalMove.pieceCaptured().getPieceType();
        final PieceType secondPieceTypeCapture = secondLegalMove.pieceCaptured().getPieceType();
        return Integer.compare(-firstPieceTypeCapture.getValue(), -secondPieceTypeCapture.getValue());
      }

      // pawn moves - preferred
      if (!CastlingUtility.calculateIsCastlingMove(firstLegalMove.moveSpecification())
          && !CastlingUtility.calculateIsCastlingMove(secondLegalMove.moveSpecification())) {
        if (firstLegalMove.movingPiece().getPieceType() == PieceType.PAWN
            && secondLegalMove.movingPiece().getPieceType() != PieceType.PAWN) {
          return -1;
        }
        if (firstLegalMove.movingPiece().getPieceType() != PieceType.PAWN
            && secondLegalMove.movingPiece().getPieceType() == PieceType.PAWN) {
          return 1;
        }
        // promotion preferred
        if (firstLegalMove.movingPiece().getPieceType() == PieceType.PAWN
            && secondLegalMove.movingPiece().getPieceType() == PieceType.PAWN) {
          if (firstLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE
              && secondLegalMove.moveSpecification().promotionPieceType() == PromotionPieceType.NONE) {
            return -1;
          }
          if (firstLegalMove.moveSpecification().promotionPieceType() == PromotionPieceType.NONE
              && secondLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE) {
            return 1;
          }
          // queen promotion most preferred
          if (firstLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE
              && secondLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE) {
            final PromotionPieceType firstPromotionPieceType = firstLegalMove.moveSpecification().promotionPieceType();
            final PromotionPieceType secondPromotionPieceType = secondLegalMove.moveSpecification()
                .promotionPieceType();
            return Integer.compare(-firstPromotionPieceType.getPieceType().getValue(),
                -secondPromotionPieceType.getPieceType().getValue());
          }

        }
      }

      // castling moves - avoid as long as possible
      if (!CastlingUtility.calculateIsCastlingMove(firstLegalMove.moveSpecification())
          && CastlingUtility.calculateIsCastlingMove(secondLegalMove.moveSpecification())) {
        return -1;
      }
      if (CastlingUtility.calculateIsCastlingMove(firstLegalMove.moveSpecification())
          && !CastlingUtility.calculateIsCastlingMove(secondLegalMove.moveSpecification())) {
        return 1;
      }

      return 0;
    }

    // intended loser is moving

    // losing piece, preferred
    if (attackedSquaresHavingMove.contains(firstLegalMove.moveSpecification().toSquare())
        && !attackedSquaresHavingMove.contains(secondLegalMove.moveSpecification().toSquare())) {
      return -1;
    }
    if (!attackedSquaresHavingMove.contains(firstLegalMove.moveSpecification().toSquare())
        && attackedSquaresHavingMove.contains(secondLegalMove.moveSpecification().toSquare())) {
      return 1;
    }
    // the higher value the losing piece, the better
    if (attackedSquaresHavingMove.contains(firstLegalMove.moveSpecification().toSquare())
        && attackedSquaresHavingMove.contains(firstLegalMove.moveSpecification().toSquare())) {
      return Integer.compare(-firstLegalMove.movingPiece().getPieceType().getValue(),
          -secondLegalMove.movingPiece().getPieceType().getValue());
    }

    // captures - not preferred - and if both are capturing, capturing a lower value piece preferred
    if (firstLegalMove.pieceCaptured() != Piece.NONE && secondLegalMove.pieceCaptured() == Piece.NONE) {
      return 1;
    }
    if (firstLegalMove.pieceCaptured() == Piece.NONE && secondLegalMove.pieceCaptured() != Piece.NONE) {
      return -1;
    }
    if (firstLegalMove.pieceCaptured() != Piece.NONE && secondLegalMove.pieceCaptured() != Piece.NONE) {
      final PieceType firstPieceTypeCapture = firstLegalMove.pieceCaptured().getPieceType();
      final PieceType secondPieceTypeCapture = secondLegalMove.pieceCaptured().getPieceType();
      return Integer.compare(firstPieceTypeCapture.getValue(), secondPieceTypeCapture.getValue());
    }

    // pawn moves - preferred
    if (!CastlingUtility.calculateIsCastlingMove(firstLegalMove.moveSpecification())
        && !CastlingUtility.calculateIsCastlingMove(secondLegalMove.moveSpecification())) {
      if (firstLegalMove.movingPiece().getPieceType() == PieceType.PAWN
          && secondLegalMove.movingPiece().getPieceType() != PieceType.PAWN) {
        return -1;
      }
      if (firstLegalMove.movingPiece().getPieceType() != PieceType.PAWN
          && secondLegalMove.movingPiece().getPieceType() == PieceType.PAWN) {
        return 1;
      }

      // promotion not preferred
      if (firstLegalMove.movingPiece().getPieceType() == PieceType.PAWN
          && secondLegalMove.movingPiece().getPieceType() == PieceType.PAWN) {
        if (firstLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE
            && secondLegalMove.moveSpecification().promotionPieceType() == PromotionPieceType.NONE) {
          return 1;
        }
        if (firstLegalMove.moveSpecification().promotionPieceType() == PromotionPieceType.NONE
            && secondLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE) {
          return -11;
        }
        // queen promotion least preferred

        if (firstLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE
            && secondLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE) {
          final PromotionPieceType firstPromotionPieceType = firstLegalMove.moveSpecification().promotionPieceType();
          final PromotionPieceType secondPromotionPieceType = secondLegalMove.moveSpecification().promotionPieceType();
          return Integer.compare(firstPromotionPieceType.getPieceType().getValue(),
              secondPromotionPieceType.getPieceType().getValue());
        }

      }
    }

    // castling moves - avoid as long as possible
    if (!CastlingUtility.calculateIsCastlingMove(firstLegalMove.moveSpecification())
        && CastlingUtility.calculateIsCastlingMove(secondLegalMove.moveSpecification())) {
      return -1;
    }
    if (CastlingUtility.calculateIsCastlingMove(firstLegalMove.moveSpecification())
        && !CastlingUtility.calculateIsCastlingMove(secondLegalMove.moveSpecification())) {
      return 1;
    }
    return 0;
  }

}
