package com.dlb.chess.generate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.DynamicPosition;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.utility.RandomUtility;
import com.dlb.chess.test.apicomparison.enums.FindRandomGame;
import com.dlb.chess.unwinnability.quick.enums.DeadPositionQuick;

public class GenerateRandomGame {

  private static final boolean IS_RUN_ALL = false;

  public static void main(final String[] args) {
    generateFivefold();

    if (IS_RUN_ALL) {
      generateNoRepetition();
      generateRandomGame(FindRandomGame.STALEMATE);
      generateSeventyFive();
      generateThreefold();
      generateFivefold();
    }
  }

  private static void generateRandomGame(FindRandomGame findRandomGame) {
    final ApiBoard board = new Board();

    Set<MoveSpecification> legalMoveSet = board.getPossibleMoveSpecificationSet();
    var numberOfMoveOptions = legalMoveSet.size();
    List<MoveSpecification> moveOptionList = new ArrayList<>(legalMoveSet);

    var halfMoveNumberLastPossibleTermination = -1;
    var numberOfHalfMovesPerformed = 0;
    while (numberOfMoveOptions != 0) {
      if (numberOfMoveOptions != 0) {
        final var randomMoveNumberIndex = RandomUtility.calculateRandomNumber(0, numberOfMoveOptions - 1);
        final MoveSpecification moveSpecification = NonNullWrapperCommon.get(moveOptionList, randomMoveNumberIndex);
        board.performMove(moveSpecification);
        numberOfHalfMovesPerformed++;
        if (numberOfHalfMovesPerformed == 1 || numberOfHalfMovesPerformed % 100 == 0) {
          System.out.println("Number of half-moves performed: " + numberOfHalfMovesPerformed);
        }
        legalMoveSet = board.getPossibleMoveSpecificationSet();
      }

      moveOptionList = new ArrayList<>();
      for (final MoveSpecification moveSpecification : legalMoveSet) {
        board.performMove(moveSpecification);
        switch (findRandomGame) {
          case CHECKMATE:
            if (board.isCheckmate()) {
              halfMoveNumberLastPossibleTermination = numberOfHalfMovesPerformed + 1;
              System.out.println("Found checkmate option for half-move " + halfMoveNumberLastPossibleTermination);
            }
            break;
          case FIFTY_MOVE_RULE:
            if (board.isFiftyMove()) {
              halfMoveNumberLastPossibleTermination = numberOfHalfMovesPerformed + 1;
              System.out.println("Found fifty-move rule option for half-move " + halfMoveNumberLastPossibleTermination);
            }
            break;
          case INSUFFICIENT_MATERIAL:
            if (board.isInsufficientMaterial()) {
              halfMoveNumberLastPossibleTermination = numberOfHalfMovesPerformed + 1;
              System.out
                  .println("Found insufficient material option for half-move " + halfMoveNumberLastPossibleTermination);
            }
            break;
          case STALEMATE:
            if (board.isStalemate()) {
              halfMoveNumberLastPossibleTermination = numberOfHalfMovesPerformed + 1;
              System.out.println("Found stalemate option for half-move " + halfMoveNumberLastPossibleTermination);
            }
            break;
          case THREEFOLD_REPETITION_RULE:
            // not implemented yet
            throw new IllegalArgumentException();
          default:
            throw new IllegalArgumentException();

        }
        if (!board.isCheckmate() && !board.isStalemate()
            && board.isDeadPositionQuick() != DeadPositionQuick.DEAD_POSITION && board.getRepetitionCount() == 1
            && !board.isFiftyMove()) {
          moveOptionList.add(moveSpecification);
        }
        board.unperformMove();
      }
      numberOfMoveOptions = moveOptionList.size();
    }

    if (halfMoveNumberLastPossibleTermination != -1) {
      System.out.println("No more non terminating moves - we have a termination option");
      for (var i = numberOfHalfMovesPerformed; i >= halfMoveNumberLastPossibleTermination; i--) {
        board.unperformMove();
      }
      // now we should have at least one checkmate move
      // perform first found
      legalMoveSet = board.getPossibleMoveSpecificationSet();
      for (final MoveSpecification moveSpecification : legalMoveSet) {
        board.performMove(moveSpecification);

        var isTerminationMoveFound = false;
        switch (findRandomGame) {
          case CHECKMATE:
            if (board.isCheckmate()) {
              isTerminationMoveFound = true;
            }
            break;
          case FIFTY_MOVE_RULE:
            if (board.isFiftyMove()) {
              isTerminationMoveFound = true;
            }
            break;
          case INSUFFICIENT_MATERIAL:
            if (board.isInsufficientMaterial()) {
              isTerminationMoveFound = true;
            }
            break;
          case STALEMATE:
            if (board.isStalemate()) {
              isTerminationMoveFound = true;
            }
            break;
          case THREEFOLD_REPETITION_RULE:
            // not implemented yet
            throw new IllegalArgumentException();
          default:
            throw new IllegalArgumentException();

        }
        if (isTerminationMoveFound) {
          System.out.println("A game with " + board.getPerformedHalfMoveCount() / 2.0 + " moves ending in "
              + findRandomGame + " was generated");
          final String moveList = calculateMoveList(board.getHalfMoveList());
          System.out.println(moveList);
          break;
        }
        board.unperformMove();
      }
    } else {
      System.out.println("No more non terminating moves - no termination option");
    }
  }

