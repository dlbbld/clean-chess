package com.dlb.chess.fen.model;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.CastlingRightBoth;

public record Fen(String fen, StaticPosition staticPosition, Side havingMove, CastlingRightBoth castlingRightBoth,
    Square enPassantCaptureTargetSquare, int halfMoveClock, int fullMoveNumber) {
}
