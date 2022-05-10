package com.dlb.chess.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.board.model.UpdateSquare;
import com.dlb.chess.common.AbstractBoard;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.constants.DynamicPositionConstants;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.model.DynamicPosition;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.utility.BasicChessUtility;
import com.dlb.chess.common.utility.HalfMoveUtility;
import com.dlb.chess.common.utility.RepetitionUtility;
import com.dlb.chess.common.utility.StaticPositionUtility;
import com.dlb.chess.exceptions.InvalidMoveException;
import com.dlb.chess.fen.FenCalculator;
import com.dlb.chess.fen.FenParser;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.CastlingRightBoth;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.legal.AbstractLegalMoves;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.moves.utility.EnPassantCaptureUtility;
import com.dlb.chess.moves.utility.PromotionUtility;
import com.dlb.chess.moves.utility.StandardMoveUtility;
import com.dlb.chess.san.MoveToLan;
import com.dlb.chess.san.MoveToSan;
import com.dlb.chess.san.SanValidation;
import com.dlb.chess.squares.to.threaten.AbstractThreatenSquares;

public class Board extends AbstractBoard {

  private final Fen initialFen;
  private final List<LegalMove> performedLegalMoveList;
  private final List<Set<LegalMove>> legalMoveListSet;
  private final List<Boolean> isCheckList;
  private final List<Boolean> isCheckmateList;
  private final List<Boolean> isStalemateList;
  private final List<DynamicPosition> dynamicPositionList;
  private final List<Integer> halfMoveClockList;
  private final List<Integer> repetitionCountList;
  private final List<String> sanList;
  private final List<String> lanList;
  private final List<HalfMove> halfMoveList;

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
    final CastlingRightBoth initialCastlingRightBoth = initialFenUse.castlingRightBoth();
    final CastlingRight initialCastlingRight = CastlingUtility.getCastlingRight(initialCastlingRightBoth,
        initialHavingMove);
    final var initialEnPassantCaptureTargetSquare = initialFenUse.enPassantCaptureTargetSquare();
    final var initialIsEnPassantCapturePossible = calculateIsEnPassantCapturePossible(
        initialEnPassantCaptureTargetSquare, initialHavingMove, initialStaticPosition);

    this.initialFen = initialFenUse;

    this.performedLegalMoveList = new ArrayList<>();
    this.legalMoveListSet = new ArrayList<>();
    final Set<LegalMove> legalMoveSet = AbstractLegalMoves.calculateLegalMoves(initialStaticPosition, initialHavingMove,
        initialCastlingRight, initialEnPassantCaptureTargetSquare);
    this.legalMoveListSet.add(legalMoveSet);

    final Set<Square> threatenedSquareSet = AbstractThreatenSquares.calculateThreatenedSquares(initialStaticPosition,
        initialHavingMove.getOppositeSide());

    final Square kingSquareHavingMove = StaticPositionUtility.calculateKingSquare(initialStaticPosition,
        initialHavingMove);

    this.isCheckList = new ArrayList<>();
    final var isCheck = threatenedSquareSet.contains(kingSquareHavingMove);
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
    if (initialFenUse.equals(FenConstants.FEN_INITIAL)) {
      this.dynamicPositionList.add(DynamicPositionConstants.INITIAL);
    } else {
      this.dynamicPositionList.add(new DynamicPosition(initialHavingMove, initialStaticPosition,
          initialIsEnPassantCapturePossible, initialCastlingRightBoth));
    }
    this.halfMoveClockList = new ArrayList<>();
    this.halfMoveClockList.add(initialFenUse.halfMoveClock());

    this.repetitionCountList = new ArrayList<>();
    this.repetitionCountList.add(1);

    this.sanList = new ArrayList<>();
    this.lanList = new ArrayList<>();

