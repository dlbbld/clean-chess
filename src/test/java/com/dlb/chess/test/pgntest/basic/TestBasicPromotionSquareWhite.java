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

class TestBasicPromotionSquareWhite extends AbstractTestBasic {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBasicPromotionSquareWhite.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_white_promotion_square_straight_a8.pgn");
    pgnFileNameList.add("02_white_promotion_square_straight_b8.pgn");
    pgnFileNameList.add("03_white_promotion_square_straight_c8.pgn");
    pgnFileNameList.add("04_white_promotion_square_straight_d8.pgn");
    pgnFileNameList.add("05_white_promotion_square_straight_e8.pgn");
    pgnFileNameList.add("06_white_promotion_square_straight_f8.pgn");
    pgnFileNameList.add("07_white_promotion_square_straight_g8.pgn");
    pgnFileNameList.add("08_white_promotion_square_straight_h8.pgn");
    pgnFileNameList.add("09_white_promotion_square_right_a8.pgn");
    pgnFileNameList.add("10_white_promotion_square_right_b8.pgn");
    pgnFileNameList.add("11_white_promotion_square_right_c8.pgn");
    pgnFileNameList.add("12_white_promotion_square_right_d8.pgn");
    pgnFileNameList.add("13_white_promotion_square_right_e8.pgn");
    pgnFileNameList.add("14_white_promotion_square_right_f8.pgn");
    pgnFileNameList.add("15_white_promotion_square_right_g8.pgn");
    pgnFileNameList.add("16_white_promotion_square_left_b8.pgn");
    pgnFileNameList.add("17_white_promotion_square_left_c8.pgn");
    pgnFileNameList.add("18_white_promotion_square_left_d8.pgn");
    pgnFileNameList.add("19_white_promotion_square_left_e8.pgn");
    pgnFileNameList.add("20_white_promotion_square_left_f8.pgn");
    pgnFileNameList.add("21_white_promotion_square_left_g8.pgn");
    pgnFileNameList.add("22_white_promotion_square_left_h8.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_PROMOTION_SQUARE_WHITE);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.BASIC_PROMOTION_SQUARE_WHITE);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = GeneralUtility.calculateChessBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_white_promotion_square_straight_a8.pgn":
          checkPromotion(WHITE, A7, A8, Piece.NONE, PromotionPieceType.QUEEN, board);
          break;
        case "02_white_promotion_square_straight_b8.pgn":
          checkPromotion(WHITE, B7, B8, Piece.NONE, PromotionPieceType.QUEEN, board);
          break;
        case "03_white_promotion_square_straight_c8.pgn":
          checkPromotion(WHITE, C7, C8, Piece.NONE, PromotionPieceType.QUEEN, board);
          break;
        case "04_white_promotion_square_straight_d8.pgn":
          checkPromotion(WHITE, D7, D8, Piece.NONE, PromotionPieceType.QUEEN, board);
          break;
        case "05_white_promotion_square_straight_e8.pgn":
          checkPromotion(WHITE, E7, E8, Piece.NONE, PromotionPieceType.QUEEN, board);
          break;
        case "06_white_promotion_square_straight_f8.pgn":
          checkPromotion(WHITE, F7, F8, Piece.NONE, PromotionPieceType.QUEEN, board);
          break;
        case "07_white_promotion_square_straight_g8.pgn":
          checkPromotion(WHITE, G7, G8, Piece.NONE, PromotionPieceType.QUEEN, board);
          break;
        case "08_white_promotion_square_straight_h8.pgn":
          checkPromotion(WHITE, H7, H8, Piece.NONE, PromotionPieceType.QUEEN, board);
          break;
        case "09_white_promotion_square_right_a8.pgn":
          checkPromotion(WHITE, B7, A8, Piece.BLACK_ROOK, PromotionPieceType.QUEEN, board);
          break;
        case "10_white_promotion_square_right_b8.pgn":
          checkPromotion(WHITE, C7, B8, Piece.BLACK_ROOK, PromotionPieceType.QUEEN, board);
          break;
        case "11_white_promotion_square_right_c8.pgn":
          checkPromotion(WHITE, D7, C8, Piece.BLACK_ROOK, PromotionPieceType.QUEEN, board);
          break;
        case "12_white_promotion_square_right_d8.pgn":
          checkPromotion(WHITE, E7, D8, Piece.BLACK_ROOK, PromotionPieceType.QUEEN, board);
          break;
        case "13_white_promotion_square_right_e8.pgn":
          checkPromotion(WHITE, F7, E8, Piece.BLACK_BISHOP, PromotionPieceType.QUEEN, board);
          break;
        case "14_white_promotion_square_right_f8.pgn":
          checkPromotion(WHITE, G7, F8, Piece.BLACK_ROOK, PromotionPieceType.QUEEN, board);
          break;
        case "15_white_promotion_square_right_g8.pgn":
          checkPromotion(WHITE, H7, G8, Piece.BLACK_ROOK, PromotionPieceType.QUEEN, board);
          break;
        case "16_white_promotion_square_left_b8.pgn":
          checkPromotion(WHITE, A7, B8, Piece.BLACK_KNIGHT, PromotionPieceType.QUEEN, board);
          break;
        case "17_white_promotion_square_left_c8.pgn":
          checkPromotion(WHITE, B7, C8, Piece.BLACK_KNIGHT, PromotionPieceType.QUEEN, board);
          break;
        case "18_white_promotion_square_left_d8.pgn":
          checkPromotion(WHITE, C7, D8, Piece.BLACK_KNIGHT, PromotionPieceType.QUEEN, board);
          break;
        case "19_white_promotion_square_left_e8.pgn":
          checkPromotion(WHITE, D7, E8, Piece.BLACK_KNIGHT, PromotionPieceType.QUEEN, board);
          break;
        case "20_white_promotion_square_left_f8.pgn":
          checkPromotion(WHITE, E7, F8, Piece.BLACK_KNIGHT, PromotionPieceType.QUEEN, board);
          break;
        case "21_white_promotion_square_left_g8.pgn":
          checkPromotion(WHITE, F7, G8, Piece.BLACK_KNIGHT, PromotionPieceType.QUEEN, board);
          break;
        case "22_white_promotion_square_left_h8.pgn":
          checkPromotion(WHITE, G7, H8, Piece.BLACK_KNIGHT, PromotionPieceType.QUEEN, board);
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
  }

}
