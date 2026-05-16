package com.dlb.chess.test.common.utility;

import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.constants.ChessConstants;

public abstract class PgnExtensionUtility extends ChessConstants {

  public static boolean hasPgnExtension(String pgnName) {
    return pgnName.endsWith(PGN_EXTENSION_INCLUDING_DOT);
  }

  public static String addPgnExtension(String pgnNameWithoutExtension) {
    return pgnNameWithoutExtension + PGN_EXTENSION_INCLUDING_DOT;
  }

  public static String removePgnExtension(String pgnName) {
    if (!pgnName.endsWith(PGN_EXTENSION_INCLUDING_DOT)) {
      throw new IllegalArgumentException("File does not end with \"" + PGN_EXTENSION_INCLUDING_DOT + "\"");
    }

    return Nulls.substring(pgnName, 0, pgnName.length() - PGN_EXTENSION_INCLUDING_DOT.length());
  }

}
