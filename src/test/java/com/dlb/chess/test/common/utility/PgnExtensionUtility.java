package com.dlb.chess.test.common.utility;

import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.constants.ChessConstants;

public abstract class PgnExtensionUtility extends ChessConstants {

  public static boolean hasPgnExtension(String pgnFileName) {
    return pgnFileName.endsWith(PGN_EXTENSION_INCLUDING_DOT);
  }

  public static String addPgnExtension(String pgnFileNameWithoutExtension) {
    return pgnFileNameWithoutExtension + PGN_EXTENSION_INCLUDING_DOT;
  }

  public static String removePgnExtension(String pgnFileName) {
    if (!pgnFileName.endsWith(PGN_EXTENSION_INCLUDING_DOT)) {
      throw new IllegalArgumentException("File does not end with \"" + PGN_EXTENSION_INCLUDING_DOT + "\"");
    }

    return Nulls.substring(pgnFileName, 0, pgnFileName.length() - PGN_EXTENSION_INCLUDING_DOT.length());
  }

}
