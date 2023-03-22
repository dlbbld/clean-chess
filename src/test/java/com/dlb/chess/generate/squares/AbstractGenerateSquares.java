package com.dlb.chess.generate.squares;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.utility.BasicUtility;

public abstract class AbstractGenerateSquares implements EnumConstants {

  static void generateKnightKingPawnCode(Side side, PieceType pieceType, GeneratePawnMoveType pawnMoveType,
      int expectedSize) {

    final String constantNamePart = calculateConstantName(side, pieceType);

    final var mapConstantName = constantNamePart + "_SQUARES";
    final var mapConstructName = constantNamePart.toLowerCase() + "Map";

    System.out.println(AbstractGenerateSquares.BEGIN_GENERATED_CODE);
    System.out.println();
    System.out.println("private static final ImmutableMap<Square, Set<Square>> " + mapConstantName + ";");
    System.out.println();
    System.out.println("static {");
    System.out.println("final  EnumMap<Square, Set<Square>> " + mapConstructName
        + " = NonNullWrapperCommon.newEnumMap(Square.class);");
    for (final Square fromSquare : Square.BOARD_SQUARE_LIST) {
      System.out.println();
      System.out.println("{");

      final Set<Square> squareSet = calculateSquareSet(side, fromSquare, pieceType, pawnMoveType);

      final String squareLiteralList = calculateSquareLiteralList(squareSet);

      final String setConstructionValue;
      if (squareSet.isEmpty()) {
        setConstructionValue = "EMPTY_UNMODIFIABLE_SET";
      } else {
        setConstructionValue = "NonNullWrapperCommon.immutableEnumMap(new TreeSet<>(Arrays.asList(" + squareLiteralList
            + ")))";
      }

      final var setConstruction = "final Set<Square> squareSet = " + setConstructionValue + ";";

      System.out.println(setConstruction);

      System.out.println(mapConstructName + ".put(" + calculateSquareLiteral(fromSquare) + ", squareSet);");
      System.out.println("}");
    }
    System.out.println();
    System.out.println(mapConstantName + " = NonNullWrapperCommon.copyOf(" + mapConstructName + ");");
    System.out.println();

    System.out.println("ValidateMoveNumberUtility.validateMapOfSet(" + mapConstantName + ", " + expectedSize + ");");
    System.out.println();
    System.out.println("}");

    System.out.println(AbstractGenerateSquares.END_GENERATED_CODE);
    System.out.println();
  }

  private static String calculateConstantName(Side side, PieceType pieceType) {
    @SuppressWarnings("null") @NonNull final String name = pieceType.name();
    return switch (side) {
      case WHITE, BLACK -> name + "_" + side.name();
      case NONE -> name;
      default -> throw new IllegalArgumentException();
    };
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
