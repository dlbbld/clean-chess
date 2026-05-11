package com.dlb.chess.test.librarycarlos.board;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.CastlingRightLoss;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.constants.DynamicPositionConstants;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ChessBoard;
import com.dlb.chess.common.model.DynamicPosition;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.utility.HalfMoveUtility;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.EnPassantCaptureUtility;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.SanSymbol;
import com.dlb.chess.san.SanTerminalMarker;
import com.dlb.chess.test.librarycarlos.NonNullWrapperLibraryCarlos;
import com.dlb.chess.test.librarycarlos.utility.MoveConversionUtility;
import com.dlb.chess.test.librarycomparison.utility.BoardConversionUtitlity;
import com.dlb.chess.test.librarycomparison.utility.EnumConversionUtility;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.CastleRight;
import com.github.bhlangonijr.chesslib.MoveBackup;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveConversionException;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class LibraryCarlosBoard implements ChessBoard {

  private final Board board = new Board();

  private int performedHalfMoveCount;
  private final List<LegalMove> performedLegalMoveList;
  private final List<DynamicPosition> dynamicPositionList;
  private final List<HalfMove> halfMoveList;

  public LibraryCarlosBoard() {

    performedHalfMoveCount = 0;
    performedLegalMoveList = new ArrayList<>();
    dynamicPositionList = new ArrayList<>();
    dynamicPositionList.add(DynamicPositionConstants.INITIAL);
    halfMoveList = new ArrayList<>();

  }

  @Override
  public boolean move(MoveSpecification moveSpecification) {
    final Side havingMove = getHavingMove();
    final var result = board.doMove(MoveConversionUtility.convertMoveSpecification(havingMove, moveSpecification));
    populateMoveHistory(moveSpecification);
    return result;
  }

  @Override
  public com.dlb.chess.san.StrictSanParserValidationResult moveStrict(String san) {
    board.doMove(san);
    final MoveSpecification lastMoveSpecification = calculateLastMoveSpecification();
    populateMoveHistory(lastMoveSpecification);
    return new com.dlb.chess.san.StrictSanParserValidationResult(lastMoveSpecification);
  }

  @Override
  public com.dlb.chess.san.LenientSanParserValidationResult moveLenient(String san) {
    // Carlos's chesslib doesn't have a lenient SAN concept; delegate to strict, then wrap into the lenient result
    // shape with empty forgiven items. Cross-validation tests only need the move to land on the board.
    final com.dlb.chess.san.StrictSanParserValidationResult strict = moveStrict(san);
    return new com.dlb.chess.san.LenientSanParserValidationResult(strict.moveSpecification(),
        com.dlb.chess.san.ForgivenItem.EMPTY_LIST);
  }

  private MoveSpecification calculateLastMoveSpecification() {
    final var moveBackup = board.getBackup().getLast();
    @SuppressWarnings("null") @NonNull final Move move = moveBackup.getMove();
    final var havingMove = NonNullWrapperLibraryCarlos.getSideToMove(moveBackup);
    final com.github.bhlangonijr.chesslib.Square fromSquare = NonNullWrapperLibraryCarlos.getFrom(move);
    com.github.bhlangonijr.chesslib.Piece movingPiece;
    if (moveBackup.isCastleMove()) {
      movingPiece = switch (havingMove) {
        case WHITE -> com.github.bhlangonijr.chesslib.Piece.WHITE_KING;
        case BLACK -> com.github.bhlangonijr.chesslib.Piece.BLACK_KING;
        default -> throw new IllegalArgumentException();
      };
    } else {
      movingPiece = NonNullWrapperLibraryCarlos.getPiece(this.board, fromSquare);
    }
    return MoveConversionUtility.convertMove(move, movingPiece);
  }

  private void populateMoveHistory(MoveSpecification moveSpecification) {
    performedHalfMoveCount++;

    final MoveBackup moveBackup = NonNullWrapperLibraryCarlos.getLast(this.board);
    final LegalMove legalMove = calculateLegalMove(moveSpecification, moveBackup);
    performedLegalMoveList.add(legalMove);
    dynamicPositionList.add(new DynamicPosition(getHavingMove(), getStaticPosition(), isEnPassantCapturePossible(),
        getCastlingRightWhite(), getCastlingRightBlack()));

    // TODO timely dependency, must be after the above code is very very dangerous
    final HalfMove halfMove = HalfMoveUtility.calculateHalfMove(moveSpecification, this);
    halfMoveList.add(halfMove);
  }

  @Override
  public void unmove() {
    board.undoMove();

    performedHalfMoveCount--;
    performedLegalMoveList.remove(performedLegalMoveList.size() - 1);
    dynamicPositionList.remove(dynamicPositionList.size() - 1);
    halfMoveList.remove(halfMoveList.size() - 1);
  }

  @Override
  public boolean canClaimFiftyMoveRuleWithOwnMove() {
    final var halfMoveClock = getHalfMoveClock();
    if (halfMoveClock == 99) {
      final List<Move> legalMoveList = LibraryCarlosImplementationUtility.generateLegalMoves(this.board);
      // need to check if there is a legal move which has half move clock 100
      for (final Move legalMove : legalMoveList) {
        board.doMove(legalMove);
        final var halfMoveClockAfterNextHalfMove = getHalfMoveClock();
        if (halfMoveClockAfterNextHalfMove == 100 && !board.isMated() && !board.isStaleMate()) {
          board.undoMove();
          return true;
        }
        board.undoMove();
      }
    }
    return false;
  }

  @Override
  public boolean canClaimThreefoldRepetitionRuleWithOwnMove() {
    for (final MoveSpecification moveSpecification : getPossibleMoveSpecificationSet()) {
      move(moveSpecification);
      if (isThreefoldRepetition()) {
        unmove();
        return true;
      }
      unmove();
    }
    return false;
  }

  @Override
  public boolean isCheck() {
    return board.isKingAttacked();
  }

  @Override
  public boolean isCheckmate() {
    return board.isMated();
  }

  @Override
  public boolean isStalemate() {
    return board.isStaleMate();
  }

  @Override
  public int getHalfMoveClock() {
    return board.getHalfMoveCounter();
  }

  @SuppressWarnings("null")
  @Override
  public int getRepetitionCount() {
    var rep = 1;
    final List<Long> history = board.getHistory();
    final var historySize = board.getHistory().size();
    final long lastKey = history.get(historySize - 1);
    for (var i = 0; i <= historySize - 2; i++) {
      final long currentKey = history.get(i);
      if (currentKey == lastKey) {
        rep++;
      }
    }
    return rep;
  }

  @Override
  public boolean isInsufficientMaterial() {
    return LibraryCarlosImplementationUtility.calculateIsInsufficientMaterial(this.board);
  }

  @Override
  public boolean isInsufficientMaterial(Side side) {
    return LibraryCarlosImplementationUtility.calculateIsInsufficientMaterial(side, this.board);
  }

  @Override
  public String getFen() {
    return NonNullWrapperLibraryCarlos.getFen(this.board);
  }

  @Override
  public Fen getInitialFen() {
    // always using initial position, starting from FEN is not supported
    return FenConstants.FEN_INITIAL;
  }

  @Override
  public String getSan() {
    if (board.getBackup().isEmpty()) {
      throw new IllegalStateException("There is no last move");
    }

    final MoveBackup lastMoveBackup = NonNullWrapperLibraryCarlos.getLast(board);
    final var sanTest = lastMoveBackup.getMove().getSan();
    if (sanTest != null) {
      return sanTest;
    }

    final MoveList moveList = new MoveList();
    moveList.addAll(calculateMoveList(this.board));
    try {
      final var sanArray = moveList.toSanArray();
      @SuppressWarnings("null") final var last = NonNullWrapperCommon.getLast(sanArray);
      return last;
    } catch (final MoveConversionException e) {
      throw new RuntimeException("San generation in Carlos's API failed", e);
    }
  }

  private static List<Move> calculateMoveList(Board board) {
    final List<Move> result = new ArrayList<>();
    for (final MoveBackup moveBackup : NonNullWrapperLibraryCarlos.getBackup(board)) {
      result.add(NonNullWrapperLibraryCarlos.getMove(moveBackup));
    }
    return result;
  }

  @Override
  public String getLan() {
    if (board.getBackup().isEmpty()) {
      throw new IllegalStateException("There is no last move");
    }
    final MoveBackup moveBackup = NonNullWrapperLibraryCarlos.getLast(this.board);
    final Move move = NonNullWrapperLibraryCarlos.getMove(moveBackup);
    final com.github.bhlangonijr.chesslib.Piece movingPiece = NonNullWrapperLibraryCarlos.getMovingPiece(moveBackup);

    if ((movingPiece == com.github.bhlangonijr.chesslib.Piece.WHITE_KING
        || movingPiece == com.github.bhlangonijr.chesslib.Piece.BLACK_KING) && board.getContext().isCastleMove(move)) {
      if (board.getContext().isKingSideCastle(move)) {
        return "O-O";
      }
      if (board.getContext().isQueenSideCastle(move)) {
        return "O-O-O";
      }
      throw new ProgrammingMistakeException(
          "There must be a programming mistake in the API, as castling is either kingside or queenside");
    }
    final StringBuilder lan = new StringBuilder();
    // need to workaround a bug that after promotion the piece move is given as promoted piece
    if (!calculateIsPawnMove(moveBackup)) {
      final var movingPieceFenSymbol = movingPiece.getFenSymbol();
      final var movingPieceSymbol = movingPieceFenSymbol.toUpperCase();
      lan.append(movingPieceSymbol);
    }
    lan.append(move.getFrom().toString().toLowerCase());
    if (isCapture()) {
      lan.append(SanSymbol.CAPTURE.getSymbol());
    }
    lan.append(move.getTo().toString().toLowerCase());
    if (calculateIsPromotion(moveBackup)) {
      lan.append(SanSymbol.PROMOTION.getSymbol());
      final var promotionPiece = move.getPromotion();
      final var promotionPieceFenSymbol = promotionPiece.getFenSymbol();
      final var promotionPieceSymbol = promotionPieceFenSymbol.toUpperCase();
      lan.append(promotionPieceSymbol);
    }

    final SanTerminalMarker sanTerminalMarker = AbstractSan.calculateSanTerminalMarker(isCheck(), isCheckmate());
    AbstractSan.appendSanTerminalMarker(lan, sanTerminalMarker);

    return NonNullWrapperCommon.toString(lan);
  }

  @Override
  public Piece getMovingPiece() {
    if (board.getBackup().isEmpty()) {
      throw new IllegalStateException("There is no last move");
    }
    final MoveBackup moveBackup = NonNullWrapperLibraryCarlos.getLast(this.board);
    if (moveBackup.isCastleMove()) {
      return Piece.NONE;
    }
    final com.github.bhlangonijr.chesslib.Piece movingPiece = NonNullWrapperLibraryCarlos.getMovingPiece(moveBackup);
    return EnumConversionUtility.convertToMyPiece(movingPiece);
  }

  @Override
  public boolean isCapture() {
    if (board.getBackup().isEmpty()) {
      throw new IllegalStateException("There is no last move");
    }
    final MoveBackup moveBackup = NonNullWrapperLibraryCarlos.getLast(this.board);
    return moveBackup.getCapturedPiece() != com.github.bhlangonijr.chesslib.Piece.NONE;
  }

  @Override
  public int getInitialFenFullMoveNumber() {
    // currently playing from FEN not supported
    return 1;
  }

  @Override
  public int getFullMoveNumber() {
    if (board.getBackup().isEmpty()) {
      throw new IllegalStateException("There is no last move");
    }
    final MoveBackup moveBackup = NonNullWrapperLibraryCarlos.getLast(this.board);
    return moveBackup.getMoveCounter();
  }

  @Override
  public boolean isFiftyMove() {
    return getHalfMoveClock() >= ChessConstants.FIFTY_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD;
  }

  @Override
  public boolean isThreefoldRepetition() {
    return board.isRepetition();
  }

  @Override
  public boolean isSeventyFiveMove() {
    return getHalfMoveClock() >= ChessConstants.SEVENTY_FIVE_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD;
  }

  @Override
  public boolean isFivefoldRepetition() {
    return board.isRepetition(5);
  }

  @Override
  public Side getHavingMove() {
    return EnumConversionUtility.convertToMySide(NonNullWrapperLibraryCarlos.getSideToMove(this.board));
  }

  @Override
  public StaticPosition getStaticPosition() {
    return BoardConversionUtitlity.convertBoardToStaticPosition(this.board);
  }

  @Override
  public boolean isEnPassantCapturePossible() {
    return LibraryCarlosImplementationUtility.calculateIsEnPassantCapturePossible(this.board);
  }

  @Override
  public @NonNull CastlingRight getCastlingRightWhite() {
    @SuppressWarnings("null") final EnumMap<com.github.bhlangonijr.chesslib.Side, CastleRight> castlingRightMap = board
        .getCastleRight();
    @SuppressWarnings("null") final CastleRight castlingRightWhite = castlingRightMap
        .get(com.github.bhlangonijr.chesslib.Side.WHITE);
    return mapCastlingRight(castlingRightWhite);
  }

  @Override
  public @NonNull CastlingRight getCastlingRightBlack() {
    @SuppressWarnings("null") final EnumMap<com.github.bhlangonijr.chesslib.Side, CastleRight> castlingRightMap = board
        .getCastleRight();
    @SuppressWarnings("null") final CastleRight castlingRightBlack = castlingRightMap
        .get(com.github.bhlangonijr.chesslib.Side.BLACK);
    return mapCastlingRight(castlingRightBlack);
  }

  private static CastlingRight mapCastlingRight(CastleRight carlosCastlingRight) {
    return switch (carlosCastlingRight) {
      case KING_AND_QUEEN_SIDE -> CastlingRight.KING_AND_QUEEN_SIDE;
      case KING_SIDE -> CastlingRight.KING_SIDE;
      case QUEEN_SIDE -> CastlingRight.QUEEN_SIDE;
      case NONE -> CastlingRight.NONE;
      default -> throw new IllegalArgumentException();
    };
  }

  @Override
  public int getPerformedHalfMoveCount() {
    return performedHalfMoveCount;
  }

  @Override
  public ImmutableList<DynamicPosition> getDynamicPositionList() {
    return NonNullWrapperCommon.copyOfList(dynamicPositionList);
  }

  @Override
  public ImmutableList<HalfMove> getHalfMoveList() {
    return NonNullWrapperCommon.copyOfList(halfMoveList);
  }

  @Override
  public DynamicPosition getDynamicPosition() {
    return NonNullWrapperCommon.getLast(dynamicPositionList);
  }

  @Override
  public ImmutableSet<MoveSpecification> getPossibleMoveSpecificationSet() {
    return NonNullWrapperCommon.copyOfSet(generateMoveSpecificationSet(this.board));
  }

  // the API does not return null
  @SuppressWarnings("null")
  private static List<Move> generateLegalMoveList(Board board) {
    try {
      return MoveGenerator.generateLegalMoves(board);
    } catch (final MoveGeneratorException e) {
      throw new RuntimeException("Problem with legal move generation", e);
    }
  }

  @SuppressWarnings("null")
  private static List<MoveBackup> generateLegalMoveBackupList(Board board) {
    List<Move> legalMoveList;
    try {
      legalMoveList = MoveGenerator.generateLegalMoves(board);
    } catch (final MoveGeneratorException e) {
      throw new RuntimeException("Problem with legal move generation", e);
    }

    final List<MoveBackup> moveBackupList = new ArrayList<>();
    for (final Move move : legalMoveList) {
      board.doMove(move);
      final var moveBackup = board.getBackup().getLast();
      board.undoMove();
      moveBackupList.add(moveBackup);
    }
    return moveBackupList;
  }

  private static Set<MoveSpecification> generateMoveSpecificationSet(Board board) {
    final List<Move> moveList = generateLegalMoveList(board);

    final Set<MoveSpecification> result = new TreeSet<>();
    for (final Move move : moveList) {
      final MoveSpecification moveSpecification = convertMove(board, move);
      result.add(moveSpecification);
    }
    return result;
  }

  private static MoveSpecification convertMove(Board board, Move move) {
    final com.github.bhlangonijr.chesslib.Square fromSquare = NonNullWrapperLibraryCarlos.getFrom(move);
    final com.github.bhlangonijr.chesslib.Piece movingPiece = NonNullWrapperLibraryCarlos.getPiece(board, fromSquare);
    return MoveConversionUtility.convertMove(move, movingPiece);
  }

  private static Set<LegalMove> generateLegalMoveSet(Board board) {
    final List<MoveBackup> moveBackupList = generateLegalMoveBackupList(board);

    final Set<LegalMove> result = new TreeSet<>();
    for (final MoveBackup moveBackup : moveBackupList) {
      final Move move = NonNullWrapperLibraryCarlos.getMove(moveBackup);
      final MoveSpecification moveSpecification = convertMove(board, move);
      final Piece movingPiece = EnumConversionUtility
          .convertPiece(NonNullWrapperLibraryCarlos.getMovingPiece(moveBackup));
      final Piece pieceCaptured = EnumConversionUtility
          .convertPiece(NonNullWrapperLibraryCarlos.getCapturedPiece(moveBackup));
      final LegalMove legalMove = new LegalMove(moveSpecification, movingPiece, pieceCaptured);
      result.add(legalMove);
    }
    return result;
  }

  @Override
  public boolean isFirstMove() {
    return board.getBackup().isEmpty();
  }

  @Override
  public LegalMove getLastMove() {
    return NonNullWrapperCommon.getLast(performedLegalMoveList);
  }

  private static LegalMove calculateLegalMove(MoveSpecification moveSpecification, MoveBackup moveBackup) {
    final Piece movingPiece = EnumConversionUtility
        .convertToMyPiece(NonNullWrapperLibraryCarlos.getMovingPiece(moveBackup));
    final Piece pieceCaptured = EnumConversionUtility
        .convertToMyPiece(NonNullWrapperLibraryCarlos.getCapturedPiece(moveBackup));
    return new LegalMove(moveSpecification, movingPiece, pieceCaptured);
  }

  @Override
  public Square getEnPassantCaptureTargetSquare() {
    return EnPassantCaptureUtility.calculateEnPassantCaptureTargetSquare(getLastMove());
  }

  @Override
  public ImmutableList<MoveSpecification> getPerformedMoveSpecificationList() {
    final List<MoveSpecification> moveSpecificationList = new ArrayList<>();
    for (final MoveBackup moveBackup : NonNullWrapperLibraryCarlos.getBackup(this.board)) {

      final Move move = NonNullWrapperLibraryCarlos.getMove(moveBackup);
      final com.github.bhlangonijr.chesslib.Piece movingPiece = NonNullWrapperLibraryCarlos.getMovingPiece(moveBackup);

      moveSpecificationList.add(MoveConversionUtility.convertMove(move, movingPiece));
    }
    return NonNullWrapperCommon.copyOfList(moveSpecificationList);
  }

  @Override
  public ImmutableSet<LegalMove> getLegalMoveSet() {
    return NonNullWrapperCommon.copyOfSet(generateLegalMoveSet(this.board));
  }

  private static boolean calculateIsPawnMove(MoveBackup moveBackup) {
    final com.github.bhlangonijr.chesslib.Piece movingPiece = NonNullWrapperLibraryCarlos.getMovingPiece(moveBackup);

    return movingPiece == com.github.bhlangonijr.chesslib.Piece.WHITE_PAWN
        || movingPiece == com.github.bhlangonijr.chesslib.Piece.BLACK_PAWN;
  }

  private static boolean calculateIsPromotion(MoveBackup moveBackup) {
    if (!calculateIsPawnMove(moveBackup)) {
      return false;
    }
    final Move move = NonNullWrapperLibraryCarlos.getMove(moveBackup);
    final com.github.bhlangonijr.chesslib.Piece movingPiece = NonNullWrapperLibraryCarlos.getMovingPiece(moveBackup);

    return switch (movingPiece.getPieceSide()) {
      case WHITE -> switch (move.getTo().getRank()) {
        case RANK_1, RANK_2, RANK_3, RANK_4, RANK_5, RANK_6, RANK_7 -> false;
        case RANK_8 -> true;
        case NONE -> throw new IllegalArgumentException();
        default -> throw new IllegalArgumentException();
      };
      case BLACK -> switch (move.getTo().getRank()) {
        case RANK_1 -> true;
        case RANK_2, RANK_3, RANK_4, RANK_5, RANK_6, RANK_7, RANK_8 ->
            // would be illegal for a white pawn, but we are not checking here
            false;
        case NONE -> throw new IllegalArgumentException();
        default -> throw new IllegalArgumentException();
      }; // would be illegal for a white pawn, but we are not checking here

      default -> throw new IllegalArgumentException();
    };
  }

  @Override
  public StaticPosition getStaticPositionBeforeLastMove() {
    if (isFirstMove()) {
      throw new ProgrammingMistakeException("The method cannot be called if no move was yet made");
    }
    final var lastMove = board.undoMove();
    final StaticPosition staticPosition = getStaticPosition();
    board.doMove(lastMove);
    return staticPosition;
  }

  @Override
  public boolean movesStrict(String... sanArray) {
    for (final String san : sanArray) {
      if (san == null) {
        throw new IllegalArgumentException();
      }
      this.moveStrict(san);
    }
    return true;
  }

  @Override
  public boolean movesLenient(String... sanArray) {
    for (final String san : sanArray) {
      if (san == null) {
        throw new IllegalArgumentException();
      }
      this.moveLenient(san);
    }
    return true;
  }

  @Override
  public ImmutableList<LegalMove> getPerformedLegalMoveList() {
    return NonNullWrapperCommon.copyOfList(performedLegalMoveList);
  }

  @Override
  public CastlingRightLoss getCastlingRightLoss(Side side, CastlingMove castlingMove) {
    throw new UnsupportedOperationException("Castling right loss tracking is not supported in Carlos's API");
  }
}
