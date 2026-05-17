package com.dlb.chess.common.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.jdt.annotation.Owning;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.common.exceptions.ProgrammingMistakeException;

public abstract class IoUtility {

  private IoUtility() {
  }

  public static Process startProcess(ProcessBuilder processBuilder) throws IOException {
    final Process result = processBuilder.start();
    if (result == null) {
      throw new ProgrammingMistakeException("Assumed value is not null");
    }
    return result;
  }

  @Owning
  public static InputStream getInputStream(Process process) {
    final InputStream result = process.getInputStream();
    if (result == null) {
      throw new ProgrammingMistakeException("Assumed value is not null");
    }
    return result;
  }

  @Owning
  public static InputStream getErrorStream(Process process) {
    final InputStream result = process.getErrorStream();
    if (result == null) {
      throw new ProgrammingMistakeException("Assumed value is not null");
    }
    return result;
  }

  @Owning
  public static OutputStream getOutputStream(Process process) {
    final OutputStream result = process.getOutputStream();
    if (result == null) {
      throw new ProgrammingMistakeException("Assumed value is not null");
    }
    return result;
  }

  @SuppressWarnings("null")
  public static List<String> readAllLines(Path path, @Nullable Charset charset) throws IOException {
    if (charset == null) {
      throw new ProgrammingMistakeException("Assumed value is not null");
    }
    final List<String> result = Files.readAllLines(path, charset);
    if (result == null) {
      throw new ProgrammingMistakeException("Assumed value is not null");
    }
    return result;
  }
}
