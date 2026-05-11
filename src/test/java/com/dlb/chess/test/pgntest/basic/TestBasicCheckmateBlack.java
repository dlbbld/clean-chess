package com.dlb.chess.test.pgntest.basic;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.board.Board;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;

class TestBasicCheckmateBlack extends AbstractTestBasic {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBasicCheckmateBlack.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_black_checkmate_rook_direct_adjacent.pgn");
    pgnFileNameList.add("02_black_checkmate_rook_direct_range.pgn");
    pgnFileNameList.add("03_black_checkmate_rook_discover.pgn");
    pgnFileNameList.add("04_black_checkmate_knight_direct.pgn");
    pgnFileNameList.add("05_black_checkmate_knight_discover_orthogonal.pgn");
    pgnFileNameList.add("06_black_checkmate_knight_discover_diagonal.pgn");
    pgnFileNameList.add("07_black_checkmate_bishop_direct_adjacent.pgn");
    pgnFileNameList.add("08_black_checkmate_bishop_direct_range.pgn");
    pgnFileNameList.add("09_black_checkmate_bishop_discover.pgn");
    pgnFileNameList.add("10_black_checkmate_queen_direct_orthogonal_adjacent.pgn");
    pgnFileNameList.add("11_black_checkmate_queen_direct_orthogonal_range.pgn");
    pgnFileNameList.add("12_black_checkmate_queen_direct_diagonal_adjacent.pgn");
    pgnFileNameList.add("13_black_checkmate_queen_direct_diagonal_range.pgn");
    pgnFileNameList.add("14_black_checkmate_king_discover_orthogonal.pgn");
    pgnFileNameList.add("15_black_checkmate_king_discover_diagonal.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_CHECKMATE_BLACK);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = CreatePgnTestCases.getTestList(PgnTest.BASIC_CHECKMATE_BLACK);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final Board board = GeneralUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_black_checkmate_rook_direct_adjacent.pgn" -> checkNonCaptureCheckmate(D2, D8, BLACK_ROOK, board);
        case "02_black_checkmate_rook_direct_range.pgn" -> checkNonCaptureCheckmate(G3, G2, BLACK_ROOK, board);
        case "03_black_checkmate_rook_discover.pgn" -> checkNonCaptureCheckmate(B6, B7, BLACK_ROOK, board);
        case "04_black_checkmate_knight_direct.pgn" -> checkNonCaptureCheckmate(G2, F4, BLACK_KNIGHT, board);
        case "05_black_checkmate_knight_discover_orthogonal.pgn" -> checkNonCaptureCheckmate(H5, F6, BLACK_KNIGHT,
            board);
        case "06_black_checkmate_knight_discover_diagonal.pgn" -> checkNonCaptureCheckmate(B4, A6, BLACK_KNIGHT, board);
        case "07_black_checkmate_bishop_direct_adjacent.pgn" -> checkNonCaptureCheckmate(E5, H2, BLACK_BISHOP, board);
        case "08_black_checkmate_bishop_direct_range.pgn" -> checkNonCaptureCheckmate(E7, C5, BLACK_BISHOP, board);
        case "09_black_checkmate_bishop_discover.pgn" -> checkNonCaptureCheckmate(G3, E5, BLACK_BISHOP, board);
        case "10_black_checkmate_queen_direct_orthogonal_adjacent.pgn" -> checkNonCaptureCheckmate(C6, G2, BLACK_QUEEN,
            board);
        case "11_black_checkmate_queen_direct_orthogonal_range.pgn" -> checkNonCaptureCheckmate(E7, G5, BLACK_QUEEN,
            board);
        case "12_black_checkmate_queen_direct_diagonal_adjacent.pgn" -> checkNonCaptureCheckmate(B2, F2, BLACK_QUEEN,
            board);
        case "13_black_checkmate_queen_direct_diagonal_range.pgn" -> checkNonCaptureCheckmate(D8, B6, BLACK_QUEEN,
            board);
        case "14_black_checkmate_king_discover_orthogonal.pgn" -> checkNonCaptureCheckmate(E3, D4, BLACK_KING, board);
        case "15_black_checkmate_king_discover_diagonal.pgn" -> checkNonCaptureCheckmate(F5, G5, BLACK_KING, board);
        default -> throw new IllegalArgumentException();
      }
    }
  }

}
