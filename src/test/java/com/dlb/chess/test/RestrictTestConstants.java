package com.dlb.chess.test;

import com.dlb.chess.test.pgntest.enums.PgnTestInclusion;

public abstract class RestrictTestConstants {

  // PGN tests — iterate over PGN test case lists
  public static final boolean IS_RESTRICT_PGN_BOARD_API_AGAINST_EACH_OTHER_TEST = true;
  public static final boolean IS_RESTRICT_PGN_FEN_PARSER_ALL_TEST = true;
  public static final boolean IS_RESTRICT_PGN_INSUFFICIENT_MATERIAL_TEST = true;
  public static final boolean IS_RESTRICT_PGN_LEGAL_MOVE_VALIDATION_AGAINST_BOTTOM_UP_TEST = true;
  public static final boolean IS_RESTRICT_PGN_LENIENT_PARSER_API_AGAINST_EACH_OTHER_TEST = true;
  // The three constants below toggle between a curated smoke subset (true) and the full ALL_EXCEPT_LONGEST_POSSIBLE
  // corpus (false). Smoke subsets live in PgnExpectedValue.getParserIntegrationSmokeList() and
  // getExportRoundtripSmokeList(). Flip to false for a pre-release / pre-merge full-coverage sweep — search-replace
  // true → false across this file switches every restriction at once, including these.
  public static final boolean IS_RESTRICT_PGN_STRICT_AGAINST_LENIENT_TEST = true;
  public static final boolean IS_RESTRICT_PGN_EXPORT_IDEMPOTENCY_TEST = true;
  public static final boolean IS_RESTRICT_PGN_WRITER_TEST = true;
  public static final boolean IS_RESTRICT_PGN_STARTING_POSITION_NONE_AGAINST_INITIAL_FEN_TEST = true;
  public static final boolean IS_RESTRICT_PGN_UNWINNABILITY_QUICK_AGAINST_WINNABILITY_TEST = true;

  public static final PgnTestInclusion PGN_TEST_INCLUSION = PgnTestInclusion.ALL_EXCEPT_LONGEST_POSSIBLE;

  // Long running tests — excluded entirely via JUnit assumeFalse
  public static final boolean IS_EXCLUDE_LONG_RUNNING_SAN_VALIDATE_FORMAT_FAILURE_ORACLE_COMPLEMENT_TEST = true;

}
