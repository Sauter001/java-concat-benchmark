# JMH 벤치마크 결과 - 장기 보드 렌더링 문자열 결합

## 실험 환경

- JDK: OpenJDK 21
- GC: G1GC (기본값)
- JMH: 1.37
- Warmup: 5 iterations, 1s each
- Measurement: 5 iterations, 1s each
- Fork: 3
- Profiler: gc

---

## 1차 실행 결과 (시나리오 A: 보드 렌더링)

### Throughput (ops/ms, 높을수록 좋음)

| Benchmark              | Score   | Error      | B/op   |
|------------------------|---------|------------|--------|
| StringBuilder (루프)     | 186.220 | +/- 8.484  | 9,200  |
| + 행 단위                 | 141.538 | +/- 5.414  | 14,320 |
| + 이중 루프                | 110.219 | +/- 7.294  | 80,792 |
| StringBuilder (단일 표현식) | 120.531 | +/- 8.178  | 11,168 |
| String.join            | 110.179 | +/- 15.802 | 10,480 |
| + 단일 표현식               | 56.064  | +/- 1.515  | 7,936  |
| String.format          | 52.992  | +/- 8.325  | 28,808 |

### AverageTime (ms/op, 낮을수록 좋음)

| Benchmark              | Score | Error     | B/op   |
|------------------------|-------|-----------|--------|
| StringBuilder (루프)     | 0.006 | +/- 0.001 | 9,200  |
| + 행 단위                 | 0.007 | +/- 0.001 | 14,320 |
| + 이중 루프                | 0.009 | +/- 0.001 | 80,792 |
| StringBuilder (단일 표현식) | 0.010 | +/- 0.002 | 11,168 |
| String.join            | 0.014 | +/- 0.003 | 10,400 |
| + 단일 표현식               | 0.018 | +/- 0.001 | 7,936  |
| String.format          | 0.023 | +/- 0.010 | 28,808 |

### StringBuilder(루프) 대비 상대 성능 (avgt 기준)

| Benchmark              | 배수 (느린 정도) |
|------------------------|------------|
| StringBuilder (루프)     | 1.0x (기준)  |
| + 행 단위                 | 1.2x       |
| + 이중 루프                | 1.5x       |
| StringBuilder (단일 표현식) | 1.7x       |
| String.join            | 2.3x       |
| + 단일 표현식               | 3.0x       |
| String.format          | 3.8x       |

---

## 분석

### 가설 판정 (+ 이중 루프 vs StringBuilder)

- 비율: 1.5x -> **가설 채택 경계**. 루프 내 +=는 Java 21에서도 비효율적.
- 할당량: 80,792 B/op vs 9,200 B/op (8.8배) -> 메모리 측면에서는 압도적 차이.

### 예상과 다른 결과: 단일 표현식

- **+ 단일 표현식(0.018ms)이 루프 +=(0.009ms)보다 2배 느림.**
- 할당량은 7,936 B/op으로 가장 적지만, 인자가 200개 이상이라 StringConcatFactory의 MethodHandle 조립 비용이 병목.
- "단일 표현식이면 + == StringBuilder"라는 가설은 **이 규모(10x9 보드)에서는 성립하지 않음.**
- StringBuilder 단일 표현식(0.010ms)도 루프 StringBuilder(0.006ms)보다 느림 -> append 호출 횟수 자체의 오버헤드.

### String.format

- 3.8x 느림, 28,808 B/op -> Formatter 파싱 + Object[] + 오토박싱 비용.
- StringConcatFactory 최적화를 전혀 받지 못함 (invokestatic).

### String.join

- 2.3x 느림, 10,400 B/op -> StringJoiner 객체 생성 비용.
- 할당량은 StringBuilder와 비슷하지만 시간은 2배 이상.
