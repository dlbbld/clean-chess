package com.dlb.chess.san.model;

import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.enums.SanFormat;

public record SanParse(SanFormat sanFormat, SanConversion sanConversion) {

}
