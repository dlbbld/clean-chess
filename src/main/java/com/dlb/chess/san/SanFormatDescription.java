package com.dlb.chess.san;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.model.SanPatternDescription;
import com.dlb.chess.san.model.SanExample;

public abstract class SanFormatDescription extends AbstractSan {

  public static void main(String[] args) {
    // printSanFormatDescription(WHITE);
    for (final Square square : Square.BOARD_SQUARE_LIST) {
      System.out.println("Square " + square + " = Square." + square + ";");
    }

  }

  // TODO SAN hardcoded or text file description - think how to organize
  public static void printSanFormatDescription(Side havingMove) {
    // we can have the following formats
    final SanPatternDescription pawnNonCapturingNonPromotionMovesWhite = new SanPatternDescription(
        "Pawn non capturing non promotion moves",
        "not d1, as pawn cannot move backwards and not d8, this is under promotion", new SanExample("d3", "d2-d3"),
        new SanExample("d4", "d3-d4 or d2-d4"), new SanExample("d5", "d4-d5"), new SanExample("d6", "d5-d6"),
        new SanExample("d7", "d6-d7"));

    final SanPatternDescription pawnCapturingNonPromotionMovesWhite = new SanPatternDescription(
        "Pawn capturing promotion moves",
        "not dxe1, as pawn cannot move backwards and not dxe8, this is under promotion",
        new SanExample("dxe3", "d2xe3"), new SanExample("dxe4", "d3xe4"), new SanExample("dxe5", "d4xe5"),
        new SanExample("dxe6", "d5xe6"), new SanExample("dxe7", "d6xe7"));

    final SanPatternDescription pawnNonCapturingPromotionMovesWhite = new SanPatternDescription(
        "Pawn non capturing promotion moves", "rank must always be white promotion rank 8",
        new SanExample("d8=Q", "d7-d8 and promotion to queen"), new SanExample("d8=R", "d7-d8 and promotion to rook"),
        new SanExample("d8=N", "d7-d8 and promotion to knight"),
        new SanExample("d8=B", "d7-d8 and promotion to bishop"));

    final SanPatternDescription pawnCapturingPromotionMovesWhite = new SanPatternDescription(
        "Pawn non capturing promotion moves", "rank must always be white promotion rank 8",
        new SanExample("dxe8=Q", "d7xe8 and promotion to queen"),
        new SanExample("dxe8=R", "d7xe8 and promotion to rook"),
        new SanExample("dxe8=N", "d7xe8 and promotion to knight"),
        new SanExample("dxe8=B", "d7xe8 and promotion to bishop"));

    final SanPatternDescription pawnNonCapturingNonPromotionMovesBlack = new SanPatternDescription(
        "Pawn non capturing non promotion moves",
        "not d8, as pawn cannot move backwards and not d1, this is under promotion", new SanExample("d6", "d7-d6"),
        new SanExample("d5", "d6-d5 or d7-d5"), new SanExample("d4", "d5-d4"), new SanExample("d3", "d4-d3"),
        new SanExample("d2", "d3-d2"));

    final SanPatternDescription pawnCapturingNonPromotionMovesBlack = new SanPatternDescription(
        "Pawn capturing promotion moves",
        "not dxe8, as pawn cannot move backwards and not dxe1, this is under promotion",
        new SanExample("dxe6", "d7xe6"), new SanExample("dxe5", "d6xe5"), new SanExample("dxe4", "d5xe4"),
        new SanExample("dxe3", "d4xe3"), new SanExample("dxe2", "d3xe2"));

    final SanPatternDescription pawnNonCapturingPromotionMovesBlack = new SanPatternDescription(
        "Pawn non capturing promotion moves", "rank must always be black promotion rank 1",
        new SanExample("d1=Q", "d2-d1 and promotion to queen"), new SanExample("d1=R", "d2-d1 and promotion to rook"),
        new SanExample("d1=N", "d2-d1 and promotion to knight"),
        new SanExample("d1=B", "d2-d1 and promotion to bishop"));

    final SanPatternDescription pawnCapturingPromotionMovesBlack = new SanPatternDescription(
        "Pawn non capturing promotion moves", "rank must always be black promotion rank 1",
        new SanExample("dxe1=Q", "d2xe1 and promotion to queen"),
        new SanExample("dxe1=R", "d2xe1 and promotion to rook"),
        new SanExample("dxe1=N", "d2xe1 and promotion to knight"),
        new SanExample("dxe1=B", "d2xe1 and promotion to bishop"));

    final SanPatternDescription queenNonCapturingMoves = new SanPatternDescription("Queen non capturing moves",
        "file not unique and/or rank not unique is possible for multiple queens",
        new SanExample("Qe5", "e.g. Qe4-e5, when file and rank unique"),
        new SanExample("Qae5", "e.g. Qa4-e5, when file unique and rank not unique - only when multiple queens"),
        new SanExample("Q2e5", "e.g. Qe2-e5, when file not unique and rank unique - only when multiple queens"),
        new SanExample("Qc3e5",
            "e.g. Qc3-e5, when file not unique and/or rank not unique - only when multiple queens"));

    final SanPatternDescription queenCapturingMoves = new SanPatternDescription("Queen capturing moves",
        "file not unique and/or rank not unique is possible for multiple queens",
        new SanExample("Qxe5", "e.g. Qe4xe5, when file and rank unique"),
        new SanExample("Qaxe5", "e.g. Qa4xe5, when file unique and rank not unique - only when multiple queens"),
        new SanExample("Q2xe5", "e.g. Qe2xe5, when file not unique and rank unique - only when multiple queens"),
        new SanExample("Qc3xe5",
            "e.g. Qc3xe5, when file not unique and/or rank not unique - only when multiple queens"));

    final SanPatternDescription rookNonCapturingMoves = new SanPatternDescription("Rook non capturing moves",
        "file not unique or rank not unique - but not both - is possible for multiple rooks",
        new SanExample("Re5", "e.g. Re4-e5, when file and rank unique"),
        new SanExample("Rae5", "e.g. Ra5-e5, when file unique and rank not unique - only when multiple rooks"),
        new SanExample("R2e5", "e.g. Re2-e5, when file not unique and rank unique - only when multiple rooks"));

    final SanPatternDescription rookCapturingMoves = new SanPatternDescription("Rook capturing moves",
        "file not unique or rank not unique - both not both - is possible for multiple rooks",
        new SanExample("Rxe5", "e.g. Re4xe5, when file and rank unique"),
        new SanExample("Raxe5", "e.g. Ra5xe5, when file unique and rank not unique - only when multiple rooks"),
        new SanExample("R2xe5", "e.g. Re2xe5, when file not unique and rank unique - only when multiple rooks"));

    final SanPatternDescription knightNonCapturingMoves = new SanPatternDescription("Knight non capturing moves",
        "file not unique and/or rank not unique is possible for multiple knights",
        new SanExample("Ne5", "e.g. Nf3-e5, when file and rank unique"),
        new SanExample("Nce5", "e.g. Nc3-e5, when file unique and rank not unique - only when multiple knights"),
        new SanExample("N4e5", "e.g. Nd4-e5, when file not unique and rank unique - only when multiple knights"),
        new SanExample("Nd3e5",
            "e.g. Nd3-e5, when file not unique and/or rank not unique - only when multiple knights"));

    final SanPatternDescription knightCapturingMoves = new SanPatternDescription("Knight capturing moves",
        "file not unique and/or rank not unique is possible for multiple knights",
        new SanExample("Nxe5", "e.g. Nf3xe5, when file and rank unique"),
        new SanExample("Ncxe5", "e.g. Nc3xe5, when file unique and rank not unique - only when multiple knights"),
        new SanExample("N4xe5", "e.g. Nd4xe5, when file not unique and rank unique - only when multiple knights"),
        new SanExample("Nd3xe5",
            "e.g. Nd3xe5, when file not unique and/or rank not unique - only when multiple knights"));

    final SanPatternDescription bishopNonCapturingMoves = new SanPatternDescription("Bishop non capturing moves",
        "file not unique or rank not unique - both not both - is possible for multiple bishops",
        new SanExample("Be5", "e.g. Bd3-e5, when file and rank unique"),
        new SanExample("B2e5", "e.g. Bh2-e5, when file unique and rank not unique - only when multiple bishops"),
        new SanExample("Bbe5", "e.g. Bb2-e5, when file not unique and rank unique - only when multiple bishops"));

    final SanPatternDescription bishopCapturingMoves = new SanPatternDescription("Bishop capturing moves",
        "file not unique or rank not unique - both not both - is possible for multiple bishops",
        new SanExample("Bxe5", "when file and rank unique"),
        new SanExample("B2xe5", "when file unique and rank not unique - only when multiple bishops"),
        new SanExample("Bbxe5", "when file not unique and rank unique - only when multiple bishops"));

    final SanPatternDescription kingNonCastlingNonCapturingMoves = new SanPatternDescription(
        "King non castling non capturing moves", "there is only one king always, so file and rank are always unique",
        new SanExample("Ke5", "e.g. Kd5-e5"));

    final SanPatternDescription kingNonCastlingCapturingMoves = new SanPatternDescription(
        "King non castling capturing moves", "there is only one king always, so file and rank are always unique",
        new SanExample("Kxe5", "e.g. Kd5xe5"));

    final SanPatternDescription kingCastlingMoves = new SanPatternDescription("King castling moves",
        "the letter O not the digit 0 must be used", new SanExample("O-O", "king-side castling"),
        new SanExample("O-O-O", "queen-side castling"));

    final List<SanPatternDescription> formatBescriptionWhite = new ArrayList<>();
    formatBescriptionWhite.add(pawnNonCapturingNonPromotionMovesWhite);
    formatBescriptionWhite.add(pawnCapturingNonPromotionMovesWhite);
    formatBescriptionWhite.add(pawnNonCapturingPromotionMovesWhite);
    formatBescriptionWhite.add(pawnCapturingPromotionMovesWhite);

    final List<SanPatternDescription> formatBescriptionBlack = new ArrayList<>();
    formatBescriptionBlack.add(pawnNonCapturingNonPromotionMovesBlack);
    formatBescriptionBlack.add(pawnCapturingNonPromotionMovesBlack);
    formatBescriptionBlack.add(pawnNonCapturingPromotionMovesBlack);
    formatBescriptionBlack.add(pawnCapturingPromotionMovesBlack);

    final List<SanPatternDescription> formatBescriptionCommon = new ArrayList<>();
    formatBescriptionCommon.add(queenNonCapturingMoves);
    formatBescriptionCommon.add(queenCapturingMoves);
    formatBescriptionCommon.add(rookNonCapturingMoves);
    formatBescriptionCommon.add(rookCapturingMoves);
    formatBescriptionCommon.add(knightNonCapturingMoves);
    formatBescriptionCommon.add(knightCapturingMoves);
    formatBescriptionCommon.add(bishopNonCapturingMoves);
    formatBescriptionCommon.add(bishopCapturingMoves);
    formatBescriptionCommon.add(kingNonCastlingNonCapturingMoves);
    formatBescriptionCommon.add(kingNonCastlingCapturingMoves);
    formatBescriptionCommon.add(kingCastlingMoves);

    formatBescriptionWhite.addAll(formatBescriptionCommon);
    formatBescriptionBlack.addAll(formatBescriptionCommon);

    System.out.println("SAN format description:");
    final var epicStatementAboutPawns = "Please note that the allowed pawn moves differ for White and Black. This regards pawn moves to eight and first rank. Pawn moves to eight rank must be white pawn promotion moves, pawn moves to first rank must be black pawn promotion moves.";
    System.out.println(epicStatementAboutPawns);
    System.out.println("");

    switch (havingMove) {
      case BLACK:
        printPatternDescriptionList(formatBescriptionBlack);
        break;
      case WHITE:
        printPatternDescriptionList(formatBescriptionWhite);
        break;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  private static void printPatternDescriptionList(List<SanPatternDescription> sanPatternList) {
    for (final SanPatternDescription pattern : sanPatternList) {
      System.out.println(calculatePatternDescription(pattern));
    }
  }

  private static String calculatePatternDescription(SanPatternDescription sanPattern) {
    final StringBuilder output = new StringBuilder();
    output.append(sanPattern.pattern());
    output.append(" (");
    output.append(sanPattern.comment());
    output.append("):\n");
    for (final SanExample example : sanPattern.sanExampleList()) {
      @SuppressWarnings("null") @NonNull final SanExample exampleNonNull = example;
      output.append(calculateExampleDescription(exampleNonNull));
      output.append("\n");
    }
    return NonNullWrapperCommon.toString(output);
  }

  private static String calculateExampleDescription(SanExample example) {
    if (example.description().length() == 0) {
      return example.san();
    }
    return example.san() + ": " + example.description();
  }
}
