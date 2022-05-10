package com.dlb.chess.test.pgntest.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.exceptions.TestSetupException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.utility.EnPassantCaptureUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;

abstract class AbstractTestBasic implements EnumConstants {

  // we check the following, to detect problems with incompleted test creation and later changes to code or file names:
  // 1a) for each JUnit hardcoded file there is a file in the expected value hardcoded file list
  // 1b) for each file in the expected value hardcoded file list there is a file in the JUnit hardcoded file list
  // 2a) for each file in the JUnit hardcoded file list there is a file in the test folder
  // 2b) for each file in the test folder there is an entry in the JUnit hardcoded file list
  protected static void checkTestFolder(List<String> junitHardcodedPgnFileNameList, PgnTest pgnTest) {

    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(pgnTest);
    final List<String> expectedValueHardcodedFileList = calculatePgnFileNameList(testCaseList.list());

    // 1a)
    for (final String pgnFileName : junitHardcodedPgnFileNameList) {
      if (!expectedValueHardcodedFileList.contains(pgnFileName)) {
        throw new TestSetupException("The JUnit hardcoded file \"" + pgnFileName
            + "\" has no corresponding entry in the expected value hardcoded file list");
      }
    }

    // 1b)
    for (final String pgnFileName : expectedValueHardcodedFileList) {
      if (!junitHardcodedPgnFileNameList.contains(pgnFileName)) {
        throw new TestSetupException("The expected value hardcoded file \"" + pgnFileName
            + "\" has no corresponding entry in the JUnit hardcoded list");
      }
    }

    // 2a)
    for (final String pgnFileName : junitHardcodedPgnFileNameList) {
      if (!FileUtility.exists(pgnTest.getFolderPath(), pgnFileName)) {
        throw new TestSetupException(
            "The JUnit hardcoded file \"" + pgnFileName + "\" does not exist in the test folder");
      }
    }

    // 2b)
    final List<String> testFolderPgnFileNameList = FileUtility.readFileNameList(pgnTest.getFolderPath());
    for (final String pgnFileName : testFolderPgnFileNameList) {
      if (!junitHardcodedPgnFileNameList.contains(pgnFileName)) {
        throw new TestSetupException(
            "The test directory file \"" + pgnFileName + "\" does not exist in the JUnit hardcoded file list");
      }
    }

  }

  private static List<String> calculatePgnFileNameList(List<PgnFileTestCase> testCaseList) {
    final List<String> result = new ArrayList<>();
    for (final PgnFileTestCase testCase : testCaseList) {
      result.add(testCase.pgnFileName());
    }
    return result;
  }

  protected static void checkCheckmate(ApiBoard board) {
    assertTrue(board.isCheck());
    assertTrue(board.isCheckmate());
    assertFalse(board.isStalemate());
  }

  static void checkCapture(Side side, Square fromSquare, Square toSquare, Piece movingPiece, Piece capturedPiece,
      ApiBoard board) {
    assertTrue(board.isCapture());
    assertFalse(board.isCheckmate());
    assertFalse(board.isStalemate());

    final var moveSpecification = new MoveSpecification(side, fromSquare, toSquare);
    final LegalMove expected = new LegalMove(moveSpecification, movingPiece, capturedPiece);
    assertEquals(expected, board.getLastMove());
  }

  static void checkNonCaptureCheck(Side side, Square fromSquare, Square toSquare, Piece movingPiece, ApiBoard board) {
    assertFalse(board.isCapture());
    assertTrue(board.isCheck());
    assertFalse(board.isCheckmate());
    assertFalse(board.isStalemate());

    final var moveSpecification = new MoveSpecification(side, fromSquare, toSquare);
    final LegalMove expected = new LegalMove(moveSpecification, movingPiece, Piece.NONE);
    assertEquals(expected, board.getLastMove());
  }

  static void checkNonCaptureCheckmate(Side side, Square fromSquare, Square toSquare, Piece movingPiece,
      ApiBoard board) {
    assertFalse(board.isCapture());
    assertTrue(board.isCheck());
    assertTrue(board.isCheckmate());
    assertFalse(board.isStalemate());

    final var moveSpecification = new MoveSpecification(side, fromSquare, toSquare);
    final LegalMove expected = new LegalMove(moveSpecification, movingPiece, Piece.NONE);
    assertEquals(expected, board.getLastMove());
  }

