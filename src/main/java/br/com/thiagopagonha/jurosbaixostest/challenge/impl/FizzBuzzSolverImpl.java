package br.com.thiagopagonha.jurosbaixostest.challenge.impl;

import br.com.thiagopagonha.jurosbaixostest.challenge.FizzBuzzSolver;
import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;
import java.util.stream.Collectors;

public class FizzBuzzSolverImpl implements FizzBuzzSolver {
    private final Gson gson = new Gson();

    @Override
    public List<String> translateNumbers(List<Integer> rawNumbers) {
        return rawNumbers.stream()
                .map(this::translateNumber)
                .collect(Collectors.toList());
    }

    @Override
    public String hashNumbers(List<String> translatedNumbers) {
        String compactJsonString = gson.toJson(translatedNumbers);
        return DigestUtils.sha256Hex(compactJsonString);
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
