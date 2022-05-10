package com.dlb.chess.san;

import java.util.Set;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.model.EmptyBoardMove;
import com.dlb.chess.model.PawnDiagonalBoardMove;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.moves.utility.PawnUtility;
import com.dlb.chess.san.enums.SanFormat;
import com.dlb.chess.san.enums.SanType;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.squares.emptyboard.AbstractEmptyBoardSquares;

public abstract class SanValidateMove extends AbstractSan implements EnumConstants {

  public static void validateMovingOntoItself(SanParse sanParse) {
    if (sanParse.sanType().getMovingPieceType() != PieceType.PAWN) {
      final Square fromSquare = AbstractSan.calculateFromSquare(sanParse.sanConversion());
      if (fromSquare != Square.NONE && fromSquare == sanParse.sanConversion().toSquare()) {
        throw new SanValidationException(SanValidationProblem.MOVING_ONTO_ITSELF,
            Message.getString("validation.san.movingOntoItself"));
      }
    }
  }

  public static void validateMovement(Side havingMove, SanParse sanParse) {
    if (sanParse.sanType().getMovingPieceType() != PieceType.PAWN) {
      validateNonPawnMovement(sanParse);
      return;
    }
    validatePawnMovement(havingMove, sanParse);
  }

  public static void validatePromotion(Side havingMove, SanParse sanParse) {
    if (sanParse.sanType().getMovingPieceType() != PieceType.PAWN) {
      return;
    }
    validatePawnPromotion(havingMove, sanParse);
  }

  private static void validateNonPawnMovement(SanParse sanParse) {
    final SanType sanType = sanParse.sanType();
    final SanConversion sanConversion = sanParse.sanConversion();

    // in a first step we need to check the castling
    // this is the only move where the to square is not set so we can afterwards set
    // the to square
    if (SanFormat.calculateIsKingCastlingMove(sanType.getSanFormat())) {
      return;
    }

    final PieceType pieceType = sanType.getMovingPieceType();
    final Square toSquare = sanConversion.toSquare();

    switch (sanType.getSanFormat()) {
      case KING_NON_CASTLING_CAPTURING_FORMAT:
      case KING_NON_CASTLING_NON_CAPTURING_FORMAT:
        validateNonPawnMovement(pieceType, toSquare);
        break;
      case PAWN_CAPTURING_NON_PROMOTION_FORMAT:
      case PAWN_CAPTURING_PROMOTION_FORMAT:
      case PAWN_NON_CAPTURING_NON_PROMOTION_FORMAT:
      case PAWN_NON_CAPTURING_PROMOTION_FORMAT:
        throw new IllegalArgumentException();
      case PIECE_NON_CAPTURING_NEITHER_FORMAT:
      case PIECE_CAPTURING_NEITHER_FORMAT:
        validateNonPawnMovement(pieceType, toSquare);
        break;
      case PIECE_NON_CAPTURING_FILE_FORMAT:
      case PIECE_CAPTURING_FILE_FORMAT:
        validateNonPawnMovement(pieceType, sanConversion.fromFile(), toSquare);
        break;
      case PIECE_NON_CAPTURING_RANK_FORMAT:
      case PIECE_CAPTURING_RANK_FORMAT:
        validateNonPawnMovement(pieceType, sanConversion.fromRank(), toSquare);
        break;
      case PIECE_NON_CAPTURING_SQUARE_FORMAT:
      case PIECE_CAPTURING_SQUARE_FORMAT:
        // in these two cases only the from square is defined in the SAN
        final Square fromSquare = AbstractSan.calculateFromSquare(sanConversion);
        validateNonPawnMovement(pieceType, fromSquare, toSquare);
        break;
      case KING_CASTLING_QUEEN_SIDE_FORMAT:
      case KING_CASTLING_KING_SIDE_FORMAT:
        throw new ProgrammingMistakeException("Not possible to come here, already returned");
      default:
        throw new IllegalArgumentException();
    }
  }

  private static void validateNonPawnMovement(PieceType movingPieceType, Square toSquare) {
    final Set<EmptyBoardMove> emptyBoardMoveSet = AbstractEmptyBoardSquares
        .calculateNonPawnEmptyBoardMoves(movingPieceType);
    for (final EmptyBoardMove emptyBoardMove : emptyBoardMoveSet) {
      final Square emptyBoardToSquare = emptyBoardMove.toSquare();
      if (emptyBoardToSquare == toSquare) {
        // found a possible move
        return;
      }
    }
    throw new ProgrammingMistakeException(
        "The assumption that rook, knight, bishop, queeen and king can potentially move to every square is not met");
  }

