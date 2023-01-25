package br.com.thiagopagonha.jurosbaixostest.challenge.impl;

import br.com.thiagopagonha.jurosbaixostest.api.JurosBaixosApi;
import br.com.thiagopagonha.jurosbaixostest.challenge.ChallengeSolver;
import br.com.thiagopagonha.jurosbaixostest.challenge.FizzBuzzSolver;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class ChallengeSolverImpl implements ChallengeSolver {

    private JurosBaixosApi jurosBaixosApi;
    private FizzBuzzSolver fizzBuzzSolver;
    public ChallengeSolverImpl(JurosBaixosApi jurosBaixosApi, FizzBuzzSolver fizzBuzzSolver) {
        this.jurosBaixosApi = jurosBaixosApi;
        this.fizzBuzzSolver = fizzBuzzSolver;
    }

    @Override
    public void solveChallenge() throws IOException {
        // -- Retrieve the numbers from the api
        Response<List<Integer>> rawNumbersResponse = jurosBaixosApi.retrieveNumbers().execute();
        List<Integer> rawNumbers = rawNumbersResponse.body();
        // -- Translate the numbers according to the rules
        List<String> translatedNumbers = fizzBuzzSolver.translateNumbers(rawNumbers);
        // -- Hash the translated numbers
        String answerHash = fizzBuzzSolver.hashNumbers(translatedNumbers);
        System.out.println(answerHash);
        System.out.println(translatedNumbers);
    }
}
