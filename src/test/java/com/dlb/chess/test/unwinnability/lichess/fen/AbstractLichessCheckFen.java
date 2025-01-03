package com.dlb.chess.test.unwinnability.lichess.fen;

import java.nio.file.Path;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ConfigurationConstants;

public abstract class AbstractLichessCheckFen {

  static final Path FEN_FOLDER_PATH = NonNullWrapperCommon.resolve(ConfigurationConstants.TEMP_FOLDER_PATH, "cha");

  static final String FEN_FILE_NAME_IN = "lichess-in.txt";

  static final String FEN_FILE_NAME_MINE_RESULT = "lichess-mine-out-quick-2.txt";

}
