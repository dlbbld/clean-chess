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

class TestBasicCaptureBlack extends AbstractTestBasic {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBasicCaptureBlack.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_black_rook_rook.pgn");
    pgnFileNameList.add("02_black_rook_knight.pgn");
    pgnFileNameList.add("03_black_rook_bishop.pgn");
    pgnFileNameList.add("04_black_rook_queen.pgn");
    pgnFileNameList.add("05_black_rook_pawn.pgn");
    pgnFileNameList.add("06_black_knight_rook.pgn");
    pgnFileNameList.add("07_black_knight_knight.pgn");
    pgnFileNameList.add("08_black_knight_bishop.pgn");
    pgnFileNameList.add("09_black_knight_queen.pgn");
    pgnFileNameList.add("10_black_knight_pawn.pgn");
    pgnFileNameList.add("11_black_bishop_rook.pgn");
    pgnFileNameList.add("12_black_bishop_knight.pgn");
    pgnFileNameList.add("13_black_bishop_bishop.pgn");
    pgnFileNameList.add("14_black_bishop_queen.pgn");
    pgnFileNameList.add("15_black_bishop_pawn.pgn");
    pgnFileNameList.add("16_black_queen_rook.pgn");
    pgnFileNameList.add("17_black_queen_knight.pgn");
    pgnFileNameList.add("18_black_queen_bishop.pgn");
    pgnFileNameList.add("19_black_queen_queen.pgn");
    pgnFileNameList.add("20_black_queen_pawn.pgn");
    pgnFileNameList.add("21_black_king_rook.pgn");
    pgnFileNameList.add("22_black_king_knight.pgn");
    pgnFileNameList.add("23_black_king_bishop.pgn");
    pgnFileNameList.add("24_black_king_queen.pgn");
    pgnFileNameList.add("25_black_king_pawn.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_CAPTURE_BLACK);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.BASIC_CAPTURE_BLACK);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = GeneralUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_black_rook_rook.pgn":
          checkCapture(BLACK, D6, D5, BLACK_ROOK, WHITE_ROOK, board);
          break;
        case "02_black_rook_knight.pgn":
          checkCapture(BLACK, A6, A7, BLACK_ROOK, WHITE_KNIGHT, board);
          break;
        case "03_black_rook_bishop.pgn":
          checkCapture(BLACK, A6, H6, BLACK_ROOK, WHITE_BISHOP, board);
          break;
        case "04_black_rook_queen.pgn":
          checkCapture(BLACK, H8, H5, BLACK_ROOK, WHITE_QUEEN, board);
          break;
        case "05_black_rook_pawn.pgn":
          checkCapture(BLACK, H6, D6, BLACK_ROOK, WHITE_PAWN, board);
          break;
        case "06_black_knight_rook.pgn":
          checkCapture(BLACK, C2, A1, BLACK_KNIGHT, WHITE_ROOK, board);
          break;
        case "07_black_knight_knight.pgn":
          checkCapture(BLACK, F3, G1, BLACK_KNIGHT, WHITE_KNIGHT, board);
          break;
        case "08_black_knight_bishop.pgn":
          checkCapture(BLACK, G3, F1, BLACK_KNIGHT, WHITE_BISHOP, board);
          break;
        case "09_black_knight_queen.pgn":
          checkCapture(BLACK, E3, D1, BLACK_KNIGHT, WHITE_QUEEN, board);
          break;
        case "10_black_knight_pawn.pgn":
          checkCapture(BLACK, C6, D4, BLACK_KNIGHT, WHITE_PAWN, board);
          break;
        case "11_black_bishop_rook.pgn":
          checkCapture(BLACK, B7, H1, BLACK_BISHOP, WHITE_ROOK, board);
          break;
        case "12_black_bishop_knight.pgn":
          checkCapture(BLACK, F5, B1, BLACK_BISHOP, WHITE_KNIGHT, board);
          break;
        case "13_black_bishop_bishop.pgn":
          checkCapture(BLACK, A6, F1, BLACK_BISHOP, WHITE_BISHOP, board);
          break;
        case "14_black_bishop_queen.pgn":
          checkCapture(BLACK, G4, D1, BLACK_BISHOP, WHITE_QUEEN, board);
          break;
        case "15_black_bishop_pawn.pgn":
          checkCapture(BLACK, H3, G2, BLACK_BISHOP, WHITE_PAWN, board);
          break;
        case "16_black_queen_rook.pgn":
          checkCapture(BLACK, F6, A1, BLACK_QUEEN, WHITE_ROOK, board);
          break;
        case "17_black_queen_knight.pgn":
          checkCapture(BLACK, F5, B1, BLACK_QUEEN, WHITE_KNIGHT, board);
          break;
        case "18_black_queen_bishop.pgn":
          checkCapture(BLACK, D8, G5, BLACK_QUEEN, WHITE_BISHOP, board);
          break;
        case "19_black_queen_queen.pgn":
          checkCapture(BLACK, G5, F4, BLACK_QUEEN, WHITE_QUEEN, board);
          break;
        case "20_black_queen_pawn.pgn":
          checkCapture(BLACK, F6, F2, BLACK_QUEEN, WHITE_PAWN, board);
          break;
        case "21_black_king_rook.pgn":
          checkCapture(BLACK, B2, A1, BLACK_KING, WHITE_ROOK, board);
          break;
        case "22_black_king_knight.pgn":
          checkCapture(BLACK, H2, G1, BLACK_KING, WHITE_KNIGHT, board);
          break;
        case "23_black_king_bishop.pgn":
          checkCapture(BLACK, C2, C1, BLACK_KING, WHITE_BISHOP, board);
          break;
        case "24_black_king_queen.pgn":
          checkCapture(BLACK, E8, F7, BLACK_KING, WHITE_QUEEN, board);
          break;
        case "25_black_king_pawn.pgn":
          checkCapture(BLACK, A3, A2, BLACK_KING, WHITE_PAWN, board);
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
  }

}
