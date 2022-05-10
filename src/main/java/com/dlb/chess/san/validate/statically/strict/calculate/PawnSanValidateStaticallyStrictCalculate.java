package com.dlb.chess.san.validate.statically.strict.calculate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.san.model.SanValidationFromTo;
import com.dlb.chess.san.validate.statically.strict.enums.PawnBlackSanValidateStaticallyStrict;
import com.dlb.chess.san.validate.statically.strict.enums.PawnWhiteSanValidateStaticallyStrict;
import com.google.common.collect.ImmutableMap;

public class PawnSanValidateStaticallyStrictCalculate extends AbstractSanValidateStaticallyStrictCalculate {

  static ImmutableMap<String, SanParse> calculateSanMap(Side side) {
    final Map<String, SanParse> sanValidateMap = new TreeMap<>();

    final List<String> enumNameList = calculateEnumNameList(side);
    for (final String enumName : enumNameList) {
      final var parse = enumName.toLowerCase();
      File fromFile;
      final var fromRank = Rank.NONE;
      final var toSquare = switch (parse.length()) {
        case 3 -> {
          fromFile = File.NONE;
          yield Square.calculate(NonNullWrapperCommon.substring(parse, 1));
        }
        case 4 -> {
          final String fileLetter = NonNullWrapperCommon.toString(parse.charAt(1));
          fromFile = File.calculateFile(fileLetter);
          yield Square.calculate(NonNullWrapperCommon.substring(parse, 2));
        }
        default -> throw new ProgrammingMistakeException(
            "The length of the " + PAWN.getName() + " enum for " + side.getName() + " does not meet the expectation");
      };
      final SanValidationFromTo model = new SanValidationFromTo(fromFile, fromRank, toSquare);
      final var isCapture = fromFile != File.NONE;
      if (Rank.calculateIsPromotionRank(side, toSquare.getRank())) {
        populatePawnPromotionMap(sanValidateMap, model, isCapture);
      } else {
        populatePawnNonPromotionMap(sanValidateMap, model, isCapture);
      }
    }

    return NonNullWrapperCommon.copyOfMap(sanValidateMap);
  }

  private static List<String> calculateEnumNameList(Side side) {

    switch (side) {
      case WHITE: {
        final List<String> enumNameList = new ArrayList<>();
        for (final PawnWhiteSanValidateStaticallyStrict sanEnum : PawnWhiteSanValidateStaticallyStrict.values()) {
          final String enumName = NonNullWrapperCommon.name(sanEnum);
          enumNameList.add(enumName);
        }
        return enumNameList;
      }
      case BLACK: {
        final List<String> enumNameList = new ArrayList<>();
        for (final PawnBlackSanValidateStaticallyStrict sanEnum : PawnBlackSanValidateStaticallyStrict.values()) {
          final String enumName = NonNullWrapperCommon.name(sanEnum);
          enumNameList.add(enumName);
        }
        return enumNameList;
      }
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

}
