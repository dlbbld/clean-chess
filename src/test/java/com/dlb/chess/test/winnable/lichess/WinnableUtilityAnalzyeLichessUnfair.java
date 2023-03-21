package com.dlb.chess.test.winnable.lichess;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.GameStatusAnalysis;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveRepresentation;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.common.utility.MaterialUtility;
import com.dlb.chess.test.winnable.PawnWall;
import com.dlb.chess.test.winnable.enums.Winnable;
import com.dlb.chess.test.winnable.model.GameForcedAnalysis;
import com.dlb.chess.test.winnable.model.GameMultipleAnalysis;
import com.dlb.chess.test.winnable.model.WinnableAnalysis;
import com.google.common.collect.ImmutableSet;

public class WinnableUtilityAnalzyeLichessUnfair {

  // Examples
  // Black on the move has only two moves, both stalemating
  // 8/6pk/6Pp/7K/3q3P/8/4p3/8 b - - 0 60
  // White on the move has only two moves both leaving black with insufficient material
  // 8/Kq6/P7/8/6k1/8/8/8 w - - 1 50
  // 8/R7/Kr6/P6k/8/8/8/8 w - - 1 57
  // 7k/5R2/6RK/7r/6PP/8/8/8 w - - 2 54

  private static final boolean IS_DEBUG = false;

  private static final Logger logger = NonNullWrapperCommon.getLogger(WinnableUtilityAnalzyeLichessUnfair.class);

  private static final ImmutableSet<GameStatusAnalysis> GAME_DRAW_SET;
  private static final ImmutableSet<GameStatusAnalysis> GAME_WHITE_UNWINNABLE_SET;
  private static final ImmutableSet<GameStatusAnalysis> GAME_BLACK_UNWINNABLE_SET;

  static {
    {
      final EnumSet<GameStatusAnalysis> set = NonNullWrapperCommon.newEnumSet(new ArrayList<>(),
          GameStatusAnalysis.class);

      set.add(GameStatusAnalysis.STALEMATE);
      set.add(GameStatusAnalysis.INSUFFICIENT_MATERIAL_BOTH);
      set.add(GameStatusAnalysis.FIVE_FOLD_REPETITION_RULE);
      set.add(GameStatusAnalysis.SEVENTY_FIVE_MOVE_RULE);
      GAME_DRAW_SET = NonNullWrapperCommon.copyOfSet(set);
    }

    {
      final EnumSet<GameStatusAnalysis> set = NonNullWrapperCommon.newEnumSet(new ArrayList<>(),
          GameStatusAnalysis.class);

      set.addAll(GAME_DRAW_SET);
      set.add(GameStatusAnalysis.BLACK_DELIVERS_CHECKMATE);
      set.add(GameStatusAnalysis.INSUFFICIENT_MATERIAL_WHITE_ONLY);
      GAME_WHITE_UNWINNABLE_SET = NonNullWrapperCommon.copyOfSet(set);
    }

    {
      final EnumSet<GameStatusAnalysis> set = NonNullWrapperCommon.newEnumSet(new ArrayList<>(),
          GameStatusAnalysis.class);

      set.addAll(GAME_DRAW_SET);
      set.add(GameStatusAnalysis.WHITE_DELIVERS_CHECKMATE);
      set.add(GameStatusAnalysis.INSUFFICIENT_MATERIAL_BLACK_ONLY);
      GAME_BLACK_UNWINNABLE_SET = NonNullWrapperCommon.copyOfSet(set);
    }
  }

