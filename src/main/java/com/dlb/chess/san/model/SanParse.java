package com.dlb.chess.san.model;

import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.enums.SanType;

public record SanParse(SanType sanType, SanConversion sanConversion) {

}
