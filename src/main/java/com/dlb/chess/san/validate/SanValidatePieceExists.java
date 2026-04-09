package com.dlb.chess.san.validate;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.MaterialUtility;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.enums.SanFormat;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;

public abstract class SanValidatePieceExists extends AbstractSan {

  public static void validatePieceExists(Side havingMove, SanFormat sanFormat, SanConversion sanConversion,
      PieceType movingPieceType, StaticPosition staticPosition) {
    switch (sanFormat) {
      case KING_CASTLING_KING_SIDE:
      case KING_CASTLING_QUEEN_SIDE:
      case KING_NON_CASTLING_CAPTURING:
      case KING_NON_CASTLING_NON_CAPTURING:
        return;
      case PAWN_NON_CAPTURING_NON_PROMOTION:
      case PAWN_NON_CAPTURING_PROMOTION: {
        // for non-capturing pawn moves, the pawn must be on the to-square's file
        final File pawnFile = sanConversion.toSquare().getFile();
        if (!MaterialUtility.calculateHasPieceType(havingMove, PieceType.PAWN, staticPosition, pawnFile)) {
          throw new SanValidationException(SanValidationProblem.PAWN_NO_PIECE_EXISTS,
              Message.getString("validation.san.pawn.noPieceExists", havingMove.getName(), pawnFile.getLetter()));
        }
        validatePawnDestinationRank(havingMove, sanConversion.toSquare().getRank());
        validatePawnFromSquareNonCapturing(havingMove, sanConversion.toSquare(), staticPosition);
        break;
      }
      case PAWN_CAPTURING_NON_PROMOTION:
      case PAWN_CAPTURING_PROMOTION: {
        // for capturing pawn moves, the SAN specifies the from-file explicitly
        final File pawnFile = sanConversion.fromFile();
        if (!MaterialUtility.calculateHasPieceType(havingMove, PieceType.PAWN, staticPosition, pawnFile)) {
          throw new SanValidationException(SanValidationProblem.PAWN_NO_PIECE_EXISTS,
              Message.getString("validation.san.pawn.noPieceExists", havingMove.getName(), pawnFile.getLetter()));
        }
        validatePawnDestinationRank(havingMove, sanConversion.toSquare().getRank());
        validatePawnCapturingDiagonal(havingMove, sanConversion.fromFile(), sanConversion.toSquare().getFile());
        validatePawnFromSquareCapturing(havingMove, sanConversion.fromFile(), sanConversion.toSquare(), staticPosition);
        break;
      }
      case PIECE_CAPTURING_NEITHER:
      case PIECE_NON_CAPTURING_NEITHER:
        if (!MaterialUtility.calculateHasPieceType(havingMove, movingPieceType, staticPosition)) {
          throw new SanValidationException(SanValidationProblem.PIECE_NEITHER_NO_PIECE_EXISTS,
              Message.getString("validation.san.notPawn.specification.none.otherThanKing.noPieceExists",
                  havingMove.getName(), movingPieceType.getName()));
        }
        break;
      case PIECE_CAPTURING_FILE:
      case PIECE_NON_CAPTURING_FILE:
        if (!MaterialUtility.calculateHasPieceType(havingMove, movingPieceType, staticPosition,
            sanConversion.fromFile())) {
          throw new SanValidationException(SanValidationProblem.PIECE_FILE_NO_PIECE_EXISTS,
              Message.getString("validation.san.notPawn.specification.file.noPieceExists", havingMove.getName(),
                  movingPieceType.getName(), sanConversion.fromFile().getLetter()));
        }
        break;
      case PIECE_CAPTURING_RANK:
      case PIECE_NON_CAPTURING_RANK:
        if (!MaterialUtility.calculateHasPieceType(havingMove, movingPieceType, staticPosition,
            sanConversion.fromRank())) {
          throw new SanValidationException(SanValidationProblem.PIECE_RANK_NO_PIECE_EXISTS,
              Message.getString("validation.san.notPawn.specification.rank.noPieceExists", havingMove.getName(),
                  movingPieceType.getName(), NonNullWrapperCommon.valueOf(sanConversion.fromRank().getNumber())));
        }
        break;
      case PIECE_CAPTURING_SQUARE:
      case PIECE_NON_CAPTURING_SQUARE:
        final Square fromSquare = Square.calculate(sanConversion.fromFile(), sanConversion.fromRank());
        final Piece pieceOnFromSquare = staticPosition.get(fromSquare);
        if (pieceOnFromSquare == Piece.NONE || pieceOnFromSquare.getSide() != havingMove
            || pieceOnFromSquare.getPieceType() != movingPieceType) {
          throw new SanValidationException(SanValidationProblem.PIECE_SQUARE_NO_PIECE_EXISTS,
              Message.getString("validation.san.notPawn.specification.square.noPieceExists", havingMove.getName(),
                  movingPieceType.getName(), fromSquare.getName()));
        }
        break;
      default:
        throw new IllegalArgumentException();

    }
  }

