package com.dlb.chess.san.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.model.SanConversion;

public record SanConversionCheck(boolean isMatch, SanConversion sanConversion) {

  public static final SanConversionCheck IS_NO_MATCH = new SanConversionCheck(false, SanConversion.EMPTY);

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (SanConversionCheck) obj;
    return isMatch == other.isMatch && Objects.equals(sanConversion, other.sanConversion);
  }

}
