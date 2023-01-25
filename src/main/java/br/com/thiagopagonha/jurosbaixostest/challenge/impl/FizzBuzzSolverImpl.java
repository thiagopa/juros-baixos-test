package br.com.thiagopagonha.jurosbaixostest.challenge.impl;

import br.com.thiagopagonha.jurosbaixostest.challenge.FizzBuzzSolver;

import java.util.List;
import java.util.stream.Collectors;

public class FizzBuzzSolverImpl implements FizzBuzzSolver {
    @Override
    public List<String> translateNumbers(List<Integer> rawNumbers) {
        return rawNumbers.stream()
                .map(this::translateNumber)
                .collect(Collectors.toList());
    }

    private String translateNumber(Integer n) {
        boolean isDivisibleby3 = (n % 3 == 0);
        boolean isDivisibleby5 = (n % 5 == 0);

        String translated = "";

        if (isDivisibleby3) {
            translated = "fizz";
        }

        if (isDivisibleby5) {
            translated += "buzz";
        }

        if ("".equals(translated)) {
            translated = Integer.toString(n);
        }
        return translated;
    }
}
