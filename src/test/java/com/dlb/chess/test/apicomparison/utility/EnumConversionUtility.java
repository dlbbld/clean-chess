package com.dlb.chess.test.apicomparison.utility;

import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.test.apicarlos.NonNullWrapperApiCarlos;
import com.github.bhlangonijr.chesslib.CastleRight;

public class EnumConversionUtility implements EnumConstants {

  public static com.github.bhlangonijr.chesslib.Piece convertToPiece(Side havingMove, PieceType pieceType) {
    switch (havingMove) {
      case BLACK:
        return switch (pieceType) {
          case BISHOP -> com.github.bhlangonijr.chesslib.Piece.BLACK_BISHOP;
          case KING -> com.github.bhlangonijr.chesslib.Piece.BLACK_KING;
          case KNIGHT -> com.github.bhlangonijr.chesslib.Piece.BLACK_KNIGHT;
          case NONE -> com.github.bhlangonijr.chesslib.Piece.NONE;
          case PAWN -> com.github.bhlangonijr.chesslib.Piece.BLACK_PAWN;
          case QUEEN -> com.github.bhlangonijr.chesslib.Piece.BLACK_QUEEN;
          case ROOK -> com.github.bhlangonijr.chesslib.Piece.BLACK_ROOK;
          default -> throw new IllegalArgumentException();
        };
      case WHITE:
        return switch (pieceType) {
          case BISHOP -> com.github.bhlangonijr.chesslib.Piece.WHITE_BISHOP;
          case KING -> com.github.bhlangonijr.chesslib.Piece.WHITE_KING;
          case KNIGHT -> com.github.bhlangonijr.chesslib.Piece.WHITE_KNIGHT;
          case NONE -> com.github.bhlangonijr.chesslib.Piece.NONE;
          case PAWN -> com.github.bhlangonijr.chesslib.Piece.WHITE_PAWN;
          case QUEEN -> com.github.bhlangonijr.chesslib.Piece.WHITE_QUEEN;
          case ROOK -> com.github.bhlangonijr.chesslib.Piece.WHITE_ROOK;
          default -> throw new IllegalArgumentException();
        };
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static com.github.bhlangonijr.chesslib.Piece convertToPiece(Side havingMove,
      PromotionPieceType promotionPieceType) {
    switch (havingMove) {
      case BLACK:
        return switch (promotionPieceType) {
          case BISHOP -> com.github.bhlangonijr.chesslib.Piece.BLACK_BISHOP;
          case KNIGHT -> com.github.bhlangonijr.chesslib.Piece.BLACK_KNIGHT;
          case NONE -> com.github.bhlangonijr.chesslib.Piece.NONE;
          case QUEEN -> com.github.bhlangonijr.chesslib.Piece.BLACK_QUEEN;
          case ROOK -> com.github.bhlangonijr.chesslib.Piece.BLACK_ROOK;
          default -> throw new IllegalArgumentException();
        };
      case WHITE:
        return switch (promotionPieceType) {
          case BISHOP -> com.github.bhlangonijr.chesslib.Piece.WHITE_BISHOP;
          case KNIGHT -> com.github.bhlangonijr.chesslib.Piece.WHITE_KNIGHT;
          case NONE -> com.github.bhlangonijr.chesslib.Piece.NONE;
          case QUEEN -> com.github.bhlangonijr.chesslib.Piece.WHITE_QUEEN;
          case ROOK -> com.github.bhlangonijr.chesslib.Piece.WHITE_ROOK;
          default -> throw new IllegalArgumentException();
        };
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static com.github.bhlangonijr.chesslib.Piece convertToPiece(Piece piece) {
    return switch (piece) {
      case BLACK_BISHOP -> com.github.bhlangonijr.chesslib.Piece.BLACK_BISHOP;
      case BLACK_KING -> com.github.bhlangonijr.chesslib.Piece.BLACK_KING;
      case BLACK_KNIGHT -> com.github.bhlangonijr.chesslib.Piece.BLACK_KNIGHT;
      case BLACK_PAWN -> com.github.bhlangonijr.chesslib.Piece.BLACK_PAWN;
      case BLACK_QUEEN -> com.github.bhlangonijr.chesslib.Piece.BLACK_QUEEN;
      case BLACK_ROOK -> com.github.bhlangonijr.chesslib.Piece.BLACK_ROOK;
      case WHITE_BISHOP -> com.github.bhlangonijr.chesslib.Piece.WHITE_BISHOP;
      case WHITE_KING -> com.github.bhlangonijr.chesslib.Piece.WHITE_KING;
      case WHITE_KNIGHT -> com.github.bhlangonijr.chesslib.Piece.WHITE_KNIGHT;
      case WHITE_PAWN -> com.github.bhlangonijr.chesslib.Piece.WHITE_PAWN;
      case WHITE_QUEEN -> com.github.bhlangonijr.chesslib.Piece.WHITE_QUEEN;
      case WHITE_ROOK -> com.github.bhlangonijr.chesslib.Piece.WHITE_ROOK;
      case NONE -> com.github.bhlangonijr.chesslib.Piece.NONE;
      default -> throw new IllegalArgumentException();
    };
  }

  public static Piece convertToMyPiece(com.github.bhlangonijr.chesslib.Piece piece) {
    return switch (piece) {
      case BLACK_BISHOP -> BLACK_BISHOP;
      case BLACK_KING -> BLACK_KING;
      case BLACK_KNIGHT -> BLACK_KNIGHT;
      case BLACK_PAWN -> BLACK_PAWN;
      case BLACK_QUEEN -> BLACK_QUEEN;
      case BLACK_ROOK -> BLACK_ROOK;
      case WHITE_BISHOP -> WHITE_BISHOP;
      case WHITE_KING -> WHITE_KING;
      case WHITE_KNIGHT -> WHITE_KNIGHT;
      case WHITE_PAWN -> WHITE_PAWN;
      case WHITE_QUEEN -> WHITE_QUEEN;
      case WHITE_ROOK -> WHITE_ROOK;
      case NONE -> Piece.NONE;
      default -> throw new IllegalArgumentException();
    };
  }

  public static PieceType convertToMyPieceType(com.github.bhlangonijr.chesslib.Piece piece) {
    return switch (piece) {
      case BLACK_BISHOP -> BISHOP;
      case BLACK_KING -> KING;
      case BLACK_KNIGHT -> KNIGHT;
      case BLACK_PAWN -> PAWN;
      case BLACK_QUEEN -> QUEEN;
      case BLACK_ROOK -> ROOK;
      case WHITE_BISHOP -> BISHOP;
      case WHITE_KING -> KING;
      case WHITE_KNIGHT -> KNIGHT;
      case WHITE_PAWN -> PAWN;
      case WHITE_QUEEN -> QUEEN;
      case WHITE_ROOK -> ROOK;
      case NONE -> PieceType.NONE;
      default -> throw new IllegalArgumentException();
    };
  }

  public static PromotionPieceType convertToMyPromotionPieceType(com.github.bhlangonijr.chesslib.Piece piece) {
    return switch (piece) {
      case BLACK_BISHOP -> PromotionPieceType.BISHOP;
      case BLACK_KNIGHT -> PromotionPieceType.KNIGHT;
      case BLACK_QUEEN -> PromotionPieceType.QUEEN;
      case BLACK_ROOK -> PromotionPieceType.ROOK;
      case WHITE_BISHOP -> PromotionPieceType.BISHOP;
      case WHITE_KNIGHT -> PromotionPieceType.KNIGHT;
      case WHITE_QUEEN -> PromotionPieceType.QUEEN;
      case WHITE_ROOK -> PromotionPieceType.ROOK;
      case BLACK_KING, WHITE_KING, BLACK_PAWN, WHITE_PAWN, NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static Side convertToMySide(com.github.bhlangonijr.chesslib.Side side) {
    return switch (side) {
      case BLACK -> BLACK;
      case WHITE -> WHITE;
      default -> throw new IllegalArgumentException();
    };
  }

  public static com.github.bhlangonijr.chesslib.Side convertToSide(Side side) {
    return switch (side) {
      case BLACK -> com.github.bhlangonijr.chesslib.Side.BLACK;
      case WHITE -> com.github.bhlangonijr.chesslib.Side.WHITE;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static Square convertToMySquare(com.github.bhlangonijr.chesslib.Square squareApiCarlos) {

    if (!existsSquare(squareApiCarlos)) {
      throw new IllegalArgumentException("For this square no corresponding square exists");
    }

    final String name = getSquareName(squareApiCarlos);

    for (final Square square : Square.values()) {
      if (square.getName().equals(name)) {
        return square;
      }
    }
    throw new ProgrammingMistakeException("The code for converting the square to square does not work");
  }

  public static boolean existsSquare(com.github.bhlangonijr.chesslib.Square squareApiCarlos) {

    final String name = getSquareName(squareApiCarlos);

    for (final Square square : Square.values()) {
      if (square.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  public static com.github.bhlangonijr.chesslib.Square convertToSquare(Square square) {

    if (!existsMySquare(square)) {
      throw new IllegalArgumentException("For this square no corresponding square exists");
    }

    for (final com.github.bhlangonijr.chesslib.Square squareApiCarlos : com.github.bhlangonijr.chesslib.Square
        .values()) {
      if (square.getName().equals(getSquareName(squareApiCarlos))) {
        return squareApiCarlos;
      }
    }
    throw new ProgrammingMistakeException("The code for converting the square to square does not work");
  }

  public static boolean existsMySquare(Square square) {

    for (final com.github.bhlangonijr.chesslib.Square squareApiCarlos : com.github.bhlangonijr.chesslib.Square
        .values()) {
      if (square.getName().equals(getSquareName(squareApiCarlos))) {
        return true;
      }
    }
    return false;
  }

  private static String getSquareName(com.github.bhlangonijr.chesslib.Square square) {
    return NonNullWrapperCommon.toLowerCase(NonNullWrapperApiCarlos.name(square));
  }

  public static Piece convertPiece(com.github.bhlangonijr.chesslib.Piece piece) {
    return switch (piece) {
      case BLACK_BISHOP -> BLACK_BISHOP;
      case BLACK_KING -> BLACK_KING;
      case BLACK_KNIGHT -> BLACK_KNIGHT;
      case BLACK_PAWN -> BLACK_PAWN;
      case BLACK_QUEEN -> BLACK_QUEEN;
      case BLACK_ROOK -> BLACK_ROOK;
      case NONE -> Piece.NONE;
      case WHITE_BISHOP -> WHITE_BISHOP;
      case WHITE_KING -> WHITE_KING;
      case WHITE_KNIGHT -> WHITE_KNIGHT;
      case WHITE_PAWN -> WHITE_PAWN;
      case WHITE_QUEEN -> WHITE_QUEEN;
      case WHITE_ROOK -> WHITE_ROOK;
      default -> throw new IllegalArgumentException();
    };
  }

  public static CastlingRight convert(CastleRight castlingRight) {
    return switch (castlingRight) {
      case KING_AND_QUEEN_SIDE -> CastlingRight.KING_AND_QUEEN_SIDE;
      case KING_SIDE -> CastlingRight.KING_SIDE;
      case NONE -> CastlingRight.NONE;
      case QUEEN_SIDE -> CastlingRight.QUEEN_SIDE;
      default -> throw new IllegalArgumentException();
    };
  }
}
