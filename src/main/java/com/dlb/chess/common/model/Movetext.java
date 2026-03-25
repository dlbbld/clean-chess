package com.dlb.chess.common.model;

// own class to avoid mixing up the movetext and the move list
// per the PGN specification the movetext contains the move and the game termination marker
public record Movetext(String movetext) {

}