  public static Winnable calculateWinnable(ApiBoard board, Side sideToEvaluate) {

    // we need an ongoing game
    if (board.isCheckmate()) {
      if (sideToEvaluate == board.getHavingMove()) {
        return Winnable.NO;
      }
      return Winnable.YES;
    }

    if (board.isStalemate() || board.isFivefoldRepetition() || board.isSeventyFiftyMove()) {
      return Winnable.NO;
    }

    if (board.isInsufficientMaterial(sideToEvaluate)) {
      GameStatusAnalysis gameStatus;
      if (board.isInsufficientMaterial(sideToEvaluate.getOppositeSide())) {
        gameStatus = GameStatusAnalysis.INSUFFICIENT_MATERIAL_BOTH;
      } else {
        gameStatus = switch (sideToEvaluate) {
          case WHITE -> GameStatusAnalysis.INSUFFICIENT_MATERIAL_WHITE_ONLY;
          case BLACK -> GameStatusAnalysis.INSUFFICIENT_MATERIAL_BLACK_ONLY;
          case NONE -> throw new IllegalArgumentException();
        };
      }

      final WinnableAnalysis result = createWinnableAnalysis(Winnable.NO, gameStatus);
      logResult(IS_DEBUG, "im;;", result, board, sideToEvaluate, "na", "na");
      return Winnable.NO;
    }

    if (board.getLegalMovesRepresentation().isEmpty()) {
      throw new ProgrammingMistakeException("At this point we must have at least one legal move");
    }

    // Example: White has only one move and must take Black's last piece
    // 7K/6qP/8/8/8/4k3/8/8 w - - 1 76
    // Example: White stalemates or leaves Black with insufficient material on first move
    // Q7/8/4Q3/7k/5Pp1/5KP1/7P/8 w - - 0 1
    // Example: Black can only stalemate or checkmate on first move
    // 8/6p1/5kP1/7K/2r2q2/8/8/8 b - - 1 52
    final var numberOfFirstHalfMoves = board.getLegalMovesRepresentation().size();
    final GameMultipleAnalysis evaluationFirst = evaluateOneMove(board);
    final WinnableAnalysis winnableFirst = calculateWinnableMultiple(evaluationFirst, sideToEvaluate);

    boolean isReturnFirst;
    if (numberOfFirstHalfMoves >= 2) {
      // we are not looking at multiple branches
      isReturnFirst = true;
    } else {
      // in face of only one first move we return only if alread decided for both sides
      isReturnFirst = winnableFirst.winnable() != Winnable.UNKNOWN;
    }
    if (isReturnFirst) {
      // TODO now is test code quality only
      var isKingOnlyNonFlagging = "na";
      var isKingOnlyFlagging = "na";
      if (numberOfFirstHalfMoves == 1) {
        final MoveRepresentation moveRepresentation = NonNullWrapperCommon
            .getFirst(board.getLegalMovesRepresentation());
        board.performMove(moveRepresentation.moveSpecification());
        if (MaterialUtility.calculateHasKingOnly(board.getHavingMove(), board.getStaticPosition())) {
          isKingOnlyNonFlagging = "yes";
        } else {
          isKingOnlyNonFlagging = "no";
        }
        if (MaterialUtility.calculateHasKingOnly(board.getHavingMove().getOppositeSide(), board.getStaticPosition())) {
          isKingOnlyFlagging = "yes";
        } else {
          isKingOnlyFlagging = "no";
        }
        board.unperformMove();
      }

      logResult(IS_DEBUG, "first;" + evaluationFirst.numberOfHalfMoves(), winnableFirst, board, sideToEvaluate,
          isKingOnlyNonFlagging, isKingOnlyFlagging);
      if (winnableFirst.winnable() == Winnable.UNKNOWN && PawnWall.calculateHasPawnWall(board)) {
        return Winnable.NO;
      }
      return winnableFirst.winnable();
    }

    // only one move, perform the move and see what's next, then unperform!
    final MoveRepresentation moveRepresentation = NonNullWrapperCommon.get(board.getLegalMovesRepresentation(), 0);
    board.performMove(moveRepresentation.moveSpecification());
    final var numberOfSecondHalfMoves = board.getLegalMovesRepresentation().size();
    final GameMultipleAnalysis evaluationSecond = evaluateOneMove(board);
    board.unperformMove();
    final WinnableAnalysis winnableSecond = calculateWinnableMultiple(evaluationSecond, sideToEvaluate);
    boolean isReturnSecond;
    if (numberOfSecondHalfMoves >= 2) {
      // we are not looking at multiple branches
      isReturnSecond = true;
    } else {
      // in face of only one first move we return only if already decided for both sides
      isReturnSecond = winnableSecond.winnable() != Winnable.UNKNOWN;
    }

    // logging winnable status for player not flagging
    if (isReturnSecond) {
      logResult(IS_DEBUG, "second;" + evaluationSecond.numberOfHalfMoves(), winnableSecond, board, sideToEvaluate, "na",
          "na");

      if (winnableSecond.winnable() == Winnable.UNKNOWN && PawnWall.calculateHasPawnWall(board)) {
        return Winnable.NO;
      }
      return winnableSecond.winnable();
    }

    // Example: Black has only forced move stalemating White and leaving with insufficient material at the same time
    // 8/7R/6pk/5p2/5bpK/8/r7/8 b - - 1 53
    // Example: Game outcome open
    // 7k/8/2b1p2K/pp2r3/2p5/8/8/6R1 w - - 0 42
    final GameForcedAnalysis evaluationForced = evaluateForced(board, sideToEvaluate);
    final WinnableAnalysis winnableForced = calculateWinnableForced(evaluationForced.gameStatus(), sideToEvaluate);

    // logging winnable status for player not flagging
    logResult(IS_DEBUG, "forced;" + evaluationForced.numberOfForcedHalfMoves(), winnableForced, board, sideToEvaluate,
        "na", "na");

    if (winnableForced.winnable() == Winnable.UNKNOWN && PawnWall.calculateHasPawnWall(board)) {
      return Winnable.NO;
    }
    return winnableForced.winnable();

  }

