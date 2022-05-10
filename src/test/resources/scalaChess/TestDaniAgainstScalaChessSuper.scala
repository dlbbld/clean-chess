package chess

import cats.data.Validated
import com.dlb.chess.board.ChessBoard
import com.dlb.chess.common.enums.{SimpleCastlingMove, SimplePieceType, SimpleSide, SimpleSquare}
import com.dlb.chess.common.exceptions.ProgrammingMistakeException
import com.dlb.chess.common.model.MoveSpecification

import java.util
import scala.jdk.CollectionConverters._

trait TestDaniAgainstScalaChessSuper extends ChessTest {

  def testAll(
      simpleBoard: SimpleBoard,
      adaptedUciList: java.util.List[String]
  ): Boolean = {

    val lastMoveAdaptedUci     = adaptedUciList.get(adaptedUciList.size() - 1)
    val sidePlayingTheHalfMove = calculateSimpleSidePlayingTheHalfMove(adaptedUciList.size())
    val lastMoveSpecification =
      convertAdaptedUciToMoveSpecification(sidePlayingTheHalfMove, lastMoveAdaptedUci)

    //play the next move
    simpleBoard.performMove(lastMoveSpecification)

    //test result
    val scalaAdaptedUciList = adaptedUciList.asScala.toList
    val moveList            = convertAdaptedUciListToMoveList(scalaAdaptedUciList)
    testGameResult(
      simpleBoard,
      moveList
    )

    //test legal moves
    testLegalMoves(simpleBoard, moveList)
  }

  def testGameResult(
      simpleBoard: SimpleBoard,
      moveList: Iterable[(Pos, Pos, Option[PromotableRole])]
  ): Boolean = {
    makeGame.playMoveListPromotion(moveList)

    makeGame.playMoveListPromotion(moveList) must beValid.like { case (game) =>
      game.game.situation.check must_== simpleBoard.isCheck()
      game.situation.checkMate must_== simpleBoard.isCheckmate()
      game.situation.staleMate must_== simpleBoard.isStalemate()
      game.board.history.threefoldRepetition must_== simpleBoard.isThreefoldRepetition()
      game.board.history.fivefoldRepetition must_== simpleBoard.isFivefoldRepetition()
      game.board.variant.fiftyMoves(game.board.history) must_== simpleBoard.isFiftyMove()
      game.board.variant.isInsufficientMaterial(game.board) must_== simpleBoard.isInsufficientMaterial()
      game.board.variant.opponentHasInsufficientMaterial(
        game.board.situationOf(Black)
      ) must_== simpleBoard.isInsufficientMaterial(SimpleSide.WHITE)
      game.board.variant.opponentHasInsufficientMaterial(
        game.board.situationOf(White)
      ) must_== simpleBoard.isInsufficientMaterial(SimpleSide.BLACK)
      game.halfMoveClock must_== simpleBoard.getHalfMoveClock()
      game.fullMoveNumber must_== simpleBoard.getFullMoveNumber()
    }
  }

  def testGameResult(
      moveList: Iterable[(Pos, Pos, Option[PromotableRole])],
      isCheck: Boolean,
      isCheckmate: Boolean,
      isStalemate: Boolean,
      isThreefoldRepetition: Boolean,
      isFivefoldRepetition: Boolean,
      isFiftyMoves: Boolean,
      isInsufficientMaterial: Boolean,
      hasInsufficientMaterialOpponentOfWhite: Boolean,
      hasInsufficientMaterialOpponentOfBlack: Boolean,
      halfMoveClock: Int,
      fullMoveNumber: Int
  ): Boolean = {
    makeGame.playMoveListPromotion(moveList)

    makeGame.playMoveListPromotion(moveList) must beValid.like { case (game) =>
      game.game.situation.check must_== isCheck
      game.situation.checkMate must_== isCheckmate
      game.situation.staleMate must_== isStalemate
      game.board.history.threefoldRepetition must_== isThreefoldRepetition
      game.board.history.fivefoldRepetition must_== isFivefoldRepetition
      game.board.variant.fiftyMoves(game.board.history) must_== isFiftyMoves
      game.board.variant.isInsufficientMaterial(game.board) must_== isInsufficientMaterial
      game.board.variant.opponentHasInsufficientMaterial(
        game.board.situationOf(Black)
      ) must_== hasInsufficientMaterialOpponentOfWhite
      game.board.variant.opponentHasInsufficientMaterial(
        game.board.situationOf(White)
      ) must_== hasInsufficientMaterialOpponentOfBlack
      game.halfMoveClock must_== halfMoveClock
      game.fullMoveNumber must_== fullMoveNumber
    }
  }

