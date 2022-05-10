package com.dlb.chess.board;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.utility.StaticPositionUtility;
import com.dlb.chess.enums.MoveCheck;
import com.dlb.chess.exceptions.InvalidMoveException;
import com.dlb.chess.model.EmptyBoardMove;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.moves.utility.EnPassantCaptureUtility;
import com.dlb.chess.moves.utility.PawnUtility;
import com.dlb.chess.squares.emptyboard.AbstractEmptyBoardSquares;
import com.dlb.chess.squares.emptyboard.KingNonCastlingEmptyBoardSquares;
import com.dlb.chess.squares.to.potential.BishopPotentialToSquares;
import com.dlb.chess.squares.to.potential.QueenPotentialToSquares;
import com.dlb.chess.squares.to.potential.RookPotentialToSquares;
import com.dlb.chess.squares.to.threaten.AbstractThreatenSquares;

public class ValidateNewMove implements EnumConstants {

  public static MoveCheck validateNewMove(ApiBoard board, MoveSpecification moveSpecification)
      throws InvalidMoveException {
    final Side havingMove = board.getHavingMove();
    final Square fromSquare = moveSpecification.fromSquare();

    final Piece movingPiece;
    if (fromSquare == Square.NONE) {
      // castling
      movingPiece = Piece.NONE;
    } else {
      movingPiece = board.getStaticPosition().get(fromSquare);
    }

    if (havingMove != moveSpecification.havingMove()) {
      throw new InvalidMoveException(moveSpecification.havingMove() + " is not having the move",
          MoveCheck.BASIC_NOT_HAVING_THE_MOVE);
    }

    if (CastlingUtility.calculateIsCastlingMove(moveSpecification)) {
      validateCastling(board, moveSpecification);
      return MoveCheck.SUCCESS;
    }

    // has moved a piece, has move an own piece, has not moved to piece onto itself
    validateNonCastlingBasic(board, moveSpecification);

    if (movingPiece.getPieceType() == PAWN) {
      validatePawnMove(board, moveSpecification);
      validateKingLeftInCheckOrExposedToCheckNonKingMove(board, moveSpecification);
      return MoveCheck.SUCCESS;
    }

    if (movingPiece.getPieceType() != KING) {
      // valid movement, to square not occupied by own piece, no jump
      validateMovingPiece(board, moveSpecification);
      validateKingLeftInCheckOrExposedToCheckNonKingMove(board, moveSpecification);
      return MoveCheck.SUCCESS;
    }

    if (movingPiece.getPieceType() == KING) {
      // valid movement, to square not occupied by own piece, not next to opponent king, not captures guarded, stay in
      // check, exposes to check
      validateKingMove(board, moveSpecification);
      // we check king left in check or put in check separately
      return MoveCheck.SUCCESS;
    }

    throw new ProgrammingMistakeException("The move check missed some cases");
  }

