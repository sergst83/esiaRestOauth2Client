package ru.sergst.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.sergst.service.SignService;
import ru.sergst.service.TokenRestService;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author sergey.stanislavsky
 * created on 18.04.16.
 */
@Service
public class TokenRestServiceImpl implements TokenRestService {

    private static final Logger logger = LoggerFactory.getLogger(TokenRestService.class);

    @Value("${oauth.token:https://localhost.test.gosuslugi.ru/aas/oauth2/te}")
    private String tokenUrl;
    @Value("${oauth.clirnt_id:TEST_SYSTEM_IMPORT}")
    private String clientId;
    @Value("${oauth.scopes:http://esia.gosuslugi.ru/ext_imp}")
    private String scopes;

    @Autowired
    private AccessTokenProvider tokenProvider;

    @Autowired
    private SignService signService;

    @Autowired
    private RestTemplate restTemplate;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss Z");


    @Override
    public OAuth2AccessToken getAccessToken() throws Exception {
        String timeStamp = format.format(new Date());
        String state = UUID.randomUUID().toString();
        String scope = scopes;

        Map<String, String> paramsToSign = new LinkedHashMap<String, String>(4);
        paramsToSign.put("scope", scope);
        paramsToSign.put("timestamp", timeStamp);
        paramsToSign.put("clientId", clientId);
        paramsToSign.put("state", state);
        String clientSecret = signService.signMap(paramsToSign);

        UriComponentsBuilder accessTokenRequestBuilder = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                .queryParam("client_id", clientId)
                .queryParam("response_type", "token")
                .queryParam("grant_type", "client_credentials")
                .queryParam("scope", scope)
                .queryParam("state", state)
                .queryParam("timestamp", timeStamp)
                .queryParam("token_type", "Bearer")
                .queryParam("access_type", "online")
                .queryParam("client_secret", clientSecret);

        OAuth2AccessToken response = null;
        try {
            response = restTemplate.postForObject(tokenUrl, accessTokenRequestBuilder.build().getQueryParams(), OAuth2AccessToken.class);
        } catch (HttpStatusCodeException e){
            logger.error("Error wile call service {}. Responce body " + new String(e.getResponseBodyAsByteArray()), tokenUrl);
        } catch(RestClientException e){
            logger.error("Error wile call service.", e);
        }

        return response;

    }

    private String scopesToString(Collection<String> scopes) {
        final StringBuilder result = new StringBuilder(1024);
        final Iterator<String> iterator = scopes.iterator();
        while(iterator.hasNext()){
            final String scope = iterator.next();
            result.append(scope);
            if (iterator.hasNext()){
                result.append(' ');
            }
        }
        return result.toString();
    }
}
