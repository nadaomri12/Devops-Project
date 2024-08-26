package backend.stageproject;

import backend.stageproject.Auth.AuthenticationService;
import backend.stageproject.Auth.RegisterRequestDto;
import backend.stageproject.Entity.User;
import backend.stageproject.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.util.Optional;

import static backend.stageproject.Entity.Role.ADMIN;
import static java.lang.Boolean.TRUE;


@SpringBootApplication
public class StageprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(StageprojectApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service,
			UserRepository userRepository

	) {
		return args -> {
			// Vérifier si l'utilisateur Admin existe déjà

			Optional<User> existingAdmin = userRepository.findByEmail("nadaomri2001@gmail.com");

			if (existingAdmin.isEmpty()) {
				// L'utilisateur Admin n'existe pas, nous pouvons le créer
				var admin = RegisterRequestDto.builder()
						.fullName("Nada Omri")
						.email("nadaomri2001@gmail.com")
						.password("12345")
						.canlogin(TRUE)
						.language("Français")
						.role(ADMIN)
						.build();

				System.out.println("Admin token: " + service.register(admin));
			} else {
				// L'utilisateur Admin existe déjà, pas besoin de le recréer
				System.out.println("Admin already exists.");
			}
		};
	}
}
