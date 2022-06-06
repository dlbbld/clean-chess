package com.dlb.chess.unwinnability.full;

import java.util.ArrayList;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.unwinnability.findhelpmate.exhaust.FindHelpmateExhaust;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.full.model.UnwinnableFullAnalysis;
import com.dlb.chess.unwinnability.mobility.Mobility;
import com.dlb.chess.unwinnability.mobility.model.MobilitySolution;
import com.dlb.chess.unwinnability.semistatic.UnwinnableSemiStatic;

//Figure 9 Main routine for deciding chess unwinnability. It is based on our semi-static
//algorithm (Figure 8) and our search routine (Figure 5) integrated via iterative deepening.
//Function bound must be increasing on d for the algorithm to be complete. The transposition
//table used by Find-Helpmatec should be initialized to empty at the beginning, but it can be
//shared between different calls to Find-Helpmatec in step 3. On the other hand, the global
//counter cnt should be initialized to 0 on every base call to Find-Helpmatec in step 3.
public class UnwinnableFullAnalyzer {

  private static final int MAX_DEPTH = 100;
  private static final int GLOBAL_NODES_BOUND = 500000;

  // Inputs: position, intended winner
  // Output: Unwinnable or Winnable (definite solution to the chess unwinnability problem)
  public static UnwinnableFullAnalysis unwinnableFull(ApiBoard board, Side c) {

    // add optimization from code
    var isForcedMove = board.getLegalMoveSet().size() == 1;
    // to avoid endless loops in positions with each side having one repeating forced move
    var allowedMoves = 100;
    var totalForcedMoves = 0;
    while (isForcedMove && allowedMoves > 0) {
      final LegalMove onlyLegalMove = NonNullWrapperCommon.getFirst(new ArrayList<>(board.getLegalMoveSet()));
      board.performMove(onlyLegalMove.moveSpecification());
      isForcedMove = board.getLegalMoveSet().size() == 1;
      allowedMoves--;
      totalForcedMoves++;
    }

    // 1: if true UnwinnableSS(pos, c, Mobility(pos)) then return Unwinnable
    final MobilitySolution mobilitySolution = Mobility.mobility(board);
    if (UnwinnableSemiStatic.unwinnableSemiStatic(board, c, mobilitySolution)) {
      undoForcedMoves(board, totalForcedMoves);
      return new UnwinnableFullAnalysis(UnwinnableFull.UNWINNABLE, new ArrayList<>());
    }

    // we must instantiate the class here to share the transposition table between calls
    final FindHelpmateExhaust findHelpmate = new FindHelpmateExhaust(c);

    // 2: for every d in N do ( -> Iterative deepening)
    var globalNodeCount = 0;
    for (var maxDepth = 2; maxDepth <= MAX_DEPTH; maxDepth++) {
      // 3: set bd Find-Helpmatec(pos, 0, maxDepth = d) (global nodesBound = bound(d))

      final var helpmateAnalysis = findHelpmate.calculateHelpmate(board, maxDepth);

      globalNodeCount += helpmateAnalysis.localNodesCount();

      if (globalNodeCount > GLOBAL_NODES_BOUND) {
        return new UnwinnableFullAnalysis(UnwinnableFull.UNDETERMINED, new ArrayList<>());
      }

      switch (helpmateAnalysis.findHelpmateResult()) {
        case YES:
          // 4: if bd = true then return Winnable
          undoForcedMoves(board, totalForcedMoves);
          return new UnwinnableFullAnalysis(UnwinnableFull.WINNABLE, helpmateAnalysis.mateLine());
        case NO:
          // 5: else if the search was not interrupted (in step 4 of Figure 5) then
          // 6: return Unwinnable
          undoForcedMoves(board, totalForcedMoves);
          return new UnwinnableFullAnalysis(UnwinnableFull.UNWINNABLE, new ArrayList<>());
        case UNKNOWN:
          // the algorithm continues with next depth
          break;
        default:
          throw new IllegalArgumentException();
      }
    }

    undoForcedMoves(board, totalForcedMoves);
    return new UnwinnableFullAnalysis(UnwinnableFull.UNWINNABLE, new ArrayList<>());
  }

  private static void undoForcedMoves(ApiBoard board, int totalForcedMoves) {
    for (var i = 1; i <= totalForcedMoves; i++) {
      board.unperformMove();
    }
  }
}
