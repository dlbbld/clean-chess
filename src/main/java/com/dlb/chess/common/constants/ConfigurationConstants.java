package com.dlb.chess.common.constants;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import com.dlb.chess.common.utility.BasicUtility;

public abstract class ConfigurationConstants {

  @SuppressWarnings("null")
  public static final Path TEMP_FOLDER_PATH = Paths.get(System.getProperty("java.io.tmpdir"));
  public static final Path PROJECT_ROOT_FOLDER_PATH = BasicUtility.readProjectFolderPath();

  @SuppressWarnings("null")
  public static final Locale LOCALE = Locale.US;
}
