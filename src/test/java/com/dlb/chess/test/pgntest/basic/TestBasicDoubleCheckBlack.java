package com.dlb.chess.test.pgntest.basic;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.pgn.PgnUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;

class TestBasicDoubleCheckBlack extends AbstractTestBasic {

  private static final Logger logger = Nulls.getLogger(TestBasicDoubleCheckBlack.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_black_double_check_rook.pgn");
    pgnFileNameList.add("02_black_double_check_knight_orthogonal.pgn");
    pgnFileNameList.add("03_black_double_check_knight_diagonal.pgn");
    pgnFileNameList.add("04_black_double_check_bishop.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_DOUBLE_CHECK_BLACK);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = CreatePgnTestCases.getTestList(PgnTest.BASIC_DOUBLE_CHECK_BLACK);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final Board board = PgnUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(), testCase.pgnFileName(), false);

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_black_double_check_rook.pgn" -> checkDoubleCheck(Piece.BLACK_ROOK, board);
        case "02_black_double_check_knight_orthogonal.pgn" -> checkDoubleCheck(Piece.BLACK_KNIGHT, board);
        case "03_black_double_check_knight_diagonal.pgn" -> checkDoubleCheck(Piece.BLACK_KNIGHT, board);
        case "04_black_double_check_bishop.pgn" -> checkDoubleCheck(Piece.BLACK_BISHOP, board);
        default -> throw new IllegalArgumentException();
      }
    }
  }

}
