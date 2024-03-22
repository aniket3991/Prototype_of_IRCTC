package anudip.project.irctc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


/**
 * Entry point of the program
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class PrototypeOfIRCTCApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrototypeOfIRCTCApplication.class, args);
    }
}
