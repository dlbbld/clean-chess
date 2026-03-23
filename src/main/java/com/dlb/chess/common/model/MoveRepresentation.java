package com.dlb.chess.common.model;

import com.dlb.chess.model.LegalMove;

public record MoveRepresentation(MoveSpecification moveSpecification, LegalMove legalMove,
    String san, String lan, String uci) {

}
