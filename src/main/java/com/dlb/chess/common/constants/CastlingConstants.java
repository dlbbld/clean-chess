package com.dlb.chess.common.constants;

import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.san.enums.SanLetter;

public abstract class CastlingConstants implements EnumConstants {

  public static final String SAN_CASTLING_KING_SIDE = SanLetter.CASTLING_O.getLetter()
      + SanLetter.CASTLING_HYPHEN.getLetter() + SanLetter.CASTLING_O.getLetter();

  public static final String SAN_CASTLING_QUEEN_SIDE = SAN_CASTLING_KING_SIDE + SanLetter.CASTLING_HYPHEN.getLetter()
      + SanLetter.CASTLING_O.getLetter();

  public static final LegalMove WHITE_KING_SIDE_CASTLING_MOVE = new LegalMove(
      new MoveSpecification(WHITE, CastlingMove.KING_SIDE));
  public static final LegalMove WHITE_QUEEN_SIDE_CASTLING_MOVE = new LegalMove(
      new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE));

  public static final LegalMove BLACK_KING_SIDE_CASTLING_MOVE = new LegalMove(
      new MoveSpecification(BLACK, CastlingMove.KING_SIDE));
  public static final LegalMove BLACK_QUEEN_SIDE_CASTLING_MOVE = new LegalMove(
      new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE));

  // constants for white
  public static final Square WHITE_KING_FROM = E1;

  public static final Square WHITE_KING_KING_SIDE_CASTLING_TO = G1;

  public static final Square WHITE_ROOK_KING_SIDE_CASTLING_FROM = H1;

  public static final Square WHITE_ROOK_KING_SIDE_CASTLING_TO = F1;

  public static final Square WHITE_KING_QUEEN_SIDE_CASTLING_TO = C1;

  public static final Square WHITE_ROOK_QUEEN_SIDE_CASTLING_FROM = A1;

  public static final Square WHITE_ROOK_QUEEN_SIDE_CASTLING_TO = D1;

  // constants for black
  public static final Square BLACK_KING_FROM = E8;

  public static final Square BLACK_KING_KING_SIDE_CASTLING_TO = G8;

  public static final Square BLACK_ROOK_KING_SIDE_CASTLING_FROM = H8;

  public static final Square BLACK_ROOK_KING_SIDE_CASTLING_TO = F8;

  public static final Square BLACK_KING_QUEEN_SIDE_CASTLING_TO = C8;

  public static final Square BLACK_ROOK_QUEEN_SIDE_CASTLING_FROM = A8;

  public static final Square BLACK_ROOK_QUEEN_SIDE_CASTLING_TO = D8;

}
