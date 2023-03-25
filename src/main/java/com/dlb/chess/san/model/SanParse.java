package com.dlb.chess.san.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.enums.SanType;

@SuppressWarnings("null")
public record SanParse(@NonNull SanType sanType, @NonNull SanConversion sanConversion) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (SanParse) obj;
    return Objects.equals(sanConversion, other.sanConversion) && sanType == other.sanType;
  }

}
