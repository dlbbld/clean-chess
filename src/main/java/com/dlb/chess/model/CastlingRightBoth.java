package com.dlb.chess.model;

import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.CastlingRightLoss;

public record CastlingRightBoth(CastlingRight castlingRightWhite, CastlingRight castlingRightBlack,
    CastlingRightLoss whiteKingSideLoss, CastlingRightLoss whiteQueenSideLoss, CastlingRightLoss blackKingSideLoss,
    CastlingRightLoss blackQueenSideLoss) {

  public CastlingRightBoth(CastlingRight castlingRightWhite, CastlingRight castlingRightBlack) {
    this(castlingRightWhite, castlingRightBlack, CastlingRightLoss.NONE, CastlingRightLoss.NONE,
        CastlingRightLoss.NONE, CastlingRightLoss.NONE);
  }

}
