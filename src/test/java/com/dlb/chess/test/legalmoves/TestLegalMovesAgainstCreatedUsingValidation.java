package com.dlb.chess.test.legalmoves;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.ValidateNewMove;
import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.exceptions.InvalidMoveException;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.squares.to.potential.AbstractPotentialToSquares;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.reader.PgnStrictCacheForTestCases;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestLegalMovesAgainstCreatedUsingValidation {

  private static final Logger logger = NonNullWrapperCommon
      .getLogger(TestLegalMovesAgainstCreatedUsingValidation.class);

  @SuppressWarnings("static-method")
  @Test
  void test() {
    // the move generation from validation for testing is about ten times slower than the used on
    // so we only perform a spot checks on the PGN^s
    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getRestrictedTestListList()) {
      if (PgnTestConstants.IS_RESTRICT_LEGAL_MOVE_VALIDATION_AGAINST_BOTTOM_UP_TEST) {
        switch (testCaseList.pgnTest()) {
          case BASIC_CHECK_WHITE:
          case BASIC_CHECK_BLACK:
          case BASIC_CHECKMATE_WHITE:
          case BASIC_CHECKMATE_BLACK:
          case BASIC_STALEMATE:
          case BASIC_FROM_FEN:
            break;
          // $CASES-OMITTED$
          default:
            continue;
        }
      }
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        checkLegalMoves(testCaseList.pgnTest().getFolderPath(), testCase.pgnFileName());
      }
    }
  }

  private static void checkLegalMoves(Path folderPath, String pgnFileName) {

    logger.info(pgnFileName);

    final PgnFile pgnFile = PgnStrictCacheForTestCases.getPgn(folderPath, pgnFileName);

    final ApiBoard board = new Board(pgnFile.startFen());
    checkLegalMoves(board);

    for (final PgnHalfMove halfMove : pgnFile.halfMoveList()) {
      board.performMove(halfMove.san());
      checkLegalMoves(board);
    }

  }

  private static void checkLegalMoves(ApiBoard board) {

    final Set<LegalMove> legalMovesActual = board.getLegalMoveSet();

    final Set<MoveSpecification> moveSpecificationsBottomUp = ValidateNewMove
        .calculateMoveSpecifications(legalMovesActual);

    final Set<MoveSpecification> moveSpecificationsFromValidation = calculateMoveSpecificationsFromValidation(board);
    if (!moveSpecificationsBottomUp.equals(moveSpecificationsFromValidation)) {
      throw new ProgrammingMistakeException(
          "The program does not work because it has a bug in generating the legal move specifications");
    }

    final Set<LegalMove> legalMovesExpected = calculateLegalMovesFromValidation(board,
        moveSpecificationsFromValidation);

    assertEquals(legalMovesExpected, legalMovesActual);
  }

  private static Set<LegalMove> calculateLegalMovesFromValidation(ApiBoard board,
      Set<MoveSpecification> moveSpecificationsFromValidation) {
    final Set<LegalMove> result = new TreeSet<>();
    for (final MoveSpecification moveSpecification : moveSpecificationsFromValidation) {
      final LegalMove legalMove = Board.calculateLegalMove(board.getStaticPosition(), moveSpecification);
      result.add(legalMove);
    }
    return result;

  }

  private static Set<MoveSpecification> calculateMoveSpecificationsFromValidation(ApiBoard board) {
    final Set<MoveSpecification> listForBoard = new TreeSet<>();
    // now we do something crazy:
    // we loop through all possible from/to square combinations and filter out the legal ones using the validation
    // this must match with the calculated legal moves - so both methods are hopefully correct (or both wrong..)
    for (final Square fromSquare : Square.BOARD_SQUARE_LIST) {
      final Set<MoveSpecification> listForSquare = calculateMoveSpecificationsFromValidation(board, fromSquare);
      listForBoard.addAll(listForSquare);
    }
    return listForBoard;
  }

  private static Set<MoveSpecification> calculateMoveSpecificationsFromValidation(ApiBoard board, Square fromSquare) {
    final Side havingMove = board.getHavingMove();

    final Set<MoveSpecification> listForSquare = new TreeSet<>();
    // now we do something crazy:
    // we loop through all possible from/to square combinations and filter out the legal ones using the validation
    // this must match with the calculated legal moves - so both methods are hopefully correct (or both wrong..)
    if (board.getStaticPosition().isEmpty(fromSquare)) {
      return listForSquare;
    }
    final Piece boardPiece = board.getStaticPosition().get(fromSquare);
    if (boardPiece.getSide() == havingMove) {
      // castling needs special treatment as always
      if (boardPiece.getPieceType() == PieceType.KING) {
        final MoveSpecification castlingKingSide = new MoveSpecification(havingMove, CastlingMove.KING_SIDE);
        try {
          ValidateNewMove.validateNewMove(board, castlingKingSide);
          listForSquare.add(castlingKingSide);
        } catch (@SuppressWarnings("unused") final InvalidMoveException e) {
          // not valid, so not adding
        }
        final MoveSpecification castlingQueenSide = new MoveSpecification(havingMove, CastlingMove.QUEEN_SIDE);
        try {
          ValidateNewMove.validateNewMove(board, castlingQueenSide);
          listForSquare.add(castlingQueenSide);
        } catch (@SuppressWarnings("unused") final InvalidMoveException e) {
          // not valid, so not adding
        }
      }
      final Set<Square> potentialToSquareSet = AbstractPotentialToSquares.calculatePotentialToSquare(
          board.getStaticPosition(), board.getEnPassantCaptureTargetSquare(), havingMove, fromSquare);
      // we cannot use all board squares - that get's too slow
      // all PGN's expected outcomes are not through in 90 minutes
      for (final Square toSquare : potentialToSquareSet) {
        final MoveSpecification move = new MoveSpecification(havingMove, fromSquare, toSquare);
        try {
          ValidateNewMove.validateNewMove(board, move);
          listForSquare.add(move);
        } catch (@SuppressWarnings("unused") final InvalidMoveException e) {
          // not valid, so not adding
        }
        // we only check the actual promotion moves and not all silly possible combinations
        // that get's too much otherwise
        if (boardPiece.getPieceType() == PieceType.PAWN
            && Rank.calculateIsPromotionRank(havingMove, toSquare.getRank())) {
          for (final PromotionPieceType promotionPieceType : PromotionPieceType.values()) {
            if (promotionPieceType == PromotionPieceType.NONE) {
              continue;
            }
            final MoveSpecification promotionMove = new MoveSpecification(havingMove, fromSquare, toSquare,
                promotionPieceType);
            try {
              ValidateNewMove.validateNewMove(board, promotionMove);
              listForSquare.add(promotionMove);
            } catch (@SuppressWarnings("unused") final InvalidMoveException e) {
              // not valid, so not adding
            }

          }
        }
      }
    }
    return listForSquare;
  }

}