  static void checkEnPassantCapture(Side side, Square fromSquare, Square toSquare, ApiBoard board) {

    assertTrue(board.isCapture());
    assertFalse(board.isCheck());
    assertFalse(board.isCheckmate());
    assertFalse(board.isStalemate());

    final LegalMove lastMoveEnPassantCapture = board.getLastMove();
    board.unperformMove();

    final LegalMove secondLastMoveTwoSquareAdvance = board.getLastMove();
    board.unperformMove();

    board.performMove(secondLastMoveTwoSquareAdvance.moveSpecification());
    assertFalse(calculateIsEnPassantCaptureLastMove(board));
    assertEquals(toSquare, board.getEnPassantCaptureTargetSquare());

    board.performMove(lastMoveEnPassantCapture.moveSpecification());
    assertTrue(calculateIsEnPassantCaptureLastMove(board));
    assertEquals(Square.NONE, board.getEnPassantCaptureTargetSquare());

    final var moveSpecification = new MoveSpecification(side, fromSquare, toSquare);
    final Piece movingPiece = Piece.calculatePawnPiece(side);
    final Piece capturedPiece = Piece.calculatePawnPiece(side.getOppositeSide());
    final LegalMove expected = new LegalMove(moveSpecification, movingPiece, capturedPiece);

    assertEquals(expected, lastMoveEnPassantCapture);
  }

  static void checkMovingPiece(Side side, Square fromSquare, Square toSquare, Piece movingPiece, ApiBoard board) {

    assertFalse(board.isCapture());
    assertFalse(board.isCheck());
    assertFalse(board.isCheckmate());
    assertFalse(board.isStalemate());

    final var moveSpecification = new MoveSpecification(side, fromSquare, toSquare);
    final LegalMove expected = new LegalMove(moveSpecification, movingPiece, Piece.NONE);
    assertEquals(expected, board.getLastMove());

  }

  static void checkPromotion(Side side, Square fromSquare, Square toSquare, Piece capturedPiece,
      PromotionPieceType promotionPieceType, ApiBoard board) {

    if (capturedPiece == Piece.NONE) {
      assertFalse(board.isCapture());
    } else {
      assertTrue(board.isCapture());
    }
    assertFalse(board.isCheck());
    assertFalse(board.isCheckmate());
    assertFalse(board.isStalemate());

    final var moveSpecification = new MoveSpecification(side, fromSquare, toSquare, promotionPieceType);
    final Piece movingPiece = Piece.calculatePawnPiece(side);
    final LegalMove expected = new LegalMove(moveSpecification, movingPiece, capturedPiece);
    assertEquals(expected, board.getLastMove());
  }

  static void checkCastle(Side side, CastlingMove castlingMove, ApiBoard board) {
    final var moveSpecification = new MoveSpecification(side, castlingMove);
    final LegalMove expected = new LegalMove(moveSpecification);
    assertEquals(expected, board.getLastMove());
  }

  static void checkDoubleCheck(Piece movingPiece, ApiBoard board) {
    assertFalse(board.isCapture());
    assertTrue(board.isCheck());
    assertFalse(board.isCheckmate());
    assertFalse(board.isStalemate());

    assertEquals(movingPiece, board.getLastMove().movingPiece());
  }

  static void checkDoubleCheckCheckmate(Piece movingPiece, ApiBoard board) {
    assertFalse(board.isCapture());
    assertTrue(board.isCheck());
    assertTrue(board.isCheckmate());
    assertFalse(board.isStalemate());

    assertEquals(movingPiece, board.getLastMove().movingPiece());
  }

  private static boolean calculateIsEnPassantCaptureLastMove(ApiBoard board) {
    return EnPassantCaptureUtility.calculateIsEnPassantCapture(board.getStaticPositionBeforeLastMove(),
        board.getLastMove().moveSpecification());
  }

}
