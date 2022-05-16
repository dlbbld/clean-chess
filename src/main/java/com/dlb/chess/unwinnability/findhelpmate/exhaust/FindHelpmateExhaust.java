package com.dlb.chess.unwinnability.findhelpmate.exhaust;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.board.enums.SquareType;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.MaterialUtility;
import com.dlb.chess.fen.FenParserRaw;
import com.dlb.chess.fen.model.FenRaw;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.squares.to.threaten.AbstractThreatenSquares;
import com.dlb.chess.unwinnability.findhelpmate.AbstractFindHelpmate;
import com.dlb.chess.unwinnability.findhelpmate.enums.FindHelpmateRecursionResult;
import com.dlb.chess.unwinnability.findhelpmate.enums.FindHelpmateResult;
import com.dlb.chess.unwinnability.findhelpmate.exhaust.classicalcheckmate.ClassicalCheckmate;
import com.dlb.chess.unwinnability.findhelpmate.exhaust.classicalcheckmate.ComparatorClassicalCheckmate;

//Figure 5 Find-Helpmatec routine, returns true if a checkmate sequence for player c in {w, b},
//the intended winner, is found or false otherwise. The base call should be done on depth = 0,
//cnt = 0, and an empty table. The value of maxDepth and nodesBound can be chosen to set the
//limits of the search. The Score routine is defined in Figure 12 (Appendix A).
public class FindHelpmateExhaust extends AbstractFindHelpmate {

  private static final Logger logger = NonNullWrapperCommon.getLogger(FindHelpmateExhaust.class);

  // empirically enough
  private static final int NODES_BOUND = 10000;

  private final Side color;
  private final HashMap<String, Integer> transpositionMap = new HashMap<>();

  private int cnt = 0;
  private boolean isCanExhaust = true;
  private List<LegalMove> moveProgressList = new ArrayList<>();

  public FindHelpmateExhaust(Side side) {
    this.color = side;
  }

  public FindHelpmateResult calculateHelpmate(ApiBoard board, int maxDepth) {

    final String invariant = board.getFen();

    if (maxDepth != 0 && maxDepth % 10 == 0) {
      logger.info("maxDepth=" + maxDepth);
    }
    this.cnt = 0;
    this.isCanExhaust = true;
    this.moveProgressList = new ArrayList<>();

    final var findHelpmate = findHelpmate(board, 0, maxDepth);

    if (!invariant.equals(board.getFen())) {
      throw new ProgrammingMistakeException("Board was changed");
    }

    switch (findHelpmate) {
      case TRUE:
        checkHelpmate(board.getFen(), moveProgressList);
        return FindHelpmateResult.YES;
      case CLASSICAL_CHECKMATE_POSITION:
        checkClassicalCheckmate(board.getFen(), moveProgressList);
        return FindHelpmateResult.YES;
      case FALSE:
        if (isCanExhaust) {
          return FindHelpmateResult.NO;
        }
        return FindHelpmateResult.UNKNOWN;
      default:
        throw new IllegalArgumentException();
    }
  }

