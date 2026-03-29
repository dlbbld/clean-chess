package com.dlb.chess.test.pgn.parser;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.pgn.parser.LenientPgnParser;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.test.pgntest.enums.PgnTest;

public class PgnCacheForLenientPgnParserTestCases {
  private static final Map<String, PgnFile> PGN_CACHE = new HashMap<>();

  public static PgnFile getPgn(Path pgnFolderPath, String pgnFileName) {
    final Path pgnFilePath = FileUtility.calculateFilePath(pgnFolderPath, pgnFileName);
    if (PgnTest.existsFolderPath(pgnFolderPath)) {
      @SuppressWarnings("null") final @NonNull String key = pgnFilePath.toAbsolutePath().toString();
      if (PGN_CACHE.containsKey(key)) {
        @SuppressWarnings("null") final PgnFile pgnFile = PGN_CACHE.get(key);
        return pgnFile;
      }
      final PgnFile pgnFile = LenientPgnParser.parse(pgnFolderPath, pgnFileName);
      PGN_CACHE.put(key, pgnFile);
      return pgnFile;
    }
    // other PGN's we are not caching
    return LenientPgnParser.parse(pgnFolderPath, pgnFileName);

  }
}
