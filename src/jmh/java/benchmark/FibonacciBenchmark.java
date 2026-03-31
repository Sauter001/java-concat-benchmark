package benchmark;

import org.openjdk.jmh.annotations.*;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
@State(Scope.Benchmark)
public class FibonacciBenchmark {

    private static final int N = 100;

    // 1. 단순 반복문
    @Benchmark
    public BigInteger iterative() {
        if (N <= 1) return BigInteger.valueOf(N);
        BigInteger a = BigInteger.ZERO;
        BigInteger b = BigInteger.ONE;
        for (int i = 2; i <= N; i++) {
            BigInteger temp = a.add(b);
            a = b;
            b = temp;
        }
        return b;
    }

    // 2. DP 배열 (bottom-up)
    @Benchmark
    public BigInteger dpArray() {
        BigInteger[] dp = new BigInteger[N + 1];
        dp[0] = BigInteger.ZERO;
        dp[1] = BigInteger.ONE;
        for (int i = 2; i <= N; i++) {
            dp[i] = dp[i - 1].add(dp[i - 2]);
        }
        return dp[N];
    }

    // 3. 메모이제이션 (top-down)
    @Benchmark
    public BigInteger memoization() {
        Map<Integer, BigInteger> cache = new HashMap<>();
        cache.put(0, BigInteger.ZERO);
        cache.put(1, BigInteger.ONE);
        return fibMemo(N, cache);
    }

    private BigInteger fibMemo(int n, Map<Integer, BigInteger> cache) {
        if (cache.containsKey(n)) return cache.get(n);
        BigInteger result = fibMemo(n - 1, cache).add(fibMemo(n - 2, cache));
        cache.put(n, result);
        return result;
    }

    // 4. 행렬 거듭제곱 O(log n)
    @Benchmark
    public BigInteger matrixExponentiation() {
        if (N <= 1) return BigInteger.valueOf(N);
        BigInteger[][] result = matPow(
                new BigInteger[][]{{BigInteger.ONE, BigInteger.ONE}, {BigInteger.ONE, BigInteger.ZERO}},
                N - 1
        );
        return result[0][0];
    }

    private BigInteger[][] matMul(BigInteger[][] a, BigInteger[][] b) {
        return new BigInteger[][]{
                {a[0][0].multiply(b[0][0]).add(a[0][1].multiply(b[1][0])),
                 a[0][0].multiply(b[0][1]).add(a[0][1].multiply(b[1][1]))},
                {a[1][0].multiply(b[0][0]).add(a[1][1].multiply(b[1][0])),
                 a[1][0].multiply(b[0][1]).add(a[1][1].multiply(b[1][1]))}
        };
    }

    private BigInteger[][] matPow(BigInteger[][] m, int p) {
        BigInteger[][] result = {{BigInteger.ONE, BigInteger.ZERO}, {BigInteger.ZERO, BigInteger.ONE}};
        while (p > 0) {
            if ((p & 1) == 1) result = matMul(result, m);
            m = matMul(m, m);
            p >>= 1;
        }
        return result;
    }

    // 5. Fast doubling O(log n) — 행렬보다 상수 작음
    @Benchmark
    public BigInteger fastDoubling() {
        return fastDoublingHelper(N)[0];
    }

    // returns {F(n), F(n+1)}
    private BigInteger[] fastDoublingHelper(int n) {
        if (n == 0) return new BigInteger[]{BigInteger.ZERO, BigInteger.ONE};
        BigInteger[] prev = fastDoublingHelper(n >> 1);
        BigInteger a = prev[0];
        BigInteger b = prev[1];
        // F(2k)   = F(k) * (2*F(k+1) - F(k))
        // F(2k+1) = F(k)^2 + F(k+1)^2
        BigInteger c = a.multiply(b.shiftLeft(1).subtract(a));
        BigInteger d = a.multiply(a).add(b.multiply(b));
        if ((n & 1) == 0) {
            return new BigInteger[]{c, d};
        } else {
            return new BigInteger[]{d, c.add(d)};
        }
    }
}
