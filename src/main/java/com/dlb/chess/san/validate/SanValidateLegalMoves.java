package com.dlb.chess.san.validate;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.analyze.ChessRuleAnalyzer;
import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.CastlingRight;
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
import com.dlb.chess.common.interfaces.ChessBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.utility.SetUtility;
import com.dlb.chess.common.utility.StaticPositionUtility;
import com.dlb.chess.enums.KingSafetyCheck;
import com.dlb.chess.enums.MovementCheck;
import com.dlb.chess.messages.Message;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.model.LegalMoveCalculation;
import com.dlb.chess.model.PseudoLegalMove;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.moves.legal.AbstractLegalMoves;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.MoveToSan;
import com.dlb.chess.san.enums.SanFormat;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.squares.to.potential.AbstractPotentialToSquares;

public abstract class SanValidateLegalMoves extends AbstractSan implements EnumConstants {

  public static MoveSpecification calculateMoveSpecificationForSan(ChessBoard board, Side havingMove, SanFormat sanFormat,
      SanConversion sanConversion, MoveSpecification legalMoveOnlyCandidate) {

    if (sanFormat == SanFormat.KING_CASTLING_QUEEN_SIDE) {
      return new MoveSpecification(CastlingMove.QUEEN_SIDE);
    }
    if (sanFormat == SanFormat.KING_CASTLING_KING_SIDE) {
      return new MoveSpecification(CastlingMove.KING_SIDE);
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
          return new MoveSpecification(fromSquare, toSquare);
        }
        // we calculate this with san information and knowing it's a legal move (so e4
        // is e2-e4 xor e3-e4)
        final Square potentialJumpOverSquare = Square.calculateJumpOverSquare(havingMove, toSquare);
        if (board.getStaticPosition().get(potentialJumpOverSquare) == Piece.NONE) {
          // two square advance
          final File fromFile = toSquare.getFile(); // moving straight forward
          final Rank fromRank = Rank.calculatePawnInitialRank(havingMove);
          final Square fromSquare = Square.calculate(fromFile, fromRank);
          return new MoveSpecification(fromSquare, toSquare);
        }

        // one square advance
        final var fromSquare = potentialJumpOverSquare;
        return new MoveSpecification(fromSquare, toSquare);
      }
      case PAWN_CAPTURING_NON_PROMOTION: {
        // from file is in the san and from rank is the rank before to rank

        final Rank fromRank = Rank.calculatePreviousRank(havingMove, toSquare.getRank());
        final Square fromSquare = Square.calculate(sanConversion.fromFile(), fromRank);
        return new MoveSpecification(fromSquare, toSquare);
      }
      case PAWN_NON_CAPTURING_PROMOTION: {
        // from file equals to file and from rank is the rank before to rank
        final File fromFile = toSquare.getFile(); // moving straight forward
        final Rank fromRank = Rank.calculatePreviousRank(havingMove, toSquare.getRank());
        final Square fromSquare = Square.calculate(fromFile, fromRank);
        return new MoveSpecification(fromSquare, toSquare, sanConversion.promotionPieceType());
      }
      case PAWN_CAPTURING_PROMOTION: {
        // from file is in the san and from rank is the rank before to rank
        final Rank fromRank = Rank.calculatePreviousRank(havingMove, toSquare.getRank());
        final Square fromSquare = Square.calculate(sanConversion.fromFile(), fromRank);
        return new MoveSpecification(fromSquare, toSquare, sanConversion.promotionPieceType());
      }
      case RNBQ_CAPTURING_SQUARE: {
        // san is enough to determine from square
        final Square fromSquare = AbstractSan.calculateFromSquare(sanConversion);
        return new MoveSpecification(fromSquare, toSquare);
      }
      case KING_NON_CASTLING_CAPTURING:
      case KING_NON_CASTLING_NON_CAPTURING:
      case RNBQ_CAPTURING_FILE:
      case RNBQ_CAPTURING_NEITHER:
      case RNBQ_CAPTURING_RANK:
      case RNBQ_NON_CAPTURING_SQUARE:
      case RNBQ_NON_CAPTURING_FILE:
      case RNBQ_NON_CAPTURING_NEITHER:
      case RNBQ_NON_CAPTURING_RANK: {
        // legal move is required to determine from square
        final Square fromSquare = legalMoveOnlyCandidate.fromSquare();
        return new MoveSpecification(fromSquare, toSquare);
      }
      default:
        throw new IllegalArgumentException();

    }
  }

  public static Set<LegalMove> calculateLegalMovesCandidates(ChessBoard board, Side havingMove, SanParse sanParse) {
    final var sanFormat = sanParse.sanFormat();
    final var sanConversion = sanParse.sanConversion();

    // for castling we need to filter the castling moves
    if (sanFormat.isKingCastlingMove()) {
      return filterCastlingMove(board.getLegalMoveSet());
    }

    final PieceType pieceType = sanConversion.movingPieceType();
    final Piece piece = PieceType.calculate(havingMove, pieceType);

    final Set<LegalMove> legalMoveSetForMovingPiece = MoveToSan.calculateLegalMoveSetForMovingPiece(piece,
        board.getLegalMoveSet());
    // for non castling moves we need to filter by the to square (which is always set for non castling)
    final Square toSquare = sanConversion.toSquare();
    final Set<LegalMove> legalMovesCandidates = filterLegalMovesCandidates(legalMoveSetForMovingPiece, toSquare);

    // for pawn moves we must filter additionally by the from file!!
    if (sanFormat == SanFormat.PAWN_CAPTURING_NON_PROMOTION || sanFormat == SanFormat.PAWN_CAPTURING_PROMOTION) {
      return calculateLegalMovesCandidates(legalMovesCandidates, sanConversion.fromFile());
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

  public static void validateAgainstLegalMoves(ChessBoard board, Side havingMove, Set<LegalMove> legalMovesCandidates,
      SanFormat sanFormat, SanConversion sanConversion) {

    final StaticPosition staticPosition = board.getStaticPosition();

    // we need an early return for castling first so for the remaining cases we can
    // calculate the to square
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
    final PieceType pieceType = sanConversion.movingPieceType();

    switch (sanFormat) {
      case KING_CASTLING_QUEEN_SIDE, KING_CASTLING_KING_SIDE -> throw new ProgrammingMistakeException(
          "Invalid program flow, the castling must be handled at this point");
      case KING_NON_CASTLING_NON_CAPTURING, KING_NON_CASTLING_CAPTURING -> validateAgainstLegalMovesForKingNonCastling(
          staticPosition, havingMove, legalMovesCandidates, toSquare);
      case PAWN_NON_CAPTURING_NON_PROMOTION, PAWN_CAPTURING_NON_PROMOTION, PAWN_NON_CAPTURING_PROMOTION, PAWN_CAPTURING_PROMOTION -> validateAgainstLegalMovesForPawn(
          staticPosition, havingMove, legalMovesCandidates, pieceType, sanFormat, sanConversion, toSquare,
          board.getEnPassantCaptureTargetSquare());
      case RNBQ_NON_CAPTURING_NEITHER, RNBQ_CAPTURING_NEITHER -> validateAgainstLegalMovesForPieceNeither(
          staticPosition, havingMove, legalMovesCandidates, pieceType, toSquare);
      case RNBQ_NON_CAPTURING_FILE, RNBQ_CAPTURING_FILE -> validateAgainstLegalMovesForPieceFile(staticPosition,
          havingMove, legalMovesCandidates, pieceType, sanFormat, sanConversion, toSquare);
      case RNBQ_NON_CAPTURING_RANK, RNBQ_CAPTURING_RANK -> validateAgainstLegalMovesForPieceRank(staticPosition,
          havingMove, legalMovesCandidates, pieceType, sanFormat, sanConversion, toSquare);
      case RNBQ_NON_CAPTURING_SQUARE, RNBQ_CAPTURING_SQUARE -> validateAgainstLegalMovesForPieceSquare(staticPosition,
          havingMove, legalMovesCandidates, pieceType, sanFormat, sanConversion, toSquare);
      default -> throw new IllegalArgumentException();
    }
  }

  private static void validateAgainstLegalMovesForKingNonCastling(StaticPosition staticPosition, Side havingMove,
      Set<LegalMove> legalMovesCandidates, Square toSquare) {
    if (!legalMovesCandidates.isEmpty()) {
      return;
    }
    final Set<PseudoLegalMove> pseudoLegalMoves = calculatePseudoLegalMovesForKingNonCastling(staticPosition,
        havingMove, toSquare);
    if (pseudoLegalMoves.isEmpty()) {
      throw new SanValidationException(SanValidationProblem.NOT_REACHABLE_KING_NON_CASTLING,
          Message.getString("validation.san.notReachable.king.nonCastling", toSquare.getName()));
    }
    // Pseudo-legal but not legal: classify via the analyzer. For king moves the safety
    // reasons (KING_CAPTURES_GUARDED_PIECE / KING_MOVES_NEXT_TO_OPPONENT_KING /
    // KING_MOVES_TO_ATTACKED_EMPTY_SQUARE) live in MovementCheck rather than the LEFT/EXPOSED
    // distinction used for non-king pieces.
    final Square kingSquare = StaticPositionUtility.calculateKingSquare(staticPosition, havingMove);
    final MovementCheck movementCheck = ChessRuleAnalyzer.analyzeMovement(staticPosition, havingMove, Square.NONE,
        new MoveSpecification(kingSquare, toSquare));
    switch (movementCheck) {
      case KING_CAPTURES_GUARDED_PIECE -> throw new SanValidationException(
          SanValidationProblem.KING_CAPTURES_GUARDED_PIECE,
          Message.getString("validation.san.king.capturesGuardedPiece", toSquare.getName()));
      case KING_MOVES_NEXT_TO_OPPONENT_KING -> throw new SanValidationException(
          SanValidationProblem.KING_MOVES_NEXT_TO_OPPONENT_KING,
          Message.getString("validation.san.king.movesNextToOpponentKing", toSquare.getName()));
      case KING_MOVES_TO_ATTACKED_EMPTY_SQUARE -> throw new SanValidationException(
          SanValidationProblem.KING_MOVES_TO_ATTACKED_EMPTY_SQUARE,
          Message.getString("validation.san.king.movesToAttackedEmptySquare", toSquare.getName()));
      default -> throw new ProgrammingMistakeException(
          "Unexpected MovementCheck for king non-castling pseudo-legal move: " + movementCheck);
    }
  }

  private static Set<PseudoLegalMove> calculatePseudoLegalMovesForKingNonCastling(StaticPosition staticPosition,
      Side havingMove, Square toSquare) {
    final Set<PseudoLegalMove> allPseudoLegal = new TreeSet<>();
    for (final Square fromSquare : Square.REAL) {
      if (!staticPosition.isOwnPiece(fromSquare, havingMove, KING)) {
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

  private static void validateAgainstLegalMovesForPawn(StaticPosition staticPosition, Side havingMove,
      Set<LegalMove> legalMovesCandidates, PieceType pieceType, SanFormat sanFormat, SanConversion sanConversion,
      Square toSquare, Square enPassantCaptureTargetSquare) {
    if (!legalMovesCandidates.isEmpty()) {
      return;
    }
    final var isCapturing = sanFormat == SanFormat.PAWN_CAPTURING_NON_PROMOTION
        || sanFormat == SanFormat.PAWN_CAPTURING_PROMOTION;
    final Set<PseudoLegalMove> pseudoLegalMoves = calculatePseudoLegalMovesForPawn(staticPosition, havingMove,
        isCapturing, sanConversion, toSquare, enPassantCaptureTargetSquare);
    if (pseudoLegalMoves.isEmpty()) {
      if (isCapturing) {
        throw new SanValidationException(SanValidationProblem.NOT_REACHABLE_PAWN_CAPTURING,
            Message.getString("validation.san.notReachable.pawn.capturing", pieceType.getName(), toSquare.getName()));
      }
      throw new SanValidationException(SanValidationProblem.NOT_REACHABLE_PAWN_NON_CAPTURING,
          Message.getString("validation.san.notReachable.pawn.nonCapturing", pieceType.getName(), toSquare.getName()));
    }
    final var reason = calculatePseudoLegalKingSafety(staticPosition, havingMove);
    if (reason == KingSafetyCheck.NON_KING_LEFT_IN_CHECK) {
      throw new SanValidationException(SanValidationProblem.KING_LEFT_IN_CHECK_PAWN,
          Message.getString("validation.san.kingLeftInCheck.pawn", pieceType.getName(), toSquare.getName()));
    }
    throw new SanValidationException(SanValidationProblem.KING_EXPOSED_TO_CHECK_PAWN,
        Message.getString("validation.san.kingExposedToCheck.pawn", pieceType.getName(), toSquare.getName()));
  }

  private static Set<PseudoLegalMove> calculatePseudoLegalMovesForPawn(StaticPosition staticPosition, Side havingMove,
      boolean isCapturing, SanConversion sanConversion, Square toSquare, Square enPassantCaptureTargetSquare) {
    final var filterFromFile = isCapturing ? sanConversion.fromFile() : toSquare.getFile();
    final Set<PseudoLegalMove> allPseudoLegal = new TreeSet<>();
    for (final Square fromSquare : Square.REAL) {
      if (fromSquare.getFile() != filterFromFile || !staticPosition.isOwnPiece(fromSquare, havingMove, PAWN)) {
        continue;
      }
      final Set<Square> potentialToSquares = AbstractPotentialToSquares.calculatePotentialToSquare(staticPosition,
          enPassantCaptureTargetSquare, havingMove, fromSquare);
      if (potentialToSquares.contains(toSquare)) {
        final LegalMoveCalculation calc = AbstractLegalMoves.calculateLegalMoveCalculation(staticPosition, havingMove,
            fromSquare, NonNullWrapperCommon.setOf(toSquare));
        allPseudoLegal.addAll(calc.pseudoLegalMoveSet());
      }
    }
    return allPseudoLegal;
  }

  private static void validateAgainstLegalMovesForPieceNeither(StaticPosition staticPosition, Side havingMove,
      Set<LegalMove> legalMovesCandidates, PieceType pieceType, Square toSquare) {
    if (legalMovesCandidates.isEmpty()) {
      final Set<PseudoLegalMove> pseudoLegalMoves = calculatePseudoLegalMovesForPieceNeither(staticPosition, havingMove,
          pieceType, toSquare);

      if (pseudoLegalMoves.isEmpty()) {
        if (countPiecesOfType(staticPosition, havingMove, pieceType) == 1) {
          throw new SanValidationException(SanValidationProblem.NOT_REACHABLE_RNBQ_NEITHER_SINGLE, Message
              .getString("validation.san.notReachable.rnbq.neither.single", pieceType.getName(), toSquare.getName()));
        }
        throw new SanValidationException(SanValidationProblem.NOT_REACHABLE_RNBQ_NEITHER_MULTIPLE, Message
            .getString("validation.san.notReachable.rnbq.neither.multiple", pieceType.getName(), toSquare.getName()));
      }
      final var reason = calculatePseudoLegalKingSafety(staticPosition, havingMove);
      if (pseudoLegalMoves.size() == 1) {
        final Square fromSquare = SetUtility.getOnly(pseudoLegalMoves).moveSpecification().fromSquare();
        if (reason == KingSafetyCheck.NON_KING_LEFT_IN_CHECK) {
          throw new SanValidationException(SanValidationProblem.KING_LEFT_IN_CHECK_RNBQ_NEITHER_SINGLE,
              Message.getString("validation.san.kingLeftInCheck.rnbq.neither.single", pieceType.getName(),
                  fromSquare.getName(), toSquare.getName()));
        }
        throw new SanValidationException(SanValidationProblem.KING_EXPOSED_TO_CHECK_RNBQ_NEITHER_SINGLE,
            Message.getString("validation.san.kingExposedToCheck.rnbq.neither.single", pieceType.getName(),
                fromSquare.getName(), toSquare.getName()));
      }
      if (reason == KingSafetyCheck.NON_KING_LEFT_IN_CHECK) {
        throw new SanValidationException(SanValidationProblem.KING_LEFT_IN_CHECK_RNBQ_NEITHER_MULTIPLE,
            Message.getString("validation.san.kingLeftInCheck.rnbq.neither.multiple", pieceType.getName(),
                toSquare.getName()));
      }
      throw new SanValidationException(SanValidationProblem.KING_EXPOSED_TO_CHECK_RNBQ_NEITHER_MULTIPLE,
          Message.getString("validation.san.kingExposedToCheck.rnbq.neither.multiple", pieceType.getName(),
              toSquare.getName()));
    }
    if (legalMovesCandidates.size() > 1) {
      throw new SanValidationException(
          SanValidationProblem.INSUFFICIENTLY_SPECIFIED_RNBQ_NEITHER_EITHER_FILE_OR_RANK_OR_SQUARE_REQUIRED,
          Message.getString("validation.san.insufficientlySpecified.rnbq.neither.eitherFileOrRankOrSquareRequired",
              pieceType.getName(), toSquare.getName()));
    }
  }

  private static void throwCastlingException(ChessBoard board, Side havingMove, String sideLabel,
      CastlingMove castlingMove) {
    final CastlingRight castlingRight = board.getCastlingRight(havingMove);
    final var castlingCheck = castlingMove == CastlingMove.QUEEN_SIDE
        ? CastlingUtility.calculateQueenSideCastlingCheck(board.getStaticPosition(), havingMove, castlingRight)
        : CastlingUtility.calculateKingSideCastlingCheck(board.getStaticPosition(), havingMove, castlingRight);

    final var castlingRightLoss = board.getCastlingRightLoss(havingMove, castlingMove);
    final String message;

    switch (castlingCheck) {
      case FINAL_NO_RIGHT: {
        final var rookLabel = castlingMove == CastlingMove.QUEEN_SIDE ? "queen-side" : "king-side";
        message = switch (castlingRightLoss) {
          case KING_MOVED -> Message.getString("validation.san.kingCastling.finalNoRight.kingMoved", sideLabel);
          case ROOK_MOVED -> Message.getString("validation.san.kingCastling.finalNoRight.rookMoved", sideLabel,
              rookLabel);
          case ROOK_CAPTURED -> Message.getString("validation.san.kingCastling.finalNoRight.rookCaptured", sideLabel,
              rookLabel);
          case CASTLED -> Message.getString("validation.san.kingCastling.finalNoRight.castled", sideLabel);
          case UNKNOWN_FEN_IMPORT -> Message.getString("validation.san.kingCastling.finalNoRight.unknownFenImport",
              sideLabel);
          default -> throw new IllegalArgumentException();
        };
        break;
      }
      case TEMPORARY_SQUARES_NOT_EMPTY:
        message = Message.getString("validation.san.kingCastling.temporary.squaresNotEmpty", sideLabel);
        break;
      case TEMPORARY_KING_IN_CHECK:
        message = Message.getString("validation.san.kingCastling.temporary.kingInCheck", sideLabel);
        break;
      case TEMPORARY_KING_TRAVELS_THROUGH_CHECK:
        message = Message.getString("validation.san.kingCastling.temporary.kingTravelsThroughCheck", sideLabel);
        break;
      case TEMPORARY_KING_ENDS_IN_CHECK:
        message = Message.getString("validation.san.kingCastling.temporary.kingEndsInCheck", sideLabel);
        break;
      case SUCCESS:
        throw new ProgrammingMistakeException("Castling check returned SUCCESS but move is not in legal moves");
      default:
        throw new ProgrammingMistakeException("Unexpected castling check result: " + castlingCheck);
    }

    throw new SanValidationException(CastlingCheckMapper.map(castlingCheck, castlingRightLoss), message,
        castlingCheck.toMoveCheck(castlingRightLoss), castlingRightLoss);
  }

  private static KingSafetyCheck calculatePseudoLegalKingSafety(StaticPosition staticPosition, Side havingMove) {
    // is check works because already narrowed down by legal move calculation to one or the other case
    if (StaticPositionUtility.calculateIsCheck(staticPosition, havingMove)) {
      return KingSafetyCheck.NON_KING_LEFT_IN_CHECK;
    }
    return KingSafetyCheck.NON_KING_EXPOSED_TO_CHECK;
  }

  private static Set<PseudoLegalMove> calculatePseudoLegalMovesForPieceNeither(StaticPosition staticPosition,
      Side havingMove, PieceType pieceType, Square toSquare) {
    final Set<PseudoLegalMove> allPseudoLegal = new TreeSet<>();
    for (final Square fromSquare : Square.REAL) {
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
    for (final Square square : Square.REAL) {
      if (staticPosition.isOwnPiece(square, havingMove, pieceType)) {
        count++;
      }
    }
    return count;
  }

  private static int countPiecesOfTypeOnFile(StaticPosition staticPosition, Side havingMove, PieceType pieceType,
      File file) {
    var count = 0;
    for (final Square square : Square.REAL) {
      if (square.getFile() == file && staticPosition.isOwnPiece(square, havingMove, pieceType)) {
        count++;
      }
    }
    return count;
  }

  private static Set<PseudoLegalMove> calculatePseudoLegalMovesForPieceFile(StaticPosition staticPosition,
      Side havingMove, PieceType pieceType, File file, Square toSquare) {
    final Set<PseudoLegalMove> allPseudoLegal = new TreeSet<>();
    for (final Square fromSquare : Square.REAL) {
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
    for (final Square square : Square.REAL) {
      if (square.getRank() == rank && staticPosition.isOwnPiece(square, havingMove, pieceType)) {
        count++;
      }
    }
    return count;
  }

  private static Set<PseudoLegalMove> calculatePseudoLegalMovesForPieceRank(StaticPosition staticPosition,
      Side havingMove, PieceType pieceType, Rank rank, Square toSquare) {
    final Set<PseudoLegalMove> allPseudoLegal = new TreeSet<>();
    for (final Square fromSquare : Square.REAL) {
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
        throw new SanValidationException(SanValidationProblem.NOT_REACHABLE_RNBQ_FILE_SINGLE,
            Message.getString("validation.san.notReachable.rnbq.file.single", pieceType.getName(),
                pieceSquare.getName(), toSquare.getName()));
      }
      throw new SanValidationException(SanValidationProblem.NOT_REACHABLE_RNBQ_FILE_MULTIPLE,
          Message.getString("validation.san.notReachable.rnbq.file.multiple", pieceType.getName(),
              fromFile.getLetterString(), toSquare.getName()));
    }

    final var numberOfLegalMovesFromSameFile = calculateNumberOfLegalMovesFromFile(fromFile, legalMovesCandidates);
    if (numberOfLegalMovesFromSameFile == 0) {
      final Set<PseudoLegalMove> pseudoLegalMoves = calculatePseudoLegalMovesForPieceFile(staticPosition, havingMove,
          pieceType, fromFile, toSquare);
      final var reason = calculatePseudoLegalKingSafety(staticPosition, havingMove);
      if (pseudoLegalMoves.size() == 1) {
        final Square pieceSquare = SetUtility.getOnly(pseudoLegalMoves).moveSpecification().fromSquare();
        if (reason == KingSafetyCheck.NON_KING_LEFT_IN_CHECK) {
          throw new SanValidationException(SanValidationProblem.KING_LEFT_IN_CHECK_RNBQ_FILE_SINGLE,
              Message.getString("validation.san.kingLeftInCheck.rnbq.file.single", pieceType.getName(),
                  pieceSquare.getName(), toSquare.getName()));
        }
        throw new SanValidationException(SanValidationProblem.KING_EXPOSED_TO_CHECK_RNBQ_FILE_SINGLE,
            Message.getString("validation.san.kingExposedToCheck.rnbq.file.single", pieceType.getName(),
                pieceSquare.getName(), toSquare.getName()));
      }
      if (reason == KingSafetyCheck.NON_KING_LEFT_IN_CHECK) {
        throw new SanValidationException(SanValidationProblem.KING_LEFT_IN_CHECK_RNBQ_FILE_MULTIPLE,
            Message.getString("validation.san.kingLeftInCheck.rnbq.file.multiple", pieceType.getName(),
                fromFile.getLetterString(), toSquare.getName()));
      }
      throw new SanValidationException(SanValidationProblem.KING_EXPOSED_TO_CHECK_RNBQ_FILE_MULTIPLE,
          Message.getString("validation.san.kingExposedToCheck.rnbq.file.multiple", pieceType.getName(),
              fromFile.getLetterString(), toSquare.getName()));
    }

    if (legalMovesCandidates.size() == 1) {
      throw new SanValidationException(SanValidationProblem.OVERSPECIFIED_RNBQ_FILE_ONLY_ONE_LEGAL_MOVE,
          Message.getString("validation.san.overspecified.rnbq.file.onlyOneLegalMove"));
    }

    if (!calculateHasOtherFilesHavingLegalMoves(sanConversion.fromFile(), legalMovesCandidates)) {
      if (numberOfLegalMovesFromSameFile < 2) {
        throw new ProgrammingMistakeException("A programming assumption about the rank turned out to be wrong");
      }
      throw new SanValidationException(SanValidationProblem.INSUFFICIENTLY_SPECIFIED_RNBQ_FILE_RANK_REQUIRED,
          Message.getString("validation.san.insufficientlySpecified.rnbq.file.rankRequired", pieceType.getName(),
              sanConversion.fromFile().getLetterString(), toSquare.getName()));
    }

    if (numberOfLegalMovesFromSameFile >= 2) {
      if (pieceType == ROOK) {
        throw new SanValidationException(SanValidationProblem.INSUFFICIENTLY_SPECIFIED_RNBQ_FILE_RANK_REQUIRED,
            Message.getString("validation.san.insufficientlySpecified.rnbq.file.rankRequired", pieceType.getName(),
                sanConversion.fromFile().getLetterString(), toSquare.getName()));
      }
      throw new SanValidationException(
          SanValidationProblem.INSUFFICIENTLY_SPECIFIED_RNBQ_FILE_EITHER_RANK_OR_SQUARE_REQUIRED,
          Message.getString("validation.san.insufficientlySpecified.rnbq.file.eitherRankOrSquareRequired",
              pieceType.getName(), sanConversion.fromFile().getLetterString(), toSquare.getName()));
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
        throw new SanValidationException(SanValidationProblem.NOT_REACHABLE_RNBQ_RANK_SINGLE,
            Message.getString("validation.san.notReachable.rnbq.rank.single", pieceType.getName(),
                pieceSquare.getName(), toSquare.getName()));
      }
      throw new SanValidationException(SanValidationProblem.NOT_REACHABLE_RNBQ_RANK_MULTIPLE,
          Message.getString("validation.san.notReachable.rnbq.rank.multiple", pieceType.getName(),
              NonNullWrapperCommon.valueOf(fromRank.getNumber()), toSquare.getName()));
    }

    final var numberOfLegalMovesFromSameRank = calculateNumberOfLegalMovesFromRank(fromRank, legalMovesCandidates);
    if (numberOfLegalMovesFromSameRank == 0) {
      final Set<PseudoLegalMove> pseudoLegalMoves = calculatePseudoLegalMovesForPieceRank(staticPosition, havingMove,
          pieceType, fromRank, toSquare);
      final var reason = calculatePseudoLegalKingSafety(staticPosition, havingMove);
      if (pseudoLegalMoves.size() == 1) {
        final Square pieceSquare = SetUtility.getOnly(pseudoLegalMoves).moveSpecification().fromSquare();
        if (reason == KingSafetyCheck.NON_KING_LEFT_IN_CHECK) {
          throw new SanValidationException(SanValidationProblem.KING_LEFT_IN_CHECK_RNBQ_RANK_SINGLE,
              Message.getString("validation.san.kingLeftInCheck.rnbq.rank.single", pieceType.getName(),
                  pieceSquare.getName(), toSquare.getName()));
        }
        throw new SanValidationException(SanValidationProblem.KING_EXPOSED_TO_CHECK_RNBQ_RANK_SINGLE,
            Message.getString("validation.san.kingExposedToCheck.rnbq.rank.single", pieceType.getName(),
                pieceSquare.getName(), toSquare.getName()));
      }
      if (reason == KingSafetyCheck.NON_KING_LEFT_IN_CHECK) {
        throw new SanValidationException(SanValidationProblem.KING_LEFT_IN_CHECK_RNBQ_RANK_MULTIPLE,
            Message.getString("validation.san.kingLeftInCheck.rnbq.rank.multiple", pieceType.getName(),
                NonNullWrapperCommon.valueOf(fromRank.getNumber()), toSquare.getName()));
      }
      throw new SanValidationException(SanValidationProblem.KING_EXPOSED_TO_CHECK_RNBQ_RANK_MULTIPLE,
          Message.getString("validation.san.kingExposedToCheck.rnbq.rank.multiple", pieceType.getName(),
              NonNullWrapperCommon.valueOf(fromRank.getNumber()), toSquare.getName()));
    }

    if (legalMovesCandidates.size() == 1) {
      throw new SanValidationException(SanValidationProblem.OVERSPECIFIED_RNBQ_RANK_ONLY_ONE_LEGAL_MOVE,
          Message.getString("validation.san.overspecified.rnbq.rank.onlyOneLegalMove"));
    }

    if (!calculateHasOtherRanksHavingLegalMoves(sanConversion.fromRank(), legalMovesCandidates)) {
      if (numberOfLegalMovesFromSameRank < 2) {
        throw new ProgrammingMistakeException("A programming assumption about the file turned out to be wrong");
      }
      throw new SanValidationException(SanValidationProblem.INSUFFICIENTLY_SPECIFIED_RNBQ_RANK_FILE_REQUIRED,
          Message.getString("validation.san.insufficientlySpecified.rnbq.rank.fileRequired", pieceType.getName(),
              NonNullWrapperCommon.valueOf(sanConversion.fromRank().getNumber()), toSquare.getName()));
    }

    if (numberOfLegalMovesFromSameRank >= 2) {
      if (pieceType == ROOK) {
        throw new SanValidationException(SanValidationProblem.INSUFFICIENTLY_SPECIFIED_RNBQ_RANK_FILE_REQUIRED,
            Message.getString("validation.san.insufficientlySpecified.rnbq.rank.fileRequired", pieceType.getName(),
                sanConversion.fromFile().getLetterString(), toSquare.getName()));
      }
      throw new SanValidationException(
          SanValidationProblem.INSUFFICIENTLY_SPECIFIED_RNBQ_RANK_EITHER_FILE_OR_SQUARE_REQUIRED,
          Message.getString("validation.san.insufficientlySpecified.rnbq.rank.eitherFileOrSquareRequired",
              pieceType.getName(), NonNullWrapperCommon.valueOf(sanConversion.fromRank().getNumber()),
              toSquare.getName()));
    }

    final File onlyPossibleFromFile = calculateOnlyPossibleFile(legalMovesCandidates, sanConversion);
    if (onlyPossibleFromFile == File.NONE) {
      throw new ProgrammingMistakeException(
          "The program made the wrong assumption that the from file is determined at this point");
    }
    final var numberOfLegalMovesFromSameFile = calculateNumberOfLegalMovesFromFile(onlyPossibleFromFile,
        legalMovesCandidates);

    if (numberOfLegalMovesFromSameFile == 1) {
      throw new SanValidationException(SanValidationProblem.NON_STANDARD_SPECIFIED_RNBQ_RANK_INSTEAD_OF_FILE,
          Message.getString("validation.san.nonStandardSpecified.rnbq.rank.rankInsteadOfFile"));
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
      throw new SanValidationException(SanValidationProblem.NOT_REACHABLE_RNBQ_SQUARE, Message.getString(
          "validation.san.notReachable.rnbq.square", pieceType.getName(), fromSquare.getName(), toSquare.getName()));
    }
    if (calculateNumberOfLegalMovesFromSquare(fromSquare, legalMovesCandidates) == 0) {
      final var reason = calculatePseudoLegalKingSafety(staticPosition, havingMove);
      if (reason == KingSafetyCheck.NON_KING_LEFT_IN_CHECK) {
        throw new SanValidationException(SanValidationProblem.KING_LEFT_IN_CHECK_RNBQ_SQUARE,
            Message.getString("validation.san.kingLeftInCheck.rnbq.square", pieceType.getName(), fromSquare.getName(),
                toSquare.getName()));
      }
      throw new SanValidationException(SanValidationProblem.KING_EXPOSED_TO_CHECK_RNBQ_SQUARE,
          Message.getString("validation.san.kingExposedToCheck.rnbq.square", pieceType.getName(), fromSquare.getName(),
              toSquare.getName()));
    }

    if (legalMovesCandidates.size() == 1) {
      throw new SanValidationException(SanValidationProblem.OVERSPECIFIED_RNBQ_SQUARE_ONLY_ONE_LEGAL_MOVE,
          Message.getString("validation.san.overspecified.rnbq.square.onlyOneLegalMove"));
    }

    final var numberOfLegalMovesFromOtherFiles = calculateNumberOfLegalMovesFromOtherFiles(sanConversion.fromFile(),
        legalMovesCandidates);

    final var numberOfLegalMovesFromFile = calculateNumberOfLegalMovesFromFile(sanConversion.fromFile(),
        legalMovesCandidates);

    if (numberOfLegalMovesFromFile == 2 && numberOfLegalMovesFromOtherFiles == 0) {
      throw new SanValidationException(SanValidationProblem.OVERSPECIFIED_RNBQ_SQUARE_FILE_NOT_NECESSARY,
          Message.getString("validation.san.overspecified.rnbq.square.fileNotNecessary"));
    }

    if (numberOfLegalMovesFromFile == 1 && numberOfLegalMovesFromOtherFiles >= 1) {
      throw new SanValidationException(SanValidationProblem.OVERSPECIFIED_RNBQ_SQUARE_RANK_NOT_NECESSARY,
          Message.getString("validation.san.overspecified.rnbq.square.rankNotNecessary"));
    }
  }

  private static Set<Square> calculatePieceCandidateSquareSet(StaticPosition staticPosition, Side havingMove,
      PieceType pieceType, SanFormat sanFormat, SanConversion sanConversion) {
    final Set<Square> result = new TreeSet<>();
    for (final Square square : Square.REAL) {
      if (!staticPosition.isOwnPiece(square, havingMove, pieceType)) {
        continue;
      }
      switch (sanFormat) {
        case RNBQ_NON_CAPTURING_NEITHER:
        case RNBQ_CAPTURING_NEITHER:
          result.add(square);
          break;
        case RNBQ_NON_CAPTURING_FILE:
        case RNBQ_CAPTURING_FILE:
          if (square.getFile() == sanConversion.fromFile()) {
            result.add(square);
          }
          break;
        case RNBQ_NON_CAPTURING_RANK:
        case RNBQ_CAPTURING_RANK:
          if (square.getRank() == sanConversion.fromRank()) {
            result.add(square);
          }
          break;
        case RNBQ_NON_CAPTURING_SQUARE:
        case RNBQ_CAPTURING_SQUARE:
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
        case RNBQ_NON_CAPTURING_NEITHER:
        case RNBQ_CAPTURING_NEITHER:
          // no from restriction
          legalMovesForFrom.add(moveCandidate);
          break;
        case RNBQ_NON_CAPTURING_FILE:
        case RNBQ_CAPTURING_FILE:
          if (isFromFileMatch) {
            legalMovesForFrom.add(moveCandidate);
          }
          break;
        case RNBQ_NON_CAPTURING_RANK:
        case RNBQ_CAPTURING_RANK:
          if (isFromRankMatch) {
            legalMovesForFrom.add(moveCandidate);
          }
          break;
        case RNBQ_NON_CAPTURING_SQUARE:
        case RNBQ_CAPTURING_SQUARE:
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
        case RNBQ_CAPTURING_SQUARE:
        case RNBQ_CAPTURING_FILE:
        case RNBQ_CAPTURING_NEITHER:
        case RNBQ_CAPTURING_RANK:
        case RNBQ_NON_CAPTURING_SQUARE:
        case RNBQ_NON_CAPTURING_FILE:
        case RNBQ_NON_CAPTURING_NEITHER:
        case RNBQ_NON_CAPTURING_RANK:
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
        case RNBQ_CAPTURING_SQUARE:
        case RNBQ_CAPTURING_FILE:
        case RNBQ_CAPTURING_NEITHER:
        case RNBQ_CAPTURING_RANK:
        case RNBQ_NON_CAPTURING_SQUARE:
        case RNBQ_NON_CAPTURING_FILE:
        case RNBQ_NON_CAPTURING_NEITHER:
        case RNBQ_NON_CAPTURING_RANK:
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
