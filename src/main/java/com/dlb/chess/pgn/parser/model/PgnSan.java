package com.dlb.chess.pgn.parser.model;

import java.util.List;

public record PgnSan(String startingFen, List<String> sanList) {

}
