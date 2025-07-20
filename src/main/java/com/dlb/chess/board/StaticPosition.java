package com.dlb.chess.board;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.board.model.UpdateSquare;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;

public record StaticPosition(@NonNull Piece a8, @NonNull Piece b8, @NonNull Piece c8, @NonNull Piece d8,
    @NonNull Piece e8, @NonNull Piece f8, @NonNull Piece g8, @NonNull Piece h8, @NonNull Piece a7, @NonNull Piece b7,
    @NonNull Piece c7, @NonNull Piece d7, @NonNull Piece e7, @NonNull Piece f7, @NonNull Piece g7, @NonNull Piece h7,
    @NonNull Piece a6, @NonNull Piece b6, @NonNull Piece c6, @NonNull Piece d6, @NonNull Piece e6, @NonNull Piece f6,
    @NonNull Piece g6, @NonNull Piece h6, @NonNull Piece a5, @NonNull Piece b5, @NonNull Piece c5, @NonNull Piece d5,
    @NonNull Piece e5, @NonNull Piece f5, @NonNull Piece g5, @NonNull Piece h5, @NonNull Piece a4, @NonNull Piece b4,
    @NonNull Piece c4, @NonNull Piece d4, @NonNull Piece e4, @NonNull Piece f4, @NonNull Piece g4, @NonNull Piece h4,
    @NonNull Piece a3, @NonNull Piece b3, @NonNull Piece c3, @NonNull Piece d3, @NonNull Piece e3, @NonNull Piece f3,
    @NonNull Piece g3, @NonNull Piece h3, @NonNull Piece a2, @NonNull Piece b2, @NonNull Piece c2, @NonNull Piece d2,
    @NonNull Piece e2, @NonNull Piece f2, @NonNull Piece g2, @NonNull Piece h2, @NonNull Piece a1, @NonNull Piece b1,
    @NonNull Piece c1, @NonNull Piece d1, @NonNull Piece e1, @NonNull Piece f1, @NonNull Piece g1, @NonNull Piece h1)
    implements EnumConstants {

  public static final StaticPosition INITIAL_POSITION = new StaticPosition(BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP,
      BLACK_QUEEN, BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN,
      BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE,
      Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE,
      Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE,
      Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE,
      WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_ROOK,
      WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK);

  public static final StaticPosition EMPTY_POSITION = new StaticPosition(Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE,
      Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE,
      Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE,
      Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE,
      Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE,
      Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE,
      Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE,
      Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE);

  @Override
  public String toString() {
    final var output = new StringBuilder();

    output.append(calculateSquareLetter(a8));
    output.append(calculateSquareLetter(b8));
    output.append(calculateSquareLetter(c8));
    output.append(calculateSquareLetter(d8));
    output.append(calculateSquareLetter(e8));
    output.append(calculateSquareLetter(f8));
    output.append(calculateSquareLetter(g8));
    output.append(calculateSquareLetter(h8));
    output.append("\n");

    output.append(calculateSquareLetter(a7));
    output.append(calculateSquareLetter(b7));
    output.append(calculateSquareLetter(c7));
    output.append(calculateSquareLetter(d7));
    output.append(calculateSquareLetter(e7));
    output.append(calculateSquareLetter(f7));
    output.append(calculateSquareLetter(g7));
    output.append(calculateSquareLetter(h7));
    output.append("\n");

    output.append(calculateSquareLetter(a6));
    output.append(calculateSquareLetter(b6));
    output.append(calculateSquareLetter(c6));
    output.append(calculateSquareLetter(d6));
    output.append(calculateSquareLetter(e6));
    output.append(calculateSquareLetter(f6));
    output.append(calculateSquareLetter(g6));
    output.append(calculateSquareLetter(h6));
    output.append("\n");

    output.append(calculateSquareLetter(a5));
    output.append(calculateSquareLetter(b5));
    output.append(calculateSquareLetter(c5));
    output.append(calculateSquareLetter(d5));
    output.append(calculateSquareLetter(e5));
    output.append(calculateSquareLetter(f5));
    output.append(calculateSquareLetter(g5));
    output.append(calculateSquareLetter(h5));
    output.append("\n");

    output.append(calculateSquareLetter(a4));
    output.append(calculateSquareLetter(b4));
    output.append(calculateSquareLetter(c4));
    output.append(calculateSquareLetter(d4));
    output.append(calculateSquareLetter(e4));
    output.append(calculateSquareLetter(f4));
    output.append(calculateSquareLetter(g4));
    output.append(calculateSquareLetter(h4));
    output.append("\n");

    output.append(calculateSquareLetter(a3));
    output.append(calculateSquareLetter(b3));
    output.append(calculateSquareLetter(c3));
    output.append(calculateSquareLetter(d3));
    output.append(calculateSquareLetter(e3));
    output.append(calculateSquareLetter(f3));
    output.append(calculateSquareLetter(g3));
    output.append(calculateSquareLetter(h3));
    output.append("\n");

    output.append(calculateSquareLetter(a2));
    output.append(calculateSquareLetter(b2));
    output.append(calculateSquareLetter(c2));
    output.append(calculateSquareLetter(d2));
    output.append(calculateSquareLetter(e2));
    output.append(calculateSquareLetter(f2));
    output.append(calculateSquareLetter(g2));
    output.append(calculateSquareLetter(h2));
    output.append("\n");

    output.append(calculateSquareLetter(a1));
    output.append(calculateSquareLetter(b1));
    output.append(calculateSquareLetter(c1));
    output.append(calculateSquareLetter(d1));
    output.append(calculateSquareLetter(e1));
    output.append(calculateSquareLetter(f1));
    output.append(calculateSquareLetter(g1));
    output.append(calculateSquareLetter(h1));
    output.append("\n");

    return NonNullWrapperCommon.toString(output);
  }

  private static String calculateSquareLetter(Piece piece) {
    if (piece == Piece.NONE) {
      return ".";
    }
    return piece.getLetter();
  }

  public StaticPosition createChangedPosition(Square square, Piece piece) {
    final List<UpdateSquare> updateSquareList = new ArrayList<>();
    updateSquareList.add(new UpdateSquare(square, piece));
    return createChangedPosition(updateSquareList);
  }

  public StaticPosition createChangedPosition(Square square) {
    final List<UpdateSquare> updateSquareList = new ArrayList<>();
    updateSquareList.add(new UpdateSquare(square));
    return createChangedPosition(updateSquareList);
  }

  public StaticPosition createChangedPosition(List<UpdateSquare> updateSquareList) {
    Piece newA8 = a8;
    Piece newB8 = b8;
    Piece newC8 = c8;
    Piece newD8 = d8;
    Piece newE8 = e8;
    Piece newF8 = f8;
    Piece newG8 = g8;
    Piece newH8 = h8;
    Piece newA7 = a7;
    Piece newB7 = b7;
    Piece newC7 = c7;
    Piece newD7 = d7;
    Piece newE7 = e7;
    Piece newF7 = f7;
    Piece newG7 = g7;
    Piece newH7 = h7;
    Piece newA6 = a6;
    Piece newB6 = b6;
    Piece newC6 = c6;
    Piece newD6 = d6;
    Piece newE6 = e6;
    Piece newF6 = f6;
    Piece newG6 = g6;
    Piece newH6 = h6;
    Piece newA5 = a5;
    Piece newB5 = b5;
    Piece newC5 = c5;
    Piece newD5 = d5;
    Piece newE5 = e5;
    Piece newF5 = f5;
    Piece newG5 = g5;
    Piece newH5 = h5;
    Piece newA4 = a4;
    Piece newB4 = b4;
    Piece newC4 = c4;
    Piece newD4 = d4;
    Piece newE4 = e4;
    Piece newF4 = f4;
    Piece newG4 = g4;
    Piece newH4 = h4;
    Piece newA3 = a3;
    Piece newB3 = b3;
    Piece newC3 = c3;
    Piece newD3 = d3;
    Piece newE3 = e3;
    Piece newF3 = f3;
    Piece newG3 = g3;
    Piece newH3 = h3;
    Piece newA2 = a2;
    Piece newB2 = b2;
    Piece newC2 = c2;
    Piece newD2 = d2;
    Piece newE2 = e2;
    Piece newF2 = f2;
    Piece newG2 = g2;
    Piece newH2 = h2;
    Piece newA1 = a1;
    Piece newB1 = b1;
    Piece newC1 = c1;
    Piece newD1 = d1;
    Piece newE1 = e1;
    Piece newF1 = f1;
    Piece newG1 = g1;
    Piece newH1 = h1;

    for (final UpdateSquare updateSquare : updateSquareList) {
      final Square square = updateSquare.square();
      final Piece newPiece = updateSquare.piece();
      switch (square) {
        case A8:
          checkUpdateSquare(square, newA8, newPiece);
          newA8 = newPiece;
          break;
        case B8:
          checkUpdateSquare(square, newB8, newPiece);
          newB8 = newPiece;
          break;
        case C8:
          checkUpdateSquare(square, newC8, newPiece);
          newC8 = newPiece;
          break;
        case D8:
          checkUpdateSquare(square, newD8, newPiece);
          newD8 = newPiece;
          break;
        case E8:
          checkUpdateSquare(square, newE8, newPiece);
          newE8 = newPiece;
          break;
        case F8:
          checkUpdateSquare(square, newF8, newPiece);
          newF8 = newPiece;
          break;
        case G8:
          checkUpdateSquare(square, newG8, newPiece);
          newG8 = newPiece;
          break;
        case H8:
          checkUpdateSquare(square, newH8, newPiece);
          newH8 = newPiece;
          break;
        case A7:
          checkUpdateSquare(square, newA7, newPiece);
          newA7 = newPiece;
          break;
        case B7:
          checkUpdateSquare(square, newB7, newPiece);
          newB7 = newPiece;
          break;
        case C7:
          checkUpdateSquare(square, newC7, newPiece);
          newC7 = newPiece;
          break;
        case D7:
          checkUpdateSquare(square, newD7, newPiece);
          newD7 = newPiece;
          break;
        case E7:
          checkUpdateSquare(square, newE7, newPiece);
          newE7 = newPiece;
          break;
        case F7:
          checkUpdateSquare(square, newF7, newPiece);
          newF7 = newPiece;
          break;
        case G7:
          checkUpdateSquare(square, newG7, newPiece);
          newG7 = newPiece;
          break;
        case H7:
          checkUpdateSquare(square, newH7, newPiece);
          newH7 = newPiece;
          break;
        case A6:
          checkUpdateSquare(square, newA6, newPiece);
          newA6 = newPiece;
          break;
        case B6:
          checkUpdateSquare(square, newB6, newPiece);
          newB6 = newPiece;
          break;
        case C6:
          checkUpdateSquare(square, newC6, newPiece);
          newC6 = newPiece;
          break;
        case D6:
          checkUpdateSquare(square, newD6, newPiece);
          newD6 = newPiece;
          break;
        case E6:
          checkUpdateSquare(square, newE6, newPiece);
          newE6 = newPiece;
          break;
        case F6:
          checkUpdateSquare(square, newF6, newPiece);
          newF6 = newPiece;
          break;
        case G6:
          checkUpdateSquare(square, newG6, newPiece);
          newG6 = newPiece;
          break;
        case H6:
          checkUpdateSquare(square, newH6, newPiece);
          newH6 = newPiece;
          break;
        case A5:
          checkUpdateSquare(square, newA5, newPiece);
          newA5 = newPiece;
          break;
        case B5:
          checkUpdateSquare(square, newB5, newPiece);
          newB5 = newPiece;
          break;
        case C5:
          checkUpdateSquare(square, newC5, newPiece);
          newC5 = newPiece;
          break;
        case D5:
          checkUpdateSquare(square, newD5, newPiece);
          newD5 = newPiece;
          break;
        case E5:
          checkUpdateSquare(square, newE5, newPiece);
          newE5 = newPiece;
          break;
        case F5:
          checkUpdateSquare(square, newF5, newPiece);
          newF5 = newPiece;
          break;
        case G5:
          checkUpdateSquare(square, newG5, newPiece);
          newG5 = newPiece;
          break;
        case H5:
          checkUpdateSquare(square, newH5, newPiece);
          newH5 = newPiece;
          break;
        case A4:
          checkUpdateSquare(square, newA4, newPiece);
          newA4 = newPiece;
          break;
        case B4:
          checkUpdateSquare(square, newB4, newPiece);
          newB4 = newPiece;
          break;
        case C4:
          checkUpdateSquare(square, newC4, newPiece);
          newC4 = newPiece;
          break;
        case D4:
          checkUpdateSquare(square, newD4, newPiece);
          newD4 = newPiece;
          break;
        case E4:
          checkUpdateSquare(square, newE4, newPiece);
          newE4 = newPiece;
          break;
        case F4:
          checkUpdateSquare(square, newF4, newPiece);
          newF4 = newPiece;
          break;
        case G4:
          checkUpdateSquare(square, newG4, newPiece);
          newG4 = newPiece;
          break;
        case H4:
          checkUpdateSquare(square, newH4, newPiece);
          newH4 = newPiece;
          break;
        case A3:
          checkUpdateSquare(square, newA3, newPiece);
          newA3 = newPiece;
          break;
        case B3:
          checkUpdateSquare(square, newB3, newPiece);
          newB3 = newPiece;
          break;
        case C3:
          checkUpdateSquare(square, newC3, newPiece);
          newC3 = newPiece;
          break;
        case D3:
          checkUpdateSquare(square, newD3, newPiece);
          newD3 = newPiece;
          break;
        case E3:
          checkUpdateSquare(square, newE3, newPiece);
          newE3 = newPiece;
          break;
        case F3:
          checkUpdateSquare(square, newF3, newPiece);
          newF3 = newPiece;
          break;
        case G3:
          checkUpdateSquare(square, newG3, newPiece);
          newG3 = newPiece;
          break;
        case H3:
          checkUpdateSquare(square, newH3, newPiece);
          newH3 = newPiece;
          break;
        case A2:
          checkUpdateSquare(square, newA2, newPiece);
          newA2 = newPiece;
          break;
        case B2:
          checkUpdateSquare(square, newB2, newPiece);
          newB2 = newPiece;
          break;
        case C2:
          checkUpdateSquare(square, newC2, newPiece);
          newC2 = newPiece;
          break;
        case D2:
          checkUpdateSquare(square, newD2, newPiece);
          newD2 = newPiece;
          break;
        case E2:
          checkUpdateSquare(square, newE2, newPiece);
          newE2 = newPiece;
          break;
        case F2:
          checkUpdateSquare(square, newF2, newPiece);
          newF2 = newPiece;
          break;
        case G2:
          checkUpdateSquare(square, newG2, newPiece);
          newG2 = newPiece;
          break;
        case H2:
          checkUpdateSquare(square, newH2, newPiece);
          newH2 = newPiece;
          break;
        case A1:
          checkUpdateSquare(square, newA1, newPiece);
          newA1 = newPiece;
          break;
        case B1:
          checkUpdateSquare(square, newB1, newPiece);
          newB1 = newPiece;
          break;
        case C1:
          checkUpdateSquare(square, newC1, newPiece);
          newC1 = newPiece;
          break;
        case D1:
          checkUpdateSquare(square, newD1, newPiece);
          newD1 = newPiece;
          break;
        case E1:
          checkUpdateSquare(square, newE1, newPiece);
          newE1 = newPiece;
          break;
        case F1:
          checkUpdateSquare(square, newF1, newPiece);
          newF1 = newPiece;
          break;
        case G1:
          checkUpdateSquare(square, newG1, newPiece);
          newG1 = newPiece;
          break;
        case H1:
          checkUpdateSquare(square, newH1, newPiece);
          newH1 = newPiece;
          break;
        case NONE:
          throw new IllegalArgumentException("The none square does not belong to the board)");
        default:
          throw new IllegalArgumentException();
      }
    }

    return new StaticPosition(newA8, newB8, newC8, newD8, newE8, newF8, newG8, newH8, newA7, newB7, newC7, newD7, newE7,
        newF7, newG7, newH7, newA6, newB6, newC6, newD6, newE6, newF6, newG6, newH6, newA5, newB5, newC5, newD5, newE5,
        newF5, newG5, newH5, newA4, newB4, newC4, newD4, newE4, newF4, newG4, newH4, newA3, newB3, newC3, newD3, newE3,
        newF3, newG3, newH3, newA2, newB2, newC2, newD2, newE2, newF2, newG2, newH2, newA1, newB1, newC1, newD1, newE1,
        newF1, newG1, newH1);
  }

  // we only allow updates which change the board enforce good programming style
  private static void checkUpdateSquare(Square square, Piece currentPiece, Piece newPiece) {
    if (currentPiece == newPiece) {
      throw new IllegalArgumentException(
          "The square " + square.getName() + " is requested to be populated with the piece " + newPiece
              + " but already contains this piece. This operation is not supported.");
    }
  }

  public Piece get(Square square) {
    return switch (square) {
      case A8 -> a8;
      case B8 -> b8;
      case C8 -> c8;
      case D8 -> d8;
      case E8 -> e8;
      case F8 -> f8;
      case G8 -> g8;
      case H8 -> h8;
      case A7 -> a7;
      case B7 -> b7;
      case C7 -> c7;
      case D7 -> d7;
      case E7 -> e7;
      case F7 -> f7;
      case G7 -> g7;
      case H7 -> h7;
      case A6 -> a6;
      case B6 -> b6;
      case C6 -> c6;
      case D6 -> d6;
      case E6 -> e6;
      case F6 -> f6;
      case G6 -> g6;
      case H6 -> h6;
      case A5 -> a5;
      case B5 -> b5;
      case C5 -> c5;
      case D5 -> d5;
      case E5 -> e5;
      case F5 -> f5;
      case G5 -> g5;
      case H5 -> h5;
      case A4 -> a4;
      case B4 -> b4;
      case C4 -> c4;
      case D4 -> d4;
      case E4 -> e4;
      case F4 -> f4;
      case G4 -> g4;
      case H4 -> h4;
      case A3 -> a3;
      case B3 -> b3;
      case C3 -> c3;
      case D3 -> d3;
      case E3 -> e3;
      case F3 -> f3;
      case G3 -> g3;
      case H3 -> h3;
      case A2 -> a2;
      case B2 -> b2;
      case C2 -> c2;
      case D2 -> d2;
      case E2 -> e2;
      case F2 -> f2;
      case G2 -> g2;
      case H2 -> h2;
      case A1 -> a1;
      case B1 -> b1;
      case C1 -> c1;
      case D1 -> d1;
      case E1 -> e1;
      case F1 -> f1;
      case G1 -> g1;
      case H1 -> h1;
      case NONE -> throw new IllegalArgumentException("The none square does not belong to the board)");
      default -> throw new IllegalArgumentException();
    };
  }

  public boolean isEmpty(Square square) {
    return switch (square) {
      case A8 -> a8 == Piece.NONE;
      case B8 -> b8 == Piece.NONE;
      case C8 -> c8 == Piece.NONE;
      case D8 -> d8 == Piece.NONE;
      case E8 -> e8 == Piece.NONE;
      case F8 -> f8 == Piece.NONE;
      case G8 -> g8 == Piece.NONE;
      case H8 -> h8 == Piece.NONE;
      case A7 -> a7 == Piece.NONE;
      case B7 -> b7 == Piece.NONE;
      case C7 -> c7 == Piece.NONE;
      case D7 -> d7 == Piece.NONE;
      case E7 -> e7 == Piece.NONE;
      case F7 -> f7 == Piece.NONE;
      case G7 -> g7 == Piece.NONE;
      case H7 -> h7 == Piece.NONE;
      case A6 -> a6 == Piece.NONE;
      case B6 -> b6 == Piece.NONE;
      case C6 -> c6 == Piece.NONE;
      case D6 -> d6 == Piece.NONE;
      case E6 -> e6 == Piece.NONE;
      case F6 -> f6 == Piece.NONE;
      case G6 -> g6 == Piece.NONE;
      case H6 -> h6 == Piece.NONE;
      case A5 -> a5 == Piece.NONE;
      case B5 -> b5 == Piece.NONE;
      case C5 -> c5 == Piece.NONE;
      case D5 -> d5 == Piece.NONE;
      case E5 -> e5 == Piece.NONE;
      case F5 -> f5 == Piece.NONE;
      case G5 -> g5 == Piece.NONE;
      case H5 -> h5 == Piece.NONE;
      case A4 -> a4 == Piece.NONE;
      case B4 -> b4 == Piece.NONE;
      case C4 -> c4 == Piece.NONE;
      case D4 -> d4 == Piece.NONE;
      case E4 -> e4 == Piece.NONE;
      case F4 -> f4 == Piece.NONE;
      case G4 -> g4 == Piece.NONE;
      case H4 -> h4 == Piece.NONE;
      case A3 -> a3 == Piece.NONE;
      case B3 -> b3 == Piece.NONE;
      case C3 -> c3 == Piece.NONE;
      case D3 -> d3 == Piece.NONE;
      case E3 -> e3 == Piece.NONE;
      case F3 -> f3 == Piece.NONE;
      case G3 -> g3 == Piece.NONE;
      case H3 -> h3 == Piece.NONE;
      case A2 -> a2 == Piece.NONE;
      case B2 -> b2 == Piece.NONE;
      case C2 -> c2 == Piece.NONE;
      case D2 -> d2 == Piece.NONE;
      case E2 -> e2 == Piece.NONE;
      case F2 -> f2 == Piece.NONE;
      case G2 -> g2 == Piece.NONE;
      case H2 -> h2 == Piece.NONE;
      case A1 -> a1 == Piece.NONE;
      case B1 -> b1 == Piece.NONE;
      case C1 -> c1 == Piece.NONE;
      case D1 -> d1 == Piece.NONE;
      case E1 -> e1 == Piece.NONE;
      case F1 -> f1 == Piece.NONE;
      case G1 -> g1 == Piece.NONE;
      case H1 -> h1 == Piece.NONE;
      case NONE -> throw new IllegalArgumentException("The none square does not belong to the board)");
      default -> throw new IllegalArgumentException();
    };
  }

  public boolean isOwnPiece(Square square, Side havingMove) {
    if (isEmpty(square)) {
      return false;
    }
    final Piece piece = get(square);
    return piece.getSide() == havingMove;
  }

  public boolean isOwnPiece(Square square, Side havingMove, PieceType pieceType) {
    if (isEmpty(square)) {
      return false;
    }
    final Piece piece = get(square);
    return piece.getSide() == havingMove && piece.getPieceType() == pieceType;
  }

  public boolean isOpponentPiece(Square square, Side havingMove) {
    return isOwnPiece(square, havingMove.getOppositeSide());
  }

  public boolean isOwnPawn(Square square, Side havingMove) {
    return isOwnPiece(square, havingMove, PAWN);
  }

  public boolean isOpponentPawn(Square square, Side havingMove) {
    return isOwnPiece(square, havingMove.getOppositeSide(), PAWN);
  }

  public boolean isPawn(Square square) {
    return isOwnPawn(square, WHITE) || isOwnPawn(square, BLACK);
  }

  public boolean isOwnKing(Square square, Side havingMove) {
    return isOwnPiece(square, havingMove, KING);
  }

  public boolean isOpponentKing(Square square, Side havingMove) {
    return isOwnPiece(square, havingMove.getOppositeSide(), KING);
  }
}
