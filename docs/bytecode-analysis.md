# JanggiConcatBenchmark 바이트코드 분석

> 환경: Java 21 (Temurin-21.0.6+7), `javac`가 `+` 연산을 `invokedynamic` + `StringConcatFactory.makeConcatWithConstants`로 컴파일 (JEP 280)

---

## 1. composeBoardWithPlusSingleExpression() - 단일 표현식 `+` 연산

### 바이트코드 패턴

```
// 모든 인자를 스택에 push한 뒤, 한 번에 makeConcatWithConstants 호출
17: aload_3                          // ln
18: getstatic COL_NUM                // COL_NUM[0]~[8] 로드
...
92: invokestatic formatSymbol        // 90개의 formatSymbol 호출
...
1493: invokedynamic #0:makeConcatWithConstants  // 인자 ~90개짜리 1차 호출
...
2096: invokedynamic #1:makeConcatWithConstants  // 나머지 ~33개짜리 2차 호출
2101: invokedynamic #2:makeConcatWithConstants  // 1차 + 2차 결합
2106: areturn
```

### 특징

- `invokedynamic makeConcatWithConstants` **3회** 호출 (JVM 인자 수 제한 때문에 분할)
- `new StringBuilder` **0회** (바이트코드 레벨에서)
- 런타임에 `StringConcatFactory`가 최적의 전략(MH_INLINE_SIZED_EXACT 등)으로 한 번에 결합
- 모든 `Position` 생성, `Map.get`, `formatSymbol` 호출을 먼저 수행 후 결과를 스택에 쌓고, 마지막에 한꺼번에 concat
- 바이트코드 크기: offset 0 ~ 2106 (**약 2,106 bytes**)

---

## 2. composeBoardWithStringBuilderSingleExpression() - 단일 표현식 StringBuilder

### 바이트코드 패턴

```
17: new java/lang/StringBuilder      // StringBuilder 1개 생성
20: invokespecial <init>
25: invokevirtual append(String)     // .append() 체이닝
30: invokevirtual append(String)
...
// formatSymbol 호출 → append → formatSymbol 호출 → append ...
216: invokestatic formatSymbol
219: invokevirtual append(String)
...
(약 279회의 append 호출)
...
invokevirtual toString               // 마지막에 toString()
areturn
```

### 특징

- `new StringBuilder` **1회**, `append()` **~279회**, `toString()` **1회**
- `invokedynamic` 호출 **0회**
- 각 `append` 호출마다 `invokevirtual`로 처리 (바이트코드가 매우 김)
- 내부 배열 리사이징이 필요할 수 있음 (초기 capacity 16)
- 바이트코드 크기: offset 0 ~ 약 2,200+ (**가장 큰 바이트코드**)

---

## 3. composeBoardWithPlusDoubleLoop() - `+=` 이중 루프

### 바이트코드 패턴

```
13: ldc ""                            // result = ""
// 바깥 루프 헤더 부분
20: invokedynamic #2:makeConcatWithConstants  // result += lineSeparator
32: invokedynamic #3:makeConcatWithConstants  // result += SPACE+SPACE+SPACE

// 안쪽 루프 (COL_NUM)
46: iload 6                           // for 조건 비교
50: if_icmpge 75                      // 루프 탈출
63: invokedynamic #4:makeConcatWithConstants  // result += SPACE + column
72: goto 46                           // 루프 반복

// 행 바깥 루프
99: iload 4
103: if_icmpge 185                    // row < 10
113: invokedynamic #6:makeConcatWithConstants  // result += ROW_NUM[row] + " " + VERTICAL_LINE

  // 열 안쪽 루프
  122: iload 5
  126: if_icmpge 169
  154: invokestatic formatSymbol
  157: invokedynamic #4:makeConcatWithConstants // result += SPACE + formatSymbol(piece)
  166: goto 122                       // 안쪽 루프

173: invokedynamic #7:makeConcatWithConstants   // result += SPACE + VERTICAL_LINE + ln
182: goto 99                          // 바깥 루프

190: invokedynamic #5:makeConcatWithConstants   // result += border + ln
197: areturn
```

### 특징

