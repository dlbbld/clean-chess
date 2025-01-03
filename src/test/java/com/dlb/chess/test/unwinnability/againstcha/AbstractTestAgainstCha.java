package com.dlb.chess.test.unwinnability.againstcha;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.FenAdvancedValidationException;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.fen.FenParserAdvanced;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.unwinnability.againstcha.model.UnwinnabilityRawRead;
import com.dlb.chess.test.unwinnability.enums.UnwinnabilityMode;

public abstract class AbstractTestAgainstCha {

  public static List<UnwinnabilityRawRead> readChaRawResultList(Path fenAnalysisFilePath) throws Exception {
    final List<UnwinnabilityRawRead> resultList = new ArrayList<>();

    final List<String> fileLineList = FileUtility.readFileLines(fenAnalysisFilePath);
    for (final String fileLine : fileLineList) {
      final var fileLineItemArray = NonNullWrapperCommon.split(fileLine, ";");

      final var fileLineItemList = NonNullWrapperCommon.asList(fileLineItemArray);

      final String fenStrRaw = NonNullWrapperCommon.get(fileLineItemList, 0);

      final String fenStr = NonNullWrapperCommon.trim(fenStrRaw);

      final Fen fen;
      try {
        fen = FenParserAdvanced.parseFenAdvanced(fenStr);
      } catch (final FenAdvancedValidationException fve) {
        throw new IllegalArgumentException("Illegal FEN of \"" + fenStr + "\" for " + fve.getMessage() + " was found");
      }

      final var lichessGameId = NonNullWrapperCommon.get(fileLineItemList, 1);

      final var chaModeStr = NonNullWrapperCommon.get(fileLineItemList, 2);
      if (!UnwinnabilityMode.exists(chaModeStr)) {
        throw new IllegalArgumentException("Illegal identifier of \"" + chaModeStr + "\" was found");
      }
      final UnwinnabilityMode chaMode = UnwinnabilityMode.calculate(chaModeStr);

      final var winnerStr = NonNullWrapperCommon.get(fileLineItemList, 3);
      if (!Side.exists(winnerStr)) {
        throw new IllegalArgumentException("Illegal winning side of \"" + winnerStr + "\" was found");
      }
      final Side winner = Side.calculate(winnerStr);

      final var result = NonNullWrapperCommon.get(fileLineItemList, 4);

      final var mateLine = NonNullWrapperCommon.get(fileLineItemList, 5);

      resultList.add(new UnwinnabilityRawRead(fen, lichessGameId, chaMode, winner, result, mateLine));
    }

    return resultList;
  }

  // list of the FEN of the past position for all PGN test case
  static void createFenList() throws Exception {

    for (final PgnTest pgnTest : PgnTest.values()) {
      final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(pgnTest);
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        System.out.println(testCase.fen() + ";noLichessGameId");
      }
    }
  }

}
