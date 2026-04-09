package com.dlb.chess.trainer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.san.MoveToSan;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.SanValidation;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class SanTrainerServer {

  private static ApiBoard board = new Board();
  private static final Random RANDOM = new Random();
  private static final Path HTML_PATH = Path.of("C:/Users/danie/claude/index.html");

  public static void main(String[] args) throws IOException {
    final int port = 8080;
    final HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

    server.createContext("/", SanTrainerServer::serveIndex);
    server.createContext("/api/move", SanTrainerServer::handleMove);
    server.createContext("/api/black-random", SanTrainerServer::handleBlackRandom);
    server.createContext("/api/reset", SanTrainerServer::handleReset);

    server.setExecutor(null);
    server.start();
    System.out.println("SAN Trainer running at http://localhost:" + port);
  }

  private static void serveIndex(HttpExchange exchange) throws IOException {
    if (!"GET".equals(exchange.getRequestMethod())) {
      exchange.sendResponseHeaders(405, -1);
      return;
    }
    final byte[] html = Files.readAllBytes(HTML_PATH);
    exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
    exchange.sendResponseHeaders(200, html.length);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(html);
    }
  }

  private static void handleMove(HttpExchange exchange) throws IOException {
    if (!"POST".equals(exchange.getRequestMethod())) {
      exchange.sendResponseHeaders(405, -1);
      return;
    }

    final String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    final String san = extractJsonValue(body, "san");

    String json;
    try {
      final MoveSpecification moveSpec = SanValidation.validateSan(san, board);
      board.performMove(moveSpec);

      final String fen = board.getFen();
      final String from = moveSpec.fromSquare().getName();
      final String to = moveSpec.toSquare().getName();

      final boolean gameOver = board.isCheckmate() || board.isStalemate() || board.getLegalMoveSet().isEmpty();
      String gameOverMsg = "";
      if (board.isCheckmate()) {
        gameOverMsg = "Checkmate! White wins!";
      } else if (board.isStalemate()) {
        gameOverMsg = "Stalemate! Draw.";
      }

      json = "{" + jsonPair("whiteSan", san) + "," + jsonPair("fenAfterWhite", fen) + ","
          + jsonPair("whiteFrom", from) + "," + jsonPair("whiteTo", to) + ","
          + jsonBool("gameOver", gameOver) + "," + jsonPair("gameOverMessage", gameOverMsg) + "}";

    } catch (final SanValidationException e) {
      final SanValidationProblem problem = e.getSanValidationProblem();
      json = "{" + jsonPair("error", e.getMessage()) + "," + jsonPair("problem", problem.name()) + "}";
    } catch (final Exception e) {
      json = "{" + jsonPair("error", "Error: " + e.getMessage()) + "}";
    }

    sendJson(exchange, json);
  }

  private static void handleBlackRandom(HttpExchange exchange) throws IOException {
    if (!"POST".equals(exchange.getRequestMethod())) {
      exchange.sendResponseHeaders(405, -1);
      return;
    }

    final Set<LegalMove> legalMovesBeforeBlack = board.getLegalMoveSet();
    final List<LegalMove> moveList = new ArrayList<>(legalMovesBeforeBlack);
    final LegalMove randomMove = moveList.get(RANDOM.nextInt(moveList.size()));

    board.performMove(randomMove.moveSpecification());
    final String blackSan = MoveToSan.calculateSanLastMove(randomMove, legalMovesBeforeBlack, board.isCheckmate(),
        board.isCheck());

    final String fen = board.getFen();
    final String from = randomMove.moveSpecification().fromSquare().getName();
    final String to = randomMove.moveSpecification().toSquare().getName();

    final boolean gameOver = board.isCheckmate() || board.isStalemate() || board.getLegalMoveSet().isEmpty();
    String gameOverMsg = "";
    if (board.isCheckmate()) {
      gameOverMsg = "Checkmate! Black wins!";
    } else if (board.isStalemate()) {
      gameOverMsg = "Stalemate! Draw.";
    }

    final String json = "{" + jsonPair("blackSan", blackSan) + "," + jsonPair("fenAfterBlack", fen) + ","
        + jsonPair("blackFrom", from) + "," + jsonPair("blackTo", to) + ","
        + jsonBool("gameOver", gameOver) + "," + jsonPair("gameOverMessage", gameOverMsg) + "}";

    sendJson(exchange, json);
  }

  private static void handleReset(HttpExchange exchange) throws IOException {
    board = new Board();
    final String json = "{" + jsonPair("fen", board.getFen()) + "}";
    sendJson(exchange, json);
  }

  private static String extractJsonValue(String json, String key) {
    final String search = "\"" + key + "\"";
    int idx = json.indexOf(search);
    if (idx < 0) {
      return "";
    }
    idx = json.indexOf(':', idx);
    if (idx < 0) {
      return "";
    }
    idx = json.indexOf('"', idx);
    if (idx < 0) {
      return "";
    }
    final int end = json.indexOf('"', idx + 1);
    if (end < 0) {
      return "";
    }
    return json.substring(idx + 1, end);
  }

  private static String jsonPair(String key, String value) {
    return "\"" + key + "\":\"" + escapeJson(value) + "\"";
  }

  private static String jsonBool(String key, boolean value) {
    return "\"" + key + "\":" + value;
  }

  private static String escapeJson(String s) {
    return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
  }

  private static void sendJson(HttpExchange exchange, String json) throws IOException {
    final byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
    exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
    exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
    exchange.sendResponseHeaders(200, bytes.length);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(bytes);
    }
  }
}
