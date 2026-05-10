package com.dlb.chess.test.pgn.parser.model;

import java.util.List;

public record PgnSan(String startingFen, List<String> sanList) {

}
