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
import java.util.List;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.stream.IntStream;

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
    
    @SuppressWarnings("unchecked")
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int n = sc.nextInt();
        final int[] a = sc.nextIntArray(n);
        final List<Integer>[] adj = new ArrayList[n];
        for (int j = 0; j < n; j++) {
            adj[j] = new ArrayList<>();
        }
        for (int j = 0; j < n - 1; j++) {
            final int x = sc.nextInt() - 1;
            final int y = sc.nextInt() - 1;
            adj[x].add(y);
            adj[y].add(x);
        }
        final int ans = new DFS(a, adj).dfs(0, -1);
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
    
    private static class DFS {
        private final List<Integer>[] adj;
        private final int[] a;
        private final int[] visited;
        private final SortedSet<Integer> missing;
        
        public DFS(final int[] a, final List<Integer>[] adj) {
            this.a = a;
            this.adj = adj;
            this.visited = new int[a.length];
            this.missing = new TreeSet<>();
            IntStream.rangeClosed(0, a.length).forEach(this.missing::add);
        }
        
        public int dfs(final int u, final int parent) {
            int ans = -1;
            final int val = a[u];
            visited[val]++;
            if (visited[val] > 0) {
                missing.remove(Integer.valueOf(val));
            }
            for (final int v : adj[u]) {
                if (v == parent) {
                    continue;
                }
                ans = Math.max(ans, dfs(v, u));
            }
            ans = Math.max(ans, missing.first());
            visited[val]--;
            if (visited[val] == 0) {
                missing.add(val);
            }
            return ans;
        }
    }
}
