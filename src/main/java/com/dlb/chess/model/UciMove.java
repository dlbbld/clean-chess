package com.dlb.chess.model;

import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Square;

public record UciMove(Square fromSquare, Square toSquare, String text, boolean isPromotion,
    PromotionPieceType promotionPieceType) {

}
