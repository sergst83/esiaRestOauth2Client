package ru.sergst.service;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * @author sergey.stanislavsky
 * created on 18.04.16.
 */
public interface TokenRestService {

    /**
     * Возвращает маркер доступа. <br>
     * Можно закешировать, чтобы отдавал не истекший токен, не запрашивая новый. <br>
     * Может быть полезно, если большое число запросов подряд.
     * @return OAuth2AccessToken.
     * @throws Exception
     *
     */
    OAuth2AccessToken getAccessToken() throws Exception;
}
