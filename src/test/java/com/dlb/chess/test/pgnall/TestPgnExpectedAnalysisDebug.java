package com.dlb.chess.test.pgnall;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;

/**
 * Analyzes every PGN test case across the restricted test corpus and validates expected values.
 *
 * <p>
 * <b>Lock-down convention.</b> The narrowing flags below ({@code IS_CHECK_FROM_PGN_TEST},
 * {@code IS_CHECK_ONLY_PGN_TEST}, {@code IS_CHECK_FROM_PGN_FILE_NAME}) let a developer narrow the test scope during
 * local debugging, but {@link #IS_DISABLED} prevents the entire {@link #test} method from running by default. Workflow:
 * <ol>
 * <li>Flip {@link #IS_DISABLED} to {@code false} locally,</li>
 * <li>Optionally set exactly one narrowing flag to {@code true},</li>
 * <li>Run the test in the IDE,</li>
 * <li><b>Restore {@link #IS_DISABLED} to {@code true} before committing.</b></li>
 * </ol>
 *
 * <p>
 * The {@link #testIsDisabledMustBeTrueOnCommit} guard test asserts the constant is {@code true} on commit, so
 * accidentally committing it as {@code false} fails the build — turning the discipline into a CI-checked invariant
 * rather than relying on developer vigilance.
 */
class TestPgnExpectedAnalysisDebug extends AbstractPgnTest {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestPgnExpectedAnalysisDebug.class);

  /**
   * Lock-down constant. MUST be {@code true} on commit. Flip locally to {@code false} for debugging only and restore
   * before committing — the guard test {@link #testIsDisabledMustBeTrueOnCommit} fails the build if this is committed
   * as {@code false}.
   */
  public static final boolean IS_DISABLED = true;

  // for custom testing
  // order of priority as listed
  private static final boolean IS_CHECK_FROM_PGN_TEST = false;
  private static final PgnTest CHECK_FROM_PGN_TEST = PgnTest.SPECIAL;

  private static final boolean IS_CHECK_ONLY_PGN_TEST = false;
  private static final PgnTest CHECK_ONLY_PGN_TEST = PgnTest.BASIC_SEVENTY_FIVE;

  private static final boolean IS_CHECK_FROM_PGN_FILE_NAME = false;
  private static final String CHECK_PGN_FILE_NAME = "unfair_lichess_helpmate_zmelXKvA.pgn";

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    assumeFalse(IS_DISABLED, "Class disabled — flip IS_DISABLED to false to run");

    var isFoundTest = false;
    var isFromOrAfterFromFolder = false;
    var isPgnFileNameOrAfterPgnFileName = false;
    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getRestrictedTestListList()) {
      if (!isFromOrAfterFromFolder && CHECK_FROM_PGN_TEST == testCaseList.pgnTest()) {
        isFromOrAfterFromFolder = true;
      }

      if (isContinueDirectoryLevel(IS_CHECK_FROM_PGN_TEST, IS_CHECK_ONLY_PGN_TEST, isFromOrAfterFromFolder,
          testCaseList.pgnTest())) {
        continue;
      }

      for (final PgnFileTestCase testCase : testCaseList.list()) {
        if (!isPgnFileNameOrAfterPgnFileName && CHECK_PGN_FILE_NAME.equals(testCase.pgnFileName())) {
          isPgnFileNameOrAfterPgnFileName = true;
        }

        if (isContinueFileLevel(IS_CHECK_FROM_PGN_TEST, IS_CHECK_ONLY_PGN_TEST, IS_CHECK_FROM_PGN_FILE_NAME,
            isPgnFileNameOrAfterPgnFileName)) {
          continue;
        }

        isFoundTest = true;
        logger.info(testCase.pgnFileName());

        testGame(testCaseList, testCase);
      }
    }

    if (!isFoundTest) {
      throw new IllegalArgumentException("No test found with the test setup");
    }
  }

  /**
   * Guard: fails the build if {@link #IS_DISABLED} is committed as {@code false}. When the developer is locally
   * debugging with {@code IS_DISABLED = false}, this test fails (expected locally), reminding them to restore it before
   * committing.
   */
  @SuppressWarnings("static-method")
  @Test
  void testIsDisabledMustBeTrueOnCommit() {
    assertTrue(IS_DISABLED, "TestPgnExpectedAnalysis.IS_DISABLED must be true on commit. "
        + "Restore to true before committing — false is for local debugging only.");
  }

  // must be in method with parameters for constants to avoid dead code warnings
  private static boolean isContinueDirectoryLevel(boolean isCheckFromPgnTest, boolean isCheckOnlyPgnTest,
      boolean isFromOrAfterFromFolder, PgnTest currentPgnTest) {
    if (isCheckFromPgnTest) {
      if (!isFromOrAfterFromFolder) {
        return true;
      }
    } else if (isCheckOnlyPgnTest && CHECK_ONLY_PGN_TEST != currentPgnTest) {
      return true;
    }
    return false;
  }

  // must be in method with parameters for constants to avoid dead code warnings
  private static boolean isContinueFileLevel(boolean isCheckFromPgnTest, boolean isCheckOnlyPgnTest,
      boolean isCheckFromPgnFileName, boolean isPgnFileNameOrAfterPgnFileName) {
    if (!isCheckFromPgnTest && !isCheckOnlyPgnTest && isCheckFromPgnFileName) {
      return !isPgnFileNameOrAfterPgnFileName;
    }
    return false;
  }

}
