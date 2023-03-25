package com.dlb.chess.common.utility;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ChessConstants;

public abstract class PgnExtensionUtility extends ChessConstants {

  public static boolean hasPgnFileExtension(String pgnFileName) {
    return pgnFileName.endsWith(PGN_FILE_EXTENSION_INCLUDING_DOT);
  }

  public static String addPgnFileExtension(String pgnFileNameWithoutExtension) {
    return pgnFileNameWithoutExtension + PGN_FILE_EXTENSION_INCLUDING_DOT;
  }

  public static String removePgnFileExtension(String pgnFileName) {
    if (!pgnFileName.endsWith(PGN_FILE_EXTENSION_INCLUDING_DOT)) {
      throw new IllegalArgumentException("File does not end with \"" + PGN_FILE_EXTENSION_INCLUDING_DOT + "\"");
    }

    return NonNullWrapperCommon.substring(pgnFileName, 0,
        pgnFileName.length() - PGN_FILE_EXTENSION_INCLUDING_DOT.length());
  }

}