  def testLegalMoves(
      simpleBoard: SimpleBoard,
      moveList: Iterable[(Pos, Pos, Option[PromotableRole])]
  ): Boolean = {
    val scalaLegalMoveSpecificationSet     = calculateScalaLegalMoveSpecificationSetPromotion(moveList)
    val daniLegalMoveSpecificationSet      = simpleBoard.getPossibleMoveSpecificationSet()
    val daniAdaptLegalMoveSpecificationSet = adaptMoveSpecification(daniLegalMoveSpecificationSet)

    scalaLegalMoveSpecificationSet must_== daniAdaptLegalMoveSpecificationSet
  }

  def testLegalMoves(
      moveList: Iterable[(Pos, Pos, Option[PromotableRole])],
      moveSpecificationList: util.List[MoveSpecification]
  ): Boolean = {
    val scalaLegalMoveSpecificationSet = calculateScalaLegalMoveSpecificationSetPromotion(moveList)
    val daniLegalMoveSpecificationSet  = calculateDaniLegalMoveSpecificationSet(moveSpecificationList)
    scalaLegalMoveSpecificationSet must_== daniLegalMoveSpecificationSet
  }

  def calculateDaniLegalMoveSpecificationSet(
      moveSpecificationList: util.List[MoveSpecification]
  ): util.Set[MoveSpecification] = {
    val board = new SimpleBoard()

    val itMoveSpecificationList = moveSpecificationList.iterator()
    while (itMoveSpecificationList.hasNext()) {
      val moveSpecification = itMoveSpecificationList.next()
      board.performMove(moveSpecification)
    }

    val daniLegalMoveSpecificationSet      = board.getPossibleMoveSpecificationSet()
    val daniAdaptLegalMoveSpecificationSet = adaptMoveSpecification(daniLegalMoveSpecificationSet)
    return daniAdaptLegalMoveSpecificationSet
  }

  def convertAdaptedUciListToMoveSpecificationList(
      adaptedUciList: List[String]
  ): util.List[MoveSpecification] = {
    val result          = new util.ArrayList[MoveSpecification]()
    var halfMoveCounter = 0
    for (adaptedUci <- adaptedUciList) {
      halfMoveCounter += 1
      val sidePlayingTheHalfMove = calculateSimpleSidePlayingTheHalfMove(halfMoveCounter)
      val moveSpecification      = convertAdaptedUciToMoveSpecification(sidePlayingTheHalfMove, adaptedUci)
      result.add(moveSpecification)
    }
    return result
  }

  def convertAdaptedUciListToMoveList(
      adaptedUciList: List[String]
  ): List[(Pos, Pos, Option[PromotableRole])] = {
    val resultJavaList  = new util.ArrayList[(Pos, Pos, Option[PromotableRole])]()
    var halfMoveCounter = 0
    for (adaptedUci <- adaptedUciList) {
      halfMoveCounter += 1
      val sidePlayingTheHalfMove = calculateColorPlayingTheHalfMove(halfMoveCounter)
      val move                   = convertAdaptedUciToMove(sidePlayingTheHalfMove, adaptedUci)
      resultJavaList.add(move)
    }
    return resultJavaList.asScala.toList
  }

