package com.dlb.chess.test.model;

import java.util.List;

import com.dlb.chess.test.pgntest.enums.PgnTest;

public record PgnFileTestCaseList(PgnTest pgnTest, List<PgnFileTestCase> list) {

  public PgnTest pgnTest() {
    return pgnTest;
  }

  public List<PgnFileTestCase> list() {
    return list;
  }
}
