package com.dlb.chess.test.generate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.exceptions.FileSystemAccessException;
import com.dlb.chess.test.ConfigurationTestConstants;
import com.dlb.chess.test.model.PgnTestCase;
import com.dlb.chess.test.model.PgnTestCaseList;
import com.dlb.chess.test.pgn.setup.PgnTestCaseCatalog;
import com.dlb.chess.test.pgntest.enums.PgnTest;

/**
 * Regenerates the Ambrona unwinnability oracle from the cached final FENs in {@link PgnTestCase}. Requires WSL with
 * D3-Chess built and Stockfish installed; pass the WSL D3-Chess checkout root as the optional first argument when it is
 * not at the default location.
 */
public final class GenerateAmbronaUnwinnabilityOracle {

  private static final Logger logger = Nulls.getLogger(GenerateAmbronaUnwinnabilityOracle.class);

  private static final String D3_CHESS_PATH_PROPERTY = "ambrona.d3.path";
  private static final String WSL_RUNNER_PATH = "/tmp/clean-chess-ambrona-oracle/cha-oracle";
  private static final int PROGRESS_LOG_INTERVAL = 25;

  private static final Path CPP_SOURCE_PATH = Nulls.pathResolve(ConfigurationTestConstants.PROJECT_ROOT_FOLDER_PATH,
      "tools/ambrona-oracle/cha_oracle.cpp");
  private static final Path ORACLE_PATH = Nulls.pathResolve(ConfigurationTestConstants.PROJECT_ROOT_FOLDER_PATH,
      "src/test/resources/oracle/ambrona-unwinnability.tsv");

  private GenerateAmbronaUnwinnabilityOracle() {
  }

  public static void main(String[] args) throws Exception {
    if (args.length > 1) {
      throw new IllegalArgumentException("Usage: GenerateAmbronaUnwinnabilityOracle [wsl-d3-chess-root]");
    }
    final String d3ChessRoot = resolveD3ChessRoot(args);
    generate(d3ChessRoot);
  }

  private static String resolveD3ChessRoot(String[] args) throws Exception {
    if (args.length == 1) {
      return Nulls.get(args, 0);
    }
    final String propertyValue = System.getProperty(D3_CHESS_PATH_PROPERTY);
    if (propertyValue != null) {
      return propertyValue;
    }
    return readWslDefaultD3ChessRoot();
  }

  private static void generate(String d3ChessRoot) throws Exception {
    final List<String> fenList = collectDistinctFinalFenList();
    logger.info("Collected {} distinct final FENs from the PGN test cases.", fenList.size());

    buildRunner(d3ChessRoot);
    final List<String> oracleLineList = runOracle(fenList);

    Files.createDirectories(Nulls.getParent(ORACLE_PATH));
    final List<String> fileLineList = new ArrayList<>();
    fileLineList.add("fen\tfullWhite\tfullBlack\tquickWhite\tquickBlack");
    fileLineList.addAll(oracleLineList);
    Files.writeString(ORACLE_PATH, Nulls.join("\n", fileLineList) + "\n", StandardCharsets.UTF_8);
    logger.info("Wrote {} oracle rows to {}", oracleLineList.size(), ORACLE_PATH);
  }

  private static List<String> collectDistinctFinalFenList() {
    final Set<String> fenSet = new LinkedHashSet<>();
    for (final PgnTest pgnTest : PgnTest.values()) {
      final PgnTestCaseList testCaseList = PgnTestCaseCatalog.getTestList(pgnTest);
      for (final PgnTestCase testCase : testCaseList.list()) {
        fenSet.add(testCase.finalFen());
      }
    }
    return new ArrayList<>(fenSet);
  }

  private static void buildRunner(String d3ChessRoot) throws Exception {
    final String cppSourcePath = windowsPathToWsl(CPP_SOURCE_PATH);
    final String quotedD3ChessRoot = shellQuote(d3ChessRoot);
    final String command = "mkdir -p /tmp/clean-chess-ambrona-oracle && g++ -o " + shellQuote(WSL_RUNNER_PATH) + " "
        + shellQuote(cppSourcePath) + " " + quotedD3ChessRoot + "/src/util.cpp " + quotedD3ChessRoot
        + "/src/semistatic.cpp " + quotedD3ChessRoot + "/src/dynamic.cpp " + quotedD3ChessRoot + "/src/cha.cpp "
        + "-lpthread -O3 -I/usr/local/include/stockfish -I" + quotedD3ChessRoot + "/src -lstockfish";
    runWslCommand(command);
  }

