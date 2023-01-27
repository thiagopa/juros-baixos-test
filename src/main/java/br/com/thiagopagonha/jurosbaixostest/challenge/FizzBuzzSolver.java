package br.com.thiagopagonha.jurosbaixostest.challenge;

import java.util.List;

/**
 * FizzBuzzSolver helper
 * @author Thiago Pagonha
 */
public interface FizzBuzzSolver {
    /**
     * Translate the numbers according to Jasper's Rule
     *
     * Note that you need to translate the numbers you receive in the first call to:
     * fizz if it is divisible by 3
     * buzz if it is divisible by 5
     * fizzbuzz if is divisible by 3 and 5
     * In the other case just return the number
     */
    List<String> translateNumbers(List<Integer> rawNumbers);

    /**
     * Hash the translated numbers into a sha256 string
     */
    String hashNumbers(List<String> translatedNumbers);
}
