package com.dlb.chess.san.validate.statically.format.calculate;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.san.validate.statically.strict.calculate.AbstractSanValidateStaticallyStrictCalculate;

public class AbstractSanValidateStaticallyFormatCalculate extends AbstractSanValidateStaticallyStrictCalculate {

  static List<String> calculateForPiece(PieceType pieceType) {
    final List<String> result = new ArrayList<>();
    // pieces - none
    for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
      final var sanBaseConstruct = new StringBuilder();
      sanBaseConstruct.append(pieceType.getLetter());
      sanBaseConstruct.append(toSquare.getName());
      final var sanBase = NonNullWrapperCommon.toString(sanBaseConstruct);
      result.add(NonNullWrapperCommon.toUpperCase(sanBase));
    }

    // pieces - file
    for (final File fromFile : File.values()) {
      if (fromFile == File.NONE) {
        continue;
      }
      for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
        final var sanBaseConstruct = new StringBuilder();
        sanBaseConstruct.append(pieceType.getLetter());
        sanBaseConstruct.append(fromFile.getLetter());
        sanBaseConstruct.append(toSquare.getName());
        final var sanBase = NonNullWrapperCommon.toString(sanBaseConstruct);
        result.add(NonNullWrapperCommon.toUpperCase(sanBase));
      }
    }

    // pieces - rank
    for (final Rank fromRank : Rank.values()) {
      if (fromRank == Rank.NONE) {
        continue;
      }
      for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
        final var sanBaseConstruct = new StringBuilder();
        sanBaseConstruct.append(pieceType.getLetter());
        sanBaseConstruct.append(fromRank.getNumber());
        sanBaseConstruct.append(toSquare.getName());
        final var sanBase = NonNullWrapperCommon.toString(sanBaseConstruct);
        result.add(NonNullWrapperCommon.toUpperCase(sanBase));
      }
    }

    // pieces - square
    for (final Square fromSquare : Square.BOARD_SQUARE_LIST) {
      for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
        final var sanBaseConstruct = new StringBuilder();
        sanBaseConstruct.append(pieceType.getLetter());
        sanBaseConstruct.append(fromSquare.getName());
        sanBaseConstruct.append(toSquare.getName());
        final var sanBase = NonNullWrapperCommon.toString(sanBaseConstruct);
        result.add(NonNullWrapperCommon.toUpperCase(sanBase));
      }
    }
    return result;
  }

}
