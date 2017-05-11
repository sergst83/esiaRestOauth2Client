package ru.sergst.service.impl;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.sergst.service.SignService;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author sergey.stanislavsky
 * created on 18.04.16.
 */
@Service
public class SignServiceImpl implements SignService {


    private static final int DEFAULT_BUFFER_CAPACITY = 1024;
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final String BOUNCY_CASTLE_PROVIDER = "BC";
    private static final String SIGNATUREALGO = "SHA256withRSA";

    @Value("${oauth.keystore.path:/home/sergst/workspace/tests/esiaRestOauth2Client/src/main/resources/keystoreTest.jks}")
    private String PATH_TO_KEYSTORE;

    @Value("${oauth.key.alias:test_sys}")
    private String KEY_ALIAS_IN_KEYSTORE;

    @Value("${oauth.keystorepassword:}")
    private String KEYSTORE_PASSWORD;

    private CMSSignedDataGenerator signGenerator;

    @Override
    public String signMap(Map<String, String> paramsToSign) throws Exception {

        final StringBuilder toSign = new StringBuilder(DEFAULT_BUFFER_CAPACITY);
        for (String s : paramsToSign.keySet()) {
            toSign.append(paramsToSign.get(s));
        }
        return Base64.encodeBase64URLSafeString(signPkcs7(toSign.toString().getBytes()));
    }

    @Override
    public String signString(String string) throws Exception {
        return Base64.encodeBase64URLSafeString(signPkcs7(string.getBytes()));
    }

    private byte[] signPkcs7(final byte[] content) throws Exception {
        CMSSignedDataGenerator signGenerator = setUpProvider();
        CMSTypedData cmsdata = new CMSProcessableByteArray(content);
        CMSSignedData signeddata = signGenerator.generate(cmsdata, false);
        return signeddata.getEncoded();
    }

    private CMSSignedDataGenerator setUpProvider() throws Exception {

        KeyStore keystore = KeyStore.getInstance("JKS");
        InputStream is = new FileInputStream(PATH_TO_KEYSTORE);
        keystore.load(is, KEYSTORE_PASSWORD.toCharArray());

        Security.addProvider(new BouncyCastleProvider());

        Certificate[] certchain = keystore.getCertificateChain(KEY_ALIAS_IN_KEYSTORE);

        final List<Certificate> certlist = new ArrayList<Certificate>();

        for (int i = 0, length = certchain == null ? 0 : certchain.length; i < length; i++) {
            certlist.add(certchain[i]);
        }

        Store certstore = new JcaCertStore(certlist);

        Certificate cert = keystore.getCertificate(KEY_ALIAS_IN_KEYSTORE);

        ContentSigner signer = new JcaContentSignerBuilder(SIGNATUREALGO).setProvider(BOUNCY_CASTLE_PROVIDER)
                .build((PrivateKey) (keystore.getKey(KEY_ALIAS_IN_KEYSTORE, KEYSTORE_PASSWORD.toCharArray())));

        CMSSignedDataGenerator generator = new CMSSignedDataGenerator();

        generator.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider(BOUNCY_CASTLE_PROVIDER)
                .build()).build(signer, (X509Certificate) cert));

        generator.addCertificates(certstore);

        return generator;
    }
}



