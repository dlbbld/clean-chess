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

  public static Set<Square> predecessorsCapture(PiecePlacement p, Square s) {
    switch (p.pieceType()) {
      case PAWN:
        final Set<Square> result = new TreeSet<>();
        if (Square.calculateHasBehindLeftDiagonalSquare(p.side(), s)) {
          result.add(Square.calculateBehindLeftDiagonalSquare(p.side(), s));
        }
        if (Square.calculateHasBehindRightDiagonalSquare(p.side(), s)) {
          result.add(Square.calculateBehindRightDiagonalSquare(p.side(), s));
        }
        return result;
      case KNIGHT:
      case BISHOP:
      case ROOK:
      case KING:
      case QUEEN:
        return MobilityFunctions.predecessors(p, s);
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  static Set<Square> promotion(PiecePlacement p) {
    switch (p.pieceType()) {
      case PAWN:
        switch (p.side()) {
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

  static Set<Square> predecessors(PiecePlacement p, Square s) {
    switch (p.pieceType()) {
      case PAWN:
        return calculateBehindSquare(p.side(), s);
      case KNIGHT:
        return KnightEmptyBoardSquares.getKnightSquares(s);
      case BISHOP:
        return KingDistanceOneFunctions.calculateDiagonalSquares(s);
      case ROOK:
        return KingDistanceOneFunctions.calculateOrthogonalSquares(s);
      case KING:
      case QUEEN:
        return KingNonCastlingEmptyBoardSquares.getKingSquares(s);
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  private static Set<Square> calculateBehindSquare(Side side, Square sq) {
    if (!Square.calculateHasBehindSquare(side, sq)) {
      return new TreeSet<>();
    }
    final Set<Square> result = new TreeSet<>();
    result.add(Square.calculateBehindSquare(side, sq));
    return result;
  }

  static Set<PiecePlacement> attackers(List<PiecePlacement> piecePlacementList, Square s) {
    final Set<PiecePlacement> result = new TreeSet<>();

    for (final PiecePlacement piecePlacement : piecePlacementList) {
      if (predecessorsCapture(piecePlacement, s).contains(piecePlacement.squareOriginal())) {
        result.add(piecePlacement);
      }
    }
    return result;

  }

}
