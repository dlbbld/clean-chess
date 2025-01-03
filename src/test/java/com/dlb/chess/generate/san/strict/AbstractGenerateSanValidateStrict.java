package com.dlb.chess.generate.san.strict;

import static com.dlb.chess.common.utility.ImmutableUtility.constructListSquare;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.utility.DiagonalLineUtility;
import com.dlb.chess.model.EmptyBoardMove;
import com.google.common.collect.ImmutableList;

public abstract class AbstractGenerateSanValidateStrict implements EnumConstants {

  static final ImmutableList<Square> FIRST_QUADRAT_EDGE_SQUARES = constructListSquare(A1, H1, H8, A8);

  static final ImmutableList<Square> FIRST_AND_EIGHT_RANK_SQUARES = constructListSquare(A1, B1, C1, D1, E1, F1, G1, H1,
      A8, B8, C8, D8, E8, F8, G8, H8);

  static final ImmutableList<Square> FIRST_QUADRAT_WITHOUT_OUTER_FILES_SQUARES = constructListSquare(B1, C1, D1, E1, F1,
      G1, G8, F8, E8, D8, C8, B8);

  static final ImmutableList<Square> FIRST_QUADRAT_WITHOUT_OUTER_RANKS_SQUARES = constructListSquare(A2, A3, A4, A5, A6,
      A7, H2, H3, H4, H5, H6, H7);

  static final ImmutableList<Square> THIRD_QUADRAT_SQUARES = constructListSquare(C3, D3, E3, F3, C4, D4, E4, F4, C5, D5,
      E5, F5, C6, D6, E6, F6);

  static final ImmutableList<Square> FOURTH_QUADRAT_SQUARES = constructListSquare(D4, E4, D5, E5);

  abstract PieceType getPieceType();

  static void printEnumConstantList(Set<String> enumConstantSet) {
    for (final String enumConstant : enumConstantSet) {
      System.out.println(enumConstant + ",");
    }
  }

  void appendOnlyMove(Set<String> set, Square toSquare) {
    set.add(calculateOnlyMove(toSquare));
  }

  void appendMoveWithFromSquare(Set<String> set, Square toSquare, Square fromSquare) {
    set.add(calculateMoveWithFromSquare(toSquare, fromSquare));
  }

  void appendMoveWithFromSquare(Set<String> set, Square toSquare, Square... fromSquareArray) {
    set.addAll(calculateMoveWithFromSquare(toSquare, fromSquareArray));
  }

  void appendMoveWithFile(Set<String> set, Square toSquare, File file) {
    set.add(calculateMoveWithFile(toSquare, file));
  }

  void appendMoveWithFile(Set<String> set, Square toSquare, File... fileArray) {
    set.addAll(calculateMoveWithFile(toSquare, fileArray));
  }

  void appendMoveWithAllFiles(Set<String> set, Square toSquare) {
    set.addAll(calculateMoveWithAllFiles(toSquare));
  }

  void appendMoveWithRank(Set<String> set, Square toSquare, Rank rank) {
    set.add(calculateMoveWithRank(toSquare, rank));
  }

  void appendMoveWithRank(Set<String> set, Square toSquare, Rank... rankArray) {
    set.addAll(calculateMoveWithRank(toSquare, rankArray));
  }

  void appendMoveWithAllRanksExceptToSquareRank(Set<String> set, Square toSquare) {
    set.addAll(calculateMoveWithAllRanksExceptToSquareRank(toSquare));
  }

  private String calculateOnlyMove(Square toSquare) {
    return getPieceType().getLetter() + toSquare.getName().toUpperCase();
  }

  private String calculateMoveWithFromSquare(Square toSquare, Square fromSquare) {
    return getPieceType().getLetter() + fromSquare.getName().toUpperCase() + toSquare.getName().toUpperCase();
  }

  private Set<String> calculateMoveWithFromSquare(Square toSquare, Square... fromSquareArray) {
    final Set<String> enumConstantSet = new TreeSet<>();
    for (final Square fromSquare : fromSquareArray) {
      if (fromSquare == null) {
        throw new ProgrammingMistakeException("The from square cannot be null");
      }
      enumConstantSet.add(calculateMoveWithFromSquare(toSquare, fromSquare));
    }
    return enumConstantSet;
  }

