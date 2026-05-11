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
    final PgnFileTestCaseList testCaseList = CreatePgnTestCases.getTestList(PgnTest.BASIC_CHECKMATE_WHITE);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final Board board = GeneralUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_white_checkmate_rook_direct_adjacent.pgn" -> checkNonCaptureCheckmate(D7, D8, WHITE_ROOK, board);
        case "02_white_checkmate_rook_direct_range.pgn" -> checkNonCaptureCheckmate(H7, H8, WHITE_ROOK, board);
        case "03_white_checkmate_rook_discover.pgn" -> checkNonCaptureCheckmate(D7, H7, WHITE_ROOK, board);
        case "04_white_checkmate_knight_direct.pgn" -> checkNonCaptureCheckmate(B4, C6, WHITE_KNIGHT, board);
        case "05_white_checkmate_knight_discover_orthogonal.pgn" -> checkNonCaptureCheckmate(D5, F6, WHITE_KNIGHT,
            board);
        case "06_white_checkmate_knight_discover_diagonal.pgn" -> checkNonCaptureCheckmate(F6, G8, WHITE_KNIGHT, board);
        case "07_white_checkmate_bishop_direct_adjacent.pgn" -> checkNonCaptureCheckmate(B3, C4, WHITE_BISHOP, board);
        case "08_white_checkmate_bishop_direct_range.pgn" -> checkNonCaptureCheckmate(E2, F3, WHITE_BISHOP, board);
        case "09_white_checkmate_bishop_discover.pgn" -> checkNonCaptureCheckmate(E5, C7, WHITE_BISHOP, board);
        case "10_white_checkmate_queen_direct_orthogonal_adjacent.pgn" -> checkNonCaptureCheckmate(F6, C6, WHITE_QUEEN,
            board);
        case "11_white_checkmate_queen_direct_orthogonal_range.pgn" -> checkNonCaptureCheckmate(D8, A5, WHITE_QUEEN,
            board);
        case "12_white_checkmate_queen_direct_diagonal_adjacent.pgn" -> checkNonCaptureCheckmate(B6, B7, WHITE_QUEEN,
            board);
        case "13_white_checkmate_queen_direct_diagonal_range.pgn" -> checkNonCaptureCheckmate(C2, B3, WHITE_QUEEN,
            board);
        case "14_white_checkmate_king_discover_orthogonal.pgn" -> checkNonCaptureCheckmate(G7, H6, WHITE_KING, board);
        case "15_white_checkmate_king_discover_diagonal.pgn" -> checkNonCaptureCheckmate(B6, B7, WHITE_KING, board);
        default -> throw new IllegalArgumentException();
      }
    }
  }

}