    this.halfMoveList = new ArrayList<>();

  }

  public Board() {
    this(FenConstants.FEN_INITIAL);
  }

  public Board(String fen) {
    this(FenParser.parseAdvancedFen(fen));
  }

  @Override
  public boolean isFirstMove() {
    return this.performedLegalMoveList.isEmpty();
  }

  @Override
  public boolean performMove(MoveSpecification moveSpecification) throws InvalidMoveException {

    ValidateNewMove.validateNewMove(this, moveSpecification);

    final CastlingRightBoth beforeCastlingRightBoth = NonNullWrapperCommon.getLast(dynamicPositionList)
        .castlingRightBoth();

    final LegalMove moveToPerform = calculateLegalMove(this.getStaticPosition(), moveSpecification);
    final MoveSpecification moveSpecificationForMoveToPerform = moveToPerform.moveSpecification();

    // values used in the following not to be get from board methods!!!
    final StaticPosition afterStaticPosition = createPositionAfterMove(this.getStaticPosition(), moveSpecification);
    final Side afterHavingMove = moveSpecificationForMoveToPerform.havingMove().getOppositeSide();
    final CastlingRightBoth afterCastlingRightBoth = CastlingUtility.calculateCastlingRightBoth(beforeCastlingRightBoth,
        moveToPerform);
    final CastlingRight afterCastlingRight = CastlingUtility.getCastlingRight(afterCastlingRightBoth, afterHavingMove);
    final var afterEnPassantCaptureTargetSquare = EnPassantCaptureUtility
        .calculateEnPassantCaptureTargetSquare(moveToPerform);
    final var afterIsEnPassantCapturePossible = calculateIsEnPassantCapturePossible(afterEnPassantCaptureTargetSquare,
        afterHavingMove, afterStaticPosition);

    // now changing board class state, so performing the move!
    this.performedLegalMoveList.add(moveToPerform);

    // now we have a depencency on instruction execution: the move must be performed before calling the legal moves
    final Set<LegalMove> legalMovesAfterMove = AbstractLegalMoves.calculateLegalMoves(afterStaticPosition,
        afterHavingMove, afterCastlingRight, afterEnPassantCaptureTargetSquare);
    this.legalMoveListSet.add(legalMovesAfterMove);

    final Set<Square> threatenedSquareSet = AbstractThreatenSquares.calculateThreatenedSquares(afterStaticPosition,
        afterHavingMove.getOppositeSide());

    final Square kingSquareHavingMove = StaticPositionUtility.calculateKingSquare(afterStaticPosition, afterHavingMove);

    final var isCheck = threatenedSquareSet.contains(kingSquareHavingMove);
    this.isCheckList.add(isCheck);

    final var isCheckmate = isCheck && legalMovesAfterMove.isEmpty();
    this.isCheckmateList.add(isCheckmate);

    final var isStalemate = !isCheck && legalMovesAfterMove.isEmpty();
    this.isStalemateList.add(isStalemate);

    final DynamicPosition newDynamicPosition = new DynamicPosition(afterHavingMove, afterStaticPosition,
        afterIsEnPassantCapturePossible, afterCastlingRightBoth);
    this.dynamicPositionList.add(newDynamicPosition);

    // order of instructions dependency!! - must be after adding the move
    final int lastHalfMoveClock = NonNullWrapperCommon.getLast(halfMoveClockList);
    this.halfMoveClockList.add(calculateNewHalfMoveClock(lastHalfMoveClock));

    // timely dependency - dynamic position list must be updated
    final var newRepetitionCount = RepetitionUtility.calculateCountRepetition(performedLegalMoveList,
        dynamicPositionList, newDynamicPosition, EnPassantCaptureRuleThreefold.DO_NOT_IGNORE);
    this.repetitionCountList.add(newRepetitionCount);

    final Set<LegalMove> legalMoveBeforeLastHalfMoveSet = NonNullWrapperCommon.get(legalMoveListSet,
        legalMoveListSet.size() - 2);
    this.sanList
        .add(MoveToSan.calculateSanLastMove(moveToPerform, legalMoveBeforeLastHalfMoveSet, isCheckmate, isCheck));
    this.lanList.add(MoveToLan.calculateLanLastMove(moveToPerform, isCheckmate, isCheck));

    final HalfMove halfMove = HalfMoveUtility.calculateHalfMove(moveSpecification, this);
    this.halfMoveList.add(halfMove);

    return true;

  }

  @Override
  public boolean performMove(String san) {
    return performMoves(san);
  }

  @Override
  public boolean performMoves(String... sanArray) {
    for (final String san : sanArray) {
      if (san == null) {
        throw new IllegalArgumentException("The SAN cannot be null");
      }
      final MoveSpecification moveSpecification = SanValidation.calculateMoveSpecificationForSan(san, this);
      this.performMove(moveSpecification);
      if (!san.equals(this.getSan())) {
        throw new ProgrammingMistakeException("The provided SAN and generated SAN are not equals");
      }
    }
    return true;
  }

  // Here we rely on that moveSpecification was validated as legal move. If this does not hold the below method will
  // just pass through this moves, there is no checking, so the error will be go through.
  public static LegalMove calculateLegalMove(StaticPosition staticPosition, MoveSpecification moveSpecification) {

    final Piece movingPiece;
    if (moveSpecification.fromSquare() == Square.NONE) {
      // castling
      movingPiece = Piece.NONE;
    } else {
      movingPiece = staticPosition.get(moveSpecification.fromSquare());
    }

    if (EnPassantCaptureUtility.calculateIsEnPassantCaptureNewMove(staticPosition, moveSpecification)) {
      final Square squareOfCapturedPawnForEnPassantCapture = EnPassantCaptureUtility
          .calculateSquareOfCapturedPawnForEnPassantCapture(moveSpecification);
      final Piece pieceCaptured = staticPosition.get(squareOfCapturedPawnForEnPassantCapture);
      return new LegalMove(moveSpecification, movingPiece, pieceCaptured);
    }
    if (CastlingUtility.calculateIsCastlingMove(moveSpecification)) {
      return new LegalMove(moveSpecification);
    }
    if (PromotionUtility.calculateIsPromotionNewMove(moveSpecification)) {
      final Piece pieceCaptured = staticPosition.get(moveSpecification.toSquare());
      return new LegalMove(moveSpecification, movingPiece, pieceCaptured);
    }
    final Piece pieceCaptured = staticPosition.get(moveSpecification.toSquare());
    return new LegalMove(moveSpecification, movingPiece, pieceCaptured);
  }

  public static StaticPosition createPositionAfterMove(StaticPosition staticPosition,
      MoveSpecification moveSpecification) {

    final List<UpdateSquare> updateSquareList = calculateUpdateSquareList(staticPosition, moveSpecification);
    return staticPosition.createChangedPosition(updateSquareList);

  }

  private static List<UpdateSquare> calculateUpdateSquareList(StaticPosition staticPosition,
      MoveSpecification moveSpecification) {

    if (EnPassantCaptureUtility.calculateIsEnPassantCaptureNewMove(staticPosition, moveSpecification)) {
      return EnPassantCaptureUtility.performEnPassantCaptureMovements(staticPosition, moveSpecification);
    }
    if (CastlingUtility.calculateIsCastlingMove(moveSpecification)) {
      return CastlingUtility.performCastlingMovements(moveSpecification);
    }
    if (PromotionUtility.calculateIsPromotionNewMove(moveSpecification)) {
      return PromotionUtility.performPromotionMovements(moveSpecification);
    }
    return StandardMoveUtility.performStandardMovements(staticPosition, moveSpecification);
  }

  @Override
  public void unperformMove() {
    if (isFirstMove()) {
      throw new ProgrammingMistakeException("Undo move requested but no move to undo");
    }

    {
      // only to test our methods correctness we perform an undo and check against the previous position
      // the below has no other purpose and could be removed
      final LegalMove moveToUndo = getLastMove();
      final StaticPosition staticPositionBeforeLastMove = NonNullWrapperCommon
          .get(dynamicPositionList, this.dynamicPositionList.size() - 2).staticPosition();

      final StaticPosition staticPositionToUndo = NonNullWrapperCommon.getLast(dynamicPositionList).staticPosition();
      final StaticPosition staticPositionUndoExpected = NonNullWrapperCommon
          .get(dynamicPositionList, dynamicPositionList.size() - 2).staticPosition();

      List<UpdateSquare> updateSquareList;
      if (EnPassantCaptureUtility.calculateIsEnPassantCapture(staticPositionBeforeLastMove,
          moveToUndo.moveSpecification())) {
        updateSquareList = EnPassantCaptureUtility.performEnPassantCaptureUndoMovements(moveToUndo);
      } else if (CastlingUtility.calculateIsCastlingMove(moveToUndo.moveSpecification())) {
        updateSquareList = CastlingUtility.performCastlingUndoMovements(moveToUndo);
      } else if (PromotionUtility.calculateIsPromotion(moveToUndo.moveSpecification())) {
        updateSquareList = PromotionUtility.performPromotionUndoMovements(moveToUndo);
      } else {
        updateSquareList = StandardMoveUtility.performStandardUndoMovements(moveToUndo);
      }
      final StaticPosition staticPositionUndoActual = staticPositionToUndo.createChangedPosition(updateSquareList);
      if (!staticPositionUndoExpected.equals(staticPositionUndoActual)) {
        throw new ProgrammingMistakeException("The undo position calculation is correct");
      }
    }

    this.performedLegalMoveList.remove(performedLegalMoveList.size() - 1);
    this.legalMoveListSet.remove(legalMoveListSet.size() - 1);

    this.isCheckList.remove(isCheckList.size() - 1);
    this.isCheckmateList.remove(isCheckmateList.size() - 1);
    this.isStalemateList.remove(isStalemateList.size() - 1);

    this.dynamicPositionList.remove(dynamicPositionList.size() - 1);
    this.halfMoveClockList.remove(halfMoveClockList.size() - 1);
    this.repetitionCountList.remove(repetitionCountList.size() - 1);

    this.sanList.remove(sanList.size() - 1);
    this.lanList.remove(lanList.size() - 1);

    this.halfMoveList.remove(halfMoveList.size() - 1);

  }

  @Override
  public LegalMove getLastMove() {
    if (isFirstMove()) {
      throw new IllegalArgumentException("There is no last move");
    }
    return NonNullWrapperCommon.getLast(this.performedLegalMoveList);
  }

  @Override
  public Set<LegalMove> getLegalMoveSet() {
    return NonNullWrapperCommon.getLast(legalMoveListSet);
  }

  @Override
  public List<MoveSpecification> getPerformedMoveSpecificationList() {
    final List<MoveSpecification> moveSpecificationList = new ArrayList<>();
    for (final LegalMove legalMove : this.performedLegalMoveList) {
      moveSpecificationList.add(legalMove.moveSpecification());
    }
    return moveSpecificationList;
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

  @Override
  public boolean isCheckmate() {
    return NonNullWrapperCommon.getLast(isCheckmateList);
  }

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
        this.performMove(legalMove.moveSpecification());
        if (isThreefoldRepetition()) {
          this.unperformMove();
          return true;
        }
        this.unperformMove();
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
    return InsufficientMaterialCalculator.calculateIsInsufficientMaterial(side, getStaticPosition());
  }

  @Override
  public String getFen() {
    return FenCalculator.calculateFen(this);
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

    switch (getHavingMove()) {
      case WHITE:
        return fullMoveNumberForNextHalfMove - 1;
      case BLACK:
        return fullMoveNumberForNextHalfMove;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  private static int calculateFullMoveNumberForNextHalfMove(boolean isFirstMove, int initialFenFullMoveNumber,
      Side initialFenHavingMove, Side havingMove, int halfMoveCount) {
    if (isFirstMove) {
      return initialFenFullMoveNumber;
    }

    switch (havingMove) {
      case WHITE:
        switch (initialFenHavingMove) {
          case BLACK:
            // must be even
            checkIsEven(halfMoveCount + 1);
            return (halfMoveCount + 1) / 2 + initialFenFullMoveNumber;
          case WHITE:
            // must be even
            checkIsEven(halfMoveCount);
            return halfMoveCount / 2 + initialFenFullMoveNumber;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      case BLACK:
        switch (initialFenHavingMove) {
          case BLACK:
            // must be even
            checkIsEven(halfMoveCount);
            return halfMoveCount / 2 + initialFenFullMoveNumber;
          case WHITE:
            // must be even
            checkIsEven(halfMoveCount - 1);
            return (halfMoveCount - 1) / 2 + initialFenFullMoveNumber;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  @Override
  public boolean isFiftyMove() {
    return getHalfMoveClock() >= ChessConstants.FIFTY_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD;
  }

  @Override
  public boolean isThreefoldRepetition() {
    return getRepetitionCount() >= ChessConstants.THREEFOLD_REPETITION_RULE_THRESHOLD;
  }

  @Override
  public boolean isSeventyFiftyMove() {
    return getHalfMoveClock() >= ChessConstants.SEVENTY_FIVE_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD;
  }

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
    return lastMove.moveSpecification().havingMove().getOppositeSide();
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
        final MoveSpecification moveSpecification = new MoveSpecification(havingMove, squareRight,
            enPassantCaptureTargetSquare);
        if (!StaticPositionUtility.calculateIsEvaluateAttackingKing(staticPosition, moveSpecification)) {
          return true;
        }
      }
    }

    // capture move from left square
    if (Square.calculateHasLeftSquare(havingMove, squareBehind)) {
      final Square squareLeft = Square.calculateLeftSquare(havingMove, squareBehind);
      if (staticPosition.isOwnPawn(squareLeft, havingMove)) {
        final MoveSpecification moveSpecification = new MoveSpecification(havingMove, squareLeft,
            enPassantCaptureTargetSquare);
        if (!StaticPositionUtility.calculateIsEvaluateAttackingKing(staticPosition, moveSpecification)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public CastlingRightBoth getCastlingRightBoth() {
    return NonNullWrapperCommon.getLast(dynamicPositionList).castlingRightBoth();
  }

  @Override
  public int getPerformedHalfMoveCount() {
    return performedLegalMoveList.size();
  }

  @Override
  public List<DynamicPosition> getDynamicPositionList() {
    return dynamicPositionList;
  }

  @Override
  public List<HalfMove> getHalfMoveList() {
    return halfMoveList;
  }

  @Override
  public DynamicPosition getDynamicPosition() {
    return NonNullWrapperCommon.getLast(dynamicPositionList);
  }

  @Override
  public Set<MoveSpecification> getPossibleMoveSpecificationSet() {
    final Set<MoveSpecification> result = new TreeSet<>();
    for (final LegalMove legalMove : this.getLegalMoveSet()) {
      result.add(legalMove.moveSpecification());
    }
    return result;
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
  public List<LegalMove> getPerformedLegalMoveList() {
    return performedLegalMoveList;
  }

  @Override
  public int hashCode() {
    return Objects.hash(dynamicPositionList, halfMoveClockList, halfMoveList, initialFen, isCheckList, isCheckmateList,
        isStalemateList, lanList, legalMoveListSet, performedLegalMoveList, repetitionCountList, sanList);
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
        && Objects.equals(legalMoveListSet, other.legalMoveListSet)
        && Objects.equals(performedLegalMoveList, other.performedLegalMoveList)
        && Objects.equals(repetitionCountList, other.repetitionCountList) && Objects.equals(sanList, other.sanList);
  }

}
