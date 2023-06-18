package com.dlb.chess.movetext.model;

import org.eclipse.jdt.annotation.NonNull;

@SuppressWarnings("null")
public record SanAnnotatedProcess(@NonNull String sanAnnotated, boolean isExhausted, @NonNull String remainingValue) {

}