- `invokedynamic makeConcatWithConstants` **루프 안에서 반복 호출**
- 바깥 루프 10회 x 안쪽 루프 9회 = `makeConcatWithConstants`가 **~90회 + 추가 호출**
- **매 `+=` 마다 새 String 객체 생성** (가장 비효율적)
- `goto` 명령어로 루프 구현: `goto 46`, `goto 122`, `goto 99`
- 바이트코드 크기: offset 0 ~ 197 (**가장 작은 바이트코드**, 루프 덕분)

---

## 4. composeBoardWithPlusRowUnit() - `+=` 행 단위 (안쪽 unroll)

### 바이트코드 패턴

```
13: ldc ""
20: invokedynamic #2:makeConcatWithConstants   // result += ln
75: invokedynamic #8:makeConcatWithConstants   // 열 헤더 (10개 인자, 한 번에)
85: invokedynamic #2:makeConcatWithConstants   // result += ln
96: invokedynamic #5:makeConcatWithConstants   // result += border + ln

// 행 루프 (바깥만 루프, 안쪽은 unroll)
105: iload 4
109: if_icmpge 335
// 9개 formatSymbol을 스택에 push
138: invokestatic formatSymbol
160: invokestatic formatSymbol
...
317: invokestatic formatSymbol
320: invokestatic lineSeparator
323: invokedynamic #9:makeConcatWithConstants   // 12개 인자로 행 전체를 한 번에 concat
332: goto 105                         // 루프 반복

340: invokedynamic #5:makeConcatWithConstants   // result += border + ln
347: areturn
```

### 특징

- 행 루프 안에서 `invokedynamic` **1회/행** (안쪽 열은 unroll)
- 총 `invokedynamic` = 헤더 ~4회 + 루프 10회 = **~14회**
- 각 행의 `makeConcatWithConstants`는 **12개 인자**를 받아 한 번에 처리
- `+=`로 행을 결합하므로 매 행마다 새 String 생성 (10회)
- 바이트코드 크기: offset 0 ~ 347

---

## 5. composeBoardWithStringBuilder() - StringBuilder + 유틸 메서드

### 바이트코드 패턴

```
7: new java/lang/StringBuilder
11: invokespecial <init>
22: invokestatic StringBuilderUtil.appendColIndex  // 유틸 호출
27: invokevirtual append(String)     // border
33: invokevirtual append(String)     // lineSeparator
39: invokestatic StringBuilderUtil.appendRows       // 유틸 호출
44: invokevirtual append(String)     // border
50: invokevirtual append(String)     // lineSeparator
55: invokevirtual toString
58: areturn
```

### 특징

- **가장 짧은 바이트코드** (offset 0 ~ 58, **58 bytes**)
- `new StringBuilder` 1회, `append` 4회, `invokestatic` (유틸) 2회
- 실제 append 로직은 `StringBuilderUtil`의 별도 메서드로 위임
- `invokedynamic` **0회** (바이트코드 레벨)
- 루프나 append가 유틸 메서드 안에 있어 이 메서드 자체는 매우 깔끔

---

## 6. composeBoardWithStringJoinExtreme() - String.join

### 바이트코드 패턴

```
// 열 헤더: CharSequence[] 배열 생성 후 String.join
15: bipush 21
17: anewarray java/lang/CharSequence  // new CharSequence[21]
// dup + index + value + aastore 반복 (21회)
22: ldc "　"
24: aastore
...
170: invokestatic String.join          // 1차 join (열 헤더)

// 행 배열 생성
176: anewarray java/lang/String        // new String[10]

// 행 루프
188: if_icmpge 531
199: anewarray java/lang/CharSequence  // new CharSequence[23] (행마다)
// dup + aastore 반복 (23회, formatSymbol 포함)
521: invokestatic String.join          // 행별 join
524: aastore                           // rows[row] = 결과
528: goto 184

// 최종 조합
536: anewarray java/lang/CharSequence  // new CharSequence[6]
559: invokestatic String.join          // 행 배열 join
572: invokestatic String.join          // 전체 join
575: areturn
```

### 특징

