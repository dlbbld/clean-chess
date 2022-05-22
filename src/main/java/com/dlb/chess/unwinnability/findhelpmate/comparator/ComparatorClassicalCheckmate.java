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
import com.dlb.chess.board.enums.SquareType;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.utility.MaterialUtility;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.unwinnability.findhelpmate.exhaust.classicalcheckmate.ClassicalCheckmate;
import com.dlb.chess.unwinnability.findhelpmate.exhaust.classicalcheckmate.enums.ClassicalCheckmateSituation;

// strategy to get to classical checkmate position
// possible states:
// state 1
// winning side has classical checkmate, losing side has only king
// winning side: exception, should not come here
// loosing side: make non capturing move
// state 2
// winning side material above classical checkmate
// state 2a: losing side has only king
// winning side: offers non-threatened affordable pieces, moves affordable pieces [so other can be captu
// loosing side: captures affordable pieces, move to affordable pieces (distinguish king and non-king)
// state 2b: losing side has more than king
// winning side: captures (affordable pieces, non-affordable pieces), offers affordable pieces
// loosing side: sacrifice, captures affordable pieces
// state 3
// winning side has less material than classical checkmate
// winning side: pawn promotion, pawn push
// loosing side: not capturing

public class ComparatorClassicalCheckmate extends AbstractLegalMovesComparator {

  public ComparatorClassicalCheckmate(Side color, Side havingMove, StaticPosition staticPosition) {
    super(color, havingMove, staticPosition);
  }

