package com.dlb.chess.test.pgn.reader;

import java.util.HashMap;
import java.util.Map;

import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.pgn.reader.PgnReaderStrict;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.pgntest.enums.PgnTest;

public class PgnStrictCacheForTestCases {
  private static final Map<String, PgnFile> PGN_CACHE = new HashMap<>();

  public static PgnFile getPgn(String pgnFolderPath, String pgnFileName) {
    final String pgnFilePath = FileUtility.calculateFilePath(pgnFolderPath, pgnFileName);
    if (PgnTest.existsFolderPath(pgnFolderPath)) {
      if (PGN_CACHE.containsKey(pgnFilePath)) {
        // checked before
        @SuppressWarnings("null") final PgnFile pgnFileCache = PGN_CACHE.get(pgnFilePath);
        return pgnFileCache;
      }
      final PgnFile pgnFile = PgnReaderStrict.readPgn(pgnFolderPath, pgnFileName);
      PGN_CACHE.put(pgnFilePath, pgnFile);
      return pgnFile;
    }
    // other PGN's we are not caching
    return PgnReaderStrict.readPgn(pgnFolderPath, pgnFileName);

  }
}
