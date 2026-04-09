package com.dlb.chess.model;

import java.util.Set;

public record LegalMoveCalculation(Set<LegalMove> legalMoveSet, Set<PseudoLegalMove> pseudoLegalMoveSet,
    PseudoLegalReason pseudoLegalReason) {

}