  private static void generateNoRepetition() {
    final ApiBoard board = new Board();

    Set<MoveSpecification> legalMoveSet = board.getPossibleMoveSpecificationSet();
    var numberOfMoveOptions = legalMoveSet.size();
    List<MoveSpecification> moveOptionList = new ArrayList<>(legalMoveSet);

    var numberOfHalfMovesPerformed = 0;
    while (numberOfMoveOptions != 0) {
      if (numberOfMoveOptions != 0) {
        final var randomMoveNumberIndex = RandomUtility.calculateRandomNumber(0, numberOfMoveOptions - 1);
        final MoveSpecification moveSpecification = NonNullWrapperCommon.get(moveOptionList, randomMoveNumberIndex);
        board.performMove(moveSpecification);
        numberOfHalfMovesPerformed++;
        if (numberOfHalfMovesPerformed == 1 || numberOfHalfMovesPerformed % 100 == 0) {
          System.out.println("Number of half-moves performed: " + numberOfHalfMovesPerformed);
        }
        legalMoveSet = board.getPossibleMoveSpecificationSet();
      }

      moveOptionList = new ArrayList<>();
      for (final MoveSpecification moveSpecification : legalMoveSet) {
        board.performMove(moveSpecification);
        if (!board.isCheckmate() && !board.isStalemate()
            && board.isDeadPositionQuick() != DeadPositionQuick.DEAD_POSITION && board.getRepetitionCount() == 1) {
          moveOptionList.add(moveSpecification);
        }
        board.unperformMove();
      }
      numberOfMoveOptions = moveOptionList.size();
    }

    System.out.println("A game with " + board.getPerformedHalfMoveCount() / 2.0 + " moves was generated");
    final String moveList = calculateMoveList(board.getHalfMoveList());
    System.out.println(moveList);
  }

  // we only want one sequence over 50, so after passing 50 first time, we try to reach 75 and if not successful that's
  // it, then try again
  private static void generateSeventyFive() {
    final ApiBoard board = new Board();

    Set<MoveSpecification> legalMoveSet = board.getPossibleMoveSpecificationSet();
    var numberOfMoveOptions = legalMoveSet.size();
    List<MoveSpecification> moveOptionList = new ArrayList<>(legalMoveSet);

    var isFiftyReached = false;
    var numberOfHalfMovesPerformed = 0;
    while (numberOfMoveOptions != 0) {
      if (numberOfMoveOptions != 0) {
        final var randomMoveNumberIndex = RandomUtility.calculateRandomNumber(0, numberOfMoveOptions - 1);
        final MoveSpecification moveSpecification = NonNullWrapperCommon.get(moveOptionList, randomMoveNumberIndex);
        board.performMove(moveSpecification);
        if (!isFiftyReached && board.isFiftyMove()) {
          isFiftyReached = true;
          System.out.println("Reached fifty move");
        }
        numberOfHalfMovesPerformed++;
        if (numberOfHalfMovesPerformed == 1 || numberOfHalfMovesPerformed % 100 == 0) {
          System.out.println("Number of half-moves performed: " + numberOfHalfMovesPerformed);
        }

        if (board.isSeventyFiftyMove()) {
          System.out.println("A game with " + board.getPerformedHalfMoveCount() / 2.0
              + " moves ending with seventy-five-move rule was generated");
          final String moveList = calculateMoveList(board.getHalfMoveList());
          System.out.println(moveList);
          return;
        }
        legalMoveSet = board.getPossibleMoveSpecificationSet();
      }

      moveOptionList = new ArrayList<>();
      for (final MoveSpecification moveSpecification : legalMoveSet) {
        board.performMove(moveSpecification);
        if (!board.isCheckmate() && !board.isStalemate()
            && board.isDeadPositionQuick() == DeadPositionQuick.DEAD_POSITION && board.getRepetitionCount() == 1
            && (!isFiftyReached || board.isFiftyMove())) {
          moveOptionList.add(moveSpecification);
        }
        board.unperformMove();
      }
      numberOfMoveOptions = moveOptionList.size();
    }

    System.out.println("Could not generate game ending with seventy-five-move rule");

  }

