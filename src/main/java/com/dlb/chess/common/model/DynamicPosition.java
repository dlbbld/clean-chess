package com.dlb.chess.common.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.model.CastlingRightBoth;

public record DynamicPosition(@NonNull Side havingMove, @NonNull StaticPosition staticPosition,
    boolean isEnPassantCapturePossible, @NonNull CastlingRightBoth castlingRightBoth) {

}
