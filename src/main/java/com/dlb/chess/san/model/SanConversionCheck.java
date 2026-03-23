package com.dlb.chess.san.model;

import com.dlb.chess.model.SanConversion;

public record SanConversionCheck(boolean isMatch, SanConversion sanConversion) {

  public static final SanConversionCheck IS_NO_MATCH = new SanConversionCheck(false, SanConversion.EMPTY);

}
