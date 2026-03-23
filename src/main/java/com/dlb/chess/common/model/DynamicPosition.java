package com.dlb.chess.common.model;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.model.CastlingRightBoth;

public record DynamicPosition(Side havingMove, StaticPosition staticPosition,
    boolean isEnPassantCapturePossible, CastlingRightBoth castlingRightBoth) {

}
