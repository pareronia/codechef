package com.github.pareronia.codechef.max01evswp;

import static java.util.Arrays.asList;

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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Same Parity Swaps in Binary Strings
 * @see <a href="https://www.codechef.com/MARCH222C/problems/MAX01EVSWP">https://www.codechef.com/MARCH222C/problems/MAX01EVSWP</a>
 */
class SameParitySwapsInBinaryStrings {

    private final InputStream in;
    private final PrintStream out;
    
    public SameParitySwapsInBinaryStrings(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int n = sc.nextInt();
        final String s = sc.next();
        final Deque<Integer> qodd = new ArrayDeque<>();
        final Deque<Integer> qeven = new ArrayDeque<>();
        for (int j = 0; j < n; j++) {
            if ((j + 1) % 2 == 0) {
                if (s.charAt(j) == '0') {
                    qeven.addFirst(0);
                } else {
                    qeven.addLast(1);
                }
            } else {
                if (s.charAt(j) == '0') {
                    qodd.addLast(0);
                } else {
                    qodd.addFirst(1);
                }
                
            }
        }
        final int ans1 = check(qodd, qeven);
        qodd.clear();
        qeven.clear();
        for (int j = 0; j < n; j++) {
            if ((j + 1) % 2 == 0) {
                if (s.charAt(j) == '0') {
                    qeven.addLast(0);
                } else {
                    qeven.addFirst(1);
                }
            } else {
                if (s.charAt(j) == '0') {
                    qodd.addFirst(0);
                } else {
                    qodd.addLast(1);
                }
            }
        }
        final int ans2 = check(qodd, qeven);
        final int ans = Math.max(ans1, ans2);
        this.out.println(ans);
    }

    private int check(final Deque<Integer> qodd, final Deque<Integer> qeven) {
        int ans = 0;
        int prev = -1;
        while (true) {
            if (qeven.isEmpty() && qodd.isEmpty()) {
                break;
            }
            if (!qodd.isEmpty()) {
                final Integer o = qodd.pollFirst();
                if (prev == 0 && o == 1) {
                    ans++;
                }
                prev = o;
            }
            if (!qeven.isEmpty()) {
                final Integer e = qeven.pollFirst();
                if (prev == 0 && e == 1) {
                    ans++;
                }
                prev = e;
            }
        }
        return ans;
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
            is = SameParitySwapsInBinaryStrings.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = System.in;
            out = System.out;
        }
        
        new SameParitySwapsInBinaryStrings(sample, is, out).solve();
        
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
                    = Paths.get(SameParitySwapsInBinaryStrings.class.getResource("sample.out").toURI());
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
