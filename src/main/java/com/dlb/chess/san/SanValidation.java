package com.dlb.chess.san;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.CastlingConstants;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.moves.utility.EnPassantCaptureUtility;
import com.dlb.chess.san.enums.CheckmateOrCheck;
import com.dlb.chess.san.enums.SanFormat;
import com.dlb.chess.san.enums.SanType;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.san.validate.statically.format.calculate.SanValidateStaticallyFormat;

// TODO today we are not checking opposite rank and diagonals!!
// TODO get clear about different validations and checks
public class SanValidation extends AbstractSan implements EnumConstants {

  public static MoveSpecification calculateMoveSpecificationForSan(String san, ApiBoard board)
      throws SanValidationException {
    final SanParse sanParse = validateNonPositionRelatedStatically(san, board.getHavingMove());
    return validatePositionRelated(board, sanParse);
  }

  private static MoveSpecification calculateMoveSpecificationForSan(ApiBoard board, Side havingMove,
      SanFormat sanFormat, SanConversion sanConversion, MoveSpecification legalMoveOnlyCandidate) {

    if (sanFormat == SanFormat.KING_CASTLING_QUEEN_SIDE_FORMAT) {
      return new MoveSpecification(havingMove, CastlingMove.QUEEN_SIDE);
    }
    if (sanFormat == SanFormat.KING_CASTLING_KING_SIDE_FORMAT) {
      return new MoveSpecification(havingMove, CastlingMove.KING_SIDE);
    }

    final Square toSquare = sanConversion.toSquare();

    switch (sanFormat) {
      case KING_CASTLING_QUEEN_SIDE_FORMAT:
      case KING_CASTLING_KING_SIDE_FORMAT:
        throw new ProgrammingMistakeException("Castling is handled before switch");
      case PAWN_NON_CAPTURING_NON_PROMOTION_FORMAT:
        if (!Rank.calculateIsPawnTwoSquareAdvanceRank(havingMove, toSquare.getRank())) {
          // one square advance, san information is enough
          // from file equals to file and from rank is the rank before to rank
          final File fromFile = toSquare.getFile(); // moving straight forward
          final Rank fromRank = Rank.calculatePreviousRank(havingMove, toSquare.getRank());
          final Square fromSquare = Square.calculate(fromFile, fromRank);
          return new MoveSpecification(havingMove, fromSquare, toSquare);
        }
        // we calculate this with san information and knowing it's a legal move (so e4
        // is e2-e4 xor e3-e4)
        final Square potentialJumpOverSquare = Square.calculateJumpOverSquare(havingMove, toSquare);
        if (board.getStaticPosition().get(potentialJumpOverSquare) == Piece.NONE) {
          // two square advance
          final File fromFile = toSquare.getFile(); // moving straight forward
          final Rank fromRank = Rank.calculatePawnInitialRank(havingMove);
          final Square fromSquare = Square.calculate(fromFile, fromRank);
          return new MoveSpecification(havingMove, fromSquare, toSquare);
        }

      {
        // one square advance
        final var fromSquare = potentialJumpOverSquare;
        return new MoveSpecification(havingMove, fromSquare, toSquare);
      }
      case PAWN_CAPTURING_NON_PROMOTION_FORMAT: {
        // from file is in the san and from rank is the rank before to rank

        final Rank fromRank = Rank.calculatePreviousRank(havingMove, toSquare.getRank());
        final Square fromSquare = Square.calculate(sanConversion.fromFile(), fromRank);
        return new MoveSpecification(havingMove, fromSquare, toSquare);
      }
      case PAWN_NON_CAPTURING_PROMOTION_FORMAT: {
        // from file equals to file and from rank is the rank before to rank
        final File fromFile = toSquare.getFile(); // moving straight forward
        final Rank fromRank = Rank.calculatePreviousRank(havingMove, toSquare.getRank());
        final Square fromSquare = Square.calculate(fromFile, fromRank);
        return new MoveSpecification(havingMove, fromSquare, toSquare, sanConversion.promotionPieceType());
      }
      case PAWN_CAPTURING_PROMOTION_FORMAT: {
        // from file is in the san and from rank is the rank before to rank
        final Rank fromRank = Rank.calculatePreviousRank(havingMove, toSquare.getRank());
        final Square fromSquare = Square.calculate(sanConversion.fromFile(), fromRank);
        return new MoveSpecification(havingMove, fromSquare, toSquare, sanConversion.promotionPieceType());
      }
      case PIECE_CAPTURING_SQUARE_FORMAT: {
        // san is enough to determine from square
        final Square fromSquare = AbstractSan.calculateFromSquare(sanConversion);
        return new MoveSpecification(havingMove, fromSquare, toSquare);
      }
      case KING_NON_CASTLING_CAPTURING_FORMAT:
      case KING_NON_CASTLING_NON_CAPTURING_FORMAT:
      case PIECE_CAPTURING_FILE_FORMAT:
      case PIECE_CAPTURING_NEITHER_FORMAT:
      case PIECE_CAPTURING_RANK_FORMAT:
      case PIECE_NON_CAPTURING_SQUARE_FORMAT:
      case PIECE_NON_CAPTURING_FILE_FORMAT:
      case PIECE_NON_CAPTURING_NEITHER_FORMAT:
      case PIECE_NON_CAPTURING_RANK_FORMAT: {
        // legal move is required to determine from square
        final Square fromSquare = legalMoveOnlyCandidate.fromSquare();
        return new MoveSpecification(havingMove, fromSquare, toSquare);
      }
      default:
        throw new IllegalArgumentException();

    }
  }

