package com.dlb.chess.model;

import com.dlb.chess.board.enums.CastlingRight;

public record CastlingRightBoth(CastlingRight castlingRightWhite, CastlingRight castlingRightBlack) {

}
