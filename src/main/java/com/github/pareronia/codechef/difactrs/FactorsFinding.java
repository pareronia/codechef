package com.github.pareronia.codechef.difactrs;

import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Factors Finding
 * @see <a href="https://www.codechef.com/CCSTART2/problems/DIFACTRS">https://www.codechef.com/CCSTART2/problems/DIFACTRS</a>
 */
class FactorsFinding {

    private final InputStream in;
    private final PrintStream out;
    
    public FactorsFinding(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int n = sc.nextInt();
        final Set<Integer> ans
                = Factors.fromPrimeFactors(Primes.primeFactorisation(n));
        this.out.println(ans.size());
        this.out.println(ans.stream().sorted()
                            .map(Object::toString).collect(joining(" ")));
    }
    
    public void solve() {
        try (final FastScanner sc = new FastScanner(this.in)) {
            final int numberOfTestCases;
            if (isSample()) {
                numberOfTestCases = sc.nextInt();
            } else {
                numberOfTestCases = 1;
            }
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
            is = FactorsFinding.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = System.in;
            out = System.out;
        }
        
        new FactorsFinding(sample, is, out).solve();
        
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
                    = Paths.get(FactorsFinding.class.getResource("sample.out").toURI());
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
    
    private static class Primes {

        public static int[] smallestPrimeFactors(final int n) {
            final int[] spf = new int[n + 1];
            spf[1] = 1;
            for (int j = 2; j <= n; j++) {
                if (spf[j] != 0) {
                    continue;
                }
                for (int k = j; k <= n; k += j) {
                    if (spf[k] == 0) {
                        spf[k] = j;
                    }
                }
            }
            return spf;
        }
        
        public static List<Integer> primeFactorisation(final int n) {
            final int[] spf = smallestPrimeFactors(n);
            final List<Integer> ans = new ArrayList<>();
            int nn = n;
            while (nn > 1) {
                ans.add(spf[nn]);
                nn /= spf[nn];
            }
            return ans;
        }
    }

    private static class Factors {
        
        public static Set<Integer> fromPrimeFactors(final List<Integer> primeFactors) {
            final Map<Integer, Long> map = primeFactors.stream()
                    .collect(groupingBy(identity(), counting()));
            final Set<Integer> ans = new HashSet<>();
            ans.add(1);
            for (final Entry<Integer, Long> e : map.entrySet()) {
                final List<Integer> tmp = new ArrayList<>();
                for (int i = 0; i <= e.getValue(); i++) {
                    final int x = power(e.getKey(), i);
                    ans.stream()
                        .map(a -> a * x)
                        .collect(toCollection(() -> tmp));
                }
                ans.addAll(tmp);
            }
            return ans;
        }
        
        private static int power(final int n, final int p) {
            if (p == 0) {
                return 1;
            }
            if (p == 1) {
                return n;
            }
            int ans = 1;
            for (int i = 0; i < p; i++) {
                ans *= n;
            }
            return ans;
        }
    }
}
