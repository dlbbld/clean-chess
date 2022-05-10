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
        switch (pieceType) {
          case BISHOP:
            return com.github.bhlangonijr.chesslib.Piece.BLACK_BISHOP;
          case KING:
            return com.github.bhlangonijr.chesslib.Piece.BLACK_KING;
          case KNIGHT:
            return com.github.bhlangonijr.chesslib.Piece.BLACK_KNIGHT;
          case NONE:
            return com.github.bhlangonijr.chesslib.Piece.NONE;
          case PAWN:
            return com.github.bhlangonijr.chesslib.Piece.BLACK_PAWN;
          case QUEEN:
            return com.github.bhlangonijr.chesslib.Piece.BLACK_QUEEN;
          case ROOK:
            return com.github.bhlangonijr.chesslib.Piece.BLACK_ROOK;
          default:
            throw new IllegalArgumentException();
        }
      case WHITE:
        switch (pieceType) {
          case BISHOP:
            return com.github.bhlangonijr.chesslib.Piece.WHITE_BISHOP;
          case KING:
            return com.github.bhlangonijr.chesslib.Piece.WHITE_KING;
          case KNIGHT:
            return com.github.bhlangonijr.chesslib.Piece.WHITE_KNIGHT;
          case NONE:
            return com.github.bhlangonijr.chesslib.Piece.NONE;
          case PAWN:
            return com.github.bhlangonijr.chesslib.Piece.WHITE_PAWN;
          case QUEEN:
            return com.github.bhlangonijr.chesslib.Piece.WHITE_QUEEN;
          case ROOK:
            return com.github.bhlangonijr.chesslib.Piece.WHITE_ROOK;
          default:
            throw new IllegalArgumentException();
        }
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static com.github.bhlangonijr.chesslib.Piece convertToPiece(Side havingMove,
      PromotionPieceType promotionPieceType) {
    switch (havingMove) {
      case BLACK:
        switch (promotionPieceType) {
          case BISHOP:
            return com.github.bhlangonijr.chesslib.Piece.BLACK_BISHOP;
          case KNIGHT:
            return com.github.bhlangonijr.chesslib.Piece.BLACK_KNIGHT;
          case NONE:
            return com.github.bhlangonijr.chesslib.Piece.NONE;
          case QUEEN:
            return com.github.bhlangonijr.chesslib.Piece.BLACK_QUEEN;
          case ROOK:
            return com.github.bhlangonijr.chesslib.Piece.BLACK_ROOK;
          default:
            throw new IllegalArgumentException();
        }
      case WHITE:
        switch (promotionPieceType) {
          case BISHOP:
            return com.github.bhlangonijr.chesslib.Piece.WHITE_BISHOP;
          case KNIGHT:
            return com.github.bhlangonijr.chesslib.Piece.WHITE_KNIGHT;
          case NONE:
            return com.github.bhlangonijr.chesslib.Piece.NONE;
          case QUEEN:
            return com.github.bhlangonijr.chesslib.Piece.WHITE_QUEEN;
          case ROOK:
            return com.github.bhlangonijr.chesslib.Piece.WHITE_ROOK;
          default:
            throw new IllegalArgumentException();
        }
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static com.github.bhlangonijr.chesslib.Piece convertToPiece(Piece piece) {
    switch (piece) {
      case BLACK_BISHOP:
        return com.github.bhlangonijr.chesslib.Piece.BLACK_BISHOP;
      case BLACK_KING:
        return com.github.bhlangonijr.chesslib.Piece.BLACK_KING;
      case BLACK_KNIGHT:
        return com.github.bhlangonijr.chesslib.Piece.BLACK_KNIGHT;
      case BLACK_PAWN:
        return com.github.bhlangonijr.chesslib.Piece.BLACK_PAWN;
      case BLACK_QUEEN:
        return com.github.bhlangonijr.chesslib.Piece.BLACK_QUEEN;
      case BLACK_ROOK:
        return com.github.bhlangonijr.chesslib.Piece.BLACK_ROOK;
      case WHITE_BISHOP:
        return com.github.bhlangonijr.chesslib.Piece.WHITE_BISHOP;
      case WHITE_KING:
        return com.github.bhlangonijr.chesslib.Piece.WHITE_KING;
      case WHITE_KNIGHT:
        return com.github.bhlangonijr.chesslib.Piece.WHITE_KNIGHT;
      case WHITE_PAWN:
        return com.github.bhlangonijr.chesslib.Piece.WHITE_PAWN;
      case WHITE_QUEEN:
        return com.github.bhlangonijr.chesslib.Piece.WHITE_QUEEN;
      case WHITE_ROOK:
        return com.github.bhlangonijr.chesslib.Piece.WHITE_ROOK;
      case NONE:
        return com.github.bhlangonijr.chesslib.Piece.NONE;
      default:
        throw new IllegalArgumentException();
    }
  }

  public static Piece convertToMyPiece(com.github.bhlangonijr.chesslib.Piece piece) {
    switch (piece) {
      case BLACK_BISHOP:
        return BLACK_BISHOP;
      case BLACK_KING:
        return BLACK_KING;
      case BLACK_KNIGHT:
        return BLACK_KNIGHT;
      case BLACK_PAWN:
        return BLACK_PAWN;
      case BLACK_QUEEN:
        return BLACK_QUEEN;
      case BLACK_ROOK:
        return BLACK_ROOK;
      case WHITE_BISHOP:
        return WHITE_BISHOP;
      case WHITE_KING:
        return WHITE_KING;
      case WHITE_KNIGHT:
        return WHITE_KNIGHT;
      case WHITE_PAWN:
        return WHITE_PAWN;
      case WHITE_QUEEN:
        return WHITE_QUEEN;
      case WHITE_ROOK:
        return WHITE_ROOK;
      case NONE:
        return Piece.NONE;
      default:
        throw new IllegalArgumentException();
    }
  }

  public static PieceType convertToMyPieceType(com.github.bhlangonijr.chesslib.Piece piece) {
    switch (piece) {
      case BLACK_BISHOP:
        return BISHOP;
      case BLACK_KING:
        return KING;
      case BLACK_KNIGHT:
        return KNIGHT;
      case BLACK_PAWN:
        return PAWN;
      case BLACK_QUEEN:
        return QUEEN;
      case BLACK_ROOK:
        return ROOK;
      case WHITE_BISHOP:
        return BISHOP;
      case WHITE_KING:
        return KING;
      case WHITE_KNIGHT:
        return KNIGHT;
      case WHITE_PAWN:
        return PAWN;
      case WHITE_QUEEN:
        return QUEEN;
      case WHITE_ROOK:
        return ROOK;
      case NONE:
        return PieceType.NONE;
      default:
        throw new IllegalArgumentException();
    }
  }

  public static PromotionPieceType convertToMyPromotionPieceType(com.github.bhlangonijr.chesslib.Piece piece) {
    switch (piece) {
      case BLACK_BISHOP:
        return PromotionPieceType.BISHOP;
      case BLACK_KNIGHT:
        return PromotionPieceType.KNIGHT;
      case BLACK_QUEEN:
        return PromotionPieceType.QUEEN;
      case BLACK_ROOK:
        return PromotionPieceType.ROOK;
      case WHITE_BISHOP:
        return PromotionPieceType.BISHOP;
      case WHITE_KNIGHT:
        return PromotionPieceType.KNIGHT;
      case WHITE_QUEEN:
        return PromotionPieceType.QUEEN;
      case WHITE_ROOK:
        return PromotionPieceType.ROOK;
      case BLACK_KING:
      case WHITE_KING:
      case BLACK_PAWN:
      case WHITE_PAWN:
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static Side convertToMySide(com.github.bhlangonijr.chesslib.Side side) {
    switch (side) {
      case BLACK:
        return BLACK;
      case WHITE:
        return WHITE;
      default:
        throw new IllegalArgumentException();
    }
  }

  public static com.github.bhlangonijr.chesslib.Side convertToSide(Side side) {
    switch (side) {
      case BLACK:
        return com.github.bhlangonijr.chesslib.Side.BLACK;
      case WHITE:
        return com.github.bhlangonijr.chesslib.Side.WHITE;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
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
    switch (piece) {
      case BLACK_BISHOP:
        return BLACK_BISHOP;
      case BLACK_KING:
        return BLACK_KING;
      case BLACK_KNIGHT:
        return BLACK_KNIGHT;
      case BLACK_PAWN:
        return BLACK_PAWN;
      case BLACK_QUEEN:
        return BLACK_QUEEN;
      case BLACK_ROOK:
        return BLACK_ROOK;
      case NONE:
        return Piece.NONE;
      case WHITE_BISHOP:
        return WHITE_BISHOP;
      case WHITE_KING:
        return WHITE_KING;
      case WHITE_KNIGHT:
        return WHITE_KNIGHT;
      case WHITE_PAWN:
        return WHITE_PAWN;
      case WHITE_QUEEN:
        return WHITE_QUEEN;
      case WHITE_ROOK:
        return WHITE_ROOK;
      default:
        throw new IllegalArgumentException();
    }
  }

  public static CastlingRight convert(CastleRight castlingRight) {
    switch (castlingRight) {
      case KING_AND_QUEEN_SIDE:
        return CastlingRight.KING_AND_QUEEN_SIDE;
      case KING_SIDE:
        return CastlingRight.KING_SIDE;
      case NONE:
        return CastlingRight.NONE;
      case QUEEN_SIDE:
        return CastlingRight.QUEEN_SIDE;
      default:
        throw new IllegalArgumentException();
    }
  }
}
