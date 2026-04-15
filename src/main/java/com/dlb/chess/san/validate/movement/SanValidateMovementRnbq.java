package com.dlb.chess.san.validate.movement;

import java.util.Set;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.model.EmptyBoardMove;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.enums.SanType;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.squares.emptyboard.AbstractEmptyBoardSquares;

public abstract class SanValidateMovementRnbq extends AbstractSan implements EnumConstants {

  public static void validateRnbqMovement(SanParse sanParse) {
    final SanType sanType = sanParse.sanType();
    final SanConversion sanConversion = sanParse.sanConversion();

    final PieceType pieceType = sanType.getMovingPieceType();
    final Square toSquare = sanConversion.toSquare();

    switch (sanType.getSanFormat()) {
      case KING_NON_CASTLING_CAPTURING:
      case KING_NON_CASTLING_NON_CAPTURING:
      case KING_CASTLING_QUEEN_SIDE:
      case KING_CASTLING_KING_SIDE:
        throw new IllegalArgumentException();
      case PAWN_CAPTURING_NON_PROMOTION:
      case PAWN_CAPTURING_PROMOTION:
      case PAWN_NON_CAPTURING_NON_PROMOTION:
      case PAWN_NON_CAPTURING_PROMOTION:
        throw new IllegalArgumentException();
      case PIECE_NON_CAPTURING_NEITHER:
      case PIECE_CAPTURING_NEITHER:
        return;
      case PIECE_NON_CAPTURING_FILE:
      case PIECE_CAPTURING_FILE:
        validateRnbqMovement(pieceType, sanConversion.fromFile(), toSquare);
        break;
      case PIECE_NON_CAPTURING_RANK:
      case PIECE_CAPTURING_RANK:
        validateRnbqMovement(pieceType, sanConversion.fromRank(), toSquare);
        break;
      case PIECE_NON_CAPTURING_SQUARE:
      case PIECE_CAPTURING_SQUARE: {
        final Square fromSquare = AbstractSan.calculateFromSquare(sanConversion);
        validateRnbqMovement(pieceType, fromSquare, toSquare);
      }
        break;
      default:
        throw new IllegalArgumentException();
    }
  }

  private static void validateRnbqMovement(PieceType movingPieceType, File fromFile, Square toSquare) {
    final Set<EmptyBoardMove> emptyBoardMoveSet = AbstractEmptyBoardSquares
        .calculateNonPawnEmptyBoardMoves(movingPieceType);

    for (final EmptyBoardMove emptyBoardMove : emptyBoardMoveSet) {
      final Square emptyBoardToSquare = emptyBoardMove.toSquare();
      if (emptyBoardToSquare == toSquare) {
        final Square emptyBoardFromSquare = emptyBoardMove.fromSquare();
        final File emptyBoardFromFile = emptyBoardFromSquare.getFile();
        if (emptyBoardFromFile == fromFile) {
          // found a possible move
          return;
        }
      }
    }

    final var messageKey = switch (movingPieceType) {
      case KNIGHT -> "validation.san.movement.knight.file";
      case BISHOP -> "validation.san.movement.bishop.file";
      case ROOK -> "validation.san.movement.rook.file";
      case QUEEN -> "validation.san.movement.queen.file";
      default -> throw new IllegalArgumentException();
    };

    throw new SanValidationException(SanValidationProblem.INVALID_MOVEMENT_NON_PAWN_FROM_FILE,
        Message.getString(messageKey, fromFile.getLetterString(), toSquare.getName()));
  }

  private static void validateRnbqMovement(PieceType movingPieceType, Rank fromRank, Square toSquare) {
    final Set<EmptyBoardMove> emptyBoardMoveSet = AbstractEmptyBoardSquares
        .calculateNonPawnEmptyBoardMoves(movingPieceType);
    for (final EmptyBoardMove emptyBoardMove : emptyBoardMoveSet) {
      final Square emptyBoardToSquare = emptyBoardMove.toSquare();
      if (emptyBoardToSquare == toSquare) {
        final Square emptyBoardFromSquare = emptyBoardMove.fromSquare();
        final Rank emptyBoardFromRank = emptyBoardFromSquare.getRank();
        if (emptyBoardFromRank == fromRank) {
          // found a possible move
          return;
        }
      }
    }

    final var messageKey = switch (movingPieceType) {
      case KNIGHT -> "validation.san.movement.knight.rank";
      case BISHOP -> "validation.san.movement.bishop.rank";
      case ROOK -> "validation.san.movement.rook.rank";
      case QUEEN -> "validation.san.movement.queen.rank";
      default -> throw new IllegalArgumentException();
    };

    throw new SanValidationException(SanValidationProblem.INVALID_MOVEMENT_NON_PAWN_FROM_RANK,
        Message.getString(messageKey, NonNullWrapperCommon.valueOf(fromRank.getNumber()), toSquare.getName()));

  }

  private static void validateRnbqMovement(PieceType movingPieceType, Square fromSquare, Square toSquare) {
    final Set<EmptyBoardMove> emptyBoardMoveSet = AbstractEmptyBoardSquares
        .calculateNonPawnEmptyBoardMoves(movingPieceType);
    for (final EmptyBoardMove emptyBoardMove : emptyBoardMoveSet) {
      final Square emptyBoardToSquare = emptyBoardMove.toSquare();
      if (emptyBoardToSquare == toSquare) {
        final Square emptyBoardFromSquare = emptyBoardMove.fromSquare();
        if (emptyBoardFromSquare == fromSquare) {
          // found a possible move
          return;
        }
      }
    }

    final var messageKey = switch (movingPieceType) {
      case KNIGHT -> "validation.san.movement.knight.square";
      case BISHOP -> "validation.san.movement.bishop.square";
      case ROOK -> "validation.san.movement.rook.square";
      case QUEEN -> "validation.san.movement.queen.square";
      default -> throw new IllegalArgumentException();
    };

    throw new SanValidationException(SanValidationProblem.INVALID_MOVEMENT_NON_PAWN_FROM_SQUARE,
        Message.getString(messageKey, fromSquare.getName(), toSquare.getName()));

  }

}
