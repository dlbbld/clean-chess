package com.dlb.chess.unwinnability.findhelpmate.comparator;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.board.enums.SquareType;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.utility.MaterialUtility;
import com.dlb.chess.common.utility.StaticPositionUtility;
import com.dlb.chess.distance.KingDistance;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.unwinnability.findhelpmate.exhaust.classicalcheckmate.ClassicalCheckmate;
import com.dlb.chess.unwinnability.findhelpmate.exhaust.classicalcheckmate.enums.ClassicalCheckmateSituation;

public class ImprovedComparatorClassicalCheckmate extends AbstractLegalMovesComparator {

  public ImprovedComparatorClassicalCheckmate(Side color, Side havingMove, StaticPosition staticPosition) {
    super(color, havingMove, staticPosition);
  }

  @Override
  int compareIntendedWinnerMoving(LegalMove firstLegalMove, LegalMove secondLegalMove) {

    // state 1
    if (ClassicalCheckmate.isClassicalCheckmatePosition(sideIntendedWinner, staticPosition)) {
      throw new ProgrammingMistakeException(
          "Program did not branch earlier as expected for reaching classical checkmate");
    }

    final PieceType firstPieceType = firstLegalMove.movingPiece().getPieceType();
    final PieceType secondPieceType = secondLegalMove.movingPiece().getPieceType();

    final Square firstFromSquare = firstLegalMove.moveSpecification().fromSquare();
    final Square firstToSquare = firstLegalMove.moveSpecification().toSquare();

    final Square secondFromSquare = secondLegalMove.moveSpecification().fromSquare();
    final Square secondToSquare = secondLegalMove.moveSpecification().toSquare();

    final Square opponentKingSquare = StaticPositionUtility.calculateKingSquare(staticPosition,
        sideIntendedWinner.getOppositeSide());

    final ClassicalCheckmateSituation aboveCheckmateMaterial = ClassicalCheckmate
        .calculateAboveClassicalCheckmateMaterial(sideIntendedWinner, staticPosition);

    if (aboveCheckmateMaterial == ClassicalCheckmateSituation.NO_HAVING_PAWN
        || aboveCheckmateMaterial == ClassicalCheckmateSituation.NO_NOT_HAVING_PAWN) {
      // case 3
      // pawn moves
      return comparePawnPushAndHighestPromotion(firstLegalMove, secondLegalMove);
    }

    final Map<SquareType, Set<PieceType>> affordablePieceTypeMap = calculateAffordablePieceTypeMap(sideIntendedWinner,
        staticPosition, aboveCheckmateMaterial);

    // state 2
    if (MaterialUtility.calculateHasKingOnly(sideIntendedWinner.getOppositeSide(), staticPosition)) {
      // state 2a

      if ((NonNullWrapperCommon.get(affordablePieceTypeMap, firstToSquare.getSquareType()).contains(firstPieceType)
          && !NonNullWrapperCommon.get(affordablePieceTypeMap, secondToSquare.getSquareType())
              .contains(secondPieceType))
          && !squaresAttackedByNotHavingMove.contains(firstFromSquare)) {
        if (squaresAttackedByNotHavingMove.contains(firstToSquare)) {
          return -1;
        }
      }

      if ((!NonNullWrapperCommon.get(affordablePieceTypeMap, firstToSquare.getSquareType()).contains(firstPieceType)
          && NonNullWrapperCommon.get(affordablePieceTypeMap, secondToSquare.getSquareType()).contains(secondPieceType))
          && !squaresAttackedByNotHavingMove.contains(secondFromSquare)) {
        if (squaresAttackedByNotHavingMove.contains(secondToSquare)) {
          return 1;
        }
      }

      if (NonNullWrapperCommon.get(affordablePieceTypeMap, firstToSquare.getSquareType()).contains(firstPieceType)
          && NonNullWrapperCommon.get(affordablePieceTypeMap, secondToSquare.getSquareType())
              .contains(secondPieceType)) {
        if (!squaresAttackedByNotHavingMove.contains(firstFromSquare)
            && !squaresAttackedByNotHavingMove.contains(secondFromSquare)) {
          if (squaresAttackedByNotHavingMove.contains(firstToSquare)
              && !squaresAttackedByNotHavingMove.contains(secondToSquare)) {
            return -1;
          }
          if (!squaresAttackedByNotHavingMove.contains(firstToSquare)
              && squaresAttackedByNotHavingMove.contains(secondToSquare)) {
            return 1;
          }
        }

        if ((squaresAttackedByNotHavingMove.contains(firstFromSquare)
            && !squaresAttackedByNotHavingMove.contains(secondFromSquare))
            && squaresAttackedByNotHavingMove.contains(firstToSquare)) {
          return -1;
        }

        if ((!squaresAttackedByNotHavingMove.contains(firstFromSquare)
            && squaresAttackedByNotHavingMove.contains(secondFromSquare))
            && squaresAttackedByNotHavingMove.contains(secondToSquare)) {
          return 1;
        }

      }

      if ((NonNullWrapperCommon.get(affordablePieceTypeMap, firstToSquare.getSquareType()).contains(firstPieceType)
          && !NonNullWrapperCommon.get(affordablePieceTypeMap, secondToSquare.getSquareType())
              .contains(secondPieceType))
          && (KingDistance.distance(firstToSquare, opponentKingSquare) < KingDistance.distance(firstFromSquare,
              opponentKingSquare))) {
        return -1;
      }

      if ((!NonNullWrapperCommon.get(affordablePieceTypeMap, firstToSquare.getSquareType()).contains(firstPieceType)
          && NonNullWrapperCommon.get(affordablePieceTypeMap, secondToSquare.getSquareType()).contains(secondPieceType))
          && (KingDistance.distance(secondToSquare, opponentKingSquare) < KingDistance.distance(secondFromSquare,
              opponentKingSquare))) {
        return 1;
      }

      if (NonNullWrapperCommon.get(affordablePieceTypeMap, firstToSquare.getSquareType()).contains(firstPieceType)
          && NonNullWrapperCommon.get(affordablePieceTypeMap, secondToSquare.getSquareType())
              .contains(secondPieceType)) {

        return Integer.compare(KingDistance.distance(firstToSquare, opponentKingSquare),
            KingDistance.distance(secondToSquare, opponentKingSquare));
      }
      // state 2b
      // capture
    } else if (calculateHasAtLeastRookKnightBishopOrQueen(sideIntendedWinner.getOppositeSide(), staticPosition)) {
      if (!calculateHasMoreThanRookKnightBishopOrQueen(sideIntendedWinner, staticPosition)) {
        return compareSacrificeHavingMove(firstLegalMove, secondLegalMove, sideIntendedWinner, staticPosition,
            aboveCheckmateMaterial);
      }
      if (firstLegalMove.pieceCaptured() != Piece.NONE && secondLegalMove.pieceCaptured() == Piece.NONE) {
        return -1;
      }

      if (firstLegalMove.pieceCaptured() == Piece.NONE && secondLegalMove.pieceCaptured() != Piece.NONE) {
        return 1;
      }
      if (firstLegalMove.pieceCaptured() != Piece.NONE && secondLegalMove.pieceCaptured() != Piece.NONE) {
        return compareLowerValueCapturedPieceFirst(firstLegalMove, secondLegalMove);
      }
    } else {
      // only pawns
      if (firstLegalMove.pieceCaptured() == Piece.NONE && secondLegalMove.pieceCaptured() != Piece.NONE) {
        return -1;
      }

      if (firstLegalMove.pieceCaptured() != Piece.NONE && secondLegalMove.pieceCaptured() == Piece.NONE) {
        return 1;
      }
    }
    return 0;
  }

