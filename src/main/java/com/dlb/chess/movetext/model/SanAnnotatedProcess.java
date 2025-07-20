package com.dlb.chess.movetext.model;

import org.eclipse.jdt.annotation.NonNull;

public record SanAnnotatedProcess(@NonNull String sanAnnotated, boolean isExhausted, @NonNull String remainingValue) {

}
