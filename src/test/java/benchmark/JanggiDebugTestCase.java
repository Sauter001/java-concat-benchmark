package benchmark;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JanggiDebugTestCase {

    private JanggiConcatBenchmark concatBenchmark;
    private JanggiNotationBenchmark notationBenchmark;

    @BeforeEach
    void setUp() {
        concatBenchmark = new JanggiConcatBenchmark();
        // JanggiConcatBenchmark는 static 블록이나 필드 초기화로 준비됨

        notationBenchmark = new JanggiNotationBenchmark();
        notationBenchmark.setMoveCount(10); // 테스트용 moveCount 설정
        notationBenchmark.setup();
    }

    @Test
    @DisplayName("보드 렌더링 방식 결과 비교 및 디버깅")
    void debugConcatBenchmarks() {
        String sbResult = concatBenchmark.composeBoardWithStringBuilder();
        String plusResult = concatBenchmark.composeBoardWithPlusDoubleLoop();
        String formatResult = concatBenchmark.composeBoardWithFormatPure();

        System.out.println("--- StringBuilder Result (Partial) ---");
        System.out.println(sbResult.substring(0, Math.min(sbResult.length(), 200)));

        // 모든 방식의 결과 문자열이 동일한지 검증
        assertEquals(sbResult, plusResult, "StringBuilder와 PlusDoubleLoop의 결과가 다릅니다.");
        assertEquals(sbResult, formatResult, "StringBuilder와 FormatPure의 결과가 다릅니다.");
    }

    @Test
    @DisplayName("기보 생성 방식 결과 비교 및 디버깅")
    void debugNotationBenchmarks() {
        String plusSingle = notationBenchmark.singleNotation_plus();
        String builderSingle = notationBenchmark.singleNotation_builder();
        
        assertEquals(plusSingle, builderSingle, "단일 기보 생성 결과가 다릅니다.");

        String plusFull = notationBenchmark.fullNotation_plus();
        String builderFull = notationBenchmark.fullNotation_builder();
        
        System.out.println("--- Full Notation Result (Partial) ---");
        System.out.println(plusFull.substring(0, Math.min(plusFull.length(), 200)));

        assertEquals(plusFull, builderFull, "전체 기보 생성 결과가 다릅니다.");
    }
}
