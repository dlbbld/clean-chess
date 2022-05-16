package com.dlb.chess.unwinnability.findhelpmate.exhaust.classicalcheckmate;

import java.util.Comparator;
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
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.unwinnability.findhelpmate.exhaust.classicalcheckmate.enums.ClassicalCheckmateType;

public class ComparatorClassicalCheckmate implements Comparator<LegalMove> {

  private final Side color;
  private final Side havingMove;
  private final StaticPosition staticPosition;
  private final Set<Square> squaresAttackedByNotHavingMove;

  public ComparatorClassicalCheckmate(Side color, Side havingMove, StaticPosition staticPosition,
      Set<Square> squaresAttackedByNotHavingMove) {
    this.color = color;
    this.havingMove = havingMove;
    this.staticPosition = staticPosition;
    this.squaresAttackedByNotHavingMove = squaresAttackedByNotHavingMove;
  }

  @Override
  public int compare(LegalMove firstLegalMove, LegalMove secondLegalMove) {

    // castling moves have least precedence
    if (!CastlingUtility.calculateIsCastlingMove(firstLegalMove.moveSpecification())
        && CastlingUtility.calculateIsCastlingMove(secondLegalMove.moveSpecification())) {
      return -1;
    }
    if (CastlingUtility.calculateIsCastlingMove(firstLegalMove.moveSpecification())
        && !CastlingUtility.calculateIsCastlingMove(secondLegalMove.moveSpecification())) {
      return 1;
    }
    if (CastlingUtility.calculateIsCastlingMove(firstLegalMove.moveSpecification())
        && CastlingUtility.calculateIsCastlingMove(secondLegalMove.moveSpecification())) {
      return 0;
    }

    // in the following we have no castling move which is essential because the moving piece type is not set for the
    // castling move by design
    // using it would cause exceptions

    // strategy easy mate
    // possible states:
    // state 1
    // winning side has classical checkmate, losing side has only king
    // winning side: exception, should not come here
    // loosing side: make non capturing move
    // state 2
    // winning side has more material than classical checkmate
    // state 2a: losing side has only king
    // winning side: offers affordable pieces for capture
    // loosing side: prefers capture moves if affordable pieces
    // state 2b: losing side has more than king
    // winning side: captures
    // loosing side: offers pieces for capture
    // state 3
    // winning side has less material than classical checkmate
    // winning side: priorities pawn promotion queen, pawn promotion other, pawn push
    // loosing side: does not capture

    // intended winner is moving
    if (color == havingMove) {

      // state 1
      if (ClassicalCheckmate.isClassicalCheckmateMaterial(color, staticPosition)) {
        throw new ProgrammingMistakeException(
            "Program did not branch earlier as expected for reaching classical checkmate");
      }

      final ClassicalCheckmateType checkmateMaterialMatingSide = ClassicalCheckmate
          .calculateClassicalCheckmateMaterialMatingSide(color, staticPosition);

      if (checkmateMaterialMatingSide != ClassicalCheckmateType.NONE) {
        // state 2
        if (MaterialUtility.calculateHasKingOnly(color.getOppositeSide(), staticPosition)) {
          // state 2a

          final var isFirstCanMoveToAttackedSquareSecondNot = squaresAttackedByNotHavingMove
              .contains(firstLegalMove.moveSpecification().toSquare())
              && !squaresAttackedByNotHavingMove.contains(secondLegalMove.moveSpecification().toSquare());

          final var isFirstCannotMoveToAttackedSquareButSecond = !squaresAttackedByNotHavingMove
              .contains(firstLegalMove.moveSpecification().toSquare())
              && squaresAttackedByNotHavingMove.contains(secondLegalMove.moveSpecification().toSquare());

          final var isBothCanMoveToAttackedSquare = squaresAttackedByNotHavingMove
              .contains(firstLegalMove.moveSpecification().toSquare())
              && squaresAttackedByNotHavingMove.contains(secondLegalMove.moveSpecification().toSquare());

          final var movingPieceHighestFirst = Integer.compare(-firstLegalMove.movingPiece().getPieceType().getValue(),
              -secondLegalMove.movingPiece().getPieceType().getValue());

          switch (checkmateMaterialMatingSide) {
            case KING_AND_QUEEN: {
              final var isHasOneQueenOnly = MaterialUtility.calculateNumberOfQueens(color, staticPosition) == 1;

              if (isFirstCanMoveToAttackedSquareSecondNot
                  && (!isHasOneQueenOnly || firstLegalMove.movingPiece().getPieceType() != PieceType.QUEEN)) {
                return -1;
              }

              if (isFirstCannotMoveToAttackedSquareButSecond
                  && (!isHasOneQueenOnly || secondLegalMove.movingPiece().getPieceType() != PieceType.QUEEN)) {
                return 1;
              }

              if (isBothCanMoveToAttackedSquare) {
                if (!isHasOneQueenOnly) {
                  return movingPieceHighestFirst;
                }
                if (firstLegalMove.movingPiece().getPieceType() != PieceType.QUEEN
                    && secondLegalMove.movingPiece().getPieceType() == PieceType.QUEEN) {
                  return -1;
                }
                if (firstLegalMove.movingPiece().getPieceType() == PieceType.QUEEN
                    && secondLegalMove.movingPiece().getPieceType() != PieceType.QUEEN) {
                  return 1;
                }
              }
            }
              break;
            case KING_AND_ROOK: {
              final var isHasOneQueenOnly = MaterialUtility.calculateNumberOfQueens(color, staticPosition) == 1;

              if (isFirstCanMoveToAttackedSquareSecondNot
                  && (!isHasOneQueenOnly || firstLegalMove.movingPiece().getPieceType() != PieceType.QUEEN)) {
                return -1;
              }

              if (isFirstCannotMoveToAttackedSquareButSecond
                  && (!isHasOneQueenOnly || secondLegalMove.movingPiece().getPieceType() != PieceType.QUEEN)) {
                return 1;
              }

              if (isBothCanMoveToAttackedSquare) {
                if (!isHasOneQueenOnly) {
                  return movingPieceHighestFirst;
                }
                if (firstLegalMove.movingPiece().getPieceType() != PieceType.QUEEN
                    && secondLegalMove.movingPiece().getPieceType() == PieceType.QUEEN) {
                  return -1;
                }
                if (firstLegalMove.movingPiece().getPieceType() == PieceType.QUEEN
                    && secondLegalMove.movingPiece().getPieceType() != PieceType.QUEEN) {
                  return 1;
                }
              }
            }
              break;
            case KING_AND_OPPOSITE_SQUARES_BISHOP: {
              final var isHasOneLightSquareBishopOnly = MaterialUtility.calculateNumberOfBishops(color, staticPosition,
                  SquareType.LIGHT_SQUARE) == 1;
              final var isHasOneDarkSquareBishopOnly = MaterialUtility.calculateNumberOfBishops(color, staticPosition,
                  SquareType.DARK_SQUARE) == 1;

              final Set<PieceType> affordablePieceTypeSetLightSquare = new TreeSet<>();
              affordablePieceTypeSetLightSquare.add(PieceType.ROOK);
              affordablePieceTypeSetLightSquare.add(PieceType.KNIGHT);
              if (!isHasOneLightSquareBishopOnly) {
                affordablePieceTypeSetLightSquare.add(PieceType.BISHOP);
              }
              affordablePieceTypeSetLightSquare.add(PieceType.QUEEN);
              affordablePieceTypeSetLightSquare.add(PieceType.PAWN);

              final Set<PieceType> affordablePieceTypeSetDarkSquare = new TreeSet<>();
              affordablePieceTypeSetDarkSquare.add(PieceType.ROOK);
              affordablePieceTypeSetDarkSquare.add(PieceType.KNIGHT);
              if (!isHasOneDarkSquareBishopOnly) {
                affordablePieceTypeSetDarkSquare.add(PieceType.BISHOP);
              }
              affordablePieceTypeSetDarkSquare.add(PieceType.QUEEN);
              affordablePieceTypeSetDarkSquare.add(PieceType.PAWN);

              final Map<SquareType, Set<PieceType>> affordablePieceTypeMap = new TreeMap<>();
              affordablePieceTypeMap.put(SquareType.LIGHT_SQUARE, affordablePieceTypeSetLightSquare);
              affordablePieceTypeMap.put(SquareType.DARK_SQUARE, affordablePieceTypeSetDarkSquare);

              final PieceType firstMovingPieceType = firstLegalMove.movingPiece().getPieceType();
              final PieceType secondMovingPieceType = secondLegalMove.movingPiece().getPieceType();

              final SquareType firstFromSquareSquareType = firstLegalMove.moveSpecification().fromSquare()
                  .getSquareType();
              final SquareType secondFromSquareSquareType = secondLegalMove.moveSpecification().fromSquare()
                  .getSquareType();

              if (isFirstCanMoveToAttackedSquareSecondNot && NonNullWrapperCommon
                  .get(affordablePieceTypeMap, firstFromSquareSquareType).contains(firstMovingPieceType)) {
                return -1;
              }

              if (isFirstCannotMoveToAttackedSquareButSecond && NonNullWrapperCommon
                  .get(affordablePieceTypeMap, secondFromSquareSquareType).contains(secondMovingPieceType)) {
                return 1;
              }

              if (isBothCanMoveToAttackedSquare) {
                if (NonNullWrapperCommon.get(affordablePieceTypeMap, firstFromSquareSquareType)
                    .contains(firstMovingPieceType)
                    && !NonNullWrapperCommon.get(affordablePieceTypeMap, secondFromSquareSquareType)
                        .contains(secondMovingPieceType)) {
                  return -1;
                }
                if (!NonNullWrapperCommon.get(affordablePieceTypeMap, firstFromSquareSquareType)
                    .contains(firstMovingPieceType)
                    && NonNullWrapperCommon.get(affordablePieceTypeMap, secondFromSquareSquareType)
                        .contains(secondMovingPieceType)) {
                  return 1;
                }
                if (NonNullWrapperCommon.get(affordablePieceTypeMap, firstFromSquareSquareType)
                    .contains(firstMovingPieceType)
                    && NonNullWrapperCommon.get(affordablePieceTypeMap, secondFromSquareSquareType)
                        .contains(secondMovingPieceType)) {
                  return movingPieceHighestFirst;
                }
              }
            }
              break;
            case KING_AND_KNIGHT_AND_BISHOP: {
              final var isHasOneKnightOnly = MaterialUtility.calculateNumberOfKnights(color, staticPosition) == 1;
              final var isHasOneBishopOnly = MaterialUtility.calculateNumberOfBishops(color, staticPosition) == 1;

              final PieceType firstMovingPieceType = firstLegalMove.movingPiece().getPieceType();
              final PieceType secondMovingPieceType = secondLegalMove.movingPiece().getPieceType();

              final Set<PieceType> affordablePieceTypeSet = new TreeSet<>();
              affordablePieceTypeSet.add(PieceType.ROOK);
              if (!isHasOneKnightOnly) {
                affordablePieceTypeSet.add(PieceType.KNIGHT);
              }
              if (!isHasOneBishopOnly) {
                affordablePieceTypeSet.add(PieceType.BISHOP);
              }
              affordablePieceTypeSet.add(PieceType.QUEEN);
              affordablePieceTypeSet.add(PieceType.PAWN);

              if (isFirstCanMoveToAttackedSquareSecondNot && affordablePieceTypeSet.contains(firstMovingPieceType)) {
                return -1;
              }

              if (isFirstCannotMoveToAttackedSquareButSecond
                  && affordablePieceTypeSet.contains(secondMovingPieceType)) {
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
                  return movingPieceHighestFirst;
                }
              }
            }
              break;
            case NONE:
            default:
              throw new IllegalArgumentException();

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

            final var comparePieceCapturedValue = Integer.compare(firstPieceTypeCapture.getValue(),
                secondPieceTypeCapture.getValue());

            // capturing lower valued pieces preferred
            if (comparePieceCapturedValue != 0) {
              return comparePieceCapturedValue;
            }

            // capturing with higher valued pieces preferred
            final PieceType firstMovingPiece = firstLegalMove.movingPiece().getPieceType();
            final PieceType secondMovingPiece = secondLegalMove.movingPiece().getPieceType();
            final var comparePieceMoving = Integer.compare(-firstMovingPiece.getValue(), -secondMovingPiece.getValue());
            return comparePieceMoving;
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
            final PromotionPieceType secondPromotionPieceType = secondLegalMove.moveSpecification()
                .promotionPieceType();

            return Integer.compare(-firstPromotionPieceType.getPieceType().getValue(),
                -secondPromotionPieceType.getPieceType().getValue());
          }
        }
      }
      return 0;
    }

