package com.dlb.chess.test.unwinnability.againstcha;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.FenValidationException;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.fen.FenParser;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.unwinnability.againstcha.model.ChaBothRead;
import com.dlb.chess.test.unwinnability.againstcha.model.ChaFullRead;
import com.dlb.chess.test.unwinnability.againstcha.model.ChaQuickRead;
import com.dlb.chess.test.unwinnability.enums.ChaFullResult;
import com.dlb.chess.test.unwinnability.enums.ChaMode;
import com.dlb.chess.test.unwinnability.enums.ChaQuickResult;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public abstract class AbstractAgainstCha {

  static final String FEN_MINE = "c:\\temp\\cha\\mine\\myFen.txt";
  static final String FEN_CHA_ANALYSIS_BOTH_FILE_PATH = "c:\\temp\\cha\\mine\\myFenChaBothAnalysis.txt";
  static final String FEN_CHA_ANALYSIS_FULL_FILE_PATH = "C:\\Users\\danie\\git\\D3-Chess-test\\tests\\myFenChaFullAnalysisNotHavingMove.txt";
  static final String FEN_CHA_ANALYSIS_QUICK_FILE_PATH = "c:\\temp\\cha\\mine\\myFenChaQuickAnalysis.txt";

  public static ChaBothRead readChaResultList(String fenAnalysisFilePath) throws Exception {
    final List<ChaFullRead> fullResultList = new ArrayList<>();
    final List<ChaQuickRead> quickResultList = new ArrayList<>();

    final List<String> fileLineList = FileUtility.readFileLines(fenAnalysisFilePath);
    for (final String fileLine : fileLineList) {
      final var fileLineItemArray = NonNullWrapperCommon.split(fileLine, ";");

      final var fileLineItemList = NonNullWrapperCommon.asList(fileLineItemArray);

      final String fenStrRaw = NonNullWrapperCommon.get(fileLineItemList, 0);

      final String fenStr = NonNullWrapperCommon.trim(fenStrRaw);

      final Fen fen;
      try {
        fen = FenParser.parseAdvancedFen(fenStr);
      } catch (final FenValidationException fve) {
        throw new IllegalArgumentException("Illegal FEN of \"" + fenStr + "\" for " + fve.getMessage() + " was found");
      }

      // second column is game id

      final var chaModeStr = NonNullWrapperCommon.get(fileLineItemList, 2);
      if (!ChaMode.exists(chaModeStr)) {
        throw new IllegalArgumentException("Illegal identifier of \"" + chaModeStr + "\" was found");
      }
      final ChaMode chaMode = ChaMode.calculate(chaModeStr);

      final var sideCheckingForWinStr = NonNullWrapperCommon.get(fileLineItemList, 3);
      if (!Side.exists(sideCheckingForWinStr)) {
        throw new IllegalArgumentException("Illegal winning side of \"" + sideCheckingForWinStr + "\" was found");
      }
      final Side sideCheckingForWin = Side.calculate(sideCheckingForWinStr);

      switch (chaMode) {
        case FULL: {
          final var chaFullResultStr = NonNullWrapperCommon.get(fileLineItemList, 4);
          if (!ChaFullResult.exists(chaFullResultStr)) {
            throw new IllegalArgumentException("Illegal full result of \"" + chaFullResultStr + "\" was found");
          }
          final ChaFullResult chaFullResult = ChaFullResult.calculate(chaFullResultStr);

          final String checkmateLine;
          if (chaFullResult != ChaFullResult.WINNABLE || fileLineItemList.size() == 5) {
            // checkmate on the board
            checkmateLine = "";
          } else {
            checkmateLine = NonNullWrapperCommon.get(fileLineItemList, 5);
          }
          fullResultList
              .add(new ChaFullRead(fen, sideCheckingForWin, chaFullResult.getUnwinnableFull(), checkmateLine));
        }
          break;
        case QUICK: {
          final var chaQuickResultStr = NonNullWrapperCommon.get(fileLineItemList, 4);
          if (!ChaQuickResult.exists(chaQuickResultStr)) {
            throw new IllegalArgumentException("Illegal quick result of \"" + chaQuickResultStr + "\" was found");
          }
          final ChaQuickResult chaQuickResult = ChaQuickResult.calculate(chaQuickResultStr);
          quickResultList.add(new ChaQuickRead(fen, sideCheckingForWin, chaQuickResult.getUnwinnableQuick()));
        }
          break;
        default:
          throw new IllegalArgumentException();
      }
    }

    return new ChaBothRead(fullResultList, quickResultList);
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

  static ChaFullRead readFullResult(Fen fen, Side sideCheckingForWin, List<ChaFullRead> fullResultList) {
    for (final ChaFullRead fullResult : fullResultList) {
      if (fullResult.sideCheckingForWin() == sideCheckingForWin && fullResult.fen().equals(fen)) {
        return fullResult;
      }
    }
    throw new IllegalArgumentException("No full test result was found");
  }

  static UnwinnableQuick readQuickTestResult(Fen fen, Side sideCheckingForWin, List<ChaQuickRead> quickResultList) {
    for (final ChaQuickRead quickResult : quickResultList) {
      if (quickResult.sideCheckingForWin() == sideCheckingForWin && quickResult.fen().equals(fen)) {
        return quickResult.unwinnableQuick();
      }
    }
    throw new IllegalArgumentException("No quick test result was found");
  }

  public static void validateChaBothTestResult(ChaBothRead bothResult) {
    if (bothResult.fullResultList().size() != bothResult.quickResultList().size()) {
      throw new IllegalArgumentException("Test result list sizes not match for full and quick test");
    }

    for (final ChaFullRead fullResult : bothResult.fullResultList()) {
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
        case UNDETERMINED:
          switch (quickResult) {
            case POSSIBLY_WINNABLE:
              // ok
              break;
            case UNWINNABLE:
              throw new IllegalArgumentException("Inconsistent CHA result");
            case WINNABLE:
              throw new IllegalArgumentException("Inconsistent CHA result, quick found mate but not full");
            default:
              throw new IllegalArgumentException();
          }
          break;
        default:
          throw new IllegalArgumentException();

      }
    }

  }
}
