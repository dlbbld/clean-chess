package com.dlb.chess.generate;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.analysis.Analyzer;
import com.dlb.chess.analysis.model.Analysis;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;

public class GenerateApiCarlosInsufficientMaterialTestCases {

  public static void main(String[] args) throws Exception {
    generateTestCase();
  }

  private static void generateTestCase() throws Exception {

    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.BASIC_INSUFFICIENT_MATERIAL);

    for (final PgnFileTestCase testCase : testCaseList.list()) {

      final Analysis analysis = Analyzer.calculateAnalysis(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());

      final InsufficientMaterial insufficientMaterial = analysis.insufficientMaterial();
      final String fen = analysis.fen();

      final String testCaseTitel = calculateTestCaseTitel(testCase.pgnFileName());
      System.out.println("//" + testCaseTitel);
      System.out.println("board.loadFromFen(\"" + fen + "\");");

      if (insufficientMaterial == InsufficientMaterial.BOTH) {
        System.out.println("assertTrue(board.isInsufficientMaterial());");
      } else {
        System.out.println("assertFalse(board.isInsufficientMaterial());");
      }
      if (insufficientMaterial == InsufficientMaterial.BOTH
          || insufficientMaterial == InsufficientMaterial.WHITE_ONLY) {
        System.out.println("assertTrue(board.isInsufficientMaterial(Side.WHITE));");
      } else {
        System.out.println("assertFalse(board.isInsufficientMaterial(Side.WHITE));");
      }
      if (insufficientMaterial == InsufficientMaterial.BOTH
          || insufficientMaterial == InsufficientMaterial.BLACK_ONLY) {
        System.out.println("assertTrue(board.isInsufficientMaterial(Side.BLACK));");
      } else {
        System.out.println("assertFalse(board.isInsufficientMaterial(Side.BLACK));");
      }
      System.out.println("");
    }
  }

  private static String calculateTestCaseTitel(String game) {
    final var step1 = game.replace("insufficient_material_", "");
    @SuppressWarnings("null") final @NonNull String step2 = step1.replace("_", "v");
    return step2;
  }

}
