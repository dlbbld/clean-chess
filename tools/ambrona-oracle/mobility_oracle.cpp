/*
  Local mobility oracle runner for Miguel Ambrona's D3-Chess/CHA implementation.

  The public CHA API only exposes the semistatic unwinnability verdict. For
  mobility comparisons we need the saturated movement variables themselves, so
  this diagnostic runner makes the System storage visible in this translation
  unit and prints the reachable target squares for each piece in each FEN.
*/

#include "stockfish.h"

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

std::string join_square_names(const std::vector<std::string>& square_names) {
  std::ostringstream result;
  for (std::size_t i = 0; i < square_names.size(); ++i) {
    if (i != 0) {
      result << ',';
    }
    result << square_names[i];
  }
  return result.str();
}

void print_mobility_rows(const std::string& fen, SemiStatic::System& system) {
  Position pos;
  StateInfo state;
  pos.set(fen, false, &state, Threads.main());

  system.saturate(pos);

  for (Square source = SQ_A1; source <= SQ_H8; ++source) {
    const Piece piece = pos.piece_on(source);
    const PieceType piece_type = type_of(piece);
    if (piece_type == NO_PIECE_TYPE) {
      continue;
    }

    const Color color = color_of(piece);
    std::vector<std::string> targets;
    for (Square target = SQ_A1; target <= SQ_H8; ++target) {
      if (system.variables[system.index(piece_type, color, source, target)]) {
        targets.push_back(square_name(target));
      }
    }

    std::cout << fen << '\t' << color_name(color) << '\t' << piece_type_name(piece_type) << '\t'
              << square_name(source) << '\t' << join_square_names(targets) << std::endl;
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
      print_mobility_rows(fen, system);
    }
  }

  Threads.stop = true;
  Threads.set(0);
  return 0;
}
