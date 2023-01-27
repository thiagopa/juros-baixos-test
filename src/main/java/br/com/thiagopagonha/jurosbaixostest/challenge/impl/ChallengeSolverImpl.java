package br.com.thiagopagonha.jurosbaixostest.challenge.impl;

import br.com.thiagopagonha.jurosbaixostest.api.JurosBaixosApi;
import br.com.thiagopagonha.jurosbaixostest.api.exception.ApiException;
import br.com.thiagopagonha.jurosbaixostest.challenge.ChallengeSolver;
import br.com.thiagopagonha.jurosbaixostest.challenge.FizzBuzzSolver;
import br.com.thiagopagonha.jurosbaixostest.logger.VerySimpleFormatter;
import okhttp3.ResponseBody;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static br.com.thiagopagonha.jurosbaixostest.api.execution.ApiExecution.safeExecute;

/**
 * Challenge Solver includes the business logic
 *
 * @author Thiago Pagonha
 */
public class ChallengeSolverImpl implements ChallengeSolver {

    /**
     * Application Log
     */
    private static final Logger LOG = Logger.getLogger(ChallengeSolverImpl.class.getName());

    /**
     * Application log initialization
     */
    static {
        var handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        var verySimpleFormatter = new VerySimpleFormatter();
        handler.setFormatter(verySimpleFormatter);
        LOG.addHandler(handler);
        try {
            var fileHandler = new FileHandler("/tmp/challenge.log");
            fileHandler.setFormatter(verySimpleFormatter);
            LOG.addHandler(fileHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LOG.setLevel(Level.ALL);
        LOG.setUseParentHandlers(false);
    }

    /**
     * Api to make the rest calls
     */
    private JurosBaixosApi jurosBaixosApi;
    /**
     * Helper to solve the number's challenge
     */
    private FizzBuzzSolver fizzBuzzSolver;

    /**
     * This component dependencies
     *
     * @param jurosBaixosApi, api to make the rest calls
     * @param fizzBuzzSolver, helper to solve the number's challenge
     */
    public ChallengeSolverImpl(JurosBaixosApi jurosBaixosApi, FizzBuzzSolver fizzBuzzSolver) {
        this.jurosBaixosApi = jurosBaixosApi;
        this.fizzBuzzSolver = fizzBuzzSolver;
    }

    @Override
    public void solveChallenge() throws IOException, ApiException {
        // -- Start the challenge with a fresh state
        // safeExecute(jurosBaixosApi.reset()); -- It always timeout
        // -- From Jasper's email:
        // -- you need to call the apis multiple times to get the treasure.
        boolean isChallengeSolved = false;
        do {
            try {
                isChallengeSolved = solveNewRange();
            } catch (IOException | ApiException e) {
                // -- Try again because the api is moody
                LOG.info(e.getMessage());
            }
        } while (!isChallengeSolved);
    }

    /**
     * Starts the whole process again with a new range from the api
     *
     * @return true if the treasure was retrieved
     * @throws IOException,  in case of a connection error
     * @throws ApiException, in case of an api error
     */
    private boolean solveNewRange() throws IOException, ApiException {
        // -- Retrieve the numbers from the api
        Response<List<Integer>> rawNumbersResponse = safeExecute(jurosBaixosApi.retrieveNumbers());
        List<Integer> rawNumbers = rawNumbersResponse.body();
        if (rawNumbers == null) {
            throw new ApiException("No numbers retrieved");
        }
        LOG.info("RETRIEVED NUMBERS=" + rawNumbers);
        // -- Translate the numbers according to the rules
        List<String> translatedNumbers = fizzBuzzSolver.translateNumbers(rawNumbers);
        LOG.info("TRANSLATED NUMBERS=" + translatedNumbers);
        // -- Hash the translated numbers
        String answerHash = fizzBuzzSolver.hashNumbers(translatedNumbers);
        LOG.info("ANSWER HASH=" + answerHash);
        // -- Post the answer hash
        safeExecute(jurosBaixosApi.consumeAnswer(answerHash, translatedNumbers));
        // -- Asks if the api wants to give me the treasure
        boolean didWeGetTheTreasure = false;
        try {
            Response<ResponseBody> treasureResponse = safeExecute(jurosBaixosApi.openTreasureChest(answerHash));
            int responseCode = treasureResponse.code();
            String rawResponse = extractRawResponse(treasureResponse);

            LOG.info(String.format("[%d] %s", responseCode, rawResponse));
            return checkIfTreasureResponseIsActuallyATreasure(responseCode, rawResponse);
        } catch (Exception e) {
            // -- Delete the hash to start again with a new range
            safeExecute(jurosBaixosApi.deleteAnswer(answerHash));
            throw e;
        }
    }

    /**
     * Extracts the raw response of the api call
     *
     * @param response, the api response
     * @return the api response as a String
     * @throws IOException, in case of a connection error
     */
    private String extractRawResponse(Response<ResponseBody> response) throws IOException {
        try (ResponseBody responseBody = response.body();
             ResponseBody errorResponseBody = response.errorBody()
        ) {
            if (errorResponseBody != null) {
                return errorResponseBody.string();
            } else if (responseBody != null) {
                return responseBody.string();
            }
        }
        return null;
    }

    /**
     * This api is trying to trick me
     * This was deemed necessary because the api returns a http 200 ok but with a response error in the body :/
     */
    private boolean checkIfTreasureResponseIsActuallyATreasure(int responseCode, String rawResponse) {
        if (responseCode == 200) {
            return rawResponse.contains("\"treasure\"");
        }
        return false;
    }
}
