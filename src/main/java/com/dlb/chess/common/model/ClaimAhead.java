package com.dlb.chess.common.model;

import com.dlb.chess.model.LegalMove;

public record ClaimAhead(LegalMove legalMove, int fullMoveNumber, String san) {

}
