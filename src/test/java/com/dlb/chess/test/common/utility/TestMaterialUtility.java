package com.dlb.chess.test.common.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.SquareType;
import com.dlb.chess.common.utility.MaterialUtility;

class TestMaterialUtility {

  // Kings only: K vs k
  private static final String FEN_KINGS_ONLY = "4k3/8/8/8/8/8/8/4K3 w - - 0 1";

  // White has rook: K+R vs k
  private static final String FEN_WHITE_ROOK = "4k3/8/8/8/8/8/8/R3K3 w - - 0 1";

  // Black has rook: K vs k+r
  private static final String FEN_BLACK_ROOK = "r3k3/8/8/8/8/8/8/4K3 w - - 0 1";

  // White has knight: K+N vs k
  private static final String FEN_WHITE_KNIGHT = "4k3/8/8/8/8/8/8/1N2K3 w - - 0 1";

  // Black has knight: K vs k+n
  private static final String FEN_BLACK_KNIGHT = "1n2k3/8/8/8/8/8/8/4K3 w - - 0 1";

  // White has bishop on d1 (dark square): K+B vs k
  private static final String FEN_WHITE_BISHOP_DARK = "4k3/8/8/8/8/8/8/3BK3 w - - 0 1";

  // White has bishop on f1 (light square): K+B vs k
  private static final String FEN_WHITE_BISHOP_LIGHT = "4k3/8/8/8/8/8/8/4KB2 w - - 0 1";

  // Black has bishop: K vs k+b
  private static final String FEN_BLACK_BISHOP = "2b1k3/8/8/8/8/8/8/4K3 w - - 0 1";

  // White has queen: K+Q vs k
  private static final String FEN_WHITE_QUEEN = "4k3/8/8/8/8/8/8/3QK3 w - - 0 1";

  // Black has queen: K vs k+q
  private static final String FEN_BLACK_QUEEN = "3qk3/8/8/8/8/8/8/4K3 w - - 0 1";

  // White has pawn: K+P vs k
  private static final String FEN_WHITE_PAWN = "4k3/8/8/8/8/8/P7/4K3 w - - 0 1";

  // Black has pawn: K vs k+p
  private static final String FEN_BLACK_PAWN = "4k3/p7/8/8/8/8/8/4K3 w - - 0 1";

  // White has knight + bishop: K+N+B vs k
  private static final String FEN_WHITE_KNIGHT_BISHOP = "4k3/8/8/8/8/8/8/1NB1K3 w - - 0 1";

  // White has 2 bishops on opposite colors: c1=dark, f4=light: K+B+B vs k
  private static final String FEN_WHITE_OPPOSITE_BISHOPS = "4k3/8/8/8/5B2/8/8/2B1K3 w - - 0 1";

  // White has 2 pawns: K+PP vs k
  private static final String FEN_WHITE_TWO_PAWNS = "4k3/8/8/8/8/8/PP6/4K3 w - - 0 1";

  private static StaticPosition position(String fen) {
    return new Board(fen).getStaticPosition();
  }

  // --- Group 1: calculateHasPieceType (including king) ---

  @SuppressWarnings("static-method")
  @Test
  void testHasPieceTypeKing() {
    final var posKingsOnly = position(FEN_KINGS_ONLY);

    // King is always present for both sides
    assertTrue(MaterialUtility.calculateHasPieceType(Side.WHITE, PieceType.KING, posKingsOnly));
    assertTrue(MaterialUtility.calculateHasPieceType(Side.BLACK, PieceType.KING, posKingsOnly));

    // Both-sides overload
    assertTrue(MaterialUtility.calculateHasPieceType(PieceType.KING, posKingsOnly));
  }

  // --- Group 2: Boolean has/hasNo per piece type and side ---

