package com.dlb.chess.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.enums.MoveSuffixAnnotation;

@SuppressWarnings("null")
public record PgnHalfMove(@NonNull String san, @NonNull MoveSuffixAnnotation moveSuffixAnnotation,
    @NonNull String commentary) {

}
