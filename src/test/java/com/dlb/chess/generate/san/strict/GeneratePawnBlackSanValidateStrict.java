package com.dlb.chess.generate.san.strict;

import com.dlb.chess.board.enums.Side;

public class GeneratePawnBlackSanValidateStrict extends AbstractPawnSanValidateStrict {

  public static void main(String[] args) {
    new GeneratePawnBlackSanValidateStrict().generateSan();
  }

  @Override
  Side getSide() {
    return Side.BLACK;
  }

}
