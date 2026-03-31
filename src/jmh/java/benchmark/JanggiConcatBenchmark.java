package benchmark;

import benchmark.util.StringBuilderUtil;
import formation.FormationFactory;
import formation.JanggiFormation;
import formatter.BoardFormatter;
import model.Board;
import model.BoardFactory;
import model.coordinate.Position;
import model.piece.Piece;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static formatter.BoardFormatter.COL_NUM;
import static formatter.BoardFormatter.ROW_NUM;
import static formatter.BoardFormatter.SPACE;
import static formatter.BoardFormatter.VERTICAL_LINE;
import static formatter.BoardFormatter.formatSymbol;

@BenchmarkMode({Mode.AverageTime, Mode.Throughput})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(3)
@State(Scope.Benchmark)
public class JanggiConcatBenchmark {
    private static final JanggiFormation HAN_FORMATION = JanggiFormation.MA_SANG_MA_SANG;
    private static final JanggiFormation CHO_FORMATION = JanggiFormation.MA_SANG_MA_SANG;
    private static final Board board;

    static {
        Map<Position, Piece> formation = FormationFactory.generateFormation(HAN_FORMATION, CHO_FORMATION);
        board = BoardFactory.generatePieces(formation);
    }

    // === 단일 표현식 벤치마크 (루프 없이 보드 전체 unroll) ===

