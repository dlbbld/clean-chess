package com.dlb.chess.test.pgntest;

import java.util.ArrayList;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ConfigurationConstants;
import com.dlb.chess.test.pgntest.enums.PgnTestInclusion;
import com.google.common.collect.ImmutableList;

public abstract class PgnTestConstants {

  public static final boolean IS_RESTRICT_PGN_READER_API_AGAINST_EACH_OTHER_TEST = true;
  public static final boolean IS_RESTRICT_BOARD_API_AGAINST_EACH_OTHER_TEST = true;
  public static final boolean IS_RESTRICT_LEGAL_MOVE_VALIDATION_AGAINST_BOTTOM_UP_TEST = true;
  public static final boolean IS_RESTRICT_PGN_WRITER_TEST = true;
  public static final boolean IS_RESTRICT_STARTING_POSITION_NONE_AGAINST_INITIAL_FEN_TEST = true;
  public static final boolean IS_RESTRICT_FEN_PARSER_ALL_TEST = true;
  public static final boolean IS_RESTRICT_INSUFFICIENT_MATERIAL_TEST = true;
  public static final boolean IS_RESTRICT_PGN_EXPORT_IDEMPOTENCY_TEST = true;

  public static final PgnTestInclusion PGN_TEST_INCLUSION = PgnTestInclusion.ALL_EXCEPT_LONGEST_POSSIBLE;

  public static final ImmutableList<String> SEQUENCE_REPETITION_NOT_DEFINED = NonNullWrapperCommon
      .copyOfList(new ArrayList<>());

  public static final String PGN_TEST_ROOT_FOLDER_PATH = ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH
      + "\\src\\test\\resources\\pgn";

  public static final String PGN_READER_TEST_ROOT_FOLDER_PATH = ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH
      + "\\src\\test\\resources\\pgnReaderTest";

  public static final String PGN_READER_STRICT_TEST_ROOT_FOLDER_PATH = ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH
      + "\\src\\test\\resources\\pgnReaderStrictTest";

  public static final String PGN_Export_TEST_ROOT_FOLDER_PATH = ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH
      + "\\src\\test\\resources\\pgnExportTest";

}
