package com.github.pareronia.codechef.maxmexpath;

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

/**
 * MEX on Trees
 * @see <a href="https://www.codechef.com/MARCH221C/problems/MAXMEXPATH">https://www.codechef.com/MARCH221C/problems/MAXMEXPATH</a>
 */
class MEXOnTrees {

    private final InputStream in;
    private final PrintStream out;
    
    public MEXOnTrees(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int n = sc.nextInt();
        final int[] a = sc.nextIntArray(n);
        final int[][] e = new int[n - 1][3];
        for (int j = 0; j < n - 1; j++) {
            e[j] = new int[] { sc.nextInt() - 1, sc.nextInt() - 1, 1 };
        }
        final int source = 0;
        final Graph.Adjacency[] adj = Graph.toAdjacencyList(n, e);
        final int[] path = Dijkstra.get(adj, source);
        int ans = 0;
        outer:
        for (int j = 0; j < n; j++) {
            final boolean[] b = new boolean[path.length];
            int parent = j;
            while (parent != source) {
                final int v = a[parent];
                if (v < b.length) {
                    b[v] = true;
                }
                parent = path[parent];
            }
            if (a[source] < b.length) {
                b[a[source]] = true;
            }
            for (int k = 0; k < b.length; k++) {
                if (!b[k]) {
                    ans = Math.max(ans, k);
                    continue outer;
                }
            }
            ans = Math.max(ans, b.length);
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
            is = MEXOnTrees.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = System.in;
            out = System.out;
        }
        
        new MEXOnTrees(sample, is, out).solve();
        
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
                    = Paths.get(MEXOnTrees.class.getResource("sample.out").toURI());
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
    
    private static class Graph {
        
        private static Adjacency[] toAdjacencyList(final int n, final int[][] e) {
            assert e[0].length == 3;
            final Adjacency[] adj = new Adjacency[n];
            for (int j = 0; j < n; j++) {
                adj[j] = new Adjacency();
            }
            for (int j = 0; j < e.length; j++) {
                final int v1 = e[j][0];
                final int v2 = e[j][1];
                adj[v1].add(new int[] { v2, e[j][2] });
                adj[v2].add(new int[] { v1, e[j][2] });
            }
            return adj;
        }
        
        public static class Adjacency extends ArrayList<int[]> {

            private static final long serialVersionUID = 1L;
        }
    }

    private static class Dijkstra {
        
        public static int[] get(final Graph.Adjacency[] adj, final int source) {
            final int n = adj.length;
            final int path[] = new int[n];
            final long dist[] = new long[n];
            Arrays.fill(dist, Long.MAX_VALUE);
            dist[source] = 0;
            final PriorityQueue<Integer> queue
                = new PriorityQueue<>((e1, e2) -> Long.compare(dist[e1], dist[e2]));
            queue.add(source);
            while (!queue.isEmpty()) {
                final int u = queue.poll();
                for (final int[] v : adj[u]) {
                    final long alt = dist[u] + v[1];
                    if (alt < dist[v[0]]) {
                        dist[v[0]] = alt;
                        path[v[0]] = u;
                        queue.add(v[0]);
                    }
                }
            }
            return path;
        }
    }
}
