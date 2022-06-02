package com.dlb.chess.unwinnability.mobility;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.squares.emptyboard.KingNonCastlingEmptyBoardSquares;
import com.dlb.chess.squares.emptyboard.KnightEmptyBoardSquares;
import com.dlb.chess.unwinnability.functions.KingDistanceOneFunctions;
import com.dlb.chess.unwinnability.model.PiecePlacement;

public class MobilityFunctions implements EnumConstants {

  public static Set<Square> predecessorsCapture(PiecePlacement piecePlacement, Square square) {
    switch (piecePlacement.pieceType()) {
      case PAWN:
        final Set<Square> result = new TreeSet<>();
        if (Square.calculateHasBehindLeftDiagonalSquare(piecePlacement.side(), square)) {
          result.add(Square.calculateBehindLeftDiagonalSquare(piecePlacement.side(), square));
        }
        if (Square.calculateHasBehindRightDiagonalSquare(piecePlacement.side(), square)) {
          result.add(Square.calculateBehindRightDiagonalSquare(piecePlacement.side(), square));
        }
        return result;
      case KNIGHT:
      case BISHOP:
      case ROOK:
      case KING:
      case QUEEN:
        return MobilityFunctions.predecessors(piecePlacement, square);
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  static Set<Square> promotion(PiecePlacement piecePlacement) {
    switch (piecePlacement.pieceType()) {
      case PAWN:
        switch (piecePlacement.side()) {
          case WHITE:
            return new TreeSet<>(Arrays.asList(A8, B8, C8, D8, E8, F8, G8, H8));
          case BLACK:
            return new TreeSet<>(Arrays.asList(A1, B1, C1, D1, E1, F1, G1, H1));
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      case KNIGHT:
      case BISHOP:
      case ROOK:
      case KING:
      case QUEEN:
        return new TreeSet<>();
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  static Set<Square> predecessors(PiecePlacement piecePlacement, Square square) {
    switch (piecePlacement.pieceType()) {
      case PAWN:
        return calculateBehindSquare(piecePlacement.side(), square);
      case KNIGHT:
        return KnightEmptyBoardSquares.getKnightSquares(square);
      case BISHOP:
        return KingDistanceOneFunctions.calculateDiagonalSquares(square);
      case ROOK:
        return KingDistanceOneFunctions.calculateOrthogonalSquares(square);
      case KING:
      case QUEEN:
        return KingNonCastlingEmptyBoardSquares.getKingSquares(square);
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  private static Set<Square> calculateBehindSquare(Side side, Square square) {
    if (!Square.calculateHasBehindSquare(side, square)) {
      return new TreeSet<>();
    }
    final Set<Square> result = new TreeSet<>();
    result.add(Square.calculateBehindSquare(side, square));
    return result;
  }

  static Set<PiecePlacement> attackers(Set<PiecePlacement> piecePlacementList, Square square) {
    final Set<PiecePlacement> result = new TreeSet<>();

    for (final PiecePlacement piecePlacement : piecePlacementList) {
      if (predecessorsCapture(piecePlacement, square).contains(piecePlacement.squareOriginal())) {
        result.add(piecePlacement);
      }
    }
    return result;
  }

  static Set<PiecePlacement> attackers(List<PiecePlacement> piecePlacementList, Square square) {
    return attackers(new TreeSet<>(piecePlacementList), square);
  }

}
