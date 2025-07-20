package com.dlb.chess.movetext.model;

import org.eclipse.jdt.annotation.NonNull;

public record ReadComment(@NonNull String comment, boolean isExhausted, @NonNull String remainingValue) {

}