  private static void validatePawnDestinationRank(Side havingMove, Rank destinationRank) {
    final var isInvalid = switch (havingMove) {
      case WHITE -> destinationRank == Rank.RANK_1 || destinationRank == Rank.RANK_2;
      case BLACK -> destinationRank == Rank.RANK_7 || destinationRank == Rank.RANK_8;
      case NONE -> throw new IllegalArgumentException();
    };
    if (isInvalid) {
      throw new SanValidationException(SanValidationProblem.PAWN_NON_REACHABLE_RANK,
          Message.getString("validation.san.pawn.destinationRank", havingMove.getName(),
              NonNullWrapperCommon.valueOf(destinationRank.getNumber())));
    }
  }

  private static void validatePawnCapturingDiagonal(Side havingMove, File fromFile, File toFile) {
    final var isAdjacentLeft = File.calculateHasLeftFile(havingMove, fromFile)
        && File.calculateLeftFile(havingMove, fromFile) == toFile;
    final var isAdjacentRight = File.calculateHasRightFile(havingMove, fromFile)
        && File.calculateRightFile(havingMove, fromFile) == toFile;

    if (!isAdjacentLeft && !isAdjacentRight) {
      // build educational message showing which files ARE valid
      if (File.calculateHasLeftFile(havingMove, fromFile) && File.calculateHasRightFile(havingMove, fromFile)) {
        final File leftFile = File.calculateLeftFile(havingMove, fromFile);
        final File rightFile = File.calculateRightFile(havingMove, fromFile);
        throw new SanValidationException(SanValidationProblem.PAWN_CAPTURING_DIAGONAL,
            Message.getString("validation.san.pawn.capturingDiagonal", fromFile.getLetter(), leftFile.getLetter(),
                rightFile.getLetter()));
      }
      // pawn on edge file (a or h) — only one adjacent file
      final File adjacentFile;
      if (File.calculateHasLeftFile(havingMove, fromFile)) {
        adjacentFile = File.calculateLeftFile(havingMove, fromFile);
      } else {
        adjacentFile = File.calculateRightFile(havingMove, fromFile);
      }
      throw new SanValidationException(SanValidationProblem.PAWN_CAPTURING_DIAGONAL, Message
          .getString("validation.san.pawn.capturingDiagonalEdge", fromFile.getLetter(), adjacentFile.getLetter()));
    }
  }

  private static void validatePawnFromSquareNonCapturing(Side havingMove, Square toSquare,
      StaticPosition staticPosition) {
    final File file = toSquare.getFile();
    final Rank toRank = toSquare.getRank();
    // one square back from the to-square
    final Rank oneBackRank = Rank.calculatePreviousRank(havingMove, toRank);
    final Square oneBackSquare = Square.calculate(file, oneBackRank);
    final Piece pieceOnOneBack = staticPosition.get(oneBackSquare);

    if (Rank.calculateIsPawnTwoSquareAdvanceRank(havingMove, toRank)) {
      // two-square advance (e.g. d4 for white): pawn on d2 and d3 empty, or pawn on d3
      final Rank twoBackRank = Rank.calculatePreviousRank(havingMove, oneBackRank);
      final Square twoBackSquare = Square.calculate(file, twoBackRank);
      final Piece pieceOnTwoBack = staticPosition.get(twoBackSquare);
      final Piece expectedPawn = PieceType.calculate(havingMove, PieceType.PAWN);

      final var pawnOnOneBack = pieceOnOneBack == expectedPawn;
      final var pawnOnTwoBackAndPathClear = pieceOnTwoBack == expectedPawn && pieceOnOneBack == Piece.NONE;

      if (!pawnOnOneBack && !pawnOnTwoBackAndPathClear) {
        throw new SanValidationException(SanValidationProblem.PAWN_FROM_SQUARE,
            Message.getString("validation.san.pawn.fromSquareTwoSquareAdvance", toSquare.getName(),
                havingMove.getName(), oneBackSquare.getName(), twoBackSquare.getName()));
      }
    } else {
      // one-square advance: pawn must be on the square directly behind
      final Piece expectedPawn = PieceType.calculate(havingMove, PieceType.PAWN);
      if (pieceOnOneBack != expectedPawn) {
        throw new SanValidationException(SanValidationProblem.PAWN_FROM_SQUARE,
            Message.getString("validation.san.pawn.fromSquare", havingMove.getName(), toSquare.getName()));
      }
    }
  }

  private static void validatePawnFromSquareCapturing(Side havingMove, File fromFile, Square toSquare,
      StaticPosition staticPosition) {
    // the pawn must be on the from-file, one rank back from the to-square
    final Rank fromRank = Rank.calculatePreviousRank(havingMove, toSquare.getRank());
    final Square fromSquare = Square.calculate(fromFile, fromRank);
    final Piece pieceOnFromSquare = staticPosition.get(fromSquare);
    final Piece expectedPawn = PieceType.calculate(havingMove, PieceType.PAWN);

    if (pieceOnFromSquare != expectedPawn) {
      throw new SanValidationException(SanValidationProblem.PAWN_FROM_SQUARE,
          Message.getString("validation.san.pawn.fromSquare", havingMove.getName(), toSquare.getName()));
    }
  }
}
