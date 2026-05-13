package com.dlb.chess.test.san.validate.statically.strict.enums;

import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendOnlyMove;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.EmptyBoardMove;
import com.dlb.chess.squares.AbstractEmptyBoardSquares;
import com.google.common.collect.ImmutableSet;

@SuppressWarnings("null")
public abstract class KingNonCastlingSanValidateStaticallyStrict {

  public static final ImmutableSet<String> VALUES;

  static {
    final Set<String> set = new TreeSet<>();
    for (final Square toSquare : Square.REAL) {
      final Set<EmptyBoardMove> moves = AbstractEmptyBoardSquares.calculateNonPawnEmptyBoardMovesTo(PieceType.KING,
          toSquare);
      for (final EmptyBoardMove move : moves) {
        appendOnlyMove(set, move.toSquare(), PieceType.KING);
      }
    }
    VALUES = ImmutableSet.copyOf(set);
  }

}