  private static void validateCastling(ApiBoard board, MoveSpecification moveSpecification)
      throws InvalidMoveException {

    if (!CastlingUtility.calculateIsCastlingMove(moveSpecification)) {
      throw new ProgrammingMistakeException("Precondition is not met");
    }

    final Side havingMove = board.getHavingMove();

    final var castlingCheck = switch (moveSpecification.castlingMove()) {
      case KING_SIDE -> CastlingUtility.calculateKingSideCastlingCheck(board.getStaticPosition(), havingMove,
          board.getCastlingRight(havingMove));
      case QUEEN_SIDE -> CastlingUtility.calculateQueenSideCastlingCheck(board.getStaticPosition(), havingMove,
          board.getCastlingRight(havingMove));
      case NONE -> throw new IllegalArgumentException();
    };
    switch (castlingCheck) {
      case CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE:
        throw new InvalidMoveException("the king or rook have left their initial position", castlingCheck);
      case CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY:
        throw new InvalidMoveException("not all squares between the rook and the king are empty", castlingCheck);
      case CASTLING_PRIORITY_4_KING_IN_CHECK:
        throw new InvalidMoveException("castling is not possible because the king is in check", castlingCheck);
      case CASTLING_PRIORITY_5_KING_WOULD_TRAVEL_THROUGH_CHECK:
        throw new InvalidMoveException("the king would travel over a field that is in check", castlingCheck);
      case CASTLING_PRIORITY_6_KING_WOULD_END_IN_CHECK:
        throw new InvalidMoveException("the king would end in check", castlingCheck);
      case CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE:
        final CastlingRight castlingRight = board.getCastlingRight(havingMove);
        if (castlingRight == CastlingRight.NONE) {
          throw new InvalidMoveException("there are no castling rights anymore on both sides", castlingCheck);
        }
        throw new InvalidMoveException("there is no castling right anymore on this side", castlingCheck);
      case SUCCESS:
        // valid castling
        break;
      case ALL_BUT_KING_KING_EXPOSED_TO_CHECK:
      case ALL_BUT_KING_KING_LEFT_IN_CHECK:
      case LONG_RANGE_PIECES_CANNOT_JUMP_OVER_PIECES:
      case ALL_MOVEMENT_NOT_POSSIBLE:
      case ALL_TO_SQUARE_OCCUPIED_BY_OWN_PIECE:
      case BASIC_MOVING_PIECE_NONE:
      case BASIC_MOVING_PIECE_OPPONENT:
      case BASIC_NON_PAWN_PROMOTION_PIECE_SET:
      case BASIC_NOT_HAVING_THE_MOVE:
      case KING_CAPTURES_GUARDED_PIECE:
      case KING_IN_CHECK_TO_EMPTY_ATTACKED_SQUARE_NO_LEGAL_MOVES:
      case KING_MOVES_INTO_CHECK:
      case KING_MOVES_NEXT_TO_OPPONENT_KING:
      case PAWN_DIAGONAL_OWN_PIECE:
      case PAWN_EN_PASSANT_CAPTURE_NO_IMMEDIATE_BEFORE_TWO_SQUARE_ADVANCE:
      case PAWN_EN_PASSANT_CAPTURE_WRONG_RANK:
      case PAWN_FORWARD_ONE_SQUARE_TO_SQUARE_NOT_EMPTY:
      case PAWN_FORWARD_TWO_SQUARE_BOTH_SQUARE_NOT_EMPTY:
      case PAWN_FORWARD_TWO_SQUARE_JUMP_OVER_SQUARE_ONLY_NOT_EMPTY:
      case PAWN_FORWARD_TWO_SQUARE_TO_SQUARE_ONLY_NOT_EMPTY:
      case PAWN_NON_PROMOTION_MOVE_PROMOTION_PIECE:
      case PAWN_PROMOTION_MOVE_NO_PROMOTION_PIECE:
      case KING_IN_CHECK_TO_EMPTY_ATTACKED_SQUARE_LEGAL_MOVES:
        // per precondition should not happen
      default:
        throw new IllegalArgumentException();
    }
  }

  private static void validateNonCastlingBasic(ApiBoard board, MoveSpecification moveSpecification)
      throws InvalidMoveException {
    final Side havingMove = board.getHavingMove();
    final Square fromSquare = moveSpecification.fromSquare();
    final Piece movingPiece = board.getStaticPosition().get(fromSquare);

    if (movingPiece == Piece.NONE) {
      throw new InvalidMoveException("the from square is empty", MoveCheck.BASIC_MOVING_PIECE_NONE);
    }

    if (movingPiece.getSide() != havingMove) {
      throw new InvalidMoveException("the moving piece is not an own piece", MoveCheck.BASIC_MOVING_PIECE_OPPONENT);
    }

  }

  // for pawns things are so much different!!
  private static void validatePawnMove(ApiBoard board, MoveSpecification moveSpecification)
      throws InvalidMoveException {
    final Side havingMove = board.getHavingMove();
    final Square fromSquare = moveSpecification.fromSquare();
    final Square toSquare = moveSpecification.toSquare();
    final Piece movingPiece = board.getStaticPosition().get(fromSquare);

    final var isForwardMove = calculateIsPawnEmptyBoardMove(havingMove, fromSquare, toSquare);
    final var isDiagonalMove = PawnUtility.calculateIsPawnDiagonalMove(havingMove, fromSquare, toSquare);

    if (!isForwardMove && !isDiagonalMove) {
      throw new InvalidMoveException("pawns cannot move in this way", MoveCheck.ALL_MOVEMENT_NOT_POSSIBLE);
    }

    if (isForwardMove) {
      if (EnPassantCaptureUtility.calculateIsPawnTwoSquareAdvanceMove(movingPiece, moveSpecification)) {
        validatePawnTwoSquareAdvanceMove(board, moveSpecification);
      }
      validatePawnOneSquareAdvanceMove(board, moveSpecification);

    } else if (isDiagonalMove) {
      // diagonal move
      validatePawnCapture(board, moveSpecification);
    }

    if (Rank.calculateIsPromotionRank(havingMove, moveSpecification.toSquare().getRank())) {
      if (moveSpecification.promotionPieceType() == PromotionPieceType.NONE) {
        throw new InvalidMoveException("this is a pawn promotion move but the promotion piece was not specified",
            MoveCheck.PAWN_PROMOTION_MOVE_NO_PROMOTION_PIECE);
      }

    } else if (moveSpecification.promotionPieceType() != PromotionPieceType.NONE) {
      throw new InvalidMoveException("this is not a pawn promotion move but the promotion piece was specified",
          MoveCheck.PAWN_NON_PROMOTION_MOVE_PROMOTION_PIECE);
    }
  }