    // non intended winner having move
    // case 1
    // state 1
    if (ClassicalCheckmate.isClassicalCheckmateMaterial(color, staticPosition)) {
      if (firstLegalMove.pieceCaptured() == Piece.NONE && secondLegalMove.pieceCaptured() != Piece.NONE) {
        return -1;
      }
      if (firstLegalMove.pieceCaptured() != Piece.NONE && secondLegalMove.pieceCaptured() == Piece.NONE) {
        return 1;
      }
    } else {

      final ClassicalCheckmateType checkmateMaterialMatingSide = ClassicalCheckmate
          .calculateClassicalCheckmateMaterialMatingSide(color, staticPosition);

      if (checkmateMaterialMatingSide != ClassicalCheckmateType.NONE) {
        // state 2
        if (MaterialUtility.calculateHasKingOnly(color.getOppositeSide(), staticPosition)) {
          // state 2a
          if (firstLegalMove.pieceCaptured() != Piece.NONE && secondLegalMove.pieceCaptured() == Piece.NONE) {
            switch (checkmateMaterialMatingSide) {
              case KING_AND_QUEEN:
                if (MaterialUtility.calculateNumberOfQueens(color, staticPosition) != 1
                    || firstLegalMove.pieceCaptured().getPieceType() != PieceType.QUEEN) {
                  return -1;
                }
                break;
              case KING_AND_ROOK:
                if (MaterialUtility.calculateNumberOfRooks(color, staticPosition) != 1
                    || firstLegalMove.pieceCaptured().getPieceType() != PieceType.ROOK) {
                  return -1;
                }
                break;
              case KING_AND_OPPOSITE_SQUARES_BISHOP:
                if (MaterialUtility.calculateNumberOfBishops(color, staticPosition, SquareType.LIGHT_SQUARE) != 1
                    || firstLegalMove.pieceCaptured().getPieceType() != PieceType.BISHOP
                        && firstLegalMove.moveSpecification().fromSquare().getSquareType() != SquareType.LIGHT_SQUARE) {
                  return -1;
                }
                if (MaterialUtility.calculateNumberOfBishops(color, staticPosition, SquareType.DARK_SQUARE) != 1
                    || firstLegalMove.pieceCaptured().getPieceType() != PieceType.BISHOP
                        && firstLegalMove.moveSpecification().fromSquare().getSquareType() != SquareType.DARK_SQUARE) {
                  return -1;
                }
                break;
              case KING_AND_KNIGHT_AND_BISHOP:
                if (MaterialUtility.calculateNumberOfKnights(color, staticPosition) != 1
                    || firstLegalMove.pieceCaptured().getPieceType() != PieceType.KNIGHT
                    || MaterialUtility.calculateNumberOfBishops(color, staticPosition) != 1
                    || firstLegalMove.pieceCaptured().getPieceType() != PieceType.BISHOP) {
                  return -1;
                }
                break;
              case NONE:
                break;
              default:
                throw new IllegalArgumentException();
            }
          }

          if (firstLegalMove.pieceCaptured() == Piece.NONE && secondLegalMove.pieceCaptured() != Piece.NONE) {
            switch (checkmateMaterialMatingSide) {
              case KING_AND_QUEEN:
                if (MaterialUtility.calculateNumberOfQueens(color, staticPosition) != 1
                    || secondLegalMove.pieceCaptured().getPieceType() != PieceType.QUEEN) {
                  return 1;
                }
                break;
              case KING_AND_ROOK:
                if (MaterialUtility.calculateNumberOfRooks(color, staticPosition) != 1
                    || secondLegalMove.pieceCaptured().getPieceType() != PieceType.ROOK) {
                  return 1;
                }
                break;
              case KING_AND_OPPOSITE_SQUARES_BISHOP:
                if (MaterialUtility.calculateNumberOfBishops(color, staticPosition, SquareType.LIGHT_SQUARE) != 1
                    || secondLegalMove.pieceCaptured().getPieceType() != PieceType.BISHOP && secondLegalMove
                        .moveSpecification().fromSquare().getSquareType() != SquareType.LIGHT_SQUARE) {
                  return 1;
                }
                if (MaterialUtility.calculateNumberOfBishops(color, staticPosition, SquareType.DARK_SQUARE) != 1
                    || secondLegalMove.pieceCaptured().getPieceType() != PieceType.BISHOP
                        && secondLegalMove.moveSpecification().fromSquare().getSquareType() != SquareType.DARK_SQUARE) {
                  return 1;
                }
                break;
              case KING_AND_KNIGHT_AND_BISHOP:
                if (MaterialUtility.calculateNumberOfKnights(color, staticPosition) != 1
                    || secondLegalMove.pieceCaptured().getPieceType() != PieceType.KNIGHT
                    || MaterialUtility.calculateNumberOfBishops(color, staticPosition) != 1
                    || secondLegalMove.pieceCaptured().getPieceType() != PieceType.BISHOP) {
                  return 1;
                }
                break;
              case NONE:
                break;
              default:
                throw new IllegalArgumentException();
            }
          }

          if (firstLegalMove.pieceCaptured() != Piece.NONE && secondLegalMove.pieceCaptured() != Piece.NONE) {
            final var isHasOneKnightOnly = MaterialUtility.calculateNumberOfKnights(color, staticPosition) == 1;
            final var isHasOneBishopOnly = MaterialUtility.calculateNumberOfBishops(color, staticPosition) == 1;

            final PieceType firstCapturedPieceType = firstLegalMove.pieceCaptured().getPieceType();
            final PieceType secondCapturedPieceType = secondLegalMove.pieceCaptured().getPieceType();

            final Set<PieceType> capturablePieceTypeSet = new TreeSet<>();
            capturablePieceTypeSet.add(PieceType.ROOK);
            if (!isHasOneKnightOnly) {
              capturablePieceTypeSet.add(PieceType.KNIGHT);
            }
            if (!isHasOneBishopOnly) {
              capturablePieceTypeSet.add(PieceType.BISHOP);
            }
            capturablePieceTypeSet.add(PieceType.QUEEN);
            capturablePieceTypeSet.add(PieceType.PAWN);

            if (capturablePieceTypeSet.contains(firstCapturedPieceType)
                && !capturablePieceTypeSet.contains(secondCapturedPieceType)) {
              return -1;
            }
            if (!capturablePieceTypeSet.contains(firstCapturedPieceType)
                && capturablePieceTypeSet.contains(secondCapturedPieceType)) {
              return 1;
            }
            if (!capturablePieceTypeSet.contains(firstCapturedPieceType)
                && !capturablePieceTypeSet.contains(secondCapturedPieceType)) {
              // higher value pieces first, so there is more space for moving
              return Integer.compare(-firstLegalMove.movingPiece().getPieceType().getValue(),
                  -secondLegalMove.movingPiece().getPieceType().getValue());
            }

          }

        } else {
          // state 2b
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

            final var comparePieceCapturedValue = Integer.compare(firstPieceTypeCapture.getValue(),
                secondPieceTypeCapture.getValue());

            // capturing lower valued pieces preferred
            if (comparePieceCapturedValue != 0) {
              return comparePieceCapturedValue;
            }

            // capturing with higher valued pieces preferred
            final PieceType firstMovingPiece = firstLegalMove.movingPiece().getPieceType();
            final PieceType secondMovingPiece = secondLegalMove.movingPiece().getPieceType();
            final var comparePieceMoving = Integer.compare(-firstMovingPiece.getValue(), -secondMovingPiece.getValue());
            return comparePieceMoving;
          }
        }
      } else {
        // state 3
        if (firstLegalMove.pieceCaptured() == Piece.NONE && secondLegalMove.pieceCaptured() != Piece.NONE) {
          return -1;
        }
        if (firstLegalMove.pieceCaptured() != Piece.NONE && secondLegalMove.pieceCaptured() == Piece.NONE) {
          return 1;
        }
      }
    }

    return 0;
  }

}
