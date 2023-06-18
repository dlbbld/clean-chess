package com.dlb.chess.san.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.enums.SanType;

@SuppressWarnings("null")
public record SanParse(@NonNull SanType sanType, @NonNull SanConversion sanConversion) {

}
