package benchmark;

import formation.FormationFactory;
import formation.JanggiFormation;
import model.Board;
import model.BoardFactory;
import model.coordinate.Position;
import model.piece.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static formatter.BoardFormatter.formatSymbol;

/**
 * 기보(棋譜) 생성 벤치마크
 * <p>
 * 시나리오: 게임 종료 후 전체 이동 기록을 문자열로 생성
 * 형식: "車 (0,0) → (0,1)"
 * <p>
 * moveCount 파라미터로 n을 변화시켜 O(n) vs O(n²) 차이를 관찰.
 * 보드 렌더링(고정 크기)과 달리 n에 비례하는 결합이 발생.
 */
public class JanggiNotationBenchmark {

    private int moveCount;

    private List<MoveRecord> moves;

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    public void setup() {
        // 실제 보드에서 기물 심볼을 가져와서 가상 이동 기록 생성
        Map<Position, Piece> formation = FormationFactory.generateFormation(
                JanggiFormation.MA_SANG_MA_SANG, JanggiFormation.MA_SANG_MA_SANG);
        Board board = BoardFactory.generatePieces(formation);
        Map<Position, Piece> boardMap = board.board();

        // 보드에 있는 기물들의 심볼 목록
        List<String> symbols = new ArrayList<>();
        for (Map.Entry<Position, Piece> entry : boardMap.entrySet()) {
            symbols.add(formatSymbol(entry.getValue()));
        }

        ThreadLocalRandom rng = ThreadLocalRandom.current();
        moves = new ArrayList<>(moveCount);
        for (int i = 0; i < moveCount; i++) {
            String symbol = symbols.get(rng.nextInt(symbols.size()));
            int fromRow = rng.nextInt(10);
            int fromCol = rng.nextInt(9);
            int toRow = rng.nextInt(10);
            int toCol = rng.nextInt(9);
            moves.add(new MoveRecord(symbol, fromRow, fromCol, toRow, toCol));
        }
    }

    // ═══════════════════════════════════════
    // 단일 표현식: 한 수의 기보 생성
    // ═══════════════════════════════════════

    public String singleNotation_plus() {
        MoveRecord m = moves.getFirst();
        return m.toNotationPlus();
    }

    public String singleNotation_builder() {
        MoveRecord m = moves.getFirst();
        StringBuilder sb = new StringBuilder();
        m.appendTo(sb);
        return sb.toString();
    }

    public String singleNotation_format() {
        MoveRecord m = moves.getFirst();
        return m.toNotationFormat();
    }

    public String singleNotation_formatted() {
        MoveRecord m = moves.getFirst();
        return m.toNotationFormatted();
    }

    // ═══════════════════════════════════════
    // 루프: 전체 기보 생성 (n수)
    // ═══════════════════════════════════════

    /**
     * + 연산 (루프 내)
     * 매 반복: result += ... → O(n²)
     */
    public String fullNotation_plus() {
        String result = "";
        for (int i = 0; i < moves.size(); i++) {
            result += (i + 1) + ". " + moves.get(i).toNotationPlus() + "\n";
        }
        return result;
    }

    /**
     * StringBuilder (루프 내)
     * 단일 버퍼에 append → O(n)
     */
    public String fullNotation_builder() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < moves.size(); i++) {
            sb.append(i + 1).append(". ");
            moves.get(i).appendTo(sb);
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * StringBuilder + 초기 용량 지정
     * 한 수당 ~25자 예상
     */
    public String fullNotation_builderSized() {
        StringBuilder sb = new StringBuilder(moveCount * 25);
        for (int i = 0; i < moves.size(); i++) {
            sb.append(i + 1).append(". ");
            moves.get(i).appendTo(sb);
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * String.format (루프 내)
     * 매 반복: 포맷 파싱 + Object[] + 오토박싱
     */
    public String fullNotation_format() {
        StringBuilder sb = new StringBuilder(moveCount * 25);
        for (int i = 0; i < moves.size(); i++) {
            sb.append(String.format("%d. %s%n", i + 1, moves.get(i).toNotationFormat()));
        }
        return sb.toString();
    }

    /**
     * formatted (루프 내, Java 15+)
     */
    public String fullNotation_formatted() {
        StringBuilder sb = new StringBuilder(moveCount * 25);
        for (int i = 0; i < moves.size(); i++) {
            sb.append("%d. %s%n".formatted(i + 1, moves.get(i).toNotationFormatted()));
        }
        return sb.toString();
    }
}
