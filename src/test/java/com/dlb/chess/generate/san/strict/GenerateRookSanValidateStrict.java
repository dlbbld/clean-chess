package com.dlb.chess.generate.san.strict;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.EmptyBoardMove;
import com.dlb.chess.squares.emptyboard.AbstractEmptyBoardSquares;

public class GenerateRookSanValidateStrict extends AbstractGenerateSanValidateStrict {

  @Override
  PieceType getPieceType() {
    return ROOK;
  }

  public static void main(String[] args) {
    new GenerateRookSanValidateStrict().generateSan();
  }

  @Override
  Set<String> calculateEnumConstantHandwoven() {

    final Set<String> resultSet = new TreeSet<>();

    final List<Square> processedList = new ArrayList<>();

    // first and eight rank squares
    for (final Square toSquare : FIRST_AND_EIGHT_RANK_SQUARES) {
      processedList.add(toSquare);
      appendOnlyMove(resultSet, toSquare);
      appendMoveWithAllFiles(resultSet, toSquare);
    }

    // remaining squares
    for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
      if (!processedList.contains(toSquare)) {
        appendOnlyMove(resultSet, toSquare);
        appendMoveWithAllFiles(resultSet, toSquare);
        appendMoveWithAllRanksExceptToSquareRank(resultSet, toSquare);
      }
    }
    return resultSet;
  }

  @Override
  Set<String> calculateEnumConstantFormal() {
    final Set<String> resultSet = new TreeSet<>();
    for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
      final Set<EmptyBoardMove> emptyBoardMoveSet = AbstractEmptyBoardSquares.calculateNonPawnEmptyBoardMovesTo(ROOK,
          toSquare);
      final List<Square> fromSquareList = calculateFromSquareList(emptyBoardMoveSet);

      // checking each from square individually
      // file and rank
      for (final Square fromSquare : fromSquareList) {
        // there is a piece which can move to the to square
        appendOnlyMove(resultSet, toSquare);

        if (calculateIsFromFilePossibleOrthogonal(fromSquare, toSquare, fromSquareList)) {
          appendMoveWithFile(resultSet, toSquare, fromSquare.getFile());
        }

        if (calculateIsFromRankPossibleOrthogonal(fromSquare, toSquare, fromSquareList)) {
          appendMoveWithRank(resultSet, toSquare, fromSquare.getRank());
        }
      }

      // checking each from square individually
      // square
      for (final Square fromSquare : fromSquareList) {
        // there is a piece which can move to the to square
        appendOnlyMove(resultSet, toSquare);

        if (calculateIsFromRankPossibleOrthogonal(fromSquare, toSquare, fromSquareList)
            && calculateHasOtherMovesFromSameRank(fromSquare, fromSquareList)) {
          appendMoveWithFromSquare(resultSet, toSquare, fromSquare);
        }
      }
    }

    return resultSet;
  }

}
