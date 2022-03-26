package com.github.pareronia.codechef.miss_num;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.github.pareronia.codechef.TestBase;

class MissingNumbersTest extends TestBase<MissingNumbers>{
    
    private final Random rand = new Random(System.nanoTime());

    protected MissingNumbersTest() {
        super(MissingNumbers.class);
    }

    @Test
    void testOK() throws Throwable {
        for (int i = 0; i < 100_000; i++) {
            final int a = 1 + rand.nextInt(9_999);
            final int b = 1 + rand.nextInt(9_999 / a);
            final String in = String.format("1\n%d %d %d %d", a + b, a - b, a * b, a / b);
            final List<String> result = run(setUpInput(in));
            assertThat(result.get(0), is(String.format("%d %d", a, b)));
        }
    }

    @Test
    void testNOK() throws Throwable {
        for (int i = 0; i < 100_000; i++) {
            final int a = rand.nextInt(999_999_999);
            final int b = rand.nextInt(999_999_999);
            final int c = rand.nextInt(999_999_999);
            final int d = rand.nextBoolean() ? 1 : -1 * rand.nextInt(999_999_999);
            final String in = String.format("1\n%d %d %d %d", a, b, c, d);
            final List<String> result = run(setUpInput(in));
            assertThat(result.get(0), is("-1 -1"));
        }
    }
}
