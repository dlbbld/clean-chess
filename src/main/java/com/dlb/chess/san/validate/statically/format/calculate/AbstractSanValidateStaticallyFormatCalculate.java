package com.dlb.chess.san.validate.statically.format.calculate;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.san.model.SanValidationFromTo;
import com.dlb.chess.san.validate.statically.strict.calculate.AbstractSanValidateStaticallyStrictCalculate;

public class AbstractSanValidateStaticallyFormatCalculate extends AbstractSanValidateStaticallyStrictCalculate {

  public static List<SanValidationFromTo> calculateForPieceWithoutDisambiguation() {
    final List<SanValidationFromTo> result = new ArrayList<>();
    for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
      result.add(new SanValidationFromTo(FILE_NONE, RANK_NONE, toSquare));
    }
    return result;
  }

  public static List<SanValidationFromTo> calculateForPieceWithFile() {
    final List<SanValidationFromTo> result = new ArrayList<>();
    for (final File fromFile : File.values()) {
      if (fromFile == File.NONE) {
        continue;
      }
      for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
        result.add(new SanValidationFromTo(fromFile, RANK_NONE, toSquare));
      }
    }
    return result;
  }

  public static List<SanValidationFromTo> calculateForPieceWithRank() {
    final List<SanValidationFromTo> result = new ArrayList<>();
    for (final Rank fromRank : Rank.values()) {
      if (fromRank == Rank.NONE) {
        continue;
      }
      for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
        result.add(new SanValidationFromTo(FILE_NONE, fromRank, toSquare));
      }
    }
    return result;
  }

  public static List<SanValidationFromTo> calculateForPieceWithSquare() {
    final List<SanValidationFromTo> result = new ArrayList<>();
    for (final Square fromSquare : Square.BOARD_SQUARE_LIST) {
      for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
        result.add(new SanValidationFromTo(fromSquare.getFile(), fromSquare.getRank(), toSquare));
      }
    }
    return result;
  }

  static List<SanValidationFromTo> calculateForPiece() {
    final List<SanValidationFromTo> result = new ArrayList<>();
    result.addAll(calculateForPieceWithoutDisambiguation());
    result.addAll(calculateForPieceWithFile());
    result.addAll(calculateForPieceWithRank());
    result.addAll(calculateForPieceWithSquare());
    return result;
  }

}
