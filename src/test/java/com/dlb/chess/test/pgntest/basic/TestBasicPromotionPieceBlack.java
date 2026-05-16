package com.dlb.chess.test.pgntest.basic;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.pgn.PgnUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;

class TestBasicPromotionPieceBlack extends AbstractTestBasic {

  private static final Logger logger = Nulls.getLogger(TestBasicPromotionPieceBlack.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_black_promotion_piece_capture_no_rook.pgn");
    pgnFileNameList.add("02_black_promotion_piece_capture_no_knight.pgn");
    pgnFileNameList.add("03_black_promotion_piece_capture_no_bishop.pgn");
    pgnFileNameList.add("04_black_promotion_piece_capture_no_queen.pgn");
    pgnFileNameList.add("05_black_promotion_piece_capture_yes_rook.pgn");
    pgnFileNameList.add("06_black_promotion_piece_capture_yes_knight.pgn");
    pgnFileNameList.add("07_black_promotion_piece_capture_yes_bishop.pgn");
    pgnFileNameList.add("08_black_promotion_piece_capture_yes_queen.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_PROMOTION_PIECE_BLACK);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = CreatePgnTestCases.getTestList(PgnTest.BASIC_PROMOTION_PIECE_BLACK);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final Board board = PgnUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(), testCase.pgnFileName(), false);

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_black_promotion_piece_capture_no_rook.pgn" -> checkPromotion(BLACK, H2, H1, Piece.NONE,
            PromotionPieceType.ROOK, board);
        case "02_black_promotion_piece_capture_no_knight.pgn" -> checkPromotion(BLACK, H2, H1, Piece.NONE,
            PromotionPieceType.KNIGHT, board);
        case "03_black_promotion_piece_capture_no_bishop.pgn" -> checkPromotion(BLACK, H2, H1, Piece.NONE,
            PromotionPieceType.BISHOP, board);
        case "04_black_promotion_piece_capture_no_queen.pgn" -> checkPromotion(BLACK, H2, H1, Piece.NONE,
            PromotionPieceType.QUEEN, board);
        case "05_black_promotion_piece_capture_yes_rook.pgn" -> checkPromotion(BLACK, B2, C1, Piece.WHITE_BISHOP,
            PromotionPieceType.ROOK, board);
        case "06_black_promotion_piece_capture_yes_knight.pgn" -> checkPromotion(BLACK, B2, C1, Piece.WHITE_BISHOP,
            PromotionPieceType.KNIGHT, board);
        case "07_black_promotion_piece_capture_yes_bishop.pgn" -> checkPromotion(BLACK, B2, C1, Piece.WHITE_BISHOP,
            PromotionPieceType.BISHOP, board);
        case "08_black_promotion_piece_capture_yes_queen.pgn" -> checkPromotion(BLACK, B2, C1, Piece.WHITE_BISHOP,
            PromotionPieceType.QUEEN, board);
        default -> throw new IllegalArgumentException();
      }
    }
  }

}
