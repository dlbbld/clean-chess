package com.dlb.chess.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.enums.UciValidateHelper;

public record UciMove(@NonNull UciValidateHelper uciMove, @NonNull Square fromSquare, @NonNull Square toSquare,
    @NonNull String text, boolean isPromotion, @NonNull PromotionPieceType promotionPieceType) {

}