  private static void validateNonPawnMovement(PieceType movingPieceType, File fromFile, Square toSquare) {
    final Set<EmptyBoardMove> emptyBoardMoveSet = AbstractEmptyBoardSquares
        .calculateNonPawnEmptyBoardMoves(movingPieceType);

    for (final EmptyBoardMove emptyBoardMove : emptyBoardMoveSet) {
      final Square emptyBoardToSquare = emptyBoardMove.toSquare();
      if (emptyBoardToSquare == toSquare) {
        final Square emptyBoardFromSquare = emptyBoardMove.fromSquare();
        final File emptyBoardFromFile = emptyBoardFromSquare.getFile();
        if (emptyBoardFromFile == fromFile && emptyBoardToSquare == toSquare) {
          // found a possible move
          return;
        }
      }
    }

    throw new SanValidationException(SanValidationProblem.INVALID_MOVEMENT_NON_PAWN_FROM_FILE,
        Message.getString("validation.san.notPawn.specification.file.invalidMovement", movingPieceType.getName(),
            fromFile.getLetter(), toSquare.getName()));
  }

  private static void validateNonPawnMovement(PieceType movingPieceType, Rank fromRank, Square toSquare) {
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

    throw new SanValidationException(SanValidationProblem.INVALID_MOVEMENT_NON_PAWN_FROM_RANK,
        Message.getString("validation.san.notPawn.specification.rank.invalidMovement", PieceType.PAWN.getName(),
            NonNullWrapperCommon.valueOf(fromRank.getNumber()), toSquare.getName()));
  }

  private static void validateNonPawnMovement(PieceType movingPieceType, Square fromSquare, Square toSquare) {
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
    throw new SanValidationException(SanValidationProblem.INVALID_MOVEMENT_NON_PAWN_FROM_SQUARE,
        Message.getString("validation.san.notPawn.specification.square.invalidMovement", movingPieceType.getName(),
            fromSquare.getName(), toSquare.getName()));
  }

  private static void validatePawnMovement(Side havingMove, SanParse sanParse) {

    final SanType sanType = sanParse.sanType();
    final SanConversion sanConversion = sanParse.sanConversion();

    final Square toSquare = sanConversion.toSquare();

    switch (sanType.getSanFormat()) {
      case KING_NON_CASTLING_CAPTURING_FORMAT:
      case KING_NON_CASTLING_NON_CAPTURING_FORMAT:
        throw new IllegalArgumentException();
      case PAWN_NON_CAPTURING_NON_PROMOTION_FORMAT:
      case PAWN_NON_CAPTURING_PROMOTION_FORMAT:
        validatePawnRankTo(havingMove, toSquare);
        break;
      case PAWN_CAPTURING_NON_PROMOTION_FORMAT:
      case PAWN_CAPTURING_PROMOTION_FORMAT:
        validatePawnRankTo(havingMove, toSquare);
        validatePawnFromAndToFile(havingMove, sanConversion.fromFile(), toSquare);
        break;
      case PIECE_NON_CAPTURING_NEITHER_FORMAT:
      case PIECE_CAPTURING_NEITHER_FORMAT:
      case PIECE_NON_CAPTURING_FILE_FORMAT:
      case PIECE_CAPTURING_FILE_FORMAT:
      case PIECE_NON_CAPTURING_RANK_FORMAT:
      case PIECE_CAPTURING_RANK_FORMAT:
      case PIECE_NON_CAPTURING_SQUARE_FORMAT:
      case PIECE_CAPTURING_SQUARE_FORMAT:
      case KING_CASTLING_QUEEN_SIDE_FORMAT:
      case KING_CASTLING_KING_SIDE_FORMAT:
      default:
        throw new IllegalArgumentException();
    }
  }

  private static void validatePawnPromotion(Side havingMove, SanParse sanParse) {

    final SanType sanType = sanParse.sanType();
    final SanConversion sanConversion = sanParse.sanConversion();

    final Square toSquare = sanConversion.toSquare();

    switch (sanType.getSanFormat()) {
      case KING_NON_CASTLING_CAPTURING_FORMAT:
      case KING_NON_CASTLING_NON_CAPTURING_FORMAT:
        throw new IllegalArgumentException();
      case PAWN_NON_CAPTURING_NON_PROMOTION_FORMAT:
      case PAWN_CAPTURING_NON_PROMOTION_FORMAT:
        validatePawnPromotionMissingPiece(havingMove, toSquare.getRank());
        break;
      case PAWN_NON_CAPTURING_PROMOTION_FORMAT:
      case PAWN_CAPTURING_PROMOTION_FORMAT:
        validatePawnPromotionRank(havingMove, toSquare.getRank());
        break;
      case PIECE_NON_CAPTURING_NEITHER_FORMAT:
      case PIECE_CAPTURING_NEITHER_FORMAT:
      case PIECE_NON_CAPTURING_FILE_FORMAT:
      case PIECE_CAPTURING_FILE_FORMAT:
      case PIECE_NON_CAPTURING_RANK_FORMAT:
      case PIECE_CAPTURING_RANK_FORMAT:
      case PIECE_NON_CAPTURING_SQUARE_FORMAT:
      case PIECE_CAPTURING_SQUARE_FORMAT:
      case KING_CASTLING_QUEEN_SIDE_FORMAT:
      case KING_CASTLING_KING_SIDE_FORMAT:
      default:
        throw new IllegalArgumentException();
    }
  }

