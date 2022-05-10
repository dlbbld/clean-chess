package com.dlb.chess.generate.squares;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.range.DiagonalRange;
import com.dlb.chess.range.OrthogonalRange;
import com.dlb.chess.range.model.BishopRange;
import com.dlb.chess.range.model.QueenRange;
import com.dlb.chess.range.model.RookRange;
import com.google.common.collect.ImmutableList;

//TODO update code generator
public class GenerateEmptyBoardSquares extends AbstractGenerateSquares {

  public static void main(String[] args) {
    generatePawnOneSquareAdvanceCode();

    // condition only to not have warnings on unused methoeds
    if (args.length > 0) {
      generateBoardSquareListCode();

      generateRookCode();
      generateKnightCode();
      generateBishopCode();
      generateQueenCode();
      generateKingNonCastlingCode();
      generatePawnAnyAdvanceCode();
      generatePawnOneSquareAdvanceCode();
      generatePawnTwoSquareAdvanceCode();
    }
  }

  private static void generateBoardSquareListCode() {
    final List<Square> allButEmpty = new ArrayList<>();
    for (final Square fromSquare : Square.values()) {
      if (fromSquare != Square.NONE) {
        allButEmpty.add(fromSquare);
      }
    }
    System.out.println(calculateSquareLiteralList(allButEmpty));
  }

  private static void generateRookBishopQueenCode(PieceType pieceType, int expectedSize) {

    final var pieceName = pieceType.getName();

    final List<String> variableNames = calculateVariableNames(pieceType);

    final var mapConstantName = NonNullWrapperCommon.toUpperCase(pieceName) + "_SQUARES";
    final var mapConstructName = pieceName + "SquaresMap";

    System.out.println(AbstractGenerateSquares.BEGIN_GENERATED_CODE);
    System.out.println();
    System.out.println("private static final ImmutableMap<Square, List<List<Square>>> " + mapConstantName + ";");
    System.out.println();
    System.out.println("static {");
    System.out.println("final EnumMap<Square, List<List<Square>>> " + mapConstructName
        + " = NonNullWrapperCommon.newEnumMap(Square.class);\r\n" + "");
    for (final Square fromSquare : Square.BOARD_SQUARE_LIST) {
      System.out.println();
      System.out.println("{");

      // TODO outdated after introducing range model
      final List<List<Square>> squareListList = new ArrayList<>();

      if (squareListList.size() != variableNames.size()) {
        throw new ProgrammingMistakeException("I must have been sleeping when programming");
      }

      for (var i = 0; i < squareListList.size(); i++) {
        final List<Square> squareList = NonNullWrapperCommon.get(squareListList, i);
        final String squareLiteralList = calculateSquareLiteralList(squareList);

        final var destinationList = "final List<Square> " + variableNames.get(i)
            + " = Collections.unmodifiableList(Arrays.asList(" + squareLiteralList + "));";
        System.out.println(destinationList);
      }

      final String variableNameList = BasicUtility.calculateCommaSeparatedList(variableNames);

      final var destinationListList = "final List<List<Square>> toSquareListList = Collections.unmodifiableList(Arrays.asList("
          + variableNameList + "));";
      System.out.println(destinationListList);
      System.out.println(mapConstructName + ".put(" + calculateSquareLiteral(fromSquare) + ", toSquareListList);");
      System.out.println("}");
    }
    System.out.println();
    System.out.println(mapConstantName + " = NonNullWrapperCommon.immutableEnumMap(" + mapConstructName + ");");
    System.out.println();
    System.out
        .println("ValidateMoveNumberUtility.validateMapOfListList(" + mapConstantName + ", " + expectedSize + ");");
    System.out.println();
    System.out.println("}");
    System.out.println(AbstractGenerateSquares.END_GENERATED_CODE);
    System.out.println();
  }

