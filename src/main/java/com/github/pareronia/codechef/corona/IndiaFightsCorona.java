package com.github.pareronia.codechef.corona;

import static java.util.Arrays.asList;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
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

        public static Adjacency[] newAdjacencyList(final int n) {
            final Adjacency[] adj = new Adjacency[n];
            for (int j = 0; j < n; j++) {
                adj[j] = new Adjacency();
            }
            return adj;
        }
        
        public static class Adjacency extends ArrayList<int[]> {

            private static final long serialVersionUID = 1L;
        }
    }
        
    private static class Dijkstra {
        
        public static long[] get(final Graph.Adjacency[] adj, final int source) {
            final int n = adj.length;
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
                        queue.add(v[0]);
                    }
                }
            }
            return dist;
        }
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) throws IOException {
        final int n = sc.nextInt();
        final int m = sc.nextInt();
        final int k = sc.nextInt();
        final Graph.Adjacency[] adj = Graph.newAdjacencyList(n + 1);
        for (int j = 0; j < k; j++) {
            final int kk = sc.nextInt();
            final int c = sc.nextInt();
            adj[0].add(new int[] { kk, c });
            adj[kk].add(new int[] { 0, c });
        }
        for (int j = k; j < k + m; j++) {
            final int c1 = sc.nextInt();
            final int c2 = sc.nextInt();
            final int d = sc.nextInt();
            adj[c1].add(new int[] { c2, d });
            adj[c2].add(new int[] { c1, d });
        }
        final long[] dist = Dijkstra.get(adj, 0);
        for (int j = 1; j <= n; j++) {
            this.out.print(dist[j]);
            this.out.print(" ");
        }
        this.out.println();
    }

    public void solve() throws IOException {
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
        final private int BUFFER_SIZE = 1 << 16;
        private final DataInputStream din;
        private final byte[] buffer;
        private int bufferPointer, bytesRead;
 
        public FastScanner(final InputStream in) {
            din = new DataInputStream(in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }
 
        @SuppressWarnings("unused")
        public String readLine() throws IOException {
            final byte[] buf = new byte[64]; // line length
            int cnt = 0, c;
            while ((c = read()) != -1) {
                if (c == '\n') {
                    if (cnt != 0) {
                        break;
                    } else {
                        continue;
                    }
                }
                buf[cnt++] = (byte)c;
            }
            return new String(buf, 0, cnt);
        }
 
        public int nextInt() throws IOException {
            int ret = 0;
            byte c = read();
            while (c <= ' ') {
                c = read();
            }
            final boolean neg = (c == '-');
            if (neg) {
                c = read();
            }
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');
 
            if (neg) {
                return -ret;
            }
            return ret;
        }
 
        @SuppressWarnings("unused")
        public long nextLong() throws IOException {
            long ret = 0;
            byte c = read();
            while (c <= ' ') {
                c = read();
            }
            final boolean neg = (c == '-');
            if (neg) {
                c = read();
            }
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');
            if (neg) {
                return -ret;
            }
            return ret;
        }
 
        @SuppressWarnings("unused")
        public double nextDouble() throws IOException {
            double ret = 0, div = 1;
            byte c = read();
            while (c <= ' ') {
                c = read();
            }
            final boolean neg = (c == '-');
            if (neg) {
                c = read();
            }
 
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');
 
            if (c == '.') {
                while ((c = read()) >= '0' && c <= '9') {
                    ret += (c - '0') / (div *= 10);
                }
            }
 
            if (neg) {
                return -ret;
            }
            return ret;
        }
 
        private void fillBuffer() throws IOException {
            bytesRead = din.read(buffer, bufferPointer = 0,
                                 BUFFER_SIZE);
            if (bytesRead == -1) {
                buffer[0] = -1;
            }
        }
 
        private byte read() throws IOException {
            if (bufferPointer == bytesRead) {
                fillBuffer();
            }
            return buffer[bufferPointer++];
        }
 
        @Override
        public void close() throws IOException {
            if (din == null) {
                return;
            }
            din.close();
        }
    }
}