  def convertAdaptedUciToMoveSpecification(
      sidePlayingTheHalfMove: SimpleSide,
      adaptedUci: String
  ): MoveSpecification = {

    //castling move?
    if (adaptedUci.equals("O-O")) {
      val moveSpecification = new MoveSpecification(sidePlayingTheHalfMove, SimpleCastlingMove.KING_SIDE)
      return moveSpecification
    }
    if (adaptedUci.equals("O-O-O")) {
      val moveSpecification = new MoveSpecification(sidePlayingTheHalfMove, SimpleCastlingMove.QUEEN_SIDE)
      return moveSpecification
    }

    val fromSquareStr = adaptedUci.substring(0, 2)
    val toSquareStr   = adaptedUci.substring(2, 4)

    val fromSquare = convertPosToSimpleSquare(convertSquareStringToPos(fromSquareStr))
    val toSquare   = convertPosToSimpleSquare(convertSquareStringToPos(toSquareStr))

    //promotion move?
    if (adaptedUci.length() == 5) {
      val promotionPieceLetter = adaptedUci.substring(4, 5)
      val promotionPieceType   = convertPromotionPieceStringStringToSimplePieceType(promotionPieceLetter)
      val moveSpecification =
        new MoveSpecification(sidePlayingTheHalfMove, fromSquare, toSquare, promotionPieceType)
      return moveSpecification
    }

    //no castling and no promotion move
    val moveSpecification = new MoveSpecification(sidePlayingTheHalfMove, fromSquare, toSquare)
    return moveSpecification
  }

  def convertAdaptedUciToMove(
      color: Color,
      adaptedUci: String
  ): (Pos, Pos, Option[PromotableRole]) = {

    //castling move?
    if (adaptedUci.equals("O-O")) {
      color match {
        case Color.White => return (Pos.E1, Pos.G1, None)
        case Color.Black => return (Pos.E8, Pos.G8, None)
      }
    }
    if (adaptedUci.equals("O-O-O")) {
      color match {
        case Color.White => return (Pos.E1, Pos.C1, None)
        case Color.Black => return (Pos.E8, Pos.C8, None)
      }
    }

    val fromSquareStr = adaptedUci.substring(0, 2)
    val toSquareStr   = adaptedUci.substring(2, 4)

    val fromSquare = convertSquareStringToPos(fromSquareStr)
    val toSquare   = convertSquareStringToPos(toSquareStr)

    //promotion move?
    if (adaptedUci.length() == 5) {
      val promotionPieceLetter = adaptedUci.substring(4, 5)
      val promotionPieceType   = convertPromotionPieceStringStringToPromotableRole(promotionPieceLetter)
      return (fromSquare, toSquare, promotionPieceType)
    }

    //no castling and no promotion move
    return (fromSquare, toSquare, None)
  }

  def convertPromotableRoleSimplePiece(promotableRole: Option[PromotableRole]): SimplePieceType = {
    promotableRole match {
      case Some(Rook)   => return SimplePieceType.ROOK
      case Some(Knight) => return SimplePieceType.KNIGHT
      case Some(Bishop) => return SimplePieceType.BISHOP
      case Some(Queen)  => return SimplePieceType.QUEEN
      case _ =>
        throw new ProgrammingMistakeException(
          "Convert scala chess promotion piece to java promotion piece has a programming error"
        )
    }

  }

  def calculateScalaLegalMoveSpecificationSetPromotion(
      moveList: Iterable[(Pos, Pos, Option[PromotableRole])]
  ): util.Set[MoveSpecification] = {
    val myLegalMovesMap                = makeGame.playMoveListPromotion(moveList) map (_.situation.moves)
    val scalaLegalMoveSpecificationSet = calculateScalaLegalMoveSpecificationSet(myLegalMovesMap)
    return scalaLegalMoveSpecificationSet
  }

  def calculateScalaLegalMoveSpecificationSet(moveList: Iterable[(Pos, Pos)]): util.Set[MoveSpecification] = {
    val myLegalMovesMap                = makeGame.playMoveList(moveList) map (_.situation.moves)
    val scalaLegalMoveSpecificationSet = calculateScalaLegalMoveSpecificationSet(myLegalMovesMap)
    return scalaLegalMoveSpecificationSet
  }

  def calculateScalaLegalMoveSpecificationSet(
      myLegalMovesMap: Validated[String, Map[Pos, List[Move]]]
  ): util.Set[MoveSpecification] = {
    val scalaLegalMoveSpecificationSet = new util.TreeSet[MoveSpecification]();
    for (r <- myLegalMovesMap) {
      for ((s, t) <- r) {
        for (u <- t) {

          // castling by dragging king onto rook??? ignore for comparison - not a legal move
          //White
          if (u.castles && u.orig == Pos.E1 && u.dest == Pos.H1) {
            //do nothing
          } else if (u.castles && u.orig == Pos.E1 && u.dest == Pos.A1) {
            //do nothing
          }
          //Black
          else if (u.castles && u.orig == Pos.E8 && u.dest == Pos.H8) {
            //do nothing
          } else if (u.castles && u.orig == Pos.E8 && u.dest == Pos.A8) {
            //do nothing
          } else {
            val scalaMoveSpecification = calculateScalaMoveSpecification(u)
            scalaLegalMoveSpecificationSet.add(scalaMoveSpecification)
          }
        }
      }
    }
    return scalaLegalMoveSpecificationSet
  }

