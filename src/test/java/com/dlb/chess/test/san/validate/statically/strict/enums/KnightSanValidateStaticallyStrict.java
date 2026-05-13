package com.dlb.chess.test.san.validate.statically.strict.enums;

import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendMoveWithFile;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendMoveWithFromSquare;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendMoveWithRank;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendOnlyMove;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.calculateFromSquareList;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.calculateHasOtherMovesFromSameRank;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.EmptyBoardMove;
import com.dlb.chess.squares.AbstractEmptyBoardSquares;
import com.google.common.collect.ImmutableSet;

@SuppressWarnings("null")
public abstract class KnightSanValidateStaticallyStrict {

  public static final ImmutableSet<String> VALUES;

  static {
    final Set<String> set = new TreeSet<>();
    for (final Square toSquare : Square.REAL) {
      final Set<EmptyBoardMove> moves = AbstractEmptyBoardSquares.calculateNonPawnEmptyBoardMovesTo(PieceType.KNIGHT,
          toSquare);
      final List<Square> fromSquareList = calculateFromSquareList(moves);

      // file/rank disambiguation
      for (final Square fromSquare : fromSquareList) {
        appendOnlyMove(set, toSquare, PieceType.KNIGHT);
        if (calculateIsFromFilePossibleKnight(fromSquare, fromSquareList)) {
          appendMoveWithFile(set, toSquare, fromSquare.getFile(), PieceType.KNIGHT);
        }
        if (calculateIsFromRankPossibleKnight(fromSquare, fromSquareList)) {
          appendMoveWithRank(set, toSquare, fromSquare.getRank(), PieceType.KNIGHT);
        }
      }

      // square disambiguation
      for (final Square fromSquare : fromSquareList) {
        appendOnlyMove(set, toSquare, PieceType.KNIGHT);
        if (calculateIsFromRankPossibleKnight(fromSquare, fromSquareList)
            && calculateHasOtherMovesFromSameRank(fromSquare, fromSquareList)) {
          appendMoveWithFromSquare(set, toSquare, fromSquare, PieceType.KNIGHT);
        }
      }
    }
    VALUES = ImmutableSet.copyOf(set);
  }

  private static boolean calculateIsFromRankPossibleKnight(Square fromSquare, List<Square> fromSquareList) {
    for (final Square otherFromSquare : fromSquareList) {
      if (otherFromSquare.getRank() != fromSquare.getRank() && otherFromSquare.getFile() == fromSquare.getFile()) {
        return true;
      }
    }
    return false;
  }

  private static boolean calculateIsFromFilePossibleKnight(Square fromSquare, List<Square> fromSquareList) {
    for (final Square otherFromSquare : fromSquareList) {
      if (otherFromSquare.getFile() != fromSquare.getFile()) {
        return true;
      }
    }
    return false;
  }

}
