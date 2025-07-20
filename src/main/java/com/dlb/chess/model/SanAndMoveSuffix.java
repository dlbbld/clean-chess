package com.dlb.chess.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.enums.MoveSuffixAnnotation;

public record SanAndMoveSuffix(@NonNull String san, @NonNull MoveSuffixAnnotation moveSuffixAnnotation) {

}
