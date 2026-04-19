package com.dlb.chess.board.enums;

import com.dlb.chess.common.exceptions.NonePointerException;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.google.common.collect.ImmutableList;

public enum Piece {
  WHITE_PAWN('P', PieceType.PAWN, Side.WHITE),
  WHITE_ROOK('R', PieceType.ROOK, Side.WHITE),
  WHITE_KNIGHT('N', PieceType.KNIGHT, Side.WHITE),
  WHITE_BISHOP('B', PieceType.BISHOP, Side.WHITE),
  WHITE_QUEEN('Q', PieceType.QUEEN, Side.WHITE),
  WHITE_KING('K', PieceType.KING, Side.WHITE),
  BLACK_PAWN('p', PieceType.PAWN, Side.BLACK),
  BLACK_ROOK('r', PieceType.ROOK, Side.BLACK),
  BLACK_KNIGHT('n', PieceType.KNIGHT, Side.BLACK),
  BLACK_BISHOP('b', PieceType.BISHOP, Side.BLACK),
  BLACK_QUEEN('q', PieceType.QUEEN, Side.BLACK),
  BLACK_KING('k', PieceType.KING, Side.BLACK),
  NONE('\0', PieceType.NONE, Side.NONE);

  @SuppressWarnings("null")
  public static final ImmutableList<Piece> REAL = ImmutableList.of(WHITE_PAWN, WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP,
      WHITE_QUEEN, WHITE_KING, BLACK_PAWN, BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING);

  private final char letter;
  private final PieceType pieceType;
  private final Side side;

  Piece(char letter, PieceType pieceType, Side side) {
    this.letter = letter;
    this.pieceType = pieceType;
    this.side = side;
  }

  public char getLetter() {
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

  public static Piece calculateRookPiece(Side side) {
    return switch (side) {
      case BLACK -> BLACK_ROOK;
      case WHITE -> WHITE_ROOK;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static Piece calculateKnightPiece(Side side) {
    return switch (side) {
      case BLACK -> BLACK_KNIGHT;
      case WHITE -> WHITE_KNIGHT;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static Piece calculateBishopPiece(Side side) {
    return switch (side) {
      case BLACK -> BLACK_BISHOP;
      case WHITE -> WHITE_BISHOP;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static Piece calculateQueenPiece(Side side) {
    return switch (side) {
      case BLACK -> BLACK_QUEEN;
      case WHITE -> WHITE_QUEEN;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static Piece calculateKingPiece(Side side) {
    return switch (side) {
      case BLACK -> BLACK_KING;
      case WHITE -> WHITE_KING;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static Piece calculatePawnPiece(Side side) {
    return switch (side) {
      case BLACK -> BLACK_PAWN;
      case WHITE -> WHITE_PAWN;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static boolean exists(char letter) {
    for (final Piece piece : values()) {
      if (piece != NONE && piece.getLetter() == letter) {
        return true;
      }
    }
    return false;
  }

  public static Piece calculate(char letter) {
    if (!exists(letter)) {
      throw new IllegalArgumentException("No piece for this letter exists");
    }
    for (final Piece piece : values()) {
      if (piece != NONE && piece.getLetter() == letter) {
        return piece;
      }
    }
    // not possible to come here
    throw new ProgrammingMistakeException();
  }

  public static Piece calculate(Side side, PieceType pieceType) {
    return switch (pieceType) {
      case PAWN -> calculatePawnPiece(side);
      case ROOK -> calculateRookPiece(side);
      case KNIGHT -> calculateKnightPiece(side);
      case BISHOP -> calculateBishopPiece(side);
      case QUEEN -> calculateQueenPiece(side);
      case KING -> calculateKingPiece(side);
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
