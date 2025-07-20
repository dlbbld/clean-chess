package com.dlb.chess.pgn.reader.model;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

public record PgnSan(@NonNull String startingFen, @NonNull List<String> sanList) {

}
