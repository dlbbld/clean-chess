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

class TestBasicEnPassantCaptureBlack extends AbstractTestBasic {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBasicEnPassantCaptureBlack.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_black_en_passant_capture_left_a3.pgn");
    pgnFileNameList.add("02_black_en_passant_capture_left_b3.pgn");
    pgnFileNameList.add("03_black_en_passant_capture_left_c3.pgn");
    pgnFileNameList.add("04_black_en_passant_capture_left_d3.pgn");
    pgnFileNameList.add("05_black_en_passant_capture_left_e3.pgn");
    pgnFileNameList.add("06_black_en_passant_capture_left_f3.pgn");
    pgnFileNameList.add("07_black_en_passant_capture_left_g3.pgn");
    pgnFileNameList.add("08_black_en_passant_capture_right_b3.pgn");
    pgnFileNameList.add("09_black_en_passant_capture_right_c3.pgn");
    pgnFileNameList.add("10_black_en_passant_capture_right_d3.pgn");
    pgnFileNameList.add("11_black_en_passant_capture_right_e3.pgn");
    pgnFileNameList.add("12_black_en_passant_capture_right_f3.pgn");
    pgnFileNameList.add("13_black_en_passant_capture_right_g3.pgn");
    pgnFileNameList.add("14_black_en_passant_capture_right_h3.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_EN_PASSANT_CAPTURE_BLACK);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.BASIC_EN_PASSANT_CAPTURE_BLACK);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = GeneralUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_black_en_passant_capture_left_a3.pgn":
          checkEnPassantCapture(BLACK, B4, A3, board);
          break;
        case "02_black_en_passant_capture_left_b3.pgn":
          checkEnPassantCapture(BLACK, C4, B3, board);
          break;
        case "03_black_en_passant_capture_left_c3.pgn":
          checkEnPassantCapture(BLACK, D4, C3, board);
          break;
        case "04_black_en_passant_capture_left_d3.pgn":
          checkEnPassantCapture(BLACK, E4, D3, board);
          break;
        case "05_black_en_passant_capture_left_e3.pgn":
          checkEnPassantCapture(BLACK, F4, E3, board);
          break;
        case "06_black_en_passant_capture_left_f3.pgn":
          checkEnPassantCapture(BLACK, G4, F3, board);
          break;
        case "07_black_en_passant_capture_left_g3.pgn":
          checkEnPassantCapture(BLACK, H4, G3, board);
          break;
        case "08_black_en_passant_capture_right_b3.pgn":
          checkEnPassantCapture(BLACK, A4, B3, board);
          break;
        case "09_black_en_passant_capture_right_c3.pgn":
          checkEnPassantCapture(BLACK, B4, C3, board);
          break;
        case "10_black_en_passant_capture_right_d3.pgn":
          checkEnPassantCapture(BLACK, C4, D3, board);
          break;
        case "11_black_en_passant_capture_right_e3.pgn":
          checkEnPassantCapture(BLACK, D4, E3, board);
          break;
        case "12_black_en_passant_capture_right_f3.pgn":
          checkEnPassantCapture(BLACK, E4, F3, board);
          break;
        case "13_black_en_passant_capture_right_g3.pgn":
          checkEnPassantCapture(BLACK, F4, G3, board);
          break;
        case "14_black_en_passant_capture_right_h3.pgn":
          checkEnPassantCapture(BLACK, G4, H3, board);
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
  }

}
