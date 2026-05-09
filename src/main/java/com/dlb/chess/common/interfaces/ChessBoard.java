package com.dlb.chess.common.interfaces;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.CastlingRightLoss;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.common.model.DynamicPosition;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.ucimove.utility.UciMoveUtility;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.unwinnability.full.UnwinnableFullAnalyzer;
import com.dlb.chess.unwinnability.full.enums.DeadPositionFull;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.quick.UnwinnableQuickAnalyzer;
import com.dlb.chess.unwinnability.quick.enums.DeadPositionQuick;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public interface ChessBoard extends EnumConstants {

  boolean performMove(MoveSpecification moveSpecification);

  boolean performMove(String san);

  boolean performMoves(String... sanArray);

  void unperformMove();

  boolean isCheck();

  boolean isCheckmate();

  boolean isStalemate();

  boolean isInsufficientMaterial();

  boolean isInsufficientMaterial(Side side);

  int getHalfMoveClock();

  /**
   * The full move number for the last half move played. Please note that this is different from the full move number
   * field in the FEN for the current position. Here the full move number for the next half move if any is set. We align
   * here with the other methods relating to moves, like for example {@link #getMovingPiece()}, which relate to the last
   * move.
   *
   * @return The full move number for the last half move played.
   */
  int getFullMoveNumber();

  int getInitialFenFullMoveNumber();

  default int getFullMoveNumberForNextHalfMove() {
    if (isFirstMove()) {
      return getInitialFenFullMoveNumber();
    }
    return switch (getHavingMove()) {
      case BLACK -> getFullMoveNumber();
      case WHITE -> getFullMoveNumber() + 1;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  int getRepetitionCount();

  boolean isFiftyMove();

  boolean canClaimFiftyMoveRuleWithOwnMove();

  default boolean canClaimFiftyMoveRule() {
    if (isFiftyMove()) {
      return true;
    }
    return canClaimFiftyMoveRuleWithOwnMove();
  }

  boolean isSeventyFiveMove();

  boolean isThreefoldRepetition();

  boolean canClaimThreefoldRepetitionRuleWithOwnMove();

  default boolean canClaimThreefoldRepetitionRule() {
    if (isThreefoldRepetition()) {
      return true;
    }
    return canClaimThreefoldRepetitionRuleWithOwnMove();
  }

  boolean isFivefoldRepetition();

  default InsufficientMaterial calculateInsufficientMaterial() {
    if (isInsufficientMaterial()) {
      return InsufficientMaterial.BOTH;
    }
    if (isInsufficientMaterial(Side.WHITE)) {
      return InsufficientMaterial.WHITE_ONLY;
    }
    if (isInsufficientMaterial(Side.BLACK)) {
      return InsufficientMaterial.BLACK_ONLY;
    }
    return InsufficientMaterial.NONE;
  }

  /**
   * Quick (microsecond-scale, structural) CHA-based check whether the current position is dead — unwinnable for both
   * sides. Three-valued: {@code DEAD_POSITION}, {@code NON_DEAD_POSITION}, {@code POSSIBLY_NON_DEAD_POSITION} (the third
   * is a deliberate honesty signal — the quick algorithm is sound but not complete).
   */
  default DeadPositionQuick isDeadPositionQuick() {
    final UnwinnableQuick unwinnableWhite = UnwinnableQuickAnalyzer.unwinnableQuick(this, Side.WHITE);
    final UnwinnableQuick unwinnableBlack = UnwinnableQuickAnalyzer.unwinnableQuick(this, Side.BLACK);

    if (unwinnableWhite == UnwinnableQuick.UNWINNABLE && unwinnableBlack == UnwinnableQuick.UNWINNABLE) {
      return DeadPositionQuick.DEAD_POSITION;
    }

    if (unwinnableWhite == UnwinnableQuick.WINNABLE && unwinnableBlack == UnwinnableQuick.WINNABLE) {
      return DeadPositionQuick.NON_DEAD_POSITION;
    }

    return DeadPositionQuick.POSSIBLY_NON_DEAD_POSITION;
  }

  /**
   * Full (deep-search) CHA-based check whether the current position is dead — unwinnable for both sides. Three-valued:
   * {@code DEAD_POSITION}, {@code NON_DEAD_POSITION}, {@code UNDETERMINED} (the third when the search hits the
   * 500&nbsp;000-position bound; most positions resolve well below it).
   */
  default DeadPositionFull isDeadPositionFull() {
    final UnwinnableFull unwinnableWhite = UnwinnableFullAnalyzer.unwinnableFull(this, Side.WHITE).unwinnableFull();
    if (unwinnableWhite == UnwinnableFull.WINNABLE) {
      return DeadPositionFull.NON_DEAD_POSITION;
    }

    final UnwinnableFull unwinnableBlack = UnwinnableFullAnalyzer.unwinnableFull(this, Side.BLACK).unwinnableFull();
    if (unwinnableBlack == UnwinnableFull.WINNABLE) {
      return DeadPositionFull.NON_DEAD_POSITION;
    }

    if (unwinnableWhite == UnwinnableFull.UNWINNABLE && unwinnableBlack == UnwinnableFull.UNWINNABLE) {
      return DeadPositionFull.DEAD_POSITION;
    }

    return DeadPositionFull.UNDETERMINED;
  }

  /**
   * Quick (microsecond-scale, structural) CHA-based check whether the given side has a theoretical mating sequence in
   * the current position. Three-valued: {@code WINNABLE}, {@code UNWINNABLE}, {@code POSSIBLY_WINNABLE} (the third when
   * the structural test cannot decide — sound but not complete).
   */
  default UnwinnableQuick isUnwinnableQuick(Side side) {
    return UnwinnableQuickAnalyzer.unwinnableQuick(this, side);
  }

  /**
   * Full (deep-search) CHA-based check whether the given side has a theoretical mating sequence in the current
   * position. Three-valued: {@code WINNABLE}, {@code UNWINNABLE}, {@code UNDETERMINED} (the third when the search hits
   * the 500&nbsp;000-position bound; most positions resolve well below it).
   */
  default UnwinnableFull isUnwinnableFull(Side side) {
    return UnwinnableFullAnalyzer.unwinnableFull(this, side).unwinnableFull();
  }

  // name collision with API Carlos's method which must be adapted for warnings
  String getFen();

  Fen getInitialFen();

  String getSan();

  String getLan();

  /**
   * The moved piece except for castling.
   *
   * @return For all moves except castling moves, the moved piece is returned. For the castling move the none piece is
   *         returned.
   */
  Piece getMovingPiece();

  boolean isCapture();

  Side getHavingMove();

  /**
   * Checks if en passant capture possible and legal.
   *
   * @return true if a pawn of first player just advanced two squares next to a pawn of the second player, and capturing
   *         the pawn of the first player en passant by the second player is a legal move, false otherwise.
   */
  boolean isEnPassantCapturePossible();

  CastlingRight getCastlingRightWhite();

  CastlingRight getCastlingRightBlack();

  default CastlingRight getCastlingRight(Side havingMove) {
    return switch (havingMove) {
      case WHITE -> getDynamicPosition().castlingRightWhite();
      case BLACK -> getDynamicPosition().castlingRightBlack();
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  int getPerformedHalfMoveCount();

  ImmutableList<DynamicPosition> getDynamicPositionList();

  DynamicPosition getDynamicPosition();

  ImmutableList<HalfMove> getHalfMoveList();

  ImmutableSet<MoveSpecification> getPossibleMoveSpecificationSet();

  ImmutableSet<LegalMove> getLegalMoveSet();

  ImmutableList<MoveSpecification> getPerformedMoveSpecificationList();

  ImmutableList<LegalMove> getPerformedLegalMoveList();

  default ImmutableSet<String> getLegalMovesSan() {
    final Set<String> result = new TreeSet<>();
    for (final MoveSpecification moveSpecification : getPossibleMoveSpecificationSet()) {
      this.performMove(moveSpecification);
      result.add(getSan());
      this.unperformMove();
    }
    return NonNullWrapperCommon.copyOfSet(result);
  }

  default ImmutableSet<String> getLegalMovesUci() {
    final Set<String> result = new TreeSet<>();
    final Side havingMove = getHavingMove();
    for (final MoveSpecification moveSpecification : getPossibleMoveSpecificationSet()) {
      final String uci = UciMoveUtility.convertMoveSpecificationToUci(havingMove, moveSpecification).text();
      result.add(uci);
    }
    return NonNullWrapperCommon.copyOfSet(result);
  }

  LegalMove getLastMove();

  StaticPosition getStaticPosition();

  StaticPosition getStaticPositionBeforeLastMove();

  boolean isFirstMove();

  Square getEnPassantCaptureTargetSquare();

  CastlingRightLoss getCastlingRightLoss(Side side, CastlingMove castlingMove);

}
