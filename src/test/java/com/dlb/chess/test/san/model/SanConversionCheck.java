package com.dlb.chess.test.san.model;

import com.dlb.chess.san.SanConversion;

public record SanConversionCheck(boolean isMatch, SanConversion sanConversion) {

  public static final SanConversionCheck IS_NO_MATCH = new SanConversionCheck(false, SanConversion.EMPTY);

}
