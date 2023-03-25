package com.dlb.chess.generate.squares;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.utility.BasicUtility;

public abstract class AbstractGenerateSquares implements EnumConstants {

  static void generateKnightKingPawnCode(Side side, PieceType pieceType, GeneratePawnMoveType pawnMoveType,
      int expectedSize) {

    System.out.println(AbstractGenerateSquares.BEGIN_GENERATED_CODE);

    // generate static methods
    for (final Square fromSquare : Square.BOARD_SQUARE_LIST) {
      System.out.println();

      final var methodName = generateMethodName(side, fromSquare);

      System.out.println("private static void " + methodName + "(EnumMap<Square, ImmutableSet<Square>> map) {");

      final Set<Square> squareSet = calculateSquareSet(side, fromSquare, pieceType, pawnMoveType);

      final String squareLiteralList = calculateSquareLiteralList(squareSet);

      final String setConstructionValue;
      if (squareSet.isEmpty()) {
        setConstructionValue = "ImmutableUtility.EMPTY_UNMODIFIABLE_SET";
      } else {
        setConstructionValue = "constructSet(" + squareLiteralList + ")";
      }

      final var setConstruction = "final ImmutableSet<Square> squareSet = " + setConstructionValue + ";";

      System.out.println(setConstruction);

      System.out.println("map.put(" + calculateSquareLiteral(fromSquare) + ", squareSet);");
      System.out.println("}");
    }

    final List<String> namePartList = calculateNamePartList(side, pieceType);

    final String constantName = calculateConstantName(namePartList);

    final String variableName = calculateVariableName(namePartList);

    System.out.println();
    System.out.println("private static final ImmutableMap<Square, ImmutableSet<Square>> " + constantName + ";");
    System.out.println();
    System.out.println("static {");
    System.out.println("final  EnumMap<Square, ImmutableSet<Square>> " + variableName
        + " = NonNullWrapperCommon.newEnumMap(Square.class);");

    // call the generated static methods
    for (final Square fromSquare : Square.BOARD_SQUARE_LIST) {
      final var methodName = generateMethodName(side, fromSquare);
      System.out.println(methodName + "(" + variableName + ");");
    }
    System.out.println();
    System.out.println(constantName + " = NonNullWrapperCommon.copyOfMap(" + variableName + ");");
    System.out.println();

    System.out.println("ValidateMoveNumberUtility.validateMapOfSet(" + constantName + ", " + expectedSize + ");");
    System.out.println();
    System.out.println("}");

    System.out.println(AbstractGenerateSquares.END_GENERATED_CODE);
  }

  static String generateMethodName(Side side, Square fromSquare) {
    return "add" + calculateSidePart(side) + StringUtils.capitalize(fromSquare.getName());
  }

  private static String calculateSidePart(Side side) {
    return switch (side) {
      case WHITE, BLACK -> NonNullWrapperCommon.capitalize(side.getName());
      case NONE -> "";
      default -> throw new IllegalArgumentException();
    };
  }

  static List<String> calculateNamePartList(Side side, PieceType pieceType) {

    final List<String> list = new ArrayList<>();

    list.add(pieceType.getName());
    switch (side) {
      case WHITE, BLACK:
        list.add(side.getName());
        break;
      case NONE:
        break;
      default:
        throw new IllegalArgumentException();
    }
    list.add("Squares");
    list.add("Map");

    return list;
  }

  static String calculateVariableName(List<String> list) {

    final StringBuilder result = new StringBuilder();
    var isFirstEntry = true;
    for (final String item : list) {
      if (isFirstEntry) {
        result.append(StringUtils.uncapitalize(item));
      } else {
        result.append(StringUtils.capitalize(item));
      }
      isFirstEntry = false;
    }

    return NonNullWrapperCommon.toString(result);
  }

  static String calculateConstantName(List<String> list) {

    final StringBuilder result = new StringBuilder();
    for (var i = 0; i < list.size(); i++) {
      final String item = NonNullWrapperCommon.get(list, i);
      result.append(NonNullWrapperCommon.toUpperCase(item));
      if (i != list.size() - 1) {
        result.append("_");
      }
    }

    return NonNullWrapperCommon.toString(result);
  }

  private static Set<Square> calculateSquareSet(Side side, Square fromSquare, PieceType pieceType,
      GeneratePawnMoveType pawnMoveType) {
    switch (pieceType) {
      case BISHOP:
      case QUEEN:
      case ROOK:
        throw new IllegalArgumentException();
      case KNIGHT:
        return GenerateEmptyBoardSquares.calculateKnightSquares(fromSquare);
      case KING:
        return GenerateEmptyBoardSquares.calculateKingNonCastlingSquares(fromSquare);
      case PAWN:
        if (pawnMoveType == GeneratePawnMoveType.DIAGONAL) {
          return GeneratePawnDiagonalSquares.calculatePawnDiagonalBoardSquares(side, fromSquare);
        }
        return GenerateEmptyBoardSquares.calculatePawnEmptyBoardSquares(side, fromSquare, pawnMoveType);
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  static String calculateSquareLiteralList(Collection<Square> squareList) {
    final List<String> literalList = new ArrayList<>();
    for (final Square squareDestination : squareList) {
      literalList.add(calculateSquareLiteral(squareDestination));
    }
    return BasicUtility.calculateCommaSeparatedList(literalList);
  }

  @SuppressWarnings("null")
  static String calculateSquareLiteral(Square square) {
    return square.name();
  }

  static void processNonEmpty(List<Square> resultList, File destinationFile, Rank destinationRank) {
    final Square toSquare = Square.calculate(destinationFile, destinationRank);
    resultList.add(toSquare);
  }

  static void processNonEmpty(Set<Square> resultSet, File destinationFile, Rank destinationRank) {
    final Square toSquare = Square.calculate(destinationFile, destinationRank);
    resultSet.add(toSquare);
  }

  public static final String BEGIN_GENERATED_CODE = "\n  // BEGIN generated code";
  public static final String END_GENERATED_CODE = "\n  // END generated code";

}