  def calculateScalaMoveSpecification(move: Move): MoveSpecification = {

    val havingMove = convertColorToSimpleSide(move.piece.color)
    val fromSquare = convertPosToSimpleSquare(move.orig)
    val toSquare   = convertPosToSimpleSquare(move.dest)

    //en passant does not require special treatment at move specification level

    //for the comparison for simplification (which is consistent) we treat promotion as move without
    //promotion piece

    return new MoveSpecification(havingMove, fromSquare, toSquare);
  }

  def convertColorToSimpleSide(color: Color): SimpleSide = {
    if (color == Color.White) {
      return SimpleSide.WHITE
    }
    return SimpleSide.BLACK
  }

  def adaptMoveSpecification(moveList: util.Set[MoveSpecification]): util.Set[MoveSpecification] = {
    val adaptedMoveSpecification = new util.TreeSet[MoveSpecification]()
    val itMoveList               = moveList.iterator()
    while (itMoveList.hasNext()) {
      val move = itMoveList.next()
      if (move.getPromotionPieceType() != SimplePieceType.NONE) {
        //for the promotion move we don't create the moves per piece promotion type,
        //we only look at the promotion to the queen to compare with scalachess
        if (move.getPromotionPieceType() == SimplePieceType.QUEEN) {
          //for the promotion move we remove the promotion piece to compare with scalachess
          val promotionMove =
            new MoveSpecification(move.getHavingMove(), move.getFromSquare, move.getToSquare)
          adaptedMoveSpecification.add(promotionMove)
        }
      } else if (move.getCastlingMove() != SimpleCastlingMove.NONE) {
        if (move.getHavingMove() == SimpleSide.WHITE) {
          if (move.getCastlingMove() == SimpleCastlingMove.KING_SIDE) {
            val castlingMove = new MoveSpecification(SimpleSide.WHITE, SimpleSquare.E1, SimpleSquare.G1)
            adaptedMoveSpecification.add(castlingMove)
          }
          if (move.getCastlingMove() == SimpleCastlingMove.QUEEN_SIDE) {
            val castlingMove = new MoveSpecification(SimpleSide.WHITE, SimpleSquare.E1, SimpleSquare.C1)
            adaptedMoveSpecification.add(castlingMove)
          }
        }
        if (move.getHavingMove() == SimpleSide.BLACK) {
          if (move.getCastlingMove() == SimpleCastlingMove.KING_SIDE) {
            val castlingMove = new MoveSpecification(SimpleSide.BLACK, SimpleSquare.E8, SimpleSquare.G8)
            adaptedMoveSpecification.add(castlingMove)
          }
          if (move.getCastlingMove() == SimpleCastlingMove.QUEEN_SIDE) {
            val castlingMove = new MoveSpecification(SimpleSide.BLACK, SimpleSquare.E8, SimpleSquare.C8)
            adaptedMoveSpecification.add(castlingMove)
          }
        }

      } else {
        adaptedMoveSpecification.add(move)
      }
    }
    return adaptedMoveSpecification
  }

