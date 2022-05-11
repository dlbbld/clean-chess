clean-chess
===========

clean-chess has the following features:
* Threefold repetition and fifty-moves report
* Detects most dead positions
* Java chess API, including PGN support

The name refers to clean code since the code relies on extensive tests as recommended in clean code.


# Building/Installing
## From source

```
$ git clone git@github.com:dlbbld/clean-chess.git
$ cd clean-chess/
$ mvn clean compile package install
```

## From repository

clean-chess dependency can be added via the jitpack repository.

### Maven

```xml
<repositories>
  ...
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

```xml
<dependency>
  <groupId>com.github.dlbbld</groupId>
  <artifactId>clean-chess</artifactId>
  <version>0.9</version>
</dependency>
```

### Gradle

```groovy
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```

```groovy
dependencies {
    ...
    compile 'com.github.dlbbld:clean-chess:0.9'
    ...
}
```

# Threefold repetition and fifty-moves
## Threefold repetition
The following game contains a threefold repetition according to [Wikipedia](https://en.wikipedia.org/wiki/Threefold_repetition#Capablanca_versus_Lasker,_1921):

```java
  final var pgn = """
      1. d4 d5 2. Nf3 Nf6 3. c4 e6 4. Bg5 Nbd7 5. e3 Be7 6. Nc3 O-O 7. Rc1 b6 8. cxd5
      exd5 9. Qa4 c5 10. Qc6 Rb8 11. Nxd5 Bb7 12. Nxe7+ Qxe7 13. Qa4 Rbc8 14. Qa3 Qe6
      15. Bxf6 Qxf6 16. Ba6 Bxf3 17. Bxc8 Rxc8 18. gxf3 Qxf3 19. Rg1 Re8 20. Qd3 g6
      21. Kf1 Re4 22. Qd1 Qh3+ 23. Rg2 Nf6 24. Kg1 cxd4 25. Rc4 dxe3 26. Rxe4 Nxe4 27.
      Qd8+ Kg7 28. Qd4+ Nf6 29. fxe3 Qe6 30. Rf2 g5 31. h4 gxh4 32. Qxh4 Ng4 33. Qg5+
      Kf8 34. Rf5 h5 35. Qd8+ Kg7 36. Qg5+ Kf8 37. Qd8+ Kg7 38. Qg5+ Kf8 39. b3 Qd6
      40. Qf4 Qd1+ 41. Qf1 Qd7 42. Rxh5 Nxe3 43. Qf3 Qd4 44. Qa8+ Ke7 45. Qb7+ Kf8 46.
      Qb8+ *""";

  Analyzer.printAnalysis(pgn);
```

Output:
```
Repetitions from twofold:
34...h5 (A - 1/3) 35.Qd8+ (B - 1/2) 35...Kg7 (C - 1/2) 36.Qg5+ (D - 1/2) 36...Kf8 (A - 2/3) 37.Qd8+ (B - 2/2) 37...Kg7 (C - 2/2) 38.Qg5+ (D - 2/2) 38...Kf8 (A - 3/3)

Threefold repetition:
Yes

Fivefold repetition:
No

Sequences without capture and pawn move starting from 50 half-moves:
None

Fifty moves without capture and pawn move:
No

Seventy-five moves without capture and pawn move:
No

