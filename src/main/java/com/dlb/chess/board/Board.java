package com.dlb.chess.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.analyze.ChessRuleAnalyzer;
import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.CastlingRightLoss;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.constants.DynamicPositionConstants;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ChessBoard;
import com.dlb.chess.common.model.DynamicPosition;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.utility.BasicChessUtility;
import com.dlb.chess.common.utility.HalfMoveUtility;
import com.dlb.chess.common.utility.InsufficientMaterialUtility;
import com.dlb.chess.common.utility.RepetitionUtility;
import com.dlb.chess.common.utility.StaticPositionUtility;
import com.dlb.chess.exceptions.InvalidMoveException;
import com.dlb.chess.fen.FenBoard;
import com.dlb.chess.fen.FenParserAdvanced;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.CastlingRightBoth;
import com.dlb.chess.model.EnPassantRole;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.legal.AbstractLegalMoves;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.moves.utility.EnPassantCaptureUtility;
import com.dlb.chess.moves.utility.PromotionUtility;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.MoveToLan;
import com.dlb.chess.san.MoveToSan;
import com.dlb.chess.san.enums.SanTerminalMarker;
import com.dlb.chess.san.lenient.LenientSanParser;
import com.dlb.chess.san.model.LenientSanParserValidationResult;
import com.dlb.chess.san.model.StrictSanParserValidationResult;
import com.dlb.chess.san.validate.StrictSanParser;
import com.dlb.chess.squares.to.attacked.AbstractAttackedSquares;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * The library's central type — a chess <em>game</em>, not merely a position. A {@code Board} carries the position
 * <strong>plus</strong> the move history from its initial FEN: every halfmove ever performed, the legal-move set after
 * each, the halfmove clock, repetition counts, castling-right loss reasons, derived SAN/LAN strings — everything needed
 * to answer rule-level questions about the game so far.
 *
 * <h2>Construction</h2>
 *
 * <p>
 * Three constructors:
 *
 * <ul>
 * <li>{@link #Board()} — start at the initial position.</li>
 * <li>{@link #Board(String)} — start at the position given by a FEN string. Validated by the advanced FEN parser, so a
 * {@code Board} cannot be constructed from a position no real game could reach.</li>
 * <li>{@link #Board(Fen)} — start at a pre-parsed {@link Fen} value.</li>
 * </ul>
 *
 * <h2>Mutating the game</h2>
 *
 * <p>
 * Move execution happens through {@link #moveStrict(String)}, {@link #moveLenient(String)},
 * {@link #move(MoveSpecification)}, {@link #movesStrict(String...)}, and is undone by {@link #unmove()}. Both
 * move-pipelines validate the candidate against the current legal-move set; an invalid move throws (see
 * {@link com.dlb.chess.exceptions.InvalidMoveException} from the {@code MoveSpecification} pipeline,
 * {@code SanValidationException} from the SAN pipeline). Once the game has reached any FIDE-automatic termination
 * (checkmate, stalemate, mutual insufficient material, fivefold repetition, 75-move rule), neither pipeline accepts
 * further moves — the package-level Javadoc on {@link com.dlb.chess.board} documents the strict-game invariant in
 * detail.
 *
 * <h2>Querying the game</h2>
 *
 * <p>
 * Beyond move execution, {@code Board} exposes the standard rule-level predicates: {@link #isCheckmate()},
 * {@link #isStalemate()}, {@link #isThreefoldRepetition()}, {@link #isFiftyMove()}, {@link #isFivefoldRepetition()},
 * {@link #isSeventyFiveMove()}, plus the unwinnability/dead-position pair from {@link ChessBoard}
 * ({@code isUnwinnableQuick}, {@code isUnwinnableFull}, {@code isDeadPositionQuick}, {@code isDeadPositionFull} — the
 * library's flagship CHA feature; see {@link com.dlb.chess.unwinnability}). Position-state accessors return Guava
 * {@code ImmutableList}/{@code ImmutableSet}; mutation is exclusively via {@code move}/{@code unmove}.
 *
 * <p>
 * For game-level reports (threefold-claim-ahead, repetition listings, no-progress sequences), use
 * {@link com.dlb.chess.report.Reporter}.
 *
 * <h2>Thread-safety</h2>
 *
 * <p>
 * {@code Board} is mutable and <strong>not thread-safe</strong>. Use one {@code Board} per thread, or synchronize
 * externally. {@link #equals(Object)} and {@link #hashCode()} reflect the current game state, so a {@code Board} placed
 * in a {@link java.util.HashMap} or {@link java.util.HashSet} and then mutated will violate the collection's invariants
 * — don't do that.
 */
public class Board implements ChessBoard {

  private final Fen initialFen;
  private final List<LegalMove> performedLegalMoveList;
  private final List<ImmutableSet<LegalMove>> legalMoveSetList;
  private final List<Boolean> isCheckList;
  private final List<Boolean> isCheckmateList;
  private final List<Boolean> isStalemateList;
  private final List<DynamicPosition> dynamicPositionList;
  private final List<Integer> halfMoveClockList;
  private final List<Integer> repetitionCountList;
  private final List<String> sanList;
  private final List<String> lanList;
  private final List<HalfMove> halfMoveList;
  private final List<CastlingRightLoss> whiteKingSideLossList;
  private final List<CastlingRightLoss> whiteQueenSideLossList;
  private final List<CastlingRightLoss> blackKingSideLossList;
  private final List<CastlingRightLoss> blackQueenSideLossList;

  /**
   * Constructs a {@code Board} at the position carried by the given pre-parsed {@link Fen}.
   */
  public Board(Fen initialFen) {

    // using the static fen in case saves a bit of memory
    Fen initialFenUse;
    if (initialFen.equals(FenConstants.FEN_INITIAL)) {
      initialFenUse = FenConstants.FEN_INITIAL;
    } else {
      initialFenUse = initialFen;
    }

    // values used in the following not to be get from board methods!!!
    final StaticPosition initialStaticPosition = initialFenUse.staticPosition();
    final Side initialHavingMove = initialFenUse.havingMove();
    final CastlingRight initialCastlingRight = CastlingUtility.getCastlingRight(initialFenUse, initialHavingMove);
    final var initialEnPassantCaptureTargetSquare = initialFenUse.enPassantCaptureTargetSquare();
    final var initialIsEnPassantCapturePossible = calculateIsEnPassantCapturePossible(
        initialEnPassantCaptureTargetSquare, initialHavingMove, initialStaticPosition);

    this.initialFen = initialFenUse;

    this.performedLegalMoveList = new ArrayList<>();
    this.legalMoveSetList = new ArrayList<>();
    final ImmutableSet<LegalMove> legalMoveSet = AbstractLegalMoves.calculateLegalMoves(initialStaticPosition,
        initialHavingMove, initialCastlingRight, initialEnPassantCaptureTargetSquare);
    this.legalMoveSetList.add(legalMoveSet);

    final Set<Square> attackedSquareSet = AbstractAttackedSquares.calculateAttackedSquares(initialStaticPosition,
        initialHavingMove.getOppositeSide());

    final Square kingSquareHavingMove = StaticPositionUtility.calculateKingSquare(initialStaticPosition,
        initialHavingMove);

    this.isCheckList = new ArrayList<>();
    final var isCheck = attackedSquareSet.contains(kingSquareHavingMove);
    this.isCheckList.add(isCheck);

    this.isCheckmateList = new ArrayList<>();
    final var isCheckmate = isCheck && legalMoveSet.isEmpty();
    this.isCheckmateList.add(isCheckmate);

    this.isStalemateList = new ArrayList<>();
    final var isStalemate = !isCheck && legalMoveSet.isEmpty();
    this.isStalemateList.add(isStalemate);

    this.dynamicPositionList = new ArrayList<>();
    // attention - must be after we calculated the legal moves - we need them to check if en passant capture is possible
    // order of instructions dependency!!
    final CastlingRight initialCastlingRightWhite = CastlingUtility.getCastlingRight(initialFenUse, Side.WHITE);
    final CastlingRight initialCastlingRightBlack = CastlingUtility.getCastlingRight(initialFenUse, Side.BLACK);
    if (initialFenUse.equals(FenConstants.FEN_INITIAL)) {
      this.dynamicPositionList.add(DynamicPositionConstants.INITIAL);
    } else {
      this.dynamicPositionList.add(new DynamicPosition(initialHavingMove, initialStaticPosition,
          initialIsEnPassantCapturePossible, initialCastlingRightWhite, initialCastlingRightBlack));
    }
    this.halfMoveClockList = new ArrayList<>();
    this.halfMoveClockList.add(initialFenUse.halfMoveClock());

    this.repetitionCountList = new ArrayList<>();
    this.repetitionCountList.add(1);

    this.sanList = new ArrayList<>();
    this.lanList = new ArrayList<>();

    this.halfMoveList = new ArrayList<>();

    this.whiteKingSideLossList = new ArrayList<>();
    this.whiteQueenSideLossList = new ArrayList<>();
    this.blackKingSideLossList = new ArrayList<>();
    this.blackQueenSideLossList = new ArrayList<>();
    this.whiteKingSideLossList.add(initialCastlingRightWhite == CastlingRight.KING_AND_QUEEN_SIDE
        || initialCastlingRightWhite == CastlingRight.KING_SIDE ? CastlingRightLoss.NOT_LOST
            : CastlingRightLoss.UNKNOWN_FEN_IMPORT);
    this.whiteQueenSideLossList.add(initialCastlingRightWhite == CastlingRight.KING_AND_QUEEN_SIDE
        || initialCastlingRightWhite == CastlingRight.QUEEN_SIDE ? CastlingRightLoss.NOT_LOST
            : CastlingRightLoss.UNKNOWN_FEN_IMPORT);
    this.blackKingSideLossList.add(initialCastlingRightBlack == CastlingRight.KING_AND_QUEEN_SIDE
        || initialCastlingRightBlack == CastlingRight.KING_SIDE ? CastlingRightLoss.NOT_LOST
            : CastlingRightLoss.UNKNOWN_FEN_IMPORT);
    this.blackQueenSideLossList.add(initialCastlingRightBlack == CastlingRight.KING_AND_QUEEN_SIDE
        || initialCastlingRightBlack == CastlingRight.QUEEN_SIDE ? CastlingRightLoss.NOT_LOST
            : CastlingRightLoss.UNKNOWN_FEN_IMPORT);

  }

  /**
   * Constructs a {@code Board} at the standard initial position.
   */
  public Board() {
    this(FenConstants.FEN_INITIAL);
  }

  /**
   * Constructs a {@code Board} from a FEN string, validated by the advanced FEN parser. Rejects positions no real game
   * could reach (impossible double-checks, halfmove clock above the 75-move-rule threshold, castling rights
   * inconsistent with rooks-and-king positions, etc.).
   */
  public Board(String fen) {
    this(FenParserAdvanced.parseFenAdvanced(fen));
  }

  @Override
  public boolean isFirstMove() {
    return this.performedLegalMoveList.isEmpty();
  }

  /**
   * Plays the given move on this board. The {@code MoveSpecification} is validated against the current legal-move set;
   * an illegal move (or a move on a game already terminated) throws {@link InvalidMoveException}.
   */
  @Override
  public boolean move(MoveSpecification moveSpecification) throws InvalidMoveException {
    ValidateNewMove.validateNewMove(this, moveSpecification);
    return performMoveWithoutValidation(moveSpecification);
  }

  /**
   * Plays the given move on this board, specified in canonical SAN. The result carries the resolved
   * {@link MoveSpecification}; for callers that only need success / fail, the absence of a thrown exception is the
   * answer. Use {@link #moveLenient(String)} when parsing real-world PGN that may contain forgivable deviations.
   *
   * @throws com.dlb.chess.san.exceptions.SanValidationException if {@code san} is not canonical SAN, or is canonical
   *                                                             but does not represent a legal move
   */
  @Override
  public StrictSanParserValidationResult moveStrict(String san) {
    final StrictSanParserValidationResult result = StrictSanParser.parseText(san, this);
    this.performMoveWithoutValidation(result.moveSpecification());
    if (!san.equals(this.getSan())) {
      throw new ProgrammingMistakeException("The provided SAN and generated SAN are different, this should not happen");
    }
    return result;
  }

  /**
   * Plays the given move on this board, specified in lenient SAN. Accepts inputs the strict pipeline rejects when those
   * inputs uniquely identify a legal move and the deviation matches a supported tolerance category (case variation,
   * long-algebraic / UCI form, castling with digit zero, missing or wrong check / checkmate suffix, over-specification,
   * missing or spurious capture marker, missing promotion equals, explicit pawn letter). The returned
   * {@link LenientSanParserValidationResult} carries the resolved {@code MoveSpecification} together with one
   * {@code ForgivenItem} per deviation that was forgiven; on canonical input the forgiven-items list is empty.
   *
   * @throws com.dlb.chess.san.exceptions.LenientSanParserValidationException if the input cannot be resolved to a legal
   *                                                                          move even after applying every supported
   *                                                                          tolerance
   */
  @Override
  public LenientSanParserValidationResult moveLenient(String san) {
    final LenientSanParserValidationResult result = LenientSanParser.parseText(san, this);
    this.performMoveWithoutValidation(result.moveSpecification());
    return result;
  }

  /**
   * Plays the given sequence of canonical SAN moves on this board, in order. Convenience for batch play; the absence of
   * a thrown exception means every move was canonical and legal.
   */
  @Override
  public boolean movesStrict(String... sanArray) {
    for (final String san : sanArray) {
      if (san == null) {
        throw new IllegalArgumentException("The SAN cannot be null");
      }
      moveStrict(san);
    }
    return true;
  }

  /**
   * Plays the given sequence of canonical SAN moves on this board, in order. Convenience for batch play; the absence of
   * a thrown exception means every move was canonical and legal.
   */
  @Override
  public boolean movesLenient(String... sanArray) {
    for (final String san : sanArray) {
      if (san == null) {
        throw new IllegalArgumentException("The SAN cannot be null");
      }
      moveLenient(san);
    }
    return true;
  }

  private boolean performMoveWithoutValidation(MoveSpecification moveSpecification) throws InvalidMoveException {

    final CastlingRight beforeCastlingRightWhite = NonNullWrapperCommon.getLast(dynamicPositionList)
        .castlingRightWhite();
    final CastlingRight beforeCastlingRightBlack = NonNullWrapperCommon.getLast(dynamicPositionList)
        .castlingRightBlack();

    final Side havingMove = this.getHavingMove();
    final LegalMove moveToPerform = calculateLegalMove(this.getStaticPosition(), havingMove, moveSpecification);

    // values used in the following not to be get from board methods!!!
    final StaticPosition afterStaticPosition = StaticPositionUtility.createPositionAfterMove(this.getStaticPosition(),
        havingMove, moveSpecification);
    final Side afterHavingMove = havingMove.getOppositeSide();
    final CastlingRightBoth afterCastlingRightBoth = CastlingUtility
        .calculateCastlingRightBoth(beforeCastlingRightWhite, beforeCastlingRightBlack, moveToPerform);
    final CastlingRight afterCastlingRightHavingMove = CastlingUtility.getCastlingRight(afterCastlingRightBoth,
        afterHavingMove);
    final var afterEnPassantCaptureTargetSquare = EnPassantCaptureUtility
        .calculateEnPassantCaptureTargetSquare(moveToPerform);
    final var afterIsEnPassantCapturePossible = calculateIsEnPassantCapturePossible(afterEnPassantCaptureTargetSquare,
        afterHavingMove, afterStaticPosition);

    // update castling loss reasons
    this.whiteKingSideLossList.add(CastlingUtility.calculateCastlingRightLoss(moveToPerform,
        NonNullWrapperCommon.getLast(whiteKingSideLossList), Side.WHITE, CastlingMove.KING_SIDE));
    this.whiteQueenSideLossList.add(CastlingUtility.calculateCastlingRightLoss(moveToPerform,
        NonNullWrapperCommon.getLast(whiteQueenSideLossList), Side.WHITE, CastlingMove.QUEEN_SIDE));
    this.blackKingSideLossList.add(CastlingUtility.calculateCastlingRightLoss(moveToPerform,
        NonNullWrapperCommon.getLast(blackKingSideLossList), Side.BLACK, CastlingMove.KING_SIDE));
    this.blackQueenSideLossList.add(CastlingUtility.calculateCastlingRightLoss(moveToPerform,
        NonNullWrapperCommon.getLast(blackQueenSideLossList), Side.BLACK, CastlingMove.QUEEN_SIDE));

    // now changing board class state, so performing the move!
    this.performedLegalMoveList.add(moveToPerform);

    // now we have a depencency on instruction execution: the move must be performed before calling the legal moves
    final ImmutableSet<LegalMove> legalMoveSetAfterMove = AbstractLegalMoves.calculateLegalMoves(afterStaticPosition,
        afterHavingMove, afterCastlingRightHavingMove, afterEnPassantCaptureTargetSquare);
    this.legalMoveSetList.add(legalMoveSetAfterMove);

    final Set<Square> attackedSquareSet = AbstractAttackedSquares.calculateAttackedSquares(afterStaticPosition,
        afterHavingMove.getOppositeSide());

    final Square kingSquareHavingMove = StaticPositionUtility.calculateKingSquare(afterStaticPosition, afterHavingMove);

    final var isCheck = attackedSquareSet.contains(kingSquareHavingMove);
    this.isCheckList.add(isCheck);

    final var isCheckmate = isCheck && legalMoveSetAfterMove.isEmpty();
    this.isCheckmateList.add(isCheckmate);

    final var isStalemate = !isCheck && legalMoveSetAfterMove.isEmpty();
    this.isStalemateList.add(isStalemate);

    final var newDynamicPosition = new DynamicPosition(afterHavingMove, afterStaticPosition,
        afterIsEnPassantCapturePossible, afterCastlingRightBoth.castlingRightWhite(),
        afterCastlingRightBoth.castlingRightBlack());
    this.dynamicPositionList.add(newDynamicPosition);

    // order of instructions dependency!! - must be after adding the move
    final int lastHalfMoveClock = NonNullWrapperCommon.getLast(halfMoveClockList);
    this.halfMoveClockList.add(calculateNewHalfMoveClock(lastHalfMoveClock));

    // timely dependency - dynamic position list must be updated
    final var newRepetitionCount = RepetitionUtility.calculateCountRepetition(performedLegalMoveList,
        dynamicPositionList, newDynamicPosition, EnPassantCaptureRuleThreefold.DO_NOT_IGNORE);
    this.repetitionCountList.add(newRepetitionCount);

    final ImmutableSet<LegalMove> legalMoveSetBeforeLastHalfMoveSet = NonNullWrapperCommon.get(legalMoveSetList,
        legalMoveSetList.size() - 2);

    final SanTerminalMarker sanTerminalMarker = AbstractSan.calculateSanTerminalMarker(isCheck, isCheckmate);

    this.sanList
        .add(MoveToSan.calculateSanLastMove(moveToPerform, legalMoveSetBeforeLastHalfMoveSet, sanTerminalMarker));
    this.lanList.add(MoveToLan.calculateLanLastMove(moveToPerform, sanTerminalMarker));

    final HalfMove halfMove = HalfMoveUtility.calculateHalfMove(moveSpecification, this);
    this.halfMoveList.add(halfMove);

    return true;

  }

  // Package-private — a LegalMove can only be safely constructed when the caller has already validated the
  // moveSpecification as legal, and that's an invariant only the rule pipeline can guarantee. If a non-pipeline
  // caller passed an unvalidated MoveSpecification here, the result would silently carry incorrect derived data
  // (wrong moving piece, wrong captured piece, wrong en-passant role).
  static LegalMove calculateLegalMove(StaticPosition staticPosition, Side havingMove,
      MoveSpecification moveSpecification) {

    if (CastlingUtility.calculateIsCastlingMove(moveSpecification)) {
      final Piece king = Piece.calculateKingPiece(havingMove);
      return new LegalMove(moveSpecification, king, Piece.NONE);
    }

    final Piece movingPiece = staticPosition.get(moveSpecification.fromSquare());

    if (EnPassantCaptureUtility.calculateIsEnPassantCaptureNewMove(staticPosition, moveSpecification)) {
      final Square squareOfCapturedPawnForEnPassantCapture = EnPassantCaptureUtility
          .calculateSquareOfCapturedPawnForEnPassantCapture(havingMove, moveSpecification);
      final Piece pieceCaptured = staticPosition.get(squareOfCapturedPawnForEnPassantCapture);
      return new LegalMove(moveSpecification, movingPiece, pieceCaptured, EnPassantRole.EN_PASSANT_CAPTURE);
    }
    if (PromotionUtility.calculateIsPromotionNewMove(moveSpecification)) {
      final Piece pieceCaptured = staticPosition.get(moveSpecification.toSquare());
      return new LegalMove(moveSpecification, movingPiece, pieceCaptured);
    }
    final Piece pieceCaptured = staticPosition.get(moveSpecification.toSquare());
    final var enPassantRole = EnPassantCaptureUtility.calculateIsPawnTwoSquareAdvanceMove(movingPiece,
        moveSpecification) ? EnPassantRole.TWO_SQUARE_ADVANCE : EnPassantRole.NONE;
    return new LegalMove(moveSpecification, movingPiece, pieceCaptured, enPassantRole);
  }

  /**
   * Undoes the most recently played halfmove, restoring the board to the state immediately before that move. Throws if
   * no move has been played from the initial FEN.
   */
  @Override
  public void unmove() {
    if (isFirstMove()) {
      throw new ProgrammingMistakeException("Undo move requested but no move to undo");
    }

    this.performedLegalMoveList.remove(performedLegalMoveList.size() - 1);
    this.legalMoveSetList.remove(legalMoveSetList.size() - 1);

    this.isCheckList.remove(isCheckList.size() - 1);
    this.isCheckmateList.remove(isCheckmateList.size() - 1);
    this.isStalemateList.remove(isStalemateList.size() - 1);

    this.dynamicPositionList.remove(dynamicPositionList.size() - 1);
    this.halfMoveClockList.remove(halfMoveClockList.size() - 1);
    this.repetitionCountList.remove(repetitionCountList.size() - 1);

    this.sanList.remove(sanList.size() - 1);
    this.lanList.remove(lanList.size() - 1);

    this.halfMoveList.remove(halfMoveList.size() - 1);

    this.whiteKingSideLossList.remove(whiteKingSideLossList.size() - 1);
    this.whiteQueenSideLossList.remove(whiteQueenSideLossList.size() - 1);
    this.blackKingSideLossList.remove(blackKingSideLossList.size() - 1);
    this.blackQueenSideLossList.remove(blackQueenSideLossList.size() - 1);

  }

  @Override
  public LegalMove getLastMove() {
    if (isFirstMove()) {
      throw new IllegalArgumentException("There is no last move");
    }
    return NonNullWrapperCommon.getLast(this.performedLegalMoveList);
  }

  @Override
  public ImmutableSet<LegalMove> getLegalMoveSet() {
    return NonNullWrapperCommon.getLast(legalMoveSetList);
  }

  @Override
  public ImmutableList<MoveSpecification> getPerformedMoveSpecificationList() {
    final List<MoveSpecification> moveSpecificationList = new ArrayList<>();
    for (final LegalMove legalMove : this.performedLegalMoveList) {
      moveSpecificationList.add(legalMove.moveSpecification());
    }
    return NonNullWrapperCommon.copyOfList(moveSpecificationList);
  }

  private boolean calculateIsCapture() {
    if (isFirstMove()) {
      throw new IllegalStateException("There is no last move");
    }
    final LegalMove lastMove = getLastMove();
    return lastMove.pieceCaptured() != Piece.NONE;
  }

  @Override
  public boolean isCheck() {
    return NonNullWrapperCommon.getLast(isCheckList);
  }

  /** True iff the side to move is in check and has no legal move (FIDE 5.1.1). */
  @Override
  public boolean isCheckmate() {
    return NonNullWrapperCommon.getLast(isCheckmateList);
  }

  /** True iff the side to move is not in check but has no legal move (FIDE 5.2.1). */
  @Override
  public boolean isStalemate() {
    return NonNullWrapperCommon.getLast(isStalemateList);
  }

  @Override
  public boolean canClaimFiftyMoveRuleWithOwnMove() {
    final var halfMoveCounterNow = this.getHalfMoveClock();
    if (halfMoveCounterNow >= 99) {
      for (final LegalMove legalMove : getLegalMoveSet()) {
        // we must not perform the move to check, which is crucial for performance reasons
        if (!BasicChessUtility.calculateIsResetHalfMoveClock(legalMove)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public boolean canClaimThreefoldRepetitionRuleWithOwnMove() {
    for (final LegalMove legalMove : getLegalMoveSet()) {
      // we must not check moves creating a position that never occurred so far
      if (!BasicChessUtility.calculateIsResetHalfMoveClock(legalMove)) {
        this.move(legalMove.moveSpecification());
        if (isThreefoldRepetition()) {
          this.unmove();
          return true;
        }
        this.unmove();
      }
    }
    return false;
  }

  @Override
  public int getHalfMoveClock() {
    return NonNullWrapperCommon.getLast(halfMoveClockList);
  }

  private int calculateNewHalfMoveClock(int lastHalfMoveClock) {
    final LegalMove legalMove = getLastMove();
    if (BasicChessUtility.calculateIsResetHalfMoveClock(legalMove)) {
      return 0;
    }
    return lastHalfMoveClock + 1;
  }

  @Override
  public int getRepetitionCount() {
    return NonNullWrapperCommon.getLast(repetitionCountList);
  }

  @Override
  public boolean isInsufficientMaterial() {
    return isInsufficientMaterial(Side.WHITE) && isInsufficientMaterial(Side.BLACK);
  }

  @Override
  public boolean isInsufficientMaterial(Side side) {
    return InsufficientMaterialUtility.calculateIsInsufficientMaterial(side, getStaticPosition());
  }

  @Override
  public String getFen() {
    if (isFirstMove()) {
      return getInitialFen().fen();
    }
    return FenBoard.calculateFen(this);
  }

  @Override
  public Fen getInitialFen() {
    return initialFen;
  }

  @Override
  public Piece getMovingPiece() {
    if (isFirstMove()) {
      throw new IllegalStateException("There is no last move");
    }
    return getLastMove().movingPiece();
  }

  @Override
  public boolean isCapture() {
    if (isFirstMove()) {
      throw new IllegalStateException("There is no last move");
    }
    return calculateIsCapture();
  }

  @Override
  public int getInitialFenFullMoveNumber() {
    return initialFen.fullMoveNumber();
  }

  @Override
  public int getFullMoveNumber() {
    // because I implemented the full move number calculation for the next half move to be played, I now calculate the
    // full move number using this existing method. nicer would be to implement full move counter calculation and then
    // derive the full move counter for the next half move from it.
    final var fullMoveNumberForNextHalfMove = calculateFullMoveNumberForNextHalfMove(isFirstMove(),
        initialFen.fullMoveNumber(), initialFen.havingMove(), getHavingMove(), getPerformedHalfMoveCount());

    return switch (getHavingMove()) {
      case WHITE -> fullMoveNumberForNextHalfMove - 1;
      case BLACK -> fullMoveNumberForNextHalfMove;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  private static int calculateFullMoveNumberForNextHalfMove(boolean isFirstMove, int initialFenFullMoveNumber,
      Side initialFenHavingMove, Side havingMove, int halfMoveCount) {
    if (isFirstMove) {
      return initialFenFullMoveNumber;
    }

    return switch (havingMove) {
      case WHITE -> switch (initialFenHavingMove) {
        case BLACK -> {
          // must be even
          checkIsEven(halfMoveCount + 1);
          yield (halfMoveCount + 1) / 2 + initialFenFullMoveNumber;
        }
        case WHITE -> {
          // must be even
          checkIsEven(halfMoveCount);
          yield halfMoveCount / 2 + initialFenFullMoveNumber;
        }
        case NONE -> throw new IllegalArgumentException();
        default -> throw new IllegalArgumentException();
      }; // must be even // must be even
      case BLACK -> switch (initialFenHavingMove) {
        case BLACK -> {
          // must be even
          checkIsEven(halfMoveCount);
          yield halfMoveCount / 2 + initialFenFullMoveNumber;
        }
        case WHITE -> {
          // must be even
          checkIsEven(halfMoveCount - 1);
          yield (halfMoveCount - 1) / 2 + initialFenFullMoveNumber;
        }
        case NONE -> throw new IllegalArgumentException();
        default -> throw new IllegalArgumentException();
      }; // must be even // must be even
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  /**
   * True iff the halfmove clock has reached the 50-move-rule threshold (FIDE 9.3). This is the on-board predicate
   * (claimable rule); the game continues until claimed.
   */
  @Override
  public boolean isFiftyMove() {
    return getHalfMoveClock() >= ChessConstants.FIFTY_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD;
  }

  /**
   * True iff the current position has occurred at least three times in the game (FIDE 9.2). This is the on-board
   * predicate (claimable rule); the game continues until claimed.
   */
  @Override
  public boolean isThreefoldRepetition() {
    return getRepetitionCount() >= ChessConstants.THREEFOLD_REPETITION_RULE_THRESHOLD;
  }

  /**
   * True iff the halfmove clock has reached the 75-move-rule threshold (FIDE 9.6.2). This is an automatic FIDE
   * termination — once true, the game has ended in a draw and no further moves are accepted.
   */
  @Override
  public boolean isSeventyFiveMove() {
    return getHalfMoveClock() >= ChessConstants.SEVENTY_FIVE_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD;
  }

  /**
   * True iff the current position has occurred at least five times in the game (FIDE 9.6.1). This is an automatic FIDE
   * termination — once true, the game has ended in a draw and no further moves are accepted.
   */
  @Override
  public boolean isFivefoldRepetition() {
    return getRepetitionCount() >= ChessConstants.FIVEFOLD_REPETITION_RULE_THRESHOLD;
  }

  @Override
  public String getSan() {
    if (isFirstMove()) {
      throw new IllegalStateException("There is no last move");
    }
    return NonNullWrapperCommon.getLast(sanList);
  }

  @Override
  public String getLan() {
    if (isFirstMove()) {
      throw new IllegalStateException("There is no last move");
    }
    return NonNullWrapperCommon.getLast(lanList);
  }

  @Override
  public Side getHavingMove() {
    if (isFirstMove()) {
      return initialFen.havingMove();
    }
    final LegalMove lastMove = getLastMove();
    return lastMove.havingMove().getOppositeSide();
  }

  @Override
  public StaticPosition getStaticPosition() {
    return NonNullWrapperCommon.getLast(dynamicPositionList).staticPosition();
  }

  @Override
  public StaticPosition getStaticPositionBeforeLastMove() {
    if (isFirstMove()) {
      throw new ProgrammingMistakeException("The method cannot be called if no move was yet made");
    }
    return NonNullWrapperCommon.get(dynamicPositionList, this.dynamicPositionList.size() - 2).staticPosition();
  }

  @Override
  public boolean isEnPassantCapturePossible() {
    return NonNullWrapperCommon.getLast(dynamicPositionList).isEnPassantCapturePossible();
  }

  private static boolean calculateIsEnPassantCapturePossible(Square enPassantCaptureTargetSquare, Side havingMove,
      StaticPosition staticPosition) {
    if (enPassantCaptureTargetSquare == Square.NONE) {
      return false;
    }
    // two potential capture moves
    if (!Square.calculateHasBehindSquare(havingMove, enPassantCaptureTargetSquare)) {
      // cannot be for en en passant target square
      throw new ProgrammingMistakeException();
    }
    final Square squareBehind = Square.calculateBehindSquare(havingMove, enPassantCaptureTargetSquare);

    // capture move from right square
    if (Square.calculateHasRightSquare(havingMove, squareBehind)) {
      final Square squareRight = Square.calculateRightSquare(havingMove, squareBehind);
      if (staticPosition.isOwnPawn(squareRight, havingMove)) {
        final MoveSpecification moveSpecification = new MoveSpecification(squareRight, enPassantCaptureTargetSquare);
        if (ChessRuleAnalyzer.isMoveKingSafe(staticPosition, havingMove, moveSpecification)) {
          return true;
        }
      }
    }

    // capture move from left square
    if (Square.calculateHasLeftSquare(havingMove, squareBehind)) {
      final Square squareLeft = Square.calculateLeftSquare(havingMove, squareBehind);
      if (staticPosition.isOwnPawn(squareLeft, havingMove)) {
        final MoveSpecification moveSpecification = new MoveSpecification(squareLeft, enPassantCaptureTargetSquare);
        if (ChessRuleAnalyzer.isMoveKingSafe(staticPosition, havingMove, moveSpecification)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public int getPerformedHalfMoveCount() {
    return performedLegalMoveList.size();
  }

  @Override
  public ImmutableList<DynamicPosition> getDynamicPositionList() {
    return NonNullWrapperCommon.copyOfList(dynamicPositionList);
  }

  @Override
  public ImmutableList<HalfMove> getHalfMoveList() {
    return NonNullWrapperCommon.copyOfList(halfMoveList);
  }

  @Override
  public DynamicPosition getDynamicPosition() {
    return NonNullWrapperCommon.getLast(dynamicPositionList);
  }

  @Override
  public ImmutableSet<MoveSpecification> getPossibleMoveSpecificationSet() {
    final Set<MoveSpecification> result = new TreeSet<>();
    for (final LegalMove legalMove : this.getLegalMoveSet()) {
      result.add(legalMove.moveSpecification());
    }
    return NonNullWrapperCommon.copyOfSet(result);
  }

  @Override
  public String toString() {
    return getStaticPosition().toString();
  }

  @Override
  public Square getEnPassantCaptureTargetSquare() {
    if (isFirstMove()) {
      return initialFen.enPassantCaptureTargetSquare();
    }
    return EnPassantCaptureUtility.calculateEnPassantCaptureTargetSquare(getLastMove());
  }

  private static void checkIsEven(int intValue) {
    final var valueFloor = intValue / 2;
    final var valueRounded = (int) StrictMath.round(intValue / 2.0);
    if (valueFloor != valueRounded) {
      throw new ProgrammingMistakeException("The programmer overlooked something");
    }
  }

  @Override
  public ImmutableList<LegalMove> getPerformedLegalMoveList() {
    return NonNullWrapperCommon.copyOfList(performedLegalMoveList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dynamicPositionList, halfMoveClockList, halfMoveList, initialFen, isCheckList, isCheckmateList,
        isStalemateList, lanList, legalMoveSetList, performedLegalMoveList, repetitionCountList, sanList);
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (Board) obj;
    return Objects.equals(dynamicPositionList, other.dynamicPositionList)
        && Objects.equals(halfMoveClockList, other.halfMoveClockList)
        && Objects.equals(halfMoveList, other.halfMoveList) && Objects.equals(initialFen, other.initialFen)
        && Objects.equals(isCheckList, other.isCheckList) && Objects.equals(isCheckmateList, other.isCheckmateList)
        && Objects.equals(isStalemateList, other.isStalemateList) && Objects.equals(lanList, other.lanList)
        && Objects.equals(legalMoveSetList, other.legalMoveSetList)
        && Objects.equals(performedLegalMoveList, other.performedLegalMoveList)
        && Objects.equals(repetitionCountList, other.repetitionCountList) && Objects.equals(sanList, other.sanList);
  }

  public CastlingRightLoss getWhiteKingSideLoss() {
    return NonNullWrapperCommon.getLast(whiteKingSideLossList);
  }

  public CastlingRightLoss getWhiteQueenSideLoss() {
    return NonNullWrapperCommon.getLast(whiteQueenSideLossList);
  }

  public CastlingRightLoss getBlackKingSideLoss() {
    return NonNullWrapperCommon.getLast(blackKingSideLossList);
  }

  public CastlingRightLoss getBlackQueenSideLoss() {
    return NonNullWrapperCommon.getLast(blackQueenSideLossList);
  }

  @Override
  public CastlingRightLoss getCastlingRightLoss(Side side, CastlingMove castlingSide) {
    return switch (side) {
      case WHITE -> castlingSide == CastlingMove.KING_SIDE ? getWhiteKingSideLoss() : getWhiteQueenSideLoss();
      case BLACK -> castlingSide == CastlingMove.KING_SIDE ? getBlackKingSideLoss() : getBlackQueenSideLoss();
      case NONE -> throw new IllegalArgumentException();
    };
  }

  @Override
  public CastlingRight getCastlingRightWhite() {
    return getDynamicPosition().castlingRightWhite();
  }

  @Override
  public CastlingRight getCastlingRightBlack() {
    return getDynamicPosition().castlingRightBlack();
  }

}