  private static WinnableAnalysis calculateWinnableMultiple(GameMultipleAnalysis evaluation, Side sideToEvaluate) {
    final Set<GameStatusAnalysis> gameStatusSet = evaluation.gameStatusSet();

    if (gameStatusSet.size() == 1) {
      final GameStatusAnalysis singleGameStatus = BasicUtility.getFirstElement(gameStatusSet);

      switch (singleGameStatus) {
        case WHITE_DELIVERS_CHECKMATE:
          return switch (sideToEvaluate) {
            case WHITE -> new WinnableAnalysis(Winnable.UNKNOWN, gameStatusSet);
            case BLACK -> new WinnableAnalysis(Winnable.NO, gameStatusSet);
            case NONE -> throw new IllegalArgumentException();
            default -> throw new IllegalArgumentException();
          };
        case BLACK_DELIVERS_CHECKMATE:
          return switch (sideToEvaluate) {
            case WHITE -> new WinnableAnalysis(Winnable.NO, gameStatusSet);
            case BLACK -> new WinnableAnalysis(Winnable.UNKNOWN, gameStatusSet);
            case NONE -> throw new IllegalArgumentException();
            default -> throw new IllegalArgumentException();
          };
        case STALEMATE:
          return new WinnableAnalysis(Winnable.NO, gameStatusSet);
        case INSUFFICIENT_MATERIAL_BOTH:
          return new WinnableAnalysis(Winnable.NO, gameStatusSet);
        case INSUFFICIENT_MATERIAL_WHITE_ONLY:
          if (sideToEvaluate == Side.WHITE) {
            return new WinnableAnalysis(Winnable.NO, gameStatusSet);
          }
          return new WinnableAnalysis(Winnable.UNKNOWN, gameStatusSet);
        case INSUFFICIENT_MATERIAL_BLACK_ONLY:
          if (sideToEvaluate == Side.BLACK) {
            return new WinnableAnalysis(Winnable.NO, gameStatusSet);
          }
          return new WinnableAnalysis(Winnable.UNKNOWN, gameStatusSet);
        case FIVE_FOLD_REPETITION_RULE:
          return new WinnableAnalysis(Winnable.NO, gameStatusSet);
        case SEVENTY_FIVE_MOVE_RULE:
          return new WinnableAnalysis(Winnable.NO, gameStatusSet);
        case OTHER:
          return new WinnableAnalysis(Winnable.UNKNOWN, gameStatusSet);
        default:
          throw new IllegalArgumentException();
      }
    }
    if (sideToEvaluate == Side.WHITE && GAME_WHITE_UNWINNABLE_SET.containsAll(gameStatusSet)
        || sideToEvaluate == Side.BLACK && GAME_BLACK_UNWINNABLE_SET.containsAll(gameStatusSet)) {
      return new WinnableAnalysis(Winnable.NO, gameStatusSet);
    }
    return new WinnableAnalysis(Winnable.UNKNOWN, gameStatusSet);
  }

