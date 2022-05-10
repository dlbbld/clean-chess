package com.dlb.chess.test.pgntest.basic;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;

class TestBasicDoubleCheckCheckmateBlack extends AbstractTestBasic {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBasicDoubleCheckCheckmateBlack.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_black_double_check_checkmate_rook.pgn");
    pgnFileNameList.add("02_black_double_check_checkmate_knight_orthogonal.pgn");
    pgnFileNameList.add("03_black_double_check_checkmate_knight_diagonal.pgn");
    pgnFileNameList.add("04_black_double_check_checkmate_bishop.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_CHECKMATE_DOUBLE_CHECK_BLACK);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.BASIC_CHECKMATE_DOUBLE_CHECK_BLACK);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = GeneralUtility.calculateChessBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_black_double_check_checkmate_rook.pgn":
          checkDoubleCheckCheckmate(Piece.BLACK_ROOK, board);
          break;
        case "02_black_double_check_checkmate_knight_orthogonal.pgn":
          checkDoubleCheckCheckmate(Piece.BLACK_KNIGHT, board);
          break;
        case "03_black_double_check_checkmate_knight_diagonal.pgn":
          checkDoubleCheckCheckmate(Piece.BLACK_KNIGHT, board);
          break;
        case "04_black_double_check_checkmate_bishop.pgn":
          checkDoubleCheckCheckmate(Piece.BLACK_BISHOP, board);
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
  }

}
