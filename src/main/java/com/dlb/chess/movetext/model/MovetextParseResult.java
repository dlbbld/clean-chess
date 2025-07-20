package com.dlb.chess.movetext.model;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.common.model.Movetext;
import com.dlb.chess.model.PgnHalfMove;

public record MovetextParseResult(@NonNull Movetext movetext, @NonNull List<PgnHalfMove> halfMoveParseList,
    @NonNull String leadingCommentary) {

}