  private static void generateThreefold() {
    generateRepetition(ChessConstants.THREEFOLD_REPETITION_RULE_THRESHOLD, 100);
  }

  private static void generateFivefold() {
    generateRepetition(ChessConstants.FIVEFOLD_REPETITION_RULE_THRESHOLD, 100);
  }

  private static void generateRepetition(int repetitionNumber, int numberOfTries) {
    for (var i = 1; i <= numberOfTries; i++) {
      System.out.println("Try " + i + " of " + numberOfTries);
      if (generateRepetition(repetitionNumber)) {
        System.out.println("Find result");
        break;
      }
    }
  }

  private static boolean generateRepetition(int repetitionNumber) {
    final ApiBoard board = new Board();

    Set<MoveSpecification> legalMoveSet = board.getPossibleMoveSpecificationSet();
    var numberOfMoveOptions = legalMoveSet.size();
    List<MoveSpecification> moveOptionList = new ArrayList<>(legalMoveSet);

    var isRepetitionReached = false;
    // we need to intialize to something other than null, the below value is not meaning ful
    DynamicPosition repetitionPosition = board.getDynamicPosition();
    var numberOfHalfMovesPerformed = 0;
    while (numberOfMoveOptions != 0) {
      {
        final var randomMoveNumberIndex = RandomUtility.calculateRandomNumber(0, numberOfMoveOptions - 1);
        final MoveSpecification moveSpecification = NonNullWrapperCommon.get(moveOptionList, randomMoveNumberIndex);
        board.performMove(moveSpecification);
        if (!isRepetitionReached && board.getRepetitionCount() == 2) {
          isRepetitionReached = true;
          repetitionPosition = board.getDynamicPosition();
          System.out.println("Reached first repetition");
        }
        numberOfHalfMovesPerformed++;
        if (numberOfHalfMovesPerformed == 1 || numberOfHalfMovesPerformed % 100 == 0) {
          System.out.println("Number of half-moves performed: " + numberOfHalfMovesPerformed);
        }

        if (board.getRepetitionCount() == repetitionNumber) {
          System.out.println("A game with " + board.getPerformedHalfMoveCount() / 2.0 + " moves ending with "
              + repetitionNumber + " repetitions was generated");
          final String moveList = calculateMoveList(board.getHalfMoveList());
          System.out.println(moveList);
          return true;
        }
        legalMoveSet = board.getPossibleMoveSpecificationSet();
      }

      moveOptionList = new ArrayList<>();

      // try to speed up
      // if we find continuation we use it, otherwise we never hit more than three using random moves
      for (final MoveSpecification moveSpecification : legalMoveSet) {
        board.performMove(moveSpecification);
        if (repetitionPosition.equals(board.getDynamicPosition())) {
          moveOptionList.add(moveSpecification);
        }
        board.unperformMove();
      }
      if (moveOptionList.isEmpty()) {
        // means we have no continuation from 3 onwards found
        for (final MoveSpecification moveSpecification : legalMoveSet) {
          board.performMove(moveSpecification);
          if (!board.isCheckmate() && !board.isStalemate()
              && board.isDeadPositionQuick() == DeadPositionQuick.DEAD_POSITION && !board.isFiftyMove()) {
            moveOptionList.add(moveSpecification);
          }
          board.unperformMove();
        }
      }
      numberOfMoveOptions = moveOptionList.size();
    }

    System.out.println("Could not generate game ending with " + repetitionNumber + " repetitions");
    return false;
  }

  private static String calculateMoveList(List<HalfMove> halfMoveList) {
    final StringBuilder moveList = new StringBuilder();
    for (var i = 0; i < halfMoveList.size(); i++) {
      final HalfMove halfMove = NonNullWrapperCommon.get(halfMoveList, i);
      // after black move if following white move
      if (i > 0 && i % 2 == 0) {
        moveList.append(" ");
      }
      if (halfMove.moveSpecification().havingMove().getIsWhite()) {
        moveList.append(halfMove.fullMoveNumber() + ". ");
      } else if (halfMove.moveSpecification().havingMove().getIsBlack()) {
        moveList.append(" ");
      } else {
        throw new ProgrammingMistakeException("That should never happen");
      }
      moveList.append(halfMove.san());
    }

    return NonNullWrapperCommon.toString(moveList);

  }

}
