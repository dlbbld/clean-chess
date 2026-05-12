package com.dlb.chess.test.san.validate.statically.strict.calculate;

import java.util.Map;
import java.util.TreeMap;

import com.dlb.chess.common.Nulls;
import com.dlb.chess.san.SanParse;
import com.dlb.chess.test.san.model.SanValidationFromTo;
import com.dlb.chess.test.san.validate.statically.strict.enums.BishopSanValidateStaticallyStrict;
import com.google.common.collect.ImmutableMap;

public class BishopSanValidateStaticallyStrictCalculate extends AbstractSanValidateStaticallyStrictCalculate {

  static ImmutableMap<String, SanParse> calculateSanMap() {

    final Map<String, SanParse> sanValidateMap = new TreeMap<>();

    for (final String enumName : BishopSanValidateStaticallyStrict.VALUES) {
      final SanValidationFromTo model = calculateFromFileAndOrRankTo(enumName, BISHOP);
      populateMap(sanValidateMap, model, BISHOP);
    }

    return Nulls.copyOfMap(sanValidateMap);
  }

}
