package com.dlb.chess.board.enums;

import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.exceptions.NonePointerException;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;

public enum Piece {
  WHITE_ROOK(ChessConstants.ROOK_LETTER, PieceType.ROOK, Side.WHITE),
  WHITE_KNIGHT(ChessConstants.KNIGHT_LETTER, PieceType.KNIGHT, Side.WHITE),
  WHITE_BISHOP(ChessConstants.BISHOP_LETTER, PieceType.BISHOP, Side.WHITE),
  WHITE_QUEEN(ChessConstants.QUEEN_LETTER, PieceType.QUEEN, Side.WHITE),
  WHITE_KING(ChessConstants.KING_LETTER, PieceType.KING, Side.WHITE),
  WHITE_PAWN(ChessConstants.PAWN_LETTER, PieceType.PAWN, Side.WHITE),
  BLACK_ROOK(ChessConstants.ROOK_LETTER_LOWER_CASE, PieceType.ROOK, Side.BLACK),
  BLACK_KNIGHT(ChessConstants.KNIGHT_LETTER_LOWER_CASE, PieceType.KNIGHT, Side.BLACK),
  BLACK_BISHOP(ChessConstants.BISHOP_LETTER_LOWER_CASE, PieceType.BISHOP, Side.BLACK),
  BLACK_QUEEN(ChessConstants.QUEEN_LETTER_LOWER_CASE, PieceType.QUEEN, Side.BLACK),
  BLACK_KING(ChessConstants.KING_LETTER_LOWER_CASE, PieceType.KING, Side.BLACK),
  BLACK_PAWN(ChessConstants.PAWN_LETTER_LOWER_CASE, PieceType.PAWN, Side.BLACK),
  NONE(" ", PieceType.NONE, Side.NONE);

  private final String letter;
  private final PieceType pieceType;
  private final Side side;

  Piece(String letter, PieceType pieceType, Side side) {

    this.letter = letter;
    this.pieceType = pieceType;
    this.side = side;
  }

  public String getLetter() {
    check();
    return letter;
  }

  public PieceType getPieceType() {
    check();
    return pieceType;
  }

  public Side getSide() {
    check();
    return side;
  }

  public static Piece calculatePawnPiece(Side side) {
    switch (side) {
      case BLACK:
        return BLACK_PAWN;
      case WHITE:
        return WHITE_PAWN;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static Piece calculateKingPiece(Side side) {
    switch (side) {
      case BLACK:
        return BLACK_KING;
      case WHITE:
        return WHITE_KING;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static Piece calculateRookPiece(Side side) {
    switch (side) {
      case BLACK:
        return BLACK_ROOK;
      case WHITE:
        return WHITE_ROOK;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static boolean exists(String letter) {
    for (final Piece piece : values()) {
      if (piece != NONE && piece.getLetter().equals(letter)) {
        return true;
      }
    }
    return false;
  }

  public static Piece calculate(String letter) {
    if (!exists(letter)) {
      throw new IllegalArgumentException("No piece for this letter exists");
    }
    for (final Piece piece : values()) {
      if (piece != NONE && piece.getLetter().equals(letter)) {
        return piece;
      }
    }
    // not possible to come here
    throw new ProgrammingMistakeException();
  }

  private void check() {
    if (this == NONE) {
      throw new NonePointerException();
    }
  }
}