  @Override
  int compareIntendedWinnerMoving(LegalMove firstLegalMove, LegalMove secondLegalMove) {
    // state 1
    if (ClassicalCheckmate.isClassicalCheckmatePosition(sideIntendedWinner, staticPosition)) {
      throw new ProgrammingMistakeException(
          "Program did not branch earlier as expected for reaching classical checkmate");
    }

    final ClassicalCheckmateSituation aboveCheckmateMaterial = ClassicalCheckmate
        .calculateAboveClassicalCheckmateMaterial(sideIntendedWinner, staticPosition);

    if (aboveCheckmateMaterial != ClassicalCheckmateSituation.NO_HAVING_PAWN
        && aboveCheckmateMaterial != ClassicalCheckmateSituation.NO_NOT_HAVING_PAWN) {
      // state 2
      if (MaterialUtility.calculateHasKingOnly(sideIntendedWinner.getOppositeSide(), staticPosition)) {
        // state 2a

        // pawn moves
        if (firstLegalMove.movingPiece().getPieceType() == PieceType.PAWN
            && secondLegalMove.movingPiece().getPieceType() != PieceType.PAWN) {
          return -1;
        }
        if (firstLegalMove.movingPiece().getPieceType() != PieceType.PAWN
            && secondLegalMove.movingPiece().getPieceType() == PieceType.PAWN) {
          return 1;
        }
        // promotion preferred
        if (firstLegalMove.movingPiece().getPieceType() == PieceType.PAWN
            && secondLegalMove.movingPiece().getPieceType() == PieceType.PAWN) {
          if (firstLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE
              && secondLegalMove.moveSpecification().promotionPieceType() == PromotionPieceType.NONE) {
            return -1;
          }
          if (firstLegalMove.moveSpecification().promotionPieceType() == PromotionPieceType.NONE
              && secondLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE) {
            return 1;
          }
          // higher promotion preferred
          if (firstLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE
              && secondLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE) {
            final PromotionPieceType firstPromotionPieceType = firstLegalMove.moveSpecification().promotionPieceType();
            final PromotionPieceType secondPromotionPieceType = secondLegalMove.moveSpecification()
                .promotionPieceType();

            return compareHigherValuePieceFirst(firstPromotionPieceType.getPieceType(),
                secondPromotionPieceType.getPieceType());
          }
        }

        final var compareSacrificeHavingMove = compareSacrificeHavingMove(firstLegalMove, secondLegalMove, sideIntendedWinner,
            staticPosition, aboveCheckmateMaterial);
        if (compareSacrificeHavingMove != 0) {
          return compareSacrificeHavingMove;
        }

      } else {
        // state 2b
        // capture

        // captures - preferred - and if both are capturing, capturing a higher value piece preferred
        if (firstLegalMove.pieceCaptured() != Piece.NONE && secondLegalMove.pieceCaptured() == Piece.NONE) {
          return -1;
        }
        if (firstLegalMove.pieceCaptured() == Piece.NONE && secondLegalMove.pieceCaptured() != Piece.NONE) {
          return 1;
        }
        if (firstLegalMove.pieceCaptured() != Piece.NONE && secondLegalMove.pieceCaptured() != Piece.NONE) {
          final PieceType firstPieceTypeCapture = firstLegalMove.pieceCaptured().getPieceType();
          final PieceType secondPieceTypeCapture = secondLegalMove.pieceCaptured().getPieceType();

          // capturing lower value pieces preferred
          // for remaining pieces must capture own pieces
          final var comparePieceCapturedValue = compareLowerValuePieceFirst(firstPieceTypeCapture,
              secondPieceTypeCapture);
          if (comparePieceCapturedValue != 0) {
            return comparePieceCapturedValue;
          }

          // capturing with higher valued pieces preferred
          return compareHigherValuePieceFirst(firstLegalMove.movingPiece(), secondLegalMove.movingPiece());
        }

        final var compareSacrificeHavingMove = compareSacrificeHavingMove(firstLegalMove, secondLegalMove, sideIntendedWinner,
            staticPosition, aboveCheckmateMaterial);
        if (compareSacrificeHavingMove != 0) {
          return compareSacrificeHavingMove;
        }
      }
    } else {
      // case 3
      // pawn moves
      if (firstLegalMove.movingPiece().getPieceType() == PieceType.PAWN
          && secondLegalMove.movingPiece().getPieceType() != PieceType.PAWN) {
        return -1;
      }
      if (firstLegalMove.movingPiece().getPieceType() != PieceType.PAWN
          && secondLegalMove.movingPiece().getPieceType() == PieceType.PAWN) {
        return 1;
      }
      // promotion preferred
      if (firstLegalMove.movingPiece().getPieceType() == PieceType.PAWN
          && secondLegalMove.movingPiece().getPieceType() == PieceType.PAWN) {
        if (firstLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE
            && secondLegalMove.moveSpecification().promotionPieceType() == PromotionPieceType.NONE) {
          return -1;
        }
        if (firstLegalMove.moveSpecification().promotionPieceType() == PromotionPieceType.NONE
            && secondLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE) {
          return 1;
        }
        // higher promotion preferred
        if (firstLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE
            && secondLegalMove.moveSpecification().promotionPieceType() != PromotionPieceType.NONE) {
          final PromotionPieceType firstPromotionPieceType = firstLegalMove.moveSpecification().promotionPieceType();
          final PromotionPieceType secondPromotionPieceType = secondLegalMove.moveSpecification().promotionPieceType();

          return compareHigherValuePieceFirst(firstPromotionPieceType.getPieceType(),
              secondPromotionPieceType.getPieceType());
        }
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
      if (comparePreferNoCapture != 0) {
        return comparePreferNoCapture;
      }
    } else {

      final ClassicalCheckmateSituation aboveCheckmateMaterial = ClassicalCheckmate
          .calculateAboveClassicalCheckmateMaterial(sideIntendedWinner, staticPosition);

      if (aboveCheckmateMaterial == ClassicalCheckmateSituation.NO_HAVING_PAWN
          || aboveCheckmateMaterial == ClassicalCheckmateSituation.NO_HAVING_PAWN) {
        // state 3
        return comparePreferNoCapture(firstLegalMove, secondLegalMove);
      }

      // state 2
      final var isLosingHasKingOnly = MaterialUtility.calculateHasKingOnly(sideIntendedWinner.getOppositeSide(), staticPosition);
      // state 2a/2b

      if (isLosingHasKingOnly) {
        if (firstLegalMove.pieceCaptured() != Piece.NONE && secondLegalMove.pieceCaptured() == Piece.NONE) {
          switch (aboveCheckmateMaterial) {
            case ABOVE_KING_AND_QUEEN:
            case ABOVE_KING_AND_ROOK:
            case ABOVE_KING_AND_KNIGHT_AND_BISHOP:
              final Set<PieceType> capturablePieceTypeSet = calculateAffordablePieceTypeSet(sideIntendedWinner.getOppositeSide(),
                  staticPosition, aboveCheckmateMaterial);
              if (capturablePieceTypeSet.contains(firstLegalMove.pieceCaptured().getPieceType())) {
                return -1;
              }
              break;
            case ABOVE_KING_AND_OPPOSITE_SQUARES_BISHOP:
              final Map<SquareType, Set<PieceType>> map = calculateOppositeBishopsAffordablePieceTypeMap(sideIntendedWinner,
                  staticPosition);
              if (NonNullWrapperCommon.get(map, firstLegalMove.moveSpecification().toSquare().getSquareType())
                  .contains(firstLegalMove.pieceCaptured().getPieceType())) {
                return -1;
              }
              break;
            case NO_HAVING_PAWN:
            case NO_NOT_HAVING_PAWN:
            default:
              throw new IllegalArgumentException();
          }
        }

        if (firstLegalMove.pieceCaptured() == Piece.NONE && secondLegalMove.pieceCaptured() != Piece.NONE) {
          switch (aboveCheckmateMaterial) {
            case ABOVE_KING_AND_QUEEN:
            case ABOVE_KING_AND_ROOK:
            case ABOVE_KING_AND_KNIGHT_AND_BISHOP:
              final Set<PieceType> capturablePieceTypeSet = calculateAffordablePieceTypeSet(sideIntendedWinner.getOppositeSide(),
                  staticPosition, aboveCheckmateMaterial);
              if (capturablePieceTypeSet.contains(secondLegalMove.pieceCaptured().getPieceType())) {
                return 1;
              }
              break;
            case ABOVE_KING_AND_OPPOSITE_SQUARES_BISHOP:
              final Map<SquareType, Set<PieceType>> map = calculateOppositeBishopsAffordablePieceTypeMap(sideIntendedWinner,
                  staticPosition);
              if (NonNullWrapperCommon.get(map, secondLegalMove.moveSpecification().toSquare().getSquareType())
                  .contains(secondLegalMove.pieceCaptured().getPieceType())) {
                return 1;
              }
              break;
            case NO_HAVING_PAWN:
            case NO_NOT_HAVING_PAWN:
            default:
              throw new IllegalArgumentException();
          }
        }

        if (firstLegalMove.pieceCaptured() != Piece.NONE && secondLegalMove.pieceCaptured() != Piece.NONE) {
          switch (aboveCheckmateMaterial) {
            case ABOVE_KING_AND_QUEEN:
            case ABOVE_KING_AND_ROOK:
            case ABOVE_KING_AND_KNIGHT_AND_BISHOP:
              final Set<PieceType> capturablePieceTypeSet = calculateAffordablePieceTypeSet(sideIntendedWinner.getOppositeSide(),
                  staticPosition, aboveCheckmateMaterial);
              if (capturablePieceTypeSet.contains(firstLegalMove.pieceCaptured().getPieceType())
                  && !capturablePieceTypeSet.contains(secondLegalMove.pieceCaptured().getPieceType())) {
                return -1;
              }
              if (!capturablePieceTypeSet.contains(firstLegalMove.pieceCaptured().getPieceType())
                  && capturablePieceTypeSet.contains(secondLegalMove.pieceCaptured().getPieceType())) {
                return 1;
              }
              if (capturablePieceTypeSet.contains(firstLegalMove.pieceCaptured().getPieceType())
                  && capturablePieceTypeSet.contains(secondLegalMove.pieceCaptured().getPieceType())) {
                if (isLosingHasKingOnly) {
                  // capture higher value pieces first so king has more space to move and capture
                  return compareHigherValuePieceFirst(firstLegalMove.pieceCaptured(), secondLegalMove.pieceCaptured());
                }
                // leave the opponent strong pieces to capture
                return compareLowerValuePieceFirst(firstLegalMove.pieceCaptured(), secondLegalMove.pieceCaptured());
              }

              break;
            case ABOVE_KING_AND_OPPOSITE_SQUARES_BISHOP:
              final Map<SquareType, Set<PieceType>> map = calculateOppositeBishopsAffordablePieceTypeMap(sideIntendedWinner,
                  staticPosition);
              if (NonNullWrapperCommon.get(map, firstLegalMove.moveSpecification().toSquare().getSquareType())
                  .contains(firstLegalMove.pieceCaptured().getPieceType())
                  && !NonNullWrapperCommon.get(map, secondLegalMove.moveSpecification().toSquare().getSquareType())
                      .contains(secondLegalMove.pieceCaptured().getPieceType())) {
                return -1;
              }
              if (!NonNullWrapperCommon.get(map, firstLegalMove.moveSpecification().toSquare().getSquareType())
                  .contains(firstLegalMove.pieceCaptured().getPieceType())
                  && NonNullWrapperCommon.get(map, secondLegalMove.moveSpecification().toSquare().getSquareType())
                      .contains(secondLegalMove.pieceCaptured().getPieceType())) {
                return 1;
              }
              if (NonNullWrapperCommon.get(map, firstLegalMove.moveSpecification().toSquare().getSquareType())
                  .contains(firstLegalMove.pieceCaptured().getPieceType())
                  && NonNullWrapperCommon.get(map, secondLegalMove.moveSpecification().toSquare().getSquareType())
                      .contains(secondLegalMove.pieceCaptured().getPieceType())) {
                if (isLosingHasKingOnly) {
                  // capture higher value pieces first so king has more space to move and capture
                  return compareHigherValuePieceFirst(firstLegalMove.pieceCaptured(), secondLegalMove.pieceCaptured());
                }
                // leave the opponent strong pieces to capture
                return compareLowerValuePieceFirst(firstLegalMove.pieceCaptured(), secondLegalMove.pieceCaptured());
              }
              break;
            case NO_HAVING_PAWN:
            case NO_NOT_HAVING_PAWN:
            default:
              throw new IllegalArgumentException();
          }
        }
      } else {
        final var compareSacrifice = compareSacrificeNotHavingMove(firstLegalMove, secondLegalMove, false);
        if (compareSacrifice != 0) {
          return compareSacrifice;
        }
      }
    }

    return 0;

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
    return NO_ORDER;
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
    return NO_ORDER;
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
      case ABOVE_KING_AND_OPPOSITE_SQUARES_BISHOP:
        throw new IllegalArgumentException("Not designed for opposite square bishops");
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
      case NO_HAVING_PAWN:
      case NO_NOT_HAVING_PAWN:
      default:
        throw new IllegalArgumentException();
    }
    return set;
  }

