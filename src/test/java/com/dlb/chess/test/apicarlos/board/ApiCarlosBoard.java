package com.dlb.chess.test.apicarlos.board;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.AbstractBoard;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.CastlingConstants;
import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.constants.DynamicPositionConstants;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.model.DynamicPosition;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.utility.HalfMoveUtility;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.CastlingRightBoth;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.moves.utility.EnPassantCaptureUtility;
import com.dlb.chess.test.apicarlos.NonNullWrapperApiCarlos;
import com.dlb.chess.test.apicarlos.utility.MoveConversionUtility;
import com.dlb.chess.test.apicomparison.utility.BoardConversionUtitlity;
import com.dlb.chess.test.apicomparison.utility.EnumConversionUtility;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.CastleRight;
import com.github.bhlangonijr.chesslib.MoveBackup;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveConversionException;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;

public class ApiCarlosBoard extends AbstractBoard {

  private final Board board = new Board();

  private int performedHalfMoveCount;
  private final List<LegalMove> performedLegalMoveList;
  private final List<DynamicPosition> dynamicPositionList;
  private final List<HalfMove> halfMoveList;

  public ApiCarlosBoard() {

    performedHalfMoveCount = 0;
    performedLegalMoveList = new ArrayList<>();
    dynamicPositionList = new ArrayList<>();
    dynamicPositionList.add(DynamicPositionConstants.INITIAL);
    halfMoveList = new ArrayList<>();

  }

  @Override
  public boolean performMove(MoveSpecification moveSpecification) {
    final var result = board.doMove(MoveConversionUtility.convertMoveSpecification(moveSpecification));
    populateMoveHistory(moveSpecification);
    return result;
  }

  @Override
  public boolean performMove(@NonNull String san) {

    final var result = board.doMove(san);
    final MoveSpecification lastMoveSpecification = calculateLastMoveSpecification();
    populateMoveHistory(lastMoveSpecification, san);
    return result;
  }

  private MoveSpecification calculateLastMoveSpecification() {
    final var moveBackup = board.getBackup().getLast();
    @SuppressWarnings("null") @NonNull final Move move = moveBackup.getMove();
    final var havingMove = NonNullWrapperApiCarlos.getSideToMove(moveBackup);
    final com.github.bhlangonijr.chesslib.Square fromSquare = NonNullWrapperApiCarlos.getFrom(move);
    com.github.bhlangonijr.chesslib.Piece movingPiece;
    if (moveBackup.isCastleMove()) {
      movingPiece = switch (havingMove) {
        case WHITE -> com.github.bhlangonijr.chesslib.Piece.WHITE_KING;
        case BLACK -> com.github.bhlangonijr.chesslib.Piece.BLACK_KING;
        default -> throw new IllegalArgumentException();
      };
    } else {
      movingPiece = NonNullWrapperApiCarlos.getPiece(this.board, fromSquare);
    }
    return MoveConversionUtility.convertMove(havingMove, move, movingPiece);
  }

  private void populateMoveHistory(MoveSpecification moveSpecification, String san) {
    performedHalfMoveCount++;

    final MoveBackup moveBackup = NonNullWrapperApiCarlos.getLast(this.board);
    final LegalMove legalMove = calculateLegalMove(moveSpecification, moveBackup);
    performedLegalMoveList.add(legalMove);
    dynamicPositionList.add(new DynamicPosition(getHavingMove(), getStaticPosition(), isEnPassantCapturePossible(),
        getCastlingRightBoth()));

    // TODO timely dependency, must be after the above code is very very dangerous
    final HalfMove halfMove = HalfMoveUtility.calculateHalfMoveApiCarlosFix(moveSpecification, this, san);
    halfMoveList.add(halfMove);
  }

  private void populateMoveHistory(MoveSpecification moveSpecification) {
    performedHalfMoveCount++;

    final MoveBackup moveBackup = NonNullWrapperApiCarlos.getLast(this.board);
    final LegalMove legalMove = calculateLegalMove(moveSpecification, moveBackup);
    performedLegalMoveList.add(legalMove);
    dynamicPositionList.add(new DynamicPosition(getHavingMove(), getStaticPosition(), isEnPassantCapturePossible(),
        getCastlingRightBoth()));

    // TODO timely dependency, must be after the above code is very very dangerous
    final HalfMove halfMove = HalfMoveUtility.calculateHalfMove(moveSpecification, this);
    halfMoveList.add(halfMove);
  }

