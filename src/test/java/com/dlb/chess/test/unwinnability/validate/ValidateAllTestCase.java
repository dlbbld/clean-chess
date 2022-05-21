package com.dlb.chess.test.unwinnability.validate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.FenValidationException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.fen.FenParser;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.pgn.reader.PgnReader;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.pgntest.enums.UnwinnableFullResultTest;
import com.dlb.chess.test.unwinnability.enums.ChaFullResult;
import com.dlb.chess.test.unwinnability.enums.ChaMode;
import com.dlb.chess.test.unwinnability.enums.ChaQuickResult;
import com.dlb.chess.test.unwinnability.model.ValidateBothResult;
import com.dlb.chess.test.unwinnability.model.ValidateFullResult;
import com.dlb.chess.test.unwinnability.model.ValidateQuickResult;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public class ValidateAllTestCase {

  private static final String FEN_ANALYSIS_FILE_PATH = "c:\\temp\\fen_analysis.txt";

  private static final Logger logger = NonNullWrapperCommon.getLogger(ValidateAllTestCase.class);

  private static final boolean IS_START_FROM_PGN_FILE = true;
  private static final String START_FROM_PGN_FILE_NAME = "no_move_half_move_clock_99_black_to_move.pgn";

  public static void main(String[] args) throws Exception {

    final ValidateBothResult bothResult = readChaResultList();
    // validateBothTestResult(bothResult); //ok
    checkTestCasesAgainstCha(bothResult);
  }

  private static void checkTestCasesAgainstCha(ValidateBothResult bothResult) throws Exception {

    var hasFound = false;
    for (final PgnTest pgnTest : PgnTest.values()) {
      if (pgnTest == PgnTest.UNFAIR_LICHESS_ANALYSIS_GAMES) {
        // TODO today
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
            ValidateAllTestCase.check(testCase.unwinnableNotHavingMove(), fullTestResultBlack, quickTestResultBlack,
                pgnTest.getFolderPath(), testCase.pgnFileName());
            break;
          case BLACK:
            ValidateAllTestCase.check(testCase.unwinnableNotHavingMove(), fullTestResultWhite, quickTestResultWhite,
                pgnTest.getFolderPath(), testCase.pgnFileName());
            break;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }

        switch (fen.havingMove()) {
          case WHITE:
            ValidateAllTestCase.check(testCase.unwinnableHavingMove(), fullTestResultWhite, quickTestResultWhite,
                pgnTest.getFolderPath(), testCase.pgnFileName());
            break;
          case BLACK:
            ValidateAllTestCase.check(testCase.unwinnableHavingMove(), fullTestResultBlack, quickTestResultBlack,
                pgnTest.getFolderPath(), testCase.pgnFileName());
            break;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }

      }
    }
  }

  private static ValidateBothResult readChaResultList() throws Exception {
    final List<ValidateFullResult> fullResultList = new ArrayList<>();
    final List<ValidateQuickResult> quickResultList = new ArrayList<>();

    final List<String> fileLineList = FileUtility.readFileLines(FEN_ANALYSIS_FILE_PATH);
    for (final String fileLine : fileLineList) {
      final var fileLineItemArray = fileLine.split(";");
      if (fileLineItemArray.length != 4) {
        throw new IllegalArgumentException("Illegal file line, must contain exactly four semicolon separate values");
      }
      final var fileLineItemList = NonNullWrapperCommon.asList(fileLineItemArray);

      final var fenStr = NonNullWrapperCommon.get(fileLineItemList, 0).trim();
      final Fen fen;
      try {
        fen = FenParser.parseAdvancedFen(fenStr);
      } catch (final FenValidationException fve) {
        throw new IllegalArgumentException("Illegal FEN of \"" + fenStr + "\" for " + fve.getMessage() + " was found");
      }

      final var chaModeStr = NonNullWrapperCommon.get(fileLineItemList, 1);
      if (!ChaMode.exists(chaModeStr)) {
        throw new IllegalArgumentException("Illegal identifier of \"" + chaModeStr + "\" was found");
      }
      final ChaMode chaMode = ChaMode.calculate(chaModeStr);

      final var sideCheckingForWinStr = NonNullWrapperCommon.get(fileLineItemList, 2);
      if (!Side.exists(sideCheckingForWinStr)) {
        throw new IllegalArgumentException("Illegal winning side of \"" + sideCheckingForWinStr + "\" was found");
      }
      final Side sideCheckingForWin = Side.calculate(sideCheckingForWinStr);

      switch (chaMode) {
        case FULL: {
          final var chaFullResultStr = NonNullWrapperCommon.get(fileLineItemList, 3);
          if (!ChaFullResult.exists(chaFullResultStr)) {
            throw new IllegalArgumentException("Illegal full result of \"" + chaFullResultStr + "\" was found");
          }
          final ChaFullResult chaFullResult = ChaFullResult.calculate(chaFullResultStr);
          fullResultList.add(new ValidateFullResult(fen, sideCheckingForWin, chaFullResult.getUnwinnableFull()));
        }
          break;
        case QUICK: {
          final var chaQuickResultStr = NonNullWrapperCommon.get(fileLineItemList, 3);
          if (!ChaQuickResult.exists(chaQuickResultStr)) {
            throw new IllegalArgumentException("Illegal quick result of \"" + chaQuickResultStr + "\" was found");
          }
          final ChaQuickResult chaQuickResult = ChaQuickResult.calculate(chaQuickResultStr);
          quickResultList.add(new ValidateQuickResult(fen, sideCheckingForWin, chaQuickResult.getUnwinnableQuick()));
        }
          break;
        default:
          throw new IllegalArgumentException();
      }
    }

    return new ValidateBothResult(fullResultList, quickResultList);
  }

  // list of the FEN of the past position for all PGN test case
  static void createFenList() throws Exception {

    for (final PgnTest pgnTest : PgnTest.values()) {
      final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(pgnTest);
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        System.out.println(testCase.fen());
      }
    }
  }

  private static UnwinnableFull readFullTestResult(Fen fen, Side sideCheckingForWin,
      List<ValidateFullResult> fullResultList) {
    for (final ValidateFullResult fullResult : fullResultList) {
      if (fullResult.sideCheckingForWin() == sideCheckingForWin) {
        if (fullResult.fen().equals(fen)) {
          return fullResult.unwinnableFull();
        }
      }
    }
    throw new IllegalArgumentException("No full test result was found");
  }

  private static UnwinnableQuick readQuickTestResult(Fen fen, Side sideCheckingForWin,
      List<ValidateQuickResult> quickResultList) {
    for (final ValidateQuickResult quickResult : quickResultList) {
      if (quickResult.sideCheckingForWin() == sideCheckingForWin) {
        if (quickResult.fen().equals(fen)) {
          return quickResult.unwinnableQuick();
        }
      }
    }
    throw new IllegalArgumentException("No quick test result was found");
  }

  static void validateBothTestResult(ValidateBothResult bothResult) {
    if (bothResult.fullResultList().size() != bothResult.quickResultList().size()) {
      throw new IllegalArgumentException("Test result list sizes not match for full and quick test");
    }

    for (final ValidateFullResult fullResult : bothResult.fullResultList()) {
      final UnwinnableQuick quickResult = readQuickTestResult(fullResult.fen(), fullResult.sideCheckingForWin(),
          bothResult.quickResultList());

      switch (fullResult.unwinnableFull()) {
        case UNWINNABLE:
          switch (quickResult) {
            // ok
            case POSSIBLY_WINNABLE:
              break;
            case UNWINNABLE:
              // ok
              break;
            case WINNABLE:
              throw new IllegalArgumentException("Inconsistent CHA result");
            default:
              throw new IllegalArgumentException();
          }
          break;
        case WINNABLE:
          switch (quickResult) {
            case POSSIBLY_WINNABLE:
              // ok
              break;
            case UNWINNABLE:
              throw new IllegalArgumentException("Inconsistent CHA result");
            case WINNABLE:
              // ok
              break;
            default:
              throw new IllegalArgumentException();
          }
          break;
        default:
          throw new IllegalArgumentException();

      }
    }

  }

  private static void check(UnwinnableFullResultTest unwinnableFullResultTest, UnwinnableFull unwinnableFull,
      UnwinnableQuick unwinnableQuick, String pgnFolderName, String pgnFileName) {
    switch (unwinnableFull) {
      case UNWINNABLE:
        switch (unwinnableQuick) {
          case UNWINNABLE:
            assertEquals(UnwinnableFullResultTest.UNWINNABLE, unwinnableFullResultTest);
            break;
          case WINNABLE:
            throw new IllegalArgumentException("Inconsistent data");
          case POSSIBLY_WINNABLE:
            assertEquals(UnwinnableFullResultTest.UNWINNABLE_NOT_QUICK, unwinnableFullResultTest);
            break;
          default:
            throw new IllegalArgumentException();
        }
        break;
      case WINNABLE:
        final PgnFile pgnFile = PgnReader.readPgn(pgnFolderName, pgnFileName);
        final ApiBoard board = GeneralUtility.calculateBoard(pgnFile);

        if (board.isFivefoldRepetition() || board.isSeventyFiftyMove() && !board.isCheckmate()
            || "unwinnable_fivefold_inevitable.pgn".equals(pgnFileName)
            || "unwinnable_seventy_five_move_rule_inevitable.pgn".equals(pgnFileName)) {
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
