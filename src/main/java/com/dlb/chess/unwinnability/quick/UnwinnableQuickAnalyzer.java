package com.dlb.chess.unwinnability.quick;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.MaterialUtility;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.unwinnability.findhelpmate.interrupt.FindHelpMateInterrupt;
import com.dlb.chess.unwinnability.mobility.Mobility;
import com.dlb.chess.unwinnability.mobility.model.MobilitySolution;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;
import com.dlb.chess.unwinnability.quick.utility.SemiOpenFilesUtility;
import com.dlb.chess.unwinnability.semistatic.UnwinnableSemiStatic;

public class UnwinnableQuickAnalyzer {

  public static UnwinnableQuick unwinnableQuick(ApiBoard board, Side c) {
    return unwinnableQuick(board, c, false, new MobilitySolution());
  }

  public static UnwinnableQuick unwinnableQuick(ApiBoard board, Side c, boolean isHasMobilitySolution,
      MobilitySolution calculatedMobilitySolution) {

    final String invariant = board.getFen();

    // 1: advance the position as long as there is only one legal move
    // if position is advanced cannot use the provided mobility solution if any
    var isCanUseMobilitySolution = true;
    var isFivefoldOrSeventyFiveMove = board.isFivefoldRepetition() || board.isSeventyFiftyMove();
    var isForcedMove = true;
    var countHalfmoves = 0;
    while (isForcedMove && !isFivefoldOrSeventyFiveMove) {
      isCanUseMobilitySolution = false;
      if (board.isCheckmate()) {
        // crucial, store the side before undoing moves, as it can change with undoing moves!!
        final Side sideBeingCheckmated = board.getHavingMove();
        unperformHalfmoves(board, countHalfmoves);
        if (!invariant.equals(board.getFen())) {
          throw new ProgrammingMistakeException("Board was changed");
        }
        if (sideBeingCheckmated == c) {
          return UnwinnableQuick.UNWINNABLE;
        }
        return UnwinnableQuick.WINNABLE;
      }

      if (board.isInsufficientMaterial(c) || board.isStalemate()) {
        unperformHalfmoves(board, countHalfmoves);
        if (!invariant.equals(board.getFen())) {
          throw new ProgrammingMistakeException("Board was changed");
        }
        return UnwinnableQuick.UNWINNABLE;
      }

      isForcedMove = board.getLegalMoveSet().size() == 1;
      if (isForcedMove) {
        final List<LegalMove> legalMoveList = new ArrayList<>(board.getLegalMoveSet());
        final LegalMove legalMove = NonNullWrapperCommon.getFirst(legalMoveList);
        board.performMove(legalMove.moveSpecification());
        isFivefoldOrSeventyFiveMove = board.isFivefoldRepetition() || board.isSeventyFiftyMove();
        countHalfmoves++;
      }
    }

    // 2: perform a depth-first search over the tree of variations of pos and interrupt the
    // search if (i) checkmate is found for player c or (ii) depth D is reached
    final String invariantTwo = board.getFen();
    final var checkmateSearchResult = FindHelpMateInterrupt.calculateHelpmate(board, c);
    if (!invariantTwo.equals(board.getFen())) {
      throw new ProgrammingMistakeException("Board was changed");
    }

    switch (checkmateSearchResult) {
      case YES:
        // 3: if checkmate was found on the previous search then return Winnable
        unperformHalfmoves(board, countHalfmoves);
        if (!invariant.equals(board.getFen())) {
          throw new ProgrammingMistakeException("Board was changed");
        }
        return UnwinnableQuick.WINNABLE;
      case NO:
        // 4: else if the search was not interrupted then return Unwinnable
        unperformHalfmoves(board, countHalfmoves);
        if (!invariant.equals(board.getFen())) {
          throw new ProgrammingMistakeException("Board was changed");
        }
        return UnwinnableQuick.UNWINNABLE;
      case UNKNOWN:
        break;
      default:
        throw new IllegalArgumentException();
    }

    // 5: else if the position only contains pieces of type P,B,K and there are no semi-open
    // files in the position then
    if (calculateHasOnlyPawnsBishopsAndKings(board.getStaticPosition())
        && !SemiOpenFilesUtility.calculateHasSemiOpenFile(board.getStaticPosition())) {

      // 6: if true UnwinnableSS(pos, c, Mobility(pos)) then return Unwinnable
      final MobilitySolution mobilitySolution;
      if (isHasMobilitySolution && isCanUseMobilitySolution) {
        mobilitySolution = calculatedMobilitySolution;
      } else {
        mobilitySolution = Mobility.mobility(board);
      }
      if (UnwinnableSemiStatic.unwinnableSemiStatic(board, c, mobilitySolution)) {
        unperformHalfmoves(board, countHalfmoves);
        if (!invariant.equals(board.getFen())) {
          throw new ProgrammingMistakeException("Board was changed");
        }
        return UnwinnableQuick.UNWINNABLE;
      }
    }

    // 7: return PossiblyWinnable ( -> Unwinnability could not be determined)
    unperformHalfmoves(board, countHalfmoves);
    if (!invariant.equals(board.getFen())) {
      throw new ProgrammingMistakeException("Board was changed");
    }
    return UnwinnableQuick.POSSIBLY_WINNABLE;
  }

  private static boolean calculateHasOnlyPawnsBishopsAndKings(StaticPosition staticPosition) {
    return !MaterialUtility.calculateHasRook(staticPosition) && !MaterialUtility.calculateHasKnight(staticPosition)
        && !MaterialUtility.calculateHasQueen(staticPosition);
  }

  private static void unperformHalfmoves(ApiBoard board, int countHalfmoves) {
    for (var i = 1; i <= countHalfmoves; i++) {
      board.unperformMove();
    }
  }
}
