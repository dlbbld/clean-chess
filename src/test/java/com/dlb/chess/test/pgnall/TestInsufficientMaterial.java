package com.dlb.chess.test.pgnall;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.InsufficientMaterialUtility;
import com.dlb.chess.common.utility.MaterialUtility;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.reader.PgnStrictCacheForTestCases;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestInsufficientMaterial implements EnumConstants {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestInsufficientMaterial.class);

  @SuppressWarnings("static-method")
  @Test
  void testPgnFiles() throws Exception {
    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getRestrictedTestListList()) {
      if (PgnTestConstants.IS_RESTRICT_INSUFFICIENT_MATERIAL_TEST) {
        switch (testCaseList.pgnTest()) {
          case BASIC_INSUFFICIENT_MATERIAL:
          case BASIC_CHECK_WHITE:
          case BASIC_CHECK_BLACK:
          case BASIC_CHECKMATE_WHITE:
          case BASIC_CHECKMATE_BLACK:
          case BASIC_STALEMATE:
          case BASIC_FROM_FEN:
            break;
          // $CASES-OMITTED$
          default:
            continue;
        }
      }
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        checkInsufficientMaterial(testCaseList.pgnTest().getFolderPath(), testCase.pgnFileName());
      }
    }
  }

  private static void checkInsufficientMaterial(String folderPath, String pgnFileName) throws Exception {

    logger.info(pgnFileName);

    final PgnFile pgnFile = PgnStrictCacheForTestCases.getPgn(folderPath, pgnFileName);
    final ApiBoard boardFromFen = new Board(pgnFile.startFen());
    for (final PgnHalfMove halfMove : pgnFile.halfMoveList()) {
      boardFromFen.performMove(halfMove.san());
    }

    final var isInsufficientMaterialDirectlyCalculated = calculateIsInsufficientMaterial(
        boardFromFen.getStaticPosition());
    final var isInsufficientMaterialDerived = boardFromFen.isInsufficientMaterial(Side.WHITE)
        && boardFromFen.isInsufficientMaterial(Side.BLACK);

    assertEquals(isInsufficientMaterialDirectlyCalculated, isInsufficientMaterialDerived);

  }

  private static boolean calculateIsInsufficientMaterial(StaticPosition staticPosition) {

    // KNvK, KvKN
    if (MaterialUtility.calculateIsKingAndKnightOnly(WHITE, staticPosition)
        && MaterialUtility.calculateIsKingOnly(BLACK, staticPosition)
        || MaterialUtility.calculateIsKingOnly(WHITE, staticPosition)
            && MaterialUtility.calculateIsKingAndKnightOnly(BLACK, staticPosition)) {
      return true;
    }

    // K(B^lightSquares)*vK(B^lightSquares)*, K(B^darkSquares)*vK(B^darkSquares)* (includes KvK)
    if (InsufficientMaterialUtility.calculateHasZeroOrMultipleLightSquareBishopOnly(WHITE, staticPosition)
        && InsufficientMaterialUtility.calculateHasZeroOrMultipleLightSquareBishopOnly(BLACK, staticPosition)
        || InsufficientMaterialUtility.calculateHasZeroOrMultipleDarkSquareBishopOnly(WHITE, staticPosition)
            && InsufficientMaterialUtility.calculateHasZeroOrMultipleDarkSquareBishopOnly(BLACK, staticPosition)) {
      return true;
    }

    return false;
  }

}
