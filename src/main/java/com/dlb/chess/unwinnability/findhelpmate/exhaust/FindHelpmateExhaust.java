package com.dlb.chess.unwinnability.findhelpmate.exhaust;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.board.enums.SquareType;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ConfigurationConstants;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.ucimove.utility.UciMoveUtility;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.common.utility.MaterialUtility;
import com.dlb.chess.fen.FenParserRaw;
import com.dlb.chess.fen.model.FenRaw;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.model.UciMove;
import com.dlb.chess.moves.utility.EnPassantCaptureUtility;
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

  private static final boolean IS_DEBUG = false;

  private static final Logger logger = NonNullWrapperCommon.getLogger(FindHelpmateExhaust.class);

  // empirically enough
  private static final int LOCAL_NODES_BOUND = 10000;

  private final Side color;
  private final HashMap<String, Integer> transpositionMap = new HashMap<>();

  private int localNodeCount = 0;

  private int evalCounter = 0;
  private final List<String> evalFenList = new ArrayList<>();
  private boolean isCanExhaust = true;
  private List<LegalMove> moveEvaluationList = new ArrayList<>();

  public FindHelpmateExhaust(Side side) {
    this.color = side;
  }

  public FindHelpmateAnalysis calculateHelpmate(ApiBoard board, int maxDepth) {

    final String invariant = board.getFen();

    if (maxDepth != 0 && maxDepth % 10 == 0) {
      logger.printf(Level.DEBUG, "maxDepth=%d", maxDepth);
    }

    this.localNodeCount = 0;
    this.isCanExhaust = true;
    this.moveEvaluationList = new ArrayList<>();

    final var findHelpmate = findHelpmate(board, 0, maxDepth, 0, false);

    if (!invariant.equals(board.getFen())) {
      throw new ProgrammingMistakeException("Board was changed");
    }

    if (IS_DEBUG) {
      final @NonNull Path filePath = NonNullWrapperCommon.resolve(ConfigurationConstants.TEMP_FOLDER_PATH,
          "fenListMine.txt");
      FileUtility.writeFile(filePath, evalFenList);
    }

    switch (findHelpmate) {
      case TRUE:
        checkHelpmate(board.getFen(), moveEvaluationList);
        return new FindHelpmateAnalysis(FindHelpmateResult.YES, localNodeCount,
            convertLegalMoveList(moveEvaluationList));
      case FALSE:
        if (isCanExhaust) {
          return new FindHelpmateAnalysis(FindHelpmateResult.NO, localNodeCount, new ArrayList<>());
        }
        return new FindHelpmateAnalysis(FindHelpmateResult.UNKNOWN, localNodeCount, new ArrayList<>());
      default:
        throw new IllegalArgumentException();
    }
  }

  // Inputs: position, depth (int), maxDepth (int)
  // Output: bool (true if a checkmate sequence was found, false otherwise)
  private FindHelpmateRecursionResult findHelpmate(ApiBoard board, int depth, int maxDepth, int actualDepth,
      boolean isPastProgress) {

    // 1: if the intended winner is checkmating their opponent in pos then return true
    if (board.getHavingMove() == color.getOppositeSide() && board.isCheckmate()) {
      return FindHelpmateRecursionResult.TRUE;
    }

    // 2: if the intended winner has just the king or the position is unwinnable according
    // to Lemma 5 or Lemma 6 or the position is stalemate or the intended winner is
    // receiving checkmate in the position then return false

    // Note: below omitted as not in the C++ code
    // or the position is stalemate or the intended winner is
    // receiving checkmate in the position then return false

    // set d := limits.max-depth - depth
    final var movesLeft = maxDepth - depth;

    final String cacheKey = calculateCacheKey(board);
    // 5: if (pos,D) in table with D >= d then return false (-> pos was already analyzed)
    if (calculateIsInTranspositionTableWithEnoughDepth(cacheKey, movesLeft)) {
      return FindHelpmateRecursionResult.FALSE;
    }

    // 4: if cnt > nodesBound or d < 0 then return false (-> The search limits are exceeded)
    if (localNodeCount > maxDepth * LOCAL_NODES_BOUND || movesLeft <= 0) {

      if (isCanExhaust) {
        isCanExhaust = false;
      }
      return FindHelpmateRecursionResult.FALSE;
    }

    // 6: store (pos,D) in table
    store(cacheKey, movesLeft);

    // adding fivefold repetition and seventy-five move rule
    if (board.isFivefoldRepetition() || board.isSeventyFiftyMove()) {
      return FindHelpmateRecursionResult.FALSE;
    }

    if (MaterialUtility.calculateHasKingOnly(color, board.getStaticPosition())
        || MaterialUtility.calculateHasNoPawns(color.getOppositeSide(), board.getStaticPosition())
            && calculateIsNeedLoserPromotion(color, board.getStaticPosition())) {
      return FindHelpmateRecursionResult.FALSE;
    }

    // 7: for every legal move m in pos do:
    final List<LegalMove> legalMoveList = new ArrayList<>(board.getLegalMoveSet());

    for (final LegalMove legalMove : legalMoveList) {
      // 8: let inc = match Score(pos,m) with Normal ! 0 | Reward ! 1 | Punish ! −2
      ScoreResult score = Score.score(color, board.getHavingMove(), board.getStaticPosition(), legalMove);

      if (board.getHavingMove() == color.getOppositeSide()
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

      if (IS_DEBUG) {
        final String uciMoveStr = UciMoveUtility.convertMoveSpecificationToUci(legalMove.moveSpecification()).text();
        final var out = uciMoveStr + " " + newDepth;
        System.out.println(out);
        evalCounter++;
        final String evaluateStockfishFen = calculateStockfishFen(board);

        if (evaluateStockfishFen.startsWith("r1bqkb1r/pppppppp/8/8/5P2/8/2n3P1/n1K5 w kq")) {
          final var debug = 100;
          System.out.println(debug);
        }

        if (evalCounter == 3527) {
          final var debug = 100;
          System.out.println(debug);
        }

        evalFenList.add(evaluateStockfishFen);
      }

      // 9: if Find-Helpmatec(pos.move(m), depth+1, maxDepth+inc) then return true
      board.performMove(legalMove.moveSpecification());

      moveEvaluationList.add(legalMove);

      final var isProgress = score == ScoreResult.REWARD;

      // 3: increase cnt
      localNodeCount++;

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

  private boolean calculateIsInTranspositionTableWithEnoughDepth(String cacheKey, int movesLeft) {
    if (transpositionMap.containsKey(cacheKey)) {
      final int storedDepth = NonNullWrapperCommon.get(transpositionMap, cacheKey);
      return storedDepth >= movesLeft;
    }
    return false;
  }

  private void store(String cacheKey, int movesLeft) {
    transpositionMap.put(cacheKey, movesLeft);
  }

  static boolean calculateIsUnwinnableAccordingLemma5(Side color, StaticPosition staticPosition) {
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

  static boolean calculateIsUnwinnableAccordingLemma6(Side color, StaticPosition staticPosition) {
    for (final SquareType squareType : SquareType.values()) {
      if (squareType == SquareType.NONE) {
        continue;
      }
      if (MaterialUtility.calculateHasKingAndBishopsOnly(color, staticPosition, squareType)
          && MaterialUtility.calculateHasNoKnights(color.getOppositeSide(), staticPosition)
          && MaterialUtility.calculateHasNoBishops(color, staticPosition, squareType.getOppositeSquareType())
          && MaterialUtility.calculateHasNoPawns(color.getOppositeSide(), staticPosition)) {
        return true;
      }
    }
    return false;
  }

  public static boolean calculateIsNeedLoserPromotion(Side winner, StaticPosition staticPosition) {
    if (calculateIsKnightNeedsPromotion(winner, staticPosition)) {
      return true;
    }

    return calculateIsBishopNeedsPromotion(winner, staticPosition);
  }

  private static boolean calculateIsKnightNeedsPromotion(Side winner, StaticPosition staticPosition) {
    // if the intended winner has just a knight and the intended loser has just pawns
    // and/or queens
    return MaterialUtility.calculateHasKingAndKnightOnly(winner, staticPosition)
        && MaterialUtility.calculateHasNoRooks(winner.getOppositeSide(), staticPosition)
        && MaterialUtility.calculateHasNoBishops(winner.getOppositeSide(), staticPosition)
        && MaterialUtility.calculateHasNoKnights(winner.getOppositeSide(), staticPosition);
  }

  private static boolean calculateIsBishopNeedsPromotion(Side winner, StaticPosition staticPosition) {
    // or the intended winner has just bishops of the same square color and
    // the intended loser does not have knights or bishops of the opposite color

    for (final SquareType squareType : SquareType.values()) {
      if (squareType == SquareType.NONE) {
        continue;
      }
      if (MaterialUtility.calculateHasKingAndBishopsOnly(winner, staticPosition, squareType)
          && MaterialUtility.calculateHasNoKnights(winner.getOppositeSide(), staticPosition) && MaterialUtility
              .calculateHasNoBishops(winner.getOppositeSide(), staticPosition, squareType.getOppositeSquareType())) {
        return true;
      }
    }

    return false;
  }

  private static List<UciMove> convertLegalMoveList(List<LegalMove> moveProgressList) {
    final List<UciMove> result = new ArrayList<>();
    for (final LegalMove legalMove : moveProgressList) {
      result.add(UciMoveUtility.convertMoveSpecificationToUci(legalMove.moveSpecification()));
    }
    return result;
  }

  private static String calculateStockfishFen(ApiBoard board) {

    if (!calculateIsEraseEnPassantCaptureTargetSquare(board)) {
      return board.getFen();
    }

    final FenRaw fenRaw = FenParserRaw.parseFenRaw(board.getFen());

    final StringBuilder fenSquareErased = new StringBuilder();

    fenSquareErased.append(fenRaw.piecePlacement());
    fenSquareErased.append(" ");

    fenSquareErased.append(fenRaw.havingMove());
    fenSquareErased.append(" ");

    fenSquareErased.append(fenRaw.castlingRightBothStr());
    fenSquareErased.append(" ");

    fenSquareErased.append("-");
    fenSquareErased.append(" ");

    fenSquareErased.append(fenRaw.halfMoveClock());
    fenSquareErased.append(" ");

    fenSquareErased.append(fenRaw.fullMoveNumber());

    return NonNullWrapperCommon.toString(fenSquareErased);
  }

  private static String calculateCacheKey(ApiBoard board) {
    final FenRaw fenRaw = FenParserRaw.parseFenRaw(board.getFen());

    final StringBuilder fenSquareErased = new StringBuilder();

    fenSquareErased.append(fenRaw.piecePlacement());
    fenSquareErased.append(" ");

    fenSquareErased.append(fenRaw.havingMove());
    fenSquareErased.append(" ");

    fenSquareErased.append(fenRaw.castlingRightBothStr());

    if (!"-".equals(fenRaw.enPassantCaptureTargetSquare()) && !calculateIsEraseEnPassantCaptureTargetSquare(board)) {
      fenSquareErased.append(" ");

      fenSquareErased.append(fenRaw.enPassantCaptureTargetSquare());
    }

    return NonNullWrapperCommon.toString(fenSquareErased);
  }

  private static boolean calculateIsEraseEnPassantCaptureTargetSquare(ApiBoard board) {
    final Square enPassantCaptureTargetSquare = board.getEnPassantCaptureTargetSquare();

    if (enPassantCaptureTargetSquare == Square.NONE) {
      return false;
    }

    final Square pawnTwoAdvanceSquare = Square.calculateAheadSquare(board.getHavingMove().getOppositeSide(),
        enPassantCaptureTargetSquare);

    return !EnPassantCaptureUtility.calculateHasOpponentPawnOnLeftOrRight(pawnTwoAdvanceSquare,
        board.getStaticPosition());

  }

}
