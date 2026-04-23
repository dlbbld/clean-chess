package com.dlb.chess.test;

import com.dlb.chess.test.pgntest.enums.PgnTestInclusion;

public abstract class RestrictTestConstants {

  /**
   * Master gate for all {@code IS_RESTRICT_PGN_*_TEST} flags below. Set to {@code false} to force every restricted
   * PGN test to run at full coverage in one flip — avoids having to search-replace each individual flag. Individual
   * flags that want to opt out of the master gate can be authored as a plain {@code true} instead of
   * {@code true && IS_RESTRICT_PGN}; that keeps the restriction on regardless.
   */
  public static final boolean IS_RESTRICT_PGN = true;

  // PGN tests — iterate over PGN test case lists. Each flag is the conjunction of its own restriction and the
  // master gate above, so one edit to IS_RESTRICT_PGN turns off every individual restriction simultaneously.
  public static final boolean IS_RESTRICT_PGN_BOARD_API_AGAINST_EACH_OTHER_TEST = true && IS_RESTRICT_PGN;
  public static final boolean IS_RESTRICT_PGN_FEN_PARSER_ALL_TEST = true && IS_RESTRICT_PGN;
  public static final boolean IS_RESTRICT_PGN_INSUFFICIENT_MATERIAL_TEST = true && IS_RESTRICT_PGN;
  public static final boolean IS_RESTRICT_PGN_LEGAL_MOVE_VALIDATION_AGAINST_BOTTOM_UP_TEST = true && IS_RESTRICT_PGN;
  public static final boolean IS_RESTRICT_PGN_LENIENT_PARSER_API_AGAINST_EACH_OTHER_TEST = true && IS_RESTRICT_PGN;
  // The three flags below toggle between a curated smoke subset (true) and the full ALL_EXCEPT_LONGEST_POSSIBLE
  // corpus (false). Smoke subsets live in PgnExpectedValue.getParserIntegrationSmokeList() and
  // getExportRoundtripSmokeList(). Flipping IS_RESTRICT_PGN to false turns these off along with the rest.
  public static final boolean IS_RESTRICT_PGN_STRICT_AGAINST_LENIENT_TEST = true && IS_RESTRICT_PGN;
  public static final boolean IS_RESTRICT_PGN_EXPORT_IDEMPOTENCY_TEST = true && IS_RESTRICT_PGN;
  public static final boolean IS_RESTRICT_PGN_WRITER_TEST = true && IS_RESTRICT_PGN;
  public static final boolean IS_RESTRICT_PGN_STARTING_POSITION_NONE_AGAINST_INITIAL_FEN_TEST = true && IS_RESTRICT_PGN;
  public static final boolean IS_RESTRICT_PGN_UNWINNABILITY_QUICK_AGAINST_WINNABILITY_TEST = true && IS_RESTRICT_PGN;

  public static final PgnTestInclusion PGN_TEST_INCLUSION = PgnTestInclusion.ALL_EXCEPT_LONGEST_POSSIBLE;

  // Long running tests — excluded entirely via JUnit assumeFalse
  public static final boolean IS_EXCLUDE_LONG_RUNNING_SAN_VALIDATE_FORMAT_FAILURE_ORACLE_COMPLEMENT_TEST = true;

}