  private static MoveSpecification validatePositionRelated(ApiBoard board, SanParse sanParse)
      throws SanValidationException {

    final Side havingMove = board.getHavingMove();
    final SanType sanType = sanParse.sanType();
    final SanConversion sanConversion = sanParse.sanConversion();
    final SanFormat sanFormat = sanType.getSanFormat();

    validateMovingOntoOwnPiece(havingMove, sanFormat, sanConversion, board.getStaticPosition());
    validateNoCaptureIsNoCapture(havingMove, sanFormat, sanConversion, board.getStaticPosition());

    // check if designated captures are captures
    validateCaptureIsCapture(board, havingMove, sanType, sanConversion);

    // validate san against legal moves
    // (1) if the san specifies a possible legal move
    // (2) if the san specifies the move uniquely
    // (3) if the san contains non unnecessary file or rank information for the from square
    // (4) if the san uses rank specification instead of file specification

    final Set<LegalMove> filterLegalMovesForValidation = filterLegalMovesForValidation(board, havingMove, sanParse);
    validateAgainstLegalMoves(havingMove, filterLegalMovesForValidation, sanType, sanConversion);

    // eight step - we now construct the move and check against the identified legal
    // move it represents
    // (1) we filter the legal moves for san which leaves the only possible legal
    // move
    // (2) we construct the move from the san, here we need the only possible legal
    // move for some cases
    // (3) we check that the only possible legal moves and the constructed moves are
    // the same!
    final LegalMove legalMoveOnlyCandidate = calculateOnlyPossibleLegalMove(sanFormat, sanConversion,
        filterLegalMovesForValidation);
    final MoveSpecification moveSpecification = calculateMoveSpecificationForSan(board, havingMove, sanFormat,
        sanConversion, legalMoveOnlyCandidate.moveSpecification());
    if (!moveSpecification.equals(legalMoveOnlyCandidate.moveSpecification())) {
      throw new ProgrammingMistakeException("A mistake happened in the move construction");
    }

    validateCheckmateOrCheck(board, sanConversion.checkmateOrCheck(), moveSpecification);

    return moveSpecification;
  }

  private static void validateCheckmateOrCheck(ApiBoard board, CheckmateOrCheck sanCheckmateOrCheck,
      MoveSpecification moveSpecification) {
    final CheckmateOrCheck boardCheckmateOrCheck = calculateBoardCheckmateOrCheck(board, moveSpecification);

    switch (sanCheckmateOrCheck) {
      case CHECKMATE:
        switch (boardCheckmateOrCheck) {
          case CHECKMATE:
            return;
          case CHECK:
            throw new SanValidationException(SanValidationProblem.CHECKMATE_BUT_CHECK_ONLY,
                "It's check only but checkmate is specified");
          case NONE:
            throw new SanValidationException(SanValidationProblem.CHECKMATE_BUT_NONE,
                "It's not checkmate but checkmate is specified");
          default:
            throw new IllegalArgumentException();
        }
      case CHECK:
        switch (boardCheckmateOrCheck) {
          case CHECKMATE:
            throw new SanValidationException(SanValidationProblem.CHECK_BUT_CHECKMATE,
                "It's checkmate but check is specified");
          case CHECK:
            break;
          case NONE:
            throw new SanValidationException(SanValidationProblem.CHECK_BUT_NONE,
                "It's not check but check is specified");
          default:
            throw new IllegalArgumentException();
        }
        break;
      case NONE:
        switch (boardCheckmateOrCheck) {
          case CHECKMATE:
            throw new SanValidationException(SanValidationProblem.NONE_BUT_CHECKMATE,
                "It's checkmate but that is not specified");
          case CHECK:
            throw new SanValidationException(SanValidationProblem.NONE_BUT_CHECK,
                "It's check but that is not specified");
          case NONE:
            break;
          default:
            throw new IllegalArgumentException();
        }
        break;
      default:
        throw new IllegalArgumentException();
    }
  }

  private static CheckmateOrCheck calculateBoardCheckmateOrCheck(ApiBoard board, MoveSpecification moveSpecification) {
    board.performMove(moveSpecification);

    CheckmateOrCheck checkmateOrCheck;
    if (board.isCheckmate()) {
      checkmateOrCheck = CheckmateOrCheck.CHECKMATE;
    } else if (board.isCheck()) {
      checkmateOrCheck = CheckmateOrCheck.CHECK;
    } else {
      checkmateOrCheck = CheckmateOrCheck.NONE;
    }
    board.unperformMove();

    return checkmateOrCheck;
  }

  public static SanParse validateNonPositionRelatedStatically(String san, Side havingMove)
      throws SanValidationException {

    final var sanParse = SanValidateStaticallyFormat.get(san);
    if (sanParse != null) {
      validateNonPositionRelatedExtended(sanParse, havingMove);
      return sanParse;
    }

    // in case it is invalid we run the parsing which will throw custom exceptions
    // the validations are checked to be exactly the same (statically and runtime), so we can do this
    SanValidateFormat.validateFormat(san);

    throw new ProgrammingMistakeException("The static and runtime format validation are not equal");
  }