    // 단일 표현식: + 연산 (StringConcatFactory 최적화 대상)
    @Benchmark
    public String composeBoardWithPlusSingleExpression() {
        Map<Position, Piece> boardMap = board.board();
        String border = BoardFormatter.formatHorizon(Board.BOARD_COL);
        String ln = System.lineSeparator();
        return ln
                + SPACE + SPACE + SPACE
                + SPACE + COL_NUM[0] + SPACE + COL_NUM[1] + SPACE + COL_NUM[2]
                + SPACE + COL_NUM[3] + SPACE + COL_NUM[4] + SPACE + COL_NUM[5]
                + SPACE + COL_NUM[6] + SPACE + COL_NUM[7] + SPACE + COL_NUM[8]
                + ln + border + ln
                + ROW_NUM[0] + " " + VERTICAL_LINE
                + SPACE + formatSymbol(boardMap.get(new Position(0, 0)))
                + SPACE + formatSymbol(boardMap.get(new Position(0, 1)))
                + SPACE + formatSymbol(boardMap.get(new Position(0, 2)))
                + SPACE + formatSymbol(boardMap.get(new Position(0, 3)))
                + SPACE + formatSymbol(boardMap.get(new Position(0, 4)))
                + SPACE + formatSymbol(boardMap.get(new Position(0, 5)))
                + SPACE + formatSymbol(boardMap.get(new Position(0, 6)))
                + SPACE + formatSymbol(boardMap.get(new Position(0, 7)))
                + SPACE + formatSymbol(boardMap.get(new Position(0, 8)))
                + SPACE + VERTICAL_LINE + ln
                + ROW_NUM[1] + " " + VERTICAL_LINE
                + SPACE + formatSymbol(boardMap.get(new Position(1, 0)))
                + SPACE + formatSymbol(boardMap.get(new Position(1, 1)))
                + SPACE + formatSymbol(boardMap.get(new Position(1, 2)))
                + SPACE + formatSymbol(boardMap.get(new Position(1, 3)))
                + SPACE + formatSymbol(boardMap.get(new Position(1, 4)))
                + SPACE + formatSymbol(boardMap.get(new Position(1, 5)))
                + SPACE + formatSymbol(boardMap.get(new Position(1, 6)))
                + SPACE + formatSymbol(boardMap.get(new Position(1, 7)))
                + SPACE + formatSymbol(boardMap.get(new Position(1, 8)))
                + SPACE + VERTICAL_LINE + ln
                + ROW_NUM[2] + " " + VERTICAL_LINE
                + SPACE + formatSymbol(boardMap.get(new Position(2, 0)))
                + SPACE + formatSymbol(boardMap.get(new Position(2, 1)))
                + SPACE + formatSymbol(boardMap.get(new Position(2, 2)))
                + SPACE + formatSymbol(boardMap.get(new Position(2, 3)))
                + SPACE + formatSymbol(boardMap.get(new Position(2, 4)))
                + SPACE + formatSymbol(boardMap.get(new Position(2, 5)))
                + SPACE + formatSymbol(boardMap.get(new Position(2, 6)))
                + SPACE + formatSymbol(boardMap.get(new Position(2, 7)))
                + SPACE + formatSymbol(boardMap.get(new Position(2, 8)))
                + SPACE + VERTICAL_LINE + ln
                + ROW_NUM[3] + " " + VERTICAL_LINE
                + SPACE + formatSymbol(boardMap.get(new Position(3, 0)))
                + SPACE + formatSymbol(boardMap.get(new Position(3, 1)))
                + SPACE + formatSymbol(boardMap.get(new Position(3, 2)))
                + SPACE + formatSymbol(boardMap.get(new Position(3, 3)))
                + SPACE + formatSymbol(boardMap.get(new Position(3, 4)))
                + SPACE + formatSymbol(boardMap.get(new Position(3, 5)))
                + SPACE + formatSymbol(boardMap.get(new Position(3, 6)))
                + SPACE + formatSymbol(boardMap.get(new Position(3, 7)))
                + SPACE + formatSymbol(boardMap.get(new Position(3, 8)))
                + SPACE + VERTICAL_LINE + ln
                + ROW_NUM[4] + " " + VERTICAL_LINE
                + SPACE + formatSymbol(boardMap.get(new Position(4, 0)))
                + SPACE + formatSymbol(boardMap.get(new Position(4, 1)))
                + SPACE + formatSymbol(boardMap.get(new Position(4, 2)))
                + SPACE + formatSymbol(boardMap.get(new Position(4, 3)))
                + SPACE + formatSymbol(boardMap.get(new Position(4, 4)))
                + SPACE + formatSymbol(boardMap.get(new Position(4, 5)))
                + SPACE + formatSymbol(boardMap.get(new Position(4, 6)))
                + SPACE + formatSymbol(boardMap.get(new Position(4, 7)))
                + SPACE + formatSymbol(boardMap.get(new Position(4, 8)))
                + SPACE + VERTICAL_LINE + ln
                + ROW_NUM[5] + " " + VERTICAL_LINE
                + SPACE + formatSymbol(boardMap.get(new Position(5, 0)))
                + SPACE + formatSymbol(boardMap.get(new Position(5, 1)))
                + SPACE + formatSymbol(boardMap.get(new Position(5, 2)))
                + SPACE + formatSymbol(boardMap.get(new Position(5, 3)))
                + SPACE + formatSymbol(boardMap.get(new Position(5, 4)))
                + SPACE + formatSymbol(boardMap.get(new Position(5, 5)))
                + SPACE + formatSymbol(boardMap.get(new Position(5, 6)))
                + SPACE + formatSymbol(boardMap.get(new Position(5, 7)))
                + SPACE + formatSymbol(boardMap.get(new Position(5, 8)))
                + SPACE + VERTICAL_LINE + ln
                + ROW_NUM[6] + " " + VERTICAL_LINE
                + SPACE + formatSymbol(boardMap.get(new Position(6, 0)))
                + SPACE + formatSymbol(boardMap.get(new Position(6, 1)))
                + SPACE + formatSymbol(boardMap.get(new Position(6, 2)))
                + SPACE + formatSymbol(boardMap.get(new Position(6, 3)))
                + SPACE + formatSymbol(boardMap.get(new Position(6, 4)))
                + SPACE + formatSymbol(boardMap.get(new Position(6, 5)))
                + SPACE + formatSymbol(boardMap.get(new Position(6, 6)))
                + SPACE + formatSymbol(boardMap.get(new Position(6, 7)))
                + SPACE + formatSymbol(boardMap.get(new Position(6, 8)))
                + SPACE + VERTICAL_LINE + ln
                + ROW_NUM[7] + " " + VERTICAL_LINE
                + SPACE + formatSymbol(boardMap.get(new Position(7, 0)))
                + SPACE + formatSymbol(boardMap.get(new Position(7, 1)))
                + SPACE + formatSymbol(boardMap.get(new Position(7, 2)))
                + SPACE + formatSymbol(boardMap.get(new Position(7, 3)))
                + SPACE + formatSymbol(boardMap.get(new Position(7, 4)))
                + SPACE + formatSymbol(boardMap.get(new Position(7, 5)))
                + SPACE + formatSymbol(boardMap.get(new Position(7, 6)))
                + SPACE + formatSymbol(boardMap.get(new Position(7, 7)))
                + SPACE + formatSymbol(boardMap.get(new Position(7, 8)))
                + SPACE + VERTICAL_LINE + ln
                + ROW_NUM[8] + " " + VERTICAL_LINE
                + SPACE + formatSymbol(boardMap.get(new Position(8, 0)))
                + SPACE + formatSymbol(boardMap.get(new Position(8, 1)))
                + SPACE + formatSymbol(boardMap.get(new Position(8, 2)))
                + SPACE + formatSymbol(boardMap.get(new Position(8, 3)))
                + SPACE + formatSymbol(boardMap.get(new Position(8, 4)))
                + SPACE + formatSymbol(boardMap.get(new Position(8, 5)))
                + SPACE + formatSymbol(boardMap.get(new Position(8, 6)))
                + SPACE + formatSymbol(boardMap.get(new Position(8, 7)))
                + SPACE + formatSymbol(boardMap.get(new Position(8, 8)))
                + SPACE + VERTICAL_LINE + ln
                + ROW_NUM[9] + " " + VERTICAL_LINE
                + SPACE + formatSymbol(boardMap.get(new Position(9, 0)))
                + SPACE + formatSymbol(boardMap.get(new Position(9, 1)))
                + SPACE + formatSymbol(boardMap.get(new Position(9, 2)))
                + SPACE + formatSymbol(boardMap.get(new Position(9, 3)))
                + SPACE + formatSymbol(boardMap.get(new Position(9, 4)))
                + SPACE + formatSymbol(boardMap.get(new Position(9, 5)))
                + SPACE + formatSymbol(boardMap.get(new Position(9, 6)))
                + SPACE + formatSymbol(boardMap.get(new Position(9, 7)))
                + SPACE + formatSymbol(boardMap.get(new Position(9, 8)))
                + SPACE + VERTICAL_LINE + ln
                + border + ln;
    }

