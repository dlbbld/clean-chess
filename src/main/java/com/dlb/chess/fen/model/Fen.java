package com.dlb.chess.fen.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.CastlingRightBoth;

public record Fen(@NonNull String fen, @NonNull StaticPosition staticPosition, @NonNull Side havingMove,
    @NonNull CastlingRightBoth castlingRightBoth, @NonNull Square enPassantCaptureTargetSquare, int halfMoveClock,
    int fullMoveNumber) {
}
