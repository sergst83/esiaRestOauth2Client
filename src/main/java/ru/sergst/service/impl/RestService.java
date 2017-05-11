package ru.sergst.service.impl;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.sergst.service.SignService;
import ru.sergst.service.TokenRestService;

import java.io.File;

/**
 * @author sergey.stanislavsky
 * created on 18.04.16.
 */
@Service
public class RestService {

    final static Logger logger = LoggerFactory.getLogger(RestService.class);

    @Value("${restservice.url:https://localhost.test.gosuslugi.ru/rs/prns/import}")
    private String serviceUri;

    @Value("${request.body.json.file:/home/sergst/workspace/tests/esiaRestOauth2Client/src/main/resources/request.json}")
    private String requestJsonPath;

    @Autowired
    TokenRestService tokenService;

    @Autowired
    SignService signService;

    @Autowired
    RestTemplate restTemplate;

    public String sendRequestToService() throws Exception {

        OAuth2AccessToken accessToken = tokenService.getAccessToken();
        String response = null;
        if (accessToken != null) {

            //запрос к ресурсу
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(serviceUri);

            String jsonStringRequest = FileUtils.readFileToString(new File(requestJsonPath), "UTF-8");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_UTF8_VALUE));
            headers.setCacheControl("no-cache");
            headers.set(HttpHeaders.AUTHORIZATION, accessToken.getTokenType() + " " + accessToken.toString());
            headers.set("Request-Data", Base64.encodeBase64URLSafeString(jsonStringRequest.getBytes()));
            headers.set("Request-Data-Sign", signService.signString(jsonStringRequest));
            HttpEntity<String> request = new HttpEntity<String>(jsonStringRequest, headers);

            try {
                response = restTemplate.postForObject(uriComponentsBuilder.toUriString(), request, String.class);
            } catch (HttpStatusCodeException e) {
                logger.error("Error wile call service {}. Responce body " + new String(e.getResponseBodyAsByteArray()), serviceUri);
            } catch (RestClientException e) {
                logger.error("Error wile call service.", e);
            }
        }

        if (response != null) {
            return "Rest call success. Response: {" + response + "}";
        } else {
            return "Rest call faild.";
        }

    }

}
