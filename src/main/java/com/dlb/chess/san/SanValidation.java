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
import com.dlb.chess.common.utility.MaterialUtility;
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
import com.dlb.chess.squares.to.potential.AbstractPotentialToSquares;

public class SanValidation extends AbstractSan implements EnumConstants {

  public static MoveSpecification validateSan(String san, ApiBoard board) throws SanValidationException {
    final var sanParse = SanValidateFormat.validateFormat(san);
    return validateAgainstPosition(board, sanParse);
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
      case PAWN_NON_CAPTURING_NON_PROMOTION_FORMAT: {
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

  private static MoveSpecification validateAgainstPosition(ApiBoard board, SanParse sanParse) throws SanValidationException {

    final Side havingMove = board.getHavingMove();
    final var sanType = sanParse.sanType();
    final var sanConversion = sanParse.sanConversion();
    final SanFormat sanFormat = sanType.getSanFormat();

    validatePieceExists(havingMove, sanFormat, sanConversion, sanType.getMovingPieceType(), board.getStaticPosition());

    validateMovingOntoOwnPiece(havingMove, sanFormat, sanConversion, board.getStaticPosition());

    validateNoCaptureIsNoCapture(havingMove, sanFormat, sanConversion, board.getStaticPosition());
    validateCaptureIsCapture(board, havingMove, sanType, sanConversion);

    // validate san against legal moves
    // (1) if the san specifies a possible legal move
    // (2) if the san specifies the move uniquely
    // (3) if the san contains non unnecessary file or rank information for the from square
    // (4) if the san uses rank specification instead of file specification

    final Set<LegalMove> legalMovesCandidates = calculateLegalMovesCandidates(board, havingMove, sanParse);
    validateAgainstLegalMoves(board.getStaticPosition(), havingMove, legalMovesCandidates, sanType, sanConversion);

    // eight step - we now construct the move and check against the identified legal
    // move it represents
    // (1) we filter the legal moves for san which leaves the only possible legal
    // move
    // (2) we construct the move from the san, here we need the only possible legal
    // move for some cases
    // (3) we check that the only possible legal moves and the constructed moves are
    // the same!
    final LegalMove legalMoveOnlyCandidate = calculateOnlyPossibleLegalMove(sanFormat, sanConversion,
        legalMovesCandidates);
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

  private static void validatePieceExists(Side havingMove, SanFormat sanFormat, SanConversion sanConversion,
      PieceType movingPieceType, StaticPosition staticPosition) {
    switch (sanFormat) {
      case KING_CASTLING_KING_SIDE_FORMAT:
      case KING_CASTLING_QUEEN_SIDE_FORMAT:
      case KING_NON_CASTLING_CAPTURING_FORMAT:
      case KING_NON_CASTLING_NON_CAPTURING_FORMAT:
        return;
      case PAWN_NON_CAPTURING_NON_PROMOTION_FORMAT:
      case PAWN_NON_CAPTURING_PROMOTION_FORMAT: {
        // for non-capturing pawn moves, the pawn must be on the to-square's file
        final File pawnFile = sanConversion.toSquare().getFile();
        if (!MaterialUtility.calculateHasPieceType(havingMove, PieceType.PAWN, staticPosition, pawnFile)) {
          throw new SanValidationException(SanValidationProblem.PAWN_NO_PIECE_EXISTS,
              Message.getString("validation.san.pawn.noPieceExists", havingMove.getName(), pawnFile.getLetter()));
        }
        validatePawnDestinationRank(havingMove, sanConversion.toSquare().getRank());
        validatePawnFromSquareNonCapturing(havingMove, sanConversion.toSquare(), staticPosition);
        break;
      }
      case PAWN_CAPTURING_NON_PROMOTION_FORMAT:
      case PAWN_CAPTURING_PROMOTION_FORMAT: {
        // for capturing pawn moves, the SAN specifies the from-file explicitly
        final File pawnFile = sanConversion.fromFile();
        if (!MaterialUtility.calculateHasPieceType(havingMove, PieceType.PAWN, staticPosition, pawnFile)) {
          throw new SanValidationException(SanValidationProblem.PAWN_NO_PIECE_EXISTS,
              Message.getString("validation.san.pawn.noPieceExists", havingMove.getName(), pawnFile.getLetter()));
        }
        validatePawnDestinationRank(havingMove, sanConversion.toSquare().getRank());
        validatePawnCapturingDiagonal(havingMove, sanConversion.fromFile(), sanConversion.toSquare().getFile());
        validatePawnFromSquareCapturing(havingMove, sanConversion.fromFile(), sanConversion.toSquare(), staticPosition);
        break;
      }
      case PIECE_CAPTURING_NEITHER_FORMAT:
      case PIECE_NON_CAPTURING_NEITHER_FORMAT:
        if (!MaterialUtility.calculateHasPieceType(havingMove, movingPieceType, staticPosition)) {
          throw new SanValidationException(SanValidationProblem.PIECE_NEITHER_NO_PIECE_EXISTS,
              Message.getString("validation.san.notPawn.specification.none.otherThanKing.noPieceExists",
                  havingMove.getName(), movingPieceType.getName()));
        }
        break;
      case PIECE_CAPTURING_FILE_FORMAT:
      case PIECE_NON_CAPTURING_FILE_FORMAT:
        if (!MaterialUtility.calculateHasPieceType(havingMove, movingPieceType, staticPosition,
            sanConversion.fromFile())) {
          throw new SanValidationException(SanValidationProblem.PIECE_FILE_NO_PIECE_EXISTS,
              Message.getString("validation.san.notPawn.specification.file.noPieceExists", havingMove.getName(),
                  movingPieceType.getName(), sanConversion.fromFile().getLetter()));
        }
        break;
      case PIECE_CAPTURING_RANK_FORMAT:
      case PIECE_NON_CAPTURING_RANK_FORMAT:
        if (!MaterialUtility.calculateHasPieceType(havingMove, movingPieceType, staticPosition,
            sanConversion.fromRank())) {
          throw new SanValidationException(SanValidationProblem.PIECE_RANK_NO_PIECE_EXISTS,
              Message.getString("validation.san.notPawn.specification.rank.noPieceExists", havingMove.getName(),
                  movingPieceType.getName(), NonNullWrapperCommon.valueOf(sanConversion.fromRank().getNumber())));
        }
        break;
      case PIECE_CAPTURING_SQUARE_FORMAT:
      case PIECE_NON_CAPTURING_SQUARE_FORMAT:
        final Square fromSquare = Square.calculate(sanConversion.fromFile(), sanConversion.fromRank());
        final Piece pieceOnFromSquare = staticPosition.get(fromSquare);
        if (pieceOnFromSquare == Piece.NONE || pieceOnFromSquare.getSide() != havingMove
            || pieceOnFromSquare.getPieceType() != movingPieceType) {
          throw new SanValidationException(SanValidationProblem.PIECE_SQUARE_NO_PIECE_EXISTS,
              Message.getString("validation.san.notPawn.specification.square.noPieceExists", havingMove.getName(),
                  movingPieceType.getName(), fromSquare.getName()));
        }
        break;
      default:
        throw new IllegalArgumentException();

    }
  }

  private static void validatePawnDestinationRank(Side havingMove, Rank destinationRank) {
    final var isInvalid = switch (havingMove) {
      case WHITE -> destinationRank == Rank.RANK_1 || destinationRank == Rank.RANK_2;
      case BLACK -> destinationRank == Rank.RANK_7 || destinationRank == Rank.RANK_8;
      case NONE -> throw new IllegalArgumentException();
    };
    if (isInvalid) {
      throw new SanValidationException(SanValidationProblem.PAWN_NON_REACHABLE_RANK,
          Message.getString("validation.san.pawn.destinationRank", havingMove.getName(),
              NonNullWrapperCommon.valueOf(destinationRank.getNumber())));
    }
  }

  private static void validatePawnCapturingDiagonal(Side havingMove, File fromFile, File toFile) {
    final var isAdjacentLeft = File.calculateHasLeftFile(havingMove, fromFile)
        && File.calculateLeftFile(havingMove, fromFile) == toFile;
    final var isAdjacentRight = File.calculateHasRightFile(havingMove, fromFile)
        && File.calculateRightFile(havingMove, fromFile) == toFile;

    if (!isAdjacentLeft && !isAdjacentRight) {
      // build educational message showing which files ARE valid
      if (File.calculateHasLeftFile(havingMove, fromFile) && File.calculateHasRightFile(havingMove, fromFile)) {
        final File leftFile = File.calculateLeftFile(havingMove, fromFile);
        final File rightFile = File.calculateRightFile(havingMove, fromFile);
        throw new SanValidationException(SanValidationProblem.PAWN_CAPTURING_DIAGONAL,
            Message.getString("validation.san.pawn.capturingDiagonal", fromFile.getLetter(), leftFile.getLetter(),
                rightFile.getLetter()));
      }
      // pawn on edge file (a or h) — only one adjacent file
      final File adjacentFile;
      if (File.calculateHasLeftFile(havingMove, fromFile)) {
        adjacentFile = File.calculateLeftFile(havingMove, fromFile);
      } else {
        adjacentFile = File.calculateRightFile(havingMove, fromFile);
      }
      throw new SanValidationException(SanValidationProblem.PAWN_CAPTURING_DIAGONAL, Message
          .getString("validation.san.pawn.capturingDiagonalEdge", fromFile.getLetter(), adjacentFile.getLetter()));
    }
  }

  private static void validatePawnFromSquareNonCapturing(Side havingMove, Square toSquare,
      StaticPosition staticPosition) {
    final File file = toSquare.getFile();
    final Rank toRank = toSquare.getRank();
    // one square back from the to-square
    final Rank oneBackRank = Rank.calculatePreviousRank(havingMove, toRank);
    final Square oneBackSquare = Square.calculate(file, oneBackRank);
    final Piece pieceOnOneBack = staticPosition.get(oneBackSquare);

    if (Rank.calculateIsPawnTwoSquareAdvanceRank(havingMove, toRank)) {
      // two-square advance (e.g. d4 for white): pawn on d2 and d3 empty, or pawn on d3
      final Rank twoBackRank = Rank.calculatePreviousRank(havingMove, oneBackRank);
      final Square twoBackSquare = Square.calculate(file, twoBackRank);
      final Piece pieceOnTwoBack = staticPosition.get(twoBackSquare);
      final Piece expectedPawn = PieceType.calculate(havingMove, PieceType.PAWN);

      final var pawnOnOneBack = pieceOnOneBack == expectedPawn;
      final var pawnOnTwoBackAndPathClear = pieceOnTwoBack == expectedPawn && pieceOnOneBack == Piece.NONE;

      if (!pawnOnOneBack && !pawnOnTwoBackAndPathClear) {
        throw new SanValidationException(SanValidationProblem.PAWN_FROM_SQUARE,
            Message.getString("validation.san.pawn.fromSquareTwoSquareAdvance", toSquare.getName(),
                havingMove.getName(), oneBackSquare.getName(), twoBackSquare.getName()));
      }
    } else {
      // one-square advance: pawn must be on the square directly behind
      final Piece expectedPawn = PieceType.calculate(havingMove, PieceType.PAWN);
      if (pieceOnOneBack != expectedPawn) {
        throw new SanValidationException(SanValidationProblem.PAWN_FROM_SQUARE,
            Message.getString("validation.san.pawn.fromSquare", havingMove.getName(), toSquare.getName()));
      }
    }
  }

  private static void validatePawnFromSquareCapturing(Side havingMove, File fromFile, Square toSquare,
      StaticPosition staticPosition) {
    // the pawn must be on the from-file, one rank back from the to-square
    final Rank fromRank = Rank.calculatePreviousRank(havingMove, toSquare.getRank());
    final Square fromSquare = Square.calculate(fromFile, fromRank);
    final Piece pieceOnFromSquare = staticPosition.get(fromSquare);
    final Piece expectedPawn = PieceType.calculate(havingMove, PieceType.PAWN);

    if (pieceOnFromSquare != expectedPawn) {
      throw new SanValidationException(SanValidationProblem.PAWN_FROM_SQUARE,
          Message.getString("validation.san.pawn.fromSquare", havingMove.getName(), toSquare.getName()));
    }
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
    if (sanType.isCapture()) {
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
          throw new SanValidationException(SanValidationProblem.CAPTURING_MOVING_ONTO_NO_PIECE,
              Message.getString("validation.san.allExceptCastling.captureIsNoCapture", toSquare.getName()));
        }
        if (pieceOnToSquare.getSide() == havingMove.getOppositeSide() && pieceOnToSquare.getPieceType() == KING) {
          throw new SanValidationException(SanValidationProblem.CAPTURING_OPPONENT_KING,
              Message.getString("validation.san.allExceptCastling.capturingOpponentKing", toSquare.getName()));
        }
      }
    }
  }

  private static Set<LegalMove> calculateLegalMovesCandidates(ApiBoard board, Side havingMove, SanParse sanParse) {
    final var sanType = sanParse.sanType();

    // for castling we need to filter the castling moves
    if (SanType.calculateIsKingCastlingMove(sanType)) {
      return filterCastlingMove(board.getLegalMoveSet());
    }

    final PieceType pieceType = sanType.getMovingPieceType();
    final Piece piece = PieceType.calculate(havingMove, pieceType);

    final Set<LegalMove> legalMoveSetForMovingPiece = MoveToSan.calculateLegalMoveSetForMovingPiece(piece,
        board.getLegalMoveSet());
    // for non castling moves we need to filter by the to square (which is always set for non castling)
    final Square toSquare = sanParse.sanConversion().toSquare();
    final Set<LegalMove> legalMovesCandidates = filterLegalMovesCandidates(legalMoveSetForMovingPiece, toSquare);

    // for pawn moves we must filter additionally by the from file!!
    if (sanType == SanType.PAWN_CAPTURING_NON_PROMOTION_MOVE || sanType == SanType.PAWN_CAPTURING_PROMOTION_MOVE) {
      return calculateLegalMovesCandidates(legalMovesCandidates, sanParse.sanConversion().fromFile());
    }
    return legalMovesCandidates;
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

  private static void validateAgainstLegalMoves(StaticPosition staticPosition, Side havingMove,
      Set<LegalMove> legalMovesCandidates, SanType sanType, SanConversion sanConversion) {

    // we need an early return for castling first so for the remaining cases we can
    // calculate the to square
    final SanFormat sanFormat = sanType.getSanFormat();
    if (sanFormat == SanFormat.KING_CASTLING_QUEEN_SIDE_FORMAT) {
      if (!isContained(legalMovesCandidates, havingMove, sanFormat)) {
        throw new SanValidationException(SanValidationProblem.KING_CASTLING_QUEEN_SIDE_NOT_POSSIBLE,
            Message.getString("validation.san.castling.queenSide"));
      }
      return;
    }
    if (sanFormat == SanFormat.KING_CASTLING_KING_SIDE_FORMAT) {
      if (!isContained(legalMovesCandidates, havingMove, sanFormat)) {
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
        validateAgainstLegalMovesForKing(legalMovesCandidates, toSquare);
        break;
      case PAWN_NON_CAPTURING_NON_PROMOTION_FORMAT:
      case PAWN_CAPTURING_NON_PROMOTION_FORMAT:
        validateAgainstLegalMovesForPawnNonPromotion(legalMovesCandidates, pieceType, toSquare);
        break;
      case PAWN_NON_CAPTURING_PROMOTION_FORMAT:
      case PAWN_CAPTURING_PROMOTION_FORMAT:
        validateAgainstLegalMovesForPawnPromotion(legalMovesCandidates, pieceType, toSquare);
        break;
      case PIECE_NON_CAPTURING_NEITHER_FORMAT:
      case PIECE_CAPTURING_NEITHER_FORMAT:
        validateAgainstLegalMovesForPieceNeither(legalMovesCandidates, pieceType, toSquare);
        break;
      case PIECE_NON_CAPTURING_FILE_FORMAT:
      case PIECE_CAPTURING_FILE_FORMAT: {
        validateAgainstLegalMovesForPieceFile(staticPosition, havingMove, legalMovesCandidates, pieceType, sanFormat,
            sanConversion, toSquare);
      }
        break;
      case PIECE_NON_CAPTURING_RANK_FORMAT:
      case PIECE_CAPTURING_RANK_FORMAT: {
        validateAgainstLegalMovesForPieceRank(staticPosition, havingMove, legalMovesCandidates, pieceType, sanFormat,
            sanConversion, toSquare);
      }
        break;
      case PIECE_NON_CAPTURING_SQUARE_FORMAT:
      case PIECE_CAPTURING_SQUARE_FORMAT: {
        validateAgainstLegalMovesForPieceSquare(staticPosition, havingMove, legalMovesCandidates, pieceType, sanFormat,
            sanConversion, toSquare);
      }
        break;
      default:
        throw new IllegalArgumentException();
    }
  }

  private static void validateAgainstLegalMovesForKing(Set<LegalMove> legalMovesCandidates, Square toSquare) {
    if (legalMovesCandidates.isEmpty()) {
      throw new SanValidationException(SanValidationProblem.KING_NON_CASTLING_NO_LEGAL_MOVE,
          Message.getString("validation.san.notPawn.specification.none.king.noLegalMove", toSquare.getName()));
    }
  }

  private static void validateAgainstLegalMovesForPawnNonPromotion(Set<LegalMove> legalMovesCandidates,
      PieceType pieceType, Square toSquare) {
    if (legalMovesCandidates.isEmpty()) {
      throw new SanValidationException(SanValidationProblem.PAWN_NON_PROMOTION_NO_LEGAL_MOVE,
          Message.getString("validation.san.pawn.noLegalMove", pieceType.getName(), toSquare.getName()));
    }
  }

  private static void validateAgainstLegalMovesForPawnPromotion(Set<LegalMove> legalMovesCandidates, PieceType pieceType,
      Square toSquare) {
    if (legalMovesCandidates.isEmpty()) {
      throw new SanValidationException(SanValidationProblem.PAWN_PROMOTION_NO_LEGAL_MOVE,
          Message.getString("validation.san.pawn.noLegalMove", pieceType.getName(), toSquare.getName()));
    }
  }

  private static void validateAgainstLegalMovesForPieceNeither(Set<LegalMove> legalMovesCandidates, PieceType pieceType,
      Square toSquare) {
    if (legalMovesCandidates.isEmpty()) {
      throw new SanValidationException(SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE,
          Message.getString("validation.san.notPawn.specification.none.otherThanKing.noLegalMove", pieceType.getName(),
              toSquare.getName()));
    }
    if (legalMovesCandidates.size() > 1) {
      throw new SanValidationException(SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES,
          Message.getString("validation.san.notPawn.specification.none.moreThanOneLegalMove", pieceType.getName(),
              toSquare.getName()));
    }
  }

  private static void validateAgainstLegalMovesForPieceFile(StaticPosition staticPosition, Side havingMove,
      Set<LegalMove> legalMovesCandidates, PieceType pieceType, SanFormat sanFormat, SanConversion sanConversion,
      Square toSquare) {
    final Set<Square> pieceCandidates = calculatePieceCandidateSquareSet(staticPosition, havingMove, pieceType,
        sanFormat, sanConversion);
    final Set<Square> movementCandidates = filterCandidateSquaresForPotentialMove(staticPosition, havingMove, toSquare,
        pieceCandidates);
    if (movementCandidates.isEmpty()) {
      throw new SanValidationException(SanValidationProblem.INVALID_MOVEMENT_NON_PAWN_FROM_FILE,
          Message.getString("validation.san.notPawn.specification.file.invalidMovement", pieceType.getName(),
              sanConversion.fromFile().getLetter(), toSquare.getName()));
    }

    final var numberOfLegalMovesFromSameFile = calculateNumberOfLegalMovesFromFile(sanConversion.fromFile(),
        legalMovesCandidates);
    if (numberOfLegalMovesFromSameFile == 0) {
      throw new SanValidationException(SanValidationProblem.PIECE_FILE_NO_LEGAL_MOVE,
          Message.getString("validation.san.noLegalMove.fromFile", pieceType.getName(),
              sanConversion.fromFile().getLetter(), toSquare.getName()));
    }

    if (legalMovesCandidates.size() == 1) {
      throw new SanValidationException(SanValidationProblem.PIECE_FILE_ONLY_ONE_LEGAL_MOVE,
          Message.getString("validation.san.overspecification.file"));
    }

    if (!calculateHasOtherFilesHavingLegalMoves(sanConversion.fromFile(), legalMovesCandidates)) {
      if (numberOfLegalMovesFromSameFile < 2) {
        throw new ProgrammingMistakeException("A programming assumption about the rank turned out to be wrong");
      }
      throw new SanValidationException(SanValidationProblem.PIECE_FILE_MUST_USE_RANK,
          Message.getString("validation.san.notDetermined.byFile", pieceType.getName(),
              sanConversion.fromFile().getLetter(), toSquare.getName()));
    }

    if (numberOfLegalMovesFromSameFile >= 2) {
      if (pieceType == ROOK) {
        throw new SanValidationException(SanValidationProblem.PIECE_FILE_MUST_USE_RANK,
            Message.getString("validation.san.notDetermined.byFile", pieceType.getName(),
                sanConversion.fromFile().getLetter(), toSquare.getName()));
      }
      throw new SanValidationException(SanValidationProblem.PIECE_FILE_MUST_USE_RANK_OR_SQUARE,
          Message.getString("validation.san.notDetermined.byFile", pieceType.getName(),
              sanConversion.fromFile().getLetter(), toSquare.getName()));
    }
  }

  private static void validateAgainstLegalMovesForPieceRank(StaticPosition staticPosition, Side havingMove,
      Set<LegalMove> legalMovesCandidates, PieceType pieceType, SanFormat sanFormat, SanConversion sanConversion,
      Square toSquare) {
    final Set<Square> pieceCandidates = calculatePieceCandidateSquareSet(staticPosition, havingMove, pieceType,
        sanFormat, sanConversion);
    final Set<Square> movementCandidates = filterCandidateSquaresForPotentialMove(staticPosition, havingMove, toSquare,
        pieceCandidates);
    if (movementCandidates.isEmpty()) {
      throw new SanValidationException(SanValidationProblem.INVALID_MOVEMENT_NON_PAWN_FROM_RANK,
          Message.getString("validation.san.notPawn.specification.rank.invalidMovement", pieceType.getName(),
              NonNullWrapperCommon.valueOf(sanConversion.fromRank().getNumber()), toSquare.getName()));
    }

    final var numberOfLegalMovesFromSameRank = calculateNumberOfLegalMovesFromRank(sanConversion.fromRank(),
        legalMovesCandidates);
    if (numberOfLegalMovesFromSameRank == 0) {
      throw new SanValidationException(SanValidationProblem.PIECE_RANK_NO_LEGAL_MOVE,
          Message.getString("validation.san.noLegalMove.fromRank", pieceType.getName(),
              NonNullWrapperCommon.valueOf(sanConversion.fromRank().getNumber()), toSquare.getName()));
    }

    if (legalMovesCandidates.size() == 1) {
      throw new SanValidationException(SanValidationProblem.PIECE_RANK_ONLY_ONE_LEGAL_MOVE,
          Message.getString("validation.san.overspecification.rank"));
    }

    if (!calculateHasOtherRanksHavingLegalMoves(sanConversion.fromRank(), legalMovesCandidates)) {
      if (numberOfLegalMovesFromSameRank < 2) {
        throw new ProgrammingMistakeException("A programming assumption about the file turned out to be wrong");
      }
      throw new SanValidationException(SanValidationProblem.PIECE_RANK_MUST_USE_FILE,
          Message.getString("validation.san.notDetermined.byRank", pieceType.getName(),
              NonNullWrapperCommon.valueOf(sanConversion.fromRank().getNumber()), toSquare.getName()));
    }

    if (numberOfLegalMovesFromSameRank >= 2) {
      throw new SanValidationException(SanValidationProblem.PIECE_RANK_MUST_USE_FILE_OR_SQUARE,
          Message.getString("validation.san.notDetermined.byRank", pieceType.getName(),
              NonNullWrapperCommon.valueOf(sanConversion.fromRank().getNumber()), toSquare.getName()));
    }

    File onlyPossibleFromFile = calculateOnlyPossibleFile(legalMovesCandidates, sanConversion);
    for (final LegalMove legalMove : legalMovesCandidates) {
      if (legalMove.moveSpecification().fromSquare().getRank() == sanConversion.fromRank()) {
        onlyPossibleFromFile = legalMove.moveSpecification().fromSquare().getFile();
        break;
      }
    }
    if (onlyPossibleFromFile == File.NONE) {
      throw new ProgrammingMistakeException(
          "The program made the wrong assumption that the from file is determined at this point");
    }
    final var numberOfLegalMovesFromSameFile = calculateNumberOfLegalMovesFromFile(onlyPossibleFromFile,
        legalMovesCandidates);

    if (numberOfLegalMovesFromSameFile == 1) {
      throw new SanValidationException(SanValidationProblem.PIECE_RANK_MUST_USE_FILE,
          Message.getString("validation.san.rankInsteadOfFileUsed"));
    }
  }

  private static void validateAgainstLegalMovesForPieceSquare(StaticPosition staticPosition, Side havingMove,
      Set<LegalMove> legalMovesCandidates, PieceType pieceType, SanFormat sanFormat, SanConversion sanConversion,
      Square toSquare) {
    final Square fromSquare = calculateFromSquare(sanConversion);
    final Set<Square> pieceCandidates = calculatePieceCandidateSquareSet(staticPosition, havingMove, pieceType,
        sanFormat, sanConversion);
    final Set<Square> movementCandidates = filterCandidateSquaresForPotentialMove(staticPosition, havingMove, toSquare,
        pieceCandidates);
    if (movementCandidates.isEmpty()) {
      throw new SanValidationException(SanValidationProblem.INVALID_MOVEMENT_NON_PAWN_FROM_SQUARE,
          Message.getString("validation.san.notPawn.specification.square.invalidMovement", pieceType.getName(),
              fromSquare.getName(), toSquare.getName()));
    }
    if (calculateNumberOfLegalMovesFromSquare(fromSquare, legalMovesCandidates) == 0) {
      throw new SanValidationException(SanValidationProblem.PIECE_SQUARE_NO_LEGAL_MOVE,
          Message.getString("validation.san.noLegalMove.fromSquare", pieceType.getName(), fromSquare.getName(),
              sanConversion.toSquare().getName()));
    }

    if (legalMovesCandidates.size() == 1) {
      throw new SanValidationException(SanValidationProblem.PIECE_SQUARE_ONLY_ONE_LEGAL_MOVE,
          Message.getString("validation.san.overspecification.square.square"));
    }

    final var numberOfLegalMovesFromOtherFiles = calculateNumberOfLegalMovesFromOtherFiles(sanConversion.fromFile(),
        legalMovesCandidates);

    final var numberOfLegalMovesFromFile = calculateNumberOfLegalMovesFromFile(sanConversion.fromFile(),
        legalMovesCandidates);

    if (numberOfLegalMovesFromFile == 2 && numberOfLegalMovesFromOtherFiles == 0) {
      throw new SanValidationException(SanValidationProblem.PIECE_SQUARE_FILE_NOT_NECESSARY,
          Message.getString("validation.san.overspecification.square.file"));
    }

    if (numberOfLegalMovesFromFile == 1 && numberOfLegalMovesFromOtherFiles >= 1) {
      throw new SanValidationException(SanValidationProblem.PIECE_SQUARE_RANK_NOT_NECESSARY,
          Message.getString("validation.san.overspecification.square.rank"));
    }
  }

  private static Set<Square> calculatePieceCandidateSquareSet(StaticPosition staticPosition, Side havingMove,
      PieceType pieceType, SanFormat sanFormat, SanConversion sanConversion) {
    final Set<Square> result = new TreeSet<>();
    for (final Square square : Square.values()) {
      if (square == Square.NONE) {
        continue;
      }
      if (!staticPosition.isOwnPiece(square, havingMove, pieceType)) {
        continue;
      }
      switch (sanFormat) {
        case PIECE_NON_CAPTURING_NEITHER_FORMAT:
        case PIECE_CAPTURING_NEITHER_FORMAT:
          result.add(square);
          break;
        case PIECE_NON_CAPTURING_FILE_FORMAT:
        case PIECE_CAPTURING_FILE_FORMAT:
          if (square.getFile() == sanConversion.fromFile()) {
            result.add(square);
          }
          break;
        case PIECE_NON_CAPTURING_RANK_FORMAT:
        case PIECE_CAPTURING_RANK_FORMAT:
          if (square.getRank() == sanConversion.fromRank()) {
            result.add(square);
          }
          break;
        case PIECE_NON_CAPTURING_SQUARE_FORMAT:
        case PIECE_CAPTURING_SQUARE_FORMAT:
          if (square == calculateFromSquare(sanConversion)) {
            result.add(square);
          }
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
    return result;
  }

  private static Set<Square> filterCandidateSquaresForPotentialMove(StaticPosition staticPosition, Side havingMove,
      Square toSquare, Set<Square> candidateSquares) {
    final Set<Square> result = new TreeSet<>();
    for (final Square candidateSquare : candidateSquares) {
      final Set<Square> potentialToSquares = AbstractPotentialToSquares.calculatePotentialToSquare(staticPosition,
          Square.NONE, havingMove, candidateSquare);
      if (potentialToSquares.contains(toSquare)) {
        result.add(candidateSquare);
      }
    }
    return result;
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
