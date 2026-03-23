package com.dlb.chess.movetext.model;

import java.util.List;

import com.dlb.chess.common.model.Movetext;
import com.dlb.chess.model.PgnHalfMove;

public record MovetextParseResult(Movetext movetext, List<PgnHalfMove> halfMoveParseList, String leadingCommentary) {

}
