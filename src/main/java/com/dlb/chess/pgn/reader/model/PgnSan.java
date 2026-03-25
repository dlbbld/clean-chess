package com.dlb.chess.pgn.reader.model;

import java.util.List;

public record PgnSan(String startingFen, List<String> sanList) {

}
