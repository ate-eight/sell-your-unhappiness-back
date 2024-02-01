package sellyourunhappiness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

@EnableJpaAuditing
@SpringBootApplication
public class SellyourunhappinessApiApplication {
    public static void main(String[] args) {
        System.setProperty("spring.config.name","application-api,application-core");
        SpringApplication.run(SellyourunhappinessApiApplication.class, args);

    }
}

