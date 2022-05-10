package com.dlb.chess.generate.san.strict;

import com.dlb.chess.board.enums.Side;

public class GeneratePawnWhiteSanValidateStrict extends AbstractPawnSanValidateStrict {

  public static void main(String[] args) {
    new GeneratePawnWhiteSanValidateStrict().generateSan();
  }

  @Override
  Side getSide() {
    return Side.WHITE;
  }

}
