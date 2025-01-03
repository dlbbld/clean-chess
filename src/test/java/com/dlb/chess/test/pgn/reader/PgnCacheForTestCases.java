package com.dlb.chess.test.pgn.reader;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.pgn.reader.PgnReader;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.pgntest.enums.PgnTest;

public class PgnCacheForTestCases {
  private static final Map<String, PgnFile> PGN_CACHE = new HashMap<>();

  public static PgnFile getPgn(Path pgnFolderPath, String pgnFileName) {
    final Path pgnFilePath = FileUtility.calculateFilePath(pgnFolderPath, pgnFileName);
    if (PgnTest.existsFolderPath(pgnFolderPath)) {
      final var key = pgnFilePath.toAbsolutePath().toString();
      if (PGN_CACHE.containsKey(key)) {
        @SuppressWarnings("null") final PgnFile pgnFile = PGN_CACHE.get(key);
        return pgnFile;
      }
      final PgnFile pgnFile = PgnReader.readPgn(pgnFolderPath, pgnFileName);
      PGN_CACHE.put(key, pgnFile);
      return pgnFile;
    }
    // other PGN's we are not caching
    return PgnReader.readPgn(pgnFolderPath, pgnFileName);

  }
}
