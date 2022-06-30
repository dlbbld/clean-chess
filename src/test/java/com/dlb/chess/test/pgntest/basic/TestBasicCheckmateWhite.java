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

class TestBasicCheckmateWhite extends AbstractTestBasic {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBasicCheckmateWhite.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_white_checkmate_rook_direct_adjacent.pgn");
    pgnFileNameList.add("02_white_checkmate_rook_direct_range.pgn");
    pgnFileNameList.add("03_white_checkmate_rook_discover.pgn");
    pgnFileNameList.add("04_white_checkmate_knight_direct.pgn");
    pgnFileNameList.add("05_white_checkmate_knight_discover_orthogonal.pgn");
    pgnFileNameList.add("06_white_checkmate_knight_discover_diagonal.pgn");
    pgnFileNameList.add("07_white_checkmate_bishop_direct_adjacent.pgn");
    pgnFileNameList.add("08_white_checkmate_bishop_direct_range.pgn");
    pgnFileNameList.add("09_white_checkmate_bishop_discover.pgn");
    pgnFileNameList.add("10_white_checkmate_queen_direct_orthogonal_adjacent.pgn");
    pgnFileNameList.add("11_white_checkmate_queen_direct_orthogonal_range.pgn");
    pgnFileNameList.add("12_white_checkmate_queen_direct_diagonal_adjacent.pgn");
    pgnFileNameList.add("13_white_checkmate_queen_direct_diagonal_range.pgn");
    pgnFileNameList.add("14_white_checkmate_king_discover_orthogonal.pgn");
    pgnFileNameList.add("15_white_checkmate_king_discover_diagonal.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_CHECKMATE_WHITE);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.BASIC_CHECKMATE_WHITE);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = GeneralUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_white_checkmate_rook_direct_adjacent.pgn":
          checkNonCaptureCheckmate(WHITE, D7, D8, WHITE_ROOK, board);
          break;
        case "02_white_checkmate_rook_direct_range.pgn":
          checkNonCaptureCheckmate(WHITE, H7, H8, WHITE_ROOK, board);
          break;
        case "03_white_checkmate_rook_discover.pgn":
          checkNonCaptureCheckmate(WHITE, D7, H7, WHITE_ROOK, board);
          break;
        case "04_white_checkmate_knight_direct.pgn":
          checkNonCaptureCheckmate(WHITE, B4, C6, WHITE_KNIGHT, board);
          break;
        case "05_white_checkmate_knight_discover_orthogonal.pgn":
          checkNonCaptureCheckmate(WHITE, D5, F6, WHITE_KNIGHT, board);
          break;
        case "06_white_checkmate_knight_discover_diagonal.pgn":
          checkNonCaptureCheckmate(WHITE, F6, G8, WHITE_KNIGHT, board);
          break;
        case "07_white_checkmate_bishop_direct_adjacent.pgn":
          checkNonCaptureCheckmate(WHITE, B3, C4, WHITE_BISHOP, board);
          break;
        case "08_white_checkmate_bishop_direct_range.pgn":
          checkNonCaptureCheckmate(WHITE, E2, F3, WHITE_BISHOP, board);
          break;
        case "09_white_checkmate_bishop_discover.pgn":
          checkNonCaptureCheckmate(WHITE, E5, C7, WHITE_BISHOP, board);
          break;
        case "10_white_checkmate_queen_direct_orthogonal_adjacent.pgn":
          checkNonCaptureCheckmate(WHITE, F6, C6, WHITE_QUEEN, board);
          break;
        case "11_white_checkmate_queen_direct_orthogonal_range.pgn":
          checkNonCaptureCheckmate(WHITE, D8, A5, WHITE_QUEEN, board);
          break;
        case "12_white_checkmate_queen_direct_diagonal_adjacent.pgn":
          checkNonCaptureCheckmate(WHITE, B6, B7, WHITE_QUEEN, board);
          break;
        case "13_white_checkmate_queen_direct_diagonal_range.pgn":
          checkNonCaptureCheckmate(WHITE, C2, B3, WHITE_QUEEN, board);
          break;
        case "14_white_checkmate_king_discover_orthogonal.pgn":
          checkNonCaptureCheckmate(WHITE, G7, H6, WHITE_KING, board);
          break;
        case "15_white_checkmate_king_discover_diagonal.pgn":
          checkNonCaptureCheckmate(WHITE, B6, B7, WHITE_KING, board);
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
  }

}