  public static SanParse validateNonPositionRelatedExtended(String san, Side havingMove) throws SanValidationException {

    final var sanParse = SanValidateFormat.validateFormat(san);

    return validateNonPositionRelatedExtended(sanParse, havingMove);
  }

  public static SanParse validateNonPositionRelatedExtended(SanParse sanParse, Side havingMove)
      throws SanValidationException {

    // check for moving onto itself
    SanValidateMove.validateMovingOntoItself(sanParse);

    // check for invalid movements
    // pieces which can never move from the specified file, rank or square to the destination square
    // pawns which moves forwards or diagonally to ranks they can never move to
    // pawns which captures from non adjacent files
    // pawns which promote on non promotion ranks
    SanValidateMove.validateMovement(havingMove, sanParse);
    SanValidateMove.validatePromotion(havingMove, sanParse);

    return sanParse;
  }

  private static void validateMovingOntoOwnPiece(Side havingMove, SanFormat sanFormat, SanConversion sanConversion,
      StaticPosition staticPosition) {
    switch (sanFormat) {
      case KING_CASTLING_KING_SIDE_FORMAT:
      case KING_CASTLING_QUEEN_SIDE_FORMAT:
        return;
      case KING_NON_CASTLING_CAPTURING_FORMAT:
      case PAWN_CAPTURING_NON_PROMOTION_FORMAT:
      case PAWN_CAPTURING_PROMOTION_FORMAT:
      case PIECE_CAPTURING_SQUARE_FORMAT:
      case PIECE_CAPTURING_FILE_FORMAT:
      case PIECE_CAPTURING_NEITHER_FORMAT:
      case PIECE_CAPTURING_RANK_FORMAT:
      case KING_NON_CASTLING_NON_CAPTURING_FORMAT:
      case PAWN_NON_CAPTURING_NON_PROMOTION_FORMAT:
      case PAWN_NON_CAPTURING_PROMOTION_FORMAT:
      case PIECE_NON_CAPTURING_SQUARE_FORMAT:
      case PIECE_NON_CAPTURING_FILE_FORMAT:
      case PIECE_NON_CAPTURING_NEITHER_FORMAT:
      case PIECE_NON_CAPTURING_RANK_FORMAT:
        // only in non castling case we can calculate the to square!
        final Square toSquare = sanConversion.toSquare();
        if (staticPosition.isOwnPiece(toSquare, havingMove)) {
          throw new SanValidationException(SanValidationProblem.MOVING_ONTO_ONE_PIECE,
              Message.getString("validation.san.allExceptCastling.movingOntoOwnPiece", toSquare.getName()));
        }
        break;
      default:
        throw new IllegalArgumentException();

    }
  }

  private static void validateCaptureIsCapture(ApiBoard board, Side havingMove, SanType sanType,
      SanConversion sanConversion) {
    final SanFormat sanFormat = sanType.getSanFormat();
    final var isCapture = SanFormat.calculateIsCapture(sanFormat);
    if (isCapture) {
      // here only we are outside castling, so we can calculate the to square!
      final Square toSquare = sanConversion.toSquare();
      final Piece pieceOnToSquare = board.getStaticPosition().get(toSquare);
      final boolean isEnPassantCapture;
      if (sanType == SanType.PAWN_CAPTURING_NON_PROMOTION_MOVE) {
        final Rank fromRank = Rank.calculatePreviousRank(havingMove, toSquare.getRank());
        final Square fromSquare = Square.calculate(sanConversion.fromFile(), fromRank);
        final MoveSpecification pawnCapturingNonPromotionMove = new MoveSpecification(havingMove, fromSquare, toSquare);
        isEnPassantCapture = EnPassantCaptureUtility.calculateIsEnPassantCapture(board.getStaticPosition(),
            pawnCapturingNonPromotionMove);
      } else {
        isEnPassantCapture = false;
      }
      if (isEnPassantCapture) {
        if (pieceOnToSquare != Piece.NONE) {
          // double check
          throw new ProgrammingMistakeException(
              "For a move identified as en passant capture the to square must be empty, but is not");
        }
      } else {
        // not a capture capture
        if (pieceOnToSquare == Piece.NONE) {
          throw new SanValidationException(SanValidationProblem.CAPTURING_NO_PIECE,
              Message.getString("validation.san.allExceptCastling.captureIsNoCapture", toSquare.getName()));
        }
        if (pieceOnToSquare.getSide() == havingMove.getOppositeSide() && pieceOnToSquare.getPieceType() == KING) {
          throw new ProgrammingMistakeException(
              "Capturing the opponent king requires making a move from an illegal position and that is not supported");
        }
      }
    }
  }

