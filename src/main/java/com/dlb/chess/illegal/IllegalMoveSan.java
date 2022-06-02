package com.dlb.chess.illegal;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.board.model.UpdateSquare;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.CastlingConstants;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveRepresentation;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.illegal.model.CheckLegalMove;
import com.dlb.chess.illegal.model.DetectEnPassantCapture;
import com.dlb.chess.illegal.model.DetectMovement;
import com.dlb.chess.illegal.model.DetectMovingPiece;
import com.dlb.chess.illegal.model.ExactlyOneOtherSquareChanged;
import com.dlb.chess.illegal.model.ExactlyOnePieceVanished;
import com.dlb.chess.illegal.model.GuessMove;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.san.enums.SanLetter;

public class IllegalMoveSan {

  private static final MoveSpecification MOVE_SPECIFICATION_NA = new MoveSpecification(Side.WHITE,
      CastlingMove.KING_SIDE);

  private static final LegalMove LEGAL_MOVE_NA = new LegalMove(MOVE_SPECIFICATION_NA, Piece.NONE, Piece.NONE);

  private static final MoveRepresentation MOVE_REPRESENTATION_NA = new MoveRepresentation(MOVE_SPECIFICATION_NA,
      LEGAL_MOVE_NA, "NA", "NA", "NA");

  private static final CheckLegalMove CHECK_NA = new CheckLegalMove(false, MOVE_REPRESENTATION_NA);

  private static final DetectMovement DETECT_MOVEMENT_NA = new DetectMovement(false, Piece.NONE, Square.NONE,
      Square.NONE, Piece.NONE, CastlingMove.NONE, false);

  private static final DetectMovingPiece DETECT_PIECE_MOVEMENT_NA = new DetectMovingPiece(false, Piece.NONE,
      Square.NONE, Square.NONE, Piece.NONE);

  public static GuessMove guessMove(ApiBoard board, StaticPosition currentPosition) {

    final CheckLegalMove check = checkLegalMove(board, currentPosition);

    if (check.isLegal()) {
      return new GuessMove(true, check.moveRepresentation().san());
    }

    return new GuessMove(false, "NA");

  }

  private static final CheckLegalMove checkLegalMove(ApiBoard board, StaticPosition currentPosition) {

    for (final MoveRepresentation moveRepresentation : board.getLegalMovesRepresentation()) {
      final MoveSpecification moveSpecification = moveRepresentation.moveSpecification();
      board.performMove(moveSpecification);
      if (currentPosition.equals(board.getStaticPosition())) {
        board.unperformMove();
        return new CheckLegalMove(true, moveRepresentation);
      }
      board.unperformMove();
    }
    return CHECK_NA;
  }

  // TODO detect if opponent having the move would be a legal position and then if legal opponent move, so potentially
  // move by wrong side, not illegal move by side having the move
  public static DetectMovement detectMovement(ApiBoard board, StaticPosition currentPosition) {

    final Side havingMove = board.getHavingMove();

    // illegal castling
    final CastlingMove castlingMove = detectCastling(board, currentPosition);
    if (castlingMove != CastlingMove.NONE) {
      return new DetectMovement(true, Piece.NONE, Square.NONE, Square.NONE, Piece.NONE, castlingMove, false);
    }

    // illegal en passant
    final DetectEnPassantCapture checkEnPassantCapture = detectEnPassantCapture(board, currentPosition);
    if (checkEnPassantCapture.isDetected()) {
      final MoveSpecification moveSpecification = checkEnPassantCapture.moveSpecification();
      final Piece movingPiece = Piece.calculatePawnPiece(havingMove);
      final Piece pieceCaptured = Piece.calculatePawnPiece(havingMove.getOppositeSide());
      return new DetectMovement(true, movingPiece, moveSpecification.fromSquare(), moveSpecification.toSquare(),
          pieceCaptured, CastlingMove.NONE, true);
    }

    // TOOD detect illegal promotion

    // illegal piece movement
    final DetectMovingPiece movingPiece = detectMovingPiece(board, currentPosition);
    if (movingPiece.isDetected()) {
      return new DetectMovement(true, movingPiece.movingPiece(), movingPiece.fromSquare(), movingPiece.toSquare(),
          movingPiece.pieceCaptured(), CastlingMove.NONE, true);
    }
    return DETECT_MOVEMENT_NA;
  }

