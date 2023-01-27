package br.com.thiagopagonha.jurosbaixostest;

import br.com.thiagopagonha.jurosbaixostest.api.JurosBaixosApi;
import br.com.thiagopagonha.jurosbaixostest.challenge.ChallengeSolver;
import br.com.thiagopagonha.jurosbaixostest.challenge.FizzBuzzSolver;
import br.com.thiagopagonha.jurosbaixostest.challenge.impl.ChallengeSolverImpl;
import br.com.thiagopagonha.jurosbaixostest.challenge.impl.FizzBuzzSolverImpl;
import br.com.thiagopagonha.jurosbaixostest.factory.JurosBaixosApiFactory;

/**
 * The main Application to the evaluation test
 * @author Thiago Pagonha
 */
public class JurosBaixosTestApplication {
    public static void main(String[] args) throws Exception {
        // -- Make sure we have the api key
        validateArgument(args);
        // -- Creating app dependencies
        JurosBaixosApi jurosBaixosApi = JurosBaixosApiFactory.createApi(args[0]);
        FizzBuzzSolver fizzBuzzSolver = new FizzBuzzSolverImpl();
        ChallengeSolver challengeSolver = new ChallengeSolverImpl(jurosBaixosApi, fizzBuzzSolver);
        // -- Start the app de facto
        challengeSolver.solveChallenge();
    }

    private static void validateArgument(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Api Key is missing");
        }
        System.out.println("Application Key: " + args[0]);
    }
}
