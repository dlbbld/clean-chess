package com.dlb.chess.test.pgntest.basic;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;

class TestBasicCastlingBlack extends AbstractTestBasic {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBasicCastlingBlack.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_black_kingside_check.pgn");
    pgnFileNameList.add("02_black_kingside_checkmate.pgn");
    pgnFileNameList.add("03_black_kingside_fifty_move.pgn");
    pgnFileNameList.add("04_black_kingside_seventy_five_move.pgn");
    pgnFileNameList.add("05_black_kingside_stalemate.pgn");
    pgnFileNameList.add("06_black_queenside_check.pgn");
    pgnFileNameList.add("07_black_queenside_checkmate.pgn");
    pgnFileNameList.add("08_black_queenside_fifty_move.pgn");
    pgnFileNameList.add("09_black_queenside_seventy_five_move.pgn");
    pgnFileNameList.add("10_black_queenside_stalemate.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_CASTLING_SPECIAL_BLACK);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.BASIC_CASTLING_SPECIAL_BLACK);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = GeneralUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_black_kingside_check.pgn":
          checkCastle(BLACK, CastlingMove.KING_SIDE, board);
          break;
        case "02_black_kingside_checkmate.pgn":
          checkCastle(BLACK, CastlingMove.KING_SIDE, board);
          break;
        case "03_black_kingside_fifty_move.pgn":
          checkCastle(BLACK, CastlingMove.KING_SIDE, board);
          break;
        case "04_black_kingside_seventy_five_move.pgn":
          checkCastle(BLACK, CastlingMove.KING_SIDE, board);
          break;
        case "05_black_kingside_stalemate.pgn":
          checkCastle(BLACK, CastlingMove.KING_SIDE, board);
          break;
        case "06_black_queenside_check.pgn":
          checkCastle(BLACK, CastlingMove.QUEEN_SIDE, board);
          break;
        case "07_black_queenside_checkmate.pgn":
          checkCastle(BLACK, CastlingMove.QUEEN_SIDE, board);
          break;
        case "08_black_queenside_fifty_move.pgn":
          checkCastle(BLACK, CastlingMove.QUEEN_SIDE, board);
          break;
        case "09_black_queenside_seventy_five_move.pgn":
          checkCastle(BLACK, CastlingMove.QUEEN_SIDE, board);
          break;
        case "10_black_queenside_stalemate.pgn":
          checkCastle(BLACK, CastlingMove.QUEEN_SIDE, board);
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
  }

}