  @Override
  int compareIntendedLooserMoving(LegalMove firstLegalMove, LegalMove secondLegalMove) {
    // non intended winner having move
    // case 1
    // state 1
    if (ClassicalCheckmate.isClassicalCheckmatePosition(sideIntendedWinner, staticPosition)) {
      // intended winner has checkmate material plus additional material
      // capture additional material
      final var comparePreferNoCapture = comparePreferNoCapture(firstLegalMove, secondLegalMove);
      return comparePreferNoCapture;
    }

    final ClassicalCheckmateSituation aboveCheckmateMaterial = ClassicalCheckmate
        .calculateAboveClassicalCheckmateMaterial(sideIntendedWinner, staticPosition);

    if (aboveCheckmateMaterial == ClassicalCheckmateSituation.NO_HAVING_PAWN
        || aboveCheckmateMaterial == ClassicalCheckmateSituation.NO_HAVING_PAWN) {
      // state 3
      return comparePreferNoCapture(firstLegalMove, secondLegalMove);
    }

    final Map<SquareType, Set<PieceType>> affordablePieceTypeMap = calculateAffordablePieceTypeMap(sideIntendedWinner,
        staticPosition, aboveCheckmateMaterial);

    // state 2
    final var isLosingHasKingOnly = MaterialUtility.calculateHasKingOnly(sideIntendedWinner.getOppositeSide(),
        staticPosition);

    // state 2a
    if (isLosingHasKingOnly) {

      return compareCaptureAffordablePiece(firstLegalMove, secondLegalMove, affordablePieceTypeMap);

      // state 2b
    }
    if (!calculateHasAtLeastRookKnightBishopOrQueen(sideIntendedWinner, staticPosition)) {
      return comparePawnPushAndHighestPromotion(firstLegalMove, secondLegalMove);
    }
    if (calculateHasMoreThanRookKnightBishopOrQueen(sideIntendedWinner, staticPosition)) {
      return compareSacrificeNotHavingMove(firstLegalMove, secondLegalMove, false);
    }
    return compareCaptureAffordablePiece(firstLegalMove, secondLegalMove, affordablePieceTypeMap);

  }

