package com.dlb.chess.pgn.reader.model;

import org.eclipse.jdt.annotation.NonNull;

@SuppressWarnings("null")
public record FirstDuplicateTag(boolean hasDuplicateTag, @NonNull String duplicateTagName) {

}
