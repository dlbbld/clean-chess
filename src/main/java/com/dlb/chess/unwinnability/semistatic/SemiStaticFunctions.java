package com.dlb.chess.unwinnability.semistatic;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.unwinnability.functions.KingDistanceOneFunctions;
import com.dlb.chess.unwinnability.mobility.MobilityFunctions;
import com.dlb.chess.unwinnability.mobility.model.MobilitySolution;
import com.dlb.chess.unwinnability.mobility.model.MobilitySolutionVariable;
import com.dlb.chess.unwinnability.model.PiecePlacement;

class SemiStaticFunctions {

  static Set<PiecePlacement> assistants(Square s, Side c, MobilitySolution ms) {
    final Set<PiecePlacement> result = new TreeSet<>();

    final Set<Square> adjacentSet = KingDistanceOneFunctions.calculateOrthogonalSquares(s);
    for (final PiecePlacement piecePlacement : ms.getPiecePlacementSet()) {
      if (piecePlacement.side() == c) {
        final Set<Square> regionPiece = SemiStaticFunctions.attackedRegion(piecePlacement, ms);
        if (!BasicUtility.calculateIsDisjoint(regionPiece, adjacentSet)) {
          result.add(piecePlacement);
        }
      }
    }
    return result;
  }

  static Set<Square> attackedRegion(PiecePlacement piecePlacement, MobilitySolution ms) {
    final Set<Square> result = new TreeSet<>();

    final Set<Square> regionPiece = SemiStaticFunctions.region(piecePlacement, ms);
    for (final Square square : Square.values()) {
      if (square == Square.NONE) {
        continue;
      }
      final Set<Square> predecessorsCaptureSet = MobilityFunctions.predecessorsCapture(piecePlacement, square);

      if (!BasicUtility.calculateIsDisjoint(predecessorsCaptureSet, regionPiece)) {
        result.add(square);
      }
    }

    return result;

  }

  static Set<PiecePlacement> blockers(Square s, Side c, MobilitySolution ms) {
    final Set<PiecePlacement> result = new TreeSet<>();

    final Set<Square> adjacentSet = KingDistanceOneFunctions.calculateOrthogonalSquares(s);
    for (final PiecePlacement piecePlacement : ms.getPiecePlacementSet()) {
      if (piecePlacement.side() != c && piecePlacement.type() != PieceType.KING) {
        final Set<Square> regionPiece = SemiStaticFunctions.region(piecePlacement, ms);
        if (!BasicUtility.calculateIsDisjoint(regionPiece, adjacentSet)) {
          result.add(piecePlacement);
        }
      }
    }
    return result;
  }

  static Set<PiecePlacement> intruders(PiecePlacement intendedLosersKing, MobilitySolution ms) {
    final Set<PiecePlacement> result = new TreeSet<>();

    final Set<Square> regionKing = SemiStaticFunctions.region(intendedLosersKing, ms);
    for (final PiecePlacement piecePlacement : ms.getPiecePlacementSet()) {
      if (piecePlacement.side() != intendedLosersKing.side()) {
        final Set<Square> regionPiece = SemiStaticFunctions.region(piecePlacement, ms);
        if (!BasicUtility.calculateIsDisjoint(regionPiece, regionKing)) {
          result.add(piecePlacement);
        }
      }
    }
    return result;
  }

  static Set<Square> region(PiecePlacement p, MobilitySolution ms) {
    final Set<Square> result = new TreeSet<>();

    for (final MobilitySolutionVariable entry : ms.calculateEntriesWithValueOne()) {
      if (entry.piecePlacement() == p) {
        result.add(entry.s());
      }
    }
    return result;
  }

}
