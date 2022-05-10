package com.dlb.chess.generate.san.strict;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.EmptyBoardMove;
import com.dlb.chess.squares.emptyboard.AbstractEmptyBoardSquares;

public class GenerateKingNonCastlingSanValidateStrict extends AbstractGenerateSanValidateStrict {

  @Override
  PieceType getPieceType() {
    return KING;
  }

  public static void main(String[] args) {
    new GenerateKingNonCastlingSanValidateStrict().generateSan();
  }

  @Override
  Set<String> calculateEnumConstantHandwoven() {

    final Set<String> resultSet = new TreeSet<>();

    for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
      appendOnlyMove(resultSet, toSquare);
    }

    return resultSet;
  }

  @Override
  Set<String> calculateEnumConstantFormal() {
    final Set<String> resultSet = new TreeSet<>();

    for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
      final Set<EmptyBoardMove> emptyBoardMoveSet = AbstractEmptyBoardSquares.calculateNonPawnEmptyBoardMovesTo(KING,
          toSquare);
      for (final EmptyBoardMove move : emptyBoardMoveSet) {
        appendOnlyMove(resultSet, move.toSquare());
      }
    }

    return resultSet;
  }
}
