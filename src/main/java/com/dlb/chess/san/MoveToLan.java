package com.dlb.chess.san;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.CastlingConstants;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.moves.utility.PromotionUtility;
import com.dlb.chess.san.enums.SanLetter;

public class MoveToLan extends AbstractSan {

  public static String calculateLanLastMove(LegalMove lastMove, boolean isCheckmate, boolean isCheck) {
    final MoveSpecification moveSpecification = lastMove.moveSpecification();
    // if castling we can return the LAN here already
    if (CastlingUtility.calculateIsCastlingMove(moveSpecification)) {
      switch (moveSpecification.castlingMove()) {
        case KING_SIDE:
          return CastlingConstants.SAN_CASTLING_KING_SIDE;
        case QUEEN_SIDE:
          return CastlingConstants.SAN_CASTLING_QUEEN_SIDE;
        case NONE:
        default:
          throw new IllegalArgumentException();
      }
    }

    final Piece movingPiece = lastMove.movingPiece();

    // now we go on to calculate the LAN
    final StringBuilder buildSan = new StringBuilder();

    final String fromSquareName = moveSpecification.fromSquare().getName();
    final String toSquareName = moveSpecification.toSquare().getName();
    final var isCapture = lastMove.pieceCaptured() != Piece.NONE;
    switch (movingPiece.getPieceType()) {
      case PAWN:
        buildSan.append(fromSquareName);
        if (isCapture) {
          buildSan.append(SanLetter.CAPTURE.getLetter());
        }
        buildSan.append(toSquareName);
        if (PromotionUtility.calculateIsPromotion(moveSpecification)) {
          final var promotionPieceLetter = moveSpecification.promotionPieceType().getPieceType().getLetter();
          buildSan.append(SanLetter.PROMOTION.getLetter());
          buildSan.append(promotionPieceLetter);
        }
        break;
      case KING:
      case ROOK:
      case KNIGHT:
      case BISHOP:
      case QUEEN:
        final var pieceLetter = String.valueOf(movingPiece.getPieceType().getLetter());
        buildSan.append(pieceLetter);
        buildSan.append(fromSquareName);
        if (isCapture) {
          buildSan.append(SanLetter.CAPTURE.getLetter());
        }
        buildSan.append(toSquareName);
        break;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
    appendCheckOrCheckmate(buildSan, isCheckmate, isCheck);

    return NonNullWrapperCommon.toString(buildSan);
  }
}
