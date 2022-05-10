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

class TestBasicPromotionSquareBlack extends AbstractTestBasic {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBasicPromotionSquareBlack.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_black_promotion_square_straight_a1.pgn");
    pgnFileNameList.add("02_black_promotion_square_straight_b1.pgn");
    pgnFileNameList.add("03_black_promotion_square_straight_c1.pgn");
    pgnFileNameList.add("04_black_promotion_square_straight_d1.pgn");
    pgnFileNameList.add("05_black_promotion_square_straight_e1.pgn");
    pgnFileNameList.add("06_black_promotion_square_straight_f1.pgn");
    pgnFileNameList.add("07_black_promotion_square_straight_g1.pgn");
    pgnFileNameList.add("08_black_promotion_square_straight_h1.pgn");
    pgnFileNameList.add("09_black_promotion_square_left_a1.pgn");
    pgnFileNameList.add("10_black_promotion_square_left_b1.pgn");
    pgnFileNameList.add("11_black_promotion_square_left_c1.pgn");
    pgnFileNameList.add("12_black_promotion_square_left_d1.pgn");
    pgnFileNameList.add("13_black_promotion_square_left_e1.pgn");
    pgnFileNameList.add("14_black_promotion_square_left_f1.pgn");
    pgnFileNameList.add("15_black_promotion_square_left_g1.pgn");
    pgnFileNameList.add("16_black_promotion_square_right_b1.pgn");
    pgnFileNameList.add("17_black_promotion_square_right_c1.pgn");
    pgnFileNameList.add("18_black_promotion_square_right_d1.pgn");
    pgnFileNameList.add("19_black_promotion_square_right_e1.pgn");
    pgnFileNameList.add("20_black_promotion_square_right_f1.pgn");
    pgnFileNameList.add("21_black_promotion_square_right_g1.pgn");
    pgnFileNameList.add("22_black_promotion_square_right_h1.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_PROMOTION_SQUARE_BLACK);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.BASIC_PROMOTION_SQUARE_BLACK);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = GeneralUtility.calculateChessBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_black_promotion_square_straight_a1.pgn":
          checkPromotion(BLACK, A2, A1, Piece.NONE, PromotionPieceType.QUEEN, board);
          break;
        case "02_black_promotion_square_straight_b1.pgn":
          checkPromotion(BLACK, B2, B1, Piece.NONE, PromotionPieceType.QUEEN, board);
          break;
        case "03_black_promotion_square_straight_c1.pgn":
          checkPromotion(BLACK, C2, C1, Piece.NONE, PromotionPieceType.QUEEN, board);
          break;
        case "04_black_promotion_square_straight_d1.pgn":
          checkPromotion(BLACK, D2, D1, Piece.NONE, PromotionPieceType.QUEEN, board);
          break;
        case "05_black_promotion_square_straight_e1.pgn":
          checkPromotion(BLACK, E2, E1, Piece.NONE, PromotionPieceType.QUEEN, board);
          break;
        case "06_black_promotion_square_straight_f1.pgn":
          checkPromotion(BLACK, F2, F1, Piece.NONE, PromotionPieceType.QUEEN, board);
          break;
        case "07_black_promotion_square_straight_g1.pgn":
          checkPromotion(BLACK, G2, G1, Piece.NONE, PromotionPieceType.QUEEN, board);
          break;
        case "08_black_promotion_square_straight_h1.pgn":
          checkPromotion(BLACK, H2, H1, Piece.NONE, PromotionPieceType.QUEEN, board);
          break;
        case "09_black_promotion_square_left_a1.pgn":
          checkPromotion(BLACK, B2, A1, Piece.WHITE_QUEEN, PromotionPieceType.QUEEN, board);
          break;
        case "10_black_promotion_square_left_b1.pgn":
          checkPromotion(BLACK, C2, B1, Piece.WHITE_QUEEN, PromotionPieceType.QUEEN, board);
          break;
        case "11_black_promotion_square_left_c1.pgn":
          checkPromotion(BLACK, D2, C1, Piece.WHITE_QUEEN, PromotionPieceType.QUEEN, board);
          break;
        case "12_black_promotion_square_left_d1.pgn":
          checkPromotion(BLACK, E2, D1, Piece.WHITE_KNIGHT, PromotionPieceType.QUEEN, board);
          break;
        case "13_black_promotion_square_left_e1.pgn":
          checkPromotion(BLACK, F2, E1, Piece.WHITE_QUEEN, PromotionPieceType.QUEEN, board);
          break;
        case "14_black_promotion_square_left_f1.pgn":
          checkPromotion(BLACK, G2, F1, Piece.WHITE_QUEEN, PromotionPieceType.QUEEN, board);
          break;
        case "15_black_promotion_square_left_g1.pgn":
          checkPromotion(BLACK, H2, G1, Piece.WHITE_QUEEN, PromotionPieceType.QUEEN, board);
          break;
        case "16_black_promotion_square_right_b1.pgn":
          checkPromotion(BLACK, A2, B1, Piece.WHITE_BISHOP, PromotionPieceType.QUEEN, board);
          break;
        case "17_black_promotion_square_right_c1.pgn":
          checkPromotion(BLACK, B2, C1, Piece.WHITE_BISHOP, PromotionPieceType.QUEEN, board);
          break;
        case "18_black_promotion_square_right_d1.pgn":
          checkPromotion(BLACK, C2, D1, Piece.WHITE_KNIGHT, PromotionPieceType.QUEEN, board);
          break;
        case "19_black_promotion_square_right_e1.pgn":
          checkPromotion(BLACK, D2, E1, Piece.WHITE_BISHOP, PromotionPieceType.QUEEN, board);
          break;
        case "20_black_promotion_square_right_f1.pgn":
          checkPromotion(BLACK, E2, F1, Piece.WHITE_BISHOP, PromotionPieceType.QUEEN, board);
          break;
        case "21_black_promotion_square_right_g1.pgn":
          checkPromotion(BLACK, F2, G1, Piece.WHITE_BISHOP, PromotionPieceType.QUEEN, board);
          break;
        case "22_black_promotion_square_right_h1.pgn":
          checkPromotion(BLACK, G2, H1, Piece.WHITE_BISHOP, PromotionPieceType.QUEEN, board);
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
  }

}