  private static WinnableAnalysis calculateWinnableForced(GameStatusAnalysis status, Side sideToEvaluate) {
    switch (status) {
      case WHITE_DELIVERS_CHECKMATE:
        return switch (sideToEvaluate) {
          case WHITE -> createWinnableAnalysis(Winnable.UNKNOWN, status);
          case BLACK -> createWinnableAnalysis(Winnable.NO, status);
          case NONE -> throw new IllegalArgumentException();
          default -> throw new IllegalArgumentException();
        };
      case BLACK_DELIVERS_CHECKMATE:
        return switch (sideToEvaluate) {
          case WHITE -> createWinnableAnalysis(Winnable.NO, status);
          case BLACK -> createWinnableAnalysis(Winnable.UNKNOWN, status);
          case NONE -> throw new IllegalArgumentException();
          default -> throw new IllegalArgumentException();
        };
      case STALEMATE:
        return createWinnableAnalysis(Winnable.NO, status);
      case INSUFFICIENT_MATERIAL_BOTH:
        return createWinnableAnalysis(Winnable.NO, status);
      case INSUFFICIENT_MATERIAL_WHITE_ONLY:
        if (sideToEvaluate == Side.WHITE) {
          return createWinnableAnalysis(Winnable.NO, status);
        }
        break;
      case INSUFFICIENT_MATERIAL_BLACK_ONLY:
        if (sideToEvaluate == Side.BLACK) {
          return createWinnableAnalysis(Winnable.NO, status);
        }
        break;
      case FIVE_FOLD_REPETITION_RULE:
        return createWinnableAnalysis(Winnable.NO, status);
      case SEVENTY_FIVE_MOVE_RULE:
        return createWinnableAnalysis(Winnable.NO, status);
      case OTHER:
        // we don't know anything about the possible game outcome
        break;
      default:
        throw new IllegalArgumentException();
    }
    return createWinnableAnalysis(Winnable.UNKNOWN, status);
  }

  private static WinnableAnalysis createWinnableAnalysis(Winnable winnable, GameStatusAnalysis gameStatusAnalysis) {
    final Set<GameStatusAnalysis> gameStatusSet = new TreeSet<>();
    gameStatusSet.add(gameStatusAnalysis);
    return new WinnableAnalysis(winnable, gameStatusSet);
  }