  def convertPosToSimpleSquare(pos: Pos): SimpleSquare = {
    if (pos == Pos.A1) {
      return SimpleSquare.A1
    }
    if (pos == Pos.B1) {
      return SimpleSquare.B1
    }
    if (pos == Pos.C1) {
      return SimpleSquare.C1
    }
    if (pos == Pos.D1) {
      return SimpleSquare.D1
    }
    if (pos == Pos.E1) {
      return SimpleSquare.E1
    }
    if (pos == Pos.F1) {
      return SimpleSquare.F1
    }
    if (pos == Pos.G1) {
      return SimpleSquare.G1
    }
    if (pos == Pos.H1) {
      return SimpleSquare.H1
    }
    if (pos == Pos.A2) {
      return SimpleSquare.A2
    }
    if (pos == Pos.B2) {
      return SimpleSquare.B2
    }
    if (pos == Pos.C2) {
      return SimpleSquare.C2
    }
    if (pos == Pos.D2) {
      return SimpleSquare.D2
    }
    if (pos == Pos.E2) {
      return SimpleSquare.E2
    }
    if (pos == Pos.F2) {
      return SimpleSquare.F2
    }
    if (pos == Pos.G2) {
      return SimpleSquare.G2
    }
    if (pos == Pos.H2) {
      return SimpleSquare.H2
    }
    if (pos == Pos.A3) {
      return SimpleSquare.A3
    }
    if (pos == Pos.B3) {
      return SimpleSquare.B3
    }
    if (pos == Pos.C3) {
      return SimpleSquare.C3
    }
    if (pos == Pos.D3) {
      return SimpleSquare.D3
    }
    if (pos == Pos.E3) {
      return SimpleSquare.E3
    }
    if (pos == Pos.F3) {
      return SimpleSquare.F3
    }
    if (pos == Pos.G3) {
      return SimpleSquare.G3
    }
    if (pos == Pos.H3) {
      return SimpleSquare.H3
    }
    if (pos == Pos.A4) {
      return SimpleSquare.A4
    }
    if (pos == Pos.B4) {
      return SimpleSquare.B4
    }
    if (pos == Pos.C4) {
      return SimpleSquare.C4
    }
    if (pos == Pos.D4) {
      return SimpleSquare.D4
    }
    if (pos == Pos.E4) {
      return SimpleSquare.E4
    }
    if (pos == Pos.F4) {
      return SimpleSquare.F4
    }
    if (pos == Pos.G4) {
      return SimpleSquare.G4
    }
    if (pos == Pos.H4) {
      return SimpleSquare.H4
    }
    if (pos == Pos.A5) {
      return SimpleSquare.A5
    }
    if (pos == Pos.B5) {
      return SimpleSquare.B5
    }
    if (pos == Pos.C5) {
      return SimpleSquare.C5
    }
    if (pos == Pos.D5) {
      return SimpleSquare.D5
    }
    if (pos == Pos.E5) {
      return SimpleSquare.E5
    }
    if (pos == Pos.F5) {
      return SimpleSquare.F5
    }
    if (pos == Pos.G5) {
      return SimpleSquare.G5
    }
    if (pos == Pos.H5) {
      return SimpleSquare.H5
    }
    if (pos == Pos.A6) {
      return SimpleSquare.A6
    }
    if (pos == Pos.B6) {
      return SimpleSquare.B6
    }
    if (pos == Pos.C6) {
      return SimpleSquare.C6
    }
    if (pos == Pos.D6) {
      return SimpleSquare.D6
    }
    if (pos == Pos.E6) {
      return SimpleSquare.E6
    }
    if (pos == Pos.F6) {
      return SimpleSquare.F6
    }
    if (pos == Pos.G6) {
      return SimpleSquare.G6
    }
    if (pos == Pos.H6) {
      return SimpleSquare.H6
    }
    if (pos == Pos.A7) {
      return SimpleSquare.A7
    }
    if (pos == Pos.B7) {
      return SimpleSquare.B7
    }
    if (pos == Pos.C7) {
      return SimpleSquare.C7
    }
    if (pos == Pos.D7) {
      return SimpleSquare.D7
    }
    if (pos == Pos.E7) {
      return SimpleSquare.E7
    }
    if (pos == Pos.F7) {
      return SimpleSquare.F7
    }
    if (pos == Pos.G7) {
      return SimpleSquare.G7
    }
    if (pos == Pos.H7) {
      return SimpleSquare.H7
    }
    if (pos == Pos.A8) {
      return SimpleSquare.A8
    }
    if (pos == Pos.B8) {
      return SimpleSquare.B8
    }
    if (pos == Pos.C8) {
      return SimpleSquare.C8
    }
    if (pos == Pos.D8) {
      return SimpleSquare.D8
    }
    if (pos == Pos.E8) {
      return SimpleSquare.E8
    }
    if (pos == Pos.F8) {
      return SimpleSquare.F8
    }
    if (pos == Pos.G8) {
      return SimpleSquare.G8
    }
    if (pos == Pos.H8) {
      return SimpleSquare.H8
    }

    //should not  reach here
    assert(1 == 2)
    return SimpleSquare.NONE
  }

