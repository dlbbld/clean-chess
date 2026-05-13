package com.dlb.chess.test.san.validate.statically.strict.enums;

import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendMoveWithFile;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendOnlyMove;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.EmptyBoardMove;
import com.dlb.chess.squares.AbstractEmptyBoardSquares;
import com.dlb.chess.squares.PawnDiagonalSquares;
import com.google.common.collect.ImmutableSet;

@SuppressWarnings("null")
public abstract class PawnWhiteSanValidateStaticallyStrict {

  public static final ImmutableSet<String> VALUES;

  static {
    final Set<String> set = new TreeSet<>();

    // one and two-square pawn advances
    for (final EmptyBoardMove move : AbstractEmptyBoardSquares.calculatePawnEmptyBoardMoves(Side.WHITE)) {
      appendOnlyMove(set, move.toSquare(), PieceType.PAWN);
    }

    // diagonal captures (file-disambiguated form)
    for (final Square fromSquare : Square.REAL) {
      for (final Square diagonalSquare : PawnDiagonalSquares.getPawnDiagonalSquares(Side.WHITE, fromSquare)) {
        appendMoveWithFile(set, diagonalSquare, fromSquare.getFile(), PieceType.PAWN);
      }
    }

    VALUES = ImmutableSet.copyOf(set);
  }

}