Result per last position:
Ongoing
```

## Fifty-moves
The following game ends with a series above fifty moves without capture and pawn move according to [Wikipedia](https://en.wikipedia.org/wiki/Fifty-move_rule#Karpov_vs._Kasparov,_1991). The number of halfmoves without capture and pawn moves are indicated in brackets. (1) for series start, (100) - fifty moves reached, (103) - end of series.

```java
  final var pgn = """
      1. d4 Nf6 2. c4 g6 3. Nc3 Bg7 4. e4 d6 5. Nf3 O-O 6. Be2 e5 7. O-O Nc6 8. d5
      Ne7 9. Nd2 a5 10. Rb1 Nd7 11. a3 f5 12. b4 Kh8 13. f3 Ng8 14. Qc2 Ngf6 15. Nb5
      axb4 16. axb4 Nh5 17. g3 Ndf6 18. c5 Bd7 19. Rb3 Nxg3 20. hxg3 Nh5 21. f4 exf4
      22. c6 bxc6 23. dxc6 Nxg3 24. Rxg3 fxg3 25. cxd7 g2 26. Rf3 Qxd7 27. Bb2 fxe4
      28. Rxf8+ Rxf8 29. Bxg7+ Qxg7 30. Qxe4 Qf6 31. Nf3 Qf4 32. Qe7 Rf7 33. Qe6 Rf6
      34. Qe8+ Rf8 35. Qe7 Rf7 36. Qe6 Rf6 37. Qb3 g5 38. Nxc7 g4 39. Nd5 Qc1+ 40.
      Qd1 Qxd1+ 41. Bxd1 Rf5 42. Ne3 Rf4 43. Ne1 Rxb4 44. Bxg4 h5 45. Bf3 d5 46.
      N3xg2 h4 47. Nd3 Ra4 48. Ngf4 Kg7 49. Kg2 Kf6 50. Bxd5 Ra5 51. Bc6 Ra6 52. Bb7
      Ra3 53. Be4 Ra4 54. Bd5 Ra5 55. Bc6 Ra6 56. Bf3 Kg5 57. Bb7 Ra1 58. Bc8 Ra4 59.
      Kf3 Rc4 60. Bd7 Kf6 61. Kg4 Rd4 62. Bc6 Rd8 63. Kxh4 Rg8 64. Be4 Rg1 65. Nh5+
      Ke6 66. Ng3 Kf6 67. Kg4 Ra1 68. Bd5 Ra5 69. Bf3 Ra1 70. Kf4 Ke6 71. Nc5+ Kd6
      72. Nge4+ Ke7 73. Ke5 Rf1 74. Bg4 Rg1 75. Be6 Re1 76. Bc8 Rc1 77. Kd4 Rd1+ 78.
      Nd3 Kf7 79. Ke3 Ra1 80. Kf4 Ke7 81. Nb4 Rc1 82. Nd5+ Kf7 83. Bd7 Rf1+ 84. Ke5
      Ra1 85. Ng5+ Kg6 86. Nf3 Kg7 87. Bg4 Kg6 88. Nf4+ Kg7 89. Nd4 Re1+ 90. Kf5 Rc1
      91. Be2 Re1 92. Bh5 Ra1 93. Nfe6+ Kh6 94. Be8 Ra8 95. Bc6 Ra1 96. Kf6 Kh7 97.
      Ng5+ Kh8 98. Nde6 Ra6 99. Be8 Ra8 100. Bh5 Ra1 101. Bg6 Rf1+ 102. Ke7 Ra1 103.
      Nf7+ Kg8 104. Nh6+ Kh8 105. Nf5 Ra7+ 106. Kf6 Ra1 107. Ne3 Re1 108. Nd5 Rg1
      109. Bf5 Rf1 110. Ndf4 Ra1 111. Ng6+ Kg8 112. Ne7+ Kh8 113. Ng5 Ra6+ 114. Kf7
      Rf6+""";

  Analyzer.printAnalysis(pgn);
```

Output:
```
Repetitions from twofold:
32.Qe7 (A - 1/2) 32...Rf7 (B - 1/2) 33.Qe6 (C - 1/2) 33...Rf6 (D - 1/2) 35.Qe7 (A - 2/2) 35...Rf7 (B - 2/2) 36.Qe6 (C - 2/2) 36...Rf6 (D - 2/2) 50.Bxd5 (E - 1/2) 50...Ra5 (F - 1/2) 51.Bc6 (G - 1/2) 51...Ra6 (H - 1/2) 54.Bd5 (E - 2/2) 54...Ra5 (F - 2/2) 55.Bc6 (G - 2/2) 55...Ra6 (H - 2/2)

Threefold repetition:
No

Fivefold repetition:
No

Sequences without capture and pawn move starting from 50 half-moves:
63...Rg8 (1) 113.Ng5 (100) 114...Rf6+ (103)

Fifty moves without capture and pawn move:
Yes

Seventy-five moves without capture and pawn move:
No

Result per last position:
Ongoing
```
# Dead position
The following is a game ending in a dead position not caused by insufficient material according to [Wikipedia](https://en.wikipedia.org/wiki/Rules_of_chess#Dead_position):

```java
  final Board board = new Board("8/2b1k3/7p/p1p1p1pP/PpP1P1P1/1P1BK3/8/8 b - - 0 50");
  System.out.println(board.isDeadPosition()); // YES
```

The following is a [further example from Lichess](https://lichess.org/r1SzzM60#131). The game can only end in a draw, evaluating the possibilities. The API does also evaluate a few halfmoves.

```java
  final Board board = new Board("2k5/2P5/8/1KN5/8/8/8/8 b - - 0 66");
  System.out.println(board.isDeadPosition()); // YES
