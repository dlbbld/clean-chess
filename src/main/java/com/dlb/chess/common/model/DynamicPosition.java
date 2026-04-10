package com.dlb.chess.common.model;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.Side;

public record DynamicPosition(Side havingMove, StaticPosition staticPosition, boolean isEnPassantCapturePossible,
    CastlingRight castlingRightWhite, CastlingRight castlingRightBlack) {

}
