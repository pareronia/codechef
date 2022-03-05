package com.github.pareronia.codechef.nandxor;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Dragonado And XOR
 * @see <a href="https://www.codechef.com/COOK139C/problems/NANDXOR">https://www.codechef.com/COOK139C/problems/NANDXOR</a>
 */
class DragonadoAndXOR {

    private final InputStream in;
    private final PrintStream out;
    
    public DragonadoAndXOR(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    private int popCount(final int x) {
        return BitSet.valueOf(new long[] { x }).cardinality();
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int n = sc.nextInt();
        final int[] a = sc.nextIntArray(n);
        int[] ans = new int[] {};
        final int limit = Math.min(n, 20);
        outer:
        for (int j = 0; j < limit; j++) {
            for (int k = 0; k < limit; k++) {
                if (k == j) {
                    continue;
                }
                for (int m = 0; m < limit; m++) {
                    if (m == k || m == j) {
                        continue;
                    }
                    for (int p = 0; p < limit; p++) {
                        if (p == m || p == k || p == j) {
                            continue;
                        }
                        if (popCount(a[j] ^ a[k]) == popCount(a[m] ^ a[p])) {
                            ans = new int[] { j + 1, k + 1, m + 1, p + 1 };
                            break outer;
                        }
                    }
                }
            }
        }
        if (ans.length == 0) {
            this.out.println(-1);
        } else {
            this.out.println(Arrays.stream(ans)
                    .mapToObj(String::valueOf).collect(joining(" ")));
        }
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
            is = DragonadoAndXOR.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = System.in;
            out = System.out;
        }
        
        new DragonadoAndXOR(sample, is, out).solve();
        
        out.flush();
        if (sample) {
            final long timeSpent = (System.nanoTime() - timerStart) / 1_000;
            final double time;
            final String unit;
            if (timeSpent < 1_000) {
                time = timeSpent;
                unit = "µs";
            } else if (timeSpent < 1_000_000) {
                time = timeSpent / 1_000.0;
                unit = "ms";
            } else {
                time = timeSpent / 1_000_000.0;
                unit = "s";
            }
            final Path path
                    = Paths.get(DragonadoAndXOR.class.getResource("sample.out").toURI());
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
        
        public int[] nextIntArray(final int n) {
            final int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = nextInt();
            }
            return a;
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
