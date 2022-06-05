package com.dlb.chess.moves.utility;

import static com.dlb.chess.common.utility.ImmutableUtility.constructListSquare;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.board.model.UpdateSquare;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.model.LegalMove;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class EnPassantCaptureUtility implements EnumConstants {

  private static final ImmutableList<ImmutableList<Square>> WHITE_EN_PASSANT_CAPTURE_FROM_TO;

  static {
    final List<ImmutableList<Square>> list = new ArrayList<>();
    list.add(constructListSquare(A5, B6));
    list.add(constructListSquare(B5, C6));
    list.add(constructListSquare(C5, D6));
    list.add(constructListSquare(D5, E6));
    list.add(constructListSquare(E5, F6));
    list.add(constructListSquare(F5, G6));
    list.add(constructListSquare(G5, H6));
    list.add(constructListSquare(B5, A6));
    list.add(constructListSquare(C5, B6));
    list.add(constructListSquare(D5, C6));
    list.add(constructListSquare(E5, D6));
    list.add(constructListSquare(F5, E6));
    list.add(constructListSquare(G5, F6));
    list.add(constructListSquare(H5, G6));
    WHITE_EN_PASSANT_CAPTURE_FROM_TO = NonNullWrapperCommon.copyOfList(list);

  }

  private static final ImmutableList<ImmutableList<Square>> BLACK_EN_PASSANT_CAPTURE_FROM_TO;

  static {
    final List<ImmutableList<Square>> list = new ArrayList<>();

    list.add(constructListSquare(A4, B3));
    list.add(constructListSquare(B4, C3));
    list.add(constructListSquare(C4, D3));
    list.add(constructListSquare(D4, E3));
    list.add(constructListSquare(E4, F3));
    list.add(constructListSquare(F4, G3));
    list.add(constructListSquare(G4, H3));
    list.add(constructListSquare(B4, A3));
    list.add(constructListSquare(C4, B3));
    list.add(constructListSquare(D4, C3));
    list.add(constructListSquare(E4, D3));
    list.add(constructListSquare(F4, E3));
    list.add(constructListSquare(G4, F3));
    list.add(constructListSquare(H4, G3));

    BLACK_EN_PASSANT_CAPTURE_FROM_TO = NonNullWrapperCommon.copyOfList(list);
  }

  private static final ImmutableMap<Square, Square> WHITE_EN_PASSANT_CAPTURE_TO_CAPTURE;

  static {
    final EnumMap<Square, Square> map = Maps.newEnumMap(Square.class);

    map.put(A6, A5);
    map.put(B6, B5);
    map.put(C6, C5);
    map.put(D6, D5);
    map.put(E6, E5);
    map.put(F6, F5);
    map.put(G6, G5);
    map.put(H6, H5);

    WHITE_EN_PASSANT_CAPTURE_TO_CAPTURE = NonNullWrapperCommon.immutableEnumMap(map);
  }

  private static final ImmutableMap<Square, Square> BLACK_EN_PASSANT_CAPTURE_TO_CAPTURE;

  static {
    final EnumMap<Square, Square> map = NonNullWrapperCommon.newEnumMap(Square.class);

    map.put(A3, A4);
    map.put(B3, B4);
    map.put(C3, C4);
    map.put(D3, D4);
    map.put(E3, E4);
    map.put(F3, F4);
    map.put(G3, G4);
    map.put(H3, H4);

    BLACK_EN_PASSANT_CAPTURE_TO_CAPTURE = NonNullWrapperCommon.immutableEnumMap(map);
  }

  private static final ImmutableMap<Square, Square> WHITE_TWO_SQUARE_ADVANCE_TO_EN_PASSANT_CAPTURE_TO;

  static {
    final EnumMap<Square, Square> map = NonNullWrapperCommon.newEnumMap(Square.class);

    map.put(A4, A3);
    map.put(B4, B3);
    map.put(C4, C3);
    map.put(D4, D3);
    map.put(E4, E3);
    map.put(F4, F3);
    map.put(G4, G3);
    map.put(H4, H3);

    WHITE_TWO_SQUARE_ADVANCE_TO_EN_PASSANT_CAPTURE_TO = NonNullWrapperCommon.immutableEnumMap(map);
  }

  private static final ImmutableMap<Square, Square> BLACK_TWO_SQUARE_ADVANCE_TO_EN_PASSANT_CAPTURE_TO;

  static {
    final EnumMap<Square, Square> map = NonNullWrapperCommon.newEnumMap(Square.class);

    map.put(A5, A6);
    map.put(B5, B6);
    map.put(C5, C6);
    map.put(D5, D6);
    map.put(E5, E6);
    map.put(F5, F6);
    map.put(G5, G6);
    map.put(H5, H6);

    BLACK_TWO_SQUARE_ADVANCE_TO_EN_PASSANT_CAPTURE_TO = NonNullWrapperCommon.immutableEnumMap(map);

  }

  // we check if the moving piece is a pawn and the move itself
  public static boolean calculateIsPawnTwoSquareAdvanceMove(Piece movingPiece, MoveSpecification move) {
    if (movingPiece != Piece.NONE && movingPiece.getPieceType() == PAWN) {
      switch (move.havingMove()) {
        case WHITE:
          return Square.WHITE_PAWN_TWO_SQUARE_ADVANCE.contains(calculateFromToList(move));
        case BLACK:
          return Square.BLACK_PAWN_TWO_SQUARE_ADVANCE.contains(calculateFromToList(move));
        case NONE:
        default:
          throw new IllegalArgumentException();
      }
    }
    return false;
  }

  public static boolean calculateHasOpponentPawnOnLeftOrRight(Square pawnSquare, StaticPosition staticPosition) {
    if (staticPosition.isEmpty(pawnSquare)) {
      throw new IllegalArgumentException("No piece on square " + pawnSquare.getName());
    }
    final Piece piece = staticPosition.get(pawnSquare);
    if (piece.getPieceType() != PAWN) {
      throw new IllegalArgumentException("No pawn on square " + pawnSquare.getName());
    }

    final Side side = piece.getSide();

    if (Square.calculateHasLeftSquare(side, pawnSquare)) {
      final Square leftSquare = Square.calculateLeftSquare(side, pawnSquare);
      if (!staticPosition.isEmpty(leftSquare)) {
        final Piece leftPiece = staticPosition.get(leftSquare);
        if (leftPiece.getPieceType() == PAWN && leftPiece.getSide() != side) {
          return true;
        }
      }
    }

    if (Square.calculateHasRightSquare(side, pawnSquare)) {
      final Square rightSquare = Square.calculateRightSquare(side, pawnSquare);
      if (!staticPosition.isEmpty(rightSquare)) {
        final Piece rightPiece = staticPosition.get(rightSquare);
        if (rightPiece.getPieceType() == PAWN && rightPiece.getSide() != side) {
          return true;
        }
      }
    }
    return false;

  }

  private static List<Square> calculateFromToList(MoveSpecification move) {
    final List<Square> result = new ArrayList<>();
    result.add(move.fromSquare());
    result.add(move.toSquare());
    return result;
  }

  /**
   * Possible en passant capture ignoring check condition.
   */
  public static Square calculateEnPassantCaptureTargetSquare(ApiBoard board) {
    if (board.isFirstMove()) {
      return board.getInitialFen().enPassantCaptureTargetSquare();
    }
    return calculateEnPassantCaptureTargetSquare(board.getLastMove());
  }

  public static Square calculateEnPassantCaptureTargetSquare(LegalMove legalMove) {
    if (calculateIsPawnTwoSquareAdvanceMove(legalMove.movingPiece(), legalMove.moveSpecification())) {
      return calculateEnPassantCaptureTargetSquareForTwoSquareAdvanceMove(legalMove.moveSpecification());
    }
    return Square.NONE;
  }

  public static Square calculateEnPassantCaptureTargetSquareForTwoSquareAdvanceMove(MoveSpecification move) {
    switch (move.havingMove()) {
      case WHITE:
        if (!WHITE_TWO_SQUARE_ADVANCE_TO_EN_PASSANT_CAPTURE_TO.containsKey(move.toSquare())) {
          throw new IllegalArgumentException("The method only applies for en passant moves");
        }
        return NonNullWrapperCommon.get(WHITE_TWO_SQUARE_ADVANCE_TO_EN_PASSANT_CAPTURE_TO, move.toSquare());
      case BLACK:
        if (!BLACK_TWO_SQUARE_ADVANCE_TO_EN_PASSANT_CAPTURE_TO.containsKey(move.toSquare())) {
          throw new IllegalArgumentException("The method only applies for en passant moves");
        }
        return NonNullWrapperCommon.get(BLACK_TWO_SQUARE_ADVANCE_TO_EN_PASSANT_CAPTURE_TO, move.toSquare());
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static boolean calculateIsEnPassantCaptureNewMove(StaticPosition staticPosition,
      MoveSpecification moveSpecification) {
    if (CastlingUtility.calculateIsCastlingMove(moveSpecification)) {
      return false;
    }
    final Piece movingPiece = staticPosition.get(moveSpecification.fromSquare());
    switch (moveSpecification.havingMove()) {
      case BLACK:
        if (movingPiece != BLACK_PAWN) {
          return false;
        }
        // when the destination capture field is empty, this was an en passant capture
        return BLACK_EN_PASSANT_CAPTURE_FROM_TO.contains(calculateFromToList(moveSpecification))
            && staticPosition.get(moveSpecification.toSquare()) == Piece.NONE;
      case WHITE:
        if (movingPiece != WHITE_PAWN) {
          return false;
        }
        // when the destination capture field is empty, this was an en passant capture
        return WHITE_EN_PASSANT_CAPTURE_FROM_TO.contains(calculateFromToList(moveSpecification))
            && staticPosition.get(moveSpecification.toSquare()) == Piece.NONE;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static boolean calculateIsEnPassantCapture(StaticPosition staticPositionBeforeMove, MoveSpecification move) {
    if (CastlingUtility.calculateIsCastlingMove(move)) {
      return false;
    }
    final Piece movingPiece = staticPositionBeforeMove.get(move.fromSquare());
    switch (move.havingMove()) {
      case WHITE:
        if (movingPiece != WHITE_PAWN) {
          return false;
        }
        // when the destination capture field is empty, this was an en passant capture
        return WHITE_EN_PASSANT_CAPTURE_FROM_TO.contains(calculateFromToList(move))
            && staticPositionBeforeMove.get(move.toSquare()) == Piece.NONE;
      case BLACK:
        if (movingPiece != BLACK_PAWN) {
          return false;
        }
        // when the destination capture field is empty, this was an en passant capture
        return BLACK_EN_PASSANT_CAPTURE_FROM_TO.contains(calculateFromToList(move))
            && staticPositionBeforeMove.get(move.toSquare()) == Piece.NONE;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static Square calculateSquareOfCapturedPawnForEnPassantCapture(MoveSpecification move) {
    return calculateSquareOfCapturedPawnForEnPassantCapture(move.havingMove(), move.toSquare());
  }

  private static Square calculateSquareOfCapturedPawnForEnPassantCapture(Side havingMove, Square square) {
    switch (havingMove) {
      case WHITE:
        if (!WHITE_EN_PASSANT_CAPTURE_TO_CAPTURE.containsKey(square)) {
          throw new IllegalArgumentException("Please provide the target square of an en passant capture");
        }
        return NonNullWrapperCommon.get(WHITE_EN_PASSANT_CAPTURE_TO_CAPTURE, square);
      case BLACK:
        if (!BLACK_EN_PASSANT_CAPTURE_TO_CAPTURE.containsKey(square)) {
          throw new IllegalArgumentException("Please provide the target square of an en passant capture");
        }
        return NonNullWrapperCommon.get(BLACK_EN_PASSANT_CAPTURE_TO_CAPTURE, square);
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static List<UpdateSquare> performEnPassantCaptureMovements(StaticPosition oldStaticPosition,
      MoveSpecification moveSpecification) {
    // arriving here, the move must have been identified as en passant capture

    final List<UpdateSquare> result = new ArrayList<>();

    final Piece movingPiece = oldStaticPosition.get(moveSpecification.fromSquare());

    // pawn move
    // from square becomes empty
    result.add(new UpdateSquare(moveSpecification.fromSquare()));

    // on to square is the moved pawn
    result.add(new UpdateSquare(moveSpecification.toSquare(), movingPiece));

    // remove captured pawn
    // move not yet done
    final Square squareOfCapturedPawnForEnPassantCapture = calculateSquareOfCapturedPawnForEnPassantCapture(
        moveSpecification);
    // remove the captured pawn
    result.add(new UpdateSquare(squareOfCapturedPawnForEnPassantCapture, Piece.NONE));

    return result;
  }

  public static List<UpdateSquare> performEnPassantCaptureUndoMovements(LegalMove lastMove) {

    final List<UpdateSquare> result = new ArrayList<>();

    // first we clear the diagonally moved to square, which is always empty before
    // an en passant capture
    result.add(new UpdateSquare(lastMove.moveSpecification().toSquare()));

    // now we put the moved pawn back to the position where it started
    final Piece ownPawnPiece = lastMove.movingPiece();
    result.add(new UpdateSquare(lastMove.moveSpecification().fromSquare(), ownPawnPiece));

    // now we put back the pawn which was captured
    final Piece oppositePawnPiece = lastMove.pieceCaptured();
    final Square squareOfCapturedPawnForEnPassantCapture = calculateSquareOfCapturedPawnForEnPassantCapture(
        lastMove.moveSpecification());
    result.add(new UpdateSquare(squareOfCapturedPawnForEnPassantCapture, oppositePawnPiece));

    return result;
  }

}
