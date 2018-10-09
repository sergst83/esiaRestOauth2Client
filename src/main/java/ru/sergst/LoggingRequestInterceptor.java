package ru.sergst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author sergey.stanislavsky
 * created on 19.04.16.
 */
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(LoggingRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) {
        String string = "\n" +
                "===========================request begin================================================" + "\n" +
                "URI : " + request.getURI() + "\n" +
                "Method : " + request.getMethod() + "\n" +
                "Headers : " + request.getHeaders().toString() + "\n" +
                "Request Body : " + new String(body, StandardCharsets.UTF_8) + "\n" +
                "==========================request end================================================" + "\n";
        logger.debug(string);
    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));
            bufferedReader.lines().forEachOrdered(inputStringBuilder::append);
        } catch (IOException e) {
            if (e.getMessage().contains("Server returned HTTP response code")) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));
                bufferedReader.lines().forEachOrdered(inputStringBuilder::append);
            }
        }

        String string = "\n" +
        "============================response begin==========================================" + "\n" +
        "status code: " + response.getStatusCode() + "\n" +
        "status text: " + response.getStatusText() + "\n" +
        "Response Body : [" + inputStringBuilder.toString() + "]" + "\n" +
        "=======================response end=================================================" + "\n";
        logger.debug(string);
    }

}