  private static void validatePawnTwoSquareAdvanceMove(ApiBoard board, MoveSpecification moveSpecification) {

    final Side havingMove = board.getHavingMove();
    final Square toSquare = moveSpecification.toSquare();

    final Square jumpOverSquare = Square.calculateJumpOverSquare(havingMove, moveSpecification.toSquare());
    final var jumpOverSquareIsEmpty = board.getStaticPosition().get(jumpOverSquare) == Piece.NONE;
    final var toSquareIsEmpty = board.getStaticPosition().get(toSquare) == Piece.NONE;

    if (!jumpOverSquareIsEmpty) {
      if (!toSquareIsEmpty) {
        throw new InvalidMoveException(
            "when moving two squares both the passing and destination square must be empty, but both squares are occcupied",
            MoveCheck.PAWN_FORWARD_TWO_SQUARE_BOTH_SQUARE_NOT_EMPTY);
      }
      throw new InvalidMoveException(
          "when moving two squares both the passing and destination square must be empty, but the passing square is occcupied",
          MoveCheck.PAWN_FORWARD_TWO_SQUARE_JUMP_OVER_SQUARE_ONLY_NOT_EMPTY);
    }

    if (!toSquareIsEmpty) {
      throw new InvalidMoveException(
          "when moving two squares both the passing and destination square must be empty, but the destination square is occcupied",
          MoveCheck.PAWN_FORWARD_TWO_SQUARE_TO_SQUARE_ONLY_NOT_EMPTY);
    }
  }

  private static void validatePawnOneSquareAdvanceMove(ApiBoard board, MoveSpecification moveSpecification) {

    final Square toSquare = moveSpecification.toSquare();
    final Piece capturedPiece = board.getStaticPosition().get(toSquare);

    if (capturedPiece != Piece.NONE) {
      throw new InvalidMoveException(
          "when moving a pawn one square forwards, the destination square must be empty, but the destination square is occupied",
          MoveCheck.PAWN_FORWARD_ONE_SQUARE_TO_SQUARE_NOT_EMPTY);
    }
  }

  private static void validatePawnCapture(ApiBoard board, MoveSpecification moveSpecification) {

    final Side havingMove = board.getHavingMove();
    final Square toSquare = moveSpecification.toSquare();
    final Piece capturedPiece = board.getStaticPosition().get(toSquare);

    if (capturedPiece == Piece.NONE) {
      validatePawnEnPassantCapture(board, moveSpecification);
    } else if (capturedPiece.getSide() == havingMove) {
      throw new InvalidMoveException("the pawn you cannot diagonally capture an own piece",
          MoveCheck.PAWN_DIAGONAL_OWN_PIECE);
    } else if (capturedPiece.getSide() != havingMove.getOppositeSide()) {
      // we are fine, opponent piece was captured
      throw new ProgrammingMistakeException();
    }
  }

  private static void validatePawnEnPassantCapture(ApiBoard board, MoveSpecification moveSpecification) {
    // diagonal move to an empty square - this can potentially be an en passant capture move

    final Side havingMove = board.getHavingMove();
    final Square toSquare = moveSpecification.toSquare();

    final var isEnPassantCaptureRank = Rank.calculateIsPawnEnPassantCaptureToRank(havingMove, toSquare.getRank());

    if (!isEnPassantCaptureRank) {
      throw new InvalidMoveException(
          "the pawn cannot move diagonally to an empty field, except when en passant capture is possible, which is not the case",
          MoveCheck.PAWN_EN_PASSANT_CAPTURE_WRONG_RANK);
    }

    final Square boardEnPassantCaptureTargetSquare = board.getEnPassantCaptureTargetSquare();

    if (!boardEnPassantCaptureTargetSquare.equals(toSquare)) {

      final Square checkOpponentPawnTwoSquareAdvanceToSquare = Square.calculateBehindSquare(havingMove, toSquare);
      final String sanTwoSquareAdvance = checkOpponentPawnTwoSquareAdvanceToSquare.getName();
      throw new InvalidMoveException(
          "the en passant capture requires that the pawn move " + sanTwoSquareAdvance
              + " was immediately played before, which is not the case",
          MoveCheck.PAWN_EN_PASSANT_CAPTURE_NO_IMMEDIATE_BEFORE_TWO_SQUARE_ADVANCE);
    }

  }

