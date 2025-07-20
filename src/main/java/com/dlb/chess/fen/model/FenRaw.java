package com.dlb.chess.fen.model;

import org.eclipse.jdt.annotation.NonNull;

public record FenRaw(@NonNull String piecePlacement, @NonNull String havingMove, @NonNull String castlingRightBothStr,
    @NonNull String enPassantCaptureTargetSquare, @NonNull String halfMoveClock, @NonNull String fullMoveNumber) {

}