  @SuppressWarnings("static-method")
  @Test
  void testHasRook() {
    final var posWhiteRook = position(FEN_WHITE_ROOK);
    final var posBlackRook = position(FEN_BLACK_ROOK);
    final var posKingsOnly = position(FEN_KINGS_ONLY);

    assertTrue(MaterialUtility.calculateHasRook(Side.WHITE, posWhiteRook));
    assertFalse(MaterialUtility.calculateHasRook(Side.BLACK, posWhiteRook));
    assertFalse(MaterialUtility.calculateHasRook(Side.WHITE, posBlackRook));
    assertTrue(MaterialUtility.calculateHasRook(Side.BLACK, posBlackRook));
    assertFalse(MaterialUtility.calculateHasRook(Side.WHITE, posKingsOnly));
    assertFalse(MaterialUtility.calculateHasRook(Side.BLACK, posKingsOnly));

    // Both-sides overload
    assertTrue(MaterialUtility.calculateHasRook(posWhiteRook));
    assertTrue(MaterialUtility.calculateHasRook(posBlackRook));
    assertFalse(MaterialUtility.calculateHasRook(posKingsOnly));

    // HasNo
    assertFalse(MaterialUtility.calculateHasNoRooks(Side.WHITE, posWhiteRook));
    assertTrue(MaterialUtility.calculateHasNoRooks(Side.BLACK, posWhiteRook));
    assertTrue(MaterialUtility.calculateHasNoRooks(Side.WHITE, posKingsOnly));
  }

  @SuppressWarnings("static-method")
  @Test
  void testHasKnight() {
    final var posWhiteKnight = position(FEN_WHITE_KNIGHT);
    final var posBlackKnight = position(FEN_BLACK_KNIGHT);
    final var posKingsOnly = position(FEN_KINGS_ONLY);

    assertTrue(MaterialUtility.calculateHasKnight(Side.WHITE, posWhiteKnight));
    assertFalse(MaterialUtility.calculateHasKnight(Side.BLACK, posWhiteKnight));
    assertFalse(MaterialUtility.calculateHasKnight(Side.WHITE, posBlackKnight));
    assertTrue(MaterialUtility.calculateHasKnight(Side.BLACK, posBlackKnight));
    assertFalse(MaterialUtility.calculateHasKnight(Side.WHITE, posKingsOnly));

    assertTrue(MaterialUtility.calculateHasKnight(posWhiteKnight));
    assertFalse(MaterialUtility.calculateHasKnight(posKingsOnly));

    assertFalse(MaterialUtility.calculateHasNoKnights(Side.WHITE, posWhiteKnight));
    assertTrue(MaterialUtility.calculateHasNoKnights(Side.WHITE, posKingsOnly));
  }

  @SuppressWarnings("static-method")
  @Test
  void testHasBishop() {
    final var posWhiteBishop = position(FEN_WHITE_BISHOP_DARK);
    final var posBlackBishop = position(FEN_BLACK_BISHOP);
    final var posKingsOnly = position(FEN_KINGS_ONLY);

    assertTrue(MaterialUtility.calculateHasBishop(Side.WHITE, posWhiteBishop));
    assertFalse(MaterialUtility.calculateHasBishop(Side.BLACK, posWhiteBishop));
    assertFalse(MaterialUtility.calculateHasBishop(Side.WHITE, posBlackBishop));
    assertTrue(MaterialUtility.calculateHasBishop(Side.BLACK, posBlackBishop));
    assertFalse(MaterialUtility.calculateHasBishop(Side.WHITE, posKingsOnly));

    assertTrue(MaterialUtility.calculateHasBishop(posWhiteBishop));
    assertFalse(MaterialUtility.calculateHasBishop(posKingsOnly));

    assertFalse(MaterialUtility.calculateHasNoBishops(Side.WHITE, posWhiteBishop));
    assertTrue(MaterialUtility.calculateHasNoBishops(Side.WHITE, posKingsOnly));
  }

  @SuppressWarnings("static-method")
  @Test
  void testHasQueen() {
    final var posWhiteQueen = position(FEN_WHITE_QUEEN);
    final var posBlackQueen = position(FEN_BLACK_QUEEN);
    final var posKingsOnly = position(FEN_KINGS_ONLY);

    assertTrue(MaterialUtility.calculateHasQueen(Side.WHITE, posWhiteQueen));
    assertFalse(MaterialUtility.calculateHasQueen(Side.BLACK, posWhiteQueen));
    assertFalse(MaterialUtility.calculateHasQueen(Side.WHITE, posBlackQueen));
    assertTrue(MaterialUtility.calculateHasQueen(Side.BLACK, posBlackQueen));
    assertFalse(MaterialUtility.calculateHasQueen(Side.WHITE, posKingsOnly));

    assertTrue(MaterialUtility.calculateHasQueen(posWhiteQueen));
    assertFalse(MaterialUtility.calculateHasQueen(posKingsOnly));

    assertFalse(MaterialUtility.calculateHasNoQueens(Side.WHITE, posWhiteQueen));
    assertTrue(MaterialUtility.calculateHasNoQueens(Side.WHITE, posKingsOnly));
  }

