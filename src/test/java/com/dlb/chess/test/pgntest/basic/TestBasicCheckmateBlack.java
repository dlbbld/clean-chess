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

class TestBasicCheckmateBlack extends AbstractTestBasic {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBasicCheckmateBlack.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_black_rook_checkmate_direct_adjacent.pgn");
    pgnFileNameList.add("02_black_rook_checkmate_direct_range.pgn");
    pgnFileNameList.add("03_black_rook_checkmate_discover.pgn");
    pgnFileNameList.add("04_black_knight_checkmate_direct.pgn");
    pgnFileNameList.add("05_black_knight_checkmate_discover_orthogonal.pgn");
    pgnFileNameList.add("06_black_knight_checkmate_discover_diagonal.pgn");
    pgnFileNameList.add("07_black_bishop_checkmate_direct_adjacent.pgn");
    pgnFileNameList.add("08_black_bishop_checkmate_direct_range.pgn");
    pgnFileNameList.add("09_black_bishop_checkmate_discover.pgn");
    pgnFileNameList.add("10_black_queen_checkmate_direct_orthogonal_adjacent.pgn");
    pgnFileNameList.add("11_black_queen_checkmate_direct_orthogonal_range.pgn");
    pgnFileNameList.add("12_black_queen_checkmate_direct_diagonal_adjacent.pgn");
    pgnFileNameList.add("13_black_queen_checkmate_direct_diagonal_range.pgn");
    pgnFileNameList.add("14_black_king_checkmate_discover_orthogonal.pgn");
    pgnFileNameList.add("15_black_king_checkmate_discover_diagonal.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_CHECKMATE_BLACK);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.BASIC_CHECKMATE_BLACK);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = GeneralUtility.calculateChessBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_black_rook_checkmate_direct_adjacent.pgn":
          checkNonCaptureCheckmate(BLACK, D2, D8, BLACK_ROOK, board);
          break;
        case "02_black_rook_checkmate_direct_range.pgn":
          checkNonCaptureCheckmate(BLACK, G3, G2, BLACK_ROOK, board);
          break;
        case "03_black_rook_checkmate_discover.pgn":
          checkNonCaptureCheckmate(BLACK, B6, B7, BLACK_ROOK, board);
          break;
        case "04_black_knight_checkmate_direct.pgn":
          checkNonCaptureCheckmate(BLACK, G2, F4, BLACK_KNIGHT, board);
          break;
        case "05_black_knight_checkmate_discover_orthogonal.pgn":
          checkNonCaptureCheckmate(BLACK, H5, F6, BLACK_KNIGHT, board);
          break;
        case "06_black_knight_checkmate_discover_diagonal.pgn":
          checkNonCaptureCheckmate(BLACK, B4, A6, BLACK_KNIGHT, board);
          break;
        case "07_black_bishop_checkmate_direct_adjacent.pgn":
          checkNonCaptureCheckmate(BLACK, E5, H2, BLACK_BISHOP, board);
          break;
        case "08_black_bishop_checkmate_direct_range.pgn":
          checkNonCaptureCheckmate(BLACK, E7, C5, BLACK_BISHOP, board);
          break;
        case "09_black_bishop_checkmate_discover.pgn":
          checkNonCaptureCheckmate(BLACK, G3, E5, BLACK_BISHOP, board);
          break;
        case "10_black_queen_checkmate_direct_orthogonal_adjacent.pgn":
          checkNonCaptureCheckmate(BLACK, C6, G2, BLACK_QUEEN, board);
          break;
        case "11_black_queen_checkmate_direct_orthogonal_range.pgn":
          checkNonCaptureCheckmate(BLACK, E7, G5, BLACK_QUEEN, board);
          break;
        case "12_black_queen_checkmate_direct_diagonal_adjacent.pgn":
          checkNonCaptureCheckmate(BLACK, B2, F2, BLACK_QUEEN, board);
          break;
        case "13_black_queen_checkmate_direct_diagonal_range.pgn":
          checkNonCaptureCheckmate(BLACK, D8, B6, BLACK_QUEEN, board);
          break;
        case "14_black_king_checkmate_discover_orthogonal.pgn":
          checkNonCaptureCheckmate(BLACK, E3, D4, BLACK_KING, board);
          break;
        case "15_black_king_checkmate_discover_diagonal.pgn":
          checkNonCaptureCheckmate(BLACK, F5, G5, BLACK_KING, board);
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
  }

}
