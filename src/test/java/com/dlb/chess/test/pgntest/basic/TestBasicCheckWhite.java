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

class TestBasicCheckWhite extends AbstractTestBasic {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBasicCheckWhite.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_white_check_rook_direct_adjacent.pgn");
    pgnFileNameList.add("02_white_check_rook_direct_range.pgn");
    pgnFileNameList.add("03_white_check_rook_discover.pgn");
    pgnFileNameList.add("04_white_check_knight_direct.pgn");
    pgnFileNameList.add("05_white_check_knight_discover_orthogonal.pgn");
    pgnFileNameList.add("06_white_check_knight_discover_diagonal.pgn");
    pgnFileNameList.add("07_white_check_bishop_direct_adjacent.pgn");
    pgnFileNameList.add("08_white_check_bishop_direct_range.pgn");
    pgnFileNameList.add("09_white_check_bishop_discover.pgn");
    pgnFileNameList.add("10_white_check_queen_direct_orthogonal_adjacent.pgn");
    pgnFileNameList.add("11_white_check_queen_direct_orthogonal_range.pgn");
    pgnFileNameList.add("12_white_check_queen_direct_diagonal_adjacent.pgn");
    pgnFileNameList.add("13_white_check_queen_direct_diagonal_range.pgn");
    pgnFileNameList.add("14_white_check_king_discover_orthogonal.pgn");
    pgnFileNameList.add("15_white_check_king_discover_diagonal.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_CHECK_WHITE);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.BASIC_CHECK_WHITE);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = GeneralUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_white_check_rook_direct_adjacent.pgn":
          checkNonCaptureCheck(WHITE, D3, D6, WHITE_ROOK, board);
          break;
        case "02_white_check_rook_direct_range.pgn":
          checkNonCaptureCheck(WHITE, B3, B6, WHITE_ROOK, board);
          break;
        case "03_white_check_rook_discover.pgn":
          checkNonCaptureCheck(WHITE, B5, D5, WHITE_ROOK, board);
          break;
        case "04_white_check_knight_direct.pgn":
          checkNonCaptureCheck(WHITE, D5, F6, WHITE_KNIGHT, board);
          break;
        case "05_white_check_knight_discover_orthogonal.pgn":
          checkNonCaptureCheck(WHITE, D5, B6, WHITE_KNIGHT, board);
          break;
        case "06_white_check_knight_discover_diagonal.pgn":
          checkNonCaptureCheck(WHITE, B5, A3, WHITE_KNIGHT, board);
          break;
        case "07_white_check_bishop_direct_adjacent.pgn":
          checkNonCaptureCheck(WHITE, C4, F7, WHITE_BISHOP, board);
          break;
        case "08_white_check_bishop_direct_range.pgn":
          checkNonCaptureCheck(WHITE, F1, B5, WHITE_BISHOP, board);
          break;
        case "09_white_check_bishop_discover.pgn":
          checkNonCaptureCheck(WHITE, B5, C4, WHITE_BISHOP, board);
          break;
        case "10_white_check_queen_direct_orthogonal_adjacent.pgn":
          checkNonCaptureCheck(WHITE, D6, E7, WHITE_QUEEN, board);
          break;
        case "11_white_check_queen_direct_orthogonal_range.pgn":
          checkNonCaptureCheck(WHITE, D2, E3, WHITE_QUEEN, board);
          break;
        case "12_white_check_queen_direct_diagonal_adjacent.pgn":
          checkNonCaptureCheck(WHITE, B3, F7, WHITE_QUEEN, board);
          break;
        case "13_white_check_queen_direct_diagonal_range.pgn":
          checkNonCaptureCheck(WHITE, D1, H5, WHITE_QUEEN, board);
          break;
        case "14_white_check_king_discover_orthogonal.pgn":
          checkNonCaptureCheck(WHITE, E2, F3, WHITE_KING, board);
          break;
        case "15_white_check_king_discover_diagonal.pgn":
          checkNonCaptureCheck(WHITE, B5, B4, WHITE_KING, board);
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
  }

}
