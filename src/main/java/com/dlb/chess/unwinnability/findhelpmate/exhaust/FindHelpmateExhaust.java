package com.dlb.chess.unwinnability.findhelpmate.exhaust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.SquareType;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.ucimove.utility.UciMoveUtility;
import com.dlb.chess.common.utility.MaterialUtility;
import com.dlb.chess.fen.FenParserRaw;
import com.dlb.chess.fen.model.FenRaw;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.model.UciMove;
import com.dlb.chess.unwinnability.findhelpmate.AbstractFindHelpmate;
import com.dlb.chess.unwinnability.findhelpmate.enums.FindHelpmateRecursionResult;
import com.dlb.chess.unwinnability.findhelpmate.enums.FindHelpmateResult;
import com.dlb.chess.unwinnability.findhelpmate.exhaust.enums.ScoreResult;
import com.dlb.chess.unwinnability.findhelpmate.exhaust.model.FindHelpmateAnalysis;

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
  private int evalCounter = 0;
  private boolean isCanExhaust = true;
  private List<LegalMove> moveEvaluationList = new ArrayList<>();

  public FindHelpmateExhaust(Side side) {
    this.color = side;
  }

  public FindHelpmateAnalysis calculateHelpmate(ApiBoard board, int maxDepth) {

    final String invariant = board.getFen();

    if (maxDepth != 0 && maxDepth % 10 == 0) {
      logger.info("maxDepth=" + maxDepth);
    }
    this.cnt = 0;
    this.isCanExhaust = true;
    this.moveEvaluationList = new ArrayList<>();

    final var findHelpmate = findHelpmate(board, 0, maxDepth, 0, false);

    if (!invariant.equals(board.getFen())) {
      throw new ProgrammingMistakeException("Board was changed");
    }

    switch (findHelpmate) {
      case TRUE:
        checkHelpmate(board.getFen(), moveEvaluationList);
        return new FindHelpmateAnalysis(FindHelpmateResult.YES, convertLegalMoveList(moveEvaluationList));
      case FALSE:
        if (isCanExhaust) {
          return new FindHelpmateAnalysis(FindHelpmateResult.NO, new ArrayList<>());
        }
        return new FindHelpmateAnalysis(FindHelpmateResult.UNKNOWN, new ArrayList<>());
      default:
        throw new IllegalArgumentException();
    }
  }

  // Inputs: position, depth (int), maxDepth (int)
  // Output: bool (true if a checkmate sequence was found, false otherwise)
  private FindHelpmateRecursionResult findHelpmate(ApiBoard board, int depth, int maxDepth, int actualDepth,
      boolean isPastProgress) {

    // adding fivefold repetition and seventy-five move rule
    // if (board.isFivefoldRepetition() || board.isSeventyFiftyMove()) {
    // return FindHelpmateRecursionResult.FALSE;
    // }

    // 2: if the intended winner has just the king or the position is unwinnable according
    // to Lemma 5 or Lemma 6 or the position is stalemate or the intended winner is
    // receiving checkmate in the position then return false

    // Note: below omitted as not in the C++ code
    // or the position is stalemate or the intended winner is
    // receiving checkmate in the position then return false

    // set d := limits.max-depth - depth
    final var movesLeft = maxDepth - depth;

    // 5: if (pos,D) in table with D >= d then return false (-> pos was already analyzed)
    if (calculateIsInTranspositionTable(board, movesLeft)
        || MaterialUtility.calculateHasKingOnly(color, board.getStaticPosition())
        || MaterialUtility.calculateHasNoPawns(color.getOppositeSide(), board.getStaticPosition())
            && calculateIsNeedLoserPromotion(color, board.getStaticPosition())) {
      return FindHelpmateRecursionResult.FALSE;
    }

    // 1: if the intended winner is checkmating their opponent in pos then return true
    if (board.getHavingMove() == color.getOppositeSide() && board.isCheckmate()) {
      return FindHelpmateRecursionResult.TRUE;
    }

    // 3: increase cnt and set d := limits.max-depth - depth

    // increase cnt
    cnt++;

    // 4: if cnt > nodesBound or d < 0 then return false (-> The search limits are exceeded)
    if (cnt > maxDepth * NODES_BOUND || movesLeft <= 0) {

      if (isCanExhaust) {
        isCanExhaust = false;
      }
      return FindHelpmateRecursionResult.FALSE;
    }

    // 6: store (pos,D) in table
    store(board, movesLeft);

    // 7: for every legal move m in pos do:
    final List<LegalMove> legalMoveList = new ArrayList<>(board.getLegalMoveSet());

    for (final LegalMove legalMove : legalMoveList) {
      // 8: let inc = match Score(pos,m) with Normal ! 0 | Reward ! 1 | Punish ! −2
      ScoreResult score = Score.score(color, board.getHavingMove(), board.getStaticPosition(), legalMove);

      if (board.getHavingMove() != color
          && MaterialUtility.calculateHasQueen(color.getOppositeSide(), board.getStaticPosition())) {
        score = score == ScoreResult.REWARD ? ScoreResult.NORMAL : score;
      }

      if (actualDepth > 300) {
        score = score == ScoreResult.REWARD ? ScoreResult.NORMAL : score;
      }

      var newDepth = depth + 1;
      switch (score) {
        case REWARD:
          newDepth = newDepth - 1;
          break;
        case PUNISH:
          newDepth = Math.min(maxDepth, newDepth + 2);
          break;
        case NORMAL:
          if (isPastProgress) {
            newDepth = newDepth - 1;
          }
          break;
        default:
          throw new IllegalArgumentException();
      }

      // 9: if Find-Helpmatec(pos.move(m), depth+1, maxDepth+inc) then return true
      board.performMove(legalMove.moveSpecification());

      final String uciMoveStr = UciMoveUtility.convertMoveSpecificationToUci(legalMove.moveSpecification()).text();
      final var out = uciMoveStr + " " + newDepth;
      System.out.println(out);
      evalCounter++;
      if (evalCounter == 4200) {
        final var debug = 100;
      }

      moveEvaluationList.add(legalMove);
      final var isProgress = score == ScoreResult.REWARD;
      final var findHelpmate = findHelpmate(board, newDepth, maxDepth, actualDepth + 1, isProgress);
      board.unperformMove();
      switch (findHelpmate) {
        case TRUE:
          return findHelpmate;
        case FALSE:
          // continue
          break;
        default:
          throw new IllegalArgumentException();
      }
      moveEvaluationList.remove(moveEvaluationList.size() - 1);
    }

    // 10: return false (-> No mate was found after exploring every legal move)
    return FindHelpmateRecursionResult.FALSE;

  }

  private boolean calculateIsInTranspositionTable(ApiBoard board, int movesLeft) {
    final String positionIdentifier = calculatePositionIdentifier(board);
    if (transpositionMap.containsKey(positionIdentifier)) {
      final int storedDepth = NonNullWrapperCommon.get(transpositionMap, positionIdentifier);
      return storedDepth >= movesLeft;
    }
    return false;
  }

  private void store(ApiBoard board, int movesLeft) {
    final String positionIdentifier = calculatePositionIdentifier(board);
    transpositionMap.put(positionIdentifier, movesLeft);
  }

  private static boolean calculateIsUnwinnableAccordingLemma5(Side color, StaticPosition staticPosition) {
    if (MaterialUtility.calculateHasKingAndKnightOnly(color, staticPosition)) {
      if (MaterialUtility.calculateHasNoKnights(color.getOppositeSide(), staticPosition)
          && MaterialUtility.calculateHasNoBishops(color.getOppositeSide(), staticPosition)
          && MaterialUtility.calculateHasNoRooks(color.getOppositeSide(), staticPosition)
          && MaterialUtility.calculateHasNoPawns(color.getOppositeSide(), staticPosition)) {
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
      if (MaterialUtility.calculateHasKingAndBishopsOnly(color, staticPosition, squareType)) {
        if (MaterialUtility.calculateHasNoKnights(color.getOppositeSide(), staticPosition)
            && MaterialUtility.calculateHasNoBishops(color, staticPosition, squareType.getOppositeSquareType())
            && MaterialUtility.calculateHasNoPawns(color.getOppositeSide(), staticPosition)) {
          return true;
        }
      }
    }
    return false;
  }

  private static boolean calculateIsNeedLoserPromotion(Side winner, StaticPosition staticPosition) {
    if (MaterialUtility.calculateHasKingAndKnightOnly(winner, staticPosition)) {
      if (MaterialUtility.calculateHasNoKnights(winner.getOppositeSide(), staticPosition)
          && MaterialUtility.calculateHasNoBishops(winner.getOppositeSide(), staticPosition)
          && MaterialUtility.calculateHasNoRooks(winner.getOppositeSide(), staticPosition)) {
        return true;
      }
    }

    for (final SquareType squareType : SquareType.values()) {
      if (squareType == SquareType.NONE) {
        continue;
      }
      if (MaterialUtility.calculateHasKingAndBishopsOnly(winner, staticPosition, squareType)) {
        if (MaterialUtility.calculateHasNoKnights(winner.getOppositeSide(), staticPosition)
            && MaterialUtility.calculateHasNoBishops(winner, staticPosition, squareType.getOppositeSquareType())) {
          return true;
        }
      }
    }

    return false;
  }

  private static String calculatePositionIdentifier(ApiBoard board) {
    final FenRaw fenRaw = FenParserRaw.parseFenRaw(board.getFen());
    final StringBuilder result = new StringBuilder();
    result.append(fenRaw.piecePlacement()).append("*");
    result.append(fenRaw.castlingRightBothStr()).append("*");
    if (board.isEnPassantCapturePossible()) {
      result.append(fenRaw.enPassantCaptureTargetSquare());
    }
    return NonNullWrapperCommon.toString(result);
  }

  private static List<UciMove> convertLegalMoveList(List<LegalMove> moveProgressList) {
    final List<UciMove> result = new ArrayList<>();
    for (final LegalMove legalMove : moveProgressList) {
      result.add(UciMoveUtility.convertMoveSpecificationToUci(legalMove.moveSpecification()));
    }
    return result;
  }
}
