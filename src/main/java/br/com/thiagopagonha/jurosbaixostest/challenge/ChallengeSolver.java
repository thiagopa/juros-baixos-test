package br.com.thiagopagonha.jurosbaixostest.challenge;

import br.com.thiagopagonha.jurosbaixostest.api.exception.ApiException;

import java.io.IOException;

/**
 * Challenge Solver Interface
 * @author Thiago Pagonha
 */
public interface ChallengeSolver {
    /**
     * Used to solve the challenge
     */
    void solveChallenge() throws IOException, ApiException;
}
