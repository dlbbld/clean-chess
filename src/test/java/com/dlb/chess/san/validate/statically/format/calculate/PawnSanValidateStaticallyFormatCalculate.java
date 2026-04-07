package com.dlb.chess.san.validate.statically.format.calculate;

import java.util.Map;
import java.util.TreeMap;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.san.model.SanValidationFromTo;
import com.google.common.collect.ImmutableMap;

public class PawnSanValidateStaticallyFormatCalculate extends AbstractSanValidateStaticallyFormatCalculate {

  static ImmutableMap<String, SanParse> calculateSanMap() {
    final Map<String, SanParse> sanValidateMap = new TreeMap<>();

    // promotion only on rank 1 and 8, non-promotion only on ranks 2-7
    for (final SanValidationFromTo model : calculateWithoutDisambiguation()) {
      if (isPromotionRank(model)) {
        populatePawnPromotionMap(sanValidateMap, model, false);
      } else {
        populatePawnNonPromotionMap(sanValidateMap, model, false);
      }
    }

    for (final File fromFile : File.values()) {
      if (fromFile == File.NONE) {
        continue;
      }
      for (final SanValidationFromTo model : calculateWithFile()) {
        if (isPromotionRank(model)) {
          populatePawnPromotionMap(sanValidateMap, model, true);
        } else {
          populatePawnNonPromotionMap(sanValidateMap, model, true);
        }
      }
    }

    return NonNullWrapperCommon.copyOfMap(sanValidateMap);
  }

  private static boolean isPromotionRank(SanValidationFromTo model) {
    final Rank toRank = model.toSquare().getRank();
    return toRank == Rank.RANK_1 || toRank == Rank.RANK_8;
  }

}
