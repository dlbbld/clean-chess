package com.dlb.chess.unwinnability;

import java.util.HashMap;
import java.util.Map;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.model.DynamicPosition;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.LegalMove;

public class UnwinnableQuickAnalyzer {

  private static final boolean IS_ALIGN_QUICK_WITH_AMBRONA_REFERENCE_IMPLEMENTATION = true;

  public static UnwinnabilityQuickVerdict unwinnableQuick(Board board, Side c) {
    return unwinnableQuick(board, c, false, new MobilitySolution());
  }

  /**
   * Runs the algorithm on a fresh detection-off board built from the caller's FEN. Isolation has two effects: (1) the
   * caller's board is not mutated, and (2) the analyzer's internal {@code board.move(...)} calls don't trigger the
   * dead-position auto-detect (which itself runs this analyzer). Repetition history from the caller's game is lost on
   * the fresh board — acceptable for the quick check, whose verdict is conservative anyway.
   */
  public static UnwinnabilityQuickVerdict unwinnableQuick(Board input, Side c, boolean isHasMobilitySolution,
      MobilitySolution calculatedMobilitySolution) {
    final Board board = copyCurrentPositionForQuickSearch(input);

    final String invariant = board.getFen();

    // 1: advance the position as long as there is only one legal move
    // if position is advanced cannot use the provided mobility solution if any
    var isCanUseMobilitySolution = true;
    var isFivefoldOrSeventyFiveMove = board.isFivefoldRepetition() || board.isSeventyFiveMove();
    var isForcedMove = true;
    var countHalfmoves = 0;
    while (isForcedMove && !isFivefoldOrSeventyFiveMove) {
      isCanUseMobilitySolution = false;
      if (board.isCheckmate()) {
        // crucial, store the side before undoing moves, as it can change with undoing moves!!
        final Side sideBeingCheckmated = board.getHavingMove();
        unperformHalfmoves(board, countHalfmoves);
        if (!invariant.equals(board.getFen())) {
          throw new ProgrammingMistakeException("Board was changed");
        }
        if (sideBeingCheckmated == c) {
          return UnwinnabilityQuickVerdict.UNWINNABLE;
        }
        return calculateWinnableVerdict();
      }

      if (board.isInsufficientMaterial(c) || board.isStalemate()) {
        unperformHalfmoves(board, countHalfmoves);
        if (!invariant.equals(board.getFen())) {
          throw new ProgrammingMistakeException("Board was changed");
        }
        return UnwinnabilityQuickVerdict.UNWINNABLE;
      }

      isForcedMove = board.getLegalMoves().size() == 1;
      if (isForcedMove) {
        final LegalMove legalMove = Nulls.getFirst(board.getLegalMoves());
        board.move(legalMove.moveSpecification());
        isFivefoldOrSeventyFiveMove = board.isFivefoldRepetition() || board.isSeventyFiveMove();
        countHalfmoves++;
      }
    }

    // 2: perform a depth-first search over the tree of variations of pos and interrupt the
    // search if (i) checkmate is found for player c or (ii) depth D is reached
    final String invariantTwo = board.getFen();
    final var checkmateSearchResult = FindHelpMateInterrupt.calculateHelpmate(board, c);
    if (!invariantTwo.equals(board.getFen())) {
      throw new ProgrammingMistakeException("Board was changed");
    }

    switch (checkmateSearchResult) {
      case YES:
        // 3: if checkmate was found on the previous search then return Winnable
        unperformHalfmoves(board, countHalfmoves);
        if (!invariant.equals(board.getFen())) {
          throw new ProgrammingMistakeException("Board was changed");
        }
        return calculateWinnableVerdict();
      case NO:
        // 4: else if the search was not interrupted then return Unwinnable
        unperformHalfmoves(board, countHalfmoves);
        if (!invariant.equals(board.getFen())) {
          throw new ProgrammingMistakeException("Board was changed");
        }
        return UnwinnabilityQuickVerdict.UNWINNABLE;
      case UNKNOWN:
        break;
      default:
        throw new IllegalArgumentException();
    }

    // 5: else if the position only contains pieces of type P,B,K and there are no semi-open
    // files in the position then
    if (calculateHasOnlyPawnsBishopsAndKings(board.getStaticPosition())
        && !SemiOpenFilesUtility.calculateHasSemiOpenFile(board.getStaticPosition())) {

      // 6: if true UnwinnableSS(pos, c, Mobility(pos)) then return Unwinnable
      final MobilitySolution mobilitySolution;
      if (isHasMobilitySolution && isCanUseMobilitySolution) {
        mobilitySolution = calculatedMobilitySolution;
      } else {
        mobilitySolution = Mobility.mobility(board);
      }
      if (UnwinnableSemiStatic.unwinnableSemiStatic(board, c, mobilitySolution)) {
        unperformHalfmoves(board, countHalfmoves);
        if (!invariant.equals(board.getFen())) {
          throw new ProgrammingMistakeException("Board was changed");
        }
        return UnwinnabilityQuickVerdict.UNWINNABLE;
      }
    }

    if (IS_ALIGN_QUICK_WITH_AMBRONA_REFERENCE_IMPLEMENTATION) {
      var isUnwinnable = false;
      final boolean hasOnlyPawnsAndBishops = calculateHasOnlyPawnsBishopsAndKings(board.getStaticPosition());
      final boolean isBlockedCandidate = calculateIsBlockedCandidate(board.getStaticPosition());
      if (isBlockedCandidate && hasOnlyPawnsAndBishops) {
        final MobilitySolution mobilitySolution = Mobility.mobility(board);
        isUnwinnable = UnwinnableSemiStatic.unwinnableSemiStatic(board, c, mobilitySolution);
      }

      if (isBlockedCandidate && !isUnwinnable && calculateIsAlmostOnlyPawnsBishopsAndKings(board.getStaticPosition())
          && (board.isCheck() || UnwinnabilityMaterial.calculateHasKnight(board.getStaticPosition()))) {
        isUnwinnable = calculateIsUnwinnableAfterOneMove(board, c);
      }

      final MovedKings movedKings = new MovedKings();
      final boolean isDynamicSearchCandidate = hasOnlyPawnsAndBishops && board.getLegalMoves().size() <= 8;
      if (!isUnwinnable && isDynamicSearchCandidate) {
        isUnwinnable = calculateIsDynamicallyUnwinnable(board, c, 7, movedKings, new HashMap<>());
      }
      if (!isUnwinnable && isDynamicSearchCandidate && movedKings.value != 3) {
        isUnwinnable = calculateIsDynamicallyUnwinnable(board, c, 15, movedKings, new HashMap<>());
      }

      if (isUnwinnable) {
        unperformHalfmoves(board, countHalfmoves);
        if (!invariant.equals(board.getFen())) {
          throw new ProgrammingMistakeException("Board was changed");
        }
        return UnwinnabilityQuickVerdict.UNWINNABLE;
      }
    }

    // 7: return PossiblyWinnable ( -> Unwinnability could not be determined)
    unperformHalfmoves(board, countHalfmoves);
    if (!invariant.equals(board.getFen())) {
      throw new ProgrammingMistakeException("Board was changed");
    }
    return UnwinnabilityQuickVerdict.POSSIBLY_WINNABLE;
  }

