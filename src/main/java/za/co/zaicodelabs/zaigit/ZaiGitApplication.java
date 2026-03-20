package za.co.zaicodelabs.zaigit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

/**
 * ZaiGit - AI-Powered Git Automation
 *
 * Crafting custom software solutions that help SMEs and organisations thrive,
 * while building in-house systems that solve real problems.
 *
 * Technology should make life better for businesses, communities, and people everywhere.
 *
 * @author ZaiCode Labs
 * @version 1.0.0
 * @see <a href="https://zaicodelabs.co.za">ZaiCode Labs</a>
 */
@SpringBootApplication
public class ZaiGitApplication {

	public static void main(String[] args) {
		// Disable Spring Boot banner for cleaner CLI output
		SpringApplication app = new SpringApplication(ZaiGitApplication.class);
		app.setBannerMode(org.springframework.boot.Banner.Mode.OFF);
		app.run(args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(za.co.zaicodelabs.zaigit.cli.GitCLIController controller) {
		return args -> {
			controller.run(args);
		};
	}
}