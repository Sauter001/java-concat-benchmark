package benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime, Mode.Throughput})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(3)
@State(Scope.Benchmark)
public class SideBenchmark {

    private static final int VALUE = 42;
    private final Helper helper = new Helper();

    @Benchmark
    public void staticMethod(Blackhole bh) {
        bh.consume(Helper.staticCompute(VALUE));
    }

    @Benchmark
    public void instanceMethod(Blackhole bh) {
        bh.consume(helper.instanceCompute(VALUE));
    }

    static class Helper {
        @CompilerControl(CompilerControl.Mode.DONT_INLINE)
        public static int staticCompute(int value) {
            return value * value + value;
        }

        @CompilerControl(CompilerControl.Mode.DONT_INLINE)
        public int instanceCompute(int value) {
            return value * value + value;
        }
    }
}
