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

class TestBasicMovingPieceBlack extends AbstractTestBasic {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBasicMovingPieceBlack.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_black_moving_rook.pgn");
    pgnFileNameList.add("02_black_moving_knight.pgn");
    pgnFileNameList.add("03_black_moving_bishop.pgn");
    pgnFileNameList.add("04_black_moving_queen.pgn");
    pgnFileNameList.add("05_black_moving_king.pgn");
    pgnFileNameList.add("06_black_moving_pawn.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_MOVING_PIECE_BLACK);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.BASIC_MOVING_PIECE_BLACK);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = GeneralUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_black_moving_rook.pgn":
          checkMovingPiece(BLACK, H8, H7, BLACK_ROOK, board);
          break;
        case "02_black_moving_knight.pgn":
          checkMovingPiece(BLACK, G8, F6, BLACK_KNIGHT, board);
          break;
        case "03_black_moving_bishop.pgn":
          checkMovingPiece(BLACK, F8, G7, BLACK_BISHOP, board);
          break;
        case "04_black_moving_queen.pgn":
          checkMovingPiece(BLACK, D8, H4, BLACK_QUEEN, board);
          break;
        case "05_black_moving_king.pgn":
          checkMovingPiece(BLACK, E8, E7, BLACK_KING, board);
          break;
        case "06_black_moving_pawn.pgn":
          checkMovingPiece(BLACK, E7, E5, BLACK_PAWN, board);
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
  }
}
