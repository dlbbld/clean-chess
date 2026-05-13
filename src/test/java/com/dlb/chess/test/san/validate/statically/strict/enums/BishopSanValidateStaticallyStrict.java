package com.dlb.chess.test.san.validate.statically.strict.enums;

import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendMoveWithFile;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendMoveWithFromSquare;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendMoveWithRank;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendOnlyMove;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.calculateFromSquareList;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.calculateHasOtherMovesFromSameRank;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.calculateIsFromFilePossibleDiagonal;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.EmptyBoardMove;
import com.dlb.chess.squares.AbstractEmptyBoardSquares;
import com.google.common.collect.ImmutableSet;

public abstract class BishopSanValidateStaticallyStrict {

  public static final ImmutableSet<String> VALUES;

  static {
    final Set<String> set = new TreeSet<>();
    for (final Square toSquare : Square.REAL) {
      final Set<EmptyBoardMove> moves = AbstractEmptyBoardSquares.calculateNonPawnEmptyBoardMovesTo(PieceType.BISHOP,
          toSquare);
      final List<Square> fromSquareList = calculateFromSquareList(moves);

      // file/rank disambiguation
      for (final Square fromSquare : fromSquareList) {
        appendOnlyMove(set, toSquare, PieceType.BISHOP);
        if (calculateIsFromFilePossibleDiagonal(fromSquare, toSquare, fromSquareList)) {
          appendMoveWithFile(set, toSquare, fromSquare.getFile(), PieceType.BISHOP);
        }
        if (calculateIsFromRankPossibleBishop(fromSquare, fromSquareList)) {
          appendMoveWithRank(set, toSquare, fromSquare.getRank(), PieceType.BISHOP);
        }
      }

      // square disambiguation
      for (final Square fromSquare : fromSquareList) {
        appendOnlyMove(set, toSquare, PieceType.BISHOP);
        if (calculateIsFromRankPossibleBishop(fromSquare, fromSquareList)
            && calculateHasOtherMovesFromSameRank(fromSquare, fromSquareList)) {
          appendMoveWithFromSquare(set, toSquare, fromSquare, PieceType.BISHOP);
        }
      }
    }
    VALUES = ImmutableSet.copyOf(set);
  }

  private static boolean calculateIsFromRankPossibleBishop(Square fromSquare, List<Square> fromSquareList) {
    for (final Square otherFromSquare : fromSquareList) {
      if (otherFromSquare.getFile() == fromSquare.getFile() && otherFromSquare.getRank() != fromSquare.getRank()) {
        return true;
      }
    }
    return false;
  }

}
