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

  private static final boolean IS_DEBUG = true;

  // Inputs: a position
  // Output: mobility solution {MP!s}P in pos,s in S
  public static MobilitySolution mobility(ApiBoard board) {

    // 1: set MP->s := 0, CP := 0, Rcs
    // := 0 for all P in pos, s in S and c in {w, b} and let X_arrow be
    // the state containing all these variables

    // set MP->s := 0
    final List<PiecePlacement> piecePlacementList = new ArrayList<>();
    for (final Square square : Square.values()) {
      if (square == Square.NONE) {
        continue;
      }
      if (!board.getStaticPosition().isEmpty(square)) {
        final Piece piece = board.getStaticPosition().get(square);
        final PiecePlacement p = new PiecePlacement(piece.getPieceType(), piece.getSide(), square);
        piecePlacementList.add(p);
      }
    }

    final MobilitySolution mobility = new MobilitySolution();
    for (final PiecePlacement piecePlacement : piecePlacementList) {
      for (final Square square : Square.values()) {
        if (square == Square.NONE) {
          continue;
        }
        mobility.put(piecePlacement, square, VariableState.ZERO);
      }
    }

    // CP := 0
    final Clearance clearance = new Clearance();
    for (final PiecePlacement piecePlacement : piecePlacementList) {
      clearance.put(piecePlacement, VariableState.ZERO);
    }

    // Rcs := 0
    final Reachability reachability = new Reachability();
    for (final Side side : Side.values()) {
      if (side == Side.NONE) {
        continue;
      }
      for (final Square square : Square.values()) {
        if (square == Square.NONE) {
          continue;
        }
        reachability.put(side, square, VariableState.ZERO);
      }
    }

    // 2: set MP -> P.sq := 1, for all P in pos ( -> Every piece can “move” to its current square)
    for (final PiecePlacement piecePlacement : piecePlacementList) {
      mobility.put(piecePlacement, piecePlacement.squareOriginal(), VariableState.ONE);
    }

    var isNewVariablesAreSetToOne = true;
    while (isNewVariablesAreSetToOne) {
      final var totalVariableCountSetToOneBefore = calculateTotalVariableCountSetToOne(mobility, clearance,
          reachability);
      // 3: for every variable V in X_arrow that is still set to 0 do

      // 4: if for every rule from Figure 6 of the form “V ) f”, formula f evaluates to true
      // on the current state of variables ~X then set V to 1 in X_arrow

      // Update clearance
      // Clearance. A piece can be cleared from its square by moving or being captured:
      for (final PiecePlacement candidateClearance : clearance.calculateEntriesWithValueZero()) {

        // clearable by moving
        var isClearancePossibleByMoving = false;
        for (final Square evaluateToSquare : Square.values()) {
          if (evaluateToSquare == Square.NONE || evaluateToSquare == candidateClearance.squareOriginal()) {
            continue;
          }
          if (mobility.get(candidateClearance, evaluateToSquare) == VariableState.ONE) {
            isClearancePossibleByMoving = true;
            clearance.put(candidateClearance, VariableState.ONE);
            break;
          }
        }

        // clearable by capturing
        if (!isClearancePossibleByMoving) {
          for (final MobilitySolutionVariable evaluateCapture : mobility.calculateEntriesWithValueOne()) {
            if (evaluateCapture.piecePlacement().side() != candidateClearance.side()
                && evaluateCapture.toSquare() == candidateClearance.squareOriginal()) {
              clearance.put(candidateClearance, VariableState.ONE);
              break;
            }
          }
        }
      }

      // Update reachability
      // Reachability. A square can be reached if some piece can move to it or it is occupied:
      for (final ReachabilityVariable candidateReachability : reachability.calculateEntriesWithValueZero()) {
        for (final MobilitySolutionVariable evaluateReachability : mobility.calculateEntriesWithValueOne()) {
          if (evaluateReachability.piecePlacement().side() == candidateReachability.sideWhichCanReach()
              && evaluateReachability.piecePlacement().pieceType() != PieceType.KING
              && evaluateReachability.toSquare() == candidateReachability.reachableSquare()) {
            reachability.put(candidateReachability.sideWhichCanReach(), candidateReachability.reachableSquare(),
                VariableState.ONE);
            break;
          }
        }
      }

      // Update mobility
      for (final MobilitySolutionVariable candidateMobility : mobility.calculateEntriesWithValueZero()) {
        var isPredecessorRequirementOk = false;
        // Move. If a piece can move to square s, it must pass first by a predecessor of s:
        // all pieces except pawns
        final PiecePlacement candidatePiecePlacement = candidateMobility.piecePlacement();
        final PieceType candidatePieceType = candidateMobility.piecePlacement().pieceType();
        final Side candidateSide = candidatePiecePlacement.side();
        final Side candidateOppositeSide = candidateSide.getOppositeSide();
        final Square candidateToSquare = candidateMobility.toSquare();
        if (candidatePieceType == PieceType.PAWN) {

          var isValidPawnForwardMove = false;
          for (final Square predecessor : MobilityFunctions.predecessors(candidatePiecePlacement, candidateToSquare)) {
            if (mobility.get(candidatePiecePlacement, predecessor) == VariableState.ONE) {
              // Pawn move. For pawn pushes, we require that a possibly enemy piece on the target
              // square can be cleared first.
              // Note: we check that we don't move onto own piece in last step

              var isNonClearableOpponentPieceAhead = false;
              for (final PiecePlacement piecePlacement : piecePlacementList) {
                if (piecePlacement.side() == candidateOppositeSide
                    && piecePlacement.squareOriginal() == candidateToSquare
                    && clearance.get(piecePlacement) == VariableState.ZERO) {
                  isNonClearableOpponentPieceAhead = true;
                }
              }
              if (!isNonClearableOpponentPieceAhead) {
                isValidPawnForwardMove = true;
                break;
              }
            }
          }

          if (isValidPawnForwardMove) {
            isPredecessorRequirementOk = true;
          } else {
            // Pawn move. For pawn captures we require that the capturing square can be reached
            // by a (non-king) opponent piece.
            var isValidPawnCapture = false;
            final Set<Square> predecessorsCaptureSet = MobilityFunctions.predecessorsCapture(candidatePiecePlacement,
                candidateToSquare);

            for (final Square predecessorCapture : predecessorsCaptureSet) {
              if (mobility.get(candidatePiecePlacement, predecessorCapture) == VariableState.ONE
                  && reachability.get(candidateOppositeSide, candidateToSquare) == VariableState.ONE) {
                isValidPawnCapture = true;
                break;
              }
            }

            if (isValidPawnCapture) {
              isPredecessorRequirementOk = true;
            } else {
              // Pawns that promote may go everywhere:
              final Set<Square> promotionSquareSet = MobilityFunctions.promotion(candidatePiecePlacement);
              final Set<Square> squareSetWithValueOne = mobility.calculateSquaresWithValueOne(candidatePiecePlacement);

              final var isValidPawnPromotion = !BasicUtility.calculateIsDisjoint(promotionSquareSet,
                  squareSetWithValueOne);
              if (isValidPawnPromotion) {
                isPredecessorRequirementOk = true;
              }
            }
          }

        } else {
          for (final Square predecessor : MobilityFunctions.predecessors(candidatePiecePlacement, candidateToSquare)) {
            if (mobility.get(candidatePiecePlacement, predecessor) == VariableState.ONE) {
              isPredecessorRequirementOk = true;
              break;
            }
          }
        }
        if (!isPredecessorRequirementOk) {
          continue;
        }

        // because we have logical and, the king case only is enough
        var isKingNonMoveIntoCheckRequirementOk = true;
        // King attackers. Direct opponent attackers must be cleared before a king can move:
        // king only
        if (candidatePieceType == PieceType.KING) {
          for (final PiecePlacement attacker : MobilityFunctions.attackers(piecePlacementList, candidateToSquare)) {
            if (attacker.side() != candidateSide && clearance.get(attacker) != VariableState.ONE) {
              isKingNonMoveIntoCheckRequirementOk = false;
              break;
            }
          }
        }
        if (!isKingNonMoveIntoCheckRequirementOk) {
          continue;
        }

        var isNotMovingOntoOwnPieceRequirementOk = true;
        // Not self-capture. A piece must be cleared from a square before other of the same
        // color can move to it:
        // all pieces
        for (final PiecePlacement checkPiecePlacement : piecePlacementList) {
          if (checkPiecePlacement != candidatePiecePlacement && checkPiecePlacement.side() == candidateSide
              && checkPiecePlacement.squareOriginal() == candidateToSquare
              && clearance.get(checkPiecePlacement) == VariableState.ZERO) {
            isNotMovingOntoOwnPieceRequirementOk = false;
            break;
          }
        }

        if (!isNotMovingOntoOwnPieceRequirementOk) {
          continue;
        }

        if (IS_DEBUG) {
          debug(clearance, reachability, mobility);
        }
        mobility.put(candidatePiecePlacement, candidateToSquare, VariableState.ONE);
      }

      // 5: repeat steps 3 and 4 until no new variables are set to 1
      final var totalVariableCountSetToOneAfter = calculateTotalVariableCountSetToOne(mobility, clearance,
          reachability);
      if (totalVariableCountSetToOneBefore > totalVariableCountSetToOneAfter) {
        throw new ProgrammingMistakeException("Variable state was incorrectly set");
      }
      isNewVariablesAreSetToOne = totalVariableCountSetToOneAfter > totalVariableCountSetToOneBefore;
    }

    debug(clearance, reachability, mobility);

    // 6: return {MP -> s}P in pos, s in S
    return mobility;

  }

  private static void debug(Clearance clearance, Reachability reachability, MobilitySolution mobility) {
    clearance.debug();
    reachability.debug();
    mobility.debug();
  }

  private static int calculateTotalVariableCountSetToOne(MobilitySolution mps, Clearance cp, Reachability rcs) {
    return cp.calculateVariableCountSetToOne() + rcs.calculateVariableCountSetToOne()
        + mps.calculateVariableCountSetToOne();
  }
}