  @SuppressWarnings("static-method")
  @Test
  void testHasPawn() {
    final var posWhitePawn = position(FEN_WHITE_PAWN);
    final var posBlackPawn = position(FEN_BLACK_PAWN);
    final var posKingsOnly = position(FEN_KINGS_ONLY);

    assertTrue(MaterialUtility.calculateHasPawn(Side.WHITE, posWhitePawn));
    assertFalse(MaterialUtility.calculateHasPawn(Side.BLACK, posWhitePawn));
    assertFalse(MaterialUtility.calculateHasPawn(Side.WHITE, posBlackPawn));
    assertTrue(MaterialUtility.calculateHasPawn(Side.BLACK, posBlackPawn));
    assertFalse(MaterialUtility.calculateHasPawn(Side.WHITE, posKingsOnly));

    assertTrue(MaterialUtility.calculateHasPawn(posWhitePawn));
    assertFalse(MaterialUtility.calculateHasPawn(posKingsOnly));

    assertFalse(MaterialUtility.calculateHasNoPawns(Side.WHITE, posWhitePawn));
    assertTrue(MaterialUtility.calculateHasNoPawns(Side.WHITE, posKingsOnly));
  }

  // --- Group 3: Count methods ---

  @SuppressWarnings("static-method")
  @Test
  void testCountKing() {
    final var posKingsOnly = position(FEN_KINGS_ONLY);

    // King count is always 1 for each side
    assertEquals(1, MaterialUtility.calculateNumberOfPieces(Side.WHITE, posKingsOnly, PieceType.KING));
    assertEquals(1, MaterialUtility.calculateNumberOfPieces(Side.BLACK, posKingsOnly, PieceType.KING));
  }

  @SuppressWarnings("static-method")
  @Test
  void testCountRooks() {
    final var posWhiteRook = position(FEN_WHITE_ROOK);
    final var posKingsOnly = position(FEN_KINGS_ONLY);

    assertEquals(1, MaterialUtility.calculateNumberOfRooks(Side.WHITE, posWhiteRook));
    assertEquals(0, MaterialUtility.calculateNumberOfRooks(Side.BLACK, posWhiteRook));
    assertEquals(0, MaterialUtility.calculateNumberOfRooks(Side.WHITE, posKingsOnly));
  }

  @SuppressWarnings("static-method")
  @Test
  void testCountKnights() {
    final var posWhiteKnight = position(FEN_WHITE_KNIGHT);
    final var posKingsOnly = position(FEN_KINGS_ONLY);

    assertEquals(1, MaterialUtility.calculateNumberOfKnights(Side.WHITE, posWhiteKnight));
    assertEquals(0, MaterialUtility.calculateNumberOfKnights(Side.BLACK, posWhiteKnight));
    assertEquals(0, MaterialUtility.calculateNumberOfKnights(Side.WHITE, posKingsOnly));
  }

  @SuppressWarnings("static-method")
  @Test
  void testCountBishops() {
    final var posWhiteBishop = position(FEN_WHITE_BISHOP_DARK);
    final var posOpposite = position(FEN_WHITE_OPPOSITE_BISHOPS);
    final var posKingsOnly = position(FEN_KINGS_ONLY);

    assertEquals(1, MaterialUtility.calculateNumberOfBishops(Side.WHITE, posWhiteBishop));
    assertEquals(0, MaterialUtility.calculateNumberOfBishops(Side.BLACK, posWhiteBishop));
    assertEquals(2, MaterialUtility.calculateNumberOfBishops(Side.WHITE, posOpposite));
    assertEquals(0, MaterialUtility.calculateNumberOfBishops(Side.WHITE, posKingsOnly));
  }

  @SuppressWarnings("static-method")
  @Test
  void testCountQueens() {
    final var posWhiteQueen = position(FEN_WHITE_QUEEN);
    final var posKingsOnly = position(FEN_KINGS_ONLY);

    assertEquals(1, MaterialUtility.calculateNumberOfQueens(Side.WHITE, posWhiteQueen));
    assertEquals(0, MaterialUtility.calculateNumberOfQueens(Side.BLACK, posWhiteQueen));
    assertEquals(0, MaterialUtility.calculateNumberOfQueens(Side.WHITE, posKingsOnly));
  }

