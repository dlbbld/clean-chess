package com.dlb.chess.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.san.model.SanExample;

public record SanPatternDescription(String pattern, String comment, @NonNull SanExample... sanExampleList) {

}
