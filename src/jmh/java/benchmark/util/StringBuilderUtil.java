package benchmark.util;

import model.Board;
import model.coordinate.Position;
import model.piece.Piece;

import java.util.Map;

import static formatter.BoardFormatter.COL_NUM;
import static formatter.BoardFormatter.ROW_NUM;
import static formatter.BoardFormatter.SPACE;
import static formatter.BoardFormatter.VERTICAL_LINE;
import static formatter.BoardFormatter.formatSymbol;

public class StringBuilderUtil {
    private StringBuilderUtil() {}

    public static void appendColIndex(StringBuilder outputBuilder) {
        outputBuilder.append(System.lineSeparator())
                .append(SPACE).append(SPACE).append(SPACE);
        for (String column : COL_NUM) {
            outputBuilder.append(SPACE).append(column);
        }
        outputBuilder.append(System.lineSeparator());
    }

    public static void appendRows(StringBuilder outputBuilder, Map<Position, Piece> board) {
        for (int row = 0; row < Board.BOARD_ROW; row++) {
            appendRow(outputBuilder, board, row);
        }
    }

    private static void appendRow(StringBuilder outputBuilder, Map<Position, Piece> board, int row) {
        outputBuilder.append(ROW_NUM[row]).append(" ").append(VERTICAL_LINE);
        for (int col = 0; col < Board.BOARD_COL; col++) {
            Piece piece = board.get(new Position(row, col));
            outputBuilder.append(SPACE).append(formatSymbol(piece));
        }
        outputBuilder.append(SPACE).append(VERTICAL_LINE).append(System.lineSeparator());
    }
}
