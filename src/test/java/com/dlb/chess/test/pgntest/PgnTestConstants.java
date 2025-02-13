package com.dlb.chess.test.pgntest;

import java.nio.file.Path;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ConfigurationConstants;
import com.dlb.chess.test.pgntest.enums.PgnTestInclusion;

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

  public static final Path PGN_TEST_ROOT_FOLDER_PATH = NonNullWrapperCommon
      .resolve(ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH, "src/test/resources/pgn");

  public static final Path PGN_READER_NON_STRICT_TEST_ROOT_FOLDER_PATH = NonNullWrapperCommon
      .resolve(ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH, "src/test/resources/pgnReader/nonStrict");

  public static final Path PGN_READER_STRICT_TEST_ROOT_FOLDER_PATH = NonNullWrapperCommon
      .resolve(ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH, "src/test/resources/pgnReader/strict");

  public static final Path PGN_READER_LINE_BREAKS_TEST_ROOT_FOLDER_PATH = NonNullWrapperCommon
      .resolve(ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH, "src/test/resources/pgnReader/lineBreaks");

  public static final Path PGN_READER_UTF8_TEST_ROOT_FOLDER_PATH = NonNullWrapperCommon
      .resolve(ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH, "src/test/resources/pgnReader/utf8");

  public static final Path PGN_EXPORT_TEST_ROOT_FOLDER_PATH = NonNullWrapperCommon
      .resolve(ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH, "src/test/resources/pgnExport");

  public static final Path PGN_EXPORT_LINE_BREAKS_TEST_ROOT_FOLDER_PATH = NonNullWrapperCommon
      .resolve(ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH, "src/test/resources/pgnExport/lineBreaks");

  public static final Path PGN_EXPORT_UTF8_TEST_ROOT_FOLDER_PATH = NonNullWrapperCommon
      .resolve(ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH, "src/test/resources/pgnExport/utf8");
}
