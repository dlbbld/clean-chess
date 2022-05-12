package com.dlb.chess.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.enums.DeadPositionQuick;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveRepresentation;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.ucimove.utility.UciMoveUtility;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.unwinnability.full.UnwinnableFullCalculator;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.quick.UnwinnableQuickCalculator;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public abstract class AbstractBoard implements ApiBoard, EnumConstants {

  // TODO very slow, must be fast, though now for generating test cases only
  @Override
  public Set<String> getLegalMovesSan() {
    final Set<String> result = new TreeSet<>();

    for (final MoveSpecification moveSpecification : getPossibleMoveSpecificationSet()) {
      this.performMove(moveSpecification);
      result.add(getSan());
      this.unperformMove();
    }
    return result;
  }

  @Override
  public Set<String> getLegalMovesUci() {
    final Set<String> result = new TreeSet<>();
    for (final MoveSpecification moveSpecification : getPossibleMoveSpecificationSet()) {
      final String uci = UciMoveUtility.convertMoveSpecificationToUci(moveSpecification).text();
      result.add(uci);
    }
    return result;
  }

  @Override
  public List<MoveRepresentation> getLegalMovesRepresentation() {
    final List<MoveRepresentation> result = new ArrayList<>();

    for (final MoveSpecification moveSpecification : getPossibleMoveSpecificationSet()) {
      this.performMove(moveSpecification);
      final LegalMove legalMove = getLastMove();
      final String san = getSan();
      final String lan = getLan();
      this.unperformMove();
      final String uci = UciMoveUtility.convertMoveSpecificationToUci(moveSpecification).text();

      final MoveRepresentation moveRepresentation = new MoveRepresentation(moveSpecification, legalMove, san, lan, uci);
      result.add(moveRepresentation);
    }
    return result;
  }

  @Override
  public DeadPositionQuick isDeadPositionQuick() {
    final UnwinnableQuick unwinnableWhite = UnwinnableQuickCalculator.unwinnableQuick(this, Side.WHITE);
    final UnwinnableQuick unwinnableBlack = UnwinnableQuickCalculator.unwinnableQuick(this, Side.BLACK);

    if (unwinnableWhite == UnwinnableQuick.UNWINNABLE && unwinnableBlack == UnwinnableQuick.UNWINNABLE) {
      return DeadPositionQuick.YES;
    }

    if (unwinnableWhite == UnwinnableQuick.WINNABLE && unwinnableBlack == UnwinnableQuick.WINNABLE) {
      return DeadPositionQuick.NO;
    }

    return DeadPositionQuick.MAYBE;
  }

  @Override
  public boolean isDeadPositionFull() {
    final UnwinnableFull unwinnableWhite = UnwinnableFullCalculator.unwinnableFull(this, Side.WHITE);
    if (unwinnableWhite == UnwinnableFull.WINNABLE) {
      return false;
    }
    final UnwinnableFull unwinnableBlack = UnwinnableFullCalculator.unwinnableFull(this, Side.BLACK);

    return unwinnableBlack == UnwinnableFull.UNWINNABLE;
  }

  @Override
  public UnwinnableQuick isUnwinnableQuick(Side side) {
    return UnwinnableQuickCalculator.unwinnableQuick(this, side);
  }

  @Override
  public boolean isUnwinnableFull(Side side) {
    return UnwinnableFullCalculator.unwinnableFull(this, side) == UnwinnableFull.UNWINNABLE;
  }

  @Override
  public InsufficientMaterial calculateInsufficientMaterial() {
    if (isInsufficientMaterial()) {
      return InsufficientMaterial.BOTH;
    }
    if (isInsufficientMaterial(WHITE)) {
      return InsufficientMaterial.WHITE_ONLY;
    }
    if (isInsufficientMaterial(BLACK)) {
      return InsufficientMaterial.BLACK_ONLY;
    }
    return InsufficientMaterial.NONE;
  }

  @Override
  public boolean isGameEnd() {
    if (isCheckmate()) {
      return true;
    }
    return isGameDraw();
  }

  private boolean isGameDraw() {
    return isStalemate() || isDeadPositionQuick() == DeadPositionQuick.YES || isFivefoldRepetition()
        || isSeventyFiftyMove();
  }

  @Override
  public boolean canClaimFiftyMoveRule() {
    if (isFiftyMove()) {
      return true;
    }
    return canClaimFiftyMoveRuleWithOwnMove();
  }

  @Override
  public boolean canClaimThreefoldRepetitionRule() {
    if (isThreefoldRepetition()) {
      return true;
    }
    return canClaimThreefoldRepetitionRuleWithOwnMove();
  }

  @Override
  public CastlingRight getCastlingRight(Side havingMove) {
    return CastlingUtility.getCastlingRight(this.getCastlingRightBoth(), havingMove);
  }

  @Override
  public int getFullMoveNumberForNextHalfMove() {
    if (isFirstMove()) {
      return getInitialFenFullMoveNumber();
    }
    switch (getHavingMove()) {
      case BLACK:
        return getFullMoveNumber();
      case WHITE:
        return getFullMoveNumber() + 1;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

}