  private String calculateMoveWithFile(Square toSquare, File file) {
    final var fileLetterUpperCase = NonNullWrapperCommon.toUpperCase(file.getLetter());
    final var piecePlusFile = getPieceType().getLetter() + fileLetterUpperCase;
    return piecePlusFile + toSquare.getName().toUpperCase();
  }

  private Set<String> calculateMoveWithFile(Square toSquare, File... fileArray) {
    final Set<String> enumConstantSet = new TreeSet<>();
    for (final File file : fileArray) {
      if (file == null) {
        throw new ProgrammingMistakeException("The file cannot be null");
      }
      enumConstantSet.add(calculateMoveWithFile(toSquare, file));
    }
    return enumConstantSet;
  }

  private Set<String> calculateMoveWithAllFiles(Square toSquare) {
    final Set<String> enumConstantSet = new TreeSet<>();
    for (final File file : File.values()) {
      if (file != FILE_NONE) {
        enumConstantSet.add(calculateMoveWithFile(toSquare, file));
      }
    }
    return enumConstantSet;
  }

  private String calculateMoveWithRank(Square toSquare, Rank rank) {
    final var piecePlusRank = getPieceType().getLetter() + rank.getNumber();
    return piecePlusRank + toSquare.getName().toUpperCase();
  }

  private Set<String> calculateMoveWithRank(Square toSquare, Rank... rankArray) {
    final Set<String> enumConstantSet = new TreeSet<>();
    for (final Rank rank : rankArray) {
      final var piecePlusRank = getPieceType().getLetter() + rank.getNumber();
      enumConstantSet.add(piecePlusRank + toSquare.getName().toUpperCase());
    }
    return enumConstantSet;
  }

  private Set<String> calculateMoveWithAllRanksExceptToSquareRank(Square toSquare) {
    final Set<String> enumConstantSet = new TreeSet<>();
    for (final Rank rank : Rank.values()) {
      if (rank != RANK_NONE && rank != toSquare.getRank()) {
        enumConstantSet.add(calculateMoveWithRank(toSquare, rank));
      }
    }
    return enumConstantSet;
  }

  void generateSan() {
    final Set<String> handwovenSet = calculateEnumConstantHandwoven();
    final Set<String> formalSet = calculateEnumConstantFormal();
    if (!handwovenSet.equals(formalSet)) {
      System.out.println("ERROR - the handwoven and formal approach differ");

      // System.out.println("handwovenSet:");
      // printEnumConstantList(handwovenSet);
      //
      // System.out.println("formalSet:");
      // printEnumConstantList(formalSet);

      System.out.println("handwovenSet minus formalSet:");
      final Set<String> handwovenSetMinusFormalSet = new TreeSet<>(handwovenSet);
      handwovenSetMinusFormalSet.removeAll(formalSet);
      printEnumConstantList(handwovenSetMinusFormalSet);

      System.out.println("formalSet minus handwovenSet:");
      final Set<String> formalSetMinusHandwovenSet = new TreeSet<>(formalSet);
      formalSetMinusHandwovenSet.removeAll(handwovenSet);
      printEnumConstantList(formalSetMinusHandwovenSet);

      throw new ProgrammingMistakeException(
          "There is a problem in my generation assumption or the generation using calculation only");
    }

    printEnumConstantList(handwovenSet);
  }

  abstract Set<String> calculateEnumConstantHandwoven();

  abstract Set<String> calculateEnumConstantFormal();

  static List<Square> calculateFromSquareList(Set<EmptyBoardMove> emptyBoardMoveSet) {
    final List<Square> fromSquareList = new ArrayList<>();
    for (final EmptyBoardMove emptyBoardMove : emptyBoardMoveSet) {
      fromSquareList.add(emptyBoardMove.fromSquare());
    }
    return fromSquareList;
  }

  static boolean calculateIsFromRankPossibleOrthogonal(Square fromSquare, Square toSquare,
      List<Square> fromSquareList) {
    for (final Square otherFromSquare : fromSquareList) {
      if (otherFromSquare.getFile() == fromSquare.getFile() && otherFromSquare.getRank() != fromSquare.getRank()
          && calculateIsOppositeVertical(fromSquare, toSquare, otherFromSquare)) {
        return true;
      }
    }
    return false;
  }

