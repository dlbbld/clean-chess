clean-chess
===========

clean-chess has the following features:
* Threefold repetition and fifty-moves report
* Implementation of unwinnability and dead position according to the [Chess Unwinnability Analyzer (CHA)](https://github.com/miguel-ambrona/D3-Chess)
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
## Threefold repetition claim ahead
The following game ended with a threefold repetition claim ahead according to [Wikipedia](https://en.wikipedia.org/wiki/Threefold_repetition#Portisch_versus_Korchnoi,_1970):

```java
  final var pgn = """
      1. Nf3 c5 2. c4 Nf6 3. Nc3 Nc6 4. d4 cxd4 5. Nxd4 e6 6. g3 Qb6 7. Nb3 Ne5 8. e4
      Bb4 9. Qe2 O-O 10. f4 Nc6 11. e5 Ne8 12. Bd2 f6 13. c5 Qd8 14. a3 Bxc3 15. Bxc3
      fxe5 16. Bxe5 b6 17. Bg2 Nxe5 18. Bxa8 Nf7 19. Bg2 bxc5 20. Nxc5 Qb6 21. Qf2
      Qb5 22. Bf1 Qc6 23. Bg2 Qb5 24. Bf1 Qc6 25. Bg2""";

  Analyzer.printAnalysis(pgn);
```

The report mentions the possible claim ahead:
```
Valid threefold claims ahead:
25... Qb5

Threefolds and beyond:
None

Sequences without capture and pawn move starting from 25 moves:
None

Fifty moves without capture and pawn move:
No
```
Black could have claimed a threefold on the 25th move with writing (but not playing) 25... Qb5. Possible claims for White are listed with the move number
followed by dot (for example 20. Ra2). Possible claims for Black are listed with the move number followed by three dots (for example 25... Qb5).

## Threefold repetition on the board
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
Valid threefold claims ahead:
38... Kf8
39. Qd8+

Threefolds and beyond:
34...h5 (A - 1/3) 36...Kf8 (A - 2/3) 38...Kf8 (A - 3/3)

Sequences without capture and pawn move starting from 25 moves:
None

Fifty moves without capture and pawn move:
No
```
The repetitions are indicated in order as occured on the board. As different positions can repeat, the positions are identified as A, B, C ... in order of first occurrence.

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
Valid threefold claims ahead:
None

Threefolds and beyond:
None

Sequences without capture and pawn move starting from 25 moves:
63...Rg8 (0.5) 113.Ng5 (50) 114...Rf6+ (51.5)

Fifty moves without capture and pawn move:
Yes
```
The numbers in parantheses are the number of fullmoves. So "0.5" is one halfmove, "50" are 100 halfmoves and "51.5" are 103 halfmoves.
The halfmove series always indicates the first halfmove with (0.5), fifty halfmoves with (50), seventy-five halfmoves if reached as (75) 
and the last halfmove in the series.

# Unwinnability and dead position
A position is said to be unwinnable for a player if he has no theoretical mating possibilities, assuming worst play of the opponent.
If the position is unwinnable for both players, it's a dead position.

## Methods
The API provides an implementation of CHA. So for both situations there is a quick and a full method.

The quick method is very fast but not 100% accurate. The full method is 100% accurate but it can also happens that it does not terminate in practical time (after hours).

As such the quick method is suited for game use, the full method only for research or interest use.

### Unwinnability
The quick method has three return values:
* UNWINNABLE - the position is not winnable by the player
* WINNABLE - the position is winnable by the player
* POSSIBLY_WINNABLE - the position is most likely winnable by the player, but it might also be unwinnable in some rare cases

The full method has two return values:
* UNWINNABLE - the position is not winnable by the player
* WINNABLE - the position is winnable by the player

### Dead position
The quick method has three return values:
* DEAD_POSITION - the position is a dead position
* NON_DEAD_POSITION - the position is not a dead position
* POSSIBLY_NON_DEAD_POSITION - the position is most likely a non dead position, but it might also be a dead position in some rare cases

The full method has two return values:
* DEAD_POSITION - the position is a dead position
* NON_DEAD_POSITION - the position is not a dead position

## Examples

### Unwinnable

#### Insufficient material
The most common situations of unwinnable situations are if one side has insufficient material.
These are treated correctly by all common chess API's.
For example White flags with the king and rook against the lone king of Black. Black cannot potentially mate with the king alone.
[Position](https://lichess.org/analysis/8/8/4k3/3R4/2K5/8/8/8_w_-_-_0_50)

```java
  final Board board = new Board("8/8/4k3/3R4/2K5/8/8/8 w - - 0 50");
  System.out.println(board.isUnwinnableQuick(Side.BLACK)); // UNWINNABLE
  System.out.println(board.isUnwinnableFull(Side.BLACK)); // UNWINNABLE
```

#### Pawn walls
In pawn walls both players cannot mate, so they are also dead positions. They are not detected
by most common chess API's. 
[Game](https://lichess.org/c3ew66ZV#123)

```java
  final Board board = new Board("8/8/3k4/1p2p1p1/pP1pP1P1/P2P4/1K6/8 b - - 32 62");
  System.out.println(board.isUnwinnableQuick(Side.BLACK)); // UNWINNABLE
  System.out.println(board.isUnwinnableFull(Side.BLACK)); // UNWINNABLE
```

#### Forced moves
There are common situations mainly in lower time controls like Bullet, where the game could only continue with a few
forced moves, and the game outcome is determined. Here Black flags, but there is no game continuation possible where
White could have won.
[Game](https://lichess.org/OawUhnkq#101)

```java
  final Board board = new Board("5r1k/6P1/7K/5q2/8/8/8/8 b - - 0 51");
  System.out.println(board.isUnwinnableQuick(Side.WHITE)); // UNWINNABLE
  System.out.println(board.isUnwinnableFull(Side.WHITE)); // UNWINNABLE
```

#### Common positions
When there are still a lot of pieces on the board, so a mate is very likely, the quick algorithm says POSSIBLE_WINNABLE.
It makes an educated guess only. The full algorithm calculates an actual mate and is therefore accurate but much slower.
[Game](https://lichess.org/SCKpvJQX#57)

```java
  final Board board = new Board("q4r2/pR3pkp/1p2p1p1/4P3/6P1/1P3Q2/1Pr2PK1/3R4 b - - 3 29");
  System.out.println(board.isUnwinnableQuick(Side.WHITE)); // POSSIBLY_WINNABLE
  System.out.println(board.isUnwinnableFull(Side.WHITE)); // WINNABLE
```

#### Positions the quick algorithm does not see
The following is an example of a position where the quick algorithm says POSSIBLY_WINNABLE but the 
position is actually winnable.

[Game](https://lichess.org/bKHPqNEw#81)

```java
  final Board board = new Board("1k6/1P5p/BP3p2/1P6/8/8/5PKP/8 b - - 0 41");
  System.out.println(board.isUnwinnableQuick(Side.WHITE)); // POSSIBLY_WINNABLE
  System.out.println(board.isUnwinnableFull(Side.WHITE)); // UNWINNABLE
```

### Dead positions
Because dead positions are just unwinnable positions for both sides, there is not much more substantially to say.

#### Insufficient material
The simplest dead position is when one player has already insufficient material and the other player
also becomes insufficient material due to a capture. Of course that is detected by all chess API's.

[Position](https://lichess.org/analysis/8/8/3kn3/8/2K5/8/8/8_w_-_-_0_50)
```java
  final Board board = new Board("8/8/3kn3/8/2K5/8/8/8 w - - 0 50");
  System.out.println(board.isDeadPositionQuick()); // DEAD_POSITION
  System.out.println(board.isDeadPositionFull()); // DEAD_POSITION
```

#### Pawn walls
Pawn walls are dead positions, here another example.
[Game](https://lichess.org/V08kX4kz#121)

```java
  final Board board = new Board("8/6b1/1p3k2/1Pp1p1p1/2P1PpP1/5P2/8/5K2 b - - 11 61");
  System.out.println(board.isDeadPositionQuick()); // DEAD_POSITION
  System.out.println(board.isDeadPositionFull()); // DEAD_POSITION
```

#### Forced moves
Positions can also often be dead due to forced moves.
[Game](https://lichess.org/8FUSHxUV#115)

```java
  final Board board = new Board("k7/P1K5/8/8/8/8/8/8 b - - 2 58");
  System.out.println(board.isDeadPositionQuick()); // DEAD_POSITION
  System.out.println(board.isDeadPositionFull()); // DEAD_POSITION
```

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
The PGN reader supports any PGN's except PGN's with move variations.

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
