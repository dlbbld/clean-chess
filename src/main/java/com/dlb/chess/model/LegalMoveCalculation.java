package com.dlb.chess.model;

import java.util.Set;

import com.dlb.chess.enums.KingSafetyCheck;

public record LegalMoveCalculation(Set<LegalMove> legalMoveSet, Set<PseudoLegalMove> pseudoLegalMoveSet,
    KingSafetyCheck pseudoLegalKingSafety) {

}