  private static Map<SquareType, Set<PieceType>> calculateOppositeBishopsAffordablePieceTypeMap(Side sideCheckmating,
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
    switch (aboveCheckmateMaterial) {
      case ABOVE_KING_AND_QUEEN:
      case ABOVE_KING_AND_ROOK:
      case ABOVE_KING_AND_KNIGHT_AND_BISHOP: {
        final Set<PieceType> affordablePieceTypeSet = calculateAffordablePieceTypeSet(color, staticPosition,
            aboveCheckmateMaterial);

        final var comparePieceSacrifice = comparePieceSacrifice(firstLegalMove, secondLegalMove,
            affordablePieceTypeSet);
        if (comparePieceSacrifice != NO_ORDER) {
          return comparePieceSacrifice;
        }
      }
        break;
      case ABOVE_KING_AND_OPPOSITE_SQUARES_BISHOP: {
        final Map<SquareType, Set<PieceType>> affordablePieceTypeMap = calculateOppositeBishopsAffordablePieceTypeMap(
            color, staticPosition);

        return comparePieceSacrifice(firstLegalMove, secondLegalMove, affordablePieceTypeMap);
      }
      case NO_HAVING_PAWN:
      case NO_NOT_HAVING_PAWN:
      default:
        throw new IllegalArgumentException();
    }
    return 0;
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
}