  @SuppressWarnings("static-method")
  @Test
  void testCountPawns() {
    final var posWhitePawn = position(FEN_WHITE_PAWN);
    final var posWhiteTwoPawns = position(FEN_WHITE_TWO_PAWNS);
    final var posKingsOnly = position(FEN_KINGS_ONLY);

    assertEquals(1, MaterialUtility.calculateNumberOfPawns(Side.WHITE, posWhitePawn));
    assertEquals(2, MaterialUtility.calculateNumberOfPawns(Side.WHITE, posWhiteTwoPawns));
    assertEquals(0, MaterialUtility.calculateNumberOfPawns(Side.BLACK, posWhitePawn));
    assertEquals(0, MaterialUtility.calculateNumberOfPawns(Side.WHITE, posKingsOnly));
  }

  // --- Group 4: Piece identity methods ---

  @SuppressWarnings("static-method")
  @Test
  void testIsOwnPiece() {
    assertTrue(MaterialUtility.calculateIsOwnPiece(Side.WHITE, Piece.WHITE_ROOK));
    assertTrue(MaterialUtility.calculateIsOwnPiece(Side.WHITE, Piece.WHITE_KING));
    assertFalse(MaterialUtility.calculateIsOwnPiece(Side.WHITE, Piece.BLACK_ROOK));
    assertFalse(MaterialUtility.calculateIsOwnPiece(Side.WHITE, Piece.NONE));

    assertTrue(MaterialUtility.calculateIsOwnPiece(Side.BLACK, Piece.BLACK_PAWN));
    assertFalse(MaterialUtility.calculateIsOwnPiece(Side.BLACK, Piece.WHITE_PAWN));
  }

  @SuppressWarnings("static-method")
  @Test
  void testIsOwnPieceButNotKing() {
    assertTrue(MaterialUtility.calculateIsOwnPieceButNotKing(Side.WHITE, Piece.WHITE_ROOK));
    assertTrue(MaterialUtility.calculateIsOwnPieceButNotKing(Side.WHITE, Piece.WHITE_PAWN));
    assertFalse(MaterialUtility.calculateIsOwnPieceButNotKing(Side.WHITE, Piece.WHITE_KING));
    assertFalse(MaterialUtility.calculateIsOwnPieceButNotKing(Side.WHITE, Piece.BLACK_ROOK));
    assertFalse(MaterialUtility.calculateIsOwnPieceButNotKing(Side.WHITE, Piece.NONE));

    assertTrue(MaterialUtility.calculateIsOwnPieceButNotKing(Side.BLACK, Piece.BLACK_QUEEN));
    assertFalse(MaterialUtility.calculateIsOwnPieceButNotKing(Side.BLACK, Piece.BLACK_KING));
  }

  @SuppressWarnings("static-method")
  @Test
  void testIsOwnPieceType() {
    assertTrue(MaterialUtility.calculateIsOwnPieceType(Side.WHITE, Piece.WHITE_ROOK, PieceType.ROOK));
    assertFalse(MaterialUtility.calculateIsOwnPieceType(Side.WHITE, Piece.WHITE_ROOK, PieceType.BISHOP));
    assertFalse(MaterialUtility.calculateIsOwnPieceType(Side.WHITE, Piece.BLACK_ROOK, PieceType.ROOK));
    assertFalse(MaterialUtility.calculateIsOwnPieceType(Side.WHITE, Piece.NONE, PieceType.ROOK));

    assertTrue(MaterialUtility.calculateIsOwnPieceType(Side.WHITE, Piece.WHITE_KING, PieceType.KING));
    assertTrue(MaterialUtility.calculateIsOwnPieceType(Side.BLACK, Piece.BLACK_KING, PieceType.KING));
  }

  // --- Group 5: Composite "king and X only" methods ---

  @SuppressWarnings("static-method")
  @Test
  void testHasKingOnly() {
    final var posKingsOnly = position(FEN_KINGS_ONLY);
    final var posWhiteRook = position(FEN_WHITE_ROOK);

    assertTrue(MaterialUtility.calculateHasKingOnly(Side.WHITE, posKingsOnly));
    assertTrue(MaterialUtility.calculateHasKingOnly(Side.BLACK, posKingsOnly));
    assertFalse(MaterialUtility.calculateHasKingOnly(Side.WHITE, posWhiteRook));
    assertTrue(MaterialUtility.calculateHasKingOnly(Side.BLACK, posWhiteRook));
  }

