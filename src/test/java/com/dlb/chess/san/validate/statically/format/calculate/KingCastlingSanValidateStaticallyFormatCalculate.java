package com.dlb.chess.san.validate.statically.format.calculate;

import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.san.validate.statically.strict.calculate.KingCastlingSanValidateStaticallyStrictCalculate;
import com.google.common.collect.ImmutableMap;

public class KingCastlingSanValidateStaticallyFormatCalculate extends AbstractSanValidateStaticallyFormatCalculate {

  static ImmutableMap<String, SanParse> calculateSanMap() {

    return KingCastlingSanValidateStaticallyStrictCalculate.calculateSanMap();
  }

}