  private static List<String> runOracle(List<String> fenList) throws Exception {
    final ProcessBuilder processBuilder = new ProcessBuilder("wsl", "bash", "-lc",
        "LD_LIBRARY_PATH=/usr/local/lib " + shellQuote(WSL_RUNNER_PATH));
    final Process process = processBuilder.start();
    final List<String> result = new ArrayList<>();

    try (BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(Nulls.getOutputStream(process), StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(Nulls.getInputStream(process),
            StandardCharsets.UTF_8))) {

      var processed = 0;
      for (final String fen : fenList) {
        writer.write(fen);
        writer.write('\n');
        writer.flush();

        final String resultLine = reader.readLine();
        if (resultLine == null) {
          throw new IllegalStateException("Ambrona oracle runner stopped before returning a result for " + fen);
        }
        validateResultLine(resultLine);
        result.add(resultLine);
        processed++;

        if (processed % PROGRESS_LOG_INTERVAL == 0 || processed == fenList.size()) {
          logger.info("Generated {}/{} Ambrona oracle rows.", processed, fenList.size());
        }
      }
    }

    final int exitCode = process.waitFor();
    if (exitCode != 0) {
      throw new IllegalStateException("Ambrona oracle runner exited with " + exitCode + ": "
          + readStream(Nulls.getErrorStream(process)));
    }
    return result;
  }

  private static void validateResultLine(String resultLine) {
    final String[] itemArray = Nulls.split(resultLine, "\t");
    if (itemArray.length != 5) {
      throw new IllegalStateException("Invalid oracle TSV row: " + resultLine);
    }
  }

  private static String windowsPathToWsl(Path path) throws Exception {
    final String windowsPath = path.toAbsolutePath().toString().replace('\\', '/');
    final ProcessBuilder processBuilder = new ProcessBuilder("wsl", "wslpath", "-a", windowsPath);
    final Process process = processBuilder.start();
    final String output = readStream(Nulls.getInputStream(process)).trim();
    final String error = readStream(Nulls.getErrorStream(process)).trim();
    final int exitCode = process.waitFor();
    if (exitCode != 0) {
      throw new IllegalStateException("wslpath failed with " + exitCode + ": " + error);
    }
    return output;
  }

  private static String readWslDefaultD3ChessRoot() throws Exception {
    final ProcessBuilder processBuilder = new ProcessBuilder("wsl", "bash", "-lc", "printf '%s' \"$HOME/D3-Chess\"");
    final Process process = processBuilder.start();
    final String output = readStream(Nulls.getInputStream(process)).trim();
    final String error = readStream(Nulls.getErrorStream(process)).trim();
    final int exitCode = process.waitFor();
    if (exitCode != 0) {
      throw new IllegalStateException("Resolving the WSL D3-Chess default path failed with " + exitCode + ": " + error);
    }
    if (output.isBlank()) {
      throw new IllegalStateException("Resolving the WSL D3-Chess default path returned an empty path");
    }
    return output;
  }

  private static void runWslCommand(String command) throws Exception {
    final ProcessBuilder processBuilder = new ProcessBuilder("wsl", "bash", "-lc", command);
    processBuilder.redirectErrorStream(true);
    final Process process = processBuilder.start();
    final String output = readStream(Nulls.getInputStream(process)).trim();
    final int exitCode = process.waitFor();
    if (!output.isEmpty()) {
      logger.info(output);
    }
    if (exitCode != 0) {
      throw new IllegalStateException("WSL command failed with " + exitCode + ": " + output);
    }
  }

  private static String readStream(InputStream inputStream) {
    try (InputStream stream = inputStream) {
      return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    } catch (final IOException ioe) {
      throw new FileSystemAccessException("Reading process output failed", ioe);
    }
  }

  private static String shellQuote(String value) {
    return "'" + value.replace("'", "'\"'\"'") + "'";
  }
}
