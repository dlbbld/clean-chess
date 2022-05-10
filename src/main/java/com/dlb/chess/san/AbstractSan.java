package com.dlb.chess.san;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.enums.SanLetter;
import com.dlb.chess.san.enums.SanType;

public abstract class AbstractSan {

  public static Square calculateFromSquare(SanConversion sanConversion) {
    if (sanConversion.fromFile() == File.NONE || sanConversion.fromRank() == Rank.NONE) {
      return Square.NONE;
    }
    return Square.calculate(sanConversion.fromFile(), sanConversion.fromRank());
  }

  public static CastlingMove calculateCastlingMove(SanType sanType) {
    switch (sanType) {
      case BISHOP_CAPTURING_SQUARE_MOVE:
      case BISHOP_CAPTURING_FILE_MOVE:
      case BISHOP_CAPTURING_NEITHER_MOVE:
      case BISHOP_CAPTURING_RANK_MOVE:
      case BISHOP_NON_CAPTURING_SQUARE_MOVE:
      case BISHOP_NON_CAPTURING_FILE_MOVE:
      case BISHOP_NON_CAPTURING_NEITHER_MOVE:
      case BISHOP_NON_CAPTURING_RANK_MOVE:
      case KING_NON_CASTLING_CAPTURING_MOVE:
      case KING_NON_CASTLING_NON_CAPTURING_MOVE:
      case KNIGHT_CAPTURING_SQUARE_MOVE:
      case KNIGHT_CAPTURING_FILE_MOVE:
      case KNIGHT_CAPTURING_NEITHER_MOVE:
      case KNIGHT_CAPTURING_RANK_MOVE:
      case KNIGHT_NON_CAPTURING_SQUARE_MOVE:
      case KNIGHT_NON_CAPTURING_FILE_MOVE:
      case KNIGHT_NON_CAPTURING_NEITHER_MOVE:
      case KNIGHT_NON_CAPTURING_RANK_MOVE:
      case PAWN_CAPTURING_NON_PROMOTION_MOVE:
      case PAWN_CAPTURING_PROMOTION_MOVE:
      case PAWN_NON_CAPTURING_NON_PROMOTION_MOVE:
      case PAWN_NON_CAPTURING_PROMOTION_MOVE:
      case QUEEN_CAPTURING_SQUARE_MOVE:
      case QUEEN_CAPTURING_FILE_MOVE:
      case QUEEN_CAPTURING_NEITHER_MOVE:
      case QUEEN_CAPTURING_RANK_MOVE:
      case QUEEN_NON_CAPTURING_SQUARE_MOVE:
      case QUEEN_NON_CAPTURING_FILE_MOVE:
      case QUEEN_NON_CAPTURING_NEITHER_MOVE:
      case QUEEN_NON_CAPTURING_RANK_MOVE:
      case ROOK_CAPTURING_FILE_MOVE:
      case ROOK_CAPTURING_NEITHER_MOVE:
      case ROOK_CAPTURING_RANK_MOVE:
      case ROOK_CAPTURING_SQUARE_MOVE:
      case ROOK_NON_CAPTURING_FILE_MOVE:
      case ROOK_NON_CAPTURING_NEITHER_MOVE:
      case ROOK_NON_CAPTURING_RANK_MOVE:
      case ROOK_NON_CAPTURING_SQUARE_MOVE:
        return CastlingMove.NONE;
      case KING_CASTLING_KING_SIDE_MOVE:
        return CastlingMove.KING_SIDE;
      case KING_CASTLING_QUEEN_SIDE_MOVE:
        return CastlingMove.QUEEN_SIDE;
      default:
        throw new IllegalArgumentException();

    }
  }

  public static Set<LegalMove> filterLegalMovesCandidates(Set<LegalMove> legalMoveSet, Square toSquare) {
    final Set<LegalMove> legalMovesForToQuare = new TreeSet<>();
    for (final LegalMove moveCandidate : legalMoveSet) {
      if (moveCandidate.moveSpecification().toSquare() == toSquare) {
        legalMovesForToQuare.add(moveCandidate);
      }
    }
    return legalMovesForToQuare;
  }

  public static Set<LegalMove> filterLegalMovesCandidates(Set<LegalMove> legalMoveSet, File fromFile) {
    final Set<LegalMove> legalMovesForToQuare = new TreeSet<>();
    for (final LegalMove moveCandidate : legalMoveSet) {
      if (moveCandidate.moveSpecification().fromSquare().getFile() == fromFile) {
        legalMovesForToQuare.add(moveCandidate);
      }
    }
    return legalMovesForToQuare;
  }

  public static boolean calculateHasOtherFilesHavingLegalMoves(File file, Set<LegalMove> legalMoveSet) {
    for (final LegalMove moveCandidate : legalMoveSet) {
      final File candidateFromFile = moveCandidate.moveSpecification().fromSquare().getFile();
      if (candidateFromFile != file) {
        return true;
      }
    }
    return false;
  }

  public static int calculateNumberOfLegalMovesFromSameFile(File file, Set<LegalMove> legalMoveSet) {
    return calculateLegalMovesFromSameFile(file, legalMoveSet).size();
  }

  private static Set<LegalMove> calculateLegalMovesFromSameFile(File file, Set<LegalMove> legalMoveSet) {
    final Set<LegalMove> filteredSet = new TreeSet<>();
    for (final LegalMove moveCandidate : legalMoveSet) {
      final File candidateFromFile = moveCandidate.moveSpecification().fromSquare().getFile();
      if (candidateFromFile == file) {
        filteredSet.add(moveCandidate);
      }
    }
    return filteredSet;
  }

  public static boolean calculateHasOtherRanksHavingLegalMoves(Rank rank, Set<LegalMove> legalMoveSet) {
    for (final LegalMove moveCandidate : legalMoveSet) {
      final Rank candidateFromRank = moveCandidate.moveSpecification().fromSquare().getRank();
      if (candidateFromRank != rank) {
        return true;
      }
    }
    return false;
  }

  public static int calculateNumberOfLegalMovesFromSameRank(Rank rank, Set<LegalMove> legalMoveSet) {
    return calculateLegalMovesFromSameRank(rank, legalMoveSet).size();
  }

  private static Set<LegalMove> calculateLegalMovesFromSameRank(Rank rank, Set<LegalMove> legalMoveSet) {
    final Set<LegalMove> filteredSet = new TreeSet<>();
    for (final LegalMove moveCandidate : legalMoveSet) {
      final Rank candidateFromRank = moveCandidate.moveSpecification().fromSquare().getRank();
      if (candidateFromRank == rank) {
        filteredSet.add(moveCandidate);
      }
    }
    return filteredSet;
  }

  public static void appendCheckOrCheckmate(StringBuilder buildSan, boolean isCheckmate, boolean isCheck) {
    if (isCheckmate) {
      buildSan.append(SanLetter.CHECKMATE.getLetter());
    } else if (isCheck) {
      buildSan.append(SanLetter.CHECK.getLetter());
    }
  }
}