- `String.join()` 호출: 고정 3회 + 루프 10회 = 총 **13회**
- `anewarray CharSequence` **매 행마다 생성** (배열 할당 오버헤드)
- `dup` + `aastore` 패턴으로 배열 초기화 (컴파일러가 varargs를 배열로 변환)
- `invokedynamic` **0회**, `new StringBuilder` **0회**
- 바이트코드 크기: offset 0 ~ 575

---

## 7. composeBoardWithFormatExtreme() - String.format

### 바이트코드 패턴

```
13: new java/lang/StringBuilder
17: invokespecial <init>
25: invokevirtual append              // lineSeparator

// 열 헤더 format (1차)
30: ldc "%s%s%s%s%s%s%s%s%s%s%s%s%s%s"  // 14개 %s
34: anewarray java/lang/Object         // new Object[15]
// dup + aastore 반복 (15회)
139: invokestatic String.format        // String.format 1차 호출
142: invokevirtual append

// 열 헤더 format (2차)
147: ldc "%s%s%s%s%s%s%s"               // 7개 %s
151: anewarray java/lang/Object         // new Object[7]
203: invokestatic String.format        // String.format 2차 호출
206: invokevirtual append

// border + ln
212: invokevirtual append
218: invokevirtual append

// 행 루프
229: if_icmpge 573
233: ldc "%s %s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s"  // 22개 %s
237: anewarray java/lang/Object         // new Object[23]
// 23개 원소 채우기 (formatSymbol 9회 포함)
560: invokestatic String.format        // 행별 String.format
563: invokevirtual append
570: goto 225                          // 루프

// 마무리
575: invokevirtual append              // border
581: invokevirtual append              // ln
586: invokevirtual toString
589: areturn
```

### 특징

- `StringBuilder` 1개 + `String.format()` **12회** (헤더 2회 + 루프 10회)
- 매 `format` 호출마다 `new Object[N]` 배열 생성 (varargs)
- `String.format`은 내부적으로 포맷 문자열 파싱 필요 (가장 무거운 오버헤드)
- `invokedynamic` **0회**
- 바이트코드 크기: offset 0 ~ 589

---

## 요약 비교표

| 메서드 | 핵심 메커니즘 | invokedynamic 횟수 | new StringBuilder | 루프 | 바이트코드 크기 |
|---|---|---|---|---|---|
| **PlusSingleExpression** | `makeConcatWithConstants` | 3회 | 0 | 없음 | ~2,106B |
| **StringBuilderSingleExpression** | `sb.append()` 체이닝 | 0 | 1 | 없음 | ~2,200B+ |
| **PlusDoubleLoop** | `makeConcatWithConstants` (루프 내) | ~100회+ | 0 | 이중 | ~197B |
| **PlusRowUnit** | `makeConcatWithConstants` (행 단위) | ~14회 | 0 | 단일 | ~347B |
| **StringBuilder (유틸)** | `sb.append()` + 유틸 위임 | 0 | 1 | 유틸 내부 | ~58B |
| **StringJoinExtreme** | `String.join()` + 배열 | 0 | 0 | 단일 | ~575B |
| **FormatExtreme** | `String.format()` + StringBuilder | 0 | 1 | 단일 | ~589B |

---

## 핵심 포인트

1. **Java 9+ (JEP 280)**: `+` 연산은 `new StringBuilder`가 아닌 `invokedynamic` + `StringConcatFactory.makeConcatWithConstants`로 컴파일됨
2. **단일 표현식 `+`**: 모든 인자를 스택에 올린 후 1~3회의 `invokedynamic`으로 처리하여 효율적
3. **루프 안의 `+=`**: **매 반복마다** `invokedynamic`이 호출되어 매번 새 String 생성 (비효율)
4. **String.format**: 포맷 문자열 파싱 + `Object[]` 배열 할당이라는 이중 오버헤드
5. **String.join**: `CharSequence[]` 배열 할당 오버헤드가 있지만 내부적으로 `StringJoiner`(StringBuilder 기반)를 사용
6. **바이트코드 크기 vs 실행 효율**: 루프 기반은 바이트코드가 작지만, 런타임 객체 생성이 많아 오히려 느릴 수 있음
