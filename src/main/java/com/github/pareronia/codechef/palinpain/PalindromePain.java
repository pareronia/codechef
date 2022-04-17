package com.github.pareronia.codechef.palinpain;

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
import java.util.List;
import java.util.StringTokenizer;

/**
 * Palindrome Pain
 * @see <a href="https://www.codechef.com/LTIME107C/problems/PALINPAIN">https://www.codechef.com/LTIME107C/problems/PALINPAIN</a>
 */
class PalindromePain {

    private final InputStream in;
    private final PrintStream out;
    
    public PalindromePain(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    private void append(final StringBuilder sb, final char ch, final int n) {
        for (int j = 0; j < n; j++) {
            sb.append(ch);
        }
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int a = sc.nextInt();
        final int b = sc.nextInt();
        final StringBuilder sb = new StringBuilder();
        if (a % 2 == 0 && a >= 2 && b >= 2) {
            append(sb, 'a', a / 2);
            append(sb, 'b', b);
            append(sb, 'a', a / 2);
            sb.append(System.lineSeparator());
            append(sb, 'b', 1);
            append(sb, 'a', a / 2);
            append(sb, 'b', b - 2);
            append(sb, 'a', a / 2);
            append(sb, 'b', 1);
        } else if (b % 2 == 0 && b >= 2 && a >= 2) {
            append(sb, 'b', b / 2);
            append(sb, 'a', a);
            append(sb, 'b', b / 2);
            sb.append(System.lineSeparator());
            append(sb, 'a', 1);
            append(sb, 'b', b / 2);
            append(sb, 'a', a - 2);
            append(sb, 'b', b / 2);
            append(sb, 'a', 1);
        } else {
            sb.append("-1");
        }
        final String ans = sb.toString();
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
            is = PalindromePain.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = System.in;
            out = System.out;
        }
        
        new PalindromePain(sample, is, out).solve();
        
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
                    = Paths.get(PalindromePain.class.getResource("sample.out").toURI());
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
