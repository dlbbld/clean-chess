package com.dlb.chess.test.common.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.enums.MoveSuffixAnnotation;
import com.dlb.chess.pgn.parser.enums.StrictPgnParserValidationProblem;
import com.dlb.chess.pgn.parser.exceptions.StrictPgnParserValidationException;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.test.pgn.parser.PgnCacheForStrictPgnParserTestCases;
import com.dlb.chess.test.pgntest.constants.PgnTestConstants;

/**
 * Restored non-commentary portion of the original {@code TestMoveTextUtilityUsingPgn}. Commentary-specific tests
 * (leading-commentary success/exception, non-leading-commentary success/exception, combined commentary success) are now
 * covered by {@link com.dlb.chess.test.pgn.parser.commentary.TestCommentaryStrict} and intentionally not restored here.
 */
class TestMoveTextUtilityUsingPgn extends AbstractTestMovetextUtility {

  private static final Path PGN_CUSTOM_TEST_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.STRICT_PGN_PARSER_TEST_ROOT_FOLDER_PATH, "movementSpecification");

  // -------------------------------------------------------------------------------------------------
  // Move-suffix annotation — exception and success
  // -------------------------------------------------------------------------------------------------

  private static final Path PGN_TEST_MOVE_SUFFIX_ANNOTATION_EXCEPTION_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PGN_CUSTOM_TEST_FOLDER_PATH, "moveSuffixAnnotation/exception");

  @SuppressWarnings("static-method")
  @Test
  void testMoveSuffixAnnotationException() {
    // 01: `e4!!!` — three !/? characters lex as one suffix token; the resulting "!!!" fails validation.
    checkMoveSuffixAnnotationException("01_example.pgn",
        StrictPgnParserValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID);
    // 02: `e5!x` — a valid suffix `!` immediately followed by a letter with no separating space. The sequential
    // tokenizer splits them cleanly (SUFFIX `!` + SYMBOL `x`), so the diagnosis surfaces as an unexpected-format
    // violation rather than a suffix-value violation. Both are correct detections; the category differs from the
    // previous parser pipeline.
    checkMoveSuffixAnnotationException("02_example.pgn", StrictPgnParserValidationProblem.MOVETEXT_UNEXPECTED_FORMAT);
    // 03: `Qxf7#!?!` — trailing `!?!` lex as one suffix token; "!?!" fails validation.
    checkMoveSuffixAnnotationException("03_example.pgn",
        StrictPgnParserValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID);
  }

  private static void checkMoveSuffixAnnotationException(String pgnFileName,
      StrictPgnParserValidationProblem expectedProblem) {
    var isException = false;
    try {
      PgnCacheForStrictPgnParserTestCases.getPgn(PGN_TEST_MOVE_SUFFIX_ANNOTATION_EXCEPTION_FOLDER_PATH, pgnFileName);
    } catch (final StrictPgnParserValidationException pre) {
      assertEquals(expectedProblem, pre.getStrictPgnParserValidationProblem());
      isException = true;
    }
    assertTrue(isException);
  }

  private static final Path PGN_TEST_MOVE_SUFFIX_ANNOTATION_SUCCESS_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PGN_CUSTOM_TEST_FOLDER_PATH, "moveSuffixAnnotation/success");

  @SuppressWarnings("static-method")
  @Test
  void testMoveSuffixAnnotationSuccess() {
    checkMoveSuffixAnnotationSuccess("01_example.pgn", NonNullWrapperCommon.asList(MoveSuffixAnnotation.BLUNDER,
        MoveSuffixAnnotation.NONE, MoveSuffixAnnotation.BRILLIANT_MOVE));
    checkMoveSuffixAnnotationSuccess("02_example.pgn", NonNullWrapperCommon.asList(MoveSuffixAnnotation.BLUNDER,
        MoveSuffixAnnotation.NONE, MoveSuffixAnnotation.GOOD_MOVE));
  }

  private static void checkMoveSuffixAnnotationSuccess(String pgnFileName,
      List<MoveSuffixAnnotation> moveSuffixAnnotationListExpected) {
    final PgnFile pgnFile = PgnCacheForStrictPgnParserTestCases
        .getPgn(PGN_TEST_MOVE_SUFFIX_ANNOTATION_SUCCESS_FOLDER_PATH, pgnFileName);
    assertEquals(moveSuffixAnnotationListExpected, calculateMoveSuffixAnnotationList(pgnFile.halfMoveList()));
  }

  // -------------------------------------------------------------------------------------------------
  // Combined (SAN + suffix + commentary) — exception (non-commentary subset) and success
  // -------------------------------------------------------------------------------------------------

  private static final Path PGN_TEST_COMBINED_EXCEPTION_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PGN_CUSTOM_TEST_FOLDER_PATH, "combined/exception");

  @SuppressWarnings("static-method")
  @Test
  void testCombinedException() {
    // The original suite included a third combined-exception fixture that fires a commentary-specific error. That
    // case is now covered by TestCommentaryStrict and intentionally omitted here.
    checkCombinedException("01_example.pgn", StrictPgnParserValidationProblem.MOVETEXT_SAN_CHARACTER_INVALID);
    checkCombinedException("02_example.pgn", StrictPgnParserValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID);
  }

  private static void checkCombinedException(String pgnFileName, StrictPgnParserValidationProblem expectedProblem) {
    var isException = false;
    try {
      PgnCacheForStrictPgnParserTestCases.getPgn(PGN_TEST_COMBINED_EXCEPTION_FOLDER_PATH, pgnFileName);
    } catch (final StrictPgnParserValidationException pre) {
      assertEquals(expectedProblem, pre.getStrictPgnParserValidationProblem());
      isException = true;
    }
    assertTrue(isException);
  }

  private static final Path PGN_TEST_COMBINED_SUCCESS_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PGN_CUSTOM_TEST_FOLDER_PATH, "combined/success");

  @SuppressWarnings("static-method")
  @Test
  void testCombinedSuccess() {
    // These fixtures exercise the full combination of leading commentary, trailing commentary, move-suffix
    // annotations, and SAN. Commentary correctness in isolation is covered by TestCommentaryStrict; here we assert
    // that all features parse together without loss of any component.
    checkCombinedSuccess("01_example.pgn", "leading commentary", NonNullWrapperCommon.asList("e4", "d5", "d4"),
        NonNullWrapperCommon.asList(MoveSuffixAnnotation.BLUNDER, MoveSuffixAnnotation.NONE,
            MoveSuffixAnnotation.BRILLIANT_MOVE),
        NonNullWrapperCommon.asList("commentWhite1", "commentBlack", "commentWhite2"));
    checkCombinedSuccess("02_example.pgn", "leading commentary", NonNullWrapperCommon.asList("d5", "a3", "Qd6"),
        NonNullWrapperCommon.asList(MoveSuffixAnnotation.BLUNDER, MoveSuffixAnnotation.NONE,
            MoveSuffixAnnotation.BRILLIANT_MOVE),
        NonNullWrapperCommon.asList("commentBlack1", "commentWhite", "commentBlack2"));
  }

  private static void checkCombinedSuccess(String pgnFileName, String leadingCommentaryExpected,
      List<String> sanListExpected, List<MoveSuffixAnnotation> moveSuffixAnnotationListExpected,
      List<String> commentaryListExpected) {
    final PgnFile pgnFile = PgnCacheForStrictPgnParserTestCases.getPgn(PGN_TEST_COMBINED_SUCCESS_FOLDER_PATH,
        pgnFileName);
    assertEquals(leadingCommentaryExpected, pgnFile.leadingCommentary().value());
    assertEquals(sanListExpected, calculateSanList(pgnFile.halfMoveList()));
    assertEquals(moveSuffixAnnotationListExpected, calculateMoveSuffixAnnotationList(pgnFile.halfMoveList()));
    assertEquals(commentaryListExpected, calculateCommentaryList(pgnFile.halfMoveList()));
  }
}
