package model;

import model.coordinate.Position;
import model.piece.Piece;

import java.util.HashMap;
import java.util.Map;

public class Board {

    public static final int BOARD_ROW = 10;
    public static final int BOARD_COL = 9;

    private final Map<Position, Piece> board;

    public Board(Map<Position, Piece> board) {
        this.board = new HashMap<>(board);
    }

    public Map<Position, Piece> board() {
        return Map.copyOf(board);
    }
}