  private static void validateMovingPiece(ApiBoard board, MoveSpecification moveSpecification) {

    if (moveSpecification.promotionPieceType() != PromotionPieceType.NONE) {
      throw new InvalidMoveException("the promotion piece type which was set as "
          + moveSpecification.promotionPieceType() + " can only be specified for pawn promotion moves",
          MoveCheck.BASIC_NON_PAWN_PROMOTION_PIECE_SET);
    }

    final Side havingMove = board.getHavingMove();
    final Square fromSquare = moveSpecification.fromSquare();
    final Square toSquare = moveSpecification.toSquare();
    final Piece movingPiece = board.getStaticPosition().get(fromSquare);
    final Piece capturedPiece = board.getStaticPosition().get(toSquare);

    final Set<EmptyBoardMove> emptyBoardMoves = AbstractEmptyBoardSquares
        .calculateNonPawnEmptyBoardMoves(movingPiece.getPieceType(), fromSquare);

    if (!calculateIsEmptyBoardMove(toSquare, emptyBoardMoves)) {
      throw new InvalidMoveException("the " + movingPiece.getPieceType().getName() + " cannot move in this way",
          MoveCheck.ALL_MOVEMENT_NOT_POSSIBLE);
    }

    if (capturedPiece != Piece.NONE && capturedPiece.getSide() == havingMove) {
      throw new InvalidMoveException("you cannot capture an own piece", MoveCheck.ALL_TO_SQUARE_OCCUPIED_BY_OWN_PIECE);
    }

    final var toSquareSet = switch (movingPiece.getPieceType()) {
      case ROOK -> RookPotentialToSquares.calculateRookPotentialToSquares(board.getStaticPosition(), fromSquare,
          havingMove);
      case BISHOP -> BishopPotentialToSquares.calculateBishopPotentialToSquares(board.getStaticPosition(), fromSquare,
          havingMove);
      case QUEEN -> QueenPotentialToSquares.calculateQueenPotentialToSquares(board.getStaticPosition(), fromSquare,
          havingMove);
      case KNIGHT -> new TreeSet<>();
      case KING, PAWN, NONE -> throw new IllegalArgumentException();
    };
    switch (movingPiece.getPieceType()) {
      case ROOK:
      case BISHOP:
      case QUEEN:
        if (!toSquareSet.contains(toSquare)) {
          throw new InvalidMoveException(
              "the " + movingPiece.getPieceType().getName() + " cannot jump over other pieces",
              MoveCheck.LONG_RANGE_PIECES_CANNOT_JUMP_OVER_PIECES);

        }
        break;
      case KNIGHT:
        break;
      case KING:
      case PAWN:
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  private static void validateKingMove(ApiBoard board, MoveSpecification moveSpecification) {

    if (moveSpecification.promotionPieceType() != PromotionPieceType.NONE) {
      throw new InvalidMoveException(
          "The move is not valid because the promotion piece type which was set as "
              + moveSpecification.promotionPieceType() + " can only be specified for pawn promotion moves",
          MoveCheck.BASIC_NON_PAWN_PROMOTION_PIECE_SET);
    }

    final Side havingMove = board.getHavingMove();
    final Square fromSquare = moveSpecification.fromSquare();
    final Square toSquare = moveSpecification.toSquare();
    final Piece movingPiece = board.getStaticPosition().get(fromSquare);
    final Side oppositeSide = havingMove.getOppositeSide();
    final Piece pieceOnToSquare = board.getStaticPosition().get(toSquare);

    final Set<EmptyBoardMove> emptyBoardMoves = AbstractEmptyBoardSquares.calculateNonPawnEmptyBoardMoves(KING,
        fromSquare);

    if (!calculateIsEmptyBoardMove(toSquare, emptyBoardMoves)) {
      throw new InvalidMoveException("the " + movingPiece.getPieceType().getName() + " cannot move in this way",
          MoveCheck.ALL_MOVEMENT_NOT_POSSIBLE);
    }

    if (pieceOnToSquare != Piece.NONE && pieceOnToSquare.getSide() == havingMove) {
      throw new InvalidMoveException("you cannot capture an an own piece",
          MoveCheck.ALL_TO_SQUARE_OCCUPIED_BY_OWN_PIECE);
    }

    if (calculateIsMoveNextToOpponentKing(board, moveSpecification)) {
      throw new InvalidMoveException("the king can not be moved next to the opponent king",
          MoveCheck.KING_MOVES_NEXT_TO_OPPONENT_KING);
    }

    final Set<Square> threatenedSquares = AbstractThreatenSquares.calculateThreatenedSquares(board.getStaticPosition(),
        oppositeSide);

    if (threatenedSquares.contains(toSquare) && pieceOnToSquare != Piece.NONE
        && pieceOnToSquare.getSide() == oppositeSide) {
      throw new InvalidMoveException("the king cannot capture this piece because it is guarded by another piece",
          MoveCheck.KING_CAPTURES_GUARDED_PIECE);
    }

    if (StaticPositionUtility.calculateIsEvaluateAttackingKing(board.getStaticPosition(), moveSpecification)) {
      if (StaticPositionUtility.calculateIsCheck(board.getStaticPosition(), havingMove)) {
        final Piece king = Piece.calculateKingPiece(moveSpecification.havingMove());
        if (calculateHasKingMove(board.getLegalMoveSet(), king)) {
          throw new InvalidMoveException(
              "it leaves the king in check. The king can only be moved to a nonthreatened square. Because there are no such squares, another piece must be moved instead.",
              MoveCheck.KING_IN_CHECK_TO_EMPTY_ATTACKED_SQUARE_LEGAL_MOVES);
        }
        throw new InvalidMoveException(
            "it leaves the king in check. . The king can only be moved to a nonthreatened square. Because there are no such squares, another piece must be moved instead.",
            MoveCheck.KING_IN_CHECK_TO_EMPTY_ATTACKED_SQUARE_NO_LEGAL_MOVES);
      }
      throw new InvalidMoveException("it exposes the king to check", MoveCheck.KING_MOVES_INTO_CHECK);
    }
  }

  private static boolean calculateHasKingMove(Set<LegalMove> legalMoves, Piece king) {
    for (final LegalMove legalMove : legalMoves) {
      if (legalMove.movingPiece() == king) {
        return true;
      }
    }
    return false;
  }

  private static void validateKingLeftInCheckOrExposedToCheckNonKingMove(ApiBoard board,
      MoveSpecification moveSpecification) throws InvalidMoveException {
    final Side havingMove = board.getHavingMove();

    if (StaticPositionUtility.calculateIsEvaluateAttackingKing(board.getStaticPosition(), moveSpecification)) {
      if (StaticPositionUtility.calculateIsCheck(board.getStaticPosition(), havingMove)) {
        throw new InvalidMoveException("it would leave the own king in check",
            MoveCheck.ALL_BUT_KING_KING_LEFT_IN_CHECK);
      }
      throw new InvalidMoveException("it would expose the own king to check",
          MoveCheck.ALL_BUT_KING_KING_EXPOSED_TO_CHECK);
    }
  }

  private static boolean calculateIsMoveNextToOpponentKing(ApiBoard board, MoveSpecification moveSpecification) {
    final Side havingMove = moveSpecification.havingMove();
    final Square opponentKingSquare = StaticPositionUtility.calculateKingSquare(board.getStaticPosition(),
        havingMove.getOppositeSide());
    final Set<Square> opponentKingThreatingSquareSet = KingNonCastlingEmptyBoardSquares
        .getKingSquares(opponentKingSquare);
    return opponentKingThreatingSquareSet.contains(moveSpecification.toSquare());
  }

  private static boolean calculateIsEmptyBoardMove(Square toSquare, Set<EmptyBoardMove> emptyBoardMoves) {
    for (final EmptyBoardMove emptyBoardMove : emptyBoardMoves) {
      if (emptyBoardMove.toSquare() == toSquare) {
        return true;
      }
    }
    return false;
  }

  private static boolean calculateIsPawnEmptyBoardMove(Side havingMove, Square fromSquare, Square toSquare) {
    final Set<EmptyBoardMove> emptyBoardMoves = AbstractEmptyBoardSquares.calculatePawnEmptyBoardMoves(havingMove,
        fromSquare);
    return calculateIsEmptyBoardMove(toSquare, emptyBoardMoves);
  }

  public static Set<MoveSpecification> calculateMoveSpecifications(Set<LegalMove> legalMoveSet) {
    final Set<MoveSpecification> result = new TreeSet<>();
    for (final LegalMove legalMove : legalMoveSet) {
      result.add(legalMove.moveSpecification());
    }
    return result;
  }

}
