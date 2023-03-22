package com.dlb.chess.moves.legal;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.utility.StaticPositionUtility;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.legal.king.KingLegalMoves;
import com.dlb.chess.moves.legal.pawn.PawnLegalMoves;
import com.dlb.chess.moves.legal.pieces.BishopLegalMoves;
import com.dlb.chess.moves.legal.pieces.KnightLegalMoves;
import com.dlb.chess.moves.legal.pieces.QueenLegalMoves;
import com.dlb.chess.moves.legal.pieces.RookLegalMoves;

public abstract class AbstractLegalMoves implements EnumConstants {

  protected static void checkPiece(Side havingMove, Piece candidatePiece, PieceType expectedPieceType)
      throws IllegalArgumentException {
    if (candidatePiece == Piece.NONE || candidatePiece.getSide() != havingMove
        || candidatePiece.getPieceType() != expectedPieceType) {
      throw new IllegalArgumentException(
          "The source square must be occupied by a " + havingMove + " " + expectedPieceType);
    }
  }

  public static Set<LegalMove> calculateLegalMoves(StaticPosition staticPosition, Side havingMove,
      CastlingRight castlingRight, final Square enPassantCaptureTargetSquare) {
    return calculateLegalMovesBottomUp(staticPosition, havingMove, castlingRight, enPassantCaptureTargetSquare);
  }

  public static Set<LegalMove> calculateLegalMovesBottomUp(StaticPosition staticPosition,
      Square enPassantCaptureTargetSquare, CastlingRight castlingRight, Side havingMove, Square fromSquare) {
    final PieceType pieceType = staticPosition.get(fromSquare).getPieceType();
    return switch (pieceType) {
      case BISHOP -> BishopLegalMoves.calculateBishopLegalMoves(staticPosition, havingMove, fromSquare);
      case KING -> KingLegalMoves.calculateKingLegalMoves(staticPosition, castlingRight, havingMove, fromSquare);
      case KNIGHT -> KnightLegalMoves.calculateKnightLegalMoves(staticPosition, havingMove, fromSquare);
      case QUEEN -> QueenLegalMoves.calculateQueenLegalMoves(staticPosition, havingMove, fromSquare);
      case ROOK -> RookLegalMoves.calculateRookLegalMoves(staticPosition, havingMove, fromSquare);
      case PAWN -> PawnLegalMoves.calculatePawnLegalMoves(staticPosition, enPassantCaptureTargetSquare, havingMove,
          fromSquare);
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static Set<LegalMove> calculateLegalMovesBottomUp(StaticPosition staticPosition, Side havingMove,
      CastlingRight castlingRight, final Square enPassantCaptureTargetSquare) {

    final Set<LegalMove> resultSet = new TreeSet<>();
    for (final Square fromSquare : Square.BOARD_SQUARE_LIST) {
      if (staticPosition.isOwnPiece(fromSquare, havingMove)) {
        final Set<LegalMove> currentMovingPieceSet = calculateLegalMovesBottomUp(staticPosition,
            enPassantCaptureTargetSquare, castlingRight, havingMove, fromSquare);
        resultSet.addAll(currentMovingPieceSet);
      }
    }
    return resultSet;
  }

  public static Set<LegalMove> calculateLegalMoveSet(StaticPosition staticPosition, Side havingMove, Square fromSquare,
      Set<Square> toSquareSet) {

    final Piece movingPiece = staticPosition.get(fromSquare);

    final Set<LegalMove> legalMoveSet = new TreeSet<>();

    for (final Square toSquare : toSquareSet) {
      final MoveSpecification moveSpecification = new MoveSpecification(havingMove, fromSquare, toSquare);
      if (!StaticPositionUtility.calculateIsEvaluateAttackingKing(staticPosition, moveSpecification)) {
        if (staticPosition.isEmpty(moveSpecification.toSquare())) {
          final LegalMove legalMove = new LegalMove(moveSpecification, movingPiece, Piece.NONE);
          legalMoveSet.add(legalMove);
        } else {
          final Piece pieceCaptured = staticPosition.get(moveSpecification.toSquare());
          if (pieceCaptured.getPieceType() != PieceType.KING) {
            final LegalMove legalMove = new LegalMove(moveSpecification, movingPiece, pieceCaptured);
            legalMoveSet.add(legalMove);
          }
        }
      }
    }
    return legalMoveSet;
  }

}
