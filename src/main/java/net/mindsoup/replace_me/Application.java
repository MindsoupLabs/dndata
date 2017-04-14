package net.mindsoup.replace_me;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 */
@SpringBootApplication
@EnableSwagger2
public class Application {

    private static Log logger = LogFactory.getLog(Application.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
        logger.info("replace_me started");
    }

}
