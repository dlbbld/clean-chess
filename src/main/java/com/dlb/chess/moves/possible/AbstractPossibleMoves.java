package com.dlb.chess.moves.possible;

import static com.dlb.chess.common.utility.ImmutableUtility.calculateMapForSet;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.NonNullWrapper;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.model.PossibleMove;
import com.dlb.chess.model.EmptyBoardMove;
import com.dlb.chess.model.PawnDiagonalBoardMove;
import com.dlb.chess.moves.utility.PawnUtility;
import com.dlb.chess.squares.emptyboard.AbstractEmptyBoardSquares;
import com.google.common.collect.ImmutableMap;

//TODO use or decommission
public class AbstractPossibleMoves implements EnumConstants {

  private static final ImmutableMap<Side, ImmutableMap<Square, ImmutableMap<Square, PossibleMove>>> ROOK_POSSIBLE_MOVE;
  private static final ImmutableMap<Side, ImmutableMap<Square, ImmutableMap<Square, PossibleMove>>> KNIGHT_POSSIBLE_MOVE;
  private static final ImmutableMap<Side, ImmutableMap<Square, ImmutableMap<Square, PossibleMove>>> BISHOP_POSSIBLE_MOVE;
  private static final ImmutableMap<Side, ImmutableMap<Square, ImmutableMap<Square, PossibleMove>>> QUEEN_POSSIBLE_MOVE;
  private static final ImmutableMap<Side, ImmutableMap<Square, ImmutableMap<Square, PossibleMove>>> KING_POSSIBLE_MOVE;
  private static final ImmutableMap<Side, ImmutableMap<Square, ImmutableMap<Square, PossibleMove>>> PAWN_POSSIBLE_MOVE;

  static {
    {
      final PieceType pieceType = ROOK;
      final Set<EmptyBoardMove> emptyBoardMoveSet = AbstractEmptyBoardSquares
          .calculateNonPawnEmptyBoardMoves(pieceType);
      ROOK_POSSIBLE_MOVE = calculateMapForSet(pieceType, emptyBoardMoveSet, emptyBoardMoveSet);
    }
    {
      final PieceType pieceType = KNIGHT;
      final Set<EmptyBoardMove> emptyBoardMoveSet = AbstractEmptyBoardSquares
          .calculateNonPawnEmptyBoardMoves(pieceType);
      KNIGHT_POSSIBLE_MOVE = calculateMapForSet(pieceType, emptyBoardMoveSet, emptyBoardMoveSet);
    }
    {
      final PieceType pieceType = BISHOP;
      final Set<EmptyBoardMove> emptyBoardMoveSet = AbstractEmptyBoardSquares
          .calculateNonPawnEmptyBoardMoves(pieceType);
      BISHOP_POSSIBLE_MOVE = calculateMapForSet(pieceType, emptyBoardMoveSet, emptyBoardMoveSet);
    }
    {
      final PieceType pieceType = QUEEN;
      final Set<EmptyBoardMove> emptyBoardMoveSet = AbstractEmptyBoardSquares
          .calculateNonPawnEmptyBoardMoves(pieceType);
      QUEEN_POSSIBLE_MOVE = calculateMapForSet(pieceType, emptyBoardMoveSet, emptyBoardMoveSet);
    }
    {
      final PieceType pieceType = KING;
      final Set<EmptyBoardMove> emptyBoardMoveSet = AbstractEmptyBoardSquares
          .calculateNonPawnEmptyBoardMoves(pieceType);
      KING_POSSIBLE_MOVE = calculateMapForSet(pieceType, emptyBoardMoveSet, emptyBoardMoveSet);
    }
    {
      PAWN_POSSIBLE_MOVE = calculatePawnMap();
    }
  }

  private static ImmutableMap<Side, ImmutableMap<Square, ImmutableMap<Square, PossibleMove>>> calculatePawnMap() {
    final PieceType pieceType = PAWN;

    final Set<EmptyBoardMove> emptyBoardMoveSetWhite = AbstractEmptyBoardSquares.calculatePawnEmptyBoardMoves(WHITE);
    final Set<EmptyBoardMove> emptyBoardMoveSetBlack = AbstractEmptyBoardSquares.calculatePawnEmptyBoardMoves(BLACK);

    final Set<PawnDiagonalBoardMove> pawnDiagonalMovesSetWhite = PawnUtility.calculatePawnDiagonalMoves(WHITE);
    final Set<PawnDiagonalBoardMove> pawnDiagonalMovesSetBlack = PawnUtility.calculatePawnDiagonalMoves(BLACK);

    final ImmutableMap<Square, ImmutableMap<Square, PossibleMove>> mapWhite = AbstractPossibleMoves
        .calculateMapForSetSide(WHITE, pieceType, emptyBoardMoveSetWhite, pawnDiagonalMovesSetWhite);
    final ImmutableMap<Square, ImmutableMap<Square, PossibleMove>> mapBlack = AbstractPossibleMoves
        .calculateMapForSetSide(BLACK, pieceType, emptyBoardMoveSetBlack, pawnDiagonalMovesSetBlack);

    final EnumMap<Side, ImmutableMap<Square, ImmutableMap<Square, PossibleMove>>> possibleMoveMap = NonNullWrapperCommon
        .newEnumMap(Side.class);

    possibleMoveMap.put(WHITE, mapWhite);
    possibleMoveMap.put(BLACK, mapBlack);

    return NonNullWrapperCommon.immutableEnumMap(possibleMoveMap);

  }

