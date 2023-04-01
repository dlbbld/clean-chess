package com.dlb.chess.generate.squares.flip;

import com.dlb.chess.board.enums.Square;

public class GenerateSquareFlip {

  public static void main(String[] args) {
    generateSquareFlip();
  }

  private static void generateSquareFlip() {
    System.out.println("public static Square flip(Square square) {");
    System.out.println("return switch (square) {");
    System.out.println("case NONE -> throw new NonePointerException();");

    for (final Square squareToFlip : Square.BOARD_SQUARE_LIST) {
      final Square squareFlipped = Square.calculate(9 - squareToFlip.getFile().getNumber(),
          9 - squareToFlip.getRank().getNumber());
      System.out.println(
          "case " + squareToFlip.name() + "  -> " + Square.class.getSimpleName() + "." + squareFlipped.name() + ";");
    }

    System.out.println("default -> throw new IllegalArgumentException();");
    System.out.println("};");
    System.out.println("}");

  }

}
