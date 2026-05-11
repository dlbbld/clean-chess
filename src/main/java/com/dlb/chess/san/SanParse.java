package com.dlb.chess.san;

import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.SanFormat;

public record SanParse(SanFormat sanFormat, SanConversion sanConversion) {

}
