package bg.fmi.plovdiv.veso.campus_processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application class for Campus Processor Internal Service.
 * This service handles data processing and validation in the internal network.
 */
@SpringBootApplication
public class CampusProcessorApplication
{
    /**
     * Main entry point for the Campus Processor application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args)
    {
        SpringApplication.run(CampusProcessorApplication.class, args);
    }
}
