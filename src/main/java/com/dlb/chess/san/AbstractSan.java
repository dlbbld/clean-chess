package com.dlb.chess.san;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.LegalMove;

abstract class AbstractSan {

  static Square calculateFromSquare(SanConversion sanConversion) {
    if (sanConversion.fromFile() == File.NONE || sanConversion.fromRank() == Rank.NONE) {
      return Square.NONE;
    }
    return Square.calculate(sanConversion.fromFile(), sanConversion.fromRank());
  }

  static Set<LegalMove> filterLegalMovesCandidates(Set<LegalMove> legalMoveSet, Square toSquare) {
    final Set<LegalMove> legalMovesForToQuare = new TreeSet<>();
    for (final LegalMove moveCandidate : legalMoveSet) {
      if (moveCandidate.moveSpecification().toSquare() == toSquare) {
        legalMovesForToQuare.add(moveCandidate);
      }
    }
    return legalMovesForToQuare;
  }

  static Set<LegalMove> calculateLegalMovesCandidates(Set<LegalMove> legalMoveSet, File fromFile) {
    final Set<LegalMove> legalMovesForToQuare = new TreeSet<>();
    for (final LegalMove moveCandidate : legalMoveSet) {
      if (moveCandidate.moveSpecification().fromSquare().getFile() == fromFile) {
        legalMovesForToQuare.add(moveCandidate);
      }
    }
    return legalMovesForToQuare;
  }

  static boolean calculateHasOtherFilesHavingLegalMoves(File file, Set<LegalMove> legalMoveSet) {
    for (final LegalMove moveCandidate : legalMoveSet) {
      final File candidateFromFile = moveCandidate.moveSpecification().fromSquare().getFile();
      if (candidateFromFile != file) {
        return true;
      }
    }
    return false;
  }

  static int calculateNumberOfLegalMovesFromFile(File file, Set<LegalMove> legalMoveSet) {
    return calculateLegalMovesFromFile(file, legalMoveSet).size();
  }

  private static Set<LegalMove> calculateLegalMovesFromFile(File file, Set<LegalMove> legalMoveSet) {
    final Set<LegalMove> filteredSet = new TreeSet<>();
    for (final LegalMove moveCandidate : legalMoveSet) {
      final File candidateFromFile = moveCandidate.moveSpecification().fromSquare().getFile();
      if (candidateFromFile == file) {
        filteredSet.add(moveCandidate);
      }
    }
    return filteredSet;
  }

  static int calculateNumberOfLegalMovesFromOtherFiles(File file, Set<LegalMove> legalMoveSet) {
    return calculateLegalMovesFromOtherFiles(file, legalMoveSet).size();
  }

  private static Set<LegalMove> calculateLegalMovesFromOtherFiles(File file, Set<LegalMove> legalMoveSet) {
    final Set<LegalMove> filteredSet = new TreeSet<>();
    for (final LegalMove moveCandidate : legalMoveSet) {
      final File candidateFromFile = moveCandidate.moveSpecification().fromSquare().getFile();
      if (candidateFromFile != file) {
        filteredSet.add(moveCandidate);
      }
    }
    return filteredSet;
  }

  static int calculateNumberOfLegalMovesFromRank(Rank rank, Set<LegalMove> legalMoveSet) {
    return calculateLegalMovesFromRank(rank, legalMoveSet).size();
  }

  private static Set<LegalMove> calculateLegalMovesFromRank(Rank rank, Set<LegalMove> legalMoveSet) {
    final Set<LegalMove> filteredSet = new TreeSet<>();
    for (final LegalMove moveCandidate : legalMoveSet) {
      final Rank candidateFromRank = moveCandidate.moveSpecification().fromSquare().getRank();
      if (candidateFromRank == rank) {
        filteredSet.add(moveCandidate);
      }
    }
    return filteredSet;
  }

  static int calculateNumberOfLegalMovesFromSquare(Square square, Set<LegalMove> legalMoveSet) {
    return calculateLegalMovesFromSquare(square, legalMoveSet).size();
  }

  private static Set<LegalMove> calculateLegalMovesFromSquare(Square square, Set<LegalMove> legalMoveSet) {
    final Set<LegalMove> filteredSet = new TreeSet<>();
    for (final LegalMove moveCandidate : legalMoveSet) {
      final Square candidateFromSquare = moveCandidate.moveSpecification().fromSquare();
      if (candidateFromSquare == square) {
        filteredSet.add(moveCandidate);
      }
    }
    return filteredSet;
  }

  static boolean calculateHasOtherRanksHavingLegalMoves(Rank rank, Set<LegalMove> legalMoveSet) {
    for (final LegalMove moveCandidate : legalMoveSet) {
      final Rank candidateFromRank = moveCandidate.moveSpecification().fromSquare().getRank();
      if (candidateFromRank != rank) {
        return true;
      }
    }
    return false;
  }

}
