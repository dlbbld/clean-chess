package com.dlb.chess.common.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.model.LegalMove;

public record MoveRepresentation(@NonNull MoveSpecification moveSpecification, @NonNull LegalMove legalMove,
    @NonNull String san, @NonNull String lan, @NonNull String uci) {

}
