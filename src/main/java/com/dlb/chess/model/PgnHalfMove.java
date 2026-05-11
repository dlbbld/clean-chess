package com.dlb.chess.model;

import com.dlb.chess.enums.MoveSuffixAnnotation;
import com.dlb.chess.pgn.PgnCommentary;

public record PgnHalfMove(String san, MoveSuffixAnnotation moveSuffixAnnotation, PgnCommentary commentary) {

}
