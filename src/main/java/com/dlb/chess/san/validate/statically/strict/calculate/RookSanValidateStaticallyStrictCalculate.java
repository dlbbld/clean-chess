package com.dlb.chess.san.validate.statically.strict.calculate;

import java.util.Map;
import java.util.TreeMap;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.san.model.SanValidationFromTo;
import com.dlb.chess.san.validate.statically.strict.enums.RookSanValidateStaticallyStrict;
import com.google.common.collect.ImmutableMap;

public class RookSanValidateStaticallyStrictCalculate extends AbstractSanValidateStaticallyStrictCalculate {

  static ImmutableMap<String, SanParse> calculateSanMap() {

    final Map<String, SanParse> sanValidateMap = new TreeMap<>();

    for (final RookSanValidateStaticallyStrict sanEnum : RookSanValidateStaticallyStrict.values()) {
      final String enumName = NonNullWrapperCommon.name(sanEnum);
      final SanValidationFromTo model = calculateFromFileXorRankTo(enumName, ROOK);
      populateMap(sanValidateMap, model, ROOK);
    }

    return NonNullWrapperCommon.copyOfMap(sanValidateMap);
  }

}
