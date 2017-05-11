package ru.sergst.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * @author sergey.stanislavsky
 * created on 20.04.16.
 */
@Configuration
@Profile("classpath")
@PropertySource(name = "props", value = {"classpath:/app-config.properties"})
public class ClasspathConfig {
}
