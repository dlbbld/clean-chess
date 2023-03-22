package com.dlb.chess.generate;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.model.EmptyBoardMove;
import com.dlb.chess.moves.possible.PawnDiagonalSquares;
import com.dlb.chess.squares.emptyboard.AbstractEmptyBoardSquares;

public class GenerateUciMove implements EnumConstants {

  public static void main(String[] args) {

    System.out.println("// Non promotion moves");
    for (final Square fromSquare : Square.BOARD_SQUARE_LIST) {

      // rook
      {
        final Set<EmptyBoardMove> moveSet = AbstractEmptyBoardSquares.calculateNonPawnEmptyBoardMoves(ROOK, fromSquare);
        printNonPromotionMoves(moveSet);
      }
      // bishop
      {
        final Set<EmptyBoardMove> moveSet = AbstractEmptyBoardSquares.calculateNonPawnEmptyBoardMoves(BISHOP,
            fromSquare);
        printNonPromotionMoves(moveSet);
      }
      // knight
      {
        final Set<EmptyBoardMove> moveSet = AbstractEmptyBoardSquares.calculateNonPawnEmptyBoardMoves(KNIGHT,
            fromSquare);
        printNonPromotionMoves(moveSet);
      }
    }

    printPawnPromotionMoves(Side.WHITE);
    printPawnPromotionMoves(Side.BLACK);

  }

  private static void printPawnPromotionMoves(Side side) {
    System.out.println("");
    System.out.println("// Promotion moves " + side.getName());

    for (final Square fromSquare : getRankBeforePromotionRank(side)) {

      final Set<Square> toSquareSet = new TreeSet<>();
      final Set<EmptyBoardMove> moveSet = AbstractEmptyBoardSquares.calculatePawnEmptyBoardMoves(side, fromSquare);
      for (final EmptyBoardMove move : moveSet) {
        toSquareSet.add(move.toSquare());
      }

      final Set<Square> diagonalSquareSet = PawnDiagonalSquares.getPawnDiagonalSquares(side, fromSquare);
      toSquareSet.addAll(diagonalSquareSet);

      printPromotionMoves(fromSquare, toSquareSet);
    }
  }

  private static void printNonPromotionMoves(Set<EmptyBoardMove> moveSet) {
    for (final EmptyBoardMove move : moveSet) {
      printMoveMinimum(move.fromSquare(), move.toSquare(), PromotionPieceType.NONE);
    }
  }

  private static void printPromotionMoves(Square fromSquare, Set<Square> toSquareSet) {
    for (final Square toSquare : toSquareSet) {
      for (final PromotionPieceType promotionPieceType : PromotionPieceType.values()) {
        if (promotionPieceType == PromotionPieceType.NONE) {
          continue;
        }
        printMoveMinimum(fromSquare, toSquare, promotionPieceType);
      }
    }
  }

  // G2H1,
  // G2H1Q,
  private static void printMoveMinimum(Square fromSquare, Square toSquare, PromotionPieceType promotionPieceType) {
    final StringBuilder enumElement = new StringBuilder();
    enumElement.append(fromSquare);
    enumElement.append(toSquare);
    if (promotionPieceType != PromotionPieceType.NONE) {
      enumElement.append(promotionPieceType.getPieceType().getLetter());
    }
    enumElement.append(",");

    System.out.println(enumElement.toString());
  }

  // G2H1(G2, H1, PromotionPieceType.NONE),
  // G2H1Q(G2, H1, PromotionPieceType.QUEEN),
  static void printMoveShort(Square fromSquare, Square toSquare, PromotionPieceType promotionPieceType) {
    final StringBuilder enumElement = new StringBuilder();
    enumElement.append(fromSquare);
    enumElement.append(toSquare);
    if (promotionPieceType != PromotionPieceType.NONE) {
      enumElement.append(promotionPieceType.getPieceType().getLetter());
    }
    enumElement.append("(");
    enumElement.append(fromSquare);
    enumElement.append(", ");
    enumElement.append(toSquare);
    enumElement.append(", ");

    enumElement.append("PromotionPieceType.");
    enumElement.append(promotionPieceType);
    enumElement.append("),");

    System.out.println(enumElement.toString());
  }

  // G2H1(Square.G2, Square.H1, "g2h1", false, PromotionPieceType.NONE),
  // G2H1Q(Square.G2, Square.H1, "g2h1q", true, PromotionPieceType.QUEEN),
  static void printMoveLong(Square fromSquare, Square toSquare, PromotionPieceType promotionPieceType) {
    final StringBuilder enumElement = new StringBuilder();
    enumElement.append(fromSquare);
    enumElement.append(toSquare);
    if (promotionPieceType != PromotionPieceType.NONE) {
      enumElement.append(promotionPieceType.getPieceType().getLetter());
    }
    enumElement.append("(Square.");
    enumElement.append(fromSquare);
    enumElement.append(", Square.");
    enumElement.append(toSquare);
    enumElement.append(", \"");
    enumElement.append(fromSquare.getName());
    enumElement.append(toSquare.getName());
    if (promotionPieceType != PromotionPieceType.NONE) {
      final var promotionPieceTypeLetterLowerCase = NonNullWrapperCommon
          .toLowerCase(promotionPieceType.getPieceType().getLetter());
      enumElement.append(promotionPieceTypeLetterLowerCase);
    }

    if (promotionPieceType == PromotionPieceType.NONE) {
      enumElement.append("\", false, PromotionPieceType.NONE),");
    } else {
      enumElement.append("\", true, PromotionPieceType.");
      enumElement.append(promotionPieceType);
      enumElement.append("),");
    }

    System.out.println(enumElement.toString());
  }

  public static List<Square> getRankBeforePromotionRank(Side side) {
    return switch (side) {
      case WHITE -> Square.SEVENTH_RANK;
      case BLACK -> Square.SECOND_RANK;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }
}