  private static final CastlingMove detectCastling(ApiBoard board, StaticPosition currentPosition) {

    final Side havingMove = board.getHavingMove();
    final StaticPosition previousPosition = board.getStaticPosition();

    switch (havingMove) {
      case WHITE:
        if (previousPosition.get(CastlingUtility.WHITE_KING_FROM) == Piece.WHITE_KING) {
          if (previousPosition.get(CastlingUtility.WHITE_ROOK_KING_SIDE_CASTLING_FROM) == Piece.WHITE_ROOK) {

            // perform castling movement
            final List<UpdateSquare> updateSquareList = new ArrayList<>();

            updateSquareList.add(new UpdateSquare(CastlingUtility.WHITE_KING_FROM));
            updateSquareList.add(new UpdateSquare(CastlingUtility.WHITE_KING_KING_SIDE_CASTLING_TO, Piece.WHITE_KING));

            updateSquareList.add(new UpdateSquare(CastlingUtility.WHITE_ROOK_KING_SIDE_CASTLING_FROM));
            updateSquareList.add(new UpdateSquare(CastlingUtility.WHITE_ROOK_KING_SIDE_CASTLING_TO, Piece.WHITE_ROOK));

            final StaticPosition previousPositionPlusCastling = board.getStaticPosition()
                .createChangedPosition(updateSquareList);
            if (previousPositionPlusCastling.equals(currentPosition)) {
              return CastlingMove.KING_SIDE;
            }
          }
          if (previousPosition.get(CastlingUtility.WHITE_ROOK_QUEEN_SIDE_CASTLING_FROM) == Piece.WHITE_ROOK) {

            // perform castling movement
            final List<UpdateSquare> updateSquareList = new ArrayList<>();

            updateSquareList.add(new UpdateSquare(CastlingUtility.WHITE_KING_FROM));
            updateSquareList.add(new UpdateSquare(CastlingUtility.WHITE_KING_QUEEN_SIDE_CASTLING_TO, Piece.WHITE_KING));

            updateSquareList.add(new UpdateSquare(CastlingUtility.WHITE_ROOK_QUEEN_SIDE_CASTLING_FROM));
            updateSquareList.add(new UpdateSquare(CastlingUtility.WHITE_ROOK_QUEEN_SIDE_CASTLING_TO, Piece.WHITE_ROOK));

            final StaticPosition previousPositionPlusCastling = board.getStaticPosition()
                .createChangedPosition(updateSquareList);
            if (previousPositionPlusCastling.equals(currentPosition)) {
              return CastlingMove.QUEEN_SIDE;
            }
          }
        }
        break;
      case BLACK:
        if (previousPosition.get(CastlingUtility.BLACK_KING_FROM) == Piece.BLACK_KING) {
          if (previousPosition.get(CastlingUtility.BLACK_ROOK_KING_SIDE_CASTLING_FROM) == Piece.BLACK_ROOK) {

            // perform castling movement
            final List<UpdateSquare> updateSquareList = new ArrayList<>();

            updateSquareList.add(new UpdateSquare(CastlingUtility.BLACK_KING_FROM));
            updateSquareList.add(new UpdateSquare(CastlingUtility.BLACK_KING_KING_SIDE_CASTLING_TO, Piece.BLACK_KING));

            updateSquareList.add(new UpdateSquare(CastlingUtility.BLACK_ROOK_KING_SIDE_CASTLING_FROM));
            updateSquareList.add(new UpdateSquare(CastlingUtility.BLACK_ROOK_KING_SIDE_CASTLING_TO, Piece.BLACK_ROOK));

            final StaticPosition previousPositionPlusCastling = board.getStaticPosition()
                .createChangedPosition(updateSquareList);
            if (previousPositionPlusCastling.equals(currentPosition)) {
              return CastlingMove.KING_SIDE;
            }
          }
          if (previousPosition.get(CastlingUtility.BLACK_ROOK_QUEEN_SIDE_CASTLING_FROM) == Piece.BLACK_ROOK) {

            // perform castling movement
            final List<UpdateSquare> updateSquareList = new ArrayList<>();

            updateSquareList.add(new UpdateSquare(CastlingUtility.BLACK_KING_FROM));
            updateSquareList.add(new UpdateSquare(CastlingUtility.BLACK_KING_QUEEN_SIDE_CASTLING_TO, Piece.BLACK_KING));

            updateSquareList.add(new UpdateSquare(CastlingUtility.BLACK_ROOK_QUEEN_SIDE_CASTLING_FROM));
            updateSquareList.add(new UpdateSquare(CastlingUtility.BLACK_ROOK_QUEEN_SIDE_CASTLING_TO, Piece.BLACK_ROOK));

            final StaticPosition previousPositionPlusCastling = board.getStaticPosition()
                .createChangedPosition(updateSquareList);
            if (previousPositionPlusCastling.equals(currentPosition)) {
              return CastlingMove.QUEEN_SIDE;
            }
          }
        }
        break;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
    return CastlingMove.NONE;
  }

  private static final DetectEnPassantCapture detectEnPassantCapture(ApiBoard board, StaticPosition currentPosition) {

    final Side havingMove = board.getHavingMove();
    final StaticPosition previousPosition = board.getStaticPosition();

    final Rank enPassantCaptureFromRank = Rank.calculateEnPassantCaptureFromRank(havingMove);

    for (final File file : File.values()) {
      if (file != File.NONE) {
        final Square fromSquare = Square.calculate(file, enPassantCaptureFromRank);
        if (previousPosition.isOwnPawn(fromSquare, havingMove)) {
          // check left file
          if (File.calculateHasLeftFile(havingMove, file)) {
            final File leftFile = File.calculateLeftFile(havingMove, file);
            final Square leftSquare = Square.calculate(leftFile, enPassantCaptureFromRank);
            if (!previousPosition.isEmpty(leftSquare)) {
              final Piece pieceOnLeftSquare = previousPosition.get(leftSquare);
              if (pieceOnLeftSquare.getSide() == havingMove.getOppositeSide()) {
                final Square enPassantCaptureTargetSquare = Square.calculateAheadSquare(havingMove, leftSquare);
                if (previousPosition.isEmpty(enPassantCaptureTargetSquare)) {
                  // perform en passant movement left

                  final List<UpdateSquare> updateSquareList = new ArrayList<>();

                  updateSquareList.add(new UpdateSquare(fromSquare));
                  final Piece pieceOnFromSquare = previousPosition.get(fromSquare);
                  updateSquareList.add(new UpdateSquare(enPassantCaptureTargetSquare, pieceOnFromSquare));
                  updateSquareList.add(new UpdateSquare(leftSquare));

                  final StaticPosition previousPositionPlusEnPassantCapture = previousPosition
                      .createChangedPosition(updateSquareList);

                  if (previousPositionPlusEnPassantCapture.equals(currentPosition)) {
                    final MoveSpecification moveSpecification = new MoveSpecification(havingMove, fromSquare,
                        enPassantCaptureTargetSquare);
                    return new DetectEnPassantCapture(true, moveSpecification);
                  }
                }
              }
            }
          }
          // check right file
          if (File.calculateHasRightFile(havingMove, file)) {
            final File rightFile = File.calculateRightFile(havingMove, file);
            final Square rightSquare = Square.calculate(rightFile, enPassantCaptureFromRank);
            if (!previousPosition.isEmpty(rightSquare)) {
              final Piece pieceOnRightSquare = previousPosition.get(rightSquare);
              if (pieceOnRightSquare.getSide() == havingMove.getOppositeSide()) {
                final Square enPassantCaptureTargetSquare = Square.calculateAheadSquare(havingMove, rightSquare);
                if (previousPosition.isEmpty(enPassantCaptureTargetSquare)) {

                  // perform en passant movement right
                  final List<UpdateSquare> updateSquareList = new ArrayList<>();

                  updateSquareList.add(new UpdateSquare(fromSquare));
                  final Piece pieceOnFromSquare = previousPosition.get(fromSquare);
                  updateSquareList.add(new UpdateSquare(enPassantCaptureTargetSquare, pieceOnFromSquare));
                  updateSquareList.add(new UpdateSquare(rightSquare));

                  final StaticPosition previousPositionPlusEnPassantCapture = previousPosition
                      .createChangedPosition(updateSquareList);

                  if (previousPositionPlusEnPassantCapture.equals(currentPosition)) {
                    final MoveSpecification moveSpecification = new MoveSpecification(havingMove, fromSquare,
                        enPassantCaptureTargetSquare);
                    return new DetectEnPassantCapture(true, moveSpecification);
                  }
                }
              }
            }
          }
        }
      }
    }
    return new DetectEnPassantCapture(false, MOVE_SPECIFICATION_NA);
  }

  private static final DetectMovingPiece detectMovingPiece(ApiBoard board, StaticPosition currentPosition) {

    final StaticPosition previousPosition = board.getStaticPosition();

    final ExactlyOnePieceVanished exactlyOnePieceVanished = checkExactlyOnePieceVanished(previousPosition,
        currentPosition);

    if (!exactlyOnePieceVanished.isHappened()) {
      return DETECT_PIECE_MOVEMENT_NA;
    }

    final Square fromSquare = exactlyOnePieceVanished.fromSquare();
    final ExactlyOneOtherSquareChanged exactlyOneOtherSquareChanged = checkExactlyOneOtherSquareChanged(
        previousPosition, currentPosition, fromSquare);

    if (!exactlyOneOtherSquareChanged.isHappened()) {
      return DETECT_PIECE_MOVEMENT_NA;
    }

    final Piece movingPiece = previousPosition.get(fromSquare);

    final Square toSquare = exactlyOneOtherSquareChanged.toSquare();
    final Piece pieceCaptured = previousPosition.get(toSquare);

    return new DetectMovingPiece(true, movingPiece, fromSquare, toSquare, pieceCaptured);

  }

  private static final ExactlyOnePieceVanished checkExactlyOnePieceVanished(StaticPosition previousPosition,
      StaticPosition currentPosition) {

    var fromSquare = Square.NONE;
    var countPieceVanished = 0;
    for (final Square testSquare : Square.BOARD_SQUARE_LIST) {
      if (!previousPosition.isEmpty(testSquare) && currentPosition.isEmpty(testSquare)) {
        fromSquare = testSquare;
        countPieceVanished++;
      }
      if (countPieceVanished >= 2) {
        break;
      }
    }
    if (countPieceVanished == 1) {
      return new ExactlyOnePieceVanished(true, fromSquare);
    }
    return new ExactlyOnePieceVanished(false, Square.NONE);
  }

  private static final ExactlyOneOtherSquareChanged checkExactlyOneOtherSquareChanged(StaticPosition previousPosition,
      StaticPosition currentPosition, Square fromSquare) {

    var toSquare = Square.NONE;
    var countOtherSquareChanged = 0;
    for (final Square testSquare : Square.BOARD_SQUARE_LIST) {
      if (testSquare != fromSquare) {
        if (previousPosition.get(testSquare) != currentPosition.get(testSquare)) {
          toSquare = testSquare;
          countOtherSquareChanged++;
        }
        if (countOtherSquareChanged >= 2) {
          break;
        }
      }
    }
    if (countOtherSquareChanged == 1) {
      return new ExactlyOneOtherSquareChanged(true, toSquare);
    }
    return new ExactlyOneOtherSquareChanged(false, Square.NONE);
  }

  public static String calculateLan(DetectMovement detectMovement) {
    if (!detectMovement.isDetected()) {
      return "NA";
    }
    switch (detectMovement.castlingMove()) {
      case KING_SIDE:
        return CastlingConstants.SAN_CASTLING_KING_SIDE;
      case QUEEN_SIDE:
        return CastlingConstants.SAN_CASTLING_QUEEN_SIDE;
      case NONE:
        // nothing to do, processing continues after the switch
        break;
      default:
        throw new IllegalArgumentException();
    }

    // en passant

    final StringBuilder san = new StringBuilder();
    if (detectMovement.movingPiece().getPieceType() != PieceType.PAWN) {
      san.append(detectMovement.movingPiece().getPieceType().getLetter());
    }
    san.append(detectMovement.fromSquare().getName());
    if (detectMovement.pieceCaptured() != Piece.NONE) {
      san.append(SanLetter.CAPTURE.getLetter());
    }
    san.append(detectMovement.toSquare().getName());

    // we don't say check or checkmate for illegal moves
    // we are not interested in this, only that the position is restored
    // besides me might not be able to determine that properly, what is if the position is illegal etc.?
    // so for now we leave that

    return NonNullWrapperCommon.toString(san);
  }
}
