package com.dlb.chess.test.pgntest.basic;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;

class TestBasicCheckBlack extends AbstractTestBasic {

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_black_check_rook_direct_adjacent.pgn");
    pgnFileNameList.add("02_black_check_rook_direct_range.pgn");
    pgnFileNameList.add("03_black_check_rook_discover.pgn");
    pgnFileNameList.add("04_black_check_knight_direct.pgn");
    pgnFileNameList.add("05_black_check_knight_discover_orthogonal.pgn");
    pgnFileNameList.add("06_black_check_knight_discover_diagonal.pgn");
    pgnFileNameList.add("07_black_check_bishop_direct_adjacent.pgn");
    pgnFileNameList.add("08_black_check_bishop_direct_range.pgn");
    pgnFileNameList.add("09_black_check_bishop_discover.pgn");
    pgnFileNameList.add("10_black_check_queen_direct_orthogonal_adjacent.pgn");
    pgnFileNameList.add("11_black_check_queen_direct_orthogonal_range.pgn");
    pgnFileNameList.add("12_black_check_queen_direct_diagonal_adjacent.pgn");
    pgnFileNameList.add("13_black_check_queen_direct_diagonal_range.pgn");
    pgnFileNameList.add("14_black_check_king_discover_orthogonal.pgn");
    pgnFileNameList.add("15_black_check_king_discover_diagonal.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_CHECK_BLACK);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.BASIC_CHECK_BLACK);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = GeneralUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_black_check_rook_direct_adjacent.pgn":
          checkNonCaptureCheck(BLACK, C6, C4, BLACK_ROOK, board);
          break;
        case "02_black_check_rook_direct_range.pgn":
          checkNonCaptureCheck(BLACK, A4, A3, BLACK_ROOK, board);
          break;
        case "03_black_check_rook_discover.pgn":
          checkNonCaptureCheck(BLACK, B4, B7, BLACK_ROOK, board);
          break;
        case "04_black_check_knight_direct.pgn":
          checkNonCaptureCheck(BLACK, D4, F3, BLACK_KNIGHT, board);
          break;
        case "05_black_check_knight_discover_orthogonal.pgn":
          checkNonCaptureCheck(BLACK, E5, G6, BLACK_KNIGHT, board);
          break;
        case "06_black_check_knight_discover_diagonal.pgn":
          checkNonCaptureCheck(BLACK, C5, E6, BLACK_KNIGHT, board);
          break;
        case "07_black_check_bishop_direct_adjacent.pgn":
          checkNonCaptureCheck(BLACK, C5, F2, BLACK_BISHOP, board);
          break;
        case "08_black_check_bishop_direct_range.pgn":
          checkNonCaptureCheck(BLACK, C8, B7, BLACK_BISHOP, board);
          break;
        case "09_black_check_bishop_discover.pgn":
          checkNonCaptureCheck(BLACK, E6, G4, BLACK_BISHOP, board);
          break;
        case "10_black_check_queen_direct_orthogonal_adjacent.pgn":
          checkNonCaptureCheck(BLACK, G4, E2, BLACK_QUEEN, board);
          break;
        case "11_black_check_queen_direct_orthogonal_range.pgn":
          checkNonCaptureCheck(BLACK, D6, E5, BLACK_QUEEN, board);
          break;
        case "12_black_check_queen_direct_diagonal_adjacent.pgn":
          checkNonCaptureCheck(BLACK, G5, D2, BLACK_QUEEN, board);
          break;
        case "13_black_check_queen_direct_diagonal_range.pgn":
          checkNonCaptureCheck(BLACK, D8, B6, BLACK_QUEEN, board);
          break;
        case "14_black_check_king_discover_orthogonal.pgn":
          checkNonCaptureCheck(BLACK, E7, D6, BLACK_KING, board);
          break;
        case "15_black_check_king_discover_diagonal.pgn":
          checkNonCaptureCheck(BLACK, G5, G4, BLACK_KING, board);
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
  }

}