  private static GameMultipleAnalysis evaluateOneMove(ApiBoard board) {

    final Set<GameStatusAnalysis> gameTermination = new TreeSet<>();
    final var numberOfHalfMoves = board.getLegalMovesRepresentation().size();

    final Side sidePerformingTheMove = board.getHavingMove();

    for (final MoveRepresentation moveRepresentation : board.getLegalMovesRepresentation()) {
      board.performMove(moveRepresentation.moveSpecification());
      if (board.isCheckmate()) {
        switch (sidePerformingTheMove) {
          case WHITE:
            gameTermination.add(GameStatusAnalysis.WHITE_DELIVERS_CHECKMATE);
            break;
          case BLACK:
            gameTermination.add(GameStatusAnalysis.BLACK_DELIVERS_CHECKMATE);
            break;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      } else if (board.isStalemate()) {
        gameTermination.add(GameStatusAnalysis.STALEMATE);
      } else if (board.isInsufficientMaterial()) {
        gameTermination.add(GameStatusAnalysis.INSUFFICIENT_MATERIAL_BOTH);
      } else if (board.isFivefoldRepetition()) {
        gameTermination.add(GameStatusAnalysis.FIVE_FOLD_REPETITION_RULE);
      } else if (board.isSeventyFiftyMove()) {
        gameTermination.add(GameStatusAnalysis.SEVENTY_FIVE_MOVE_RULE);
      } else if (board.isInsufficientMaterial(Side.WHITE)) {
        gameTermination.add(GameStatusAnalysis.INSUFFICIENT_MATERIAL_WHITE_ONLY);
      } else if (board.isInsufficientMaterial(Side.BLACK)) {
        gameTermination.add(GameStatusAnalysis.INSUFFICIENT_MATERIAL_BLACK_ONLY);
      } else {
        gameTermination.add(GameStatusAnalysis.OTHER);
      }
      board.unperformMove();
    }

    return new GameMultipleAnalysis(gameTermination, numberOfHalfMoves, board.getHavingMove());
  }

  private static GameForcedAnalysis evaluateForced(ApiBoard board, Side sideToEvaluate) {
    // we check position after series of forced moves
    // we cannot use early returns for after evaluation we need to undo the moves
    var countForcedHalfMoves = 0;
    var evaluation = GameStatusAnalysis.OTHER;
    while (board.getLegalMovesRepresentation().size() == 1) {
      countForcedHalfMoves++;
      final MoveRepresentation moveRepresentation = NonNullWrapperCommon.get(board.getLegalMovesRepresentation(), 0);
      board.performMove(moveRepresentation.moveSpecification());
      evaluation = calculateGameStatusAnalysis(board, sideToEvaluate);
      if (evaluation != GameStatusAnalysis.OTHER) {
        break;
      }
    }

    // undo moves
    for (var i = 1; i <= countForcedHalfMoves; i++) {
      board.unperformMove();
    }

    return new GameForcedAnalysis(evaluation, countForcedHalfMoves);
  }

  private static GameStatusAnalysis calculateGameStatusAnalysis(ApiBoard board, Side sideToEvaluate) {
    if (board.isCheckmate()) {
      if (board.getHavingMove() == Side.WHITE) {
        return GameStatusAnalysis.BLACK_DELIVERS_CHECKMATE;
      }
      return GameStatusAnalysis.WHITE_DELIVERS_CHECKMATE;
    }
    if (board.isStalemate()) {
      return GameStatusAnalysis.STALEMATE;
    }
    if (board.isInsufficientMaterial()) {
      return GameStatusAnalysis.INSUFFICIENT_MATERIAL_BOTH;
    }
    if (board.isFivefoldRepetition()) {
      return GameStatusAnalysis.FIVE_FOLD_REPETITION_RULE;
    }
    if (board.isSeventyFiftyMove()) {
      return GameStatusAnalysis.SEVENTY_FIVE_MOVE_RULE;
    }
    if (board.isInsufficientMaterial(sideToEvaluate)) {
      return switch (sideToEvaluate) {
        case WHITE -> GameStatusAnalysis.INSUFFICIENT_MATERIAL_WHITE_ONLY;
        case BLACK -> GameStatusAnalysis.INSUFFICIENT_MATERIAL_BLACK_ONLY;
        case NONE -> throw new IllegalArgumentException();
        default -> throw new IllegalArgumentException();
      };
    }
    return GameStatusAnalysis.OTHER;
  }

  private static void logResult(boolean isLogResult, String message, WinnableAnalysis winnable, ApiBoard board,
      Side sideToEvaluate, String isKingOnlyNonFlagging, String isKingOnlyFlagging) {
    // we need the non flagging player for the lichess examples
    if (isLogResult && sideToEvaluate == board.getHavingMove().getOppositeSide()) {
      logger.printf(Level.INFO, ";%s;%s;%s;%s;%s;%s", message, winnable.winnable(), winnable.gameStatusSet(),
          board.getFen(), isKingOnlyNonFlagging, isKingOnlyFlagging);
    }
  }
}
