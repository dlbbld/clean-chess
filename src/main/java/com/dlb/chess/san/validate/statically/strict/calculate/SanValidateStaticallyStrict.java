package com.dlb.chess.san.validate.statically.strict.calculate;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.san.model.SanParse;
import com.google.common.collect.ImmutableMap;

public class SanValidateStaticallyStrict implements EnumConstants {

  private static final ImmutableMap<String, SanParse> SAN_VALIDATION_WHITE_MAP;
  private static final ImmutableMap<String, SanParse> SAN_VALIDATION_BLACK_MAP;

  static {
    final Map<String, SanParse> sanValidationAllMap = new TreeMap<>(
        RookSanValidateStaticallyStrictCalculate.calculateSanMap());

    sanValidationAllMap.putAll(KnightSanValidateStaticallyStrictCalculate.calculateSanMap());

    sanValidationAllMap.putAll(BishopSanValidateStaticallyStrictCalculate.calculateSanMap());

    sanValidationAllMap.putAll(QueenSanValidateStaticallyStrictCalculate.calculateSanMap());

    sanValidationAllMap.putAll(KingNonCastlingSanValidateStaticallyStrictCalculate.calculateSanMap());
    sanValidationAllMap.putAll(KingCastlingSanValidateStaticallyStrictCalculate.calculateSanMap());

    final Map<String, SanParse> sanValidationWhiteMap = new TreeMap<>(sanValidationAllMap);
    sanValidationWhiteMap.putAll(PawnSanValidateStaticallyStrictCalculate.calculateSanMap(WHITE));
    SAN_VALIDATION_WHITE_MAP = NonNullWrapperCommon.copyOfMap(sanValidationWhiteMap);

    final Map<String, SanParse> sanValidationBlackMap = new TreeMap<>(sanValidationAllMap);
    sanValidationBlackMap.putAll(PawnSanValidateStaticallyStrictCalculate.calculateSanMap(BLACK));
    SAN_VALIDATION_BLACK_MAP = NonNullWrapperCommon.copyOfMap(sanValidationBlackMap);

  }

  public static ImmutableMap<String, SanParse> getSanValidationWhiteMap() {
    return SAN_VALIDATION_WHITE_MAP;
  }

  public static ImmutableMap<String, SanParse> getSanValidationBlackMap() {
    return SAN_VALIDATION_BLACK_MAP;
  }

  public static boolean exists(String san, Side side) {
    switch (side) {
      case WHITE:
        return SAN_VALIDATION_WHITE_MAP.containsKey(san);
      case BLACK:
        return SAN_VALIDATION_BLACK_MAP.containsKey(san);
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static SanParse getChecked(String san, Side side) {
    if (!exists(san, side)) {
      throw new IllegalArgumentException("The SAN does not exist");
    }
    switch (side) {
      case WHITE:
        return NonNullWrapperCommon.get(SAN_VALIDATION_WHITE_MAP, san);
      case BLACK:
        return NonNullWrapperCommon.get(SAN_VALIDATION_BLACK_MAP, san);
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  // for performance reasons
  public static @Nullable SanParse get(String san, Side side) {
    switch (side) {
      case WHITE:
        return SAN_VALIDATION_WHITE_MAP.get(san);
      case BLACK:
        return SAN_VALIDATION_BLACK_MAP.get(san);
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

}
