package com.dlb.chess.moves.utility;

import static com.dlb.chess.common.utility.ImmutableUtility.constructListSquare;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.board.model.UpdateSquare;
import com.dlb.chess.common.constants.CastlingConstants;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.enums.MoveCheck;
import com.dlb.chess.model.CastlingRightBoth;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.squares.to.threaten.AbstractThreatenSquares;
import com.google.common.collect.ImmutableList;

public class CastlingUtility implements EnumConstants {

  // TODO improve design - to separate file as used elsewhere
  // constants for white
  public static final Square WHITE_KING_FROM = E1;

  public static final Square WHITE_KING_KING_SIDE_CASTLING_TO = G1;
  public static final Square WHITE_ROOK_KING_SIDE_CASTLING_FROM = H1;
  public static final Square WHITE_ROOK_KING_SIDE_CASTLING_TO = F1;

  public static final Square WHITE_KING_QUEEN_SIDE_CASTLING_TO = C1;
  public static final Square WHITE_ROOK_QUEEN_SIDE_CASTLING_FROM = A1;
  public static final Square WHITE_ROOK_QUEEN_SIDE_CASTLING_TO = D1;

  // constants for black
  public static final Square BLACK_KING_FROM = E8;

  public static final Square BLACK_KING_KING_SIDE_CASTLING_TO = G8;
  public static final Square BLACK_ROOK_KING_SIDE_CASTLING_FROM = H8;
  public static final Square BLACK_ROOK_KING_SIDE_CASTLING_TO = F8;

  public static final Square BLACK_KING_QUEEN_SIDE_CASTLING_TO = C8;
  public static final Square BLACK_ROOK_QUEEN_SIDE_CASTLING_FROM = A8;
  public static final Square BLACK_ROOK_QUEEN_SIDE_CASTLING_TO = D8;

  private static final ImmutableList<Square> WHITE_QUEEN_SIDE_CASTLING_REQUIRED_EMPTY_SQUARE_LIST = constructListSquare(
      B1, C1, D1);

  private static final ImmutableList<Square> WHITE_KING_SIDE_CASTLING_REQUIRED_EMPTY_SQUARE_LIST = constructListSquare(
      F1, G1);

  private static final ImmutableList<Square> BLACK_QUEEN_SIDE_CASTLING_REQUIRED_EMPTY_SQUARE_LIST = constructListSquare(
      B8, C8, D8);

  private static final ImmutableList<Square> BLACK_KING_SIDE_CASTLING_REQUIRED_EMPTY_SQUARE_LIST = constructListSquare(
      F8, G8);

