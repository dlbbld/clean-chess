package com.dlb.chess.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.enums.CastlingRight;

public record CastlingRightBoth(@NonNull CastlingRight castlingRightWhite, @NonNull CastlingRight castlingRightBlack) {

}
