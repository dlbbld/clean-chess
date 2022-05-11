package com.dlb.chess.unwinnability.findhelpmate;

import java.util.Comparator;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.model.LegalMove;

public class ComparatorLegalMoves implements Comparator<LegalMove> {

  private final Side color;
  private final StaticPosition staticPosition;

  public ComparatorLegalMoves(Side color, StaticPosition staticPosition) {
    this.color = color;
    this.staticPosition = staticPosition;
  }

  @Override
  public int compare(LegalMove firstPlayer, LegalMove secondPlayer) {
    return 0;
  }

}
