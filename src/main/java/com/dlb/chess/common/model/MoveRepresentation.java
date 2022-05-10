package com.dlb.chess.common.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.model.LegalMove;

public record MoveRepresentation(MoveSpecification moveSpecification, LegalMove legalMove, String san, String lan,
    String uci) {

  public MoveSpecification moveSpecification() {
    return moveSpecification;
  }

  public LegalMove legalMove() {
    return legalMove;
  }

  public String san() {
    return san;
  }

  public String lan() {
    return lan;
  }

  public String uci() {
    return uci;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (MoveRepresentation) obj;
    return Objects.equals(lan, other.lan) && Objects.equals(legalMove, other.legalMove)
        && Objects.equals(moveSpecification, other.moveSpecification) && Objects.equals(san, other.san)
        && Objects.equals(uci, other.uci);
  }

}
