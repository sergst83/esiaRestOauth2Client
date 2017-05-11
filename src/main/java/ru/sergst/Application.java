package ru.sergst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.sergst.config.RemoteResourceConfiguration;
import ru.sergst.service.impl.RestService;

/**
 * @author sergey.stanislavsky
 * created on 18.04.16.
 */

//@SpringBootApplication
//@EnableAutoConfiguration
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
//        ConfigurableApplicationContext applicationContext = SpringApplication.run(SergstApplication.class, args);
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(RemoteResourceConfiguration.class);
        logger.info("---------------------------Start------------------------------------------");
        RestService restService = applicationContext.getBean(RestService.class);
        logger.info(restService.sendRequestToService());
        logger.info("---------------------------Finish------------------------------------------");
    }
}
