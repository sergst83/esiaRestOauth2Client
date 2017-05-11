package ru.sergst.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * @author sergey.stanislavsky
 * created on 20.04.16.
 */
@Configuration
@Profile("file")
@PropertySource("file:${APP_CFG}/app-config.properties")
public class FileConfig {
}
