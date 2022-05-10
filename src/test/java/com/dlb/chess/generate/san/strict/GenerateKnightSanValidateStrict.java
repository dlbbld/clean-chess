package com.dlb.chess.generate.san.strict;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.EmptyBoardMove;
import com.dlb.chess.squares.emptyboard.AbstractEmptyBoardSquares;

public class GenerateKnightSanValidateStrict extends AbstractGenerateSanValidateStrict {

  @Override
  PieceType getPieceType() {
    return KNIGHT;
  }

  public static void main(String[] args) {
    new GenerateKnightSanValidateStrict().generateSan();
  }

  @Override
  Set<String> calculateEnumConstantHandwoven() {

    final Set<String> resultSet = new TreeSet<>();

    // edge squares
    {
      final var toSquare = Square.A1;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_B);
      appendMoveWithFile(resultSet, toSquare, FILE_C);
    }
    {
      final var toSquare = Square.A8;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_B);
      appendMoveWithFile(resultSet, toSquare, FILE_C);
    }
    {
      final var toSquare = Square.H1;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_F);
      appendMoveWithFile(resultSet, toSquare, FILE_G);
    }
    {
      final var toSquare = Square.H8;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_F);
      appendMoveWithFile(resultSet, toSquare, FILE_G);
    }

    // one away from edge squares but not inside board
    {
      final var toSquare = Square.B1;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_A, FILE_C, FILE_D);
    }
    {
      final var toSquare = Square.B8;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_A, FILE_C, FILE_D);
    }
    {
      final var toSquare = Square.G1;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_E, FILE_F, FILE_H);
    }
    {
      final var toSquare = Square.G8;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_E, FILE_F, FILE_H);
    }

    {
      final var toSquare = Square.A2;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_B, FILE_C);
      appendMoveWithRank(resultSet, toSquare, RANK_1, RANK_3);
    }
    {
      final var toSquare = Square.A7;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_B, FILE_C);
      appendMoveWithRank(resultSet, toSquare, RANK_6, RANK_8);
    }
    {
      final var toSquare = Square.H2;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_F, FILE_G);
      appendMoveWithRank(resultSet, toSquare, RANK_1, RANK_3);
    }
    {
      final var toSquare = Square.H7;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_F, FILE_G);
      appendMoveWithRank(resultSet, toSquare, RANK_6, RANK_8);
    }

    // remaining outer square on first and eigth rank
    {
      final var toSquare = Square.C1;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_A, FILE_B, FILE_D, FILE_E);
    }
    {
      final var toSquare = Square.C8;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_A, FILE_B, FILE_D, FILE_E);
    }
    {
      final var toSquare = Square.D1;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_B, FILE_C, FILE_E, FILE_F);
    }
    {
      final var toSquare = Square.D8;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_B, FILE_C, FILE_E, FILE_F);
    }
    {
      final var toSquare = Square.E1;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_C, FILE_D, FILE_F, FILE_G);
    }
    {
      final var toSquare = Square.E8;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_C, FILE_D, FILE_F, FILE_G);
    }
    {
      final var toSquare = Square.F1;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_D, FILE_E, FILE_G, FILE_H);
    }
    {
      final var toSquare = Square.F8;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_D, FILE_E, FILE_G, FILE_H);
    }

    // remaining outer squares one A and H file
    {
      final var toSquare = Square.A3;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_B, FILE_C);
      appendMoveWithRank(resultSet, toSquare, RANK_1, RANK_2, RANK_4, RANK_5);
    }
    {
      final var toSquare = Square.H3;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_F, FILE_G);
      appendMoveWithRank(resultSet, toSquare, RANK_1, RANK_2, RANK_4, RANK_5);
    }
    {
      final var toSquare = Square.A4;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_B, FILE_C);
      appendMoveWithRank(resultSet, toSquare, RANK_2, RANK_3, RANK_5, RANK_6);
    }
    {
      final var toSquare = Square.H4;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_F, FILE_G);
      appendMoveWithRank(resultSet, toSquare, RANK_2, RANK_3, RANK_5, RANK_6);
    }
    {
      final var toSquare = Square.A5;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_B, FILE_C);
      appendMoveWithRank(resultSet, toSquare, RANK_3, RANK_4, RANK_6, RANK_7);
    }
    {
      final var toSquare = Square.H5;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_F, FILE_G);
      appendMoveWithRank(resultSet, toSquare, RANK_3, RANK_4, RANK_6, RANK_7);
    }
    {
      final var toSquare = Square.A6;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_B, FILE_C);
      appendMoveWithRank(resultSet, toSquare, RANK_4, RANK_5, RANK_7, RANK_8);
    }
    {
      final var toSquare = Square.H6;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_F, FILE_G);
      appendMoveWithRank(resultSet, toSquare, RANK_4, RANK_5, RANK_7, RANK_8);
    }

    // first inner quadrat edges
    {
      final var toSquare = Square.B2;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_A, FILE_C, FILE_D);
      appendMoveWithRank(resultSet, toSquare, RANK_1, RANK_3);
    }
    {
      final var toSquare = Square.B7;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_A, FILE_C, FILE_D);
      appendMoveWithRank(resultSet, toSquare, RANK_6, RANK_8);
    }
    {
      final var toSquare = Square.G2;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_E, FILE_F, FILE_H);
      appendMoveWithRank(resultSet, toSquare, RANK_1, RANK_3);
    }
    {
      final var toSquare = Square.G7;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_E, FILE_F, FILE_H);
      appendMoveWithRank(resultSet, toSquare, RANK_6, RANK_8);
    }

    // first inner quadrat without edges second and seventh rank
    {
      final var toSquare = Square.C2;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_A, FILE_B, FILE_D, FILE_E);
      appendMoveWithRank(resultSet, toSquare, RANK_1, RANK_3);
      appendMoveWithFromSquare(resultSet, toSquare, A1, A3, E1, E3);
    }
    {
      final var toSquare = Square.C7;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_A, FILE_B, FILE_D, FILE_E);
      appendMoveWithRank(resultSet, toSquare, RANK_6, RANK_8);
      appendMoveWithFromSquare(resultSet, toSquare, A6, A8, E6, E8);
    }
    {
      final var toSquare = Square.D2;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_B, FILE_C, FILE_E, FILE_F);
      appendMoveWithRank(resultSet, toSquare, RANK_1, RANK_3);
      appendMoveWithFromSquare(resultSet, toSquare, B1, B3, F1, F3);
    }
    {
      final var toSquare = Square.D7;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_B, FILE_C, FILE_E, FILE_F);
      appendMoveWithRank(resultSet, toSquare, RANK_6, RANK_8);
      appendMoveWithFromSquare(resultSet, toSquare, B6, B8, F6, F8);
    }
    {
      final var toSquare = Square.E2;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_C, FILE_D, FILE_F, FILE_G);
      appendMoveWithRank(resultSet, toSquare, RANK_1, RANK_3);
      appendMoveWithFromSquare(resultSet, toSquare, C1, C3, G1, G3);
    }
    {
      final var toSquare = Square.E7;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_C, FILE_D, FILE_F, FILE_G);
      appendMoveWithRank(resultSet, toSquare, RANK_6, RANK_8);
      appendMoveWithFromSquare(resultSet, toSquare, C6, C8, G6, G8);
    }
    {
      final var toSquare = Square.F2;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_D, FILE_E, FILE_G, FILE_H);
      appendMoveWithRank(resultSet, toSquare, RANK_1, RANK_3);
      appendMoveWithFromSquare(resultSet, toSquare, D1, D3, H1, H3);
    }
    {
      final var toSquare = Square.F7;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_D, FILE_E, FILE_G, FILE_H);
      appendMoveWithRank(resultSet, toSquare, RANK_6, RANK_8);
      appendMoveWithFromSquare(resultSet, toSquare, D6, D8, H6, H8);
    }

    // first inner quadrat without edges remaining squares on A and H file
    {
      final var toSquare = Square.B3;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_A, FILE_C, FILE_D);
      appendMoveWithRank(resultSet, toSquare, RANK_1, RANK_2, RANK_4, RANK_5);
      appendMoveWithFromSquare(resultSet, toSquare, A1, A5, C1, C5);
    }
    {
      final var toSquare = Square.G3;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_E, FILE_F, FILE_H);
      appendMoveWithRank(resultSet, toSquare, RANK_1, RANK_2, RANK_4, RANK_5);
      appendMoveWithFromSquare(resultSet, toSquare, F1, F5, H1, H5);
    }
    {
      final var toSquare = Square.B4;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_A, FILE_C, FILE_D);
      appendMoveWithRank(resultSet, toSquare, RANK_2, RANK_3, RANK_5, RANK_6);
      appendMoveWithFromSquare(resultSet, toSquare, A2, A6, C2, C6);
    }
    {
      final var toSquare = Square.G4;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_E, FILE_F, FILE_H);
      appendMoveWithRank(resultSet, toSquare, RANK_2, RANK_3, RANK_5, RANK_6);
      appendMoveWithFromSquare(resultSet, toSquare, F2, F6, H2, H6);
    }
    {
      final var toSquare = Square.B5;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_A, FILE_C, FILE_D);
      appendMoveWithRank(resultSet, toSquare, RANK_3, RANK_4, RANK_6, RANK_7);
      appendMoveWithFromSquare(resultSet, toSquare, A3, A7, C3, C7);
    }
    {
      final var toSquare = Square.G5;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_E, FILE_F, FILE_H);
      appendMoveWithRank(resultSet, toSquare, RANK_3, RANK_4, RANK_6, RANK_7);
      appendMoveWithFromSquare(resultSet, toSquare, F3, F7, H3, H7);
    }
    {
      final var toSquare = Square.B6;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_A, FILE_C, FILE_D);
      appendMoveWithRank(resultSet, toSquare, RANK_4, RANK_5, RANK_7, RANK_8);
      appendMoveWithFromSquare(resultSet, toSquare, A4, A8, C4, C8);
    }
    {
      final var toSquare = Square.G6;

      appendOnlyMove(resultSet, toSquare);
      appendMoveWithFile(resultSet, toSquare, FILE_E, FILE_F, FILE_H);
      appendMoveWithRank(resultSet, toSquare, RANK_4, RANK_5, RANK_7, RANK_8);
      appendMoveWithFromSquare(resultSet, toSquare, F4, F8, H4, H8);
    }

    // third and fourth innner quadrat
    // remaining squares
    for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
      if (THIRD_QUADRAT_SQUARES.contains(toSquare) || FOURTH_QUADRAT_SQUARES.contains(toSquare)) {
        appendOnlyMove(resultSet, toSquare);

        // we have the list of all moves from that square to another square
        // so any knight on such another square can move to the to square
        final Set<File> possibleFileList = new TreeSet<>();
        final Set<Rank> possibleRankList = new TreeSet<>();
        for (final EmptyBoardMove move : AbstractEmptyBoardSquares.calculateNonPawnEmptyBoardMoves(KNIGHT, toSquare)) {
          final Square squareFromWhichKnightCanMoveToToSquare = move.toSquare();
          appendMoveWithFromSquare(resultSet, toSquare, squareFromWhichKnightCanMoveToToSquare);

          // the files repeat so we sample them with a treeset to remove duplicates
          possibleFileList.add(squareFromWhichKnightCanMoveToToSquare.getFile());
          // the ranks repeat so we sample them with a treeset to remove duplicates
          possibleRankList.add(squareFromWhichKnightCanMoveToToSquare.getRank());
        }

        for (final File file : possibleFileList) {
          appendMoveWithFile(resultSet, toSquare, file);
        }
        for (final Rank rank : possibleRankList) {
          appendMoveWithRank(resultSet, toSquare, rank);
        }
      }
    }

    return resultSet;
  }

  @Override
  Set<String> calculateEnumConstantFormal() {
    final Set<String> resultSet = new TreeSet<>();
    for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
      final Set<EmptyBoardMove> emptyBoardMoveSet = AbstractEmptyBoardSquares.calculateNonPawnEmptyBoardMovesTo(KNIGHT,
          toSquare);
      final List<Square> fromSquareList = calculateFromSquareList(emptyBoardMoveSet);

      // checking each from square individually
      // file and rank
      for (final Square fromSquare : fromSquareList) {
        // there is a piece which can move to the to square
        appendOnlyMove(resultSet, toSquare);

        if (calculateIsFromFilePossibleKnight(fromSquare, fromSquareList)) {
          appendMoveWithFile(resultSet, toSquare, fromSquare.getFile());
        }

        if (calculateIsFromRankPossibleKnight(fromSquare, fromSquareList)) {
          appendMoveWithRank(resultSet, toSquare, fromSquare.getRank());
        }
      }

      // checking each from square individually
      // square
      for (final Square fromSquare : fromSquareList) {
        // there is a piece which can move to the to square
        appendOnlyMove(resultSet, toSquare);

        if (calculateIsFromRankPossibleKnight(fromSquare, fromSquareList)
            && calculateHasOtherMovesFromSameRank(fromSquare, fromSquareList)) {
          appendMoveWithFromSquare(resultSet, toSquare, fromSquare);
        }
      }
    }

    return resultSet;
  }

  private static boolean calculateIsFromRankPossibleKnight(Square fromSquare, List<Square> fromSquareList) {
    for (final Square otherFromSquare : fromSquareList) {
      if (otherFromSquare.getRank() != fromSquare.getRank() && otherFromSquare.getFile() == fromSquare.getFile()) {
        return true;
      }
    }
    return false;
  }

  static boolean calculateIsFromFilePossibleKnight(Square fromSquare, List<Square> fromSquareList) {
    for (final Square otherFromSquare : fromSquareList) {
      if (otherFromSquare.getFile() != fromSquare.getFile()) {
        return true;
      }
    }
    return false;
  }
}
