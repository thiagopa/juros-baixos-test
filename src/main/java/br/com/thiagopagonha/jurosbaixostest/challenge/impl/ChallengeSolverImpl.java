package br.com.thiagopagonha.jurosbaixostest.challenge.impl;

import br.com.thiagopagonha.jurosbaixostest.api.JurosBaixosApi;
import br.com.thiagopagonha.jurosbaixostest.challenge.ChallengeSolver;
import br.com.thiagopagonha.jurosbaixostest.challenge.FizzBuzzSolver;
import br.com.thiagopagonha.jurosbaixostest.logger.VerySimpleFormatter;
import com.google.gson.Gson;
import okhttp3.ResponseBody;
import okhttp3.internal.http2.StreamResetException;
import retrofit2.Response;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChallengeSolverImpl implements ChallengeSolver {

    private static final Logger LOG = Logger.getLogger(ChallengeSolverImpl.class.getName());
    private final Gson gson = new Gson();

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
        if (!rawNumbersResponse.isSuccessful()) {
            throw new RuntimeException("Api doesn't want to give me the numbers");
        }
        List<Integer> rawNumbers = rawNumbersResponse.body();
        LOG.info("RETRIEVED NUMBERS=" + rawNumbers);
        // -- Translate the numbers according to the rules
        List<String> translatedNumbers = fizzBuzzSolver.translateNumbers(rawNumbers);
        LOG.info("TRANSLATED NUMBERS=" + translatedNumbers);
        // -- Hash the translated numbers
        String answerHash = fizzBuzzSolver.hashNumbers(translatedNumbers);
        LOG.info("ANSWER HASH=" + answerHash);
        // -- Post the answer hash
        Response<ResponseBody> postHashResponse = jurosBaixosApi.consumeAnswer(answerHash, translatedNumbers).execute();
        if (!postHashResponse.isSuccessful()) {
            throw new RuntimeException("Api doesn't believe my hash is correct");
        }
        // -- Asks if the api wants to give me the treasure
        // -- Just keep asking until it gives me the treasure
        boolean didWeGetTheTreasure = false;
        do {
            Response<ResponseBody> treasureResponse = null;
            try {
                treasureResponse = jurosBaixosApi.openTreasureChest(answerHash).execute();
                int responseCode = treasureResponse.code();
                String rawResponse = extractRawResponse(treasureResponse);

                LOG.info(String.format("[%d] %s", responseCode, rawResponse));
                didWeGetTheTreasure = checkIfTreasureResponseIsActuallyATreasure(responseCode, rawResponse);

            } catch (ProtocolException | StreamResetException e) {
                // -- api trying to trick me into giving up
                LOG.info(e.getMessage());
            }

        } while (!didWeGetTheTreasure);
    }

    private String extractRawResponse(Response<ResponseBody> treasureResponse) throws IOException {
        try (ResponseBody responseBody = treasureResponse.body();
             ResponseBody errorResponseBody = treasureResponse.errorBody()
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
     */
    private boolean checkIfTreasureResponseIsActuallyATreasure(int responseCode, String rawResponse) {
        if (responseCode == 200) {
            return rawResponse.contains("\"treasure\"");
        }
        return false;
    }

}
