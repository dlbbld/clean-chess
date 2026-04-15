package com.dlb.chess.test.pgntest.constants;

import com.dlb.chess.test.pgntest.enums.PgnTestInclusion;

public abstract class PgnRestrictTestConstants {

  public static final boolean IS_RESTRICT_BOARD_API_AGAINST_EACH_OTHER_TEST = true;
  public static final boolean IS_RESTRICT_FEN_PARSER_ALL_TEST = true;
  public static final boolean IS_RESTRICT_INSUFFICIENT_MATERIAL_TEST = true;
  public static final boolean IS_RESTRICT_LEGAL_MOVE_VALIDATION_AGAINST_BOTTOM_UP_TEST = true;
  public static final boolean IS_RESTRICT_LENIENT_PGN_PARSER_API_AGAINST_EACH_OTHER_TEST = true;
  public static final boolean IS_RESTRICT_PGN_EXPORT_IDEMPOTENCY_TEST = true;
  public static final boolean IS_RESTRICT_PGN_WRITER_TEST = true;
  public static final boolean IS_RESTRICT_STARTING_POSITION_NONE_AGAINST_INITIAL_FEN_TEST = true;
  public static final boolean IS_RESTRICT_PGN_UNWINNABILITY_QUICK_AGAINST_WINNABILITY_TEST = true;

  public static final PgnTestInclusion PGN_TEST_INCLUSION = PgnTestInclusion.ALL_EXCEPT_LONGEST_POSSIBLE;

}
