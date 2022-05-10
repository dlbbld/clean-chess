package com.dlb.chess.illegal.model;

import com.dlb.chess.board.enums.Square;

// empty
// one or more such pieces wrongly placed on the board?
// yes - advice to move with list (ordered by distance)
// no - advice to put according piece on the square

// not empty
// one more such pieces missing on the board?
// yes - advice to move with list (ordered by distance)
// no - advice to remove piece from the board

public interface RestorePositionAdvice {
  Square square();

}
