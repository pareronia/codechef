package com.github.pareronia.codechef.cdiglnum;

import static java.util.Arrays.asList;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Supplier;

/**
 * Chef and Digits of Large Numbers
 * @see <a href="https://www.codechef.com/problems/CDIGLNUM">https://www.codechef.com/problems/CDIGLNUM</a>
 */
class ChefAndDigitsOfLargeNumbers {

    private final boolean sample;
    private final InputStream in;
    private final PrintStream out;
    
    public ChefAndDigitsOfLargeNumbers(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.sample = sample;
        this.in = in;
        this.out = out;
    }
    
    @SuppressWarnings("unused")
    private void log(final Supplier<Object> supplier) {
        if (!sample) {
            return;
        }
        System.out.println(supplier.get());
    }
    
    public static final class Factorial<N extends Number> {
        private static final Long[] FACT = new Long[] {
                /* 0*/ 1L,
                /* 1*/ 1L,
                /* 2*/ 2L,
                /* 3*/ 6L,
                /* 4*/ 24L,
                /* 5*/ 120L,
                /* 6*/ 720L,
                /* 7*/ 5_040L,
                /* 8*/ 40_320L,
                /* 9*/ 362_880L,
                /*10*/ 3_628_800L,
                /*11*/ 39_916_800L,
                /*12*/ 479_001_600L,
                /*13*/ 6_227_020_800L,
                /*14*/ 87_178_291_200L,
                /*15*/ 1_307_674_368_000L,
                /*16*/ 20_922_789_888_000L,
                /*17*/ 355_687_428_096_000L,
                /*18*/ 6_402_373_705_728_000L,
                /*19*/ 121_645_100_408_832_000L,
                /*20*/ 2_432_902_008_176_640_000L
        };
        
        private static final Map<Integer, BigInteger> MAP = new HashMap<>();
        
        @SuppressWarnings("unchecked")
        public static <N extends Number> N fact(final int n) {
            if (n <= 20) {
                return (N) FACT[n];
            } else {
                return (N) bigFact(n);
            }
        }
        
        public static BigInteger bigFact(final int n) {
            if (n <= 20) {
                return BigInteger.valueOf(fact(n).longValue());
            }
            if (MAP.containsKey(n)) {
                return MAP.get(n);
            }
            final BigInteger fact = BigInteger.valueOf(n).multiply(bigFact(n - 1));
            MAP.put(n, fact);
            return fact;
        }
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final long n = sc.nextLong();
        int ans = 0;
        for (int j = 0; j < n; j++) {
            if (j == 0) {
                ans++;
            } else if (j % 10 == 0) {
                continue;
            } else {
                long product = 1;
                int nn = j;
                int d = 0;
                while (nn != 0) {
                    d++;
                    final int dd = nn % 10;
                    product *= dd;
                    if (dd == 0) {
                        break;
                    }
                    nn /= 10;
                }
                if (product >= Factorial.<Long> fact(d)) {
                    ans++;
                }
            }
        }
        this.out.println(ans);
    }
    
    public void solve() {
        try (final FastScanner sc = new FastScanner(this.in)) {
            final int numberOfTestCases = sc.nextInt();
            for (int i = 0; i < numberOfTestCases; i++) {
                handleTestCase(i, sc);
            }
        }
    }

    public static void main(final String[] args) throws IOException, URISyntaxException {
        final boolean sample = isSample();
        final InputStream is;
        final PrintStream out;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        long timerStart = 0;
        if (sample) {
            is = ChefAndDigitsOfLargeNumbers.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = System.in;
            out = System.out;
        }
        
        new ChefAndDigitsOfLargeNumbers(sample, is, out).solve();
        
        out.flush();
        if (sample) {
            final long timeSpent = (System.nanoTime() - timerStart) / 1_000;
            final double time;
            final String unit;
            if (timeSpent < 1_000) {
                time = timeSpent;
                unit = "??s";
            } else if (timeSpent < 1_000_000) {
                time = timeSpent / 1_000.0;
                unit = "ms";
            } else {
                time = timeSpent / 1_000_000.0;
                unit = "s";
            }
            final Path path
                    = Paths.get(ChefAndDigitsOfLargeNumbers.class.getResource("sample.out").toURI());
            final List<String> expected = Files.readAllLines(path);
            final List<String> actual = asList(baos.toString().split("\\r?\\n"));
            if (!expected.equals(actual)) {
                throw new AssertionError(String.format(
                        "Expected %s, got %s", expected, actual));
            }
            actual.forEach(System.out::println);
            System.out.println(String.format("took: %.3f %s", time, unit));
        }
    }
    
    private static boolean isSample() {
        try {
            return "sample".equals(System.getProperty("codechef"));
        } catch (final SecurityException e) {
            return false;
        }
    }
    
    private static final class FastScanner implements Closeable {
        private final BufferedReader br;
        private StringTokenizer st;
        
        public FastScanner(final InputStream in) {
            this.br = new BufferedReader(new InputStreamReader(in));
            st = new StringTokenizer("");
        }
        
        public String next() {
            while (!st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return st.nextToken();
        }
    
        public int nextInt() {
            return Integer.parseInt(next());
        }
        
        @SuppressWarnings("unused")
        public int[] nextIntArray(final int n) {
            final int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = nextInt();
            }
            return a;
        }
        
        @SuppressWarnings("unused")
        public long nextLong() {
            return Long.parseLong(next());
        }

        @Override
        public void close() {
            try {
                this.br.close();
            } catch (final IOException e) {
                // ignore
            }
        }
    }
}
