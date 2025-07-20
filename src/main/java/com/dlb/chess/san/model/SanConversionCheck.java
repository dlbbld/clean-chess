package com.dlb.chess.san.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.model.SanConversion;

public record SanConversionCheck(boolean isMatch, @NonNull SanConversion sanConversion) {

  public static final SanConversionCheck IS_NO_MATCH = new SanConversionCheck(false, SanConversion.EMPTY);

}
