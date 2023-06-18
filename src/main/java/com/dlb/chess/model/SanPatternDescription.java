package com.dlb.chess.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.san.model.SanExample;

@SuppressWarnings("null")
public record SanPatternDescription(@NonNull String pattern, @NonNull String comment,
    @NonNull SanExample... sanExampleList) {

}
