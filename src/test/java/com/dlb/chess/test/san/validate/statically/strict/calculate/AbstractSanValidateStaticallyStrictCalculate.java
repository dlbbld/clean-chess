package com.dlb.chess.test.san.validate.statically.strict.calculate;

import java.util.Map;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.SanCalculate;
import com.dlb.chess.san.enums.CheckmateOrCheck;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.san.model.SanValidationFromTo;

public abstract class AbstractSanValidateStaticallyStrictCalculate implements EnumConstants {

  public static SanValidationFromTo calculateFromFileAndOrRankTo(String enumName, PieceType movingPieceType) {

    final var parse = enumName.toLowerCase();
    File fromFile;
    Rank fromRank;
    Square toSquare;
    switch (parse.length()) {
      case 3:
        fromFile = File.NONE;
        fromRank = Rank.NONE;
        toSquare = Square.calculate(NonNullWrapperCommon.substring(parse, 1));
        break;
      case 4:
        final var checkLetter = NonNullWrapperCommon.toString(parse.charAt(1));

        if (File.exists(checkLetter)) {
          fromFile = File.calculateFile(checkLetter);
          fromRank = Rank.NONE;
        } else {
          if (!BasicUtility.isInt(checkLetter)) {
            throw new ProgrammingMistakeException(
                "The fourth letter in " + parse + " must be a valid file letter or rank number");
          }
          final var rankNumber = BasicUtility.parseInt(checkLetter);
          if (!Rank.exists(rankNumber)) {
            throw new ProgrammingMistakeException(
                "The fourth letter in " + parse + " must be a valid file letter or rank number");
          }
          fromFile = File.NONE;
          fromRank = Rank.calculateRank(rankNumber);
        }
        toSquare = Square.calculate(NonNullWrapperCommon.substring(parse, 2));
        break;
      case 5:
        final Square fromSquare = Square.calculate(NonNullWrapperCommon.substring(parse, 1, 3));
        fromFile = fromSquare.getFile();
        fromRank = fromSquare.getRank();
        toSquare = Square.calculate(NonNullWrapperCommon.substring(parse, 3));
        break;
      default:
        throw new ProgrammingMistakeException(
            "The length of the " + movingPieceType.getName() + " enum does not meet the expectation");
    }

    return new SanValidationFromTo(fromFile, fromRank, toSquare);
  }

  static SanValidationFromTo calculateFromFileXorRankTo(String enumName, PieceType movingPieceType) {
    if (enumName.length() == 5) {
      throw new ProgrammingMistakeException(
          "The file/rank of the " + movingPieceType.getName() + " enum does not meet the expectation");
    }
    return calculateFromFileAndOrRankTo(enumName, movingPieceType);
  }

  public static void populateMap(Map<String, SanParse> sanValidateMap, SanValidationFromTo model,
      PieceType movingPieceType) {
    populateMap(sanValidateMap, model, PromotionPieceType.NONE, movingPieceType);
  }

  static void populateMap(Map<String, SanParse> sanValidateMap, SanValidationFromTo model,
      PromotionPieceType promotionPieceType, PieceType movingPieceType) {
    populateMap(sanValidateMap, model, false, promotionPieceType, movingPieceType);
    populateMap(sanValidateMap, model, true, promotionPieceType, movingPieceType);
  }

  private static void populateMap(Map<String, SanParse> sanValidateMap, SanValidationFromTo model, boolean isCapture,
      PromotionPieceType promotionPieceType, PieceType movingPieceType) {
    populateMap(sanValidateMap, model, promotionPieceType, isCapture, CheckmateOrCheck.NONE, movingPieceType);
    populateMap(sanValidateMap, model, promotionPieceType, isCapture, CheckmateOrCheck.CHECKMATE, movingPieceType);
    populateMap(sanValidateMap, model, promotionPieceType, isCapture, CheckmateOrCheck.CHECK, movingPieceType);
  }

  static void populateMap(Map<String, SanParse> sanValidateMap, SanValidationFromTo model,
      PromotionPieceType promotionPieceType, boolean isCapture, CheckmateOrCheck checkmateOrCheck,
      PieceType movingPieceType) {
    final File fromFile = model.fromFile();
    final Rank fromRank = model.fromRank();
    final Square toSquare = model.toSquare();

    final String san = SanCalculate.calculateSan(fromFile, fromRank, toSquare, promotionPieceType, isCapture,
        checkmateOrCheck, movingPieceType);
    final var sanType = SanCalculate.calculateSanType(isCapture, fromFile, fromRank, movingPieceType, promotionPieceType);
    final SanParse sanParse = new SanParse(sanType,
        new SanConversion(fromFile, fromRank, toSquare, promotionPieceType, checkmateOrCheck));
    sanValidateMap.put(san, sanParse);
  }

  public static void populatePawnNonPromotionMap(Map<String, SanParse> sanValidateMap, SanValidationFromTo model,
      boolean isCapture) {
    populateMap(sanValidateMap, model, PromotionPieceType.NONE, isCapture, CheckmateOrCheck.NONE, PAWN);
    populateMap(sanValidateMap, model, PromotionPieceType.NONE, isCapture, CheckmateOrCheck.CHECKMATE, PAWN);
    populateMap(sanValidateMap, model, PromotionPieceType.NONE, isCapture, CheckmateOrCheck.CHECK, PAWN);
  }

  public static void populatePawnPromotionMap(Map<String, SanParse> sanValidateMap, SanValidationFromTo model,
      boolean isCapture) {
    for (final PromotionPieceType promotionPieceType : PromotionPieceType.values()) {
      if (promotionPieceType != PromotionPieceType.NONE) {
        populateMap(sanValidateMap, model, promotionPieceType, isCapture, CheckmateOrCheck.NONE, PAWN);
        populateMap(sanValidateMap, model, promotionPieceType, isCapture, CheckmateOrCheck.CHECKMATE, PAWN);
        populateMap(sanValidateMap, model, promotionPieceType, isCapture, CheckmateOrCheck.CHECK, PAWN);
      }
    }
  }
}
