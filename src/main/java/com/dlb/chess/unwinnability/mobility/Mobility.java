package com.dlb.chess.unwinnability.mobility;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.unwinnability.mobility.enums.VariableState;
import com.dlb.chess.unwinnability.mobility.model.Clearance;
import com.dlb.chess.unwinnability.mobility.model.MobilitySolution;
import com.dlb.chess.unwinnability.mobility.model.MobilitySolutionVariable;
import com.dlb.chess.unwinnability.mobility.model.Reachability;
import com.dlb.chess.unwinnability.mobility.model.ReachabilityVariable;
import com.dlb.chess.unwinnability.model.PiecePlacement;

//Figure 7 Mobility algorithm.
public class Mobility {

  // Inputs: a position
  // Output: mobility solution {MP!s}P in pos,s in S
  public static MobilitySolution mobility(ApiBoard board) {

    // 1: set MP->s := 0, CP := 0, Rcs
    // := 0 for all P in pos, s in S and c in {w, b} and let X_arrow be
    // the state containing all these variables

    // set MP->s := 0
    final List<PiecePlacement> piecePlacementList = new ArrayList<>();
    for (final Square sq : Square.values()) {
      if (sq == Square.NONE) {
        continue;
      }
      if (!board.getStaticPosition().isEmpty(sq)) {
        final Piece piece = board.getStaticPosition().get(sq);
        final PiecePlacement p = new PiecePlacement(piece.getPieceType(), piece.getSide(), sq);
        piecePlacementList.add(p);
      }
    }

    final MobilitySolution mps = new MobilitySolution();
    for (final PiecePlacement p : piecePlacementList) {
      for (final Square s : Square.values()) {
        if (s == Square.NONE) {
          continue;
        }
        mps.put(p, s, VariableState.ZERO);
      }
    }

    // CP := 0
    final Clearance cp = new Clearance();
    for (final PiecePlacement p : piecePlacementList) {
      cp.put(p, VariableState.ZERO);
    }

    // Rcs := 0
    final Reachability rcs = new Reachability();
    for (final Side c : Side.values()) {
      if (c == Side.NONE) {
        continue;
      }
      for (final Square s : Square.values()) {
        if (s == Square.NONE) {
          continue;
        }
        rcs.put(c, s, VariableState.ZERO);
      }
    }

    // 2: set MP -> P.sq := 1, for all P in pos ( -> Every piece can “move” to its current square)
    for (final PiecePlacement p : piecePlacementList) {
      mps.put(p, p.squareOriginal(), VariableState.ONE);
    }

    var isNewVariablesAreSetToOne = true;
    while (isNewVariablesAreSetToOne) {
      final var totalVariableCountSetToOneBefore = calculateTotalVariableCountSetToOne(mps, cp, rcs);
      // 3: for every variable V in X_arrow that is still set to 0 do

      // 4: if for every rule from Figure 6 of the form “V ) f”, formula f evaluates to true
      // on the current state of variables ~X then set V to 1 in X_arrow

      // Clearance
      // Clearance. A piece can be cleared from its square by moving or being captured:
      for (final PiecePlacement checkClearancePiece : cp.calculateEntriesWithValueZero()) {

        // moving
        var isClearanceSetToOne = false;
        for (final Square checkClearanceMoving : Square.values()) {
          if (checkClearanceMoving == Square.NONE || checkClearanceMoving == checkClearancePiece.squareOriginal()) {
            continue;
          }
          if (mps.get(checkClearancePiece, checkClearanceMoving) == VariableState.ONE) {
            isClearanceSetToOne = true;
            cp.put(checkClearancePiece, VariableState.ONE);
            break;
          }
        }

        // captured
        if (!isClearanceSetToOne) {
          for (final MobilitySolutionVariable checkClearanceCapture : mps.calculateEntriesWithValueOne()) {
            if (checkClearanceCapture.piecePlacement().side() != checkClearancePiece.side()
                && checkClearanceCapture.squareTo() == checkClearancePiece.squareOriginal()) {
              cp.put(checkClearancePiece, VariableState.ONE);
              break;
            }
          }
        }
      }

      // Reachability
      // Reachability. A square can be reached if some piece can move to it or it is occupied:
      for (final ReachabilityVariable checkReachability : rcs.calculateEntriesWithValueZero()) {
        for (final MobilitySolutionVariable checkReaching : mps.calculateEntriesWithValueOne()) {
          if (checkReaching.piecePlacement().side() == checkReachability.c()
              && checkReaching.piecePlacement().type() != PieceType.KING
              && checkReaching.squareTo() == checkReachability.s()) {
            rcs.put(checkReachability.c(), checkReachability.s(), VariableState.ONE);
            break;
          }
        }
      }

      // Mobility
      for (final MobilitySolutionVariable mobilitySolutionValueZero : mps.calculateEntriesWithValueZero()) {

        var isFirstCondition = false;
        // Move. If a piece can move to square s, it must pass first by a predecessor of s:
        // all pieces except pawns
        if (mobilitySolutionValueZero.piecePlacement().type() == PieceType.PAWN) {

          var isValidPawnForwardMove = false;
          for (final Square predecessor : MobilityFunctions.predecessors(mobilitySolutionValueZero.piecePlacement(),
              mobilitySolutionValueZero.squareTo())) {
            if (mps.get(mobilitySolutionValueZero.piecePlacement(), predecessor) == VariableState.ONE) {
              // Pawn push cannot be performed if there is an obstacle in target
              var isNonClearablePieceAhead = false;
              for (final PiecePlacement piecePlacement : piecePlacementList) {
                if (piecePlacement.squareOriginal() == mobilitySolutionValueZero.squareTo()
                    && cp.get(piecePlacement) == VariableState.ZERO) {
                  isNonClearablePieceAhead = true;
                }
              }
              if (!isNonClearablePieceAhead) {
                isValidPawnForwardMove = true;
                break;
              }
            }
          }

          if (isValidPawnForwardMove) {
            isFirstCondition = true;
          } else {
            // Pawn move. For pawn captures we require that the capturing square can be reached
            // by a (non-king) opponent piece. Pawns that promote may go everywhere:
            // only pawns
            var isValidPawnCapture = false;
            final Set<Square> predecessorsCaptureSet = MobilityFunctions
                .predecessorsCapture(mobilitySolutionValueZero.piecePlacement(), mobilitySolutionValueZero.squareTo());

            for (final Square predecessorCapture : predecessorsCaptureSet) {
              if (mps.get(mobilitySolutionValueZero.piecePlacement(), predecessorCapture) == VariableState.ONE
                  && rcs.get(mobilitySolutionValueZero.piecePlacement().side().getOppositeSide(),
                      mobilitySolutionValueZero.squareTo()) == VariableState.ONE) {
                isValidPawnCapture = true;
                break;
              }
            }

            if (isValidPawnCapture) {
              isFirstCondition = true;
            } else {

              final Set<Square> promotionSquareSet = MobilityFunctions
                  .promotion(mobilitySolutionValueZero.piecePlacement());
              final Set<Square> squareSetWithValueOne = mps
                  .calculateSquaresWithValueOne(mobilitySolutionValueZero.piecePlacement());

              final var isValidPawnPromotion = !BasicUtility.calculateIsDisjoint(promotionSquareSet,
                  squareSetWithValueOne);
              if (isValidPawnPromotion) {
                isFirstCondition = true;
              }
            }
          }

        } else {
          for (final Square predecessor : MobilityFunctions.predecessors(mobilitySolutionValueZero.piecePlacement(),
              mobilitySolutionValueZero.squareTo())) {
            if (mps.get(mobilitySolutionValueZero.piecePlacement(), predecessor) == VariableState.ONE) {
              isFirstCondition = true;
              break;
            }
          }
        }
        if (!isFirstCondition) {
          continue;
        }

        // because we have logical and, the king case only is enough
        var isThirdCondition = true;
        // King attackers. Direct opponent attackers must be cleared before a king can move:
        // king only
        if (mobilitySolutionValueZero.piecePlacement().type() == PieceType.KING) {
          for (final PiecePlacement attacker : MobilityFunctions.attackers(piecePlacementList,
              mobilitySolutionValueZero.squareTo())) {
            if (attacker.side() != mobilitySolutionValueZero.piecePlacement().side()
                && cp.get(attacker) != VariableState.ONE) {
              isThirdCondition = false;
              break;
            }
          }
        }
        if (!isThirdCondition) {
          continue;
        }

        var isFourthCondition = true;
        // Not self-capture. A piece must be cleared from a square before other of the same
        // color can move to it:
        // all pieces
        for (final PiecePlacement checkPiecePlacement : piecePlacementList) {
          if (checkPiecePlacement != mobilitySolutionValueZero.piecePlacement()
              && checkPiecePlacement.side() == mobilitySolutionValueZero.piecePlacement().side()
              && checkPiecePlacement.squareOriginal() == mobilitySolutionValueZero.squareTo()
              && cp.get(checkPiecePlacement) == VariableState.ZERO) {
            isFourthCondition = false;
            break;
          }
        }

        if (!isFourthCondition) {
          continue;
        }

        mps.put(mobilitySolutionValueZero.piecePlacement(), mobilitySolutionValueZero.squareTo(), VariableState.ONE);
      }

      // 5: repeat steps 3 and 4 until no new variables are set to 1
      final var totalVariableCountSetToOneAfter = calculateTotalVariableCountSetToOne(mps, cp, rcs);
      if (totalVariableCountSetToOneBefore > totalVariableCountSetToOneAfter) {
        throw new ProgrammingMistakeException("Variable state was incorrectly set");
      }
      isNewVariablesAreSetToOne = totalVariableCountSetToOneAfter > totalVariableCountSetToOneBefore;
    }

    // 6: return {MP -> s}P in pos, s in S
    return mps;

  }

  private static int calculateTotalVariableCountSetToOne(MobilitySolution mps, Clearance cp, Reachability rcs) {
    return mps.calculateVariableCountSetToOne() + cp.calculateVariableCountSetToOne()
        + rcs.calculateVariableCountSetToOne();
  }
}
