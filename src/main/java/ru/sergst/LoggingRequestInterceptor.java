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

/**
 * @author sergey.stanislavsky
 * created on 19.04.16.
 */
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    final static Logger logger = LoggerFactory.getLogger(LoggingRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) throws IOException {
        logger.debug("===========================request begin================================================");

        logger.debug("URI : " + request.getURI());
        logger.debug("Method : " + request.getMethod());
        logger.debug("Headers : " + request.getHeaders().toString());
        logger.debug("Request Body : " + new String(body, "UTF-8"));
        logger.debug("==========================request end================================================");
    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
            String line = bufferedReader.readLine();
            while (line != null) {
                inputStringBuilder.append(line);
                inputStringBuilder.append('\n');
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            inputStringBuilder.append("");
        }
        logger.debug("============================response begin==========================================");
        logger.debug("status code: " + response.getStatusCode());
        logger.debug("status text: " + response.getStatusText());
        logger.debug("Response Body : {" + inputStringBuilder.toString() + "}");
        logger.debug("=======================response end=================================================");
    }

}
