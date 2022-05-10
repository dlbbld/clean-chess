package com.dlb.chess.san.validate.statically.format.calculate;

import java.util.Map;
import java.util.TreeMap;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.san.model.SanValidationFromTo;
import com.google.common.collect.ImmutableMap;

public class PawnSanValidateStaticallyFormatCalculate extends AbstractSanValidateStaticallyFormatCalculate {

  static ImmutableMap<String, SanParse> calculateSanMap() {
    final Map<String, SanParse> sanValidateMap = new TreeMap<>();

    // format-wise we allow
    // -moving to squares not allowed
    // -capturing from any rank
    // -prommotion on every rank
    // validation for such moves takes place in the next step
    for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
      final SanValidationFromTo model = new SanValidationFromTo(File.NONE, Rank.NONE, toSquare);
      populatePawnPromotionMap(sanValidateMap, model, false);
      populatePawnNonPromotionMap(sanValidateMap, model, false);
    }

    for (final File fromFile : File.values()) {
      if (fromFile == File.NONE) {
        continue;
      }
      for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
        final SanValidationFromTo model = new SanValidationFromTo(fromFile, Rank.NONE, toSquare);
        populatePawnPromotionMap(sanValidateMap, model, true);
        populatePawnNonPromotionMap(sanValidateMap, model, true);
      }
    }

    return NonNullWrapperCommon.copyOfMap(sanValidateMap);
  }

}
