package com.dlb.chess.report;

import java.util.List;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.board.Board;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.report.CheckmateOrStalemate;

/**
 * Result of analyzing a fully-replayed game. Note that under the strict move-validation pipeline a game cannot continue
 * past automatic FIDE terminations (fivefold, 75-move, etc.), so the "continued past" diagnostics that previously lived
 * here have moved to {@link com.dlb.chess.pgn.diagnostic.GameContinuationScanner}, which operates on raw halfmove
 * sequences for corpus-mining use cases.
 */
public record Report(Side havingMove, List<HalfMove> halfMoveList, List<List<HalfMove>> repetitionListList,
    List<List<HalfMove>> repetitionListListInitialEnPassantCapture,
    List<List<NoProgressHalfMove>> noProgressMoveListList, boolean hasThreefoldRepetition,
    boolean hasThreefoldRepetitionInitialEnPassantCapture, boolean hasFivefoldRepetition, boolean hasFiftyMoveRule,
    boolean hasSeventyFiveMoveRule, int firstCapture, boolean hasCapture, int maxNoProgressSequence,
    CheckmateOrStalemate checkmateOrStalemate, InsufficientMaterial insufficientMaterial, String fen,
    Board board) {

}
