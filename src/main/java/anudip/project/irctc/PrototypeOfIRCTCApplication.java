package anudip.project.irctc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class PrototypeOfIRCTCApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrototypeOfIRCTCApplication.class, args);
    }
}
