package com.dlb.chess.test.librarycarlos.utility;

import java.nio.file.Path;

import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.test.librarycarlos.NonNullWrapperLibraryCarlos;
import com.github.bhlangonijr.chesslib.game.Game;
import com.github.bhlangonijr.chesslib.pgn.PgnHolder;

public class PgnParserUtilityLibraryCarlos {

  private PgnParserUtilityLibraryCarlos() {
  }

  public static Game parse(Path pgnFolderPath, String pgnFileName) {

    final Path pgnFilePath = FileUtility.calculateFilePath(pgnFolderPath, pgnFileName);

    final var pgnHolder = new PgnHolder(pgnFilePath.toAbsolutePath().toString());
    try {
      pgnHolder.loadPgn();
    } catch (final Exception e) {
      throw new RuntimeException("An error occurred loading the PGN with Carlos API", e);
    }

    if (pgnHolder.getGames().size() != 1) {
      throw new IllegalArgumentException("The PGN must contain exactly one game");
    }

    final Game game = NonNullWrapperLibraryCarlos.getGame(pgnHolder, 0);
    try {
      game.loadMoveText();
    } catch (final Exception e) {
      throw new RuntimeException("An error occurred loading the PGN movetext with Carlos API", e);
    }

    return game;
  }

}