  private static boolean calculateHasOnlyPawnsBishopsAndKings(StaticPosition staticPosition) {
    return !UnwinnabilityMaterial.calculateHasRook(staticPosition)
        && !UnwinnabilityMaterial.calculateHasKnight(staticPosition)
        && !UnwinnabilityMaterial.calculateHasQueen(staticPosition);
  }

  private static Board copyCurrentPositionForQuickSearch(Board input) {
    final Fen fen = new Fen(input.getFen(), input.getStaticPosition(), input.getHavingMove(),
        input.getCastlingRightWhite(), input.getCastlingRightBlack(), input.getEnPassantCaptureTargetSquare(), 0,
        input.getFullMoveNumberForNextHalfMove());
    return new Board(fen, false);
  }

  private static boolean calculateIsAlmostOnlyPawnsBishopsAndKings(StaticPosition staticPosition) {
    var heavyPieceCount = 0;
    for (final var square : Square.REAL) {
      final Piece piece = staticPosition.get(square);
      if (piece == Piece.NONE) {
        continue;
      }
      final PieceType pieceType = piece.getPieceType();
      if (pieceType == PieceType.KNIGHT || pieceType == PieceType.ROOK || pieceType == PieceType.QUEEN) {
        heavyPieceCount++;
        if (heavyPieceCount > 1) {
          return false;
        }
      }
    }
    return true;
  }

