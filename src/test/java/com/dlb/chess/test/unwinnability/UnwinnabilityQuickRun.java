package com.dlb.chess.test.unwinnability;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.pgn.LenientPgnParser;
import com.dlb.chess.pgn.PgnGame;
import com.dlb.chess.pgn.PgnUtility;
import com.dlb.chess.test.pgn.setup.PgnTestCaseCatalog;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.unwinnability.UnwinnableQuickAnalyzer;

public class UnwinnabilityQuickRun {

  public static void main(String[] args) {

    final var pgnFileName = "05_helpmate2_white_to_move.pgn";

    final PgnTest pgnTest = PgnTestCaseCatalog.findPgnTestPgnNotListed(pgnFileName);
    final PgnGame pgnGame = LenientPgnParser.parse(pgnTest.getFolderPath(), pgnFileName);
    final Board board = PgnUtility.calculateBoard(pgnGame, false);

    System.out.println("White quick: " + UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.WHITE));

    System.out.println("Black quick: " + UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.BLACK));

  }

}
