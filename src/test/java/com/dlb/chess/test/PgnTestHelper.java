package com.dlb.chess.test;

public class PgnTestHelper {

  /** Minimal seven-tag-roster header ending with the blank line that separates tags from movetext. */
  public static String header(String result) {
    return """
        [Event "?"]
        [Site "?"]
        [Date "?"]
        [Round "?"]
        [White "?"]
        [Black "?"]
        [Result \"""" + result + "\"]\n\n";
  }
}
