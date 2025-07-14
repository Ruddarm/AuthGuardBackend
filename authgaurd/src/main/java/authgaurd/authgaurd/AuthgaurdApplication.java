package authgaurd.authgaurd;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthgaurdApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(AuthgaurdApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		System.out.println("oh Fuck");
	}

}
