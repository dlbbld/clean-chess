/*
  Local semistatic oracle runner for Miguel Ambrona's D3-Chess/CHA implementation.

  The rows with JAVA_ names project Ambrona's saturated movement variables into
  the helper shapes used by the Java Figure-8 implementation. The AMBRONA_
  rows expose native bridge values used by the shipping C++ implementation.
*/

#include "stockfish.h"
#include "util.h"

#define private public
#include "semistatic.h"
#undef private

#include <iostream>
#include <sstream>
#include <string>
#include <vector>

namespace {

const char* color_name(Color c) {
  switch (c) {
    case WHITE:
      return "WHITE";
    case BLACK:
      return "BLACK";
    default:
      return "NONE";
  }
}

const char* piece_type_name(PieceType p) {
  switch (p) {
    case PAWN:
      return "PAWN";
    case KNIGHT:
      return "KNIGHT";
    case BISHOP:
      return "BISHOP";
    case ROOK:
      return "ROOK";
    case QUEEN:
      return "QUEEN";
    case KING:
      return "KING";
    default:
      return "NONE";
  }
}

std::string square_name(Square s) {
  std::string result;
  result += static_cast<char>('a' + file_of(s));
  result += static_cast<char>('1' + rank_of(s));
  return result;
}

std::string piece_id(Position& pos, Square s) {
  const Piece piece = pos.piece_on(s);
  return std::string(color_name(color_of(piece))) + ":" + piece_type_name(type_of(piece)) + ":" + square_name(s);
}

std::string join_values(const std::vector<std::string>& values) {
  if (values.empty()) {
    return "-";
  }
  std::ostringstream result;
  for (std::size_t i = 0; i < values.size(); ++i) {
    if (i != 0) {
      result << ',';
    }
    result << values[i];
  }
  return result.str();
}

std::string format_squares(Bitboard squares) {
  std::vector<std::string> values;
  for (Square square = SQ_A1; square <= SQ_H8; ++square) {
    if (squares & square_bb(square)) {
      values.push_back(square_name(square));
    }
  }
  return join_values(values);
}

std::string format_pieces(Position& pos, Bitboard pieces) {
  std::vector<std::string> values;
  for (Square square = SQ_A1; square <= SQ_H8; ++square) {
    if (pieces & square_bb(square)) {
      values.push_back(piece_id(pos, square));
    }
  }
  return join_values(values);
}

Bitboard orthogonal_neighbours(Square square) {
  Bitboard result = 0;
  const int file = file_of(square);
  const int rank = rank_of(square);
  if (rank < 7) result |= square_bb(static_cast<Square>(square + 8));
  if (file < 7) result |= square_bb(static_cast<Square>(square + 1));
  if (rank > 0) result |= square_bb(static_cast<Square>(square - 8));
  if (file > 0) result |= square_bb(static_cast<Square>(square - 1));
  return result;
}

Bitboard region(SemiStatic::System& system, Position& pos, Square source) {
  const Piece piece = pos.piece_on(source);
  const PieceType piece_type = type_of(piece);
  const Color color = color_of(piece);

  Bitboard result = 0;
  for (Square target = SQ_A1; target <= SQ_H8; ++target) {
    if (system.variables[system.index(piece_type, color, source, target)]) {
      result |= square_bb(target);
    }
  }
  return result;
}

Bitboard attacked_region(SemiStatic::System& system, Position& pos, Square source) {
  const Piece piece = pos.piece_on(source);
  const PieceType piece_type = type_of(piece);
  const Color color = color_of(piece);
  const Bitboard source_region = region(system, pos, source);

  Bitboard result = 0;
  Square presquares[8];
  for (Square target = SQ_A1; target <= SQ_H8; ++target) {
    UTIL::unmove(presquares, piece_type, color, target);
    const int start = piece_type == PAWN ? 1 : 0;
    for (int j = start; j < 8; ++j) {
      if (presquares[j] < 0) break;
      if (source_region & square_bb(presquares[j])) {
        result |= square_bb(target);
        break;
      }
    }
  }
  return result;
}

Bitboard java_intruders(SemiStatic::System& system, Position& pos, Color winner, Bitboard loser_king_region) {
  Bitboard result = 0;
  for (Square source = SQ_A1; source <= SQ_H8; ++source) {
    const Piece piece = pos.piece_on(source);
    if (type_of(piece) == NO_PIECE_TYPE || color_of(piece) != winner) {
      continue;
    }
    if (region(system, pos, source) & loser_king_region) {
      result |= square_bb(source);
    }
  }
  return result;
}

Bitboard java_blockers(SemiStatic::System& system, Position& pos, Color winner, Square square) {
  Bitboard result = 0;
  const Bitboard adjacent = orthogonal_neighbours(square);
  for (Square source = SQ_A1; source <= SQ_H8; ++source) {
    const Piece piece = pos.piece_on(source);
    const PieceType piece_type = type_of(piece);
    if (piece_type == NO_PIECE_TYPE || color_of(piece) == winner || piece_type == KING) {
      continue;
    }
    if (region(system, pos, source) & adjacent) {
      result |= square_bb(source);
    }
  }
  return result;
}

Bitboard java_assistants(SemiStatic::System& system, Position& pos, Color winner, Square square) {
  Bitboard result = 0;
  const Bitboard adjacent = orthogonal_neighbours(square);
  for (Square source = SQ_A1; source <= SQ_H8; ++source) {
    const Piece piece = pos.piece_on(source);
    if (type_of(piece) == NO_PIECE_TYPE || color_of(piece) != winner) {
      continue;
    }
    if (attacked_region(system, pos, source) & adjacent) {
      result |= square_bb(source);
    }
  }
  return result;
}

bool semistatic_verdict(SemiStatic::System& system, Position& pos, Color winner) {
  if (MoveList<LEGAL>(pos).size() == 0) {
    return !pos.checkers() || pos.side_to_move() == winner;
  }

  for (const auto& move : MoveList<LEGAL>(pos)) {
    if (type_of(move) == ENPASSANT) {
      return false;
    }
  }

  return system.is_unwinnable(pos, winner);
}

void print_row(const std::string& fen, const std::string& winner, const std::string& kind,
               const std::string& subject, const std::string& value) {
  std::cout << fen << '\t' << winner << '\t' << kind << '\t' << subject << '\t' << value << std::endl;
}

void print_semistatic_rows(const std::string& fen, SemiStatic::System& system) {
  Position pos;
  StateInfo state;
  pos.set(fen, false, &state, Threads.main());

  system.saturate(pos);

  for (Square source = SQ_A1; source <= SQ_H8; ++source) {
    if (type_of(pos.piece_on(source)) != NO_PIECE_TYPE) {
      print_row(fen, "-", "JAVA_ATTACKED_REGION", piece_id(pos, source),
                format_squares(attacked_region(system, pos, source)));
    }
  }

  for (Color winner : {WHITE, BLACK}) {
    const std::string winner_name = color_name(winner);
    const Bitboard loser_king_region = system.king_region(pos, ~winner);
    print_row(fen, winner_name, "VERDICT", "-", semistatic_verdict(system, pos, winner) ? "UNWINNABLE" : "POSSIBLY_WINNABLE");
    print_row(fen, winner_name, "JAVA_LOSER_KING_REGION", "-", format_squares(loser_king_region));
    print_row(fen, winner_name, "JAVA_INTRUDERS", "-", format_pieces(pos, java_intruders(system, pos, winner, loser_king_region)));

    const Bitboard ambrona_visitors = system.visitors(pos, loser_king_region, winner, true) & ~pos.pieces(winner, KING);
    print_row(fen, winner_name, "AMBRONA_VISITORS_EXPANDED", "-", format_pieces(pos, ambrona_visitors));

    for (Square square = SQ_A1; square <= SQ_H8; ++square) {
      if (!(loser_king_region & square_bb(square))) {
        continue;
      }
      const std::string subject = square_name(square);
      print_row(fen, winner_name, "JAVA_BLOCKERS", subject,
                format_pieces(pos, java_blockers(system, pos, winner, square)));
      print_row(fen, winner_name, "JAVA_ASSISTANTS", subject,
                format_pieces(pos, java_assistants(system, pos, winner, square)));
    }
  }

  std::cout << "END\t" << fen << std::endl;
}

}  // namespace

int main(int argc, char* argv[]) {
  init_stockfish();
  CommandLine::init(argc, argv);

  SemiStatic::System system;
  system.init();

  std::string fen;
  while (std::getline(std::cin, fen)) {
    if (!fen.empty() && fen.back() == '\r') {
      fen.pop_back();
    }
    if (!fen.empty()) {
      print_semistatic_rows(fen, system);
    }
  }

  Threads.stop = true;
  Threads.set(0);
  return 0;
}
