package com.dlb.chess.san.validate;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.moves.utility.EnPassantCaptureUtility;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.enums.SanType;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;

public abstract class SanValidateDestination extends AbstractSan implements EnumConstants {

  public static void validateDestinationSquareSemantics(ApiBoard board, Side havingMove, SanType sanType,
      SanConversion sanConversion) {
    if (SanType.calculateIsKingCastlingMove(sanType)) {
      return;
    }

    final Square toSquare = sanConversion.toSquare();
    final StaticPosition staticPosition = board.getStaticPosition();
    final Piece pieceOnToSquare = staticPosition.get(toSquare);

    if (pieceOnToSquare != Piece.NONE) {
      // own piece on destination
      if (pieceOnToSquare.getSide() == havingMove) {
        if (sanType.isCapture()) {
          throw new SanValidationException(SanValidationProblem.DESTINATION_OWN_PIECE_CAPTURING,
              Message.getString("validation.san.destination.ownPiece.capturing", toSquare.getName()));
        }
        throw new SanValidationException(SanValidationProblem.DESTINATION_OWN_PIECE_NON_CAPTURING,
            Message.getString("validation.san.destination.ownPiece.nonCapturing", toSquare.getName()));
      }

      // opponent piece on destination
      if (!sanType.isCapture() && pieceOnToSquare.getPieceType() == KING) {
        throw new SanValidationException(SanValidationProblem.DESTINATION_OPPONENT_KING_NON_CAPTURING,
            Message.getString("validation.san.destination.opponentKing.nonCapturing", toSquare.getName()));
      }
      // opponent piece on destination
      if (sanType.isCapture() && pieceOnToSquare.getPieceType() == KING) {
        throw new SanValidationException(SanValidationProblem.DESTINATION_OPPONENT_KING_CAPTURING,
            Message.getString("validation.san.destination.opponentKing.capturing", toSquare.getName()));
      }

      if (!sanType.isCapture()) {
        throw new SanValidationException(SanValidationProblem.DESTINATION_NOT_EMPTY_NO_CAPTURE_SYMBOL,
            Message.getString("validation.san.destination.notEmpty.noCaptureSymbol", toSquare.getName()));
      }
      return;
    }

    // empty destination
    if (sanType.isCapture()) {
      if (calculateIsEnPassantCapture(board, havingMove, sanType, sanConversion, toSquare)) {
        return;
      }
      if (sanType.getMovingPieceType() == PAWN) {
        throw new SanValidationException(SanValidationProblem.DESTINATION_EMPTY_CAPTURE_SYMBOL_PAWN,
            Message.getString("validation.san.destination.empty.captureSymbol.pawn", toSquare.getName()));
      }
      throw new SanValidationException(SanValidationProblem.DESTINATION_EMPTY_CAPTURE_SYMBOL_RNBQK,
          Message.getString("validation.san.destination.empty.captureSymbol.rnbqk", toSquare.getName()));
    }
  }

  private static boolean calculateIsEnPassantCapture(ApiBoard board, Side havingMove, SanType sanType,
      SanConversion sanConversion, Square toSquare) {
    if (sanType != SanType.PAWN_CAPTURING_NON_PROMOTION_MOVE) {
      return false;
    }
    final Rank fromRank = Rank.calculatePreviousRank(havingMove, toSquare.getRank());
    final Square fromSquare = Square.calculate(sanConversion.fromFile(), fromRank);
    final MoveSpecification pawnCapturingNonPromotionMove = new MoveSpecification(havingMove, fromSquare, toSquare);
    return EnPassantCaptureUtility.calculateIsEnPassantCapture(board.getStaticPosition(),
        pawnCapturingNonPromotionMove);
  }
}
