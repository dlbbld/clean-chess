package com.dlb.chess.test.pgntest.basic;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;

class TestBasicMovingPieceWhite extends AbstractTestBasic {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBasicMovingPieceWhite.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_white_moving_rook.pgn");
    pgnFileNameList.add("02_white_moving_knight.pgn");
    pgnFileNameList.add("03_white_moving_bishop.pgn");
    pgnFileNameList.add("04_white_moving_queen.pgn");
    pgnFileNameList.add("05_white_moving_king.pgn");
    pgnFileNameList.add("06_white_moving_pawn.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_MOVING_PIECE_WHITE);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.BASIC_MOVING_PIECE_WHITE);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = GeneralUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_white_moving_rook.pgn":
          checkMovingPiece(WHITE, A1, A2, WHITE_ROOK, board);
          break;
        case "02_white_moving_knight.pgn":
          checkMovingPiece(WHITE, B1, C3, WHITE_KNIGHT, board);
          break;
        case "03_white_moving_bishop.pgn":
          checkMovingPiece(WHITE, C1, B2, WHITE_BISHOP, board);
          break;
        case "04_white_moving_queen.pgn":
          checkMovingPiece(WHITE, D1, D3, WHITE_QUEEN, board);
          break;
        case "05_white_moving_king.pgn":
          checkMovingPiece(WHITE, E1, E2, WHITE_KING, board);
          break;
        case "06_white_moving_pawn.pgn":
          checkMovingPiece(WHITE, E2, E4, WHITE_PAWN, board);
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
  }

}