  @SuppressWarnings("static-method")
  @Test
  void testHasKingAndPieceOnly() {
    final var posWhiteRook = position(FEN_WHITE_ROOK);
    final var posWhiteKnight = position(FEN_WHITE_KNIGHT);
    final var posWhiteBishop = position(FEN_WHITE_BISHOP_DARK);
    final var posWhiteQueen = position(FEN_WHITE_QUEEN);
    final var posKingsOnly = position(FEN_KINGS_ONLY);

    // King and rook only
    assertTrue(MaterialUtility.calculateHasKingAndRookOnly(Side.WHITE, posWhiteRook));
    assertFalse(MaterialUtility.calculateHasKingAndRookOnly(Side.BLACK, posWhiteRook));
    assertFalse(MaterialUtility.calculateHasKingAndRookOnly(Side.WHITE, posKingsOnly));

    // King and knight only
    assertTrue(MaterialUtility.calculateHasKingAndKnightOnly(Side.WHITE, posWhiteKnight));
    assertFalse(MaterialUtility.calculateHasKingAndKnightOnly(Side.WHITE, posKingsOnly));

    // King and bishop only
    assertTrue(MaterialUtility.calculateHasKingAndBishopOnly(Side.WHITE, posWhiteBishop));
    assertFalse(MaterialUtility.calculateHasKingAndBishopOnly(Side.WHITE, posKingsOnly));

    // King and queen only
    assertTrue(MaterialUtility.calculateHasKingAndQueenOnly(Side.WHITE, posWhiteQueen));
    assertFalse(MaterialUtility.calculateHasKingAndQueenOnly(Side.WHITE, posKingsOnly));
  }

  @SuppressWarnings("static-method")
  @Test
  void testHasKingAndKnightAndBishopOnly() {
    final var posKnightBishop = position(FEN_WHITE_KNIGHT_BISHOP);
    final var posWhiteKnight = position(FEN_WHITE_KNIGHT);
    final var posKingsOnly = position(FEN_KINGS_ONLY);

    assertTrue(MaterialUtility.calculateHasKingAndKnightAndBishopOnly(Side.WHITE, posKnightBishop));
    assertFalse(MaterialUtility.calculateHasKingAndKnightAndBishopOnly(Side.WHITE, posWhiteKnight));
    assertFalse(MaterialUtility.calculateHasKingAndKnightAndBishopOnly(Side.WHITE, posKingsOnly));
  }

  @SuppressWarnings("static-method")
  @Test
  void testHasKingAndKnightAndBishop() {
    final var posKnightBishop = position(FEN_WHITE_KNIGHT_BISHOP);
    final var posWhiteKnight = position(FEN_WHITE_KNIGHT);

    assertTrue(MaterialUtility.calculateHasKingAndKnightAndBishop(Side.WHITE, posKnightBishop));
    assertFalse(MaterialUtility.calculateHasKingAndKnightAndBishop(Side.WHITE, posWhiteKnight));
  }

  // --- Group 6: Bishop square-color methods ---

  @SuppressWarnings("static-method")
  @Test
  void testBishopSquareColors() {
    // d1 is a dark square
    final var posDarkBishop = position(FEN_WHITE_BISHOP_DARK);
    // f1 is a light square
    final var posLightBishop = position(FEN_WHITE_BISHOP_LIGHT);
    final var posOpposite = position(FEN_WHITE_OPPOSITE_BISHOPS);
    final var posKingsOnly = position(FEN_KINGS_ONLY);

    // Dark square bishop
    assertTrue(MaterialUtility.calculateHasDarkSquareBishops(Side.WHITE, posDarkBishop));
    assertFalse(MaterialUtility.calculateHasLightSquareBishops(Side.WHITE, posDarkBishop));
    assertFalse(MaterialUtility.calculateHasNoDarkSquareBishops(Side.WHITE, posDarkBishop));
    assertTrue(MaterialUtility.calculateHasNoLightSquareBishops(Side.WHITE, posDarkBishop));

    // Light square bishop
    assertTrue(MaterialUtility.calculateHasLightSquareBishops(Side.WHITE, posLightBishop));
    assertFalse(MaterialUtility.calculateHasDarkSquareBishops(Side.WHITE, posLightBishop));

    // Opposite square bishops
    assertTrue(MaterialUtility.calculateHasLightSquareBishops(Side.WHITE, posOpposite));
    assertTrue(MaterialUtility.calculateHasDarkSquareBishops(Side.WHITE, posOpposite));
    assertEquals(1, MaterialUtility.calculateNumberOfBishops(Side.WHITE, posOpposite, SquareType.LIGHT_SQUARE));
    assertEquals(1, MaterialUtility.calculateNumberOfBishops(Side.WHITE, posOpposite, SquareType.DARK_SQUARE));

    // No bishops
    assertFalse(MaterialUtility.calculateHasLightSquareBishops(Side.WHITE, posKingsOnly));
    assertFalse(MaterialUtility.calculateHasDarkSquareBishops(Side.WHITE, posKingsOnly));
    assertEquals(0, MaterialUtility.calculateNumberOfBishops(Side.WHITE, posKingsOnly, SquareType.LIGHT_SQUARE));
  }

