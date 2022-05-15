package com.dlb.chess.test.pgntest.basic;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;

class TestBasicPromotionPieceWhite extends AbstractTestBasic {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBasicPromotionPieceWhite.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_white_promotion_capture_no_rook.pgn");
    pgnFileNameList.add("02_white_promotion_capture_no_knight.pgn");
    pgnFileNameList.add("03_white_promotion_capture_no_bishop.pgn");
    pgnFileNameList.add("04_white_promotion_capture_no_queen.pgn");
    pgnFileNameList.add("05_white_promotion_capture_yes_rook.pgn");
    pgnFileNameList.add("06_white_promotion_capture_yes_knight.pgn");
    pgnFileNameList.add("07_white_promotion_capture_yes_bishop.pgn");
    pgnFileNameList.add("08_white_promotion_capture_yes_queen.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_PROMOTION_PIECE_WHITE);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.BASIC_PROMOTION_PIECE_WHITE);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = GeneralUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_white_promotion_capture_no_rook.pgn":
          checkPromotion(WHITE, B7, B8, Piece.NONE, PromotionPieceType.ROOK, board);
          break;
        case "02_white_promotion_capture_no_knight.pgn":
          checkPromotion(WHITE, B7, B8, Piece.NONE, PromotionPieceType.KNIGHT, board);
          break;
        case "03_white_promotion_capture_no_bishop.pgn":
          checkPromotion(WHITE, B7, B8, Piece.NONE, PromotionPieceType.BISHOP, board);
          break;
        case "04_white_promotion_capture_no_queen.pgn":
          checkPromotion(WHITE, B7, B8, Piece.NONE, PromotionPieceType.QUEEN, board);
          break;
        case "05_white_promotion_capture_yes_rook.pgn":
          checkPromotion(WHITE, B7, A8, Piece.BLACK_ROOK, PromotionPieceType.ROOK, board);
          break;
        case "06_white_promotion_capture_yes_knight.pgn":
          checkPromotion(WHITE, B7, A8, Piece.BLACK_ROOK, PromotionPieceType.KNIGHT, board);
          break;
        case "07_white_promotion_capture_yes_bishop.pgn":
          checkPromotion(WHITE, B7, A8, Piece.BLACK_ROOK, PromotionPieceType.BISHOP, board);
          break;
        case "08_white_promotion_capture_yes_queen.pgn":
          checkPromotion(WHITE, B7, A8, Piece.BLACK_ROOK, PromotionPieceType.QUEEN, board);
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
  }

}
