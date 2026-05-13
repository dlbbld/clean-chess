package com.dlb.chess.test.san.validate.statically.strict.enums;

import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendMoveWithFile;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendMoveWithFromSquare;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendMoveWithRank;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.appendOnlyMove;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.calculateFromSquareList;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.calculateHasOtherMovesFromSameRank;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.calculateIsFromFilePossibleDiagonal;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.calculateIsFromFilePossibleOrthogonal;
import static com.dlb.chess.test.san.validate.statically.strict.enums.SanValidateStaticallyStrictHelpers.calculateIsOppositeVertical;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.EmptyBoardMove;
import com.dlb.chess.squares.AbstractEmptyBoardSquares;
import com.google.common.collect.ImmutableSet;

@SuppressWarnings("null")
public abstract class QueenSanValidateStaticallyStrict {

  public static final ImmutableSet<String> VALUES;

  static {
    final Set<String> set = new TreeSet<>();
    for (final Square toSquare : Square.REAL) {
      final Set<EmptyBoardMove> moves = AbstractEmptyBoardSquares.calculateNonPawnEmptyBoardMovesTo(PieceType.QUEEN,
          toSquare);
      final List<Square> fromSquareList = calculateFromSquareList(moves);

      // file/rank disambiguation
      for (final Square fromSquare : fromSquareList) {
        appendOnlyMove(set, toSquare, PieceType.QUEEN);
        if (calculateIsFromFilePossibleOrthogonal(fromSquare, toSquare, fromSquareList)
            && calculateIsFromFilePossibleDiagonal(fromSquare, toSquare, fromSquareList)) {
          appendMoveWithFile(set, toSquare, fromSquare.getFile(), PieceType.QUEEN);
        }
        if (calculateIsFromRankPossibleQueen(fromSquare, toSquare, fromSquareList)) {
          appendMoveWithRank(set, toSquare, fromSquare.getRank(), PieceType.QUEEN);
        }
      }

      // square disambiguation
      for (final Square fromSquare : fromSquareList) {
        appendOnlyMove(set, toSquare, PieceType.QUEEN);
        if (calculateIsFromRankPossibleQueen(fromSquare, toSquare, fromSquareList)
            && calculateHasOtherMovesFromSameRank(fromSquare, fromSquareList)) {
          appendMoveWithFromSquare(set, toSquare, fromSquare, PieceType.QUEEN);
        }
      }
    }
    VALUES = ImmutableSet.copyOf(set);
  }

  private static boolean calculateIsFromRankPossibleQueen(Square fromSquare, Square toSquare,
      List<Square> fromSquareList) {
    for (final Square otherFromSquare : fromSquareList) {
      if (otherFromSquare.getFile() == fromSquare.getFile() && otherFromSquare.getRank() != fromSquare.getRank()
          && (fromSquare.getFile() != toSquare.getFile()
              || calculateIsOppositeVertical(fromSquare, toSquare, otherFromSquare))) {
        return true;
      }
    }
    return false;
  }

}
