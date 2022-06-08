package com.dlb.chess.test.unwinnability.againstcha;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.analysis.enums.CheckmateOrStalemate;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.fen.FenParser;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.unwinnability.againstcha.model.ChaBothRead;
import com.dlb.chess.test.unwinnability.againstcha.model.ChaFullRead;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public class TestAgainstChaPgnTestCase extends AbstractAgainstCha {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestAgainstChaPgnTestCase.class);

  private static final boolean IS_START_FROM_PGN_FILE = false;
  private static final String START_FROM_PGN_FILE_NAME = "no_move_half_move_clock_99_black_to_move.pgn";

  public static void main(String[] args) throws Exception {

    final ChaBothRead bothResult = readChaResultList(FEN_CHA_ANALYSIS_BOTH_FILE_PATH);
    // validateBothTestResult(bothResult); //ok
    checkAllTestCasesAgainstCha(bothResult);
  }

  private static void checkAllTestCasesAgainstCha(ChaBothRead bothResult) throws Exception {

    var hasFound = false;
    for (final PgnTest pgnTest : PgnTest.values()) {
      if (pgnTest != PgnTest.UNFAIR_LICHESS_ANALYSIS_GAMES) {
        continue;
      }
      final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(pgnTest);
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        if (!hasFound) {
          if (IS_START_FROM_PGN_FILE) {
            if (START_FROM_PGN_FILE_NAME.equals(testCase.pgnFileName())) {
              hasFound = true;
            }
          } else {
            hasFound = true;
          }
        }
        if (!hasFound) {
          continue;
        }

        logger.info(testCase.pgnFileName());

        final Fen fen = FenParser.parseAdvancedFen(testCase.fen());

        final ChaFullRead chaFullWhite = readFullResult(fen, Side.WHITE, bothResult.fullResultList());
        if (chaFullWhite.unwinnableFull() == UnwinnableFull.WINNABLE) {
          if (testCase.repetitionCountFinalPosition() >= ChessConstants.FIVEFOLD_REPETITION_RULE_THRESHOLD
              || fen.halfMoveClock() >= ChessConstants.SEVENTY_FIVE_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD
                  && testCase.checkmateOrStalemate() != CheckmateOrStalemate.CHECKMATE
              || "unwinnable_fivefold_inevitable.pgn".equals(testCase.pgnFileName())
              || "unwinnable_seventy_five_move_rule_inevitable.pgn".equals(testCase.pgnFileName())) {
            assertEquals(UnwinnableFull.UNWINNABLE, testCase.unwinnableFullWhite());
          } else {
            assertEquals(chaFullWhite.unwinnableFull(), testCase.unwinnableFullWhite());
          }
        } else {
          assertEquals(chaFullWhite.unwinnableFull(), testCase.unwinnableFullWhite());
        }

        final ChaFullRead chaFullBlack = readFullResult(fen, Side.BLACK, bothResult.fullResultList());
        if (chaFullBlack.unwinnableFull() == UnwinnableFull.WINNABLE) {
          if (testCase.repetitionCountFinalPosition() >= ChessConstants.FIVEFOLD_REPETITION_RULE_THRESHOLD
              || fen.halfMoveClock() >= ChessConstants.SEVENTY_FIVE_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD
                  && testCase.checkmateOrStalemate() != CheckmateOrStalemate.CHECKMATE
              || "unwinnable_fivefold_inevitable.pgn".equals(testCase.pgnFileName())
              || "unwinnable_seventy_five_move_rule_inevitable.pgn".equals(testCase.pgnFileName())) {
            assertEquals(UnwinnableFull.UNWINNABLE, testCase.unwinnableFullBlack());
          } else {
            assertEquals(chaFullBlack.unwinnableFull(), testCase.unwinnableFullBlack());
          }
        } else {
          assertEquals(chaFullBlack.unwinnableFull(), testCase.unwinnableFullBlack());
        }

        final UnwinnableQuick chaQuickTestResultHavingMove = readQuickTestResult(fen, fen.havingMove(),
            bothResult.quickResultList());
        assertEquals(chaQuickTestResultHavingMove, testCase.unwinnableQuickWhite());

        final UnwinnableQuick chaQuickTestResultNotHavingMove = readQuickTestResult(fen,
            fen.havingMove().getOppositeSide(), bothResult.quickResultList());
        assertEquals(chaQuickTestResultNotHavingMove, testCase.unwinnableQuickBlack());

      }
    }
  }

}
