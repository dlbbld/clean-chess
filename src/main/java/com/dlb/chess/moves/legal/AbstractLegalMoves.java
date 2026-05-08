package com.dlb.chess.moves.legal;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.analyze.ChessRuleAnalyzer;
import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.utility.StaticPositionUtility;
import com.dlb.chess.enums.KingSafetyCheck;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.model.LegalMoveCalculation;
import com.dlb.chess.model.PseudoLegalMove;
import com.dlb.chess.moves.legal.king.KingLegalMoves;
import com.dlb.chess.moves.legal.pawn.PawnLegalMoves;
import com.dlb.chess.moves.legal.pieces.BishopLegalMoves;
import com.dlb.chess.moves.legal.pieces.KnightLegalMoves;
import com.dlb.chess.moves.legal.pieces.QueenLegalMoves;
import com.dlb.chess.moves.legal.pieces.RookLegalMoves;
import com.google.common.collect.ImmutableSet;

public abstract class AbstractLegalMoves implements EnumConstants {

  protected static void checkPiece(Side havingMove, Piece candidatePiece, PieceType expectedPieceType)
      throws IllegalArgumentException {
    if (candidatePiece == Piece.NONE || candidatePiece.getSide() != havingMove
        || candidatePiece.getPieceType() != expectedPieceType) {
      throw new IllegalArgumentException(
          "The source square must be occupied by a " + havingMove + " " + expectedPieceType);
    }
  }

  public static ImmutableSet<LegalMove> calculateLegalMoves(StaticPosition staticPosition, Side havingMove,
      CastlingRight castlingRight, final Square enPassantCaptureTargetSquare) {
    return NonNullWrapperCommon.copyOfSet(
        calculateLegalMovesBottomUp(staticPosition, havingMove, castlingRight, enPassantCaptureTargetSquare));
  }

  public static Set<LegalMove> calculateLegalMovesBottomUp(StaticPosition staticPosition,
      Square enPassantCaptureTargetSquare, CastlingRight castlingRight, Side havingMove, Square fromSquare) {
    final PieceType pieceType = staticPosition.get(fromSquare).getPieceType();
    return switch (pieceType) {
      case PAWN -> PawnLegalMoves.calculatePawnLegalMoves(staticPosition, enPassantCaptureTargetSquare, havingMove,
          fromSquare);
      case ROOK -> RookLegalMoves.calculateRookLegalMoves(staticPosition, havingMove, fromSquare);
      case KNIGHT -> KnightLegalMoves.calculateKnightLegalMoves(staticPosition, havingMove, fromSquare);
      case BISHOP -> BishopLegalMoves.calculateBishopLegalMoves(staticPosition, havingMove, fromSquare);
      case QUEEN -> QueenLegalMoves.calculateQueenLegalMoves(staticPosition, havingMove, fromSquare);
      case KING -> KingLegalMoves.calculateKingLegalMoves(staticPosition, castlingRight, havingMove, fromSquare);
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static Set<LegalMove> calculateLegalMovesBottomUp(StaticPosition staticPosition, Side havingMove,
      CastlingRight castlingRight, final Square enPassantCaptureTargetSquare) {

    final Set<LegalMove> resultSet = new TreeSet<>();
    for (final Square fromSquare : Square.REAL) {
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
    return calculateLegalMoveCalculation(staticPosition, havingMove, fromSquare, toSquareSet).legalMoveSet();
  }

  public static LegalMoveCalculation calculateLegalMoveCalculation(StaticPosition staticPosition, Side havingMove,
      Square fromSquare, Set<Square> toSquareSet) {

    final Piece movingPiece = staticPosition.get(fromSquare);

    final Set<LegalMove> legalMoveSet = new TreeSet<>();
    final Set<PseudoLegalMove> pseudoLegalMoveSet = new TreeSet<>();

    for (final Square toSquare : toSquareSet) {
      final MoveSpecification moveSpecification = new MoveSpecification(fromSquare, toSquare);
      final var pieceCaptured = staticPosition.isEmpty(toSquare) ? Piece.NONE : staticPosition.get(toSquare);

      if (pieceCaptured != Piece.NONE && pieceCaptured.getPieceType() == PieceType.KING) {
        continue;
      }

      if (ChessRuleAnalyzer.isMoveKingSafe(staticPosition, havingMove, moveSpecification)) {
        final LegalMove legalMove = new LegalMove(moveSpecification, movingPiece, pieceCaptured);
        legalMoveSet.add(legalMove);
      } else {
        final PseudoLegalMove pseudoLegalMove = new PseudoLegalMove(moveSpecification, movingPiece, pieceCaptured);
        pseudoLegalMoveSet.add(pseudoLegalMove);
      }
    }
    final KingSafetyCheck pseudoLegalKingSafety;
    if (!legalMoveSet.isEmpty() || pseudoLegalMoveSet.isEmpty()) {
      pseudoLegalKingSafety = KingSafetyCheck.SUCCESS;
    } else if (StaticPositionUtility.calculateIsCheck(staticPosition, havingMove)) {
      pseudoLegalKingSafety = KingSafetyCheck.NON_KING_LEFT_IN_CHECK;
    } else {
      pseudoLegalKingSafety = KingSafetyCheck.NON_KING_EXPOSED_TO_CHECK;
    }
    return new LegalMoveCalculation(legalMoveSet, pseudoLegalMoveSet, pseudoLegalKingSafety);
  }

}
