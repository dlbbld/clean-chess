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

class TestBasicCastlingSpecialWhite extends AbstractTestBasic {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBasicCastlingSpecialWhite.class);

  static {
    final List<String> pgnFileNameList = new ArrayList<>();

    pgnFileNameList.add("01_white_castling_special_kingside_check.pgn");
    pgnFileNameList.add("02_white_castling_special_kingside_checkmate.pgn");
    pgnFileNameList.add("03_white_castling_special_kingside_fifty_move.pgn");
    pgnFileNameList.add("04_white_castling_special_kingside_seventy_five_move.pgn");
    pgnFileNameList.add("05_white_castling_special_kingside_stalemate.pgn");
    pgnFileNameList.add("06_white_castling_special_queenside_check.pgn");
    pgnFileNameList.add("07_white_castling_special_queenside_checkmate.pgn");
    pgnFileNameList.add("08_white_castling_special_queenside_fifty_move.pgn");
    pgnFileNameList.add("09_white_castling_special_queenside_seventy_five_move.pgn");
    pgnFileNameList.add("10_white_castling_special_queenside_stalemate.pgn");

    checkTestFolder(pgnFileNameList, PgnTest.BASIC_CASTLING_SPECIAL_WHITE);
  }

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.BASIC_CASTLING_SPECIAL_WHITE);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = GeneralUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      logger.info(testCase.pgnFileName());

      switch (testCase.pgnFileName()) {
        case "01_white_castling_special_kingside_check.pgn":
          checkCastle(WHITE, CastlingMove.KING_SIDE, board);
          break;
        case "02_white_castling_special_kingside_checkmate.pgn":
          checkCastle(WHITE, CastlingMove.KING_SIDE, board);
          break;
        case "03_white_castling_special_kingside_fifty_move.pgn":
          checkCastle(WHITE, CastlingMove.KING_SIDE, board);
          break;
        case "04_white_castling_special_kingside_seventy_five_move.pgn":
          checkCastle(WHITE, CastlingMove.KING_SIDE, board);
          break;
        case "05_white_castling_special_kingside_stalemate.pgn":
          checkCastle(WHITE, CastlingMove.KING_SIDE, board);
          break;
        case "06_white_castling_special_queenside_check.pgn":
          checkCastle(WHITE, CastlingMove.QUEEN_SIDE, board);
          break;
        case "07_white_castling_special_queenside_checkmate.pgn":
          checkCastle(WHITE, CastlingMove.QUEEN_SIDE, board);
          break;
        case "08_white_castling_special_queenside_fifty_move.pgn":
          checkCastle(WHITE, CastlingMove.QUEEN_SIDE, board);
          break;
        case "09_white_castling_special_queenside_seventy_five_move.pgn":
          checkCastle(WHITE, CastlingMove.QUEEN_SIDE, board);
          break;
        case "10_white_castling_special_queenside_stalemate.pgn":
          checkCastle(WHITE, CastlingMove.QUEEN_SIDE, board);
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
  }

}