```

Attention. The API does not detect all dead positions, but from average games around 99%. The following is an example of a dead position the API does not see, from this [page](https://chasolver.org/analyzer.html?example=3).

```java
  final Board board = new Board("8/1k5B/7b/8/1p1p1p1p/1PpP1P1P/2P3K1/N3b3 b - - 0 50");
  System.out.println(board.isDeadPosition()); // UNKNOWN
```

For more accurate dead position analysis, please visit this [page](https://chasolver.org/index.html).

# Java chess API
## Board
```java
  final Board board = new Board();

  board.performMove("e4"); // specifying the SAN
  board.performMoves("e5", "Bc4"); // specifying multiple SAN's

  final var newMove = new MoveSpecification(Side.BLACK, Square.F8, Square.C5);
  board.performMove(newMove); // move specification without SAN

  board.unperformMove(); // undoes last move

  board.performMoves("Bc5", "Qf3", "h6", "Qxf7#");

  System.out.println(board.isCheckmate()); // true
```
      
## PGN reader
The PGN reader reads comments and move suffix annotation (like "!!") but will throw an exception if variations are encountered.

```java
  final var pgn = """
      [Event "Spassky - Fischer World Championship Match"]
      [Site "Reykjavik ISL"]
      [Date "1972.08.22"]
      [EventDate "?"]
      [Round "17"]
      [Result "1/2-1/2"]
      [White "Boris Spassky"]
      [Black "Robert James Fischer"]
      [ECO "B09"]
      [WhiteElo "?"]
      [BlackElo "?"]
      [PlyCount "89"]

      1. e4 d6 2. d4 g6 3. Nc3 Nf6 4. f4 Bg7 5. Nf3 c5 6. dxc5 Qa5
      7. Bd3 Qxc5 8. Qe2 O-O 9. Be3 Qa5 10. O-O Bg4 11. Rad1 Nc6
      12. Bc4 Nh5 13. Bb3 Bxc3 14. bxc3 Qxc3 15. f5 Nf6 16. h3 Bxf3
      17. Qxf3 Na5 18. Rd3 Qc7 19. Bh6 Nxb3 20. cxb3 Qc5+ 21. Kh1
      Qe5 22. Bxf8 Rxf8 23. Re3 Rc8 24. fxg6 hxg6 25. Qf4 Qxf4
      26. Rxf4 Nd7 27. Rf2 Ne5 28. Kh2 Rc1 29. Ree2 Nc6 30. Rc2 Re1
      31. Rfe2 Ra1 32. Kg3 Kg7 33. Rcd2 Rf1 34. Rf2 Re1 35. Rfe2 Rf1
      36. Re3 a6 37. Rc3 Re1 38. Rc4 Rf1 39. Rdc2 Ra1 40. Rf2 Re1
      41. Rfc2 g5 42. Rc1 Re2 43. R1c2 Re1 44. Rc1 Re2 45. R1c2
      1/2-1/2
      """;

  final PgnFile pgnFile = PgnReader.readPgn(pgn);

  // play through the game
  final Board board = new Board();
  for (final PgnHalfMove halfMove : pgnFile.halfMoveList()) {
    board.performMove(halfMove.san());
  }

  System.out.println(board.getSan()); // SAN of last move, R1c2
```

Reading PGN files from the file system:

```java
  if (FileUtility.exists("C:\\myPgnFolder", "myPgnFile.pgn")) {
    final PgnFile pgnFile = PgnReader.readPgn("C:\\temp\\myPgnFolder", "myPgnFile.pgn");
    System.out.println(PgnCreate.createPgnFileString(pgnFile));
  }
```
      
## PGN creation

You can write a board or an imported PGN. The result will comply with the PGN export format, so will add the required tags, formatting etc. for exported PGN's.

```java
  final Board board = new Board();
  board.performMoves("e4", "e5", "Nf3", "Nf6", "Bc4", "Bc5");

  final PgnFile pgnFile = PgnCreate.createPgnFile(board);
  System.out.println(PgnCreate.createPgnFileString(pgnFile));
```

Output
```
[Event "?"]
[Site "?"]
[Date "2022.05.10"]
[Round "?"]
[White "?"]
[Black "?"]
[Result "*"]

1. e4 e5 2. Nf3 Nf6 3. Bc4 Bc5 *
```
