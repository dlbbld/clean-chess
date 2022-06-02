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
import com.dlb.chess.test.pgntest.enums.UnwinnableFullResultTest;
import com.dlb.chess.test.unwinnability.model.ValidateBothResult;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public class AgainstChaPgnTestCase extends AbstractAgainstCha {

  private static final Logger logger = NonNullWrapperCommon.getLogger(AgainstChaPgnTestCase.class);

  private static final boolean IS_START_FROM_PGN_FILE = false;
  private static final String START_FROM_PGN_FILE_NAME = "no_move_half_move_clock_99_black_to_move.pgn";

  public static void main(String[] args) throws Exception {

    final ValidateBothResult bothResult = readChaResultList(FEN_CHA_ANALYSIS_BOTH_FILE_PATH);
    // validateBothTestResult(bothResult); //ok
    checkAllTestCasesAgainstCha(bothResult);
  }

  private static void checkAllTestCasesAgainstCha(ValidateBothResult bothResult) throws Exception {

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

        final UnwinnableFull fullTestResultWhite = readFullTestResult(fen, Side.WHITE, bothResult.fullResultList());
        final UnwinnableFull fullTestResultBlack = readFullTestResult(fen, Side.BLACK, bothResult.fullResultList());

        final UnwinnableQuick quickTestResultWhite = readQuickTestResult(fen, Side.WHITE, bothResult.quickResultList());
        final UnwinnableQuick quickTestResultBlack = readQuickTestResult(fen, Side.BLACK, bothResult.quickResultList());

        switch (fen.havingMove()) {
          case WHITE:
            AgainstChaPgnTestCase.check(testCase.unwinnableNotHavingMove(), fullTestResultBlack, quickTestResultBlack,
                testCase);
            break;
          case BLACK:
            AgainstChaPgnTestCase.check(testCase.unwinnableNotHavingMove(), fullTestResultWhite, quickTestResultWhite,
                testCase);
            break;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }

        switch (fen.havingMove()) {
          case WHITE:
            AgainstChaPgnTestCase.check(testCase.unwinnableHavingMove(), fullTestResultWhite, quickTestResultWhite,
                testCase);
            break;
          case BLACK:
            AgainstChaPgnTestCase.check(testCase.unwinnableHavingMove(), fullTestResultBlack, quickTestResultBlack,
                testCase);
            break;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }

      }
    }
  }

  private static void check(UnwinnableFullResultTest unwinnableFullResultTest, UnwinnableFull unwinnableFull,
      UnwinnableQuick unwinnableQuick, PgnFileTestCase testCase) {
    switch (unwinnableFull) {
      case UNWINNABLE:
        switch (unwinnableQuick) {
          case UNWINNABLE:
            assertEquals(UnwinnableFullResultTest.UNWINNABLE, unwinnableFullResultTest);
            break;
          case WINNABLE:
            throw new IllegalArgumentException("Inconsistent data");
          case POSSIBLY_WINNABLE:
            assertEquals(UnwinnableFullResultTest.UNWINNABLE_QUICK_DOES_NOT_SEE, unwinnableFullResultTest);
            break;
          default:
            throw new IllegalArgumentException();
        }
        break;
      case WINNABLE:
        final Fen fen = FenParser.parseAdvancedFen(testCase.fen());

        if (testCase.repetitionCountFinalPosition() >= ChessConstants.FIVEFOLD_REPETITION_RULE_THRESHOLD
            || fen.halfMoveClock() >= ChessConstants.SEVENTY_FIVE_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD
                && testCase.checkmateOrStalemate() != CheckmateOrStalemate.CHECKMATE
            || "unwinnable_fivefold_inevitable.pgn".equals(testCase.pgnFileName())
            || "unwinnable_seventy_five_move_rule_inevitable.pgn".equals(testCase.pgnFileName())) {
          assertEquals(UnwinnableFullResultTest.UNWINNABLE, unwinnableFullResultTest);
        } else {
          assertEquals(UnwinnableFullResultTest.WINNABLE, unwinnableFullResultTest);
        }
        break;
      default:
        throw new IllegalArgumentException();

    }
  }

}