    // 단일 표현식: StringBuilder (루프 없이 전부 append)
    @Benchmark
    public String composeBoardWithStringBuilderSingleExpression() {
        Map<Position, Piece> boardMap = board.board();
        String border = BoardFormatter.formatHorizon(Board.BOARD_COL);
        String ln = System.lineSeparator();
        return new StringBuilder()
                .append(ln)
                .append(SPACE).append(SPACE).append(SPACE)
                .append(SPACE).append(COL_NUM[0]).append(SPACE).append(COL_NUM[1]).append(SPACE).append(COL_NUM[2])
                .append(SPACE).append(COL_NUM[3]).append(SPACE).append(COL_NUM[4]).append(SPACE).append(COL_NUM[5])
                .append(SPACE).append(COL_NUM[6]).append(SPACE).append(COL_NUM[7]).append(SPACE).append(COL_NUM[8])
                .append(ln).append(border).append(ln)
                .append(ROW_NUM[0]).append(" ").append(VERTICAL_LINE)
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(0, 0))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(0, 1))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(0, 2))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(0, 3))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(0, 4))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(0, 5))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(0, 6))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(0, 7))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(0, 8))))
                .append(SPACE).append(VERTICAL_LINE).append(ln)
                .append(ROW_NUM[1]).append(" ").append(VERTICAL_LINE)
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(1, 0))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(1, 1))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(1, 2))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(1, 3))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(1, 4))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(1, 5))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(1, 6))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(1, 7))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(1, 8))))
                .append(SPACE).append(VERTICAL_LINE).append(ln)
                .append(ROW_NUM[2]).append(" ").append(VERTICAL_LINE)
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(2, 0))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(2, 1))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(2, 2))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(2, 3))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(2, 4))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(2, 5))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(2, 6))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(2, 7))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(2, 8))))
                .append(SPACE).append(VERTICAL_LINE).append(ln)
                .append(ROW_NUM[3]).append(" ").append(VERTICAL_LINE)
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(3, 0))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(3, 1))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(3, 2))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(3, 3))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(3, 4))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(3, 5))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(3, 6))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(3, 7))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(3, 8))))
                .append(SPACE).append(VERTICAL_LINE).append(ln)
                .append(ROW_NUM[4]).append(" ").append(VERTICAL_LINE)
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(4, 0))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(4, 1))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(4, 2))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(4, 3))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(4, 4))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(4, 5))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(4, 6))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(4, 7))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(4, 8))))
                .append(SPACE).append(VERTICAL_LINE).append(ln)
                .append(ROW_NUM[5]).append(" ").append(VERTICAL_LINE)
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(5, 0))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(5, 1))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(5, 2))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(5, 3))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(5, 4))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(5, 5))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(5, 6))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(5, 7))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(5, 8))))
                .append(SPACE).append(VERTICAL_LINE).append(ln)
                .append(ROW_NUM[6]).append(" ").append(VERTICAL_LINE)
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(6, 0))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(6, 1))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(6, 2))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(6, 3))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(6, 4))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(6, 5))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(6, 6))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(6, 7))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(6, 8))))
                .append(SPACE).append(VERTICAL_LINE).append(ln)
                .append(ROW_NUM[7]).append(" ").append(VERTICAL_LINE)
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(7, 0))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(7, 1))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(7, 2))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(7, 3))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(7, 4))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(7, 5))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(7, 6))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(7, 7))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(7, 8))))
                .append(SPACE).append(VERTICAL_LINE).append(ln)
                .append(ROW_NUM[8]).append(" ").append(VERTICAL_LINE)
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(8, 0))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(8, 1))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(8, 2))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(8, 3))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(8, 4))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(8, 5))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(8, 6))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(8, 7))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(8, 8))))
                .append(SPACE).append(VERTICAL_LINE).append(ln)
                .append(ROW_NUM[9]).append(" ").append(VERTICAL_LINE)
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(9, 0))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(9, 1))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(9, 2))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(9, 3))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(9, 4))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(9, 5))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(9, 6))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(9, 7))))
                .append(SPACE).append(formatSymbol(boardMap.get(new Position(9, 8))))
                .append(SPACE).append(VERTICAL_LINE).append(ln)
                .append(border).append(ln)
                .toString();
    }

    // === 보드 전체 렌더링 벤치마크 (루프) ===

    // (1) + 이중 루프: 안쪽/바깥 루프 모두 += 사용
    @Benchmark
    public String composeBoardWithPlusDoubleLoop() {
        Map<Position, Piece> boardMap = board.board();
        String border = BoardFormatter.formatHorizon(Board.BOARD_COL);

        String result = "";
        result += System.lineSeparator();
        result += SPACE + SPACE + SPACE;
        for (String column : COL_NUM) {
            result += SPACE + column;
        }
        result += System.lineSeparator();
        result += border + System.lineSeparator();
        for (int row = 0; row < Board.BOARD_ROW; row++) {
            result += ROW_NUM[row] + " " + VERTICAL_LINE;
            for (int col = 0; col < Board.BOARD_COL; col++) {
                Piece piece = boardMap.get(new Position(row, col));
                result += SPACE + formatSymbol(piece);
            }
            result += SPACE + VERTICAL_LINE + System.lineSeparator();
        }
        result += border + System.lineSeparator();
        return result;
    }

    // (2) + 행 단위: 안쪽은 단일 표현식(unroll), 바깥은 +=
    @Benchmark
    public String composeBoardWithPlusRowUnit() {
        Map<Position, Piece> boardMap = board.board();
        String border = BoardFormatter.formatHorizon(Board.BOARD_COL);

        String result = "";
        result += System.lineSeparator();
        result += SPACE + SPACE + SPACE
                + SPACE + COL_NUM[0] + SPACE + COL_NUM[1] + SPACE + COL_NUM[2]
                + SPACE + COL_NUM[3] + SPACE + COL_NUM[4] + SPACE + COL_NUM[5]
                + SPACE + COL_NUM[6] + SPACE + COL_NUM[7] + SPACE + COL_NUM[8];
        result += System.lineSeparator();
        result += border + System.lineSeparator();
        for (int row = 0; row < Board.BOARD_ROW; row++) {
            result += ROW_NUM[row] + " " + VERTICAL_LINE
                    + SPACE + formatSymbol(boardMap.get(new Position(row, 0)))
                    + SPACE + formatSymbol(boardMap.get(new Position(row, 1)))
                    + SPACE + formatSymbol(boardMap.get(new Position(row, 2)))
                    + SPACE + formatSymbol(boardMap.get(new Position(row, 3)))
                    + SPACE + formatSymbol(boardMap.get(new Position(row, 4)))
                    + SPACE + formatSymbol(boardMap.get(new Position(row, 5)))
                    + SPACE + formatSymbol(boardMap.get(new Position(row, 6)))
                    + SPACE + formatSymbol(boardMap.get(new Position(row, 7)))
                    + SPACE + formatSymbol(boardMap.get(new Position(row, 8)))
                    + SPACE + VERTICAL_LINE + System.lineSeparator();
        }
        result += border + System.lineSeparator();
        return result;
    }

    // (3) StringBuilder
    @Benchmark
    public String composeBoardWithStringBuilder() {
        Map<Position, Piece> boardMap = board.board();
        StringBuilder outputBuilder = new StringBuilder();
        String border = BoardFormatter.formatHorizon(Board.BOARD_COL);
        StringBuilderUtil.appendColIndex(outputBuilder);
        outputBuilder.append(border).append(System.lineSeparator());
        StringBuilderUtil.appendRows(outputBuilder, boardMap);
        outputBuilder.append(border).append(System.lineSeparator());

        return outputBuilder.toString();
    }

    // (4) String.join: 좌표 헤더까지 join으로 조립
    @Benchmark
    public String composeBoardWithStringJoinExtreme() {
        Map<Position, Piece> boardMap = board.board();
        String border = BoardFormatter.formatHorizon(Board.BOARD_COL);

        String colHeader = String.join("",
                SPACE, SPACE, SPACE,
                SPACE, COL_NUM[0], SPACE, COL_NUM[1], SPACE, COL_NUM[2],
                SPACE, COL_NUM[3], SPACE, COL_NUM[4], SPACE, COL_NUM[5],
                SPACE, COL_NUM[6], SPACE, COL_NUM[7], SPACE, COL_NUM[8]);

        String[] rows = new String[Board.BOARD_ROW];
        for (int row = 0; row < Board.BOARD_ROW; row++) {
            rows[row] = String.join("",
                    ROW_NUM[row], " ", VERTICAL_LINE,
                    SPACE, formatSymbol(boardMap.get(new Position(row, 0))),
                    SPACE, formatSymbol(boardMap.get(new Position(row, 1))),
                    SPACE, formatSymbol(boardMap.get(new Position(row, 2))),
                    SPACE, formatSymbol(boardMap.get(new Position(row, 3))),
                    SPACE, formatSymbol(boardMap.get(new Position(row, 4))),
                    SPACE, formatSymbol(boardMap.get(new Position(row, 5))),
                    SPACE, formatSymbol(boardMap.get(new Position(row, 6))),
                    SPACE, formatSymbol(boardMap.get(new Position(row, 7))),
                    SPACE, formatSymbol(boardMap.get(new Position(row, 8))),
                    SPACE, VERTICAL_LINE);
        }

        return String.join(System.lineSeparator(),
                "", colHeader, border,
                String.join(System.lineSeparator(), rows),
                border, "");
    }

    // (5) String.format: 좌표 헤더까지 format으로, + 없이
    @Benchmark
    public String composeBoardWithFormatExtreme() {
        Map<Position, Piece> boardMap = board.board();
        String border = BoardFormatter.formatHorizon(Board.BOARD_COL);

        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator());
        sb.append(String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s",
                SPACE, SPACE, SPACE,
                SPACE, COL_NUM[0], SPACE, COL_NUM[1], SPACE, COL_NUM[2],
                SPACE, COL_NUM[3], SPACE, COL_NUM[4], SPACE, COL_NUM[5]));
        sb.append(String.format("%s%s%s%s%s%s%s",
                SPACE, COL_NUM[6], SPACE, COL_NUM[7], SPACE, COL_NUM[8],
                System.lineSeparator()));
        sb.append(border).append(System.lineSeparator());
        for (int row = 0; row < Board.BOARD_ROW; row++) {
            sb.append(String.format("%s %s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s",
                    ROW_NUM[row], VERTICAL_LINE,
                    SPACE, formatSymbol(boardMap.get(new Position(row, 0))),
                    SPACE, formatSymbol(boardMap.get(new Position(row, 1))),
                    SPACE, formatSymbol(boardMap.get(new Position(row, 2))),
                    SPACE, formatSymbol(boardMap.get(new Position(row, 3))),
                    SPACE, formatSymbol(boardMap.get(new Position(row, 4))),
                    SPACE, formatSymbol(boardMap.get(new Position(row, 5))),
                    SPACE, formatSymbol(boardMap.get(new Position(row, 6))),
                    SPACE, formatSymbol(boardMap.get(new Position(row, 7))),
                    SPACE, formatSymbol(boardMap.get(new Position(row, 8))),
                    SPACE, VERTICAL_LINE, System.lineSeparator()));
        }
        sb.append(border).append(System.lineSeparator());
        return sb.toString();
    }
}
