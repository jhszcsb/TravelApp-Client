package frontend;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.Resource;

@Configuration
@EnableWebMvc
@ComponentScan
public class WebAppConfig {

    @Resource
    private Environment env;

}
