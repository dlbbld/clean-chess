package com.dlb.chess.san.validate.statically.format.calculate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.san.model.SanValidationFromTo;
import com.google.common.collect.ImmutableMap;

public class KingNonCastlingSanValidateStaticallyFormatCalculate extends AbstractSanValidateStaticallyFormatCalculate {

  static ImmutableMap<String, SanParse> calculateSanMap() {

    final Map<String, SanParse> sanValidateMap = new TreeMap<>();

    for (final String enumName : calculateForKing()) {

      final String parse = NonNullWrapperCommon.toLowerCase(enumName);
      final var fromFile = File.NONE;
      final var fromRank = Rank.NONE;
      final Square toSquare = Square.calculate(NonNullWrapperCommon.substring(parse, 1));

      final SanValidationFromTo model = new SanValidationFromTo(fromFile, fromRank, toSquare);
      populateMap(sanValidateMap, model, KING);
    }

    return NonNullWrapperCommon.copyOfMap(sanValidateMap);
  }

  static List<String> calculateForKing() {
    final List<String> result = new ArrayList<>();
    for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
      final var sanBaseConstruct = new StringBuilder();
      sanBaseConstruct.append(PieceType.KING.getLetter());
      sanBaseConstruct.append(toSquare.getName());
      final var sanBase = NonNullWrapperCommon.toString(sanBaseConstruct);
      result.add(NonNullWrapperCommon.toUpperCase(sanBase));
    }
    return result;
  }
}
