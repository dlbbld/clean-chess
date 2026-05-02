package com.dlb.chess.test.pgntest.basic;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ChessBoard;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.model.EnPassantRole;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;

class TestBasicMovingPieceBlack extends AbstractTestBasic {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBasicMovingPieceBlack.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_black_moving_piece_rook.pgn");
    pgnFileNameList.add("02_black_moving_piece_knight.pgn");
    pgnFileNameList.add("03_black_moving_piece_bishop.pgn");
    pgnFileNameList.add("04_black_moving_piece_queen.pgn");
    pgnFileNameList.add("05_black_moving_piece_king.pgn");
    pgnFileNameList.add("06_black_moving_piece_pawn.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_MOVING_PIECE_BLACK);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.BASIC_MOVING_PIECE_BLACK);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ChessBoard board = GeneralUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_black_moving_piece_rook.pgn" -> checkMovingPiece(H8, H7, BLACK_ROOK, board);
        case "02_black_moving_piece_knight.pgn" -> checkMovingPiece(G8, F6, BLACK_KNIGHT, board);
        case "03_black_moving_piece_bishop.pgn" -> checkMovingPiece(F8, G7, BLACK_BISHOP, board);
        case "04_black_moving_piece_queen.pgn" -> checkMovingPiece(D8, H4, BLACK_QUEEN, board);
        case "05_black_moving_piece_king.pgn" -> checkMovingPiece(E8, E7, BLACK_KING, board);
        case "06_black_moving_piece_pawn.pgn" -> checkMovingPiece(E7, E5, BLACK_PAWN, board,
            EnPassantRole.TWO_SQUARE_ADVANCE);
        default -> throw new IllegalArgumentException();
      }
    }
  }
}
