package com.dlb.chess.test.unwinnability.againstcha.model;

import com.dlb.chess.unwinnability.UnwinnabilityFullVerdict;
import com.dlb.chess.unwinnability.UnwinnabilityQuickVerdict;

public record AmbronaUnwinnabilityVerdicts(UnwinnabilityFullVerdict fullWhite, UnwinnabilityFullVerdict fullBlack,
    UnwinnabilityQuickVerdict quickWhite, UnwinnabilityQuickVerdict quickBlack) {

}
