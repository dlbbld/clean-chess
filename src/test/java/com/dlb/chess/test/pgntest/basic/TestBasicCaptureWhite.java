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

class TestBasicCaptureWhite extends AbstractTestBasic {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBasicCaptureWhite.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_white_rook_rook.pgn");
    pgnFileNameList.add("02_white_rook_knight.pgn");
    pgnFileNameList.add("03_white_rook_bishop.pgn");
    pgnFileNameList.add("04_white_rook_queen.pgn");
    pgnFileNameList.add("05_white_rook_pawn.pgn");
    pgnFileNameList.add("06_white_knight_rook.pgn");
    pgnFileNameList.add("07_white_knight_knight.pgn");
    pgnFileNameList.add("08_white_knight_bishop.pgn");
    pgnFileNameList.add("09_white_knight_queen.pgn");
    pgnFileNameList.add("10_white_knight_pawn.pgn");
    pgnFileNameList.add("11_white_bishop_rook.pgn");
    pgnFileNameList.add("12_white_bishop_knight.pgn");
    pgnFileNameList.add("13_white_bishop_bishop.pgn");
    pgnFileNameList.add("14_white_bishop_queen.pgn");
    pgnFileNameList.add("15_white_bishop_pawn.pgn");
    pgnFileNameList.add("16_white_queen_rook.pgn");
    pgnFileNameList.add("17_white_queen_knight.pgn");
    pgnFileNameList.add("18_white_queen_bishop.pgn");
    pgnFileNameList.add("19_white_queen_queen.pgn");
    pgnFileNameList.add("20_white_queen_pawn.pgn");
    pgnFileNameList.add("21_white_king_rook.pgn");
    pgnFileNameList.add("22_white_king_knight.pgn");
    pgnFileNameList.add("23_white_king_bishop.pgn");
    pgnFileNameList.add("24_white_king_queen.pgn");
    pgnFileNameList.add("25_white_king_pawn.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_CAPTURE_WHITE);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.BASIC_CAPTURE_WHITE);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = GeneralUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_white_rook_rook.pgn":
          checkCapture(WHITE, B3, B6, WHITE_ROOK, BLACK_ROOK, board);
          break;
        case "02_white_rook_knight.pgn":
          checkCapture(WHITE, E3, E5, WHITE_ROOK, BLACK_KNIGHT, board);
          break;
        case "03_white_rook_bishop.pgn":
          checkCapture(WHITE, H3, A3, WHITE_ROOK, BLACK_BISHOP, board);
          break;
        case "04_white_rook_queen.pgn":
          checkCapture(WHITE, H1, H4, WHITE_ROOK, BLACK_QUEEN, board);
          break;
        case "05_white_rook_pawn.pgn":
          checkCapture(WHITE, B3, B5, WHITE_ROOK, BLACK_PAWN, board);
          break;
        case "06_white_knight_rook.pgn":
          checkCapture(WHITE, B6, A8, WHITE_KNIGHT, BLACK_ROOK, board);
          break;
        case "07_white_knight_knight.pgn":
          checkCapture(WHITE, D5, F6, WHITE_KNIGHT, BLACK_KNIGHT, board);
          break;
        case "08_white_knight_bishop.pgn":
          checkCapture(WHITE, E4, D6, WHITE_KNIGHT, BLACK_BISHOP, board);
          break;
        case "09_white_knight_queen.pgn":
          checkCapture(WHITE, D5, F6, WHITE_KNIGHT, BLACK_QUEEN, board);
          break;
        case "10_white_knight_pawn.pgn":
          checkCapture(WHITE, C3, D5, WHITE_KNIGHT, BLACK_PAWN, board);
          break;
        case "11_white_bishop_rook.pgn":
          checkCapture(WHITE, B2, H8, WHITE_BISHOP, BLACK_ROOK, board);
          break;
        case "12_white_bishop_knight.pgn":
          checkCapture(WHITE, G5, F6, WHITE_BISHOP, BLACK_KNIGHT, board);
          break;
        case "13_white_bishop_bishop.pgn":
          checkCapture(WHITE, B2, G7, WHITE_BISHOP, BLACK_BISHOP, board);
          break;
        case "14_white_bishop_queen.pgn":
          checkCapture(WHITE, B2, F6, WHITE_BISHOP, BLACK_QUEEN, board);
          break;
        case "15_white_bishop_pawn.pgn":
          checkCapture(WHITE, G5, E7, WHITE_BISHOP, BLACK_PAWN, board);
          break;
        case "16_white_queen_rook.pgn":
          checkCapture(WHITE, F3, A8, WHITE_QUEEN, BLACK_ROOK, board);
          break;
        case "17_white_queen_knight.pgn":
          checkCapture(WHITE, A4, C6, WHITE_QUEEN, BLACK_KNIGHT, board);
          break;
        case "18_white_queen_bishop.pgn":
          checkCapture(WHITE, F3, G4, WHITE_QUEEN, BLACK_BISHOP, board);
          break;
        case "19_white_queen_queen.pgn":
          checkCapture(WHITE, H5, H4, WHITE_QUEEN, BLACK_QUEEN, board);
          break;
        case "20_white_queen_pawn.pgn":
          checkCapture(WHITE, H5, H7, WHITE_QUEEN, BLACK_PAWN, board);
          break;
        case "21_white_king_rook.pgn":
          checkCapture(WHITE, E3, D3, WHITE_KING, BLACK_ROOK, board);
          break;
        case "22_white_king_knight.pgn":
          checkCapture(WHITE, D3, D4, WHITE_KING, BLACK_KNIGHT, board);
          break;
        case "23_white_king_bishop.pgn":
          checkCapture(WHITE, C4, B4, WHITE_KING, BLACK_BISHOP, board);
          break;
        case "24_white_king_queen.pgn":
          checkCapture(WHITE, E2, E3, WHITE_KING, BLACK_QUEEN, board);
          break;
        case "25_white_king_pawn.pgn":
          checkCapture(WHITE, D3, D4, WHITE_KING, BLACK_PAWN, board);
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
  }

}
