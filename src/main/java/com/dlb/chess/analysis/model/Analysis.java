package com.dlb.chess.analysis.model;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.analysis.enums.CheckmateOrStalemate;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

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

}