  // Inputs: position, depth (int), maxDepth (int)
  // Output: bool (true if a checkmate sequence was found, false otherwise)
  private FindHelpmateRecursionResult findHelpmate(ApiBoard board, int depth, int maxDepth) {

    // added for in c code
    if (board.isInsufficientMaterial(color)) {
      return FindHelpmateRecursionResult.FALSE;
    }

    // 1: if the intended winner is checkmating their opponent in pos then return true
    if (board.getHavingMove() == color.getOppositeSide() && board.isCheckmate()) {
      return FindHelpmateRecursionResult.TRUE;
    }

    // adding fivefold repetition and seventy-five move rule
    if (board.isFivefoldRepetition() || board.isSeventyFiftyMove()) {
      return FindHelpmateRecursionResult.FALSE;
    }

    // we add classical checkmate as game end
    if (ClassicalCheckmate.isClassicalCheckmateMaterial(board.getHavingMove(), board.getStaticPosition())) {
      return FindHelpmateRecursionResult.CLASSICAL_CHECKMATE_POSITION;
    }

    // 2: if the intended winner has just the king or the position is unwinnable according
    // to Lemma 5 or Lemma 6 or the position is stalemate or the intended winner is
    // receiving checkmate in the position then return false

    // if the intended winner has just the king

    // position is unwinnable according to Lemma 5
    // position is unwinnable according to Lemma 6
    if (MaterialUtility.calculateHasKingOnly(color, board.getStaticPosition())
        || calculateIsUnwinnableAccordingLemma5(color, board.getStaticPosition())
        || calculateIsUnwinnableAccordingLemma6(color, board.getStaticPosition())) {
      return FindHelpmateRecursionResult.FALSE;
    }

    // stalemate
    // intended winner is receiving checkmate in the position then return false
    if (board.isStalemate() || board.getHavingMove() == color && board.isCheckmate()) {
      return FindHelpmateRecursionResult.FALSE;
    }

    // set d := limits.max-depth - depth
    final var d = maxDepth - depth;

    // 5: if (pos,D) in table with D >= d then return false (-> pos was already analyzed)
    if (calculateIsInTranspositionTable(board.getFen(), d)) {
      return FindHelpmateRecursionResult.FALSE;
    }

    // 3: increase cnt and set d := limits.max-depth - depth

    // increase cnt
    cnt++;

    // 4: if cnt > nodesBound or d < 0 then return false (-> The search limits are exceeded)
    if (cnt > NODES_BOUND || d < 0) {

      if (isCanExhaust) {
        isCanExhaust = false;
      }
      return FindHelpmateRecursionResult.FALSE;
    }

    // 6: store (pos,D) in table
    store(board.getFen(), d);

    // 7: for every legal move m in pos do:
    final List<LegalMove> legalMoveList = new ArrayList<>(board.getLegalMoveSet());
    final Set<Square> squaresAttackedByNotHavingMove = AbstractThreatenSquares
        .calculateThreatenedSquares(board.getStaticPosition(), board.getHavingMove().getOppositeSide());
    Collections.sort(legalMoveList, new ComparatorClassicalCheckmate(color, board.getHavingMove(),
        board.getStaticPosition(), squaresAttackedByNotHavingMove));
    for (final LegalMove legalMove : legalMoveList) {
      // 8: let inc = match Score(pos,m) with Normal ! 0 | Reward ! 1 | Punish ! −2
      // TODO today add again
      // final var inc = Score.score(color, board.getHavingMove(), board.getStaticPosition(), legalMove).getIncrement();
      final var inc = 0;

      // 9: if Find-Helpmatec(pos.move(m), depth+1, maxDepth+inc) then return true
      board.performMove(legalMove.moveSpecification());
      moveProgressList.add(legalMove);
      final var findHelpmate = findHelpmate(board, depth + 1, maxDepth + inc);
      board.unperformMove();
      switch (findHelpmate) {
        case TRUE:
        case CLASSICAL_CHECKMATE_POSITION:
          return findHelpmate;
        case FALSE:
          // continue
          break;
        default:
          throw new IllegalArgumentException();
      }
      moveProgressList.remove(moveProgressList.size() - 1);
    }

    // 10: return false (-> No mate was found after exploring every legal move)
    return FindHelpmateRecursionResult.FALSE;

  }

  private boolean calculateIsInTranspositionTable(String fen, int d) {
    final String positionIdentifier = calculatePositionIdentifier(fen);
    if (transpositionMap.containsKey(positionIdentifier)) {
      final int storedDepth = NonNullWrapperCommon.get(transpositionMap, positionIdentifier);
      return storedDepth >= d;
    }
    return false;
  }

  private void store(String fen, int d) {
    final String positionIdentifier = calculatePositionIdentifier(fen);
    transpositionMap.put(positionIdentifier, d);
  }

  private static boolean calculateIsUnwinnableAccordingLemma5(Side color, StaticPosition staticPosition) {
    if (!MaterialUtility.calculateHasPawn(staticPosition)
        && MaterialUtility.calculateHasKingAndKnightOnly(color, staticPosition)) {
      if (MaterialUtility.calculateHasNoKnights(color.getOppositeSide(), staticPosition)
          && MaterialUtility.calculateHasNoBishops(color.getOppositeSide(), staticPosition)
          && MaterialUtility.calculateHasNoRooks(color.getOppositeSide(), staticPosition)) {
        return true;
      }
    }
    return false;
  }

  private static boolean calculateIsUnwinnableAccordingLemma6(Side color, StaticPosition staticPosition) {
    for (final SquareType squareType : SquareType.values()) {
      if (squareType == SquareType.NONE) {
        continue;
      }
      if (!MaterialUtility.calculateHasPawn(staticPosition)
          && MaterialUtility.calculateHasKingAndBishopsOnly(color, staticPosition, squareType)
          && MaterialUtility.calculateHasNoKnights(color.getOppositeSide(), staticPosition)
          && MaterialUtility.calculateHasNoBishops(color, staticPosition, squareType.getOppositeSquareType())) {
        return true;
      }
    }
    return false;
  }

  private static String calculatePositionIdentifier(String fen) {
    final FenRaw fenRaw = FenParserRaw.parseFenRaw(fen);
    final StringBuilder result = new StringBuilder();
    result.append(fenRaw.piecePlacement()).append("*");
    result.append(fenRaw.castlingRightBothStr()).append("*");
    result.append(fenRaw.enPassantCaptureTargetSquare());
    return NonNullWrapperCommon.toString(result);
  }
}
