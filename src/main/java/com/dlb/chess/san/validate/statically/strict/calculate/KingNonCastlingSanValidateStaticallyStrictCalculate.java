package com.dlb.chess.san.validate.statically.strict.calculate;

import java.util.Map;
import java.util.TreeMap;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.san.model.SanValidationFromTo;
import com.dlb.chess.san.validate.statically.strict.enums.KingNonCastlingSanValidateStaticallyStrict;
import com.google.common.collect.ImmutableMap;

public class KingNonCastlingSanValidateStaticallyStrictCalculate extends AbstractSanValidateStaticallyStrictCalculate {

  static ImmutableMap<String, SanParse> calculateSanMap() {

    final Map<String, SanParse> sanValidateMap = new TreeMap<>();

    for (final KingNonCastlingSanValidateStaticallyStrict sanEnum : KingNonCastlingSanValidateStaticallyStrict
        .values()) {
      final String enumName = NonNullWrapperCommon.name(sanEnum);

      final String parse = NonNullWrapperCommon.toLowerCase(enumName);
      final var fromFile = File.NONE;
      final var fromRank = Rank.NONE;
      final Square toSquare = Square.calculate(NonNullWrapperCommon.substring(parse, 1));

      final SanValidationFromTo model = new SanValidationFromTo(fromFile, fromRank, toSquare);
      populateMap(sanValidateMap, model, KING);
    }

    return NonNullWrapperCommon.copyOfMap(sanValidateMap);
  }

}
