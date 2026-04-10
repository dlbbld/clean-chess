package com.dlb.chess.fen.model;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;

public record Fen(String fen, StaticPosition staticPosition, Side havingMove, CastlingRight castlingRightWhite,
    CastlingRight castlingRightBlack, Square enPassantCaptureTargetSquare, int halfMoveClock, int fullMoveNumber) {
}
