package com.dlb.chess.board.enums;

import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.exceptions.NonePointerException;
import com.dlb.chess.internationalization.Message;

public enum PieceType {
  ROOK(ChessConstants.ROOK_LETTER, 5, Message.getString("pieceType.rook.name")),
  KNIGHT(ChessConstants.KNIGHT_LETTER, 3, Message.getString("pieceType.knight.name")),
  BISHOP(ChessConstants.BISHOP_LETTER, 3, Message.getString("pieceType.bishop.name")),
  QUEEN(ChessConstants.QUEEN_LETTER, 9, Message.getString("pieceType.queen.name")),
  // value is only used for move ordering for unwinnability
  KING(ChessConstants.KING_LETTER, 0, Message.getString("pieceType.king.name")),
  PAWN(ChessConstants.PAWN_LETTER, 1, Message.getString("pieceType.pawn.name")),
  NONE(" ", -1, "");

  private final String letter;
  private final int value;
  private final String name;

  public String getLetter() {
    check();
    return letter;
  }

  public int getValue() {
    check();
    return value;
  }

  public String getName() {
    check();
    return name;
  }

  PieceType(String letter, int value, String name) {
    this.letter = letter;
    this.value = value;
    this.name = name;
  }

  public static Piece calculate(Side havingMove, PieceType pieceType) {
    return switch (havingMove) {
      case BLACK -> switch (pieceType) {
        case BISHOP -> Piece.BLACK_BISHOP;
        case KING -> Piece.BLACK_KING;
        case KNIGHT -> Piece.BLACK_KNIGHT;
        case QUEEN -> Piece.BLACK_QUEEN;
        case ROOK -> Piece.BLACK_ROOK;
        case PAWN -> Piece.BLACK_PAWN;
        case NONE -> throw new IllegalArgumentException();
        default -> throw new IllegalArgumentException();
      };
      case WHITE -> switch (pieceType) {
        case BISHOP -> Piece.WHITE_BISHOP;
        case KING -> Piece.WHITE_KING;
        case KNIGHT -> Piece.WHITE_KNIGHT;
        case QUEEN -> Piece.WHITE_QUEEN;
        case ROOK -> Piece.WHITE_ROOK;
        case PAWN -> Piece.WHITE_PAWN;
        case NONE -> throw new IllegalArgumentException();
        default -> throw new IllegalArgumentException();
      };
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  private void check() {
    if (this == NONE) {
      throw new NonePointerException();
    }
  }

}
