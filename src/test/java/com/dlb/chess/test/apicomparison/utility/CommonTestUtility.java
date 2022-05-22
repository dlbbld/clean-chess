package com.dlb.chess.test.apicomparison.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ApiBoard;

public class CommonTestUtility implements EnumConstants {

  public static void checkBoardsAgainstEachOtherAll(ApiBoard boardFirst, ApiBoard boardSecond) {
    checkBoardsAgainstEachOther(boardFirst, boardSecond, true);
  }

  public static void checkBoardsAgainstEachOtherExcludeHistory(ApiBoard boardFirst, ApiBoard boardSecond) {
    checkBoardsAgainstEachOther(boardFirst, boardSecond, false);
  }

  // RE must be aligned with board API interface
  // Must contain all methods in API interface which are not implemented in AbstractBoard
  // that is also crucial performance wise because in AbstractBoard there are methods taking very long,
  // and testing against makes no sense as always true as long board implentation match, what we test here.
  private static void checkBoardsAgainstEachOther(ApiBoard boardFirst, ApiBoard boardSecond, boolean isIncludeHistory) {

    assertEquals(boardFirst.isCheck(), boardSecond.isCheck());
    assertEquals(boardFirst.isCheckmate(), boardSecond.isCheckmate());
    assertEquals(boardFirst.isStalemate(), boardSecond.isStalemate());

    assertEquals(boardFirst.isInsufficientMaterial(), boardSecond.isInsufficientMaterial());
    assertEquals(boardFirst.isInsufficientMaterial(WHITE), boardSecond.isInsufficientMaterial(WHITE));
    assertEquals(boardFirst.isInsufficientMaterial(BLACK), boardSecond.isInsufficientMaterial(BLACK));

    assertEquals(boardFirst.getHalfMoveClock(), boardSecond.getHalfMoveClock());

    assertEquals(boardFirst.getFullMoveNumber(), boardSecond.getFullMoveNumber());
    if (isIncludeHistory) {
      assertEquals(boardFirst.getInitialFenFullMoveNumber(), boardSecond.getInitialFenFullMoveNumber());
    }
    // in super
    // assertEquals(boardFirst.getFullMoveNumberForNextHalfMove(), boardSecond.getFullMoveNumberForNextHalfMove());

    if (isIncludeHistory) {
      assertEquals(boardFirst.getRepetitionCount(), boardSecond.getRepetitionCount());
    }

    assertEquals(boardFirst.isFiftyMove(), boardSecond.isFiftyMove());
    // extremely slow for performing and unperforming all legal moves per position
    assertEquals(boardFirst.canClaimFiftyMoveRuleWithOwnMove(), boardSecond.canClaimFiftyMoveRuleWithOwnMove());
    // extremely slow for performing and unperforming all legal moves per position
    assertEquals(boardFirst.canClaimFiftyMoveRule(), boardSecond.canClaimFiftyMoveRule());
    assertEquals(boardFirst.isSeventyFiftyMove(), boardSecond.isSeventyFiftyMove());

    if (isIncludeHistory) {
      assertEquals(boardFirst.isThreefoldRepetition(), boardSecond.isThreefoldRepetition());
      // extremely slow for performing and unperforming all legal moves per position
      // in super
      // assertEquals(boardFirst.canClaimThreefoldRepetitionRuleWithOwnMove(),
      // boardSecond.canClaimThreefoldRepetitionRuleWithOwnMove());
      // extremely slow for performing and unperforming all legal moves per position
      assertEquals(boardFirst.canClaimThreefoldRepetitionRule(), boardSecond.canClaimThreefoldRepetitionRule());
      assertEquals(boardFirst.isFivefoldRepetition(), boardSecond.isFivefoldRepetition());
    }

    // in super
    // assertEquals(boardFirst.calculateInsufficientMaterial(), boardSecond.calculateInsufficientMaterial());
    // in super
    // assertEquals(boardFirst.isDeadPosition(), boardSecond.isDeadPosition());
    // in super
    // assertEquals(boardFirst.isGameEnd(), boardSecond.isGameEnd());

    assertEquals(boardFirst.getFen(), boardSecond.getFen());

    if (isIncludeHistory) {
      assertEquals(boardFirst.getInitialFen(), boardSecond.getInitialFen());
    }

    if (!boardFirst.isFirstMove() && !boardSecond.isFirstMove()) {
      assertEquals(boardFirst.getSan(), boardSecond.getSan());
      assertEquals(boardFirst.getLan(), boardSecond.getLan());

      assertEquals(boardFirst.getMovingPiece(), boardSecond.getMovingPiece());
      assertEquals(boardFirst.isCapture(), boardSecond.isCapture());
    }

    assertEquals(boardFirst.getHavingMove(), boardSecond.getHavingMove());

    assertEquals(boardFirst.isEnPassantCapturePossible(), boardSecond.isEnPassantCapturePossible());

    assertEquals(boardFirst.getCastlingRightBoth(), boardSecond.getCastlingRightBoth());
    // in super
    // assertEquals(boardFirst.getCastlingRight(WHITE), boardSecond.getCastlingRight(WHITE));
    // in super
    // assertEquals(boardFirst.getCastlingRight(BLACK), boardSecond.getCastlingRight(BLACK));

    if (isIncludeHistory) {
      assertEquals(boardFirst.getPerformedHalfMoveCount(), boardSecond.getPerformedHalfMoveCount());
    }

    if (isIncludeHistory) {
      assertEquals(boardFirst.getDynamicPositionList(), boardSecond.getDynamicPositionList());
      assertEquals(boardFirst.getDynamicPosition(), boardSecond.getDynamicPosition());

      assertEquals(boardFirst.getHalfMoveList(), boardSecond.getHalfMoveList());
      assertEquals(boardFirst.getPossibleMoveSpecificationSet(), boardSecond.getPossibleMoveSpecificationSet());

      assertEquals(boardFirst.getLegalMoveSet(), boardSecond.getLegalMoveSet());
      assertEquals(boardFirst.getPerformedMoveSpecificationList(), boardSecond.getPerformedMoveSpecificationList());
      // in super
      // assertEquals(boardFirst.getLegalMovesRepresentation(), boardSecond.getLegalMovesRepresentation());
      assertEquals(boardFirst.getLegalMovesSan(), boardSecond.getLegalMovesSan());
    }

    // in super
    // assertEquals(boardFirst.getLegalMovesUci(), boardSecond.getLegalMovesUci());
    if (!boardFirst.isFirstMove() && !boardSecond.isFirstMove()) {
      assertEquals(boardFirst.getLastMove(), boardSecond.getLastMove());
    }

    assertEquals(boardFirst.getStaticPosition(), boardSecond.getStaticPosition());
    if (!boardFirst.isFirstMove() && !boardSecond.isFirstMove()) {
      assertEquals(boardFirst.getStaticPositionBeforeLastMove(), boardSecond.getStaticPositionBeforeLastMove());
    }

    if (isIncludeHistory) {
      assertEquals(boardFirst.isFirstMove(), boardSecond.isFirstMove());
    }

    assertEquals(boardFirst.getEnPassantCaptureTargetSquare(), boardSecond.getEnPassantCaptureTargetSquare());

  }

}