  @SuppressWarnings("static-method")
  @Test
  void testHasKingAndOppositeSquaresBishop() {
    final var posOpposite = position(FEN_WHITE_OPPOSITE_BISHOPS);
    final var posDarkBishop = position(FEN_WHITE_BISHOP_DARK);

    assertTrue(MaterialUtility.calculateHasKingAndOppositeSquaresBishop(Side.WHITE, posOpposite));
    assertFalse(MaterialUtility.calculateHasKingAndOppositeSquaresBishop(Side.WHITE, posDarkBishop));
  }

  @SuppressWarnings("static-method")
  @Test
  void testHasKingAndOppositeSquaresBishopOnly() {
    final var posOpposite = position(FEN_WHITE_OPPOSITE_BISHOPS);
    final var posDarkBishop = position(FEN_WHITE_BISHOP_DARK);
    final var posKingsOnly = position(FEN_KINGS_ONLY);

    assertTrue(MaterialUtility.calculateHasKingAndOppositeSquaresBishopOnly(Side.WHITE, posOpposite));
    assertFalse(MaterialUtility.calculateHasKingAndOppositeSquaresBishopOnly(Side.WHITE, posDarkBishop));
    assertFalse(MaterialUtility.calculateHasKingAndOppositeSquaresBishopOnly(Side.WHITE, posKingsOnly));
  }

  @SuppressWarnings("static-method")
  @Test
  void testHasKingAndBishopsOnly() {
    // c1=dark square, so white has king + dark-square bishop only
    final var posDarkBishop = position(FEN_WHITE_BISHOP_DARK);
    final var posOpposite = position(FEN_WHITE_OPPOSITE_BISHOPS);
    final var posKingsOnly = position(FEN_KINGS_ONLY);

    assertTrue(MaterialUtility.calculateHasKingAndBishopsOnly(Side.WHITE, posDarkBishop, SquareType.DARK_SQUARE));
    assertFalse(MaterialUtility.calculateHasKingAndBishopsOnly(Side.WHITE, posDarkBishop, SquareType.LIGHT_SQUARE));
    assertFalse(MaterialUtility.calculateHasKingAndBishopsOnly(Side.WHITE, posOpposite, SquareType.DARK_SQUARE));
    assertFalse(MaterialUtility.calculateHasKingAndBishopsOnly(Side.WHITE, posKingsOnly, SquareType.DARK_SQUARE));
  }

  // --- Group 7: Other composite methods ---

  @SuppressWarnings("static-method")
  @Test
  void testHasKingAndPawnsOrQueensOnly() {
    final var posWhitePawn = position(FEN_WHITE_PAWN);
    final var posWhiteQueen = position(FEN_WHITE_QUEEN);
    final var posWhiteRook = position(FEN_WHITE_ROOK);
    final var posKingsOnly = position(FEN_KINGS_ONLY);

    assertTrue(MaterialUtility.calculateHasKingAndPawnsOrQueensOnly(Side.WHITE, posWhitePawn));
    assertTrue(MaterialUtility.calculateHasKingAndPawnsOrQueensOnly(Side.WHITE, posWhiteQueen));
    assertFalse(MaterialUtility.calculateHasKingAndPawnsOrQueensOnly(Side.WHITE, posWhiteRook));
    assertFalse(MaterialUtility.calculateHasKingAndPawnsOrQueensOnly(Side.WHITE, posKingsOnly));
  }

}
