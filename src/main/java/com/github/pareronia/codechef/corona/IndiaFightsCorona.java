package com.github.pareronia.codechef.corona;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.util.function.Supplier;

/**
 * India Fights Corona
 * @see <a href="https://www.codechef.com/problems/CORONA">https://www.codechef.com/problems/CORONA</a>
 */
class IndiaFightsCorona {

    private final boolean sample;
    private final InputStream in;
    private final PrintStream out;
    
    public IndiaFightsCorona(
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
    
    private static class Graph {
        
        public static List<int[]>[] toAdjacencyList(final int n, final int[][] e) {
            return toAdjacencyList(n, e, false);
        }
        
        @SuppressWarnings("unchecked")
        private static List<int[]>[] toAdjacencyList(final int n, final int[][] e, final boolean bidi) {
            assert e[0].length == 3;
            final List<int[]>[] a = new List[n];
            for (int j = 0; j < n; j++) {
                a[j] = new ArrayList<>();
            }
            for (int j = 0; j < e.length; j++) {
                final int v1 = e[j][0];
                final int v2 = e[j][1];
                a[v1].add(new int[] { v2, e[j][2] });
                if (bidi) {
                    a[v2].add(new int[] { v1, e[j][2] });
                }
            }
            return a;
        }
    }
    
    private static class Dijkstra {
        
        public static long[] get(final List<int[]>[] g, final int source) {
            final int n = g.length;
            final int path[] = new int[n];
            final long dist[] = new long[n];
            Arrays.fill(dist, Long.MAX_VALUE);
            dist[source] = 0;
            final PriorityQueue<Integer> queue
                = new PriorityQueue<>((e1, e2) -> Long.compare(dist[e1], dist[e2]));
            queue.add(source);
            while (!queue.isEmpty()) {
                final int u = queue.poll();
                for (final int[] v : g[u]) {
                    final long alt = dist[u] + v[1];
                    if (alt < dist[v[0]]) {
                        dist[v[0]] = alt;
                        path[v[0]] = u;
                        queue.add(v[0]);
                    }
                }
            }
            return dist;
        }
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int n = sc.nextInt();
        final int m = sc.nextInt();
        final int k = sc.nextInt();
        final int[][] h = new int[k][];
        final int[][] e = new int[k + 2 * m][3];
        for (int j = 0; j < k; j++) {
            final int kk = sc.nextInt() -1;
            final int c = sc.nextInt();
            h[j] = new int[] { kk, c };
        }
        for (int j = 0; j < 2 * m; j += 2) {
            final int c1 = sc.nextInt() - 1;
            final int c2 = sc.nextInt() - 1;
            final int d = sc.nextInt();
            e[j] = new int[] { c1, c2, d };
            e[j + 1] = new int[] { c2, c1, d };
        }
        for (int j = 0; j < k; j++) {
            e[2 * m + j] = new int[] { h[j][0], n + h[j][0], h[j][1] };
        }
        final List<int[]>[] adj = Graph.toAdjacencyList(n + k, e);
        for (int j = 0; j < n; j++) {
            final long[] dist = Dijkstra.get(adj, j);
            long min = Long.MAX_VALUE;
            for (int jj = 0; jj < k; jj++) {
                min = Math.min(min, dist[n + h[jj][0]]);
            }
            this.out.print(min);
            this.out.print(" ");
        }
        this.out.println("");
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
            is = IndiaFightsCorona.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = System.in;
            out = System.out;
        }
        
        new IndiaFightsCorona(sample, is, out).solve();
        
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
                    = Paths.get(IndiaFightsCorona.class.getResource("sample.out").toURI());
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