  private static Set<LegalMove> filterLegalMovesForValidation(ApiBoard board, Side havingMove, SanParse sanParse) {
    final SanType sanType = sanParse.sanType();
    final SanFormat sanFormat = sanType.getSanFormat();

    // for castling we need to filter the castling moves
    if (SanFormat.calculateIsKingCastlingMove(sanFormat)) {
      return filterCastlingMove(board.getLegalMoveSet());
    }

    final PieceType pieceType = sanType.getMovingPieceType();
    final Piece piece = PieceType.calculate(havingMove, pieceType);

    final Set<LegalMove> legalMoveSetForMovingPiece = MoveToSan.calculateLegalMoveSetForMovingPiece(piece,
        board.getLegalMoveSet());
    // for non castling moves we need to filter by the to square (which is always set for non castling)
    final Square toSquare = sanParse.sanConversion().toSquare();
    final Set<LegalMove> filteredLegalMovesForToSquare = filterLegalMovesCandidates(legalMoveSetForMovingPiece,
        toSquare);

    // for pawn moves we must filter additionally by the from file!!
    if (sanType == SanType.PAWN_CAPTURING_NON_PROMOTION_MOVE || sanType == SanType.PAWN_CAPTURING_PROMOTION_MOVE) {
      return filterLegalMovesCandidates(filteredLegalMovesForToSquare, sanParse.sanConversion().fromFile());
    }
    return filteredLegalMovesForToSquare;
  }

  private static Set<LegalMove> filterCastlingMove(Set<LegalMove> allLegalMoves) {
    final Set<LegalMove> filteredLegalMoves = new TreeSet<>();
    for (final LegalMove legalMove : allLegalMoves) {
      if (CastlingUtility.calculateIsCastlingMove(legalMove.moveSpecification())) {
        filteredLegalMoves.add(legalMove);
      }
    }
    return filteredLegalMoves;
  }

  private static LegalMove calculateOnlyPossibleLegalMove(SanFormat sanFormat, SanConversion sanConversion,
      Set<LegalMove> legalMovesForSanValidation) {

    final Set<LegalMove> filtered0 = legalMovesForSanValidation;

    final Set<LegalMove> filtered1 = filterLegalMovesCandidatesForFrom(sanFormat, sanConversion, filtered0);

    final Set<LegalMove> filtered2 = filterLegalMovesCandidatesForPromotion(sanFormat, sanConversion, filtered1);

    final Set<LegalMove> filtered3 = filterLegalMovesCandidatesForCastling(sanFormat, filtered2);

    if (filtered3.size() != 1) {
      throw new ProgrammingMistakeException(
          "At this point it is expected that filtering the legal moves against the SAN result in exactly one legal move");
    }
    return calculateOnlyElement(filtered3);
  }

  private static void validateNoCaptureIsNoCapture(Side havingMove, SanFormat sanFormat, SanConversion sanConversion,
      StaticPosition staticPosition) {

    switch (sanFormat) {
      case KING_CASTLING_KING_SIDE_FORMAT:
      case KING_CASTLING_QUEEN_SIDE_FORMAT:
        return;
      case KING_NON_CASTLING_CAPTURING_FORMAT:
      case PAWN_CAPTURING_NON_PROMOTION_FORMAT:
      case PAWN_CAPTURING_PROMOTION_FORMAT:
      case PIECE_CAPTURING_SQUARE_FORMAT:
      case PIECE_CAPTURING_FILE_FORMAT:
      case PIECE_CAPTURING_NEITHER_FORMAT:
      case PIECE_CAPTURING_RANK_FORMAT:
        return;
      case KING_NON_CASTLING_NON_CAPTURING_FORMAT:
      case PAWN_NON_CAPTURING_NON_PROMOTION_FORMAT:
      case PAWN_NON_CAPTURING_PROMOTION_FORMAT:
      case PIECE_NON_CAPTURING_SQUARE_FORMAT:
      case PIECE_NON_CAPTURING_FILE_FORMAT:
      case PIECE_NON_CAPTURING_NEITHER_FORMAT:
      case PIECE_NON_CAPTURING_RANK_FORMAT:
        // only in non castling case we can calculate the to square!
        final Square toSquare = sanConversion.toSquare();
        if (staticPosition.isOpponentPiece(toSquare, havingMove)) {
          throw new SanValidationException(SanValidationProblem.NON_CAPTURING_MOVING_ONTO_OPPONENT_PIECE,
              Message.getString("validation.san.allExceptCastling.noCaptureIsCapture", toSquare.getName()));
        }
        break;
      default:
        throw new IllegalArgumentException();

    }
  }

