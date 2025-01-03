package com.dlb.chess.generate;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.analysis.Analyzer;
import com.dlb.chess.analysis.model.Analysis;
import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ConfigurationConstants;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.common.utility.RepetitionUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.PgnTestConstants;

public class GeneratePythonTestCases implements EnumConstants {

  private static final Logger logger = NonNullWrapperCommon.getLogger(GeneratePythonTestCases.class);

  private static final int PRINT_MOVES_INTERVAL = 100;
  private static final int PRINT_GENERATED_LINES_INTERVAL = 1000;

  private static final int WRITE_LINE_INTERVAL = 100000;
  private static final Path PYTHON_SCRIPT = FileUtility.calculateFilePath(
      ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH.resolve("../python-chess"), "test_play_game.py");

  public static void main(String[] args) throws Exception {
    generatePythonTestCase();
  }

  public static void generatePythonTestCase() throws Exception {

    FileUtility.deleteFile(PYTHON_SCRIPT);

    logger.info("BEGIN generating code");
    List<String> codeLineList = new ArrayList<>();
    final List<Boolean> counterList = new ArrayList<>();

    processPythonCodeLine("import chess", counterList, codeLineList);
    processPythonCodeLine("import unittest", counterList, codeLineList);
    processPythonCodeLine("", counterList, codeLineList);
    processPythonCodeLine("class GameTestCase(unittest.TestCase):", counterList, codeLineList);

    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getRestrictedTestListList()) {
      final Path folderPath = testCaseList.pgnTest().getFolderPath();
      logger.info("Processing folder " + folderPath);
      processPythonCodeLine("", counterList, codeLineList);
      final String folderIndication;
      {
        final var folderIndication0 = folderPath.toAbsolutePath().toString();
        final var folderIndication1 = folderIndication0
            .replace(PgnTestConstants.PGN_TEST_ROOT_FOLDER_PATH.toAbsolutePath().toString(), "");
        folderIndication = folderIndication1.replace("\\", "_");
      }
      processPythonCodeLine("  def test_" + folderIndication + "(self):", counterList, codeLineList);
      processPythonCodeLine("    print(\"Processing module " + folderIndication + "\")", counterList, codeLineList);
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        logger.info("Processing game " + testCase.pgnFileName());

        final Analysis analysis = Analyzer.calculateAnalysis(folderPath, testCase.pgnFileName());
        processPythonCodeLine("", counterList, codeLineList);
        processPythonCodeLine("    #" + testCase.pgnFileName(), counterList, codeLineList);
        processPythonCodeLine("    print(\"  Processing game " + testCase.pgnFileName() + "\")", counterList,
            codeLineList);
        processPythonCodeLine("    board = chess.Board()", counterList, codeLineList);

        final ApiBoard boardPlayAlong = new Board();
        for (final HalfMove halfMove : analysis.halfMoveList()) {
          boardPlayAlong.performMove(halfMove.moveSpecification());
          processPythonCodeLine("    board.push_san(\"" + halfMove.san() + "\")", counterList, codeLineList);
          final var isMadeByWhite = halfMove.moveSpecification().havingMove().getIsWhite();
          if (isMadeByWhite && halfMove.fullMoveNumber() % PRINT_MOVES_INTERVAL == 0) {
            final var moveDescription = halfMove.fullMoveNumber() + "." + halfMove.san();
            processPythonCodeLine("    print(\"Performed " + moveDescription + "\")", counterList, codeLineList);
          }

          processPythonCodeLine("    self.assertEqual(board.is_check(), "
              + convertJavaBooleanToPythonBoolean(boardPlayAlong.isCheck()) + ")", counterList, codeLineList);

          processPythonCodeLine("    self.assertEqual(board.is_checkmate(), "
              + convertJavaBooleanToPythonBoolean(boardPlayAlong.isCheckmate()) + ")", counterList, codeLineList);

          processPythonCodeLine("    self.assertEqual(board.is_stalemate(), "
              + convertJavaBooleanToPythonBoolean(boardPlayAlong.isStalemate()) + ")", counterList, codeLineList);

          final var isInsufficientMaterial = boardPlayAlong.isInsufficientMaterial();
          processPythonCodeLine("    self.assertEqual(board.is_insufficient_material(), "
              + convertJavaBooleanToPythonBoolean(isInsufficientMaterial) + ")", counterList, codeLineList);

          final var hasInsufficientMaterialWhite = boardPlayAlong.isInsufficientMaterial(WHITE);
          processPythonCodeLine("    self.assertEqual(board.has_insufficient_material(chess.WHITE), "
              + convertJavaBooleanToPythonBoolean(hasInsufficientMaterialWhite) + ")", counterList, codeLineList);

          final var hasInsufficientMaterialBlack = boardPlayAlong.isInsufficientMaterial(BLACK);
          processPythonCodeLine("    self.assertEqual(board.has_insufficient_material(chess.BLACK), "
              + convertJavaBooleanToPythonBoolean(hasInsufficientMaterialBlack) + ")", counterList, codeLineList);

          final var canClaimThreefoldRepetitionRule = boardPlayAlong.canClaimThreefoldRepetitionRule();
          processPythonCodeLine(
              "    self.assertEqual(board.can_claim_threefold_repetition(), "
                  + convertJavaBooleanToPythonBoolean(canClaimThreefoldRepetitionRule) + ")",
              counterList, codeLineList);

          final var canClaimFiftyMoveRule = boardPlayAlong.canClaimFiftyMoveRule();
          processPythonCodeLine("    self.assertEqual(board.can_claim_fifty_moves(), "
              + convertJavaBooleanToPythonBoolean(canClaimFiftyMoveRule) + ")", counterList, codeLineList);

          final var isTwoFoldRepetition = RepetitionUtility.getCountRepetition(halfMove,
              EnPassantCaptureRuleThreefold.DO_NOT_IGNORE) >= 2;
          processPythonCodeLine("    self.assertEqual(board.is_repetition(2), "
              + convertJavaBooleanToPythonBoolean(isTwoFoldRepetition) + ")", counterList, codeLineList);

          final var isThreeFoldRepetition = RepetitionUtility.getCountRepetition(halfMove,
              EnPassantCaptureRuleThreefold.DO_NOT_IGNORE) >= 3;
          processPythonCodeLine("    self.assertEqual(board.is_repetition(), "
              + convertJavaBooleanToPythonBoolean(isThreeFoldRepetition) + ")", counterList, codeLineList);

          final var isFourFoldRepetition = RepetitionUtility.getCountRepetition(halfMove,
              EnPassantCaptureRuleThreefold.DO_NOT_IGNORE) >= 4;
          processPythonCodeLine("    self.assertEqual(board.is_repetition(4), "
              + convertJavaBooleanToPythonBoolean(isFourFoldRepetition) + ")", counterList, codeLineList);

          final var isFiveFoldRepetitionRule = RepetitionUtility.getCountRepetition(halfMove,
              EnPassantCaptureRuleThreefold.DO_NOT_IGNORE) >= 5;
          processPythonCodeLine("    self.assertEqual(board.is_fivefold_repetition(), "
              + convertJavaBooleanToPythonBoolean(isFiveFoldRepetitionRule) + ")", counterList, codeLineList);

          // no boolean methods for fifty-move rule and seventy-five-move rule in python

          final var halfMoveClock = boardPlayAlong.getHalfMoveClock();
          processPythonCodeLine("    self.assertEqual(board.halfmove_clock, " + halfMoveClock + ")", counterList,
              codeLineList);

          // test legal moves
          final Set<String> legalMovesUci = boardPlayAlong.getLegalMovesUci();
          final var expectedSize = legalMovesUci.size();

          // test number of legal moves
          processPythonCodeLine("    self.assertEqual(board.legal_moves.count(), " + expectedSize + ")", counterList,
              codeLineList);

          // test legal moves values
          for (final String uci : legalMovesUci) {
            processPythonCodeLine("    move = chess.Move.from_uci(\"" + uci + "\")", counterList, codeLineList);
            processPythonCodeLine("    self.assertTrue(move in board.legal_moves)", counterList, codeLineList);
          }

          if (codeLineList.size() >= WRITE_LINE_INTERVAL) {
            FileUtility.appendFile(PYTHON_SCRIPT, codeLineList);
            codeLineList = new ArrayList<>();
          }
        }
      }
    }
    // write remaining
    FileUtility.appendFile(PYTHON_SCRIPT, codeLineList);
    logger.info("END generating code");
  }

  private static String convertJavaBooleanToPythonBoolean(boolean value) {
    if (value) {
      return "True";
    }
    return "False";
  }

  private static void processPythonCodeLine(String codeLine, List<Boolean> counterList, List<String> codeLineList) {
    // System.out.println(codeLine);
    codeLineList.add(codeLine);
    counterList.add(true);
    // the code line list is emptied after appending, so we need a separate list for the count
    if (counterList.size() % PRINT_GENERATED_LINES_INTERVAL == 0) {
      System.out.println("Generated " + counterList.size() + " lines");
    }
  }
}