  @Override
  public void unperformMove() {
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
      final List<Move> legalMoveList = ApiCarlosImplementationUtility.generateLegalMoves(this.board);
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
      performMove(moveSpecification);
      if (isThreefoldRepetition()) {
        unperformMove();
        return true;
      }
      unperformMove();
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
    return ApiCarlosImplementationUtility.calculateIsInsufficientMaterial(this.board);
  }

  @Override
  public boolean isInsufficientMaterial(Side side) {
    return ApiCarlosImplementationUtility.calculateIsInsufficientMaterial(side, this.board);
  }

  @Override
  public String getFen() {
    return NonNullWrapperApiCarlos.getFen(this.board);
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

    final MoveBackup lastMoveBackup = NonNullWrapperApiCarlos.getLast(board);
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
    for (final MoveBackup moveBackup : NonNullWrapperApiCarlos.getBackup(board)) {
      result.add(NonNullWrapperApiCarlos.getMove(moveBackup));
    }
    return result;
  }

  @Override
  public String getLan() {
    if (board.getBackup().isEmpty()) {
      throw new IllegalStateException("There is no last move");
    }
    final MoveBackup moveBackup = NonNullWrapperApiCarlos.getLast(this.board);
    final Move move = NonNullWrapperApiCarlos.getMove(moveBackup);
    final com.github.bhlangonijr.chesslib.Piece movingPiece = NonNullWrapperApiCarlos.getMovingPiece(moveBackup);

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
      lan.append("x");
    }
    lan.append(move.getTo().toString().toLowerCase());
    if (calculateIsPromotion(moveBackup)) {
      lan.append("=");
      final var promotionPiece = move.getPromotion();
      final var promotionPieceFenSymbol = promotionPiece.getFenSymbol();
      final var promotionPieceSymbol = promotionPieceFenSymbol.toUpperCase();
      lan.append(promotionPieceSymbol);
    }
    if (isCheckmate()) {
      lan.append("#");
    } else if (isCheck()) {
      lan.append("+");
    }
    return NonNullWrapperCommon.toString(lan);
  }

  @Override
  public Piece getMovingPiece() {
    if (board.getBackup().isEmpty()) {
      throw new IllegalStateException("There is no last move");
    }
    final MoveBackup moveBackup = NonNullWrapperApiCarlos.getLast(this.board);
    if (moveBackup.isCastleMove()) {
      return Piece.NONE;
    }
    final com.github.bhlangonijr.chesslib.Piece movingPiece = NonNullWrapperApiCarlos.getMovingPiece(moveBackup);
    return EnumConversionUtility.convertToMyPiece(movingPiece);
  }

  @Override
  public boolean isCapture() {
    if (board.getBackup().isEmpty()) {
      throw new IllegalStateException("There is no last move");
    }
    final MoveBackup moveBackup = NonNullWrapperApiCarlos.getLast(this.board);
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
    final MoveBackup moveBackup = NonNullWrapperApiCarlos.getLast(this.board);
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
  public boolean isSeventyFiftyMove() {
    return getHalfMoveClock() >= ChessConstants.SEVENTY_FIVE_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD;
  }

  @Override
  public boolean isFivefoldRepetition() {
    return board.isRepetition(5);
  }

  @Override
  public Side getHavingMove() {
    return EnumConversionUtility.convertToMySide(NonNullWrapperApiCarlos.getSideToMove(this.board));
  }

  @Override
  public StaticPosition getStaticPosition() {
    return BoardConversionUtitlity.convertBoardToStaticPosition(this.board);
  }

  @Override
  public boolean isEnPassantCapturePossible() {
    return ApiCarlosImplementationUtility.calculateIsEnPassantCapturePossible(this.board);
  }

  @SuppressWarnings("null")
  @Override
  public CastlingRightBoth getCastlingRightBoth() {
    final EnumMap<com.github.bhlangonijr.chesslib.Side, CastleRight> castlingRightMap = board.getCastleRight();
    final CastleRight castlingRightWhite = castlingRightMap.get(com.github.bhlangonijr.chesslib.Side.WHITE);
    final CastleRight castlingRightBlack = castlingRightMap.get(com.github.bhlangonijr.chesslib.Side.BLACK);

    // because we have object comparison (for performance!) we lookup here the statically defined castling right
    // generating a new castling right map would not work!!
    return switch (castlingRightWhite) {
      case KING_AND_QUEEN_SIDE -> switch (castlingRightBlack) {
        case KING_AND_QUEEN_SIDE -> CastlingConstants.CASTLING_KQ_KQ;
        case KING_SIDE -> CastlingConstants.CASTLING_KQ_K;
        case NONE -> CastlingConstants.CASTLING_KQ_NONE;
        case QUEEN_SIDE -> CastlingConstants.CASTLING_KQ_Q;
        default -> throw new IllegalArgumentException();
      };
      case KING_SIDE -> switch (castlingRightBlack) {
        case KING_AND_QUEEN_SIDE -> CastlingConstants.CASTLING_K_KQ;
        case KING_SIDE -> CastlingConstants.CASTLING_K_K;
        case NONE -> CastlingConstants.CASTLING_K_NONE;
        case QUEEN_SIDE -> CastlingConstants.CASTLING_K_Q;
        default -> throw new IllegalArgumentException();
      };
      case NONE -> switch (castlingRightBlack) {
        case KING_AND_QUEEN_SIDE -> CastlingConstants.CASTLING_NONE_KQ;
        case KING_SIDE -> CastlingConstants.CASTLING_NONE_K;
        case NONE -> CastlingConstants.CASTLING_NONE_NONE;
        case QUEEN_SIDE -> CastlingConstants.CASTLING_NONE_Q;
        default -> throw new IllegalArgumentException();
      };
      case QUEEN_SIDE -> switch (castlingRightBlack) {
        case KING_AND_QUEEN_SIDE -> CastlingConstants.CASTLING_Q_KQ;
        case KING_SIDE -> CastlingConstants.CASTLING_Q_K;
        case NONE -> CastlingConstants.CASTLING_Q_NONE;
        case QUEEN_SIDE -> CastlingConstants.CASTLING_Q_Q;
        default -> throw new IllegalArgumentException();
      };
      default -> throw new IllegalArgumentException();
    };
  }

  @Override
  public int getPerformedHalfMoveCount() {
    return performedHalfMoveCount;
  }

  @Override
  public List<DynamicPosition> getDynamicPositionList() {
    return dynamicPositionList;
  }

  @Override
  public List<HalfMove> getHalfMoveList() {
    return halfMoveList;
  }

  @Override
  public DynamicPosition getDynamicPosition() {
    return NonNullWrapperCommon.getLast(dynamicPositionList);
  }

  @Override
  public Set<MoveSpecification> getPossibleMoveSpecificationSet() {
    return generateMoveSpecificationSet(this.board);
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
    final var havingMove = NonNullWrapperApiCarlos.getSideToMove(board);
    final com.github.bhlangonijr.chesslib.Square fromSquare = NonNullWrapperApiCarlos.getFrom(move);
    final com.github.bhlangonijr.chesslib.Piece movingPiece = NonNullWrapperApiCarlos.getPiece(board, fromSquare);
    return MoveConversionUtility.convertMove(havingMove, move, movingPiece);
  }

  private static Set<LegalMove> generateLegalMoveSet(Board board) {
    final List<MoveBackup> moveBackupList = generateLegalMoveBackupList(board);

    final Set<LegalMove> result = new TreeSet<>();
    for (final MoveBackup moveBackup : moveBackupList) {
      final Move move = NonNullWrapperApiCarlos.getMove(moveBackup);
      final MoveSpecification moveSpecification = convertMove(board, move);
      LegalMove legalMove;
      if (CastlingUtility.calculateIsCastlingMove(moveSpecification)) {
        legalMove = new LegalMove(moveSpecification);
      } else {
        final Piece movingPiece = EnumConversionUtility
            .convertPiece(NonNullWrapperApiCarlos.getMovingPiece(moveBackup));
        final Piece pieceCaptured = EnumConversionUtility
            .convertPiece(NonNullWrapperApiCarlos.getCapturedPiece(moveBackup));
        legalMove = new LegalMove(moveSpecification, movingPiece, pieceCaptured);
      }
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
    if (moveBackup.isCastleMove()) {
      return new LegalMove(moveSpecification);
    }
    final Piece movingPiece = EnumConversionUtility
        .convertToMyPiece(NonNullWrapperApiCarlos.getMovingPiece(moveBackup));
    final Piece pieceCaptured = EnumConversionUtility
        .convertToMyPiece(NonNullWrapperApiCarlos.getCapturedPiece(moveBackup));
    return new LegalMove(moveSpecification, movingPiece, pieceCaptured);
  }

  @Override
  public Square getEnPassantCaptureTargetSquare() {
    return EnPassantCaptureUtility.calculateEnPassantCaptureTargetSquare(getLastMove());
  }

  @Override
  public List<MoveSpecification> getPerformedMoveSpecificationList() {
    final List<MoveSpecification> moveSpecificationList = new ArrayList<>();
    for (final MoveBackup moveBackup : NonNullWrapperApiCarlos.getBackup(this.board)) {

      final com.github.bhlangonijr.chesslib.Side side = NonNullWrapperApiCarlos.getSideToMove(moveBackup);
      final Move move = NonNullWrapperApiCarlos.getMove(moveBackup);
      final com.github.bhlangonijr.chesslib.Piece movingPiece = NonNullWrapperApiCarlos.getMovingPiece(moveBackup);

      moveSpecificationList.add(MoveConversionUtility.convertMove(side, move, movingPiece));
    }
    return moveSpecificationList;
  }

  @Override
  public Set<LegalMove> getLegalMoveSet() {
    return generateLegalMoveSet(this.board);
  }

  private static boolean calculateIsPawnMove(MoveBackup moveBackup) {
    final com.github.bhlangonijr.chesslib.Piece movingPiece = NonNullWrapperApiCarlos.getMovingPiece(moveBackup);

    return movingPiece == com.github.bhlangonijr.chesslib.Piece.WHITE_PAWN
        || movingPiece == com.github.bhlangonijr.chesslib.Piece.BLACK_PAWN;
  }

  private static boolean calculateIsPromotion(MoveBackup moveBackup) {
    if (!calculateIsPawnMove(moveBackup)) {
      return false;
    }
    final Move move = NonNullWrapperApiCarlos.getMove(moveBackup);
    final com.github.bhlangonijr.chesslib.Piece movingPiece = NonNullWrapperApiCarlos.getMovingPiece(moveBackup);

    return switch (movingPiece.getPieceSide()) {
      case WHITE -> switch (move.getTo().getRank()) {
        case RANK_1, RANK_2, RANK_3, RANK_4, RANK_5, RANK_6, RANK_7 -> false;
        case RANK_8 -> true;
        case NONE -> throw new IllegalArgumentException();
        default -> throw new IllegalArgumentException();
      };
      case BLACK -> switch (move.getTo().getRank()) {
        case RANK_1 -> true;
        case RANK_2, RANK_3, RANK_4, RANK_5, RANK_6, RANK_7, RANK_8 -> /*
                                                                        * would be illegal for a white pawn, but we are
                                                                        * not checking here
                                                                        */ false;
        case NONE -> throw new IllegalArgumentException();
        default -> throw new IllegalArgumentException();
      }; /*
          * would be illegal for a white pawn, but we are not checking here
          */
      default -> throw new IllegalArgumentException();
    };
  }

  public void loadFromFen(String fen) {
    board.loadFromFen(fen);
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
  public boolean performMoves(String... sanArray) {
    for (final String san : sanArray) {
      if (san == null) {
        throw new IllegalArgumentException();
      }
      this.performMove(san);
    }
    return true;
  }

  @Override
  public List<LegalMove> getPerformedLegalMoveList() {
    return performedLegalMoveList;
  }

}