  private int comparePieceSacrifice(LegalMove firstLegalMove, LegalMove secondLegalMove,
      final Set<PieceType> affordablePieceTypeSet) {
    final PieceType firstMovingPieceType = firstLegalMove.movingPiece().getPieceType();
    final PieceType secondMovingPieceType = secondLegalMove.movingPiece().getPieceType();

    final var isFirstCanMoveToAttackedSquareSecondNot = emptySquaresAttackedByNotHavingMove
        .contains(firstLegalMove.moveSpecification().toSquare())
        && !emptySquaresAttackedByNotHavingMove.contains(secondLegalMove.moveSpecification().toSquare());

    final var isFirstCannotMoveToAttackedSquareButSecond = !emptySquaresAttackedByNotHavingMove
        .contains(firstLegalMove.moveSpecification().toSquare())
        && emptySquaresAttackedByNotHavingMove.contains(secondLegalMove.moveSpecification().toSquare());

    final var isBothCanMoveToAttackedSquare = emptySquaresAttackedByNotHavingMove
        .contains(firstLegalMove.moveSpecification().toSquare())
        && emptySquaresAttackedByNotHavingMove.contains(secondLegalMove.moveSpecification().toSquare());

    if (isFirstCanMoveToAttackedSquareSecondNot && affordablePieceTypeSet.contains(firstMovingPieceType)) {
      return -1;
    }

    if (isFirstCannotMoveToAttackedSquareButSecond && affordablePieceTypeSet.contains(secondMovingPieceType)) {
      return 1;
    }

    if (isBothCanMoveToAttackedSquare) {
      if (affordablePieceTypeSet.contains(firstMovingPieceType)
          && !affordablePieceTypeSet.contains(secondMovingPieceType)) {
        return -1;
      }
      if (!affordablePieceTypeSet.contains(firstMovingPieceType)
          && affordablePieceTypeSet.contains(secondMovingPieceType)) {
        return 1;
      }
      if (affordablePieceTypeSet.contains(firstMovingPieceType)
          && affordablePieceTypeSet.contains(secondMovingPieceType)) {
        return compareHigherValuePieceFirst(firstMovingPieceType, secondMovingPieceType);

      }
    }
    return 0;
  }