  private static void validateAgainstLegalMoves(Side havingMove, Set<LegalMove> legalMovesForValidation,
      SanType sanType, SanConversion sanConversion) {

    // we need an early return for castling first so for the remaining cases we can
    // calculate the to square
    final SanFormat sanFormat = sanType.getSanFormat();
    if (sanFormat == SanFormat.KING_CASTLING_QUEEN_SIDE_FORMAT) {
      if (!isContained(legalMovesForValidation, havingMove, sanFormat)) {
        throw new SanValidationException(SanValidationProblem.KING_CASTLING_QUEEN_SIDE_NOT_POSSIBLE,
            Message.getString("validation.san.castling.queenSide"));
      }
      return;
    }
    if (sanFormat == SanFormat.KING_CASTLING_KING_SIDE_FORMAT) {
      if (!isContained(legalMovesForValidation, havingMove, sanFormat)) {
        throw new SanValidationException(SanValidationProblem.KING_CASTLING_KING_SIDE_NOT_POSSIBLE,
            Message.getString("validation.san.castling.kingSide"));
      }
      return;
    }

    // only in non castling case we can calculate the to square!
    final Square toSquare = sanConversion.toSquare();
    final PieceType pieceType = sanType.getMovingPieceType();

    switch (sanFormat) {
      case KING_CASTLING_QUEEN_SIDE_FORMAT:
      case KING_CASTLING_KING_SIDE_FORMAT:
        throw new ProgrammingMistakeException("Invalid program flow, the castling must be handled at this point");
      case KING_NON_CASTLING_NON_CAPTURING_FORMAT:
      case KING_NON_CASTLING_CAPTURING_FORMAT:
        if (legalMovesForValidation.isEmpty()) {
          throw new SanValidationException(SanValidationProblem.KING_NON_CASTLING_NO_LEGAL_MOVE,
              Message.getString("validation.san.notPawn.specification.none.king.noLegalMove", toSquare.getName()));
        }
        break;
      case PAWN_NON_CAPTURING_NON_PROMOTION_FORMAT:
      case PAWN_CAPTURING_NON_PROMOTION_FORMAT:
        if (legalMovesForValidation.isEmpty()) {
          throw new SanValidationException(SanValidationProblem.PAWN_NON_PROMOTION_NO_LEGAL_MOVE,
              Message.getString("validation.san.pawn.noLegalMove", pieceType.getName(), toSquare.getName()));
        }
        break;
      case PAWN_NON_CAPTURING_PROMOTION_FORMAT:
      case PAWN_CAPTURING_PROMOTION_FORMAT:
        if (legalMovesForValidation.isEmpty()) {
          throw new SanValidationException(SanValidationProblem.PAWN_PROMOTION_NO_LEGAL_MOVE,
              Message.getString("validation.san.pawn.noLegalMove", pieceType.getName(), toSquare.getName()));
        }
        break;
      case PIECE_NON_CAPTURING_NEITHER_FORMAT:
      case PIECE_CAPTURING_NEITHER_FORMAT:
        // if no legal move from any square throw an exception (no legal move)
        if (legalMovesForValidation.isEmpty()) {
          throw new SanValidationException(SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE,
              Message.getString("validation.san.notPawn.specification.none.otherThanKing.noLegalMove",
                  pieceType.getName(), toSquare.getName()));
        }
        // if more than one legal move from any square throw an exception (ambiguous)
        if (legalMovesForValidation.size() > 1) {
          throw new SanValidationException(SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES,
              Message.getString("validation.san.notPawn.specification.none.moreThanOneLegalMove", pieceType.getName(),
                  toSquare.getName()));
        }
        break;
      case PIECE_NON_CAPTURING_FILE_FORMAT:
      case PIECE_CAPTURING_FILE_FORMAT: {
        // if no legal move from specified file throw an exception (no legal move)
        if (calculateNumberOfLegalMovesFromSameFile(sanConversion.fromFile(), legalMovesForValidation) == 0) {
          throw new SanValidationException(SanValidationProblem.PIECE_FILE_NO_LEGAL_MOVE,
              Message.getString("validation.san.noLegalMove.fromFile", pieceType.getName(),
                  sanConversion.fromFile().getLetter(), toSquare.getName()));
        }
        // we have: there is a piece on specified file
        // if only one legal move from any square throw an exception (file not required as only move)
        if (legalMovesForValidation.size() == 1) {
          throw new SanValidationException(SanValidationProblem.PIECE_FILE_ONLY_ONE_LEGAL_MOVE,
              Message.getString("validation.san.overspecification.file", pieceType.getName(), toSquare.getName()));
        }
        // we have: there is a piece on specified file
        // we have: there is more than one legal move
        // if the file is the only file having legal moves throw an exception (use rank)
        final var numberOfLegalMovesFromSameFile = calculateNumberOfLegalMovesFromSameFile(sanConversion.fromFile(),
            legalMovesForValidation);
        if (!calculateHasOtherFilesHavingLegalMoves(sanConversion.fromFile(), legalMovesForValidation)) {
          // now we must have more than one legal move from the file, for we have total more than one legal moves and
          // only moves from the file
          if (numberOfLegalMovesFromSameFile < 2) {
            throw new ProgrammingMistakeException("A programming assumption about the rank turned out to be wrong");
          }
          // TODO plural - how to address
          // TODO today test cases for all situations?? are they already there???
          throw new SanValidationException(SanValidationProblem.PIECE_FILE_MUST_USE_RANK,
              Message.getString("validation.san.notDetermined.byFile", pieceType.getName(),
                  sanConversion.fromFile().getLetter(), toSquare.getName()));
        }
        // we have: there is a piece on specified file
        // we have: there is more than one legal move
        // we have: there are also legal moves from other files possible
        // if more than one legal move from the file throw an exception (rank/file or square needed)
        if (numberOfLegalMovesFromSameFile >= 2) {
          if (pieceType == ROOK) {
            // because rooks move orthogonal only in this case again the rank must be used
            throw new SanValidationException(SanValidationProblem.PIECE_FILE_MUST_USE_RANK,
                Message.getString("validation.san.notDetermined.byFile", pieceType.getName(),
                    sanConversion.fromFile().getLetter(), toSquare.getName()));
          }
          throw new SanValidationException(SanValidationProblem.PIECE_FILE_MUST_USE_RANK_OR_SQUARE,
              Message.getString("validation.san.notDetermined.byFile", pieceType.getName(),
                  sanConversion.fromFile().getLetter(), toSquare.getName()));

        }
        // we have: there is a piece on specified file
        // we have: there is more than one legal move
        // we have: there are also legal moves from other files possible
        // we have: there is only one move possible from the file
        // we are ok
      }
        break;
      case PIECE_NON_CAPTURING_RANK_FORMAT:
      case PIECE_CAPTURING_RANK_FORMAT: {
        // if no legal move from specified rank throw an exception (no legal move)
        if (calculateNumberOfLegalMovesFromSameRank(sanConversion.fromRank(), legalMovesForValidation) == 0) {
          throw new SanValidationException(SanValidationProblem.PIECE_RANK_NO_LEGAL_MOVE,
              Message.getString("validation.san.noLegalMove.fromRank", pieceType.getName(),
                  NonNullWrapperCommon.valueOf(sanConversion.fromRank().getNumber()), toSquare.getName()));
        }
        // we have: there is a piece on specified rank
        // if only one legal move from any square throw an exception (rank not required as only move)
        if (legalMovesForValidation.size() == 1) {
          throw new SanValidationException(SanValidationProblem.PIECE_RANK_ONLY_ONE_LEGAL_MOVE,
              Message.getString("validation.san.overspecification.rank", pieceType.getName(), toSquare.getName()));
        }
        // we have: there is a piece on specified rank
        // we have: there is more than one legal move
        // if the rank is the only rank having legal moves throw an exception (use file)
        final var numberOfLegalMovesFromSameRank = calculateNumberOfLegalMovesFromSameRank(sanConversion.fromRank(),
            legalMovesForValidation);
        if (!calculateHasOtherRanksHavingLegalMoves(sanConversion.fromRank(), legalMovesForValidation)) {
          // now we must have more than one legal move from the file, for we have total more than one legal moves and
          // only moves from the file
          if (numberOfLegalMovesFromSameRank < 2) {
            throw new ProgrammingMistakeException("A programming assumption about the file turned out to be wrong");
          }
          throw new SanValidationException(SanValidationProblem.PIECE_RANK_MUST_USE_FILE,
              Message.getString("validation.san.notDetermined.byRank", pieceType.getName(),
                  NonNullWrapperCommon.valueOf(sanConversion.fromRank().getNumber()), toSquare.getName()));
        }
        // we have: there is a piece on specified rank
        // we have: there is more than one legal move
        // we have: there are also legal moves from other ranks possible
        // if more than one legal move from the rank throw an exception (rank/file or square needed)
        if (numberOfLegalMovesFromSameRank >= 2) {
          throw new SanValidationException(SanValidationProblem.PIECE_RANK_MUST_USE_FILE_OR_SQUARE,
              Message.getString("validation.san.notDetermined.byRank", pieceType.getName(),
                  NonNullWrapperCommon.valueOf(sanConversion.fromRank().getNumber()), toSquare.getName()));

        }
        // we have: there is a piece on specified rank
        // we have: there is more than one legal move
        // we have: there are also legal moves from other ranks possible
        // we have: there is only one move possible from the rank
        // we need to check if the file could be used instead

        File onlyPossibleFromFile = calculateOnlyPossibleFile(legalMovesForValidation, sanConversion);
        for (final LegalMove legalMove : legalMovesForValidation) {
          if (legalMove.moveSpecification().fromSquare().getRank() == sanConversion.fromRank()) {
            onlyPossibleFromFile = legalMove.moveSpecification().fromSquare().getFile();
            break;
          }
        }
        if (onlyPossibleFromFile == File.NONE) {
          throw new ProgrammingMistakeException(
              "The program made the wrong assumption that the from file is determined at this point");
        }
        final var numberOfLegalMovesFromSameFile = calculateNumberOfLegalMovesFromSameFile(onlyPossibleFromFile,
            legalMovesForValidation);

        if (numberOfLegalMovesFromSameFile == 1) {
          throw new SanValidationException(SanValidationProblem.PIECE_RANK_MUST_USE_FILE,
              Message.getString("validation.san.rankInsteadOfFileUsed"));
        }
      }
        break;
      case PIECE_NON_CAPTURING_SQUARE_FORMAT:
      case PIECE_CAPTURING_SQUARE_FORMAT: {
        // if no legal move from specified square throw an exception (no legal move)
        final var numberOfLegalMovesFromSameFile = calculateNumberOfLegalMovesFromSameFile(sanConversion.fromFile(),
            legalMovesForValidation);
        final var numberOfLegalMovesFromSameRank = calculateNumberOfLegalMovesFromSameRank(sanConversion.fromRank(),
            legalMovesForValidation);
        final Square fromSquare = calculateFromSquare(sanConversion);

        if (numberOfLegalMovesFromSameFile == 0 || numberOfLegalMovesFromSameRank == 0) {
          throw new SanValidationException(SanValidationProblem.PIECE_SQUARE_NO_LEGAL_MOVE, Message.getString(
              "validation.san.noLegalMove.fromFile", pieceType.getName(), fromSquare.getName(), toSquare.getName()));
        }

        // we have: there is a piece on specified square
        // if only one legal move from any square throw an exception (square not required as only move)
        if (legalMovesForValidation.size() == 1) {
          throw new SanValidationException(SanValidationProblem.PIECE_SQUARE_ONLY_ONE_LEGAL_MOVE,
              Message.getString("validation.san.overspecification.square", pieceType.getName(), toSquare.getName()));
        }

        // we have: there is a piece on specified square
        // we have: there is more than one legal move
        // if the file is having only one piece throw an exception (file is enough)
        if (numberOfLegalMovesFromSameFile == 1) {
          throw new SanValidationException(SanValidationProblem.PIECE_SQUARE_ONLY_ONE_PIECE_ON_FILE,
              "The san is not valid, because there is only one piece in the file");
        }

        // we have: there is a piece on specified square
        // we have: there is more than one legal move
        // we have: there is more than one piece on the same file
        // if the rank is having only one piece throw an exception (rank is enough)
        if (numberOfLegalMovesFromSameRank == 1) {
          throw new SanValidationException(SanValidationProblem.PIECE_SQUARE_ONLY_ONE_PIECE_ON_RANK,
              "The san is not valid, because there is only one piece on the same rank");
        }
      }
        break;
      default:
        throw new IllegalArgumentException();
    }
  }

