package sellyourunhappiness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SellyourunhappinessApiApplication {
	public static void main(String[] args) {
		System.setProperty("spring.config.name", "application-api,application-core");
		SpringApplication.run(SellyourunhappinessApiApplication.class, args);

	}
}