  private int comparePieceSacrifice(LegalMove firstLegalMove, LegalMove secondLegalMove,
      final Map<SquareType, Set<PieceType>> affordablePieceTypeMap) {
    final PieceType firstMovingPieceType = firstLegalMove.movingPiece().getPieceType();
    final PieceType secondMovingPieceType = secondLegalMove.movingPiece().getPieceType();

    final SquareType firstFromSquareSquareType = firstLegalMove.moveSpecification().fromSquare().getSquareType();
    final SquareType secondFromSquareSquareType = secondLegalMove.moveSpecification().fromSquare().getSquareType();

    final var isFirstCanMoveToAttackedSquareSecondNot = emptySquaresAttackedByNotHavingMove
        .contains(firstLegalMove.moveSpecification().toSquare())
        && !emptySquaresAttackedByNotHavingMove.contains(secondLegalMove.moveSpecification().toSquare());

    final var isFirstCannotMoveToAttackedSquareButSecond = !emptySquaresAttackedByNotHavingMove
        .contains(firstLegalMove.moveSpecification().toSquare())
        && emptySquaresAttackedByNotHavingMove.contains(secondLegalMove.moveSpecification().toSquare());

    final var isBothCanMoveToAttackedSquare = emptySquaresAttackedByNotHavingMove
        .contains(firstLegalMove.moveSpecification().toSquare())
        && emptySquaresAttackedByNotHavingMove.contains(secondLegalMove.moveSpecification().toSquare());

    if (isFirstCanMoveToAttackedSquareSecondNot
        && NonNullWrapperCommon.get(affordablePieceTypeMap, firstFromSquareSquareType).contains(firstMovingPieceType)) {
      return -1;
    }

    if (isFirstCannotMoveToAttackedSquareButSecond && NonNullWrapperCommon
        .get(affordablePieceTypeMap, secondFromSquareSquareType).contains(secondMovingPieceType)) {
      return 1;
    }

    if (isBothCanMoveToAttackedSquare) {
      if (NonNullWrapperCommon.get(affordablePieceTypeMap, firstFromSquareSquareType).contains(firstMovingPieceType)
          && !NonNullWrapperCommon.get(affordablePieceTypeMap, secondFromSquareSquareType)
              .contains(secondMovingPieceType)) {
        return -1;
      }
      if (!NonNullWrapperCommon.get(affordablePieceTypeMap, firstFromSquareSquareType).contains(firstMovingPieceType)
          && NonNullWrapperCommon.get(affordablePieceTypeMap, secondFromSquareSquareType)
              .contains(secondMovingPieceType)) {
        return 1;
      }
      if (NonNullWrapperCommon.get(affordablePieceTypeMap, firstFromSquareSquareType).contains(firstMovingPieceType)
          && NonNullWrapperCommon.get(affordablePieceTypeMap, secondFromSquareSquareType)
              .contains(secondMovingPieceType)) {
        return compareHigherValuePieceFirst(firstMovingPieceType, secondMovingPieceType);
      }
    }
    return 0;
  }

  private static int compareLowerValueCapturedPieceFirst(LegalMove firstLegalMove, LegalMove secondLegalMove) {
    return compareLowerValuePieceFirst(firstLegalMove.pieceCaptured(), secondLegalMove.pieceCaptured());
  }

  private static int compareLowerValuePieceFirst(Piece firstPiece, Piece secondPiece) {
    return compareLowerValuePieceFirst(firstPiece.getPieceType(), secondPiece.getPieceType());
  }

  private static int compareLowerValuePieceFirst(PieceType firstPieceType, PieceType secondPieceType) {
    return Integer.compare(firstPieceType.getValue(), secondPieceType.getValue());
  }