  private static Set<LegalMove> filterLegalMovesCandidatesForFrom(SanFormat sanFormat, SanConversion sanConversion,
      Set<LegalMove> legalMoveSet) {

    switch (sanFormat) {
      case KING_CASTLING_QUEEN_SIDE_FORMAT:
      case KING_CASTLING_KING_SIDE_FORMAT:
      case KING_NON_CASTLING_CAPTURING_FORMAT:
      case KING_NON_CASTLING_NON_CAPTURING_FORMAT:
        // no from restriction
        return new TreeSet<>(legalMoveSet);
      // $CASES-OMITTED$
      default:
        break;
    }

    final Set<LegalMove> legalMovesForFrom = new TreeSet<>();

    // always set for non castling
    final File sanFromFile = sanConversion.fromFile();
    // attention - empty for some san formats
    final Rank sanFromRank = sanConversion.fromRank();

    for (final LegalMove moveCandidate : legalMoveSet) {
      final File candidateFromFile = moveCandidate.moveSpecification().fromSquare().getFile();
      final Rank candidateFromRank = moveCandidate.moveSpecification().fromSquare().getRank();

      final var isFromFileMatch = candidateFromFile == sanFromFile;
      // attention does not make sense for all san formats
      final var isFromRankMatch = candidateFromRank == sanFromRank;

      switch (sanFormat) {
        case KING_CASTLING_QUEEN_SIDE_FORMAT:
        case KING_CASTLING_KING_SIDE_FORMAT:
        case KING_NON_CASTLING_CAPTURING_FORMAT:
        case KING_NON_CASTLING_NON_CAPTURING_FORMAT:
          throw new ProgrammingMistakeException("Handled before");
        case PAWN_CAPTURING_NON_PROMOTION_FORMAT:
        case PAWN_CAPTURING_PROMOTION_FORMAT:
          if (isFromFileMatch) {
            legalMovesForFrom.add(moveCandidate);
          }
          break;
        case PAWN_NON_CAPTURING_NON_PROMOTION_FORMAT:
        case PAWN_NON_CAPTURING_PROMOTION_FORMAT:
          // no from restriction
          legalMovesForFrom.add(moveCandidate);
          break;
        case PIECE_NON_CAPTURING_NEITHER_FORMAT:
        case PIECE_CAPTURING_NEITHER_FORMAT:
          // no from restriction
          legalMovesForFrom.add(moveCandidate);
          break;
        case PIECE_NON_CAPTURING_FILE_FORMAT:
        case PIECE_CAPTURING_FILE_FORMAT:
          if (isFromFileMatch) {
            legalMovesForFrom.add(moveCandidate);
          }
          break;
        case PIECE_NON_CAPTURING_RANK_FORMAT:
        case PIECE_CAPTURING_RANK_FORMAT:
          if (isFromRankMatch) {
            legalMovesForFrom.add(moveCandidate);
          }
          break;
        case PIECE_NON_CAPTURING_SQUARE_FORMAT:
        case PIECE_CAPTURING_SQUARE_FORMAT:
          if (isFromFileMatch && isFromRankMatch) {
            legalMovesForFrom.add(moveCandidate);
          }
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
    return legalMovesForFrom;
  }

  private static Set<LegalMove> filterLegalMovesCandidatesForPromotion(SanFormat sanFormat, SanConversion sanConversion,
      Set<LegalMove> legalMoveSet) {
    final Set<LegalMove> legalMovesForPromotion = new TreeSet<>();
    for (final LegalMove moveCandidate : legalMoveSet) {
      switch (sanFormat) {
        case KING_CASTLING_KING_SIDE_FORMAT:
        case KING_CASTLING_QUEEN_SIDE_FORMAT:
        case KING_NON_CASTLING_CAPTURING_FORMAT:
        case KING_NON_CASTLING_NON_CAPTURING_FORMAT:
        case PAWN_CAPTURING_NON_PROMOTION_FORMAT:
        case PAWN_NON_CAPTURING_NON_PROMOTION_FORMAT:
        case PIECE_CAPTURING_SQUARE_FORMAT:
        case PIECE_CAPTURING_FILE_FORMAT:
        case PIECE_CAPTURING_NEITHER_FORMAT:
        case PIECE_CAPTURING_RANK_FORMAT:
        case PIECE_NON_CAPTURING_SQUARE_FORMAT:
        case PIECE_NON_CAPTURING_FILE_FORMAT:
        case PIECE_NON_CAPTURING_NEITHER_FORMAT:
        case PIECE_NON_CAPTURING_RANK_FORMAT:
          legalMovesForPromotion.add(moveCandidate);
          break;
        case PAWN_CAPTURING_PROMOTION_FORMAT:
        case PAWN_NON_CAPTURING_PROMOTION_FORMAT:
          if (moveCandidate.moveSpecification().promotionPieceType() == sanConversion.promotionPieceType()) {
            legalMovesForPromotion.add(moveCandidate);
          }
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
    return legalMovesForPromotion;
  }

  private static Set<LegalMove> filterLegalMovesCandidatesForCastling(SanFormat sanFormat,
      Set<LegalMove> legalMoveSet) {
    final Set<LegalMove> legalMovesForCastling = new TreeSet<>();
    for (final LegalMove moveCandidate : legalMoveSet) {
      switch (sanFormat) {
        case KING_CASTLING_KING_SIDE_FORMAT:
          if (moveCandidate.moveSpecification().castlingMove() == CastlingMove.KING_SIDE) {
            legalMovesForCastling.add(moveCandidate);
          }
          break;
        case KING_CASTLING_QUEEN_SIDE_FORMAT:
          if (moveCandidate.moveSpecification().castlingMove() == CastlingMove.QUEEN_SIDE) {
            legalMovesForCastling.add(moveCandidate);
          }
          break;
        case KING_NON_CASTLING_CAPTURING_FORMAT:
        case KING_NON_CASTLING_NON_CAPTURING_FORMAT:
        case PAWN_CAPTURING_NON_PROMOTION_FORMAT:
        case PAWN_NON_CAPTURING_NON_PROMOTION_FORMAT:
        case PIECE_CAPTURING_SQUARE_FORMAT:
        case PIECE_CAPTURING_FILE_FORMAT:
        case PIECE_CAPTURING_NEITHER_FORMAT:
        case PIECE_CAPTURING_RANK_FORMAT:
        case PIECE_NON_CAPTURING_SQUARE_FORMAT:
        case PIECE_NON_CAPTURING_FILE_FORMAT:
        case PIECE_NON_CAPTURING_NEITHER_FORMAT:
        case PIECE_NON_CAPTURING_RANK_FORMAT:
        case PAWN_CAPTURING_PROMOTION_FORMAT:
        case PAWN_NON_CAPTURING_PROMOTION_FORMAT:
          legalMovesForCastling.add(moveCandidate);
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
    return legalMovesForCastling;
  }

  private static LegalMove calculateOnlyElement(Set<LegalMove> moveSet) {
    if (moveSet.size() != 1) {
      throw new IllegalArgumentException("The set must contain exactly one element");
    }
    return NonNullWrapperCommon.getFirst(new ArrayList<>(moveSet));
  }

  private static File calculateOnlyPossibleFile(Set<LegalMove> legalMovesForSanValidation,
      SanConversion sanConversion) {
    var countMatches = 0;
    for (final LegalMove legalMove : legalMovesForSanValidation) {
      if (legalMove.moveSpecification().fromSquare().getRank() == sanConversion.fromRank()) {
        countMatches++;
      }
    }
    if (countMatches != 1) {
      throw new ProgrammingMistakeException(
          "The program made the wrong assumption that the from file is determined at this point");
    }

    // now return first match, which is the only match
    for (final LegalMove legalMove : legalMovesForSanValidation) {
      if (legalMove.moveSpecification().fromSquare().getRank() == sanConversion.fromRank()) {
        return legalMove.moveSpecification().fromSquare().getFile();
      }
    }
    throw new ProgrammingMistakeException("The program in mistake failed to determine the file");
  }

  private static boolean isContained(Set<LegalMove> legalMoves, Side havingMove, SanFormat sanFormat) {
    return legalMoves.contains(calculateCastlingMove(havingMove, sanFormat));
  }

  private static LegalMove calculateCastlingMove(Side havingMove, SanFormat sanFormat) {
    switch (havingMove) {
      case WHITE:
        if (sanFormat == SanFormat.KING_CASTLING_KING_SIDE_FORMAT) {
          return CastlingConstants.WHITE_KING_SIDE_CASTLING_MOVE;
        }
        if (sanFormat == SanFormat.KING_CASTLING_QUEEN_SIDE_FORMAT) {
          return CastlingConstants.WHITE_QUEEN_SIDE_CASTLING_MOVE;
        }
        throw new IllegalArgumentException();
      case BLACK:
        if (sanFormat == SanFormat.KING_CASTLING_KING_SIDE_FORMAT) {
          return CastlingConstants.BLACK_KING_SIDE_CASTLING_MOVE;
        }
        if (sanFormat == SanFormat.KING_CASTLING_QUEEN_SIDE_FORMAT) {
          return CastlingConstants.BLACK_QUEEN_SIDE_CASTLING_MOVE;
        }
        throw new IllegalArgumentException();
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }
}
