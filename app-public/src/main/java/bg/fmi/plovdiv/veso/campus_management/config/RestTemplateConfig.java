package bg.fmi.plovdiv.veso.campus_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Configuration class for RestTemplate bean.
 * Configures HTTP client settings for communication with internal services.
 */
@Configuration
public class RestTemplateConfig
{
    /**
     * Creates a RestTemplate bean with configured timeouts.
     * Connect timeout: 5 seconds
     * Read timeout: 10 seconds
     *
     * @return configured RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate()
    {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(10).toMillis());

        return new RestTemplate(factory);
    }
}