  private static int compareHigherValuePieceFirst(Piece firstPiece, Piece secondPiece) {
    return compareHigherValuePieceFirst(firstPiece.getPieceType(), secondPiece.getPieceType());
  }

  private static int compareHigherValuePieceFirst(PieceType firstPieceType, PieceType secondPieceType) {
    return Integer.compare(-firstPieceType.getValue(), -secondPieceType.getValue());
  }

  private static Map<SquareType, Set<PieceType>> calculateAffordablePieceTypeMap(Side sideCheckmating,
      StaticPosition staticPosition, ClassicalCheckmateSituation aboveCheckmateMaterial) {

    return switch (aboveCheckmateMaterial) {
      case ABOVE_KING_AND_QUEEN, ABOVE_KING_AND_ROOK, ABOVE_KING_AND_KNIGHT_AND_BISHOP -> {
        final Set<PieceType> affordablePieceTypeSet = calculateAffordablePieceTypeSet(sideCheckmating, staticPosition,
            aboveCheckmateMaterial);
        final Map<SquareType, Set<PieceType>> map = new TreeMap<>();
        map.put(SquareType.LIGHT_SQUARE, affordablePieceTypeSet);
        map.put(SquareType.DARK_SQUARE, affordablePieceTypeSet);
        yield map;
      }
      case ABOVE_KING_AND_OPPOSITE_SQUARES_BISHOP -> calculateAffordablePieceTypeMapOppositeBishops(sideCheckmating,
          staticPosition);
      case NO_HAVING_PAWN, NO_NOT_HAVING_PAWN -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };

  }

  private static Set<PieceType> calculateAffordablePieceTypeSet(Side sideCheckmating, StaticPosition staticPosition,
      ClassicalCheckmateSituation aboveCheckmateMaterial) {
    final Set<PieceType> set = new TreeSet<>();

    set.add(PieceType.PAWN);

    switch (aboveCheckmateMaterial) {
      case ABOVE_KING_AND_QUEEN:
        set.add(PieceType.ROOK);
        set.add(PieceType.KNIGHT);
        set.add(PieceType.BISHOP);
        if (MaterialUtility.calculateNumberOfQueens(sideCheckmating, staticPosition) >= 2) {
          set.add(PieceType.QUEEN);
        }
        break;
      case ABOVE_KING_AND_ROOK:
        if (MaterialUtility.calculateNumberOfRooks(sideCheckmating, staticPosition) >= 2) {
          set.add(PieceType.ROOK);
        }
        set.add(PieceType.KNIGHT);
        set.add(PieceType.BISHOP);
        set.add(PieceType.QUEEN);
        break;
      case ABOVE_KING_AND_KNIGHT_AND_BISHOP:
        set.add(PieceType.ROOK);
        if (MaterialUtility.calculateNumberOfKnights(sideCheckmating, staticPosition) >= 2) {
          set.add(PieceType.KNIGHT);
        }
        if (MaterialUtility.calculateNumberOfBishops(sideCheckmating, staticPosition) >= 2) {
          set.add(PieceType.BISHOP);
        }
        set.add(PieceType.QUEEN);
        break;
      case ABOVE_KING_AND_OPPOSITE_SQUARES_BISHOP:
      case NO_HAVING_PAWN:
      case NO_NOT_HAVING_PAWN:
      default:
        throw new IllegalArgumentException();
    }
    return set;
  }

