package com.dlb.chess.san.validate;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.CastlingRightLoss;
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
import com.dlb.chess.common.utility.SetUtility;
import com.dlb.chess.common.utility.StaticPositionUtility;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.enums.MoveCheck;
import com.dlb.chess.model.LegalMoveCalculation;
import com.dlb.chess.model.PseudoLegalMove;
import com.dlb.chess.model.PseudoLegalReason;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.moves.legal.AbstractLegalMoves;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.MoveToSan;
import com.dlb.chess.san.enums.SanFormat;
import com.dlb.chess.san.enums.SanType;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.squares.to.potential.AbstractPotentialToSquares;

public abstract class SanValidateLegalMoves extends AbstractSan implements EnumConstants {

  public static MoveSpecification calculateMoveSpecificationForSan(ApiBoard board, Side havingMove, SanFormat sanFormat,
      SanConversion sanConversion, MoveSpecification legalMoveOnlyCandidate) {

    if (sanFormat == SanFormat.KING_CASTLING_QUEEN_SIDE) {
      return new MoveSpecification(havingMove, CastlingMove.QUEEN_SIDE);
    }
    if (sanFormat == SanFormat.KING_CASTLING_KING_SIDE) {
      return new MoveSpecification(havingMove, CastlingMove.KING_SIDE);
    }

    final Square toSquare = sanConversion.toSquare();

    switch (sanFormat) {
      case KING_CASTLING_QUEEN_SIDE:
      case KING_CASTLING_KING_SIDE:
        throw new ProgrammingMistakeException("Castling is handled before switch");
      case PAWN_NON_CAPTURING_NON_PROMOTION: {
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
      case PAWN_CAPTURING_NON_PROMOTION: {
        // from file is in the san and from rank is the rank before to rank

        final Rank fromRank = Rank.calculatePreviousRank(havingMove, toSquare.getRank());
        final Square fromSquare = Square.calculate(sanConversion.fromFile(), fromRank);
        return new MoveSpecification(havingMove, fromSquare, toSquare);
      }
      case PAWN_NON_CAPTURING_PROMOTION: {
        // from file equals to file and from rank is the rank before to rank
        final File fromFile = toSquare.getFile(); // moving straight forward
        final Rank fromRank = Rank.calculatePreviousRank(havingMove, toSquare.getRank());
        final Square fromSquare = Square.calculate(fromFile, fromRank);
        return new MoveSpecification(havingMove, fromSquare, toSquare, sanConversion.promotionPieceType());
      }
      case PAWN_CAPTURING_PROMOTION: {
        // from file is in the san and from rank is the rank before to rank
        final Rank fromRank = Rank.calculatePreviousRank(havingMove, toSquare.getRank());
        final Square fromSquare = Square.calculate(sanConversion.fromFile(), fromRank);
        return new MoveSpecification(havingMove, fromSquare, toSquare, sanConversion.promotionPieceType());
      }
      case PIECE_CAPTURING_SQUARE: {
        // san is enough to determine from square
        final Square fromSquare = AbstractSan.calculateFromSquare(sanConversion);
        return new MoveSpecification(havingMove, fromSquare, toSquare);
      }
      case KING_NON_CASTLING_CAPTURING:
      case KING_NON_CASTLING_NON_CAPTURING:
      case PIECE_CAPTURING_FILE:
      case PIECE_CAPTURING_NEITHER:
      case PIECE_CAPTURING_RANK:
      case PIECE_NON_CAPTURING_SQUARE:
      case PIECE_NON_CAPTURING_FILE:
      case PIECE_NON_CAPTURING_NEITHER:
      case PIECE_NON_CAPTURING_RANK: {
        // legal move is required to determine from square
        final Square fromSquare = legalMoveOnlyCandidate.fromSquare();
        return new MoveSpecification(havingMove, fromSquare, toSquare);
      }
      default:
        throw new IllegalArgumentException();

    }
  }

  public static Set<LegalMove> calculateLegalMovesCandidates(ApiBoard board, Side havingMove, SanParse sanParse) {
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

  public static LegalMove calculateOnlyPossibleLegalMove(SanFormat sanFormat, SanConversion sanConversion,
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

  public static void validateAgainstLegalMoves(ApiBoard board, Side havingMove,
      Set<LegalMove> legalMovesCandidates, SanType sanType, SanConversion sanConversion) {

    final StaticPosition staticPosition = board.getStaticPosition();

    // we need an early return for castling first so for the remaining cases we can
    // calculate the to square
    final SanFormat sanFormat = sanType.getSanFormat();
    if (sanFormat == SanFormat.KING_CASTLING_QUEEN_SIDE) {
      if (!isContained(legalMovesCandidates, havingMove, sanFormat)) {
        throwCastlingException(board, havingMove, "Queen-side", CastlingMove.QUEEN_SIDE);
      }
      return;
    }
    if (sanFormat == SanFormat.KING_CASTLING_KING_SIDE) {
      if (!isContained(legalMovesCandidates, havingMove, sanFormat)) {
        throwCastlingException(board, havingMove, "King-side", CastlingMove.KING_SIDE);
      }
      return;
    }

    // only in non castling case we can calculate the to square!
    final Square toSquare = sanConversion.toSquare();
    final PieceType pieceType = sanType.getMovingPieceType();

    switch (sanFormat) {
      case KING_CASTLING_QUEEN_SIDE, KING_CASTLING_KING_SIDE -> throw new ProgrammingMistakeException(
          "Invalid program flow, the castling must be handled at this point");
      case KING_NON_CASTLING_NON_CAPTURING, KING_NON_CASTLING_CAPTURING -> validateAgainstLegalMovesForKing(
          legalMovesCandidates, toSquare);
      case PAWN_NON_CAPTURING_NON_PROMOTION, PAWN_CAPTURING_NON_PROMOTION -> validateAgainstLegalMovesForPawnNonPromotion(
          legalMovesCandidates, pieceType, toSquare);
      case PAWN_NON_CAPTURING_PROMOTION, PAWN_CAPTURING_PROMOTION -> validateAgainstLegalMovesForPawnPromotion(
          legalMovesCandidates, pieceType, toSquare);
      case PIECE_NON_CAPTURING_NEITHER, PIECE_CAPTURING_NEITHER -> validateAgainstLegalMovesForPieceNeither(
          staticPosition, havingMove, legalMovesCandidates, pieceType, toSquare);
      case PIECE_NON_CAPTURING_FILE, PIECE_CAPTURING_FILE -> validateAgainstLegalMovesForPieceFile(staticPosition,
          havingMove, legalMovesCandidates, pieceType, sanFormat, sanConversion, toSquare);
      case PIECE_NON_CAPTURING_RANK, PIECE_CAPTURING_RANK -> validateAgainstLegalMovesForPieceRank(staticPosition,
          havingMove, legalMovesCandidates, pieceType, sanFormat, sanConversion, toSquare);
      case PIECE_NON_CAPTURING_SQUARE, PIECE_CAPTURING_SQUARE -> validateAgainstLegalMovesForPieceSquare(staticPosition,
          havingMove, legalMovesCandidates, pieceType, sanFormat, sanConversion, toSquare);
      default -> throw new IllegalArgumentException();
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

  private static void validateAgainstLegalMovesForPawnPromotion(Set<LegalMove> legalMovesCandidates,
      PieceType pieceType, Square toSquare) {
    if (legalMovesCandidates.isEmpty()) {
      throw new SanValidationException(SanValidationProblem.PAWN_PROMOTION_NO_LEGAL_MOVE,
          Message.getString("validation.san.pawn.noLegalMove", pieceType.getName(), toSquare.getName()));
    }
  }

  private static void validateAgainstLegalMovesForPieceNeither(StaticPosition staticPosition, Side havingMove,
      Set<LegalMove> legalMovesCandidates, PieceType pieceType, Square toSquare) {
    if (legalMovesCandidates.isEmpty()) {
      final Set<PseudoLegalMove> pseudoLegalMoves = calculatePseudoLegalMovesForPieceNeither(staticPosition, havingMove,
          pieceType, toSquare);

      if (pseudoLegalMoves.isEmpty()) {
        if (countPiecesOfType(staticPosition, havingMove, pieceType) == 1) {
          throw new SanValidationException(SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_SINGLE,
              Message.getString("validation.san.notPawn.specification.none.notReachable.single", pieceType.getName(),
                  toSquare.getName()));
        }
        throw new SanValidationException(SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_MULTIPLE,
            Message.getString("validation.san.notPawn.specification.none.notReachable.multiple", pieceType.getName(),
                toSquare.getName()));
      }
      final var reason = calculatePseudoLegalReason(staticPosition, havingMove);
      final var msgKey = reason == PseudoLegalReason.KING_LEFT_IN_CHECK ? "kingLeftInCheck" : "kingExposedToCheck";
      if (pseudoLegalMoves.size() == 1) {
        final Square fromSquare = SetUtility.getOnly(pseudoLegalMoves).moveSpecification().fromSquare();
        final var problem = reason == PseudoLegalReason.KING_LEFT_IN_CHECK
            ? SanValidationProblem.PIECE_NEITHER_KING_LEFT_IN_CHECK_SINGLE
            : SanValidationProblem.PIECE_NEITHER_KING_EXPOSED_TO_CHECK_SINGLE;
        throw new SanValidationException(problem,
            Message.getString("validation.san.notPawn.specification.none." + msgKey + ".single", pieceType.getName(),
                fromSquare.getName(), toSquare.getName()));
      }
      final var problem = reason == PseudoLegalReason.KING_LEFT_IN_CHECK
          ? SanValidationProblem.PIECE_NEITHER_KING_LEFT_IN_CHECK_MULTIPLE
          : SanValidationProblem.PIECE_NEITHER_KING_EXPOSED_TO_CHECK_MULTIPLE;
      throw new SanValidationException(problem,
          Message.getString("validation.san.notPawn.specification.none." + msgKey + ".multiple", pieceType.getName(),
              toSquare.getName()));
    }
    if (legalMovesCandidates.size() > 1) {
      throw new SanValidationException(SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES, Message.getString(
          "validation.san.notPawn.specification.none.moreThanOneLegalMove", pieceType.getName(), toSquare.getName()));
    }
  }

  private static void throwCastlingException(ApiBoard board, Side havingMove, String sideLabel,
      CastlingMove castlingMove) {
    final CastlingRight castlingRight = board.getCastlingRight(havingMove);
    final MoveCheck moveCheck = castlingMove == CastlingMove.QUEEN_SIDE
        ? CastlingUtility.calculateQueenSideCastlingCheck(board.getStaticPosition(), havingMove, castlingRight)
        : CastlingUtility.calculateKingSideCastlingCheck(board.getStaticPosition(), havingMove, castlingRight);

    final CastlingRightLoss castlingRightLoss;
    final String message;

    switch (moveCheck) {
      case CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE:
        castlingRightLoss = CastlingRightLoss.NONE;
        message = Message.getString("validation.san.castling.kingOrRookNotOnRequiredSquare", sideLabel);
        break;
      case CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE: {
        castlingRightLoss = board instanceof com.dlb.chess.board.Board concreteBoard
            ? concreteBoard.getCastlingRightLoss(havingMove, castlingMove)
            : CastlingRightLoss.NONE;
        final String rookLabel = castlingMove == CastlingMove.QUEEN_SIDE ? "queen-side" : "king-side";
        message = switch (castlingRightLoss) {
          case KING_MOVED -> Message.getString("validation.san.castling.noRight.kingMoved", sideLabel);
          case ROOK_MOVED -> Message.getString("validation.san.castling.noRight.rookMoved", sideLabel, rookLabel);
          case ROOK_CAPTURED -> Message.getString("validation.san.castling.noRight.rookCaptured", sideLabel, rookLabel);
          case CASTLED -> Message.getString("validation.san.castling.noRight.castled", sideLabel);
          default -> Message.getString("validation.san.castling.noRight.unknown", sideLabel);
        };
        break;
      }
      case CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY:
        castlingRightLoss = CastlingRightLoss.NONE;
        message = Message.getString("validation.san.castling.squaresNotEmpty", sideLabel);
        break;
      case CASTLING_PRIORITY_4_KING_IN_CHECK:
        castlingRightLoss = CastlingRightLoss.NONE;
        message = Message.getString("validation.san.castling.kingInCheck", sideLabel);
        break;
      case CASTLING_PRIORITY_5_KING_WOULD_TRAVEL_THROUGH_CHECK:
        castlingRightLoss = CastlingRightLoss.NONE;
        message = Message.getString("validation.san.castling.kingWouldTravelThroughCheck", sideLabel);
        break;
      case CASTLING_PRIORITY_6_KING_WOULD_END_IN_CHECK:
        castlingRightLoss = CastlingRightLoss.NONE;
        message = Message.getString("validation.san.castling.kingWouldEndInCheck", sideLabel);
        break;
      case SUCCESS:
        throw new ProgrammingMistakeException("Castling check returned SUCCESS but move is not in legal moves");
      default:
        throw new ProgrammingMistakeException("Unexpected castling check result: " + moveCheck);
    }

    throw new SanValidationException(SanValidationProblem.KING_CASTLING_NOT_POSSIBLE, message, moveCheck,
        castlingRightLoss);
  }

  private static PseudoLegalReason calculatePseudoLegalReason(StaticPosition staticPosition, Side havingMove) {
    if (StaticPositionUtility.calculateIsEvaluateAttackingKing(staticPosition, havingMove.getOppositeSide())) {
      return PseudoLegalReason.KING_LEFT_IN_CHECK;
    }
    return PseudoLegalReason.KING_EXPOSED_TO_CHECK;
  }

  private static Set<PseudoLegalMove> calculatePseudoLegalMovesForPieceNeither(StaticPosition staticPosition,
      Side havingMove, PieceType pieceType, Square toSquare) {
    final Set<PseudoLegalMove> allPseudoLegal = new TreeSet<>();
    for (final Square fromSquare : Square.BOARD_SQUARE_LIST) {
      if (!staticPosition.isOwnPiece(fromSquare, havingMove, pieceType)) {
        continue;
      }
      final Set<Square> potentialToSquares = AbstractPotentialToSquares.calculatePotentialToSquare(staticPosition,
          Square.NONE, havingMove, fromSquare);
      if (potentialToSquares.contains(toSquare)) {
        final LegalMoveCalculation calc = AbstractLegalMoves.calculateLegalMoveCalculation(staticPosition, havingMove,
            fromSquare, NonNullWrapperCommon.setOf(toSquare));
        allPseudoLegal.addAll(calc.pseudoLegalMoveSet());
      }
    }
    return allPseudoLegal;
  }

  private static int countPiecesOfType(StaticPosition staticPosition, Side havingMove, PieceType pieceType) {
    var count = 0;
    for (final Square square : Square.BOARD_SQUARE_LIST) {
      if (staticPosition.isOwnPiece(square, havingMove, pieceType)) {
        count++;
      }
    }
    return count;
  }

  private static int countPiecesOfTypeOnFile(StaticPosition staticPosition, Side havingMove, PieceType pieceType,
      File file) {
    var count = 0;
    for (final Square square : Square.BOARD_SQUARE_LIST) {
      if (square.getFile() == file && staticPosition.isOwnPiece(square, havingMove, pieceType)) {
        count++;
      }
    }
    return count;
  }

  private static Set<PseudoLegalMove> calculatePseudoLegalMovesForPieceFile(StaticPosition staticPosition,
      Side havingMove, PieceType pieceType, File file, Square toSquare) {
    final Set<PseudoLegalMove> allPseudoLegal = new TreeSet<>();
    for (final Square fromSquare : Square.BOARD_SQUARE_LIST) {
      if (fromSquare.getFile() != file || !staticPosition.isOwnPiece(fromSquare, havingMove, pieceType)) {
        continue;
      }
      final Set<Square> potentialToSquares = AbstractPotentialToSquares.calculatePotentialToSquare(staticPosition,
          Square.NONE, havingMove, fromSquare);
      if (potentialToSquares.contains(toSquare)) {
        final LegalMoveCalculation calc = AbstractLegalMoves.calculateLegalMoveCalculation(staticPosition, havingMove,
            fromSquare, NonNullWrapperCommon.setOf(toSquare));
        allPseudoLegal.addAll(calc.pseudoLegalMoveSet());
      }
    }
    return allPseudoLegal;
  }

  private static int countPiecesOfTypeOnRank(StaticPosition staticPosition, Side havingMove, PieceType pieceType,
      Rank rank) {
    var count = 0;
    for (final Square square : Square.BOARD_SQUARE_LIST) {
      if (square.getRank() == rank && staticPosition.isOwnPiece(square, havingMove, pieceType)) {
        count++;
      }
    }
    return count;
  }

  private static Set<PseudoLegalMove> calculatePseudoLegalMovesForPieceRank(StaticPosition staticPosition,
      Side havingMove, PieceType pieceType, Rank rank, Square toSquare) {
    final Set<PseudoLegalMove> allPseudoLegal = new TreeSet<>();
    for (final Square fromSquare : Square.BOARD_SQUARE_LIST) {
      if (fromSquare.getRank() != rank || !staticPosition.isOwnPiece(fromSquare, havingMove, pieceType)) {
        continue;
      }
      final Set<Square> potentialToSquares = AbstractPotentialToSquares.calculatePotentialToSquare(staticPosition,
          Square.NONE, havingMove, fromSquare);
      if (potentialToSquares.contains(toSquare)) {
        final LegalMoveCalculation calc = AbstractLegalMoves.calculateLegalMoveCalculation(staticPosition, havingMove,
            fromSquare, NonNullWrapperCommon.setOf(toSquare));
        allPseudoLegal.addAll(calc.pseudoLegalMoveSet());
      }
    }
    return allPseudoLegal;
  }

  private static void validateAgainstLegalMovesForPieceFile(StaticPosition staticPosition, Side havingMove,
      Set<LegalMove> legalMovesCandidates, PieceType pieceType, SanFormat sanFormat, SanConversion sanConversion,
      Square toSquare) {
    final File fromFile = sanConversion.fromFile();
    final Set<Square> pieceCandidates = calculatePieceCandidateSquareSet(staticPosition, havingMove, pieceType,
        sanFormat, sanConversion);
    final Set<Square> movementCandidates = filterCandidateSquaresForPotentialMove(staticPosition, havingMove, toSquare,
        pieceCandidates);
    if (movementCandidates.isEmpty()) {
      if (countPiecesOfTypeOnFile(staticPosition, havingMove, pieceType, fromFile) == 1) {
        final Square pieceSquare = SetUtility.getOnly(pieceCandidates);
        throw new SanValidationException(SanValidationProblem.PIECE_FILE_NOT_REACHABLE_SINGLE,
            Message.getString("validation.san.notPawn.specification.file.notReachable.single", pieceType.getName(),
                pieceSquare.getName(), toSquare.getName()));
      }
      throw new SanValidationException(SanValidationProblem.PIECE_FILE_NOT_REACHABLE_MULTIPLE,
          Message.getString("validation.san.notPawn.specification.file.notReachable.multiple", pieceType.getName(),
              fromFile.getLetter(), toSquare.getName()));
    }

    final var numberOfLegalMovesFromSameFile = calculateNumberOfLegalMovesFromFile(fromFile, legalMovesCandidates);
    if (numberOfLegalMovesFromSameFile == 0) {
      final Set<PseudoLegalMove> pseudoLegalMoves = calculatePseudoLegalMovesForPieceFile(staticPosition, havingMove,
          pieceType, fromFile, toSquare);
      final var reason = calculatePseudoLegalReason(staticPosition, havingMove);
      final var msgKey = reason == PseudoLegalReason.KING_LEFT_IN_CHECK ? "kingLeftInCheck" : "kingExposedToCheck";
      if (pseudoLegalMoves.size() == 1) {
        final Square pieceSquare = SetUtility.getOnly(pseudoLegalMoves).moveSpecification().fromSquare();
        final var problem = reason == PseudoLegalReason.KING_LEFT_IN_CHECK
            ? SanValidationProblem.PIECE_FILE_KING_LEFT_IN_CHECK_SINGLE
            : SanValidationProblem.PIECE_FILE_KING_EXPOSED_TO_CHECK_SINGLE;
        throw new SanValidationException(problem,
            Message.getString("validation.san.notPawn.specification.file." + msgKey + ".single", pieceType.getName(),
                pieceSquare.getName(), toSquare.getName()));
      }
      final var problem = reason == PseudoLegalReason.KING_LEFT_IN_CHECK
          ? SanValidationProblem.PIECE_FILE_KING_LEFT_IN_CHECK_MULTIPLE
          : SanValidationProblem.PIECE_FILE_KING_EXPOSED_TO_CHECK_MULTIPLE;
      throw new SanValidationException(problem,
          Message.getString("validation.san.notPawn.specification.file." + msgKey + ".multiple", pieceType.getName(),
              fromFile.getLetter(), toSquare.getName()));
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
    final Rank fromRank = sanConversion.fromRank();
    final Set<Square> pieceCandidates = calculatePieceCandidateSquareSet(staticPosition, havingMove, pieceType,
        sanFormat, sanConversion);
    final Set<Square> movementCandidates = filterCandidateSquaresForPotentialMove(staticPosition, havingMove, toSquare,
        pieceCandidates);
    if (movementCandidates.isEmpty()) {
      if (countPiecesOfTypeOnRank(staticPosition, havingMove, pieceType, fromRank) == 1) {
        final Square pieceSquare = SetUtility.getOnly(pieceCandidates);
        throw new SanValidationException(SanValidationProblem.PIECE_RANK_NOT_REACHABLE_SINGLE,
            Message.getString("validation.san.notPawn.specification.rank.notReachable.single", pieceType.getName(),
                pieceSquare.getName(), toSquare.getName()));
      }
      throw new SanValidationException(SanValidationProblem.PIECE_RANK_NOT_REACHABLE_MULTIPLE,
          Message.getString("validation.san.notPawn.specification.rank.notReachable.multiple", pieceType.getName(),
              NonNullWrapperCommon.valueOf(fromRank.getNumber()), toSquare.getName()));
    }

    final var numberOfLegalMovesFromSameRank = calculateNumberOfLegalMovesFromRank(fromRank, legalMovesCandidates);
    if (numberOfLegalMovesFromSameRank == 0) {
      final Set<PseudoLegalMove> pseudoLegalMoves = calculatePseudoLegalMovesForPieceRank(staticPosition, havingMove,
          pieceType, fromRank, toSquare);
      final var reason = calculatePseudoLegalReason(staticPosition, havingMove);
      final var msgKey = reason == PseudoLegalReason.KING_LEFT_IN_CHECK ? "kingLeftInCheck" : "kingExposedToCheck";
      if (pseudoLegalMoves.size() == 1) {
        final Square pieceSquare = SetUtility.getOnly(pseudoLegalMoves).moveSpecification().fromSquare();
        final var problem = reason == PseudoLegalReason.KING_LEFT_IN_CHECK
            ? SanValidationProblem.PIECE_RANK_KING_LEFT_IN_CHECK_SINGLE
            : SanValidationProblem.PIECE_RANK_KING_EXPOSED_TO_CHECK_SINGLE;
        throw new SanValidationException(problem,
            Message.getString("validation.san.notPawn.specification.rank." + msgKey + ".single", pieceType.getName(),
                pieceSquare.getName(), toSquare.getName()));
      }
      final var problem = reason == PseudoLegalReason.KING_LEFT_IN_CHECK
          ? SanValidationProblem.PIECE_RANK_KING_LEFT_IN_CHECK_MULTIPLE
          : SanValidationProblem.PIECE_RANK_KING_EXPOSED_TO_CHECK_MULTIPLE;
      throw new SanValidationException(problem,
          Message.getString("validation.san.notPawn.specification.rank." + msgKey + ".multiple", pieceType.getName(),
              NonNullWrapperCommon.valueOf(fromRank.getNumber()), toSquare.getName()));
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
      if (pieceType == ROOK) {
        throw new SanValidationException(SanValidationProblem.PIECE_RANK_MUST_USE_FILE,
            Message.getString("validation.san.notDetermined.byRank", pieceType.getName(),
                sanConversion.fromFile().getLetter(), toSquare.getName()));
      }
      throw new SanValidationException(SanValidationProblem.PIECE_RANK_MUST_USE_FILE_OR_SQUARE,
          Message.getString("validation.san.notDetermined.byRank", pieceType.getName(),
              NonNullWrapperCommon.valueOf(sanConversion.fromRank().getNumber()), toSquare.getName()));
    }

    final File onlyPossibleFromFile = calculateOnlyPossibleFile(legalMovesCandidates, sanConversion);
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
      throw new SanValidationException(SanValidationProblem.PIECE_SQUARE_NOT_REACHABLE,
          Message.getString("validation.san.notPawn.specification.square.notReachable", pieceType.getName(),
              fromSquare.getName(), toSquare.getName()));
    }
    if (calculateNumberOfLegalMovesFromSquare(fromSquare, legalMovesCandidates) == 0) {
      final var reason = calculatePseudoLegalReason(staticPosition, havingMove);
      final var msgKey = reason == PseudoLegalReason.KING_LEFT_IN_CHECK ? "kingLeftInCheck" : "kingExposedToCheck";
      final var problem = reason == PseudoLegalReason.KING_LEFT_IN_CHECK
          ? SanValidationProblem.PIECE_SQUARE_KING_LEFT_IN_CHECK
          : SanValidationProblem.PIECE_SQUARE_KING_EXPOSED_TO_CHECK;
      throw new SanValidationException(problem,
          Message.getString("validation.san.notPawn.specification.square." + msgKey, pieceType.getName(),
              fromSquare.getName(), toSquare.getName()));
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
      if (square == Square.NONE || !staticPosition.isOwnPiece(square, havingMove, pieceType)) {
        continue;
      }
      switch (sanFormat) {
        case PIECE_NON_CAPTURING_NEITHER:
        case PIECE_CAPTURING_NEITHER:
          result.add(square);
          break;
        case PIECE_NON_CAPTURING_FILE:
        case PIECE_CAPTURING_FILE:
          if (square.getFile() == sanConversion.fromFile()) {
            result.add(square);
          }
          break;
        case PIECE_NON_CAPTURING_RANK:
        case PIECE_CAPTURING_RANK:
          if (square.getRank() == sanConversion.fromRank()) {
            result.add(square);
          }
          break;
        case PIECE_NON_CAPTURING_SQUARE:
        case PIECE_CAPTURING_SQUARE:
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
      case KING_CASTLING_QUEEN_SIDE:
      case KING_CASTLING_KING_SIDE:
      case KING_NON_CASTLING_CAPTURING:
      case KING_NON_CASTLING_NON_CAPTURING:
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
        case KING_CASTLING_QUEEN_SIDE:
        case KING_CASTLING_KING_SIDE:
        case KING_NON_CASTLING_CAPTURING:
        case KING_NON_CASTLING_NON_CAPTURING:
          throw new ProgrammingMistakeException("Handled before");
        case PAWN_CAPTURING_NON_PROMOTION:
        case PAWN_CAPTURING_PROMOTION:
          if (isFromFileMatch) {
            legalMovesForFrom.add(moveCandidate);
          }
          break;
        case PAWN_NON_CAPTURING_NON_PROMOTION:
        case PAWN_NON_CAPTURING_PROMOTION:
          // no from restriction
          legalMovesForFrom.add(moveCandidate);
          break;
        case PIECE_NON_CAPTURING_NEITHER:
        case PIECE_CAPTURING_NEITHER:
          // no from restriction
          legalMovesForFrom.add(moveCandidate);
          break;
        case PIECE_NON_CAPTURING_FILE:
        case PIECE_CAPTURING_FILE:
          if (isFromFileMatch) {
            legalMovesForFrom.add(moveCandidate);
          }
          break;
        case PIECE_NON_CAPTURING_RANK:
        case PIECE_CAPTURING_RANK:
          if (isFromRankMatch) {
            legalMovesForFrom.add(moveCandidate);
          }
          break;
        case PIECE_NON_CAPTURING_SQUARE:
        case PIECE_CAPTURING_SQUARE:
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
        case KING_CASTLING_KING_SIDE:
        case KING_CASTLING_QUEEN_SIDE:
        case KING_NON_CASTLING_CAPTURING:
        case KING_NON_CASTLING_NON_CAPTURING:
        case PAWN_CAPTURING_NON_PROMOTION:
        case PAWN_NON_CAPTURING_NON_PROMOTION:
        case PIECE_CAPTURING_SQUARE:
        case PIECE_CAPTURING_FILE:
        case PIECE_CAPTURING_NEITHER:
        case PIECE_CAPTURING_RANK:
        case PIECE_NON_CAPTURING_SQUARE:
        case PIECE_NON_CAPTURING_FILE:
        case PIECE_NON_CAPTURING_NEITHER:
        case PIECE_NON_CAPTURING_RANK:
          legalMovesForPromotion.add(moveCandidate);
          break;
        case PAWN_CAPTURING_PROMOTION:
        case PAWN_NON_CAPTURING_PROMOTION:
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
        case KING_CASTLING_KING_SIDE:
          if (moveCandidate.moveSpecification().castlingMove() == CastlingMove.KING_SIDE) {
            legalMovesForCastling.add(moveCandidate);
          }
          break;
        case KING_CASTLING_QUEEN_SIDE:
          if (moveCandidate.moveSpecification().castlingMove() == CastlingMove.QUEEN_SIDE) {
            legalMovesForCastling.add(moveCandidate);
          }
          break;
        case KING_NON_CASTLING_CAPTURING:
        case KING_NON_CASTLING_NON_CAPTURING:
        case PAWN_CAPTURING_NON_PROMOTION:
        case PAWN_NON_CAPTURING_NON_PROMOTION:
        case PIECE_CAPTURING_SQUARE:
        case PIECE_CAPTURING_FILE:
        case PIECE_CAPTURING_NEITHER:
        case PIECE_CAPTURING_RANK:
        case PIECE_NON_CAPTURING_SQUARE:
        case PIECE_NON_CAPTURING_FILE:
        case PIECE_NON_CAPTURING_NEITHER:
        case PIECE_NON_CAPTURING_RANK:
        case PAWN_CAPTURING_PROMOTION:
        case PAWN_NON_CAPTURING_PROMOTION:
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
        if (sanFormat == SanFormat.KING_CASTLING_KING_SIDE) {
          return CastlingConstants.WHITE_KING_SIDE_CASTLING_MOVE;
        }
        if (sanFormat == SanFormat.KING_CASTLING_QUEEN_SIDE) {
          return CastlingConstants.WHITE_QUEEN_SIDE_CASTLING_MOVE;
        }
        throw new IllegalArgumentException();
      case BLACK:
        if (sanFormat == SanFormat.KING_CASTLING_KING_SIDE) {
          return CastlingConstants.BLACK_KING_SIDE_CASTLING_MOVE;
        }
        if (sanFormat == SanFormat.KING_CASTLING_QUEEN_SIDE) {
          return CastlingConstants.BLACK_QUEEN_SIDE_CASTLING_MOVE;
        }
        throw new IllegalArgumentException();
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }
}
