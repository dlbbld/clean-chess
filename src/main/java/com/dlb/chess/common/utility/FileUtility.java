package com.dlb.chess.common.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.FileSystemAccessException;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;

public abstract class FileUtility {

  /**
   * Reads the entire contents of a file as a single UTF-8 string, preserving line terminators exactly as they
   * appear on disk. Complements {@link #readFileLines(Path)} — use this when the parser needs to see the raw source
   * (for example to detect a missing trailing newline).
   */
  public static String readFileAsString(Path filePath) {
    final var file = filePath.toFile();
    if (!file.exists()) {
      throw new FileSystemAccessException("File \"" + filePath + "\" was not found.");
    }
    if (!file.isFile()) {
      throw new FileSystemAccessException("\"" + filePath + "\" is not a file.");
    }
    try {
      @SuppressWarnings("null") @NonNull final String content = Files.readString(filePath, StandardCharsets.UTF_8);
      return content;
    } catch (final IOException ioe) {
      throw new FileSystemAccessException("Reading file \"" + filePath + "\" failed.", ioe);
    }
  }

  /**
   * Reading a file linewise, without including linebreaks or adding spaces after a line break.
   */
  public static List<String> readFileLines(Path folderPath, String fileName) {
    return readFileLines(calculateFilePath(folderPath, fileName));
  }

  public static List<String> readFileLines(Path filePath) {
    final List<String> fileLines = new ArrayList<>();

    final var file = filePath.toFile();

    if (!file.exists()) {
      throw new FileSystemAccessException("File \"" + filePath + "\" was not found.");
    }

    if (!file.isFile()) {
      throw new FileSystemAccessException("\"" + filePath + "\" is not a file.");
    }

    try (final Scanner myReader = new Scanner(file, StandardCharsets.UTF_8)) {
      while (myReader.hasNextLine()) {
        final String currentLine = NonNullWrapperCommon.nextLine(myReader);
        fileLines.add(currentLine);
      }
    } catch (final IOException ioe) {
      throw new FileSystemAccessException("Reading file \"" + filePath + "\" failed.", ioe);
    }

    return fileLines;
  }

  public static Path calculateFilePath(Path folderPath, String fileName) {
    return NonNullWrapperCommon.resolve(folderPath, fileName);
  }

  public static void writeFile(Path folderPath, String fileName, List<String> lineList) {
    writeFile(calculateFilePath(folderPath, fileName), lineList);
  }

  public static void writeFile(Path folderPath, String fileName, String line) {
    final List<String> lineList = new ArrayList<>();
    lineList.add(line);

    writeFile(calculateFilePath(folderPath, fileName), lineList);
  }

  public static void writeFile(Path filePath, String line) {
    final List<String> lineList = new ArrayList<>();
    lineList.add(line);

    writeFile(filePath, lineList);
  }

  public static void writeFile(Path filePath, List<String> lineList) {
    deleteFile(filePath);
    try (var writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
      for (final String line : lineList) {
        writer.write(line);
        writer.write("\n");
      }

    } catch (final IOException ex) {
      ex.printStackTrace();
    }
  }

  public static void appendFile(Path filePath, String line) {

    final List<String> lineList = new ArrayList<>();
    lineList.add(line);

    appendFile(filePath, lineList);
  }

  public static void appendFile(Path filePath, List<String> lineList) {

    if (!exists(filePath)) {
      writeFile(filePath, lineList);
    } else {

      final var file = filePath.toFile();
      if (!file.isFile()) {
        throw new IllegalArgumentException("\"" + filePath + "\" is not a file");
      }
      try (Writer w = new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8.name());
          PrintWriter pw = new PrintWriter(w)) {
        for (final String line : lineList) {
          pw.println(line);
        }
      } catch (final IOException ioe) {
        throw new FileSystemAccessException("File appending to \"" + filePath + "\" failed", ioe);
      }
    }
  }

  public static void deleteIfExists(Path folderPath, String fileName) {
    if (exists(folderPath, fileName)) {
      deleteFile(folderPath, fileName);
    }
  }

  public static void deleteIfExists(Path path) {
    if (exists(path)) {
      deleteFile(path);
    }
  }

  public static boolean exists(Path folderPath, String fileName) {
    return exists(calculateFilePath(folderPath, fileName));
  }

  public static boolean exists(Path path) {
    return Files.exists(path);
  }

  public static void deleteFile(Path folderPath, String fileName) {
    deleteFile(calculateFilePath(folderPath, fileName));
  }

  public static void deleteFile(Path path) {
    try {
      Files.deleteIfExists(path);
    } catch (final IOException ioe) {
      throw new FileSystemAccessException("Deletion of file \"" + path + "\" failed", ioe);
    }
  }

  public static void deleteFilesInDirectory(Path folderPath) {
    final var folder = folderPath.toFile();
    if (!folder.isDirectory()) {
      throw new IllegalArgumentException("\"" + folderPath + "\" is not a directory");
    }

    final var filesList = folder.listFiles();
    if (filesList == null) {
      throw new FileSystemAccessException("File list retrieval for \"" + folderPath + "\" failed");
    }

    for (final File file : filesList) {
      if (file == null) {
        throw new ProgrammingMistakeException("Wrong assumption about API behaviour");
      }
      final var path = file.toPath();
      try {
        Files.delete(path);
      } catch (final NoSuchFileException nsfe) {
        throw new FileSystemAccessException(
            "The file " + file.getAbsolutePath() + " could not be deleted - no such file", nsfe);
      } catch (final DirectoryNotEmptyException dnee) {
        throw new FileSystemAccessException(
            "The file " + file.getAbsolutePath() + " could not be deleted - directory not empty", dnee);
      } catch (final IOException ioe) {
        throw new FileSystemAccessException(
            "The file " + file.getAbsolutePath() + " could not be deleted - permission issue", ioe);
      }
    }
  }

  /**
   * Walks {@code root} recursively and returns every regular file under it as a {@link List} of {@link Path}.
   * The underlying {@link Stream} is fully drained and closed before this method returns, so callers do not
   * have to (and should not try to) manage stream lifetime themselves.
   */
  @SuppressWarnings("null")
  public static List<Path> listAllFilesRecursively(Path root) {
    try (Stream<@NonNull Path> stream = Files.walk(root)) {
      @NonNull final List<@NonNull Path> result = stream.filter(Files::isRegularFile).toList();
      return result;
    } catch (final IOException ioe) {
      throw new FileSystemAccessException("Recursive file listing of \"" + root + "\" failed", ioe);
    }
  }

  public static List<String> readFileNameList(Path folderPath) {
    final List<String> result = new ArrayList<>();
    final var folder = folderPath.toFile();
    if (!folder.isDirectory()) {
      throw new IllegalArgumentException("\"" + folderPath + "\" is not a directory");
    }
    final var filesList = folder.listFiles();
    if (filesList == null) {
      throw new FileSystemAccessException("File list retrieval for \"" + folderPath + "\" failed");
    }

    for (final File file : filesList) {
      if (file == null) {
        throw new ProgrammingMistakeException("Wrong assumption about API behaviour");
      }
      result.add(NonNullWrapperCommon.getName(file));
    }
    return result;
  }

}
