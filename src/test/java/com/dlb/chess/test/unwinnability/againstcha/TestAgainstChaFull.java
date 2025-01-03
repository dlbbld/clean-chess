package com.dlb.chess.test.unwinnability.againstcha;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ConfigurationConstants;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.test.unwinnability.againstcha.model.UnwinnabilityFullRead;
import com.dlb.chess.test.unwinnability.againstcha.model.UnwinnabilityRawRead;
import com.dlb.chess.test.unwinnability.enums.UnwinnabilityMode;
import com.dlb.chess.unwinnability.full.UnwinnableFullAnalyzer;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.full.model.UnwinnableFullAnalysis;

public class TestAgainstChaFull extends AbstractTestAgainstCha {

  // lichess 30k
  // Difference found:
  // CHA: winnable, mine: undetermined
  // 8/6bk/8/1Q6/3P4/8/PP3PPP/3R2K1 b - - 0 36;eSHrCMEQ;full;b;undetermined;
  // 60'000 positions in 7 hours, about 0.4 seconds per position

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestAgainstChaFull.class);

  // format: fen;lichessGameId;mode;side;result;mateLine
  private static final Path CHA_FULL_RESULT = ConfigurationConstants.TEMP_FOLDER_PATH.resolve("chaFullResult.txt");
  // format: fen;lichessGameId;mode;side;result;mateLine
  private static final Path MINE_OUT = ConfigurationConstants.TEMP_FOLDER_PATH.resolve("mineFullResult.txt");

  public static void main(String[] args) throws Exception {
    checkMineFullAgainstChaFull();
  }

  public static List<UnwinnabilityFullRead> readFullResultList(Path path) throws Exception {
    final List<UnwinnabilityFullRead> resultList = new ArrayList<>();

    final List<UnwinnabilityRawRead> chaRawResultList = readChaRawResultList(path);
    for (final UnwinnabilityRawRead rawResult : chaRawResultList) {
      if (rawResult.mode() != UnwinnabilityMode.FULL) {
        throw new IllegalArgumentException("Invalid testing line, expected full result");
      }
      final UnwinnableFull unwinnableFull = UnwinnableFull.calculate(rawResult.result());
      final UnwinnabilityFullRead chaFullRead = new UnwinnabilityFullRead(rawResult.fen(), rawResult.lichessGameId(),
          rawResult.winner(), unwinnableFull, rawResult.mateLine());
      resultList.add(chaFullRead);
    }
    return resultList;

  }

  private static void checkMineFullAgainstChaFull() throws Exception {

    final List<UnwinnabilityFullRead> chaFullResultList = readFullResultList(CHA_FULL_RESULT);

    final List<UnwinnabilityFullRead> mineFullResultList;
    if (FileUtility.exists(MINE_OUT)) {
      mineFullResultList = readFullResultList(MINE_OUT);
    } else {
      mineFullResultList = new ArrayList<>();
    }

    logger.printf(Level.INFO, "%d positions to test", chaFullResultList.size());
    logger.printf(Level.INFO, "%d positions already tested", mineFullResultList.size());

    final var remaining = chaFullResultList.size() - mineFullResultList.size();
    logger.printf(Level.INFO, "%d positions remaining to test", remaining);

    if (remaining == 0) {
      logger.info("Testing completed");
      return;
    }

    final int startIndex;
    if (mineFullResultList.size() > 0) {
      final var lastProcessedIndex = mineFullResultList.size() - 1;

      final UnwinnabilityFullRead lastChaResult = NonNullWrapperCommon.get(chaFullResultList, lastProcessedIndex);
      final UnwinnabilityFullRead lastMineResult = NonNullWrapperCommon.get(mineFullResultList, lastProcessedIndex);

      if (lastChaResult.fen() == lastMineResult.fen()
          || !lastChaResult.lichessGameId().equals(lastMineResult.lichessGameId())) {
        throw new IllegalArgumentException("The test results lists are not in sync");
      }
      startIndex = lastProcessedIndex + 1;
    } else {
      startIndex = 0;
    }

    var testCounter = 0;
    for (var i = startIndex; i < chaFullResultList.size(); i++) {
      testCounter++;

      final UnwinnabilityFullRead fullChaResult = NonNullWrapperCommon.get(chaFullResultList, i);

      final Board board = new Board(fullChaResult.fen());
      final UnwinnableFullAnalysis fullMineResult = UnwinnableFullAnalyzer.unwinnableFull(board,
          fullChaResult.winner());

      final StringBuilder mineOut = new StringBuilder();
      mineOut.append(fullChaResult.fen().fen()).append(";");
      mineOut.append(fullChaResult.lichessGameId()).append(";");
      mineOut.append(UnwinnabilityMode.FULL.getIdentifier()).append(";");
      mineOut.append(fullChaResult.winner().getFenLetter()).append(";");
      mineOut.append(fullMineResult.unwinnableFull().getIdentifier()).append(";");
      final String mateLine = GeneralUtility.composeCheckmateLine(fullMineResult.mateLine());
      mineOut.append(mateLine);

      final String mineOutStr = NonNullWrapperCommon.toString(mineOut);

      FileUtility.appendFile(MINE_OUT, mineOutStr);

      if (fullChaResult.unwinnableFull() != fullMineResult.unwinnableFull()) {
        System.out.println("Difference found:");
        System.out.println("CHA: " + fullChaResult.unwinnableFull().getIdentifier() + ", mine: "
            + fullMineResult.unwinnableFull().getIdentifier());
        System.out.println(mineOutStr);
      }

      if (testCounter % 50 == 0) {
        logger.printf(Level.INFO, "Processed %d / %d", testCounter, remaining);
      }
    }
  }

}
