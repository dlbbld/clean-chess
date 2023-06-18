package com.dlb.chess.fen.model;

import org.eclipse.jdt.annotation.NonNull;

@SuppressWarnings("null")
public record FenRaw(@NonNull String piecePlacement, @NonNull String havingMove, @NonNull String castlingRightBothStr,
    @NonNull String enPassantCaptureTargetSquare, @NonNull String halfMoveClock, @NonNull String fullMoveNumber) {

}
