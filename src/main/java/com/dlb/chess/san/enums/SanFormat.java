package com.dlb.chess.san.enums;

// (1a) pawnNonCapturingNonPromotionMoves d3 not d8
// (1b) pawnCapturingNonPromotionMoves dxe5 not dxe8
// (1c) pawnNonCapturingPromotionMoves d8=Q
// (1d) pawnCapturingPromotionMoves dxe8=Q
// (2a) queenNonCapturingMoves Qe5, Qae5, Q2e5, Qc3e5
// (2b) queenCapturingMoves Qxe5, Qaxe5, Q2xe5, Qc3xe5
// (3a) rookNonCapturingMoves Re5, Rae5, R2e5
// (3b) rookCapturingMoves Rxe5, Raxe5, R2xe5
// (4a) knightNonCapturingMoves Ne5, Nce5, N4e5, Nd3e5
// (4b) knightCapturingMoves Nxe5, Ncxe5, N4xe5, Nd3xe5
// (5a) bishopNonCapturingMoves Be5, Bbe5, B2e5
// (5b) bishopCapturingMoves Bxe5, Bbxe5, B2xe5
// (6a) kingNonCastlingNonCapturingMoves Ke5
// (6b) kingNonCastlingCapturingMoves Kxe5
// (6c) kingMovesCastling O-O and O-O-O

public enum SanFormat {

  // length, piece, fromFile, fromRank, x, toFile, toRank, =, new piece

  // d3
  PAWN_NON_CAPTURING_NON_PROMOTION_FORMAT(2, -1, -1, -1, -1, 0, 1, -1, -1),
  // dxe5
  PAWN_CAPTURING_NON_PROMOTION_FORMAT(4, -1, 0, -1, 1, 2, 3, -1, -1),
  // d8=Q
  PAWN_NON_CAPTURING_PROMOTION_FORMAT(4, -1, -1, -1, -1, 0, 1, 2, 3),
  // dxe8=Q
  PAWN_CAPTURING_PROMOTION_FORMAT(6, -1, 0, -1, 1, 2, 3, 4, 5),
  // Qe5
  PIECE_NON_CAPTURING_NEITHER_FORMAT(3, 0, -1, -1, -1, 1, 2, -1, -1),
  // Qae5
  PIECE_NON_CAPTURING_FILE_FORMAT(4, 0, 1, -1, -1, 2, 3, -1, -1),
  // Q2e5
  PIECE_NON_CAPTURING_RANK_FORMAT(4, 0, -1, 1, -1, 2, 3, -1, -1),
  // Qc3e5
  PIECE_NON_CAPTURING_SQUARE_FORMAT(5, 0, 1, 2, -1, 3, 4, -1, -1),
  // Qxe5
  PIECE_CAPTURING_NEITHER_FORMAT(4, 0, -1, -1, 1, 2, 3, -1, -1),
  // Qaxe5
  PIECE_CAPTURING_FILE_FORMAT(5, 0, 1, -1, 2, 3, 4, -1, -1),
  // Q2xe5
  PIECE_CAPTURING_RANK_FORMAT(5, 0, -1, 1, 2, 3, 4, -1, -1),
  // Qc3xe5
  PIECE_CAPTURING_SQUARE_FORMAT(6, 0, 1, 2, 3, 4, 5, -1, -1),
  // Ke5
  KING_NON_CASTLING_NON_CAPTURING_FORMAT(3, 0, -1, -1, -1, 1, 2, -1, -1),
  // Kxe5
  KING_NON_CASTLING_CAPTURING_FORMAT(4, 0, -1, -1, 1, 2, 3, -1, -1),
  // O-O or O-O-O
  KING_CASTLING_QUEEN_SIDE_FORMAT(5, -1, -1, -1, -1, -1, -1, -1, -1),
  KING_CASTLING_KING_SIDE_FORMAT(3, -1, -1, -1, -1, -1, -1, -1, -1);

  private final int length;
  private final int movingPieceTypeIndex;
  private final int fromFileIndex;
  private final int fromRankIndex;
  private final int captureSymbolIndex;
  private final int toFileIndex;
  private final int toRankIndex;
  private final int promotionSymbolIndex;
  private final int promotionPieceTypeIndex;

