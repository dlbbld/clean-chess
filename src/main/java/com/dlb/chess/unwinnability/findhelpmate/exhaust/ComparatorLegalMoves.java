package com.dlb.chess.unwinnability.findhelpmate.exhaust;

import java.util.Comparator;
import java.util.Set;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.unwinnability.findhelpmate.exhaust.enums.ScoreResult;

public class ComparatorLegalMoves implements Comparator<LegalMove> {

  private final Side color;
  private final Side havingMove;
  private final StaticPosition staticPosition;
  private final Set<Square> attackedSquaresHavingMove;
  private final ApiBoard board;

  public ComparatorLegalMoves(Side color, Side havingMove, StaticPosition staticPosition,
      Set<Square> attackedSquaresHavingMove, ApiBoard board) {
    this.color = color;
    this.havingMove = havingMove;
    this.staticPosition = staticPosition;
    this.attackedSquaresHavingMove = attackedSquaresHavingMove;
    this.board = board;
  }

  @Override
  public int compare(LegalMove firstLegalMove, LegalMove secondLegalMove) {

    // castling moves have least precedence
    if (!CastlingUtility.calculateIsCastlingMove(firstLegalMove.moveSpecification())
        && CastlingUtility.calculateIsCastlingMove(secondLegalMove.moveSpecification())) {
      return -1;
    }
    if (CastlingUtility.calculateIsCastlingMove(firstLegalMove.moveSpecification())
        && !CastlingUtility.calculateIsCastlingMove(secondLegalMove.moveSpecification())) {
      return 1;
    }
    if (CastlingUtility.calculateIsCastlingMove(firstLegalMove.moveSpecification())
        && CastlingUtility.calculateIsCastlingMove(secondLegalMove.moveSpecification())) {
      return 0;
    }

    // in the following we have no castling move which is essential because the moving piece type is not set for the
    // castling move by design
    // using it would cause exceptions

    // intended winner is moving
    if (color == havingMove) {

      // prioritizing checkmating in one does not seem to bering much performance

      // if (isCheckmatingMove(firstLegalMove)) {
      // return -1;
      // }
      // if (isCheckmatingMove(secondLegalMove)) {
      // return 1;
      // }

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
        final var comparePieceCapturedValue = Integer.compare(-firstPieceTypeCapture.getValue(),
            -secondPieceTypeCapture.getValue());

        // capturing higher valued pieces preferred
        if (comparePieceCapturedValue != 0) {
          return comparePieceCapturedValue;
        }

        // capturing with higher valued pieces preferred
        final PieceType firstMovingPiece = firstLegalMove.movingPiece().getPieceType();
        final PieceType secondMovingPiece = secondLegalMove.movingPiece().getPieceType();
        final var comparePieceMoving = Integer.compare(-firstMovingPiece.getValue(), -secondMovingPiece.getValue());
        return comparePieceMoving;
      }

      // pawn moves - preferred
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
        // queen promotion only preferred if no queen
        // multiple queens give too much legal moves
        if (firstLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE
            && secondLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE) {
          final PromotionPieceType firstPromotionPieceType = firstLegalMove.moveSpecification().promotionPieceType();
          final PromotionPieceType secondPromotionPieceType = secondLegalMove.moveSpecification().promotionPieceType();

          // our algorithm likes knights
          if (firstPromotionPieceType == PromotionPieceType.KNIGHT
              && secondPromotionPieceType != PromotionPieceType.KNIGHT) {
            return -1;
          }
          if (firstPromotionPieceType != PromotionPieceType.KNIGHT
              && secondPromotionPieceType == PromotionPieceType.KNIGHT) {
            return 1;
          }

          // our algorithm likes bishops
          if (firstPromotionPieceType == PromotionPieceType.BISHOP
              && secondPromotionPieceType != PromotionPieceType.BISHOP) {
            return -1;
          }
          if (firstPromotionPieceType != PromotionPieceType.BISHOP
              && secondPromotionPieceType == PromotionPieceType.BISHOP) {
            return 1;
          }

          // better rooks than queens for the number of legal moves
          if (firstPromotionPieceType == PromotionPieceType.ROOK
              && secondPromotionPieceType != PromotionPieceType.ROOK) {
            return -1;
          }
          if (firstPromotionPieceType != PromotionPieceType.ROOK
              && secondPromotionPieceType == PromotionPieceType.ROOK) {
            return 1;
          }

          return 0;

        }
      }

      final ScoreResult firstScore = Score.score(color, havingMove, staticPosition, firstLegalMove);
      final ScoreResult secondScore = Score.score(color, havingMove, staticPosition, secondLegalMove);
      // preferred reward - normal - punish
      final var compareScore = Integer.compare(-firstScore.getIncrement(), -secondScore.getIncrement());
      return compareScore;
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
        return -1;
      }

      if (firstLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE
          && secondLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE) {
        final PromotionPieceType firstPromotionPieceType = firstLegalMove.moveSpecification().promotionPieceType();
        final PromotionPieceType secondPromotionPieceType = secondLegalMove.moveSpecification().promotionPieceType();

        // our algorithm likes knights
        if (firstPromotionPieceType == PromotionPieceType.KNIGHT
            && secondPromotionPieceType != PromotionPieceType.KNIGHT) {
          return -1;
        }
        if (firstPromotionPieceType != PromotionPieceType.KNIGHT
            && secondPromotionPieceType == PromotionPieceType.KNIGHT) {
          return 1;
        }

        // our algorithm likes bishops
        if (firstPromotionPieceType == PromotionPieceType.BISHOP
            && secondPromotionPieceType != PromotionPieceType.BISHOP) {
          return -1;
        }
        if (firstPromotionPieceType != PromotionPieceType.BISHOP
            && secondPromotionPieceType == PromotionPieceType.BISHOP) {
          return 1;
        }

        // better rooks than queens for the number of legal moves
        if (firstPromotionPieceType == PromotionPieceType.ROOK && secondPromotionPieceType != PromotionPieceType.ROOK) {
          return -1;
        }
        if (firstPromotionPieceType != PromotionPieceType.ROOK && secondPromotionPieceType == PromotionPieceType.ROOK) {
          return 1;
        }

        return 0;

      }
    }

    final ScoreResult firstScore = Score.score(color, havingMove, staticPosition, firstLegalMove);
    final ScoreResult secondScore = Score.score(color, havingMove, staticPosition, secondLegalMove);
    // preferred reward - normal - punish
    final var compareScore = Integer.compare(-firstScore.getIncrement(), -secondScore.getIncrement());
    return compareScore;
  }

  boolean isCheckmatingMove(LegalMove legalMove) {
    board.performMove(legalMove.moveSpecification());
    final var isCheckmate = board.isCheckmate();
    board.unperformMove();
    return isCheckmate;
  }
}
