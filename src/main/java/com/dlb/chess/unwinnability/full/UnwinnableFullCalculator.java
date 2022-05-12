package com.dlb.chess.unwinnability.full;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.unwinnability.findhelpmate.FindHelpmate;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.mobility.Mobility;
import com.dlb.chess.unwinnability.mobility.model.MobilitySolution;
import com.dlb.chess.unwinnability.semistatic.UnwinnableSemiStatic;

//Figure 9 Main routine for deciding chess unwinnability. It is based on our semi-static
//algorithm (Figure 8) and our search routine (Figure 5) integrated via iterative deepening.
//Function bound must be increasing on d for the algorithm to be complete. The transposition
//table used by Find-Helpmatec should be initialized to empty at the beginning, but it can be
//shared between different calls to Find-Helpmatec in step 3. On the other hand, the global
//counter cnt should be initialized to 0 on every base call to Find-Helpmatec in step 3.
public class UnwinnableFullCalculator {

  // Inputs: position, intended winner
  // Output: Unwinnable or Winnable (definite solution to the chess unwinnability problem)
  public static UnwinnableFull unwinnableFull(ApiBoard board, Side c) {

    // 1: if true UnwinnableSS(pos, c, Mobility(pos)) then return Unwinnable

    final MobilitySolution mobilitySolution = Mobility.mobility(board);
    if (UnwinnableSemiStatic.unwinnableSemiStatic(board, c, mobilitySolution)) {
      return UnwinnableFull.UNWINNABLE;
    }

    // we must instantiate the class here to share the transposition table between calls
    final FindHelpmate findHelpmate = new FindHelpmate(c);

    // 2: for every d in N do ( -> Iterative deepening)
    for (var maxDepth = 0; maxDepth <= Integer.MAX_VALUE; maxDepth++) {
      // 3: set bd Find-Helpmatec(pos, 0, maxDepth = d) (global nodesBound = bound(d))

      final var bd = findHelpmate.findHelpmate(board, maxDepth);

      switch (bd) {
        case TRUE:
          // 4: if bd = true then return Winnable
          return UnwinnableFull.WINNABLE;
        case FALSE:
          // 5: else if the search was not interrupted (in step 4 of Figure 5) then
          // 6: return Unwinnable
          return UnwinnableFull.UNWINNABLE;
        case INTERRUPTED:
          // the algorithm continues with next depth
          break;
        default:
          throw new IllegalArgumentException();
      }
    }

    return UnwinnableFull.UNWINNABLE;
  }
}
