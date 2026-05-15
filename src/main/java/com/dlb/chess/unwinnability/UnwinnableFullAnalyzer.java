package com.dlb.chess.unwinnability;

import java.util.ArrayList;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.model.LegalMove;

//Figure 9 Main routine for deciding chess unwinnability. It is based on our semi-static
//algorithm (Figure 8) and our search routine (Figure 5) integrated via iterative deepening.
//Function bound must be increasing on d for the algorithm to be complete. The transposition
//table used by Find-Helpmatec should be initialized to empty at the beginning, but it can be
//shared between different calls to Find-Helpmatec in step 3. On the other hand, the global
//counter cnt should be initialized to 0 on every base call to Find-Helpmatec in step 3.
public class UnwinnableFullAnalyzer {

  private static final int MAX_DEPTH = 100;
  private static final int GLOBAL_NODES_BOUND = 500000;

  /**
   * Public entry — wrapped in {@link Board#withDeadPositionDetectionSuppressed} so the analyzer's internal
   * {@code board.move(...)} calls don't re-trigger the auto-detect (which itself runs the quick analyzer).
   */
  public static UnwinnabilityFullAnalysis unwinnableFull(Board board, Side winner) {
    return board.withDeadPositionDetectionSuppressed(() -> unwinnableFull(board, winner, false, new MobilitySolution()));
  }

  // Inputs: position, intended winner
  // Output: Unwinnable or Winnable (definite solution to the chess unwinnability problem)
  private static UnwinnabilityFullAnalysis unwinnableFull(Board board, Side winner, boolean isHasMobilitySolution,
      MobilitySolution calculatedMobilitySolution) {

    // add optimization from code
    // if position is advanced cannot use the provided mobility solution if any
    var isCanUseMobilitySolution = true;
    var isForcedMove = board.getLegalMoves().size() == 1;
    // to avoid endless loops in positions with each side having one repeating forced move
    var isFivefoldOrSeventyFiveMove = board.isFivefoldRepetition() || board.isSeventyFiveMove();
    var totalForcedMoves = 0;
    while (isForcedMove && !isFivefoldOrSeventyFiveMove) {
      isCanUseMobilitySolution = false;
      final LegalMove onlyLegalMove = Nulls.getFirst(board.getLegalMoves());
      board.move(onlyLegalMove.moveSpecification());
      isForcedMove = board.getLegalMoves().size() == 1;
      isFivefoldOrSeventyFiveMove = board.isFivefoldRepetition() || board.isSeventyFiveMove();
      totalForcedMoves++;
    }

    // 1: if true UnwinnableSS(pos, c, Mobility(pos)) then return Unwinnable
    final MobilitySolution mobilitySolution;
    if (isHasMobilitySolution && isCanUseMobilitySolution) {
      mobilitySolution = calculatedMobilitySolution;
    } else {
      mobilitySolution = Mobility.mobility(board);
    }
    if (UnwinnableSemiStatic.unwinnableSemiStatic(board, winner, mobilitySolution)) {
      undoForcedMoves(board, totalForcedMoves);
      return new UnwinnabilityFullAnalysis(UnwinnabilityFullVerdict.UNWINNABLE, new ArrayList<>());
    }

    // we must instantiate the class here to share the transposition table between calls
    final FindHelpmateExhaust findHelpmate = new FindHelpmateExhaust(winner);

    // 2: for every d in N do ( -> Iterative deepening)
    var globalNodeCount = 0;
    for (var maxDepth = 2; maxDepth <= MAX_DEPTH; maxDepth++) {
      // 3: set bd Find-Helpmatec(pos, 0, maxDepth = d) (global nodesBound = bound(d))

      final var helpmateAnalysis = findHelpmate.calculateHelpmate(board, maxDepth);

      globalNodeCount += helpmateAnalysis.localNodesCount();

      if (globalNodeCount > GLOBAL_NODES_BOUND) {
        return new UnwinnabilityFullAnalysis(UnwinnabilityFullVerdict.UNDETERMINED, new ArrayList<>());
      }

      switch (helpmateAnalysis.findHelpmateResult()) {
        case YES:
          // 4: if bd = true then return Winnable
          undoForcedMoves(board, totalForcedMoves);
          return new UnwinnabilityFullAnalysis(UnwinnabilityFullVerdict.WINNABLE, helpmateAnalysis.mateLine());
        case NO:
          // 5: else if the search was not interrupted (in step 4 of Figure 5) then
          // 6: return Unwinnable
          undoForcedMoves(board, totalForcedMoves);
          return new UnwinnabilityFullAnalysis(UnwinnabilityFullVerdict.UNWINNABLE, new ArrayList<>());
        case UNKNOWN:
          // the algorithm continues with next depth
          break;
        default:
          throw new IllegalArgumentException();
      }
    }

    undoForcedMoves(board, totalForcedMoves);
    return new UnwinnabilityFullAnalysis(UnwinnabilityFullVerdict.UNWINNABLE, new ArrayList<>());
  }

  private static void undoForcedMoves(Board board, int totalForcedMoves) {
    for (var i = 1; i <= totalForcedMoves; i++) {
      board.unmove();
    }
  }
}
