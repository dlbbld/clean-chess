package com.dlb.chess.analysis.model;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.analysis.enums.CheckmateOrStalemate;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

@SuppressWarnings("null")
public record Analysis(@NonNull Side havingMove, @NonNull List<HalfMove> halfMoveList,
    @NonNull List<List<HalfMove>> repetitionListList,
    @NonNull List<List<HalfMove>> repetitionListListInitialEnPassantCapture,
    @NonNull List<List<YawnHalfMove>> yawnMoveListList, boolean hasThreefoldRepetition,
    boolean hasThreefoldRepetitionInitialEnPassantCapture, boolean hasFivefoldRepetition, boolean hasFiftyMoveRule,
    boolean hasSeventyFiveMoveRule, boolean isGameContinuedOverFivefoldRepetition,
    boolean isGameContinuedOverSeventyFiveMove, int firstCapture, boolean hasCapture, int maxYawnSequence,
    @NonNull CheckmateOrStalemate checkmateOrStalemate, @NonNull InsufficientMaterial insufficientMaterial,
    @NonNull UnwinnableFull unwinnableFullWhite, @NonNull UnwinnableFull unwinnableFullBlack,
    @NonNull UnwinnableQuick unwinnableQuickWhite, @NonNull UnwinnableQuick unwinnableQuickBlack, @NonNull String fen,
    @NonNull ApiBoard board) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (Analysis) obj;
    return Objects.equals(board, other.board) && Objects.equals(fen, other.fen)
        && Objects.equals(yawnMoveListList, other.yawnMoveListList) && firstCapture == other.firstCapture
        && Objects.equals(halfMoveList, other.halfMoveList) && hasCapture == other.hasCapture
        && hasFiftyMoveRule == other.hasFiftyMoveRule && hasFivefoldRepetition == other.hasFivefoldRepetition
        && hasSeventyFiveMoveRule == other.hasSeventyFiveMoveRule
        && hasThreefoldRepetition == other.hasThreefoldRepetition
        && hasThreefoldRepetitionInitialEnPassantCapture == other.hasThreefoldRepetitionInitialEnPassantCapture
        && havingMove == other.havingMove && insufficientMaterial == other.insufficientMaterial
        && isGameContinuedOverFivefoldRepetition == other.isGameContinuedOverFivefoldRepetition
        && isGameContinuedOverSeventyFiveMove == other.isGameContinuedOverSeventyFiveMove
        && checkmateOrStalemate == other.checkmateOrStalemate && maxYawnSequence == other.maxYawnSequence
        && Objects.equals(repetitionListList, other.repetitionListList)
        && Objects.equals(repetitionListListInitialEnPassantCapture, other.repetitionListListInitialEnPassantCapture)
        && unwinnableFullWhite == other.unwinnableFullWhite && unwinnableFullBlack == other.unwinnableFullBlack
        && unwinnableQuickWhite == other.unwinnableQuickWhite && unwinnableQuickBlack == other.unwinnableQuickBlack;
  }

}
