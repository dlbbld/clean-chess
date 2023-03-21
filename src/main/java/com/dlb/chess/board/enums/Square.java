package com.dlb.chess.board.enums;

import static com.dlb.chess.common.utility.ImmutableUtility.constructListSquare;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.NonePointerException;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public enum Square implements Comparable<Square> {

  A1(SquareType.DARK_SQUARE, File.FILE_A, Rank.RANK_1, "a1"),
  B1(SquareType.LIGHT_SQUARE, File.FILE_B, Rank.RANK_1, "b1"),
  C1(SquareType.DARK_SQUARE, File.FILE_C, Rank.RANK_1, "c1"),
  D1(SquareType.LIGHT_SQUARE, File.FILE_D, Rank.RANK_1, "d1"),
  E1(SquareType.DARK_SQUARE, File.FILE_E, Rank.RANK_1, "e1"),
  F1(SquareType.LIGHT_SQUARE, File.FILE_F, Rank.RANK_1, "f1"),
  G1(SquareType.DARK_SQUARE, File.FILE_G, Rank.RANK_1, "g1"),
  H1(SquareType.LIGHT_SQUARE, File.FILE_H, Rank.RANK_1, "h1"),
  A2(SquareType.LIGHT_SQUARE, File.FILE_A, Rank.RANK_2, "a2"),
  B2(SquareType.DARK_SQUARE, File.FILE_B, Rank.RANK_2, "b2"),
  C2(SquareType.LIGHT_SQUARE, File.FILE_C, Rank.RANK_2, "c2"),
  D2(SquareType.DARK_SQUARE, File.FILE_D, Rank.RANK_2, "d2"),
  E2(SquareType.LIGHT_SQUARE, File.FILE_E, Rank.RANK_2, "e2"),
  F2(SquareType.DARK_SQUARE, File.FILE_F, Rank.RANK_2, "f2"),
  G2(SquareType.LIGHT_SQUARE, File.FILE_G, Rank.RANK_2, "g2"),
  H2(SquareType.DARK_SQUARE, File.FILE_H, Rank.RANK_2, "h2"),
  A3(SquareType.DARK_SQUARE, File.FILE_A, Rank.RANK_3, "a3"),
  B3(SquareType.LIGHT_SQUARE, File.FILE_B, Rank.RANK_3, "b3"),
  C3(SquareType.DARK_SQUARE, File.FILE_C, Rank.RANK_3, "c3"),
  D3(SquareType.LIGHT_SQUARE, File.FILE_D, Rank.RANK_3, "d3"),
  E3(SquareType.DARK_SQUARE, File.FILE_E, Rank.RANK_3, "e3"),
  F3(SquareType.LIGHT_SQUARE, File.FILE_F, Rank.RANK_3, "f3"),
  G3(SquareType.DARK_SQUARE, File.FILE_G, Rank.RANK_3, "g3"),
  H3(SquareType.LIGHT_SQUARE, File.FILE_H, Rank.RANK_3, "h3"),
  A4(SquareType.LIGHT_SQUARE, File.FILE_A, Rank.RANK_4, "a4"),
  B4(SquareType.DARK_SQUARE, File.FILE_B, Rank.RANK_4, "b4"),
  C4(SquareType.LIGHT_SQUARE, File.FILE_C, Rank.RANK_4, "c4"),
  D4(SquareType.DARK_SQUARE, File.FILE_D, Rank.RANK_4, "d4"),
  E4(SquareType.LIGHT_SQUARE, File.FILE_E, Rank.RANK_4, "e4"),
  F4(SquareType.DARK_SQUARE, File.FILE_F, Rank.RANK_4, "f4"),
  G4(SquareType.LIGHT_SQUARE, File.FILE_G, Rank.RANK_4, "g4"),
  H4(SquareType.DARK_SQUARE, File.FILE_H, Rank.RANK_4, "h4"),
  A5(SquareType.DARK_SQUARE, File.FILE_A, Rank.RANK_5, "a5"),
  B5(SquareType.LIGHT_SQUARE, File.FILE_B, Rank.RANK_5, "b5"),
  C5(SquareType.DARK_SQUARE, File.FILE_C, Rank.RANK_5, "c5"),
  D5(SquareType.LIGHT_SQUARE, File.FILE_D, Rank.RANK_5, "d5"),
  E5(SquareType.DARK_SQUARE, File.FILE_E, Rank.RANK_5, "e5"),
  F5(SquareType.LIGHT_SQUARE, File.FILE_F, Rank.RANK_5, "f5"),
  G5(SquareType.DARK_SQUARE, File.FILE_G, Rank.RANK_5, "g5"),
  H5(SquareType.LIGHT_SQUARE, File.FILE_H, Rank.RANK_5, "h5"),
  A6(SquareType.LIGHT_SQUARE, File.FILE_A, Rank.RANK_6, "a6"),
  B6(SquareType.DARK_SQUARE, File.FILE_B, Rank.RANK_6, "b6"),
  C6(SquareType.LIGHT_SQUARE, File.FILE_C, Rank.RANK_6, "c6"),
  D6(SquareType.DARK_SQUARE, File.FILE_D, Rank.RANK_6, "d6"),
  E6(SquareType.LIGHT_SQUARE, File.FILE_E, Rank.RANK_6, "e6"),
  F6(SquareType.DARK_SQUARE, File.FILE_F, Rank.RANK_6, "f6"),
  G6(SquareType.LIGHT_SQUARE, File.FILE_G, Rank.RANK_6, "g6"),
  H6(SquareType.DARK_SQUARE, File.FILE_H, Rank.RANK_6, "h6"),
  A7(SquareType.DARK_SQUARE, File.FILE_A, Rank.RANK_7, "a7"),
  B7(SquareType.LIGHT_SQUARE, File.FILE_B, Rank.RANK_7, "b7"),
  C7(SquareType.DARK_SQUARE, File.FILE_C, Rank.RANK_7, "c7"),
  D7(SquareType.LIGHT_SQUARE, File.FILE_D, Rank.RANK_7, "d7"),
  E7(SquareType.DARK_SQUARE, File.FILE_E, Rank.RANK_7, "e7"),
  F7(SquareType.LIGHT_SQUARE, File.FILE_F, Rank.RANK_7, "f7"),
  G7(SquareType.DARK_SQUARE, File.FILE_G, Rank.RANK_7, "g7"),
  H7(SquareType.LIGHT_SQUARE, File.FILE_H, Rank.RANK_7, "h7"),
  A8(SquareType.LIGHT_SQUARE, File.FILE_A, Rank.RANK_8, "a8"),
  B8(SquareType.DARK_SQUARE, File.FILE_B, Rank.RANK_8, "b8"),
  C8(SquareType.LIGHT_SQUARE, File.FILE_C, Rank.RANK_8, "c8"),
  D8(SquareType.DARK_SQUARE, File.FILE_D, Rank.RANK_8, "d8"),
  E8(SquareType.LIGHT_SQUARE, File.FILE_E, Rank.RANK_8, "e8"),
  F8(SquareType.DARK_SQUARE, File.FILE_F, Rank.RANK_8, "f8"),
  G8(SquareType.LIGHT_SQUARE, File.FILE_G, Rank.RANK_8, "g8"),
  H8(SquareType.DARK_SQUARE, File.FILE_H, Rank.RANK_8, "h8"),
  NONE(SquareType.NONE, File.NONE, Rank.NONE, "none");

  private final SquareType squareType;
  private final File file;
  private final Rank rank;
  private final String name;

  public static final ImmutableList<Square> SEVENTH_RANK = constructListSquare(A7, B7, C7, D7, E7, F7, G7, H7);
  public static final ImmutableList<Square> SECOND_RANK = constructListSquare(A2, B2, C2, D2, E2, F2, G2, H2);

  Square(SquareType squareType, File file, Rank rank, String name) {
    this.squareType = squareType;
    this.file = file;
    this.rank = rank;
    this.name = name;
  }

  public SquareType getSquareType() {
    check();
    return squareType;
  }

  public File getFile() {
    check();
    return file;
  }

  public Rank getRank() {
    check();
    return rank;
  }

  public String getName() {
    check();
    return name;
  }

  public static boolean exists(String name) {
    for (final Square square : BOARD_SQUARE_LIST) {
      if (square.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  public static boolean exists(File file, Rank rank) {
    for (final Square square : BOARD_SQUARE_LIST) {
      if (square.getFile() == file && square.getRank() == rank) {
        return true;
      }
    }
    return false;
  }

  public static boolean exists(int fileNumber, int rankNumber) {
    for (final Square square : BOARD_SQUARE_LIST) {
      if (square.getFile().getNumber() == fileNumber && square.getRank().getNumber() == rankNumber) {
        return true;
      }
    }
    return false;
  }

  public static Square calculate(String name) {
    if (!exists(name)) {
      throw new IllegalArgumentException("No board square exists for square name " + name);
    }
    for (final Square square : BOARD_SQUARE_LIST) {
      if (square.getName().equals(name)) {
        return square;
      }
    }
    throw new ProgrammingMistakeException("The code for calculating the square by name is wrong");
  }

  public static Square calculate(File file, Rank rank) {
    if (!exists(file, rank)) {
      throw new IllegalArgumentException("No board square exists for file enum " + file + " and rank enum " + rank);
    }
    for (final Square square : BOARD_SQUARE_LIST) {
      if (square.getFile() == file && square.getRank() == rank) {
        return square;
      }
    }
    throw new ProgrammingMistakeException("The code for calculating the square by file and rank is wrong");
  }

  public static Square calculate(int fileNumber, int rankNumber) {
    if (!exists(fileNumber, rankNumber)) {
      throw new IllegalArgumentException(
          "No board square exists for file number " + fileNumber + " and rank number " + rankNumber);
    }
    for (final Square square : BOARD_SQUARE_LIST) {
      if (square.getFile().getNumber() == fileNumber && square.getRank().getNumber() == rankNumber) {
        return square;
      }
    }
    throw new ProgrammingMistakeException("The code for calculating the square by file and rank number is wrong");
  }

  // all squares except the empty one
  // order is not allowed to be changed as this will cause semantical errors
  public static final ImmutableList<Square> BOARD_SQUARE_LIST = constructListSquare(A1, B1, C1, D1, E1, F1, G1, H1, A2,
      B2, C2, D2, E2, F2, G2, H2, A3, B3, C3, D3, E3, F3, G3, H3, A4, B4, C4, D4, E4, F4, G4, H4, A5, B5, C5, D5, E5,
      F5, G5, H5, A6, B6, C6, D6, E6, F6, G6, H6, A7, B7, C7, D7, E7, F7, G7, H7, A8, B8, C8, D8, E8, F8, G8, H8);

  public static final ImmutableList<Square> WHITE_LEFT_FILE = constructListSquare(A1, A2, A3, A4, A5, A6, A7, A8);

  public static final ImmutableList<Square> BLACK_LEFT_FILE = constructListSquare(H8, H7, H6, H5, H4, H3, H2, H1);

  public static List<Square> getLeftFile(Side side) {
    return switch (side) {
      case WHITE -> WHITE_LEFT_FILE;
      case BLACK -> BLACK_LEFT_FILE;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static boolean calculateIsLeftMostFile(Square square, Side side) {
    return switch (side) {
      case WHITE -> square.getFile() == File.FILE_A;
      case BLACK -> square.getFile() == File.FILE_H;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static boolean calculateIsRightMostFile(Square square, Side side) {
    return switch (side) {
      case WHITE -> square.getFile() == File.FILE_H;
      case BLACK -> square.getFile() == File.FILE_A;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static final ImmutableList<Square> WHITE_PROMOTION_RANK = constructListSquare(A8, B8, C8, D8, E8, F8, G8, H8);

  public static final ImmutableList<Square> BLACK_PROMOTION_RANK = constructListSquare(A1, B1, C1, D1, E1, F1, G1, H1);

  public static List<Square> getPromotionRank(Side side) {
    return switch (side) {
      case WHITE -> WHITE_PROMOTION_RANK;
      case BLACK -> BLACK_PROMOTION_RANK;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static final ImmutableList<Square> WHITE_MOVE_EN_PASSANT_CAPTURE_TARGET_SQUARE_LIST = constructListSquare(A6,
      B6, C6, D6, E6, F6, G6, H6);

  public static final ImmutableList<Square> BLACK_MOVE_EN_PASSANT_CAPTURE_TARGET_SQUARE_LIST = constructListSquare(A3,
      B3, C3, D3, E3, F3, G3, H3);

  private static final ImmutableMap<Square, Square> WHITE_TWO_SQUARE_ADVANCE_TO_JUMP_OVER;

  static {
    final EnumMap<Square, Square> map = NonNullWrapperCommon.newEnumMap(Square.class);

    map.put(A4, A3);
    map.put(B4, B3);
    map.put(C4, C3);
    map.put(D4, D3);
    map.put(E4, E3);
    map.put(F4, F3);
    map.put(G4, G3);
    map.put(H4, H3);

    WHITE_TWO_SQUARE_ADVANCE_TO_JUMP_OVER = NonNullWrapperCommon.immutableEnumMap(map);
  }

  private static final ImmutableMap<Square, Square> BLACK_TWO_SQUARE_ADVANCE_TO_JUMP_OVER;

  static {
    final EnumMap<Square, Square> map = NonNullWrapperCommon.newEnumMap(Square.class);

    map.put(A5, A6);
    map.put(B5, B6);
    map.put(C5, C6);
    map.put(D5, D6);
    map.put(E5, E6);
    map.put(F5, F6);
    map.put(G5, G6);
    map.put(H5, H6);

    BLACK_TWO_SQUARE_ADVANCE_TO_JUMP_OVER = NonNullWrapperCommon.immutableEnumMap(map);

  }

  public static Square calculateJumpOverSquare(Side sideHavingMadeTheMove, Square pawnTwoAdvanceSquare) {
    switch (sideHavingMadeTheMove) {
      case WHITE:
        if (!WHITE_TWO_SQUARE_ADVANCE_TO_JUMP_OVER.containsKey(pawnTwoAdvanceSquare)) {
          throw new IllegalArgumentException("The method only applies for pawn two square advance moves");
        }
        return NonNullWrapperCommon.get(WHITE_TWO_SQUARE_ADVANCE_TO_JUMP_OVER, pawnTwoAdvanceSquare);
      case BLACK:
        if (!BLACK_TWO_SQUARE_ADVANCE_TO_JUMP_OVER.containsKey(pawnTwoAdvanceSquare)) {
          throw new IllegalArgumentException("The method only applies for pawn two square advance moves");
        }
        return NonNullWrapperCommon.get(BLACK_TWO_SQUARE_ADVANCE_TO_JUMP_OVER, pawnTwoAdvanceSquare);
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static final boolean calculateIsRightDiagonalSquare(final Side havingMove, final Square squareLookFrom,
      final Square squareCheckIfRightDiagonal) {

    if (!calculateHasRightDiagonalSquare(havingMove, squareLookFrom)) {
      return false;
    }

    final Square diagonalSquare = calculateRightDiagonalSquare(havingMove, squareLookFrom);

    return diagonalSquare.equals(squareCheckIfRightDiagonal);
  }

  public static final boolean calculateIsLeftDiagonalSquare(final Side havingMove, final Square squareLookFrom,
      final Square squareCheckIfLeftDiagonal) {

    if (!calculateHasLeftDiagonalSquare(havingMove, squareLookFrom)) {
      return false;
    }
    final Square diagonalSquare = calculateLeftDiagonalSquare(havingMove, squareLookFrom);

    return diagonalSquare.equals(squareCheckIfLeftDiagonal);
  }

  public static final boolean calculateHasRightSquare(final Side havingMove, final Square square) {
    return File.calculateHasRightFile(havingMove, square.getFile());
  }

  public static final boolean calculateHasLeftSquare(final Side havingMove, final Square square) {
    return File.calculateHasLeftFile(havingMove, square.getFile());
  }

  public static final boolean calculateHasLeftDiagonalSquare(final Side havingMove, final Square square) {

    return File.calculateHasLeftFile(havingMove, square.getFile())
        && Rank.calculateHasNextRank(havingMove, square.getRank());
  }

  public static final boolean calculateHasRightDiagonalSquare(final Side havingMove, final Square square) {

    return File.calculateHasRightFile(havingMove, square.getFile())
        && Rank.calculateHasNextRank(havingMove, square.getRank());
  }

  public static final Square calculateLeftDiagonalSquare(final Side havingMove, final Square square) {

    if (!calculateHasLeftDiagonalSquare(havingMove, square)) {
      throw new IllegalArgumentException();
    }

    final File leftFile = File.calculateLeftFile(havingMove, square.getFile());
    final Rank nextRank = Rank.calculateNextRank(havingMove, square.getRank());
    return calculate(leftFile, nextRank);
  }

  public static final Square calculateRightDiagonalSquare(final Side havingMove, final Square square) {

    if (!calculateHasRightDiagonalSquare(havingMove, square)) {
      throw new IllegalArgumentException();
    }

    final File rightFile = File.calculateRightFile(havingMove, square.getFile());
    final Rank nextRank = Rank.calculateNextRank(havingMove, square.getRank());
    return calculate(rightFile, nextRank);
  }

  public static final boolean calculateHasBehindLeftDiagonalSquare(final Side havingMove, final Square square) {
    return calculateHasRightDiagonalSquare(havingMove.getOppositeSide(), square);
  }

  public static final boolean calculateHasBehindRightDiagonalSquare(final Side havingMove, final Square square) {
    return calculateHasLeftDiagonalSquare(havingMove.getOppositeSide(), square);
  }

  public static final Square calculateBehindLeftDiagonalSquare(final Side havingMove, final Square square) {
    return calculateRightDiagonalSquare(havingMove.getOppositeSide(), square);
  }

  public static final Square calculateBehindRightDiagonalSquare(final Side havingMove, final Square square) {
    return calculateLeftDiagonalSquare(havingMove.getOppositeSide(), square);
  }

  public static Square calculateKingOriginalSquare(Side side) {
    return switch (side) {
      case BLACK -> E8;
      case WHITE -> E1;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static Square calculateQueenSideRookOriginalSquare(Side side) {
    return switch (side) {
      case BLACK -> A8;
      case WHITE -> A1;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static Square calculateKingSideRookOriginalSquare(Side side) {
    return switch (side) {
      case BLACK -> H8;
      case WHITE -> H1;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static Square calculateRightSquare(Side havingMove, Square square) {

    if (!calculateHasRightSquare(havingMove, square)) {
      throw new IllegalArgumentException();
    }

    final File file = File.calculateRightFile(havingMove, square.getFile());
    final Rank rank = square.getRank();
    return calculate(file, rank);
  }

  public static Square calculateLeftSquare(Side havingMove, Square square) {

    if (!calculateHasLeftSquare(havingMove, square)) {
      throw new IllegalArgumentException();
    }

    final File file = File.calculateLeftFile(havingMove, square.getFile());
    final Rank rank = square.getRank();
    return calculate(file, rank);
  }

  public static boolean calculateHasAheadSquare(Side havingMove, Square square) {
    return Rank.calculateHasNextRank(havingMove, square.getRank());
  }

  public static Square calculateAheadSquare(Side havingMove, Square square) {

    if (!calculateHasAheadSquare(havingMove, square)) {
      throw new IllegalArgumentException();
    }

    final File file = square.getFile();
    final Rank rank = Rank.calculateNextRank(havingMove, square.getRank());
    return calculate(file, rank);
  }

  public static boolean calculateHasBehindSquare(Side havingMove, Square square) {
    return Rank.calculateHasPreviousRank(havingMove, square.getRank());
  }

  public static Square calculateBehindSquare(Side havingMove, Square square) {

    if (!calculateHasBehindSquare(havingMove, square)) {
      throw new IllegalArgumentException();
    }

    final File file = square.getFile();
    final Rank rank = Rank.calculatePreviousRank(havingMove, square.getRank());
    return calculate(file, rank);
  }

  public static List<Square> calculateEnPassantCaptureTargetSquareList(Side havingMove) {
    return switch (havingMove) {
      case BLACK -> BLACK_MOVE_EN_PASSANT_CAPTURE_TARGET_SQUARE_LIST;
      case WHITE -> WHITE_MOVE_EN_PASSANT_CAPTURE_TARGET_SQUARE_LIST;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static final ImmutableList<ImmutableList<Square>> WHITE_PAWN_TWO_SQUARE_ADVANCE;

  static {
    {
      final List<ImmutableList<Square>> listList = new ArrayList<>();

      listList.add(constructListSquare(A2, A4));
      listList.add(constructListSquare(B2, B4));
      listList.add(constructListSquare(C2, C4));
      listList.add(constructListSquare(D2, D4));
      listList.add(constructListSquare(E2, E4));
      listList.add(constructListSquare(F2, F4));
      listList.add(constructListSquare(G2, G4));
      listList.add(constructListSquare(H2, H4));

      WHITE_PAWN_TWO_SQUARE_ADVANCE = NonNullWrapperCommon.copyOfList(listList);
    }
  }

  public static final ImmutableList<ImmutableList<Square>> BLACK_PAWN_TWO_SQUARE_ADVANCE;

  static {
    {
      final List<ImmutableList<Square>> listList = new ArrayList<>();

      listList.add(constructListSquare(A7, A5));
      listList.add(constructListSquare(B7, B5));
      listList.add(constructListSquare(C7, C5));
      listList.add(constructListSquare(D7, D5));
      listList.add(constructListSquare(E7, E5));
      listList.add(constructListSquare(F7, F5));
      listList.add(constructListSquare(G7, G5));
      listList.add(constructListSquare(H7, H5));

      BLACK_PAWN_TWO_SQUARE_ADVANCE = NonNullWrapperCommon.copyOfList(listList);
    }

  }

  public static boolean calculateIsDisjoint(Set<Square> setOne, Set<Square> setTwo) {
    for (final Square squareSetOne : setOne) {
      if (setTwo.contains(squareSetOne)) {
        return false;
      }
    }
    for (final Square squareSetTwo : setTwo) {
      if (setOne.contains(squareSetTwo)) {
        return false;
      }
    }
    return true;
  }

  private void check() {
    if (this == NONE) {
      throw new NonePointerException();
    }
  }

  // TODO unwinnability - test and make static
  public static Square flip(Square square) {
    if (square == NONE) {
      throw new NonePointerException();
    }
    return calculate(9 - square.getFile().getNumber(), 9 - square.getRank().getNumber());
  }

}