  def convertSquareStringToPos(squareString: String): Pos = {
    squareString match {
      case "a1" => return Pos.A1
      case "b1" => return Pos.B1
      case "c1" => return Pos.C1
      case "d1" => return Pos.D1
      case "e1" => return Pos.E1
      case "f1" => return Pos.F1
      case "g1" => return Pos.G1
      case "h1" => return Pos.H1
      case "a2" => return Pos.A2
      case "b2" => return Pos.B2
      case "c2" => return Pos.C2
      case "d2" => return Pos.D2
      case "e2" => return Pos.E2
      case "f2" => return Pos.F2
      case "g2" => return Pos.G2
      case "h2" => return Pos.H2
      case "a3" => return Pos.A3
      case "b3" => return Pos.B3
      case "c3" => return Pos.C3
      case "d3" => return Pos.D3
      case "e3" => return Pos.E3
      case "f3" => return Pos.F3
      case "g3" => return Pos.G3
      case "h3" => return Pos.H3
      case "a4" => return Pos.A4
      case "b4" => return Pos.B4
      case "c4" => return Pos.C4
      case "d4" => return Pos.D4
      case "e4" => return Pos.E4
      case "f4" => return Pos.F4
      case "g4" => return Pos.G4
      case "h4" => return Pos.H4
      case "a5" => return Pos.A5
      case "b5" => return Pos.B5
      case "c5" => return Pos.C5
      case "d5" => return Pos.D5
      case "e5" => return Pos.E5
      case "f5" => return Pos.F5
      case "g5" => return Pos.G5
      case "h5" => return Pos.H5
      case "a6" => return Pos.A6
      case "b6" => return Pos.B6
      case "c6" => return Pos.C6
      case "d6" => return Pos.D6
      case "e6" => return Pos.E6
      case "f6" => return Pos.F6
      case "g6" => return Pos.G6
      case "h6" => return Pos.H6
      case "a7" => return Pos.A7
      case "b7" => return Pos.B7
      case "c7" => return Pos.C7
      case "d7" => return Pos.D7
      case "e7" => return Pos.E7
      case "f7" => return Pos.F7
      case "g7" => return Pos.G7
      case "h7" => return Pos.H7
      case "a8" => return Pos.A8
      case "b8" => return Pos.B8
      case "c8" => return Pos.C8
      case "d8" => return Pos.D8
      case "e8" => return Pos.E8
      case "f8" => return Pos.F8
      case "g8" => return Pos.G8
      case "h8" => return Pos.H8
      case _ =>
        throw new IllegalArgumentException(
          "Non square string passed"
        )

    }

  }

  def convertPromotionPieceStringStringToSimplePieceType(promotionPiece: String): SimplePieceType = {
    promotionPiece match {
      case "r" => return SimplePieceType.ROOK
      case "n" => return SimplePieceType.KNIGHT
      case "b" => return SimplePieceType.BISHOP
      case "q" => return SimplePieceType.QUEEN
      case _ =>
        throw new IllegalArgumentException(
          "Non promotion piece string passed"
        )
    }

  }

  def convertPromotionPieceStringStringToPromotableRole(promotionPiece: String): Option[PromotableRole] = {
    promotionPiece match {
      case "r" => return Option(Rook)
      case "n" => return Option(Knight)
      case "b" => return Option(Bishop)
      case "q" => return Option(Queen)
      case _   => throw new IllegalArgumentException("Non promotion piece string passed")
    }
  }

  def calculateSimpleSidePlayingTheHalfMove(halfMoveNumberToPlay: Int): SimpleSide = {
    val color = calculateColorPlayingTheHalfMove(halfMoveNumberToPlay)
    return convertColorToSimpleSide(color)

  }

  def calculateColorPlayingTheHalfMove(halfMoveNumberToPlay: Int): Color = {
    val isEven = (halfMoveNumberToPlay % 2) == 0
    if (isEven) {
      return Black
    }
    return White
  }

}
