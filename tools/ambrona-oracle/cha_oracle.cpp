/*
  Local oracle runner for Miguel Ambrona's D3-Chess/CHA implementation.

  This program is intentionally tiny: it links against an existing D3-Chess checkout
  and prints the full and quick unwinnability verdicts for both sides for every FEN
  read from stdin. The stock CHA CLI suppresses non-unwinnable quick results, so it
  cannot generate the four-verdict oracle file directly.
*/

#include "cha.h"
#include "dynamic.h"
#include "stockfish.h"

#include <iostream>
#include <string>

namespace {

constexpr uint64_t NODE_LIMIT = 500000;

const char* full_result(DYNAMIC::SearchResult result) {
  switch (result) {
    case DYNAMIC::WINNABLE:
      return "WINNABLE";
    case DYNAMIC::UNWINNABLE:
      return "UNWINNABLE";
    case DYNAMIC::UNDETERMINED:
      return "UNDETERMINED";
  }
  return "UNDETERMINED";
}

const char* quick_result(DYNAMIC::SearchResult result) {
  switch (result) {
    case DYNAMIC::WINNABLE:
      return "WINNABLE";
    case DYNAMIC::UNWINNABLE:
      return "UNWINNABLE";
    case DYNAMIC::UNDETERMINED:
      return "POSSIBLY_WINNABLE";
  }
  return "POSSIBLY_WINNABLE";
}

DYNAMIC::SearchResult analyze_full(const std::string& fen, Color winner) {
  Position pos;
  StateInfo state;
  pos.set(fen, false, &state, Threads.main());

  DYNAMIC::Search search;
  search.set_limit(NODE_LIMIT);
  search.set_winner(winner);
  return DYNAMIC::full_analysis(pos, search);
}

DYNAMIC::SearchResult analyze_quick(const std::string& fen, Color winner) {
  Position pos;
  StateInfo state;
  pos.set(fen, false, &state, Threads.main());

  DYNAMIC::Search search;
  search.set_limit(NODE_LIMIT);
  search.set_winner(winner);
  return DYNAMIC::quick_analysis(pos, search, false);
}

}  // namespace

int main(int argc, char* argv[]) {
  init_stockfish();
  CommandLine::init(argc, argv);
  CHA::init();

  std::string fen;
  while (std::getline(std::cin, fen)) {
    if (!fen.empty() && fen.back() == '\r') {
      fen.pop_back();
    }
    if (fen.empty()) {
      continue;
    }

    const auto fullWhite = analyze_full(fen, WHITE);
    const auto fullBlack = analyze_full(fen, BLACK);
    const auto quickWhite = analyze_quick(fen, WHITE);
    const auto quickBlack = analyze_quick(fen, BLACK);

    std::cout << fen << '\t' << full_result(fullWhite) << '\t' << full_result(fullBlack) << '\t'
              << quick_result(quickWhite) << '\t' << quick_result(quickBlack) << std::endl;
  }

  Threads.stop = true;
  Threads.set(0);
  return 0;
}
