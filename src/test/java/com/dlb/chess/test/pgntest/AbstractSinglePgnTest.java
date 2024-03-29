package com.dlb.chess.test.pgntest;

import com.dlb.chess.analysis.model.Analysis;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.pgnall.AbstractPgnTest;

public abstract class AbstractSinglePgnTest extends AbstractPgnTest {

  static boolean runTestCase(String pgnFileName, Analysis analysis) throws Exception {
    final PgnFileTestCase testCase = PgnExpectedValue.findPgnFileBelongingPgnTestCase(pgnFileName);
    return testGame(testCase, analysis);
  }

}
