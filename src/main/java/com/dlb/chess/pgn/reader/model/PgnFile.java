package com.dlb.chess.pgn.reader.model;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.PgnHalfMove;

@SuppressWarnings("null")
public record PgnFile(@NonNull List<@NonNull Tag> tagList, @NonNull Fen startFen, @NonNull String leadingCommentary,
    @NonNull List<@NonNull PgnHalfMove> halfMoveList) {

}
