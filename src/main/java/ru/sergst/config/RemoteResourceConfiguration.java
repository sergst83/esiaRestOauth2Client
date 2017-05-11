package ru.sergst.config;

import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.util.Log4jConfigurer;
import org.springframework.web.client.RestTemplate;
import ru.sergst.LoggingRequestInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sergey.stanislavsky
 * created on 18.04.16.
 */
@Configuration
@EnableOAuth2Client
@ComponentScan(basePackages = "ru.sergst")
@Import({FileConfig.class,ClasspathConfig.class})
public class RemoteResourceConfiguration {

    @Bean
    public AccessTokenProvider provider() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new LoggingRequestInterceptor());

        ClientCredentialsAccessTokenProvider provider = new ClientCredentialsAccessTokenProvider();
        provider.setInterceptors(interceptors);
        provider.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        return provider;
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean MethodInvokingFactoryBean log4jInitialization() {
        MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setTargetClass(Log4jConfigurer.class);
        factoryBean.setTargetMethod("initLogging");
        factoryBean.setArguments(new String[]{"${log4j.configuration:classpath:log4j.xml}"});
        return factoryBean;
    }

    @Bean
    RestTemplate restTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new LoggingRequestInterceptor());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        restTemplate.setInterceptors(interceptors);
//        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }
}
