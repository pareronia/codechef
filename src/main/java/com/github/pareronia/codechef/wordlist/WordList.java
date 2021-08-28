package com.github.pareronia.codechef.wordlist;

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
import java.util.List;
import java.util.TreeSet;
import java.util.function.Supplier;

/**
 * Word List
 * @see <a href="https://www.codechef.com/IARCSJUD/problems/WORDLIST">https://www.codechef.com/IARCSJUD/problems/WORDLIST</a>
 */
class WordList {

    private final boolean sample;
    private final InputStream in;
    private final PrintStream out;
    
    public WordList(
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
        final TreeSet<String> words = new TreeSet<>();
        for (int j = 0; j < n; j++) {
            final String line = sc.readLine().replaceAll("[\\.,;:']", "").toLowerCase();
            for (final String word : line.split("\\s+")) {
                if (!word.isEmpty()) {
                    words.add(word);
                }
            }
        }
        this.out.println(words.size());
        words.forEach(this.out::println);
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
            is = WordList.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = System.in;
            out = System.out;
        }
        
        new WordList(sample, is, out).solve();
        
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
                    = Paths.get(WordList.class.getResource("sample.out").toURI());
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
