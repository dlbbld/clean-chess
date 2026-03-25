package com.dlb.chess.analysis.model;

import java.util.List;

import com.dlb.chess.analysis.enums.CheckmateOrStalemate;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public record Analysis(Side havingMove, List<HalfMove> halfMoveList, List<List<HalfMove>> repetitionListList,
    List<List<HalfMove>> repetitionListListInitialEnPassantCapture, List<List<YawnHalfMove>> yawnMoveListList,
    boolean hasThreefoldRepetition, boolean hasThreefoldRepetitionInitialEnPassantCapture,
    boolean hasFivefoldRepetition, boolean hasFiftyMoveRule, boolean hasSeventyFiveMoveRule,
    boolean isGameContinuedOverFivefoldRepetition, boolean isGameContinuedOverSeventyFiveMove, int firstCapture,
    boolean hasCapture, int maxYawnSequence, CheckmateOrStalemate checkmateOrStalemate,
    InsufficientMaterial insufficientMaterial, UnwinnableFull unwinnableFullWhite, UnwinnableFull unwinnableFullBlack,
    UnwinnableQuick unwinnableQuickWhite, UnwinnableQuick unwinnableQuickBlack, String fen, ApiBoard board) {

}
