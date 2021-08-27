package com.github.pareronia.codechef.iarevers;

import static java.util.Arrays.asList;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
import java.util.function.Supplier;

/**
 * Reverse
 * @see <a href="https://www.codechef.com/IARCSJUD/problems/IAREVERS">https://www.codechef.com/IARCSJUD/problems/IAREVERS</a>
 */
class Reverse {

    private final boolean sample;
    private final InputStream in;
    private final PrintStream out;
    
    public Reverse(
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
    
    private void handleTestCase(final Integer i, final BufferedReader sc) throws IOException {
        final int n = Integer.parseInt(sc.readLine());
        final Deque<Deque<String>> lines = new ArrayDeque<>();
        for (int j = 0; j < n; j++) {
            final Deque<String> words = new ArrayDeque<>();
            final String line = sc.readLine();
            for (final String word : line.split("\\s+")) {
                words.addFirst(word.replaceAll("[\\.,;:']", ""));
            }
            lines.addFirst(words);
        }
        while (!lines.isEmpty()) {
            final Deque<String> words = lines.poll();
            while (!words.isEmpty()) {
                this.out.print(words.poll());
                this.out.print(" ");
            }
            this.out.println();
        }
    }
    
    public void solve() throws IOException {
        try (final BufferedReader sc = new BufferedReader(new InputStreamReader(this.in))) {
            final int numberOfTestCases;
            if (this.sample) {
                numberOfTestCases = Integer.parseInt(sc.readLine());
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
            is = Reverse.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = System.in;
            out = System.out;
        }
        
        new Reverse(sample, is, out).solve();
        
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
                    = Paths.get(Reverse.class.getResource("sample.out").toURI());
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
}
