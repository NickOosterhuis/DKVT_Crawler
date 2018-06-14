package nl.getthere;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Nick Oosterhuis
 */
@SpringBootApplication
public class Application {

	@Autowired
	private ConsoleApp consoleApp;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	@Bean
	public CommandLineRunner startGUI() {
		return (args -> consoleApp.run());
	}
}