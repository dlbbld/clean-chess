package com.dlb.chess.unwinnability.semistatic;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.board.enums.SquareType;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.unwinnability.functions.KingDistanceOneFunctions;
import com.dlb.chess.unwinnability.mobility.model.MobilitySolution;
import com.dlb.chess.unwinnability.model.PiecePlacement;

//Figure 8 Semi-statically unwinnable algorithm, which may conclude that a position is
//unwinnable for an intended winner based on an admissible solution to the mobility problem.

//a We could design a more complete check that looks at all neighbours of s, but the condition on
//step 10 would be significantly more involved (to ensure monotonicity).
public class UnwinnableSemiStatic {

  // Inputs: position, intended winner, solution to the mobility problem
  // Output: bool (true if position is declared unwinnable, false otherwise)
  public static boolean unwinnableSemiStatic(ApiBoard board, Side c, MobilitySolution mobilitySolution) {

    // 1: if en passant is possible or a player has castling rights in pos then return false

    // en passant is possible
    // a player has castling rights
    if (board.isEnPassantCapturePossible() || board.getCastlingRightBoth().castlingRightWhite() != CastlingRight.NONE
        || board.getCastlingRightBoth().castlingRightBlack() != CastlingRight.NONE) {
      return false;
    }

    // 2: for every piece P in pos, define region(P) := {s in S |MP>s = 1} ( -> The squares that
    // can potentially be reached by piece P)

    // 3: let Kc (resp. K¬c) be the intended winner’s king (resp. intended loser’s king)
    // final PiecePlacement intendedWinnerKing = calculateKing(c, mobilitySolution);
    final PiecePlacement intendedLoserKing = calculateKing(c.getOppositeSide(), mobilitySolution);

    // 4: set intruders := {P in pos | P.side = c ^ region(P) \ region(K¬c) != the empty set} ( -> The
    // intended winner’s pieces that can potentially reach the intended loser’s king region)

    // 5: if ex P in intruders with P.type != bishop then return false ( -> We require that the set of
    // intruders be empty or formed entirely by bishops for the position to be unwinnable)
    final Set<PiecePlacement> intruderSet = SemiStaticFunctions.intruders(intendedLoserKing, mobilitySolution);
    for (final PiecePlacement intruder : intruderSet) {
      if (intruder.type() != PieceType.BISHOP) {
        return false;
      }
    }

    // 6: if 9P, P0 2 intruders with color(P.sq) 6= color(P0.sq) then return false ( -> We
    // require that all intruders (only bishops at this point) be of the same square color)
    final Set<SquareType> squareTypeSet = new TreeSet<>();
    for (final PiecePlacement intruder : intruderSet) {
      squareTypeSet.add(intruder.sq().getSquareType());
    }
    if (squareTypeSet.size() > 1) {
      return false;
    }

    // 7: for P 2 pos, define att-region(P) := {s 2 S | pred-captP (s) \ region(P) 6= ;} (-> The
    // squares that can potentially be attacked by piece P)

    // 8: for s 2 S, let blockers(s) := {P 2 pos | P.side 6= c ^ region(P) \ (s) 6= ;}a ( -> The
    // intended loser’s pieces that can potentially reach an adjacent square to s)

    // 9: for s 2 S, define assistants(s) := {P 2 pos | P.side = c ^ att-region(P) \ (s) 6= ;}
    // ( -> The intended winner’s pieces that can potentially attack an adjacent square to s)

    // 10: if 9s 2 region(K¬c) such that |blockers(s)| + |assistants(s)|  | (s)| and 9P 2 pos
    // satisfying s 2 att-region(P) ^ P.side = c then return false . There is a square s
    // that can potentially be reached by K¬c and attacked by the intended winner; and
    // there are enough defenders/attackers that can block/cover all adjacent squares to s

    for (final Square squareKingEscape : SemiStaticFunctions.region(intendedLoserKing, mobilitySolution)) {
      final Set<PiecePlacement> blockerPieceSet = SemiStaticFunctions.blockers(squareKingEscape, c, mobilitySolution);
      final Set<PiecePlacement> assistantsPieceSet = SemiStaticFunctions.assistants(squareKingEscape, c,
          mobilitySolution);
      final Set<Square> adjacentSet = KingDistanceOneFunctions.calculateOrthogonalSquares(squareKingEscape);

      if (blockerPieceSet.size() + assistantsPieceSet.size() >= adjacentSet.size()) {
        for (final PiecePlacement checkPiece : mobilitySolution.getPiecePlacementSet()) {
          if (checkPiece.side() == c) {
            final Set<Square> attRegionSet = SemiStaticFunctions.attackedRegion(checkPiece, mobilitySolution);

            if (attRegionSet.contains(squareKingEscape)) {
              return false;
            }
          }
        }
      }
    }

    // 11: return true . The position must be unwinnable
    return true;

  }

  private static PiecePlacement calculateKing(Side c, MobilitySolution mobilitySolution) {
    for (final PiecePlacement piecePlacement : mobilitySolution.getPiecePlacementSet()) {
      if (piecePlacement.side() == c && piecePlacement.type() == PieceType.KING) {
        return piecePlacement;
      }
    }
    throw new ProgrammingMistakeException("King not in the list");
  }
}
