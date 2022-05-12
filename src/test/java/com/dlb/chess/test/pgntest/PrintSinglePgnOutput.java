package com.dlb.chess.test.pgntest;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.analysis.model.SingleOutput;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.test.analysis.output.BasicOutput;
import com.dlb.chess.test.pgntest.enums.PgnTest;

public class PrintSinglePgnOutput extends AbstractSinglePgnTest {

  private static final String PGN_FILE_NAME = "threefold_11_1_max_position_repetitions_for_threefold_castling.pgn";

  private static final Logger logger = NonNullWrapperCommon.getLogger(PrintSinglePgnOutput.class);

  public static void main(String[] args) throws Exception {

    final PgnTest pgnTest = PgnExpectedValue.findPgnFileBelongingPgnTestNotHavingTestValuesAlready(PGN_FILE_NAME);
    final SingleOutput testResult = BasicOutput.calculateTestResult(pgnTest.getFolderPath(), PGN_FILE_NAME);

    GeneralUtility.logLines(logger, testResult.output());
  }

}
