package br.com.thiagopagonha.jurosbaixostest.challenge.impl;

import br.com.thiagopagonha.jurosbaixostest.challenge.FizzBuzzSolver;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FizzBuzzSolverImplTest {

    FizzBuzzSolver fizzBuzzSolver = new FizzBuzzSolverImpl();

    @Test
    void given_divisible_by_3_translate_to_fizz() {
        List<String> response = fizzBuzzSolver.translateNumbers(
                List.of(3)
        );

        assertThat(response).containsExactly("fizz");
    }

    @Test
    void given_divisible_by_5_translate_to_buzz() {
        List<String> response = fizzBuzzSolver.translateNumbers(
                List.of(5)
        );

        assertThat(response).containsExactly("buzz");
    }

    @Test
    void given_divisible_by_3_and_5_translate_to_fizzbuzz() {
        List<String> response = fizzBuzzSolver.translateNumbers(
                List.of(15)
        );

        assertThat(response).containsExactly("fizzbuzz");
    }

    @Test
    void given_divisible_by_other_translate_to_number() {
        List<String> response = fizzBuzzSolver.translateNumbers(
                List.of(1)
        );

        assertThat(response).containsExactly("1");
    }
    @Test
    void given_translated_numbers_return_hashed_json_in_sha256() {
        String hash = fizzBuzzSolver.hashNumbers(
                List.of("fizz", "buzz", "fizzbuzz")
        );

        assertThat(hash).isEqualTo("c66a63862cf416c2acfe81ae697c066cff80b430af31fc9cae70957f355ded7d");
    }
}
