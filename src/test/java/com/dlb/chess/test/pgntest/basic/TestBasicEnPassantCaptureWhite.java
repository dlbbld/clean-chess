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

class TestBasicEnPassantCaptureWhite extends AbstractTestBasic {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBasicEnPassantCaptureWhite.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_white_en_passant_capture_right_a6.pgn");
    pgnFileNameList.add("02_white_en_passant_capture_right_b6.pgn");
    pgnFileNameList.add("03_white_en_passant_capture_right_c6.pgn");
    pgnFileNameList.add("04_white_en_passant_capture_right_d6.pgn");
    pgnFileNameList.add("05_white_en_passant_capture_right_e6.pgn");
    pgnFileNameList.add("06_white_en_passant_capture_right_f6.pgn");
    pgnFileNameList.add("07_white_en_passant_capture_right_g6.pgn");
    pgnFileNameList.add("08_white_en_passant_capture_left_b6.pgn");
    pgnFileNameList.add("09_white_en_passant_capture_left_c6.pgn");
    pgnFileNameList.add("10_white_en_passant_capture_left_d6.pgn");
    pgnFileNameList.add("11_white_en_passant_capture_left_e6.pgn");
    pgnFileNameList.add("12_white_en_passant_capture_left_f6.pgn");
    pgnFileNameList.add("13_white_en_passant_capture_left_g6.pgn");
    pgnFileNameList.add("14_white_en_passant_capture_left_h6.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_EN_PASSANT_CAPTURE_WHITE);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.BASIC_EN_PASSANT_CAPTURE_WHITE);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = GeneralUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_white_en_passant_capture_right_a6.pgn":
          checkEnPassantCapture(WHITE, B5, A6, board);
          break;
        case "02_white_en_passant_capture_right_b6.pgn":
          checkEnPassantCapture(WHITE, C5, B6, board);
          break;
        case "03_white_en_passant_capture_right_c6.pgn":
          checkEnPassantCapture(WHITE, D5, C6, board);
          break;
        case "04_white_en_passant_capture_right_d6.pgn":
          checkEnPassantCapture(WHITE, E5, D6, board);
          break;
        case "05_white_en_passant_capture_right_e6.pgn":
          checkEnPassantCapture(WHITE, F5, E6, board);
          break;
        case "06_white_en_passant_capture_right_f6.pgn":
          checkEnPassantCapture(WHITE, G5, F6, board);
          break;
        case "07_white_en_passant_capture_right_g6.pgn":
          checkEnPassantCapture(WHITE, H5, G6, board);
          break;
        case "08_white_en_passant_capture_left_b6.pgn":
          checkEnPassantCapture(WHITE, A5, B6, board);
          break;
        case "09_white_en_passant_capture_left_c6.pgn":
          checkEnPassantCapture(WHITE, B5, C6, board);
          break;
        case "10_white_en_passant_capture_left_d6.pgn":
          checkEnPassantCapture(WHITE, C5, D6, board);
          break;
        case "11_white_en_passant_capture_left_e6.pgn":
          checkEnPassantCapture(WHITE, D5, E6, board);
          break;
        case "12_white_en_passant_capture_left_f6.pgn":
          checkEnPassantCapture(WHITE, E5, F6, board);
          break;
        case "13_white_en_passant_capture_left_g6.pgn":
          checkEnPassantCapture(WHITE, F5, G6, board);
          break;
        case "14_white_en_passant_capture_left_h6.pgn":
          checkEnPassantCapture(WHITE, G5, H6, board);
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
  }

}