  SanFormat(int length, int movingPieceTypeIndex, int fromFileIndex, int fromRankIndex, int captureSymbolIndex,
      int toFileIndex, int toRankIndex, int promotionSymbolIndex, int promotionPieceTypeIndex) {
    this.length = length;
    this.movingPieceTypeIndex = movingPieceTypeIndex;
    this.fromFileIndex = fromFileIndex;
    this.fromRankIndex = fromRankIndex;
    this.captureSymbolIndex = captureSymbolIndex;
    this.toFileIndex = toFileIndex;
    this.toRankIndex = toRankIndex;
    this.promotionSymbolIndex = promotionSymbolIndex;
    this.promotionPieceTypeIndex = promotionPieceTypeIndex;
  }

  public int getLength() {
    return length;
  }

  public int getMovingPieceTypeIndex() {
    return movingPieceTypeIndex;
  }

  public int getFromFileIndex() {
    return fromFileIndex;
  }

  public int getFromRankIndex() {
    return fromRankIndex;
  }

  public int getCaptureSymbolIndex() {
    return captureSymbolIndex;
  }

  public int getToFileIndex() {
    return toFileIndex;
  }

  public int getToRankIndex() {
    return toRankIndex;
  }

  public int getPromotionSymbolIndex() {
    return promotionSymbolIndex;
  }

  public int getPromotionPieceTypeIndex() {
    return promotionPieceTypeIndex;
  }

  public static boolean calculateIsCapture(SanFormat sanFormat) {
    return sanFormat.getCaptureSymbolIndex() != -1;
  }

  public static boolean calculateIsKingCastlingMove(SanFormat sanFormat) {
    return switch (sanFormat) {
      case KING_CASTLING_QUEEN_SIDE_FORMAT, KING_CASTLING_KING_SIDE_FORMAT -> true;
      case KING_NON_CASTLING_CAPTURING_FORMAT, KING_NON_CASTLING_NON_CAPTURING_FORMAT, PAWN_CAPTURING_NON_PROMOTION_FORMAT, PAWN_CAPTURING_PROMOTION_FORMAT, PAWN_NON_CAPTURING_NON_PROMOTION_FORMAT, PAWN_NON_CAPTURING_PROMOTION_FORMAT, PIECE_CAPTURING_SQUARE_FORMAT, PIECE_CAPTURING_FILE_FORMAT, PIECE_CAPTURING_NEITHER_FORMAT, PIECE_CAPTURING_RANK_FORMAT, PIECE_NON_CAPTURING_SQUARE_FORMAT, PIECE_NON_CAPTURING_FILE_FORMAT, PIECE_NON_CAPTURING_NEITHER_FORMAT, PIECE_NON_CAPTURING_RANK_FORMAT -> false;
      default -> false;
    };
  }

  public static boolean calculateIsKingNonCastlingMove(SanFormat sanFormat) {
    return switch (sanFormat) {
      case KING_NON_CASTLING_NON_CAPTURING_FORMAT, KING_NON_CASTLING_CAPTURING_FORMAT -> true;
      case KING_CASTLING_QUEEN_SIDE_FORMAT, KING_CASTLING_KING_SIDE_FORMAT, PAWN_CAPTURING_NON_PROMOTION_FORMAT, PAWN_CAPTURING_PROMOTION_FORMAT, PAWN_NON_CAPTURING_NON_PROMOTION_FORMAT, PAWN_NON_CAPTURING_PROMOTION_FORMAT, PIECE_CAPTURING_SQUARE_FORMAT, PIECE_CAPTURING_FILE_FORMAT, PIECE_CAPTURING_NEITHER_FORMAT, PIECE_CAPTURING_RANK_FORMAT, PIECE_NON_CAPTURING_SQUARE_FORMAT, PIECE_NON_CAPTURING_FILE_FORMAT, PIECE_NON_CAPTURING_NEITHER_FORMAT, PIECE_NON_CAPTURING_RANK_FORMAT -> false;
      default -> false;
    };
  }
}
