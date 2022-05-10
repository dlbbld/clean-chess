package com.dlb.chess.san.validate.statically.format.calculate;

import java.util.Map;
import java.util.TreeMap;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.san.model.SanValidationFromTo;
import com.google.common.collect.ImmutableMap;

public class RookSanValidateStaticallyFormatCalculate extends AbstractSanValidateStaticallyFormatCalculate {

  static ImmutableMap<String, SanParse> calculateSanMap() {

    final Map<String, SanParse> sanValidateMap = new TreeMap<>();

    for (final String enumName : calculateForPiece(ROOK)) {
      final SanValidationFromTo model = calculateFromFileAndOrRankTo(enumName, ROOK);
      populateMap(sanValidateMap, model, ROOK);
    }

    return NonNullWrapperCommon.copyOfMap(sanValidateMap);
  }

}