  private static List<String> calculateVariableNames(PieceType pieceType) {

    @SuppressWarnings("null") @NonNull final List<String> variableNamesOrthogonal = Arrays.asList("squareListNorth",
        "squareListEast", "squareListSouth", "squareListWest");

    @SuppressWarnings("null") @NonNull final List<String> variableNamesDiagonal = Arrays.asList("squareListNorthEast",
        "squareListSouthEast", "squareListSouthWest", "squareListNorthWest");

    final List<String> variableNamesBoth = new ArrayList<>(variableNamesOrthogonal);
    variableNamesBoth.addAll(variableNamesDiagonal);

    switch (pieceType) {
      case BISHOP:
        return variableNamesDiagonal;
      case QUEEN:
        return variableNamesBoth;
      case ROOK:
        return variableNamesOrthogonal;
      case KNIGHT:
      case KING:
      case PAWN:
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  private static void generateRookCode() {
    generateRookBishopQueenCode(ROOK, 896);
  }

  private static void generateKnightCode() {
    generateKnightKingPawnCode(Side.NONE, KNIGHT, GeneratePawnMoveType.NONE, 336);
  }

  private static void generateBishopCode() {
    generateRookBishopQueenCode(BISHOP, 560);
  }

  private static void generateQueenCode() {
    generateRookBishopQueenCode(QUEEN, 1456);
  }

  private static void generateKingNonCastlingCode() {
    generateKnightKingPawnCode(Side.NONE, KING, GeneratePawnMoveType.NONE, 420);
  }

  private static void generatePawnAnyAdvanceCode() {
    generateKnightKingPawnCode(WHITE, PAWN, GeneratePawnMoveType.ANY_ADVANCE, 56);
    generateKnightKingPawnCode(BLACK, PAWN, GeneratePawnMoveType.ANY_ADVANCE, 56);
  }

  private static void generatePawnOneSquareAdvanceCode() {
    generateKnightKingPawnCode(WHITE, PAWN, GeneratePawnMoveType.ONE_SQUARE_ADVANCE, 48);
    generateKnightKingPawnCode(BLACK, PAWN, GeneratePawnMoveType.ONE_SQUARE_ADVANCE, 48);
  }

  private static void generatePawnTwoSquareAdvanceCode() {
    generateKnightKingPawnCode(WHITE, PAWN, GeneratePawnMoveType.TWO_SQUARE_ADVANCE, 8);
    generateKnightKingPawnCode(BLACK, PAWN, GeneratePawnMoveType.TWO_SQUARE_ADVANCE, 8);
  }

  public static OrthogonalRange calculateRookSquares(Square fromSquare) {
    return calculateOrthogonalSquares(fromSquare);
  }

  private static OrthogonalRange calculateOrthogonalSquares(Square fromSquare) {
    if (fromSquare == Square.NONE) {
      throw new IllegalArgumentException("The empty square is not supported");
    }

    // we calculate directions in order north, east, south and west

    // north
    final ImmutableList<Square> squareListNorth;
    {
      final List<Square> resultList = new ArrayList<>();

      Rank rank = fromSquare.getRank();
      final File fixedFile = fromSquare.getFile();
      while (Rank.calculateHasNextRank(WHITE, rank)) {
        rank = Rank.calculateNextRank(WHITE, rank);
        processNonEmpty(resultList, fixedFile, rank);
      }
      squareListNorth = NonNullWrapperCommon.copyOfList(resultList);
    }

    // east
    final ImmutableList<Square> squareListEast;
    {
      final List<Square> resultList = new ArrayList<>();

      File file = fromSquare.getFile();
      final Rank fixedRank = fromSquare.getRank();
      while (File.calculateHasRightFile(WHITE, file)) {
        file = File.calculateRightFile(WHITE, file);
        processNonEmpty(resultList, file, fixedRank);
      }
      squareListEast = NonNullWrapperCommon.copyOfList(resultList);
    }

    // south
    final ImmutableList<Square> squareListSouth;
    {
      final List<Square> resultList = new ArrayList<>();

      Rank rank = fromSquare.getRank();
      final File fixedFile = fromSquare.getFile();
      while (Rank.calculateHasPreviousRank(WHITE, rank)) {
        rank = Rank.calculatePreviousRank(WHITE, rank);
        processNonEmpty(resultList, fixedFile, rank);
      }
      squareListSouth = NonNullWrapperCommon.copyOfList(resultList);
    }

    // west
    final ImmutableList<Square> squareListWest;
    {
      final List<Square> resultList = new ArrayList<>();

      File file = fromSquare.getFile();

      final Rank fixedRank = fromSquare.getRank();
      while (File.calculateHasLeftFile(WHITE, file)) {
        file = File.calculateLeftFile(WHITE, file);
        processNonEmpty(resultList, file, fixedRank);
      }
      squareListWest = NonNullWrapperCommon.copyOfList(resultList);
    }

    return new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
  }

  public static Set<Square> calculateKnightSquares(Square fromSquare) {
    if (fromSquare == Square.NONE) {
      throw new IllegalArgumentException("The empty square is not supported");
    }

    final TreeSet<Square> resultSet = new TreeSet<>();

    final var sourceFile = fromSquare.getFile();
    final var sourceRank = fromSquare.getRank();

    // we calculate starting from right one and up two, and then clockwise

    // first
    {
      final File candidateFile;
      if (File.calculateHasRightFile(WHITE, sourceFile)) {
        candidateFile = File.calculateRightFile(WHITE, sourceFile);
      } else {
        candidateFile = FILE_NONE;
      }

      var candidateRank = RANK_NONE;
      if (Rank.calculateHasNextNextRank(WHITE, sourceRank)) {
        candidateRank = Rank.calculateNextNextRank(WHITE, sourceRank);
      } else {
        candidateRank = RANK_NONE;
      }

      process(resultSet, candidateFile, candidateRank);
    }

    // second
    {
      final File candidateFile;
      if (File.calculateHasRightRightFile(WHITE, sourceFile)) {
        candidateFile = File.calculateRightRightFile(WHITE, sourceFile);
      } else {
        candidateFile = FILE_NONE;
      }

      var candidateRank = RANK_NONE;
      if (Rank.calculateHasNextRank(WHITE, sourceRank)) {
        candidateRank = Rank.calculateNextRank(WHITE, sourceRank);
      } else {
        candidateRank = RANK_NONE;
      }

      process(resultSet, candidateFile, candidateRank);
    }

    // third
    {
      final File candidateFile;
      if (File.calculateHasRightRightFile(WHITE, sourceFile)) {
        candidateFile = File.calculateRightRightFile(WHITE, sourceFile);
      } else {
        candidateFile = FILE_NONE;
      }

      var candidateRank = RANK_NONE;
      if (Rank.calculateHasPreviousRank(WHITE, sourceRank)) {
        candidateRank = Rank.calculatePreviousRank(WHITE, sourceRank);
      } else {
        candidateRank = RANK_NONE;
      }

      process(resultSet, candidateFile, candidateRank);
    }

    // fourth
    {
      final File candidateFile;
      if (File.calculateHasRightFile(WHITE, sourceFile)) {
        candidateFile = File.calculateRightFile(WHITE, sourceFile);
      } else {
        candidateFile = FILE_NONE;
      }

      var candidateRank = RANK_NONE;
      if (Rank.calculateHasPreviousPreviousRank(WHITE, sourceRank)) {
        candidateRank = Rank.calculatePreviousPreviousRank(WHITE, sourceRank);
      } else {
        candidateRank = RANK_NONE;
      }

      process(resultSet, candidateFile, candidateRank);
    }

    // fifth
    {
      final File candidateFile;
      if (File.calculateHasLeftFile(WHITE, sourceFile)) {
        candidateFile = File.calculateLeftFile(WHITE, sourceFile);
      } else {
        candidateFile = FILE_NONE;
      }

      var candidateRank = RANK_NONE;
      if (Rank.calculateHasPreviousPreviousRank(WHITE, sourceRank)) {
        candidateRank = Rank.calculatePreviousPreviousRank(WHITE, sourceRank);
      } else {
        candidateRank = RANK_NONE;
      }

      process(resultSet, candidateFile, candidateRank);
    }

    // sixth
    {
      final File candidateFile;
      if (File.calculateHasLeftLeftFile(WHITE, sourceFile)) {
        candidateFile = File.calculateLeftLeftFile(WHITE, sourceFile);
      } else {
        candidateFile = FILE_NONE;
      }

      var candidateRank = RANK_NONE;
      if (Rank.calculateHasPreviousRank(WHITE, sourceRank)) {
        candidateRank = Rank.calculatePreviousRank(WHITE, sourceRank);
      } else {
        candidateRank = RANK_NONE;
      }

      process(resultSet, candidateFile, candidateRank);
    }

    // seventh
    {
      final File candidateFile;
      if (File.calculateHasLeftLeftFile(WHITE, sourceFile)) {
        candidateFile = File.calculateLeftLeftFile(WHITE, sourceFile);
      } else {
        candidateFile = FILE_NONE;
      }

      var candidateRank = RANK_NONE;
      if (Rank.calculateHasNextRank(WHITE, sourceRank)) {
        candidateRank = Rank.calculateNextRank(WHITE, sourceRank);
      } else {
        candidateRank = RANK_NONE;
      }

      process(resultSet, candidateFile, candidateRank);
    }

    // eight
    {
      final File candidateFile;
      if (File.calculateHasLeftFile(WHITE, sourceFile)) {
        candidateFile = File.calculateLeftFile(WHITE, sourceFile);
      } else {
        candidateFile = FILE_NONE;
      }

      var candidateRank = RANK_NONE;
      if (Rank.calculateHasNextNextRank(WHITE, sourceRank)) {
        candidateRank = Rank.calculateNextNextRank(WHITE, sourceRank);
      } else {
        candidateRank = RANK_NONE;
      }

      process(resultSet, candidateFile, candidateRank);
    }

    return resultSet;
  }

  public static DiagonalRange calculateBishopSquares(Square fromSquare) {
    return calculateDiagonalSquares(fromSquare);
  }

  private static DiagonalRange calculateDiagonalSquares(Square fromSquare) {
    if (fromSquare == Square.NONE) {
      throw new IllegalArgumentException("The empty square is not supported");
    }

    // we calculate directions in order northeast, southeast, southwest and
    // northwest

    // northeast
    final ImmutableList<Square> squareListNorthEast;
    {
      final List<Square> resultList = new ArrayList<>();

      File file = fromSquare.getFile();
      Rank rank = fromSquare.getRank();

      while (File.calculateHasRightFile(WHITE, file) && Rank.calculateHasNextRank(WHITE, rank)) {
        file = File.calculateRightFile(WHITE, file);
        rank = Rank.calculateNextRank(WHITE, rank);
        processNonEmpty(resultList, file, rank);
      }
      squareListNorthEast = NonNullWrapperCommon.copyOfList(resultList);
    }

    // southeast
    final ImmutableList<Square> squareListSouthEast;
    {
      final List<Square> resultList = new ArrayList<>();

      File file = fromSquare.getFile();
      Rank rank = fromSquare.getRank();

      while (File.calculateHasRightFile(WHITE, file) && Rank.calculateHasPreviousRank(WHITE, rank)) {
        file = File.calculateRightFile(WHITE, file);
        rank = Rank.calculatePreviousRank(WHITE, rank);
        processNonEmpty(resultList, file, rank);
      }
      squareListSouthEast = NonNullWrapperCommon.copyOfList(resultList);
    }

    // southwest
    final ImmutableList<Square> squareListSouthWest;
    {
      final List<Square> resultList = new ArrayList<>();

      File file = fromSquare.getFile();
      Rank rank = fromSquare.getRank();

      while (File.calculateHasLeftFile(WHITE, file) && Rank.calculateHasPreviousRank(WHITE, rank)) {
        file = File.calculateLeftFile(WHITE, file);
        rank = Rank.calculatePreviousRank(WHITE, rank);
        processNonEmpty(resultList, file, rank);
      }
      squareListSouthWest = NonNullWrapperCommon.copyOfList(resultList);
    }

    // northwest
    final ImmutableList<Square> squareListNorthWest;
    {
      final List<Square> resultList = new ArrayList<>();

      File file = fromSquare.getFile();
      Rank rank = fromSquare.getRank();

      while (File.calculateHasLeftFile(WHITE, file) && Rank.calculateHasNextRank(WHITE, rank)) {
        file = File.calculateLeftFile(WHITE, file);
        rank = Rank.calculateNextRank(WHITE, rank);
        processNonEmpty(resultList, file, rank);
      }
      squareListNorthWest = NonNullWrapperCommon.copyOfList(resultList);
    }

    return new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
  }

  public static QueenRange calculateQueenSquares(Square fromSquare) {

    final OrthogonalRange orthogonalRange = calculateOrthogonalSquares(fromSquare);

    final DiagonalRange diagonalRange = calculateDiagonalSquares(fromSquare);

    return new QueenRange(orthogonalRange.squareListNorth(), orthogonalRange.squareListEast(),
        orthogonalRange.squareListSouth(), orthogonalRange.squareListWest(), diagonalRange.squareListNorthEast(),
        diagonalRange.squareListSouthEast(), diagonalRange.squareListSouthWest(), diagonalRange.squareListNorthWest());

  }

  public static Set<Square> calculateKingNonCastlingSquares(Square fromSquare) {
    if (fromSquare == Square.NONE) {
      throw new IllegalArgumentException("The empty square is not supported");
    }

    final Set<Square> resultSet = new TreeSet<>();

    // we start on same file and one rank higher, than clockwise for the eight
    // possible fields

    // first
    {
      final File fixedFile = fromSquare.getFile();
      if (Rank.calculateHasNextRank(WHITE, fromSquare.getRank())) {
        final Rank nextRank = Rank.calculateNextRank(WHITE, fromSquare.getRank());
        processNonEmpty(resultSet, fixedFile, nextRank);
      }
    }

    // second
    {
      if (File.calculateHasRightFile(WHITE, fromSquare.getFile())) {
        final File rightFile = File.calculateRightFile(WHITE, fromSquare.getFile());
        if (Rank.calculateHasNextRank(WHITE, fromSquare.getRank())) {
          final Rank nextRank = Rank.calculateNextRank(WHITE, fromSquare.getRank());
          processNonEmpty(resultSet, rightFile, nextRank);
        }
      }
    }

    // third
    {
      if (File.calculateHasRightFile(WHITE, fromSquare.getFile())) {
        final File rightFile = File.calculateRightFile(WHITE, fromSquare.getFile());
        final Rank fixedRank = fromSquare.getRank();
        processNonEmpty(resultSet, rightFile, fixedRank);
      }
    }

    // fourth
    {
      if (File.calculateHasRightFile(WHITE, fromSquare.getFile())) {
        final File rightFile = File.calculateRightFile(WHITE, fromSquare.getFile());
        if (Rank.calculateHasPreviousRank(WHITE, fromSquare.getRank())) {
          final Rank previousRank = Rank.calculatePreviousRank(WHITE, fromSquare.getRank());
          processNonEmpty(resultSet, rightFile, previousRank);
        }
      }
    }

    // fifth
    {
      final File fixedFile = fromSquare.getFile();
      if (Rank.calculateHasPreviousRank(WHITE, fromSquare.getRank())) {
        final Rank previousRank = Rank.calculatePreviousRank(WHITE, fromSquare.getRank());
        processNonEmpty(resultSet, fixedFile, previousRank);
      }
    }

    // sixth
    {
      if (File.calculateHasLeftFile(WHITE, fromSquare.getFile())) {
        final File leftFile = File.calculateLeftFile(WHITE, fromSquare.getFile());
        if (Rank.calculateHasPreviousRank(WHITE, fromSquare.getRank())) {
          final Rank previousRank = Rank.calculatePreviousRank(WHITE, fromSquare.getRank());
          processNonEmpty(resultSet, leftFile, previousRank);
        }
      }
    }

    // sixth
    {
      if (File.calculateHasLeftFile(WHITE, fromSquare.getFile())) {
        final File leftFile = File.calculateLeftFile(WHITE, fromSquare.getFile());
        final Rank fixedRank = fromSquare.getRank();
        processNonEmpty(resultSet, leftFile, fixedRank);
      }
    }

    // eight
    {
      if (File.calculateHasLeftFile(WHITE, fromSquare.getFile())) {
        final File leftFile = File.calculateLeftFile(WHITE, fromSquare.getFile());
        if (Rank.calculateHasNextRank(WHITE, fromSquare.getRank())) {
          final Rank nextRank = Rank.calculateNextRank(WHITE, fromSquare.getRank());
          processNonEmpty(resultSet, leftFile, nextRank);
        }
      }
    }

    return resultSet;

  }

  public static Set<Square> calculatePawnEmptyBoardSquares(Side havingMove, Square fromSquare,
      GeneratePawnMoveType pawnMoveType) {
    if (fromSquare == Square.NONE) {
      throw new IllegalArgumentException("The empty square is not supported");
    }

    final Set<Square> resultSet = new TreeSet<>();

    final Rank rank = fromSquare.getRank();
    if (Rank.calculateIsGroundRank(havingMove, rank) || Rank.calculateIsPromotionRank(havingMove, rank)) {
      // no moves possibles which we represent as empty list
      // needed later in legal move generation to find illegal moves
      return resultSet;
    }

    final Rank nextRank = Rank.calculateNextRank(havingMove, rank);
    // one ahead
    final File fixedFile = fromSquare.getFile();

    switch (pawnMoveType) {
      case ANY_ADVANCE:
        processNonEmpty(resultSet, fixedFile, nextRank);
        // two ahead
        if (Rank.calculateIsPawnInititalRank(havingMove, rank)) {
          final Rank nextNextRank = Rank.calculateNextRank(havingMove, nextRank);
          processNonEmpty(resultSet, fixedFile, nextNextRank);
        }
        break;
      case ONE_SQUARE_ADVANCE:
        processNonEmpty(resultSet, fixedFile, nextRank);
        break;
      case TWO_SQUARE_ADVANCE:
        // two ahead
        if (Rank.calculateIsPawnInititalRank(havingMove, rank)) {
          final Rank nextNextRank = Rank.calculateNextRank(havingMove, nextRank);
          processNonEmpty(resultSet, fixedFile, nextNextRank);
        }
        break;
      case DIAGONAL:
      case NONE:
      default:
        throw new IllegalArgumentException();
    }

    return resultSet;
  }

  private static void process(Set<Square> resultSet, File candidateFile, Rank candidateRank) {

    if (candidateFile != File.NONE && candidateRank != Rank.NONE) {
      final Square toSquare = Square.calculate(candidateFile, candidateRank);

      resultSet.add(toSquare);
    }
  }

}