  public static PossibleMove getPossibleMove(Side side, PieceType pieceType, Square fromSquare, Square toSquare) {
    switch (pieceType) {
      case BISHOP:
        return NonNullWrapper.getPossibleMove(BISHOP_POSSIBLE_MOVE, side, fromSquare, toSquare);
      case KING:
        return NonNullWrapper.getPossibleMove(KING_POSSIBLE_MOVE, side, fromSquare, toSquare);
      case KNIGHT:
        return NonNullWrapper.getPossibleMove(KNIGHT_POSSIBLE_MOVE, side, fromSquare, toSquare);
      case PAWN:
        return NonNullWrapper.getPossibleMove(PAWN_POSSIBLE_MOVE, side, fromSquare, toSquare);
      case QUEEN:
        return NonNullWrapper.getPossibleMove(QUEEN_POSSIBLE_MOVE, side, fromSquare, toSquare);
      case ROOK:
        return NonNullWrapper.getPossibleMove(ROOK_POSSIBLE_MOVE, side, fromSquare, toSquare);
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static Set<Square> calculateDiagonalSquares(Square fromSquare,
      Set<PawnDiagonalBoardMove> diagonalBoardMoveSetSide) {
    final var diagonalSquares = EnumSet.noneOf(Square.class);
    for (final PawnDiagonalBoardMove diagonalBoardMove : diagonalBoardMoveSetSide) {
      if (diagonalBoardMove.fromSquare() == fromSquare) {
        diagonalSquares.add(diagonalBoardMove.toSquare());
      }
    }
    if (diagonalSquares == null) {
      // to avoid null pointer warning for non possible null value
      throw new ProgrammingMistakeException("The variable can't be null for design of enumset.");
    }
    return diagonalSquares;
  }

  public static ImmutableMap<Square, ImmutableMap<Square, PossibleMove>> calculateMapForSetSide(Side havingMove,
      PieceType pieceType, Set<EmptyBoardMove> emptyBoardMoveSetSide,
      Set<PawnDiagonalBoardMove> diagonalBoardMoveSetSide) {

    final EnumMap<Square, ImmutableMap<Square, PossibleMove>> mapSide = NonNullWrapperCommon.newEnumMap(Square.class);

    for (final Square fromSquare : Square.BOARD_SQUARE_LIST) {
      final EnumMap<Square, PossibleMove> squareFromToMapSide = NonNullWrapperCommon.newEnumMap(Square.class);
      for (final EmptyBoardMove emptyBoardMove : emptyBoardMoveSetSide) {
        final PossibleMove possibleMoveSide = new PossibleMove(havingMove, pieceType, emptyBoardMove.fromSquare(),
            emptyBoardMove.toSquare());
        squareFromToMapSide.put(fromSquare, possibleMoveSide);

        // now get the diagonal moves
        // there must be always at least one, this we check
        final Set<Square> calculateDiagonalSquares = calculateDiagonalSquares(fromSquare, diagonalBoardMoveSetSide);
        final Side oppositeSide = havingMove.getOppositeSide();

        if (Rank.calculateIsPromotionRank(havingMove, fromSquare.getRank())
            || Rank.calculateIsPromotionRank(oppositeSide, fromSquare.getRank())) {
          if (calculateDiagonalSquares.isEmpty()) {
            throw new ProgrammingMistakeException(
                "Number of diagonal capture pawn squares for opponent promotion rank must be zero");
          }
        } else if (fromSquare.getFile().getIsBorderFile()) {
          if (calculateDiagonalSquares.size() != 1) {
            throw new ProgrammingMistakeException(
                "Number of diagonal capture pawn squares for border files must be one");
          }
        } else if (calculateDiagonalSquares.size() != 2) {
          throw new ProgrammingMistakeException(
              "Number of diagonal capture pawn squares for non border files must be two");
        }
        for (final Square diagonalSquare : calculateDiagonalSquares) {
          final PossibleMove diagonalMoveSide = new PossibleMove(havingMove, pieceType, fromSquare, diagonalSquare);
          squareFromToMapSide.put(fromSquare, diagonalMoveSide);
        }
      }
      @NonNull final ImmutableMap<Square, PossibleMove> squareFromToMapSideUnmodifiableMap = NonNullWrapperCommon
          .immutableEnumMap(squareFromToMapSide);
      mapSide.put(fromSquare, squareFromToMapSideUnmodifiableMap);
    }

    @NonNull final ImmutableMap<Square, ImmutableMap<Square, PossibleMove>> mapSideUnmodifiableMap = NonNullWrapperCommon
        .immutableEnumMap(mapSide);

    return mapSideUnmodifiableMap;
  }

}
