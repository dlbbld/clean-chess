package com.dlb.chess.test.pgn.writer;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.FileSystemAccessException;
import com.dlb.chess.pgn.create.PgnCreate;
import com.dlb.chess.pgn.writer.PgnWriter;

class TestPgnWriterErrorHandling {

  @SuppressWarnings("static-method")
  @Test
  void testWritePgnFilePropagatesFileSystemAccessException(@TempDir Path tempFolder) {
    final Path filePath = NonNullWrapperCommon.pathResolve(tempFolder, "missing/game.pgn");
    final var pgnFile = PgnCreate.createPgnFile(new Board());

    assertThrows(FileSystemAccessException.class, () -> PgnWriter.writePgnFile(pgnFile, filePath));
  }
}
