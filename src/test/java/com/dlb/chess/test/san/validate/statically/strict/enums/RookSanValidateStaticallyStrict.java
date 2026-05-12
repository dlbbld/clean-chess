package com.dlb.chess.test.san.validate.statically.strict.enums;

import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendMoveWithFile;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendMoveWithFromSquare;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendMoveWithRank;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendOnlyMove;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.calculateFromSquareList;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.calculateHasOtherMovesFromSameRank;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.calculateIsFromFilePossibleOrthogonal;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.calculateIsFromRankPossibleOrthogonal;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.EmptyBoardMove;
import com.dlb.chess.squares.AbstractEmptyBoardSquares;
import com.google.common.collect.ImmutableSet;

public abstract class RookSanValidateStaticallyStrict {

  public static final ImmutableSet<String> VALUES;

  static {
    final Set<String> set = new TreeSet<>();
    for (final Square toSquare : Square.REAL) {
      final Set<EmptyBoardMove> moves = AbstractEmptyBoardSquares.calculateNonPawnEmptyBoardMovesTo(PieceType.ROOK,
          toSquare);
      final List<Square> fromSquareList = calculateFromSquareList(moves);

      // file/rank disambiguation
      for (final Square fromSquare : fromSquareList) {
        appendOnlyMove(set, toSquare, PieceType.ROOK);
        if (calculateIsFromFilePossibleOrthogonal(fromSquare, toSquare, fromSquareList)) {
          appendMoveWithFile(set, toSquare, fromSquare.getFile(), PieceType.ROOK);
        }
        if (calculateIsFromRankPossibleOrthogonal(fromSquare, toSquare, fromSquareList)) {
          appendMoveWithRank(set, toSquare, fromSquare.getRank(), PieceType.ROOK);
        }
      }

      // square disambiguation
      for (final Square fromSquare : fromSquareList) {
        appendOnlyMove(set, toSquare, PieceType.ROOK);
        if (calculateIsFromRankPossibleOrthogonal(fromSquare, toSquare, fromSquareList)
            && calculateHasOtherMovesFromSameRank(fromSquare, fromSquareList)) {
          appendMoveWithFromSquare(set, toSquare, fromSquare, PieceType.ROOK);
        }
      }
    }
    VALUES = ImmutableSet.copyOf(set);
  }

}
