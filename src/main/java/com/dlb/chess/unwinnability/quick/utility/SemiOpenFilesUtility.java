package com.dlb.chess.unwinnability.quick.utility;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.constants.EnumConstants;

public class SemiOpenFilesUtility implements EnumConstants {
  public static boolean calculateHasSemiOpenFile(StaticPosition staticPosition) {
    for (final File file : File.values()) {
      if (file == File.NONE) {
        continue;
      }
      if (calculateIsSemiOpenFile(staticPosition, file)) {
        return true;
      }
    }
    return false;
  }

  private static boolean calculateIsSemiOpenFile(StaticPosition staticPosition, File file) {
    return calculateIsSemiOpenFile(staticPosition, file, Side.WHITE)
        || calculateIsSemiOpenFile(staticPosition, file, Side.BLACK);

  }

  public static boolean calculateIsSemiOpenFile(StaticPosition staticPosition, File file, Side sideHavingSemiOpenFile) {

    final List<Square> squareListToCheck = calculateSquareListToCheck(file, sideHavingSemiOpenFile);

    for (final Square squareCheck : squareListToCheck) {
      if (!staticPosition.isEmpty(squareCheck)) {
        final Piece piece = staticPosition.get(squareCheck);
        if (piece.getPieceType() == PieceType.PAWN) {
          return piece.getSide() == sideHavingSemiOpenFile;
        }
      }
    }
    return false;
  }

  private static List<Square> calculateSquareListToCheck(File file, Side sideHavingSemiOpenFile) {

    final List<Square> result = new ArrayList<>();

    switch (sideHavingSemiOpenFile) {
      case WHITE:
        result.add(Square.calculate(file, RANK_7));
        result.add(Square.calculate(file, RANK_6));
        result.add(Square.calculate(file, RANK_5));
        result.add(Square.calculate(file, RANK_4));
        result.add(Square.calculate(file, RANK_3));
        result.add(Square.calculate(file, RANK_2));
        break;
      case BLACK:
        result.add(Square.calculate(file, RANK_2));
        result.add(Square.calculate(file, RANK_3));
        result.add(Square.calculate(file, RANK_4));
        result.add(Square.calculate(file, RANK_5));
        result.add(Square.calculate(file, RANK_6));
        result.add(Square.calculate(file, RANK_7));
        break;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }

    return result;
  }
}
