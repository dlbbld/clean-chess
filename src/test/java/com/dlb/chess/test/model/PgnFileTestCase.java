package com.dlb.chess.test.model;

import com.dlb.chess.analysis.enums.CheckmateOrStalemate;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public record PgnFileTestCase(String pgnFileName, String expectedRepetition,
    String expectedRepetitionInitialEnPassantCapture, String expectedYawnMoveRule, int firstCapture,
    int maxYawnSequence, CheckmateOrStalemate checkmateOrStalemate, int repetitionCountFinalPosition,
    InsufficientMaterial insufficientMaterial, UnwinnableFull unwinnableFullWhite, UnwinnableFull unwinnableFullBlack,
    UnwinnableQuick unwinnableQuickWhite, UnwinnableQuick unwinnableQuickBlack, String fen) {

}