  private static Map<SquareType, Set<PieceType>> calculateAffordablePieceTypeMapOppositeBishops(Side sideCheckmating,
      StaticPosition staticPosition) {

    final Set<PieceType> affordablePieceTypeSetLightSquare = new TreeSet<>();
    affordablePieceTypeSetLightSquare.add(PieceType.ROOK);
    affordablePieceTypeSetLightSquare.add(PieceType.KNIGHT);
    if (MaterialUtility.calculateNumberOfBishops(sideCheckmating, staticPosition, SquareType.LIGHT_SQUARE) >= 2) {
      affordablePieceTypeSetLightSquare.add(PieceType.BISHOP);
    }
    affordablePieceTypeSetLightSquare.add(PieceType.QUEEN);
    affordablePieceTypeSetLightSquare.add(PieceType.PAWN);

    final Set<PieceType> affordablePieceTypeSetDarkSquare = new TreeSet<>();
    affordablePieceTypeSetDarkSquare.add(PieceType.ROOK);
    affordablePieceTypeSetDarkSquare.add(PieceType.KNIGHT);
    if (MaterialUtility.calculateNumberOfBishops(sideCheckmating, staticPosition, SquareType.DARK_SQUARE) >= 2) {
      affordablePieceTypeSetDarkSquare.add(PieceType.BISHOP);
    }
    affordablePieceTypeSetDarkSquare.add(PieceType.QUEEN);
    affordablePieceTypeSetDarkSquare.add(PieceType.PAWN);

    final Map<SquareType, Set<PieceType>> map = new TreeMap<>();
    map.put(SquareType.LIGHT_SQUARE, affordablePieceTypeSetLightSquare);
    map.put(SquareType.DARK_SQUARE, affordablePieceTypeSetDarkSquare);

    return map;

  }

