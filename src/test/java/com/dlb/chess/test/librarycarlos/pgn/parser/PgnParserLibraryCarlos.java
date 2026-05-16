package com.dlb.chess.test.librarycarlos.pgn.parser;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.test.librarycarlos.utility.PgnParserUtilityLibraryCarlos;
import com.dlb.chess.test.pgn.parser.model.PgnSan;
import com.github.bhlangonijr.chesslib.game.Game;
import com.github.bhlangonijr.chesslib.move.Move;

public class PgnParserLibraryCarlos {

  public static PgnSan parsePgnSan(Path pgnFolderPath, String pgnName) {

    final Game game = PgnParserUtilityLibraryCarlos.parse(pgnFolderPath, pgnName);

    // we only check if the fen is set
    // the API does not check the startup value
    // also we need to check ourselves when playing the moves if the game starts from starting position or custom
    // position
    String startingFen;
    if (game.getFen() == null) {
      startingFen = FenConstants.FEN_INITIAL_STR;
    } else {
      @SuppressWarnings("null") @NonNull final String gameFen = game.getFen();
      startingFen = gameFen;
    }

    final List<String> sanList = new ArrayList<>();
    for (final Move move : game.getHalfMoves()) {
      // we know the API sets a value when loading from PGN
      @SuppressWarnings("null") @NonNull final String san = move.getSan();
      sanList.add(san);
    }

    return new PgnSan(startingFen, sanList);

  }

}
