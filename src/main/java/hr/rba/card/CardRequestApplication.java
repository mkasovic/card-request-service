package hr.rba.card;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CardRequestApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(CardRequestApplication.class, args);
    }
}
