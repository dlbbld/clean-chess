package com.dlb.chess.test.unwinnability;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.pgn.LenientPgnParser;
import com.dlb.chess.pgn.PgnFile;
import com.dlb.chess.pgn.PgnUtility;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.unwinnability.UnwinnableFullAnalyzer;

public class UnwinnabilityFullRun {

  public static void main(String[] args) {

    final var pgnFileName = "01_m1_white_to_move.pgn";

    final PgnTest pgnTest = CreatePgnTestCases.findPgnTestPgnNotListed(pgnFileName);
    final PgnFile pgnFile = LenientPgnParser.parse(pgnTest.getFolderPath(), pgnFileName);
    final Board board = PgnUtility.calculateBoard(pgnFile, false);

    System.out.println("White full: " + UnwinnableFullAnalyzer.unwinnableFull(board, Side.WHITE).verdict());

    System.out.println("Black full: " + UnwinnableFullAnalyzer.unwinnableFull(board, Side.BLACK).verdict());

  }

}
