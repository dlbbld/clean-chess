package com.dlb.chess.unwinnability.findhelpmate;

import java.util.HashMap;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.SquareType;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.DynamicPosition;
import com.dlb.chess.common.utility.MaterialUtility;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.unwinnability.findhelpmate.enums.FindHelpmateResult;

//Figure 5 Find-Helpmatec routine, returns true if a checkmate sequence for player c in {w, b},
//the intended winner, is found or false otherwise. The base call should be done on depth = 0,
//cnt = 0, and an empty table. The value of maxDepth and nodesBound can be chosen to set the
//limits of the search. The Score routine is defined in Figure 12 (Appendix A).
public class FindHelpmate {

  private static final Logger logger = NonNullWrapperCommon.getLogger(FindHelpmate.class);

  private final Side color;

  private final HashMap<DynamicPosition, Integer> map = new HashMap<>();

  private int cnt = 0;

  private boolean isInterrupted = false;

  // empirically enough
  private static final int NODES_BOUND = 10000;

  public FindHelpmate(Side side) {
    this.color = side;
  }

  public FindHelpmateResult findHelpmate(ApiBoard board, int maxDepth) {
    this.cnt = 0;
    this.isInterrupted = false;

    final var hasHelpmate = findHelpmate(board, 0, maxDepth);

    if (!isInterrupted) {
      if (hasHelpmate) {
        return FindHelpmateResult.TRUE;
      }
      return FindHelpmateResult.FALSE;
    }

    return FindHelpmateResult.INTERRUPTED;
  }

  // Inputs: position, depth (int), maxDepth (int)
  // Output: bool (true if a checkmate sequence was found, false otherwise)
  private boolean findHelpmate(ApiBoard board, int depth, int maxDepth) {

    logger.info(depth + " / " + maxDepth);

    // added for in c code
    if (board.isInsufficientMaterial(color)) {
      return false;
    }

    // 1: if the intended winner is checkmating their opponent in pos then return true

    if (board.getHavingMove() == color.getOppositeSide() && board.isCheckmate()) {
      return true;
    }

    // 2: if the intended winner has just the king or the position is unwinnable according
    // to Lemma 5 or Lemma 6 or the position is stalemate or the intended winner is
    // receiving checkmate in the position then return false

    // if the intended winner has just the king
    if (MaterialUtility.calculateIsKingOnly(color, board.getStaticPosition())) {
      return false;
    }

    // position is unwinnable according to Lemma 5
    if (!MaterialUtility.calculateHasPawn(board.getStaticPosition())
        && MaterialUtility.calculateIsKingAndKnightOnly(color, board.getStaticPosition())) {
      if (MaterialUtility.calculateNumberOfPieces(color.getOppositeSide(), board.getStaticPosition(),
          PieceType.KNIGHT) == 0
          && MaterialUtility.calculateNumberOfPieces(color.getOppositeSide(), board.getStaticPosition(),
              PieceType.BISHOP) == 0
          && MaterialUtility.calculateNumberOfPieces(color.getOppositeSide(), board.getStaticPosition(),
              PieceType.ROOK) == 0) {
        return false;
      }
    }

    // position is unwinnable according to Lemma 6
    for (final SquareType squareType : SquareType.values()) {
      if (squareType == SquareType.NONE) {
        continue;
      }
      if (!MaterialUtility.calculateHasPawn(board.getStaticPosition())
          && MaterialUtility.calculateIsKingAndBishopsOnly(color, board.getStaticPosition(), squareType)
          && MaterialUtility.calculateNumberOfPieces(color.getOppositeSide(), board.getStaticPosition(),
              PieceType.KNIGHT) == 0
          && MaterialUtility.calculateNumberOfBishops(color, board.getStaticPosition(),
              squareType.getOppositeSquareType()) == 0) {
        return false;
      }
    }

    // stalemate
    // intended winner is receiving checkmate in the position then return false
    if (board.isStalemate() || board.getHavingMove() == color && board.isCheckmate()) {
      return false;
    }

    // 3: increase cnt and set d := limits.max-depth - depth

    // increase cnt
    cnt++;

    // set d := limits.max-depth - depth
    final var d = maxDepth - depth;

    // 4: if cnt > nodesBound or d < 0 then return false (-> The search limits are exceeded)
    if (cnt > NODES_BOUND || d < 0) {
      if (!isInterrupted) {
        isInterrupted = true;
      }
      return false;
    }

    // 5: if (pos,D) in table with D >= d then return false (-> pos was already analyzed)
    if (calculateIsInTranspositionTable(board.getDynamicPosition(), d)) {
      return false;
    }

    // 6: store (pos,D) in table
    map.put(board.getDynamicPosition(), d);

    // 7: for every legal move m in pos do:
    for (final LegalMove legalMove : board.getLegalMoveSet()) {

      // 8: let inc = match Score(pos,m) with Normal ! 0 | Reward ! 1 | Punish ! −2
      final var inc = Score.score(color, board, legalMove).getChangeIncrement();

      // 9: if Find-Helpmatec(pos.move(m), depth+1, maxDepth+inc) then return true
      board.performMove(legalMove.moveSpecification());
      final var hasHelpmate = findHelpmate(board, depth + 1, maxDepth + inc);
      board.unperformMove();
      if (hasHelpmate) {
        return true;
      }
    }

    // 10: return false (-> No mate was found after exploring every legal move)
    return false;
  }

  private boolean calculateIsInTranspositionTable(DynamicPosition dynamicPosition, int d) {
    if (map.containsKey(dynamicPosition)) {
      final int storedDepth = NonNullWrapperCommon.get(map, dynamicPosition);
      return storedDepth >= d;
    }
    return false;
  }
}