  private static boolean calculateIsDynamicallyUnwinnable(Board board, Side intendedWinner, int depth,
      MovedKings movedKings, Map<DynamicSearchKey, Boolean> transpositionMap) {
    if (board.isInsufficientMaterial(intendedWinner)) {
      return true;
    }

    if (board.getLegalMoves().isEmpty() && board.isCheck()) {
      return board.getHavingMove() == intendedWinner;
    }

    if (depth <= 0) {
      return false;
    }

    final var cacheKey = new DynamicSearchKey(board.getDynamicPosition(), depth);
    if (transpositionMap.containsKey(cacheKey)) {
      return Nulls.get(transpositionMap, cacheKey).booleanValue();
    }

    for (final LegalMove legalMove : board.getLegalMoves()) {
      if (legalMove.movingPiece().getPieceType() == PieceType.KING) {
        movedKings.value |= board.getHavingMove() == Side.WHITE ? 2 : 1;
      }
      board.move(legalMove.moveSpecification());
      final boolean isUnwinnable = calculateIsDynamicallyUnwinnable(board, intendedWinner, depth - 1, movedKings,
          transpositionMap);
      board.unmove();
      if (!isUnwinnable) {
        transpositionMap.put(cacheKey, Boolean.FALSE);
        return false;
      }
    }

    transpositionMap.put(cacheKey, Boolean.TRUE);
    return true;
  }

  private static boolean calculateIsUnwinnableAfterOneMove(Board board, Side intendedWinner) {
    if (board.getLegalMoves().isEmpty()) {
      return !board.isCheck() || board.getHavingMove() == intendedWinner;
    }

    for (final LegalMove legalMove : board.getLegalMoves()) {
      board.move(legalMove.moveSpecification());
      final MobilitySolution mobilitySolution = Mobility.mobility(board);
      final boolean isUnwinnable = UnwinnableSemiStatic.unwinnableSemiStatic(board, intendedWinner, mobilitySolution);
      board.unmove();
      if (!isUnwinnable) {
        return false;
      }
    }
    return true;
  }

  private static boolean calculateIsBlockedCandidate(StaticPosition staticPosition) {
    return calculateNumberOfBlockedPawns(staticPosition) >= 1 && !calculateHasLonelyPawns(staticPosition);
  }

  private static int calculateNumberOfBlockedPawns(StaticPosition staticPosition) {
    var result = 0;
    for (final var square : Square.REAL) {
      if (staticPosition.get(square) == Piece.WHITE_PAWN && Square.calculateHasAheadSquare(Side.WHITE, square)) {
        final var aheadSquare = Square.calculateAheadSquare(Side.WHITE, square);
        if (staticPosition.get(aheadSquare) == Piece.BLACK_PAWN) {
          result++;
        }
      }
    }
    return result;
  }

  private static boolean calculateHasLonelyPawns(StaticPosition staticPosition) {
    var whitePawnFileMask = 0;
    var blackPawnFileMask = 0;
    for (final var square : Square.REAL) {
      final Piece piece = staticPosition.get(square);
      if (piece == Piece.WHITE_PAWN && square.getRank().getNumber() < 7) {
        whitePawnFileMask |= 1 << (square.getFile().getNumber() - 1);
      }
      if (piece == Piece.BLACK_PAWN && square.getRank().getNumber() > 2) {
        blackPawnFileMask |= 1 << (square.getFile().getNumber() - 1);
      }
    }
    return whitePawnFileMask != blackPawnFileMask;
  }

  private static UnwinnabilityQuickVerdict calculateWinnableVerdict() {
    if (IS_ALIGN_QUICK_WITH_AMBRONA_REFERENCE_IMPLEMENTATION) {
      return UnwinnabilityQuickVerdict.POSSIBLY_WINNABLE;
    }
    return UnwinnabilityQuickVerdict.WINNABLE;
  }

  private static void unperformHalfmoves(Board board, int countHalfmoves) {
    for (var i = 1; i <= countHalfmoves; i++) {
      board.unmove();
    }
  }

  private static final class MovedKings {
    private int value;
  }

  private record DynamicSearchKey(DynamicPosition dynamicPosition, int depth) {
  }
}