  private int compareSacrificeHavingMove(LegalMove firstLegalMove, LegalMove secondLegalMove, Side color,
      StaticPosition staticPosition, ClassicalCheckmateSituation aboveCheckmateMaterial) {
    return switch (aboveCheckmateMaterial) {
      case ABOVE_KING_AND_QUEEN, ABOVE_KING_AND_ROOK, ABOVE_KING_AND_KNIGHT_AND_BISHOP -> {
        final Set<PieceType> affordablePieceTypeSet = calculateAffordablePieceTypeSet(color, staticPosition,
            aboveCheckmateMaterial);
        final var comparePieceSacrifice = comparePieceSacrifice(firstLegalMove, secondLegalMove,
            affordablePieceTypeSet);
        yield comparePieceSacrifice;
      }
      case ABOVE_KING_AND_OPPOSITE_SQUARES_BISHOP -> {
        final Map<SquareType, Set<PieceType>> affordablePieceTypeMap = calculateAffordablePieceTypeMapOppositeBishops(
            color, staticPosition);
        yield comparePieceSacrifice(firstLegalMove, secondLegalMove, affordablePieceTypeMap);
      }
      case NO_HAVING_PAWN, NO_NOT_HAVING_PAWN -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  private int compareSacrificeNotHavingMove(LegalMove firstLegalMove, LegalMove secondLegalMove,
      boolean isLoseHigherPieces) {
    if (emptySquaresAttackedByNotHavingMove.contains(firstLegalMove.moveSpecification().toSquare())
        && !emptySquaresAttackedByNotHavingMove.contains(secondLegalMove.moveSpecification().toSquare())) {
      return -1;
    }
    if (!emptySquaresAttackedByNotHavingMove.contains(firstLegalMove.moveSpecification().toSquare())
        && emptySquaresAttackedByNotHavingMove.contains(secondLegalMove.moveSpecification().toSquare())) {
      return 1;
    }
    if (emptySquaresAttackedByNotHavingMove.contains(firstLegalMove.moveSpecification().toSquare())
        && emptySquaresAttackedByNotHavingMove.contains(secondLegalMove.moveSpecification().toSquare())) {
      if (isLoseHigherPieces) {
        return compareHigherValuePieceFirst(firstLegalMove.movingPiece(), secondLegalMove.movingPiece());
      }
      return compareLowerValuePieceFirst(firstLegalMove.movingPiece(), secondLegalMove.movingPiece());
    }
    return 0;
  }

  private static int comparePreferNoCapture(LegalMove firstLegalMove, LegalMove secondLegalMove) {
    if (firstLegalMove.pieceCaptured() == Piece.NONE && secondLegalMove.pieceCaptured() != Piece.NONE) {
      return -1;
    }
    if (firstLegalMove.pieceCaptured() != Piece.NONE && secondLegalMove.pieceCaptured() == Piece.NONE) {
      return 1;
    }
    return 0;
  }

  private static boolean calculateHasAtLeastRookKnightBishopOrQueen(Side side, StaticPosition staticPosition) {
    return MaterialUtility.calculateHasRook(side, staticPosition)
        || MaterialUtility.calculateHasKnight(side, staticPosition)
        || MaterialUtility.calculateHasBishop(side, staticPosition)
        || MaterialUtility.calculateHasQueen(side, staticPosition);
  }

  private static boolean calculateHasMoreThanRookKnightBishopOrQueen(Side side, StaticPosition staticPosition) {
    return MaterialUtility.calculateNumberOfRooks(side, staticPosition) >= 2
        || MaterialUtility.calculateNumberOfKnights(side, staticPosition) >= 2
        || MaterialUtility.calculateNumberOfBishops(side, staticPosition) >= 2
        || MaterialUtility.calculateNumberOfQueens(side, staticPosition) >= 2
        || MaterialUtility.calculateHasPawn(side, staticPosition);
  }

  private static int comparePawnPushAndHighestPromotion(LegalMove firstLegalMove, LegalMove secondLegalMove) {
    final PieceType firstPieceType = firstLegalMove.movingPiece().getPieceType();
    final PieceType secondPieceType = secondLegalMove.movingPiece().getPieceType();

    if (firstPieceType == PieceType.PAWN && secondPieceType != PieceType.PAWN) {
      return -1;
    }

    if (firstPieceType != PieceType.PAWN && secondPieceType == PieceType.PAWN) {
      return 1;
    }

    // promotion preferred
    if (firstPieceType == PieceType.PAWN && secondPieceType == PieceType.PAWN) {

      final PromotionPieceType firstPromotionPieceType = firstLegalMove.moveSpecification().promotionPieceType();
      final PromotionPieceType secondPromotionPieceType = secondLegalMove.moveSpecification().promotionPieceType();

      if (firstPromotionPieceType != PromotionPieceType.NONE && secondPromotionPieceType == PromotionPieceType.NONE) {
        return -1;
      }
      if (firstPromotionPieceType == PromotionPieceType.NONE && secondPromotionPieceType != PromotionPieceType.NONE) {
        return 1;
      }
      // higher promotion preferred
      if (firstPromotionPieceType != PromotionPieceType.NONE && secondPromotionPieceType != PromotionPieceType.NONE) {

        return compareHigherValuePieceFirst(firstPromotionPieceType.getPieceType(),
            secondPromotionPieceType.getPieceType());
      }
    }
    return 0;
  }

  private static int compareCaptureAffordablePiece(LegalMove firstLegalMove, LegalMove secondLegalMove,
      final Map<SquareType, Set<PieceType>> affordablePieceTypeMap) {

    final var isFirstCaptureAffordablePiece = firstLegalMove.pieceCaptured() != Piece.NONE && NonNullWrapperCommon
        .get(affordablePieceTypeMap, firstLegalMove.moveSpecification().toSquare().getSquareType())
        .contains(firstLegalMove.pieceCaptured().getPieceType());
    final var isSecondCaptureAffordablePiece = secondLegalMove.pieceCaptured() != Piece.NONE && NonNullWrapperCommon
        .get(affordablePieceTypeMap, secondLegalMove.moveSpecification().toSquare().getSquareType())
        .contains(secondLegalMove.pieceCaptured().getPieceType());

    if (isFirstCaptureAffordablePiece && !isSecondCaptureAffordablePiece) {
      return -1;
    }

    if (!isFirstCaptureAffordablePiece && isSecondCaptureAffordablePiece) {
      return 1;
    }

    if (isFirstCaptureAffordablePiece && isSecondCaptureAffordablePiece) {
      return -compareLowerValueCapturedPieceFirst(firstLegalMove, secondLegalMove);
    }
    return 0;
  }
}