  static boolean calculateHasOtherMovesFromSameRank(Square fromSquare, List<Square> fromSquareList) {
    for (final Square otherFromSquare : fromSquareList) {
      if (otherFromSquare != fromSquare && otherFromSquare.getRank() == fromSquare.getRank()) {
        return true;
      }
    }
    return false;
  }

  static boolean calculateIsOppositeVertical(Square fromSquare, Square toSquare, Square otherFromSquare) {
    if (fromSquare.getFile() != toSquare.getFile() || otherFromSquare.getFile() != toSquare.getFile()) {
      return false;
    }

    // all three squares on same file
    if (fromSquare.getRank().getNumber() > toSquare.getRank().getNumber()) {
      // from square above
      return otherFromSquare.getRank().getNumber() < toSquare.getRank().getNumber();

    }
    if (fromSquare.getRank().getNumber() < toSquare.getRank().getNumber()) {
      // from square below
      return otherFromSquare.getRank().getNumber() > toSquare.getRank().getNumber();

    }
    throw new ProgrammingMistakeException("Unexpected program flow - at this point the rank numbers must be different");
  }

  static boolean calculateIsFromFilePossibleOrthogonal(Square fromSquare, Square toSquare,
      List<Square> fromSquareList) {
    for (final Square otherFromSquare : fromSquareList) {
      if (otherFromSquare.getFile() != fromSquare.getFile()
          && calculateIsFromFilePossibleOrthogonal(fromSquare, toSquare, otherFromSquare)) {
        return true;
      }
    }
    return false;
  }

  private static boolean calculateIsFromFilePossibleOrthogonal(Square fromSquare, Square toSquare,
      Square otherFromSquare) {
    if (fromSquare.getRank() != toSquare.getRank() || toSquare.getRank() != otherFromSquare.getRank()) {
      return true;
    }

    // all three squares on same rank
    // we check that the two pieces are opposite, then specifying the file can be necessary
    // from square to the left
    if (fromSquare.getFile().getNumber() < toSquare.getFile().getNumber()) {
      return otherFromSquare.getFile().getNumber() > toSquare.getFile().getNumber();
    }

    // from square to the right
    if (fromSquare.getFile().getNumber() > toSquare.getFile().getNumber()) {
      return otherFromSquare.getFile().getNumber() < toSquare.getFile().getNumber();
    }
    return false;
  }

  static boolean calculateIsFromFilePossibleDiagonal(Square fromSquare, Square toSquare, List<Square> fromSquareList) {
    for (final Square otherFromSquare : fromSquareList) {
      if (otherFromSquare.getFile() != fromSquare.getFile()
          && calculateIsFromFilePossibleDiagonal(fromSquare, toSquare, otherFromSquare)) {
        return true;
      }
    }
    return false;
  }

  private static boolean calculateIsFromFilePossibleDiagonal(Square fromSquare, Square toSquare,
      Square otherFromSquare) {
    // if they are not on the same diagonal then specifying the file can be necessary
    if (!DiagonalLineUtility.calculateIsOnDiagonalLine(fromSquare, toSquare, otherFromSquare)) {
      return true;
    }

    // if all squares on the same diagonal, the two pieces must be opposite to the target square, for specifying the
    // file can be necessary
    return calculateIsOppositeDiagonal(fromSquare, toSquare, otherFromSquare);
  }

  private static boolean calculateIsOppositeDiagonal(Square fromSquare, Square toSquare, Square otherFromSquare) {
    if (!DiagonalLineUtility.calculateIsOnDiagonalLine(fromSquare, toSquare, otherFromSquare)) {
      throw new ProgrammingMistakeException(
          "The program is not expected to call this method when the squares are not on the same diagonal");
    }

    // all three squares on same file
    if (fromSquare.getRank().getNumber() < toSquare.getRank().getNumber()) {
      // from square to the left of to square
      return otherFromSquare.getRank().getNumber() > toSquare.getRank().getNumber();

    }
    if (fromSquare.getRank().getNumber() > toSquare.getRank().getNumber()) {
      // from square to the right of to square
      return otherFromSquare.getRank().getNumber() < toSquare.getRank().getNumber();

    }
    throw new ProgrammingMistakeException("Unexpected program flow - at this point the rank numbers must be different");
  }
}
