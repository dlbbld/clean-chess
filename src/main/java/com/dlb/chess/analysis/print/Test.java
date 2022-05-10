package com.dlb.chess.analysis.print;

import com.dlb.chess.analysis.Analyzer;

public class Test {

  public static void main(String[] args) throws Exception {
    Analyzer.printAnalysis("C:\\Users\\danie\\git\\clean-chess\\src\\test\\resources\\pgn\\basic\\fivefold",
        "fivefold_03_2_continue_previous_and_end_with_sixfold.pgn");

    Analyzer.printAnalysis("C:\\Users\\danie\\git\\clean-chess\\src\\test\\resources\\pgn\\basic\\seventyFive",
        "seventy_five_02_6_beyond_seventy_five_end_beyond_new_seventy_five_with_yawn_moves.pgn");

  }

}
