package com.dlb.chess.model;

import com.dlb.chess.enums.KingSafetyCheck;
import com.google.common.collect.ImmutableSet;

public record LegalMoveCalculation(ImmutableSet<LegalMove> legalMoveSet, ImmutableSet<PseudoLegalMove> pseudoLegalMoveSet,
    KingSafetyCheck pseudoLegalKingSafety) {

}
