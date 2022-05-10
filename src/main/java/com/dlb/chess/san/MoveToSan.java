package com.dlb.chess.san;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.CastlingConstants;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.moves.utility.PromotionUtility;
import com.dlb.chess.san.enums.SanLetter;
import com.dlb.chess.san.enums.SanSourceSpecification;

public class MoveToSan extends AbstractSan {

  public static String calculateSanLastMove(LegalMove lastMove, Set<LegalMove> legalMoveBeforeLastHalfMoveSet,
      boolean isCheckmate, boolean isCheck) {

    // first - check if castling move
    final MoveSpecification moveSpecification = lastMove.moveSpecification();
    if (CastlingUtility.calculateIsCastlingMove(moveSpecification)) {
      return calculateSanLastMoveCastling(moveSpecification, isCheckmate, isCheck);
    }
    return calculateSanLastMoveNonCastling(lastMove, legalMoveBeforeLastHalfMoveSet, isCheckmate, isCheck);
  }

  private static SanSourceSpecification calculateSourceSpecification(LegalMove legalMove,
      Set<LegalMove> legalMoveSetForMovingPiece) {

    final MoveSpecification moveSpecification = legalMove.moveSpecification();

    final Set<LegalMove> legalMoveSetForPieceAndToSquare = filterLegalMovesCandidates(legalMoveSetForMovingPiece,
        moveSpecification.toSquare());
    final var numberOfLegalMovesFromSameFile = calculateNumberOfLegalMovesFromSameFile(
        moveSpecification.fromSquare().getFile(), legalMoveSetForPieceAndToSquare);
    final var numberOfLegalMovesFromSameRank = calculateNumberOfLegalMovesFromSameRank(
        moveSpecification.fromSquare().getRank(), legalMoveSetForPieceAndToSquare);
    final var hasOtherFilesHavingLegalMoves = calculateHasOtherFilesHavingLegalMoves(
        moveSpecification.fromSquare().getFile(), legalMoveSetForPieceAndToSquare);

    if (hasOtherFilesHavingLegalMoves) {
      if (numberOfLegalMovesFromSameFile == 1) {
        return SanSourceSpecification.SOURCE_REQUIRED_FILE_BUT_NOT_RANK;
      }
      if (numberOfLegalMovesFromSameRank == 1) {
        return SanSourceSpecification.SOURCE_REQUIRED_RANK_BUT_NOT_FILE;
      }
      return SanSourceSpecification.SOURCE_REQUIRED_SQUARE;
    }

    if (numberOfLegalMovesFromSameFile == 1) {
      // only one legal move
      return SanSourceSpecification.SOURCE_NOT_REQUIRED;
    }
    if (numberOfLegalMovesFromSameRank == 1) {
      return SanSourceSpecification.SOURCE_REQUIRED_RANK_BUT_NOT_FILE;
    }
    return SanSourceSpecification.SOURCE_REQUIRED_SQUARE;
  }

  private static String calculateSanLastMoveCastling(MoveSpecification moveSpecification, boolean isCheckmate,
      boolean isCheck) {
    final StringBuilder buildSan = new StringBuilder();
    switch (moveSpecification.castlingMove()) {
      case KING_SIDE:
        buildSan.append(CastlingConstants.SAN_CASTLING_KING_SIDE);
        break;
      case QUEEN_SIDE:
        buildSan.append(CastlingConstants.SAN_CASTLING_QUEEN_SIDE);
        break;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }

    appendCheckOrCheckmate(buildSan, isCheckmate, isCheck);
    return NonNullWrapperCommon.toString(buildSan);
  }

  private static String calculateSanLastMoveNonCastling(LegalMove lastMove,
      Set<LegalMove> legalMoveBeforeLastHalfMoveSet, boolean isCheckmate, boolean isCheck) {

    final MoveSpecification moveSpecification = lastMove.moveSpecification();
    final Piece movingPiece = lastMove.movingPiece();
    if (movingPiece == Piece.NONE) {
      throw new ProgrammingMistakeException(
          "Something is wrong, a non castling move always specifies a piece to be moved");
    }

    final var pieceLetter = String.valueOf(movingPiece.getPieceType().getLetter());
    final Square fromSquare = moveSpecification.fromSquare();
    final File fromFile = fromSquare.getFile();
    final Rank fromRank = fromSquare.getRank();
    final var fromFileLetter = String.valueOf(fromFile.getLetter());
    final var fromRankNumber = fromRank.getNumber();
    final String toSquareName = moveSpecification.toSquare().getName();
    final var isCapture = lastMove.pieceCaptured() != Piece.NONE;

    final StringBuilder buildSan = new StringBuilder();

    switch (movingPiece.getPieceType()) {
      case KING:
        buildSan.append(pieceLetter);
        if (isCapture) {
          buildSan.append(SanLetter.CAPTURE.getLetter());
        }
        buildSan.append(toSquareName);
        break;
      case PAWN:
        if (!PromotionUtility.calculateIsPromotion(moveSpecification)) {
          if (isCapture) {
            buildSan.append(fromFileLetter + SanLetter.CAPTURE.getLetter());
          }
          buildSan.append(toSquareName);
        } else {
          final var promotionPieceLetter = moveSpecification.promotionPieceType().getPieceType().getLetter();
          if (isCapture) {
            buildSan.append(fromFileLetter + SanLetter.CAPTURE.getLetter());
          }
          buildSan.append(toSquareName + SanLetter.PROMOTION.getLetter() + promotionPieceLetter);
        }
        break;
      case ROOK:
      case KNIGHT:
      case BISHOP:
      case QUEEN:
        buildSan.append(pieceLetter);

        final Set<LegalMove> legalMoveSetForMovingPiece = calculateLegalMoveSetForMovingPiece(lastMove.movingPiece(),
            legalMoveBeforeLastHalfMoveSet);

        final SanSourceSpecification sourceSpecification = calculateSourceSpecification(lastMove,
            legalMoveSetForMovingPiece);
        switch (sourceSpecification) {
          case SOURCE_NOT_REQUIRED:
            // nothing to add
            break;
          case SOURCE_REQUIRED_FILE_BUT_NOT_RANK:
            buildSan.append(fromFileLetter);
            break;
          case SOURCE_REQUIRED_RANK_BUT_NOT_FILE:
            buildSan.append(fromRankNumber);
            break;
          case SOURCE_REQUIRED_SQUARE:
            buildSan.append(fromFileLetter);
            buildSan.append(fromRankNumber);
            break;
          default:
            throw new IllegalArgumentException();
        }

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

  // semantics for moving piece: for castling the moving piece is none! so the castling is not returned here when
  // searching for the king as moving piece!!!
  public static Set<LegalMove> calculateLegalMoveSetForMovingPiece(Piece movingPiece, Set<LegalMove> legalMoveSet) {
    final Set<LegalMove> legalMoveSetForMovingPiece = new TreeSet<>();
    for (final LegalMove legalMove : legalMoveSet) {
      if (legalMove.movingPiece() == movingPiece) {
        legalMoveSetForMovingPiece.add(legalMove);
      }
    }
    return legalMoveSetForMovingPiece;
  }
}
