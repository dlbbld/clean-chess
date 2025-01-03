package com.dlb.chess.common.constants;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import com.dlb.chess.common.utility.BasicUtility;

public abstract class ConfigurationConstants {

  // when using the speaking chess board, the system variable GOOGLE_APPLICATION_CREDENTIALS must be set to the path of
  // the json file for the google text to speech service account (non free service)

  public static final Path TEMP_FOLDER_PATH = Paths.get(System.getProperty("java.io.tmpdir"));
  public static final Path PROJECT_ROOT_FOLDER_PATH = BasicUtility.readProjectFolderPath();

  public static final int DGT_MY_BLUETOOTH_BOARD_ID = 23944;
  public static final int DGT_MY_USB_BOARD_ID = 43462;

  public static final int DGT_ACTIVE_BOARD_ID = DGT_MY_BLUETOOTH_BOARD_ID;

  @SuppressWarnings("null")
  public static final Locale LOCALE = Locale.US;
}
