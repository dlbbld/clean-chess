package com.dlb.chess.board;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.PgnGame;
import com.dlb.chess.test.RestrictTestConstants;
import com.dlb.chess.test.model.PgnTestCase;
import com.dlb.chess.test.model.PgnTestCaseList;
import com.dlb.chess.test.pgn.parser.PgnCacheForStrictPgnParserTestCases;
import com.dlb.chess.test.pgn.setup.PgnTestCaseCatalog;

class TestInsufficientMaterial implements EnumConstants {

  private static final Logger logger = Nulls.getLogger(TestInsufficientMaterial.class);

  @SuppressWarnings("static-method")
  @Test
  void testPgnFiles() throws Exception {
    for (final PgnTestCaseList testCaseList : PgnTestCaseCatalog.getRestrictedTestListList()) {
      if (RestrictTestConstants.IS_RESTRICT_PGN_INSUFFICIENT_MATERIAL_TEST) {
        switch (testCaseList.pgnTest()) {
          case BASIC_INSUFFICIENT_MATERIAL_BOTH:
          case BASIC_INSUFFICIENT_MATERIAL_ONLY_WHITE:
          case BASIC_INSUFFICIENT_MATERIAL_ONLY_BLACK:
          case BASIC_INSUFFICIENT_MATERIAL_NONE:
          case BASIC_CHECK_WHITE:
          case BASIC_CHECK_BLACK:
          case BASIC_CHECKMATE_WHITE:
          case BASIC_CHECKMATE_BLACK:
          case BASIC_STALEMATE:
            break;
          // $CASES-OMITTED$
          default:
            continue;
        }
      }
      for (final PgnTestCase testCase : testCaseList.list()) {
        checkInsufficientMaterial(testCaseList.pgnTest().getFolderPath(), testCase.pgnFileName());
      }
    }
  }

  private static void checkInsufficientMaterial(Path folderPath, String pgnFileName) throws Exception {

    logger.info(pgnFileName);

    final PgnGame pgnGame = PgnCacheForStrictPgnParserTestCases.getPgn(folderPath, pgnFileName);
    // PGN replay disables dead-position-unwinnable-quick auto-detection. Some of these recorded games pass through
    // positions the quick analyzer would classify as dead before reaching the actually-insufficient-material final
    // position; the test cares about the mechanical insufficient-material check on the final state, not the
    // analyzer's prediction on intermediate positions.
    final Board boardFromFen = new Board(pgnGame.startFen(), false);
    for (final PgnHalfMove halfMove : pgnGame.halfMoveList()) {
      boardFromFen.moveStrict(halfMove.san());
    }

    final var isInsufficientMaterialDirectlyCalculated = calculateIsInsufficientMaterial(
        boardFromFen.getStaticPosition());
    final var isInsufficientMaterialDerived = boardFromFen.isInsufficientMaterial(Side.WHITE)
        && boardFromFen.isInsufficientMaterial(Side.BLACK);

    assertEquals(isInsufficientMaterialDirectlyCalculated, isInsufficientMaterialDerived);

  }

  private static boolean calculateIsInsufficientMaterial(StaticPosition staticPosition) {

    // KNvK, KvKN
    if (BoardMaterial.calculateHasKingAndKnightOnly(WHITE, staticPosition)
        && BoardMaterial.calculateHasKingOnly(BLACK, staticPosition)
        || BoardMaterial.calculateHasKingOnly(WHITE, staticPosition)
            && BoardMaterial.calculateHasKingAndKnightOnly(BLACK, staticPosition)
        || BoardMaterial.calculateHasKingOnly(WHITE, staticPosition)
            && BoardMaterial.calculateHasKingAndBishopOnly(BLACK, staticPosition)
        || BoardMaterial.calculateHasKingOnly(WHITE, staticPosition)
            && BoardMaterial.calculateHasKingAndBishopOnly(BLACK, staticPosition)

    ) {
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
