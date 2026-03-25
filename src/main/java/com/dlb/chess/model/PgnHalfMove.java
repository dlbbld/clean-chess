package com.dlb.chess.model;

import com.dlb.chess.enums.MoveSuffixAnnotation;

public record PgnHalfMove(String san, MoveSuffixAnnotation moveSuffixAnnotation, String commentary) {

}
