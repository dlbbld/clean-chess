package com.dlb.chess.san.validate.statically.format.calculate;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.san.model.SanParse;
import com.google.common.collect.ImmutableMap;

public class SanValidateStaticallyFormat implements EnumConstants {

  private static final ImmutableMap<String, SanParse> SAN_VALIDATE_MAP;

  static {
    final Map<String, SanParse> sanValidationAllMap = new TreeMap<>(
        RookSanValidateStaticallyFormatCalculate.calculateSanMap());

    sanValidationAllMap.putAll(KnightSanValidateStaticallyFormatCalculate.calculateSanMap());

    sanValidationAllMap.putAll(BishopSanValidateStaticallyFormatCalculate.calculateSanMap());

    sanValidationAllMap.putAll(QueenSanValidateStaticallyFormatCalculate.calculateSanMap());

    sanValidationAllMap.putAll(KingNonCastlingSanValidateStaticallyFormatCalculate.calculateSanMap());
    sanValidationAllMap.putAll(KingCastlingSanValidateStaticallyFormatCalculate.calculateSanMap());

    sanValidationAllMap.putAll(PawnSanValidateStaticallyFormatCalculate.calculateSanMap());

    SAN_VALIDATE_MAP = NonNullWrapperCommon.copyOfMap(sanValidationAllMap);
  }

  public static boolean exists(String san) {
    return SAN_VALIDATE_MAP.containsKey(san);
  }

  public static SanParse calculate(String san) {
    if (!exists(san)) {
      throw new IllegalArgumentException("The SAN does not exist");
    }
    return NonNullWrapperCommon.get(SAN_VALIDATE_MAP, san);
  }

  // for performance reasons
  public static @Nullable SanParse get(String san) {
    return SAN_VALIDATE_MAP.get(san);
  }

  public static ImmutableMap<String, SanParse> getSanValidationMap() {
    return SAN_VALIDATE_MAP;
  }

}
