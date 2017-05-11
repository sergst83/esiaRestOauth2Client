package ru.sergst.service;

import java.util.Map;

/**
 * @author sergey.stanislavsky
 * created on 18.04.16.
 */
public interface SignService {

    /* <client_secret> – подпись запроса в формате PKCS#7 detached signature в кодировке UTF-
    8 от значений четырех параметров HTTP–запроса: scope, timestamp, clientId, state (без разделителей).
    <client_secret> должен быть закодирован в формате base64 url safe.
    Используемый для формирования подписи сертификат должен быть зарегистрирован в
    ЕСИА и привязан к учетной записи системы-клиента в ЕСИА. ЕСИА поддерживает
    сертификаты в формате X.509. ЕСИА поддерживает алгоритмы формирования
    электронной подписи RSA с длиной ключа 2048 и алгоритмом криптографического
    хэширования SHA-256, а также алгоритм электронной подписи ГОСТ Р 34.10-2001 и
    алгоритм криптографического хэширования ГОСТ Р 34.11-94.
    */
    String signMap(Map<String, String> paramsToSign) throws Exception;

    String signString(String string) throws Exception;

}
