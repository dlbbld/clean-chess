package com.dlb.chess.board.enums;

import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.exceptions.NonePointerException;
import com.dlb.chess.internationalization.Message;

public enum PieceType {
  ROOK(ChessConstants.ROOK_LETTER, Message.getString("pieceType.rook.name")),
  KNIGHT(ChessConstants.KNIGHT_LETTER, Message.getString("pieceType.knight.name")),
  BISHOP(ChessConstants.BISHOP_LETTER, Message.getString("pieceType.bishop.name")),
  QUEEN(ChessConstants.QUEEN_LETTER, Message.getString("pieceType.queen.name")),
  KING(ChessConstants.KING_LETTER, Message.getString("pieceType.king.name")),
  PAWN(ChessConstants.PAWN_LETTER, Message.getString("pieceType.pawn.name")),
  NONE(" ", "");

  private final String letter;
  private final String name;

  public String getLetter() {
    check();
    return letter;
  }

  public String getName() {
    check();
    return name;
  }

  PieceType(String letter, String name) {
    this.letter = letter;
    this.name = name;
  }

  public static Piece calculate(Side havingMove, PieceType pieceType) {
    switch (havingMove) {
      case BLACK:
        switch (pieceType) {
          case BISHOP:
            return Piece.BLACK_BISHOP;
          case KING:
            return Piece.BLACK_KING;
          case KNIGHT:
            return Piece.BLACK_KNIGHT;
          case QUEEN:
            return Piece.BLACK_QUEEN;
          case ROOK:
            return Piece.BLACK_ROOK;
          case PAWN:
            return Piece.BLACK_PAWN;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      case WHITE:
        switch (pieceType) {
          case BISHOP:
            return Piece.WHITE_BISHOP;
          case KING:
            return Piece.WHITE_KING;
          case KNIGHT:
            return Piece.WHITE_KNIGHT;
          case QUEEN:
            return Piece.WHITE_QUEEN;
          case ROOK:
            return Piece.WHITE_ROOK;
          case PAWN:
            return Piece.WHITE_PAWN;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  private void check() {
    if (this == NONE) {
      throw new NonePointerException();
    }
  }

}
