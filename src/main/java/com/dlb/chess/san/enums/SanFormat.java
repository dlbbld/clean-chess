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

  // (1a) d3
  PAWN_NON_CAPTURING_NON_PROMOTION_FORMAT,
  // (1b) dxe5
  PAWN_CAPTURING_NON_PROMOTION_FORMAT,
  // (1c) d8=Q
  PAWN_NON_CAPTURING_PROMOTION_FORMAT,
  // (1d) dxe8=Q
  PAWN_CAPTURING_PROMOTION_FORMAT,
  // (2) Qe5, Qae5, Q2e5, Qc3e5 / Qxe5, Qaxe5, Q2xe5, Qc3xe5 (and same for rook, knight, bishop)
  PIECE_NON_CAPTURING_NEITHER_FORMAT,
  PIECE_NON_CAPTURING_FILE_FORMAT,
  PIECE_NON_CAPTURING_RANK_FORMAT,
  PIECE_NON_CAPTURING_SQUARE_FORMAT,
  PIECE_CAPTURING_NEITHER_FORMAT,
  PIECE_CAPTURING_FILE_FORMAT,
  PIECE_CAPTURING_RANK_FORMAT,
  PIECE_CAPTURING_SQUARE_FORMAT,
  // (3a) Ke5
  KING_NON_CASTLING_NON_CAPTURING_FORMAT,
  // (3b) Kxe5
  KING_NON_CASTLING_CAPTURING_FORMAT,
  // (3c) O-O-O
  KING_CASTLING_QUEEN_SIDE_FORMAT,
  // (3d) O-O
  KING_CASTLING_KING_SIDE_FORMAT;

}
