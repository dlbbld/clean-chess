package com.dlb.chess.test;

import com.dlb.chess.test.pgntest.enums.PgnTestInclusion;

public abstract class RestrictTestConstants {

  // PGN tests — iterate over PGN test case lists
  public static final boolean IS_RESTRICT_PGN_BOARD_API_AGAINST_EACH_OTHER_TEST = true;
  public static final boolean IS_RESTRICT_PGN_FEN_PARSER_ALL_TEST = true;
  public static final boolean IS_RESTRICT_PGN_INSUFFICIENT_MATERIAL_TEST = true;
  public static final boolean IS_RESTRICT_PGN_LEGAL_MOVE_VALIDATION_AGAINST_BOTTOM_UP_TEST = true;
  public static final boolean IS_RESTRICT_PGN_LENIENT_PARSER_API_AGAINST_EACH_OTHER_TEST = true;
  // Formerly IS_RESTRICT_PGN_EXPORT_IDEMPOTENCY_TEST and IS_RESTRICT_PGN_WRITER_TEST — replaced by the curated
  // export-roundtrip smoke subset in PgnExpectedValue.getExportRoundtripSmokeList(). The parser-comparison test
  // (TestStrictPgnParserAgainstLenientPgnParser) moved to PgnExpectedValue.getParserIntegrationSmokeList().
  public static final boolean IS_RESTRICT_PGN_STARTING_POSITION_NONE_AGAINST_INITIAL_FEN_TEST = true;
  public static final boolean IS_RESTRICT_PGN_UNWINNABILITY_QUICK_AGAINST_WINNABILITY_TEST = true;

  public static final PgnTestInclusion PGN_TEST_INCLUSION = PgnTestInclusion.ALL_EXCEPT_LONGEST_POSSIBLE;

  // Long running tests — excluded entirely via JUnit assumeFalse
  public static final boolean IS_EXCLUDE_LONG_RUNNING_SAN_VALIDATE_FORMAT_FAILURE_ORACLE_COMPLEMENT_TEST = true;

}