  private static List<Square> calculateQueenSideCastlingRequiredEmptySquareList(Side havingMove) {
    switch (havingMove) {
      case BLACK:
        return BLACK_QUEEN_SIDE_CASTLING_REQUIRED_EMPTY_SQUARE_LIST;
      case WHITE:
        return WHITE_QUEEN_SIDE_CASTLING_REQUIRED_EMPTY_SQUARE_LIST;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  private static List<Square> calculateKingSideCastlingRequiredEmptySquareList(Side havingMove) {
    switch (havingMove) {
      case BLACK:
        return BLACK_KING_SIDE_CASTLING_REQUIRED_EMPTY_SQUARE_LIST;
      case WHITE:
        return WHITE_KING_SIDE_CASTLING_REQUIRED_EMPTY_SQUARE_LIST;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  private static final Square WHITE_QUEEN_SIDE_TRAVEL_OVER_SQUARE = D1;
  private static final Square BLACK_QUEEN_SIDE_TRAVEL_OVER_SQUARE = D8;

  private static final Square WHITE_KING_SIDE_TRAVEL_OVER_SQUARE = F1;
  private static final Square BLACK_KING_SIDE_TRAVEL_OVER_SQUARE = F8;

  private static Square calculateKingOriginalSquare(Side havingMove) {
    switch (havingMove) {
      case BLACK:
        return BLACK_KING_FROM;
      case WHITE:
        return WHITE_KING_FROM;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  private static Square calculateQueenSideKingTravelOverSquare(Side havingMove) {
    switch (havingMove) {
      case BLACK:
        return BLACK_QUEEN_SIDE_TRAVEL_OVER_SQUARE;
      case WHITE:
        return WHITE_QUEEN_SIDE_TRAVEL_OVER_SQUARE;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  private static Square calculateKingSideKingTravelOverSquare(Side havingMove) {
    switch (havingMove) {
      case BLACK:
        return BLACK_KING_SIDE_TRAVEL_OVER_SQUARE;
      case WHITE:
        return WHITE_KING_SIDE_TRAVEL_OVER_SQUARE;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  private static Square calculateQueenSideKingDestinationSquare(Side havingMove) {
    switch (havingMove) {
      case BLACK:
        return BLACK_KING_QUEEN_SIDE_CASTLING_TO;
      case WHITE:
        return WHITE_KING_QUEEN_SIDE_CASTLING_TO;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  private static Square calculateKingSideKingDestinationSquare(Side havingMove) {
    switch (havingMove) {
      case BLACK:
        return BLACK_KING_KING_SIDE_CASTLING_TO;
      case WHITE:
        return WHITE_KING_KING_SIDE_CASTLING_TO;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static MoveCheck calculateQueenSideCheckCondition(StaticPosition staticPosition, Side havingMove) {

    final Side oppositeSide = havingMove.getOppositeSide();
    final Set<Square> threatenedSquares = AbstractThreatenSquares.calculateThreatenedSquares(staticPosition,
        oppositeSide);

    if (threatenedSquares.contains(calculateKingOriginalSquare(havingMove))) {
      return MoveCheck.CASTLING_PRIORITY_4_KING_IN_CHECK;
    }
    if (threatenedSquares.contains(calculateQueenSideKingTravelOverSquare(havingMove))) {
      return MoveCheck.CASTLING_PRIORITY_5_KING_WOULD_TRAVEL_THROUGH_CHECK;
    }
    if (threatenedSquares.contains(calculateQueenSideKingDestinationSquare(havingMove))) {
      return MoveCheck.CASTLING_PRIORITY_6_KING_WOULD_END_IN_CHECK;
    }
    return MoveCheck.SUCCESS;
  }

  public static MoveCheck calculateKingSideCheckCondition(StaticPosition staticPosition, Side havingMove) {

    final Side oppositeSide = havingMove.getOppositeSide();
    final Set<Square> threatenedSquares = AbstractThreatenSquares.calculateThreatenedSquares(staticPosition,
        oppositeSide);

    if (threatenedSquares.contains(calculateKingOriginalSquare(havingMove))) {
      return MoveCheck.CASTLING_PRIORITY_4_KING_IN_CHECK;
    }
    if (threatenedSquares.contains(calculateKingSideKingTravelOverSquare(havingMove))) {
      return MoveCheck.CASTLING_PRIORITY_5_KING_WOULD_TRAVEL_THROUGH_CHECK;
    }
    if (threatenedSquares.contains(calculateKingSideKingDestinationSquare(havingMove))) {
      return MoveCheck.CASTLING_PRIORITY_6_KING_WOULD_END_IN_CHECK;
    }
    return MoveCheck.SUCCESS;
  }

  public static boolean calculateQueenSideCastlingIsEmptySquaresBetweenRookAndKing(StaticPosition staticPosition,
      Side havingMove) {
    final List<Square> requiredEmptySquareList = calculateQueenSideCastlingRequiredEmptySquareList(havingMove);
    return calculateIsAllEmpty(staticPosition, requiredEmptySquareList);
  }

  public static boolean calculateKingSideCastlingIsEmptySquaresBetweenRookAndKing(StaticPosition staticPosition,
      Side havingMove) {
    final List<Square> requiredEmptySquareList = calculateKingSideCastlingRequiredEmptySquareList(havingMove);
    return calculateIsAllEmpty(staticPosition, requiredEmptySquareList);
  }

  private static boolean calculateIsAllEmpty(StaticPosition staticPosition, List<Square> squareList) {
    for (final Square square : squareList) {
      if (!calculateIsEmptySquare(staticPosition, square)) {
        return false;
      }
    }
    return true;
  }

  private static boolean calculateIsEmptySquare(StaticPosition staticPosition, Square square) {
    return staticPosition.get(square) == Piece.NONE;
  }

  public static List<UpdateSquare> performCastlingMovements(MoveSpecification moveSpecification) {
    final List<UpdateSquare> result = new ArrayList<>(performKingMovement(moveSpecification));
    result.addAll(performRookMovement(moveSpecification));
    return result;
  }

  public static List<UpdateSquare> performCastlingUndoMovements(LegalMove move) {
    final List<UpdateSquare> result = new ArrayList<>(performKingUndoMovement(move));
    result.addAll(performRookUndoMovement(move));
    return result;
  }

  private static List<UpdateSquare> performKingMovement(MoveSpecification moveSpecification) {
    final List<UpdateSquare> result = new ArrayList<>();
    switch (moveSpecification.castlingMove()) {
      case KING_SIDE:

        switch (moveSpecification.havingMove()) {
          case BLACK:
            // king move
            result.add(new UpdateSquare(BLACK_KING_FROM));
            result.add(new UpdateSquare(BLACK_KING_KING_SIDE_CASTLING_TO, BLACK_KING));
            break;
          case WHITE:
            // king move
            result.add(new UpdateSquare(WHITE_KING_FROM));
            result.add(new UpdateSquare(WHITE_KING_KING_SIDE_CASTLING_TO, WHITE_KING));
            break;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
        break;
      case QUEEN_SIDE:

        switch (moveSpecification.havingMove()) {
          case BLACK:
            // king move
            result.add(new UpdateSquare(BLACK_KING_FROM));
            result.add(new UpdateSquare(BLACK_KING_QUEEN_SIDE_CASTLING_TO, BLACK_KING));
            break;
          case WHITE:
            // king move
            result.add(new UpdateSquare(WHITE_KING_FROM));
            result.add(new UpdateSquare(WHITE_KING_QUEEN_SIDE_CASTLING_TO, WHITE_KING));
            break;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
        break;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
    return result;
  }

  private static List<UpdateSquare> performRookMovement(MoveSpecification moveSpecification) {
    final List<UpdateSquare> result = new ArrayList<>();
    switch (moveSpecification.castlingMove()) {
      case KING_SIDE:

        switch (moveSpecification.havingMove()) {
          case BLACK:
            // rook move
            result.add(new UpdateSquare(BLACK_ROOK_KING_SIDE_CASTLING_FROM));
            result.add(new UpdateSquare(BLACK_ROOK_KING_SIDE_CASTLING_TO, BLACK_ROOK));
            break;
          case WHITE:
            // rook move
            result.add(new UpdateSquare(WHITE_ROOK_KING_SIDE_CASTLING_FROM));
            result.add(new UpdateSquare(WHITE_ROOK_KING_SIDE_CASTLING_TO, WHITE_ROOK));
            break;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
        break;
      case QUEEN_SIDE:

        switch (moveSpecification.havingMove()) {
          case BLACK:
            // rook move
            result.add(new UpdateSquare(BLACK_ROOK_QUEEN_SIDE_CASTLING_FROM));
            result.add(new UpdateSquare(BLACK_ROOK_QUEEN_SIDE_CASTLING_TO, BLACK_ROOK));
            break;
          case WHITE:
            // rook move
            result.add(new UpdateSquare(WHITE_ROOK_QUEEN_SIDE_CASTLING_FROM));
            result.add(new UpdateSquare(WHITE_ROOK_QUEEN_SIDE_CASTLING_TO, WHITE_ROOK));
            break;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
        break;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
    return result;
  }

  private static List<UpdateSquare> performKingUndoMovement(LegalMove legalMove) {

    final List<UpdateSquare> result = new ArrayList<>();

    final MoveSpecification moveSpecification = legalMove.moveSpecification();
    switch (moveSpecification.castlingMove()) {
      case KING_SIDE:
        switch (moveSpecification.havingMove()) {
          case BLACK:
            // undo king move
            result.add(new UpdateSquare(BLACK_KING_FROM, BLACK_KING));
            result.add(new UpdateSquare(BLACK_KING_KING_SIDE_CASTLING_TO));
            break;
          case WHITE:
            // undo king move
            result.add(new UpdateSquare(WHITE_KING_FROM, WHITE_KING));
            result.add(new UpdateSquare(WHITE_KING_KING_SIDE_CASTLING_TO));
            break;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
        break;
      case QUEEN_SIDE:
        switch (moveSpecification.havingMove()) {
          case BLACK:
            // king move
            result.add(new UpdateSquare(BLACK_KING_FROM, BLACK_KING));
            result.add(new UpdateSquare(BLACK_KING_QUEEN_SIDE_CASTLING_TO));
            break;
          case WHITE:
            // king move
            result.add(new UpdateSquare(WHITE_KING_FROM, WHITE_KING));
            result.add(new UpdateSquare(WHITE_KING_QUEEN_SIDE_CASTLING_TO));
            break;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
        break;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }

    return result;
  }

  private static List<UpdateSquare> performRookUndoMovement(LegalMove legalMove) {

    final List<UpdateSquare> result = new ArrayList<>();

    final MoveSpecification moveSpecification = legalMove.moveSpecification();
    switch (moveSpecification.castlingMove()) {
      case KING_SIDE:
        switch (moveSpecification.havingMove()) {
          case BLACK:
            // undo rook move
            result.add(new UpdateSquare(BLACK_ROOK_KING_SIDE_CASTLING_FROM, BLACK_ROOK));
            result.add(new UpdateSquare(BLACK_ROOK_KING_SIDE_CASTLING_TO));
            break;
          case WHITE:
            // undo rook move
            result.add(new UpdateSquare(WHITE_ROOK_KING_SIDE_CASTLING_FROM, WHITE_ROOK));
            result.add(new UpdateSquare(WHITE_ROOK_KING_SIDE_CASTLING_TO));
            break;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
        break;
      case QUEEN_SIDE:
        switch (moveSpecification.havingMove()) {
          case BLACK:
            // rook move
            result.add(new UpdateSquare(BLACK_ROOK_QUEEN_SIDE_CASTLING_FROM, BLACK_ROOK));
            result.add(new UpdateSquare(BLACK_ROOK_QUEEN_SIDE_CASTLING_TO));
            break;
          case WHITE:
            // rook move
            result.add(new UpdateSquare(WHITE_ROOK_QUEEN_SIDE_CASTLING_FROM, WHITE_ROOK));
            result.add(new UpdateSquare(WHITE_ROOK_QUEEN_SIDE_CASTLING_TO));
            break;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
        break;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }

    return result;
  }

  private static boolean calculateIsCastlingQueenSide(MoveSpecification moveSpecification) {
    switch (moveSpecification.castlingMove()) {
      case NONE:
        return false;
      case KING_SIDE:
        return false;
      case QUEEN_SIDE:
        return true;
      default:
        throw new IllegalArgumentException();
    }
  }

  private static boolean calculateIsCastlingKingSide(MoveSpecification moveSpecification) {
    switch (moveSpecification.castlingMove()) {
      case NONE:
        return false;
      case KING_SIDE:
        return true;
      case QUEEN_SIDE:
        return false;
      default:
        throw new IllegalArgumentException();
    }
  }

  public static boolean calculateIsCastlingMove(MoveSpecification moveSpecification) {
    return calculateIsCastlingQueenSide(moveSpecification) || calculateIsCastlingKingSide(moveSpecification);
  }

  public static CastlingRightBoth calculateCastlingRightBoth(CastlingRightBoth lastCastlingRightBoth,
      LegalMove legalMove) {

    final Side havingMoveBefore = legalMove.moveSpecification().havingMove();
    final Side havingMove = havingMoveBefore.getOppositeSide();

    final CastlingRight oldCastlingRightHavingMoveBefore = CastlingUtility.getCastlingRight(lastCastlingRightBoth,
        havingMoveBefore);
    final CastlingRight oldCastlingRightHavingMove = CastlingUtility.getCastlingRight(lastCastlingRightBoth,
        havingMove);

    final CastlingRight newCastlingRightHavingMoveBefore;
    final CastlingRight newCastlingRightHavingMove;

    // as always, the castling needs a separate treatment
    final MoveSpecification moveSpecification = legalMove.moveSpecification();
    if (calculateIsCastlingMove(moveSpecification)) {
      newCastlingRightHavingMoveBefore = CastlingRight.NONE;
      newCastlingRightHavingMove = oldCastlingRightHavingMove;
    } else {
      switch (oldCastlingRightHavingMoveBefore) {
        case KING_AND_QUEEN_SIDE:
          if (calculateHasKingMoved(legalMove)) {
            newCastlingRightHavingMoveBefore = CastlingRight.NONE;
          } else if (calculateHasKingSideRookMoved(legalMove)) {
            newCastlingRightHavingMoveBefore = CastlingRight.QUEEN_SIDE;
          } else if (calculateHasQueenSideRookMoved(legalMove)) {
            newCastlingRightHavingMoveBefore = CastlingRight.KING_SIDE;
          } else {
            newCastlingRightHavingMoveBefore = CastlingRight.KING_AND_QUEEN_SIDE;
          }
          break;
        case KING_SIDE:
          if (calculateHasKingMoved(legalMove) || calculateHasKingSideRookMoved(legalMove)) {
            newCastlingRightHavingMoveBefore = CastlingRight.NONE;
          } else {
            newCastlingRightHavingMoveBefore = CastlingRight.KING_SIDE;
          }
          break;
        case QUEEN_SIDE:
          if (calculateHasKingMoved(legalMove) || calculateHasQueenSideRookMoved(legalMove)) {
            newCastlingRightHavingMoveBefore = CastlingRight.NONE;
          } else {
            newCastlingRightHavingMoveBefore = CastlingRight.QUEEN_SIDE;
          }
          break;
        case NONE:
          newCastlingRightHavingMoveBefore = CastlingRight.NONE;
          break;
        default:
          throw new IllegalArgumentException();
      }

      switch (oldCastlingRightHavingMove) {
        case KING_AND_QUEEN_SIDE:
          if (calculateHasCapturedOpponentRookKingSide(legalMove)) {
            newCastlingRightHavingMove = CastlingRight.QUEEN_SIDE;
          } else if (calculateHasCapturedOpponentRookQueenSide(legalMove)) {
            newCastlingRightHavingMove = CastlingRight.KING_SIDE;
          } else {
            newCastlingRightHavingMove = CastlingRight.KING_AND_QUEEN_SIDE;
          }
          break;
        case KING_SIDE:
          if (calculateHasCapturedOpponentRookKingSide(legalMove)) {
            newCastlingRightHavingMove = CastlingRight.NONE;
          } else {
            newCastlingRightHavingMove = CastlingRight.KING_SIDE;
          }
          break;
        case QUEEN_SIDE:
          if (calculateHasCapturedOpponentRookQueenSide(legalMove)) {
            newCastlingRightHavingMove = CastlingRight.NONE;
          } else {
            newCastlingRightHavingMove = CastlingRight.QUEEN_SIDE;
          }
          break;
        case NONE:
          newCastlingRightHavingMove = CastlingRight.NONE;
          break;
        default:
          throw new IllegalArgumentException();
      }
    }

    return lookupStaticCastlingRightBoth(havingMove, newCastlingRightHavingMoveBefore, newCastlingRightHavingMove);
  }

  private static boolean calculateHasKingMoved(LegalMove legalMove) {
    return legalMove.movingPiece().getPieceType() == KING;
  }

  private static boolean calculateHasQueenSideRookMoved(LegalMove legalMove) {
    if (legalMove.movingPiece().getPieceType() != ROOK) {
      return false;
    }
    final var rookOriginalSquare = switch (legalMove.moveSpecification().havingMove()) {
      case BLACK -> BLACK_ROOK_QUEEN_SIDE_CASTLING_FROM;
      case WHITE -> WHITE_ROOK_QUEEN_SIDE_CASTLING_FROM;
      case NONE -> throw new IllegalArgumentException();
    };
    return legalMove.moveSpecification().fromSquare() == rookOriginalSquare;
  }

  private static boolean calculateHasKingSideRookMoved(LegalMove legalMove) {
    if (legalMove.movingPiece().getPieceType() != ROOK) {
      return false;
    }
    final var rookOriginalSquare = switch (legalMove.moveSpecification().havingMove()) {
      case BLACK -> BLACK_ROOK_KING_SIDE_CASTLING_FROM;
      case WHITE -> WHITE_ROOK_KING_SIDE_CASTLING_FROM;
      case NONE -> throw new IllegalArgumentException();
    };
    return legalMove.moveSpecification().fromSquare() == rookOriginalSquare;
  }

  private static boolean calculateHasCapturedOpponentRookQueenSide(LegalMove legalMove) {
    if (legalMove.pieceCaptured() != Piece.NONE && legalMove.pieceCaptured().getPieceType() == ROOK) {
      final var rookOpponentOriginalSquare = switch (legalMove.moveSpecification().havingMove()) {
        case BLACK -> WHITE_ROOK_QUEEN_SIDE_CASTLING_FROM;
        case WHITE -> BLACK_ROOK_QUEEN_SIDE_CASTLING_FROM;
        case NONE -> throw new IllegalArgumentException();
      };
      return legalMove.moveSpecification().toSquare() == rookOpponentOriginalSquare;
    }
    return false;
  }

  private static boolean calculateHasCapturedOpponentRookKingSide(LegalMove legalMove) {
    if (legalMove.pieceCaptured() != Piece.NONE && legalMove.pieceCaptured().getPieceType() == ROOK) {
      final var rookOpponentOriginalSquare = switch (legalMove.moveSpecification().havingMove()) {
        case BLACK -> WHITE_ROOK_KING_SIDE_CASTLING_FROM;
        case WHITE -> BLACK_ROOK_KING_SIDE_CASTLING_FROM;
        case NONE -> throw new IllegalArgumentException();
      };
      return legalMove.moveSpecification().toSquare() == rookOpponentOriginalSquare;
    }
    return false;
  }

  private static CastlingRightBoth lookupStaticCastlingRightBoth(Side havingMove,
      CastlingRight newCastlingRightHavingMoveBefore, CastlingRight newCastlingRightHavingMove) {

    final CastlingRight castlingRightWhite;
    final var castlingRightBlack = switch (havingMove) {
      case BLACK -> {
        castlingRightWhite = newCastlingRightHavingMoveBefore;
        yield newCastlingRightHavingMove;
      }
      case WHITE -> {
        castlingRightWhite = newCastlingRightHavingMove;
        yield newCastlingRightHavingMoveBefore;
      }
      case NONE -> throw new IllegalArgumentException();
    };

    switch (castlingRightWhite) {
      case KING_AND_QUEEN_SIDE:
        switch (castlingRightBlack) {
          case KING_AND_QUEEN_SIDE:
            return CastlingConstants.CASTLING_KQ_KQ;
          case KING_SIDE:
            return CastlingConstants.CASTLING_KQ_K;
          case NONE:
            return CastlingConstants.CASTLING_KQ_NONE;
          case QUEEN_SIDE:
            return CastlingConstants.CASTLING_KQ_Q;
          default:
            throw new IllegalArgumentException();
        }
      case KING_SIDE:
        switch (castlingRightBlack) {
          case KING_AND_QUEEN_SIDE:
            return CastlingConstants.CASTLING_K_KQ;
          case KING_SIDE:
            return CastlingConstants.CASTLING_K_K;
          case NONE:
            return CastlingConstants.CASTLING_K_NONE;
          case QUEEN_SIDE:
            return CastlingConstants.CASTLING_K_Q;
          default:
            throw new IllegalArgumentException();
        }
      case NONE:
        switch (castlingRightBlack) {
          case KING_AND_QUEEN_SIDE:
            return CastlingConstants.CASTLING_NONE_KQ;
          case KING_SIDE:
            return CastlingConstants.CASTLING_NONE_K;
          case NONE:
            return CastlingConstants.CASTLING_NONE_NONE;
          case QUEEN_SIDE:
            return CastlingConstants.CASTLING_NONE_Q;
          default:
            throw new IllegalArgumentException();
        }
      case QUEEN_SIDE:
        switch (castlingRightBlack) {
          case KING_AND_QUEEN_SIDE:
            return CastlingConstants.CASTLING_Q_KQ;
          case KING_SIDE:
            return CastlingConstants.CASTLING_Q_K;
          case NONE:
            return CastlingConstants.CASTLING_Q_NONE;
          case QUEEN_SIDE:
            return CastlingConstants.CASTLING_Q_Q;
          default:
            throw new IllegalArgumentException();
        }
      default:
        throw new IllegalArgumentException();
    }
  }

  public static MoveCheck calculateQueenSideCastlingCheck(StaticPosition staticPosition, Side havingMove,
      CastlingRight castlingRight) {

    final var isOriginalPosition = calculateQueenSideCastlingIsOriginalPosition(staticPosition, havingMove);
    if (!isOriginalPosition) {
      return MoveCheck.CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE;
    }

    final var hasLostCastlingRight = castlingRight != CastlingRight.KING_AND_QUEEN_SIDE
        && castlingRight != CastlingRight.QUEEN_SIDE;
    if (hasLostCastlingRight) {
      return MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE;
    }

    final var isEmptySquaresBetweenRookAndKing = calculateQueenSideCastlingIsEmptySquaresBetweenRookAndKing(
        staticPosition, havingMove);
    if (!isEmptySquaresBetweenRookAndKing) {
      return MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY;
    }

    return calculateQueenSideCheckCondition(staticPosition, havingMove);
  }

  public static MoveCheck calculateKingSideCastlingCheck(StaticPosition staticPosition, Side havingMove,
      CastlingRight castlingRight) {

    final var isOriginalPosition = calculateKingSideCastlingIsOriginalPosition(staticPosition, havingMove);
    if (!isOriginalPosition) {
      return MoveCheck.CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE;
    }

    final var hasLostCastlingRight = castlingRight != CastlingRight.KING_AND_QUEEN_SIDE
        && castlingRight != CastlingRight.KING_SIDE;
    if (hasLostCastlingRight) {
      return MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE;
    }

    final var isEmptySquaresBetweenRookAndKing = CastlingUtility
        .calculateKingSideCastlingIsEmptySquaresBetweenRookAndKing(staticPosition, havingMove);
    if (!isEmptySquaresBetweenRookAndKing) {
      return MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY;
    }

    return calculateKingSideCheckCondition(staticPosition, havingMove);
  }

  public static boolean calculateQueenSideCastlingIsOriginalPosition(StaticPosition staticPosition, Side havingMove) {
    final Square kingOriginalSquare = Square.calculateKingOriginalSquare(havingMove);
    final Piece kingPiece = Piece.calculateKingPiece(havingMove);
    if (staticPosition.get(kingOriginalSquare) != kingPiece) {
      return false;
    }
    final Square rookOriginalSquare = Square.calculateQueenSideRookOriginalSquare(havingMove);
    final Piece rookPiece = Piece.calculateRookPiece(havingMove);
    return staticPosition.get(rookOriginalSquare) == rookPiece;
  }

  public static boolean calculateKingSideCastlingIsOriginalPosition(StaticPosition staticPosition, Side havingMove) {
    final Square kingOriginalSquare = Square.calculateKingOriginalSquare(havingMove);
    final Piece kingPiece = Piece.calculateKingPiece(havingMove);
    if (staticPosition.get(kingOriginalSquare) != kingPiece) {
      return false;
    }
    final Square rookOriginalSquare = Square.calculateKingSideRookOriginalSquare(havingMove);
    final Piece rookPiece = Piece.calculateRookPiece(havingMove);
    return staticPosition.get(rookOriginalSquare) == rookPiece;
  }

  public static Square calculateKingCastlingFrom(MoveSpecification moveSpecification) {
    if (!calculateIsCastlingMove(moveSpecification)) {
      throw new IllegalArgumentException();
    }
    switch (moveSpecification.castlingMove()) {
      case KING_SIDE:
        switch (moveSpecification.havingMove()) {
          case BLACK:
            return CastlingUtility.BLACK_KING_FROM;
          case WHITE:
            return CastlingUtility.WHITE_KING_FROM;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      case QUEEN_SIDE:
        switch (moveSpecification.havingMove()) {
          case BLACK:
            return CastlingUtility.BLACK_KING_FROM;
          case WHITE:
            return CastlingUtility.WHITE_KING_FROM;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static Square calculateKingCastlingTo(MoveSpecification moveSpecification) {
    if (!calculateIsCastlingMove(moveSpecification)) {
      throw new IllegalArgumentException();
    }
    switch (moveSpecification.castlingMove()) {
      case KING_SIDE:
        switch (moveSpecification.havingMove()) {
          case BLACK:
            return CastlingUtility.BLACK_KING_KING_SIDE_CASTLING_TO;
          case WHITE:
            return CastlingUtility.WHITE_KING_KING_SIDE_CASTLING_TO;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      case QUEEN_SIDE:
        switch (moveSpecification.havingMove()) {
          case BLACK:
            return CastlingUtility.BLACK_KING_QUEEN_SIDE_CASTLING_TO;
          case WHITE:
            return CastlingUtility.WHITE_KING_QUEEN_SIDE_CASTLING_TO;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static CastlingRight getCastlingRight(CastlingRightBoth castlingRightBoth, Side side) {
    switch (side) {
      case WHITE:
        return castlingRightBoth.castlingRightWhite();
      case BLACK:
        return castlingRightBoth.castlingRightBlack();
      default:
      case NONE:
        throw new IllegalArgumentException();
    }
  }

}
