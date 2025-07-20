package com.dlb.chess.common.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.model.LegalMove;

public record ClaimAhead(@NonNull LegalMove legalMove, int fullMoveNumber, @NonNull String san) {

}
