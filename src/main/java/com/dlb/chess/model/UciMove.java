package com.dlb.chess.model;

import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.enums.UciValidateHelper;

public record UciMove(UciValidateHelper uciMove, Square fromSquare, Square toSquare,
    String text, boolean isPromotion, PromotionPieceType promotionPieceType) {

}
