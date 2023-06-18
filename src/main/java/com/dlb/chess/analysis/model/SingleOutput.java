package com.dlb.chess.analysis.model;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

@SuppressWarnings("null")
public record SingleOutput(@NonNull Analysis analysis, @NonNull List<String> output) {
}