  private static void validatePawnRankTo(Side havingMove, Square toSquare) {
    final var isValidRankToUsingForwardMoves = calculateIsValidRankUsingForwardMoves(havingMove, toSquare);
    final var isValidRankToUsingRank = calculateIsValidRankUsingRank(havingMove, toSquare);

    if (isValidRankToUsingForwardMoves != isValidRankToUsingRank) {
      throw new ProgrammingMistakeException("The pawn rank to calculation is wrong");
    }

    if (!isValidRankToUsingForwardMoves) {
      throw new SanValidationException(SanValidationProblem.INVALID_MOVEMENT_PAWN_TO_RANK,
          Message.getString("validation.san.pawn.invalidRank", havingMove.getName(), PieceType.PAWN.getName(),
              NonNullWrapperCommon.valueOf(toSquare.getRank().getNumber())));
    }
  }

  private static boolean calculateIsValidRankUsingForwardMoves(Side havingMove, Square toSquare) {
    final Set<EmptyBoardMove> emptyBoardMoveSet = AbstractEmptyBoardSquares.calculatePawnEmptyBoardMoves(havingMove);
    for (final EmptyBoardMove emptyBoardMove : emptyBoardMoveSet) {
      if (emptyBoardMove.toSquare() == toSquare) {
        // found a possible move
        return true;
      }
    }
    return false;
  }

  private static boolean calculateIsValidRankUsingRank(Side havingMove, Square toSquare) {
    return !Rank.calculateIsGroundRank(havingMove, toSquare.getRank())
        && !Rank.calculateIsPawnInititalRank(havingMove, toSquare.getRank());

  }

  private static void validatePawnFromAndToFile(Side havingMove, File fromFile, Square toSquare) {

    final var isAdjacentFileUsingDiagonalMoves = calculateIsAdjacentFileUsingDiagonalMoves(havingMove, fromFile,
        toSquare);
    final var isAdjacentFileUsingFile = calculateIsAdjacentFileUsingFile(havingMove, fromFile, toSquare);

    if (isAdjacentFileUsingDiagonalMoves != isAdjacentFileUsingFile) {
      throw new ProgrammingMistakeException("The pawn adjacent file calculation is wrong");
    }

    if (!isAdjacentFileUsingDiagonalMoves) {
      throw new SanValidationException(SanValidationProblem.INVALID_MOVEMENT_PAWN_FROM_FILE, Message.getString(
          "validation.san.pawn.invalidFromAndToFileNotAdjacent", fromFile.getLetter(), toSquare.getFile().getLetter()));
    }
  }

  private static boolean calculateIsAdjacentFileUsingDiagonalMoves(Side havingMove, File fromFile, Square toSquare) {
    // diagonal moves
    final Set<PawnDiagonalBoardMove> pawnDiagonalMoveSet = PawnUtility.calculatePawnDiagonalMoves(havingMove);
    for (final PawnDiagonalBoardMove diagonalMove : pawnDiagonalMoveSet) {
      if (diagonalMove.fromSquare().getFile() == fromFile && diagonalMove.toSquare().getFile() == toSquare.getFile()) {
        // adjacent file
        // this can be done easier but I want to reuse existing logic
        return true;
      }
    }
    return false;
  }

  private static boolean calculateIsAdjacentFileUsingFile(Side havingMove, File fromFile, Square toSquare) {
    if (File.calculateHasLeftFile(havingMove, fromFile)) {
      final File leftFile = File.calculateLeftFile(havingMove, fromFile);
      if (leftFile == toSquare.getFile()) {
        return true;
      }
    }
    if (File.calculateHasRightFile(havingMove, fromFile)) {
      final File rightFile = File.calculateRightFile(havingMove, fromFile);
      if (rightFile == toSquare.getFile()) {
        return true;
      }
    }
    return false;
  }

  private static void validatePawnPromotionMissingPiece(Side havingMove, Rank rank) {
    if (Rank.calculateIsPromotionRank(havingMove, rank)) {
      throw new SanValidationException(SanValidationProblem.INVALID_PROMOTION_NO_PROMOTION_PIECE,
          Message.getString("validation.san.pawn.invalidNoPromotionPiece"));
    }
  }

  private static void validatePawnPromotionRank(Side havingMove, Rank rank) {
    if (!Rank.calculateIsPromotionRank(havingMove, rank)) {
      final Rank promotionRank = Rank.calculatePromotionRank(havingMove);
      final String promotionRankNumber = NonNullWrapperCommon.valueOf(promotionRank.getNumber());

      throw new SanValidationException(SanValidationProblem.INVALID_PROMOTION_RANK_PAWN,
          Message.getString("validation.san.pawn.invalidPromotionRank", havingMove.getName(), PieceType.PAWN.getName(),
              promotionRankNumber));
    }
  }

}
