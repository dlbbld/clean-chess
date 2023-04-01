package com.dlb.chess.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveRepresentation;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.ucimove.utility.UciMoveUtility;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.unwinnability.full.UnwinnableFullAnalyzer;
import com.dlb.chess.unwinnability.full.enums.DeadPositionFull;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.quick.UnwinnableQuickAnalyzer;
import com.dlb.chess.unwinnability.quick.enums.DeadPositionQuick;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public abstract class AbstractBoard implements ApiBoard, EnumConstants {

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

  @Override
  public DeadPositionFull isDeadPositionFull() {
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

  @Override
  public UnwinnableQuick isUnwinnableQuick(Side side) {
    return UnwinnableQuickAnalyzer.unwinnableQuick(this, side);
  }

  @Override
  public UnwinnableFull isUnwinnableFull(Side side) {
    return UnwinnableFullAnalyzer.unwinnableFull(this, side).unwinnableFull();
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
    return isStalemate() || isDeadPositionQuick() == DeadPositionQuick.DEAD_POSITION || isFivefoldRepetition()
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
    return switch (getHavingMove()) {
      case BLACK -> getFullMoveNumber();
      case WHITE -> getFullMoveNumber() + 1;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

}
