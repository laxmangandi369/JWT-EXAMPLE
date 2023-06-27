package com.jwt;

import com.jwt.model.Role;
import com.jwt.model.User;
import com.jwt.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;

@SpringBootApplication
@EnableWebSecurity
@EnableJpaRepositories
public class SecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityJwtApplication.class, args);
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			userService.saveRole(new Role(null, "ROLE_USER", "this is User"));
			userService.saveRole(new Role(null, "ROLE_ADMIN", "this is User"));
			userService.saveRole(new Role(null, "ROLE_MANAGER", "this is User"));

			userService.saveUser(new User("9890325433", "omkar", "omkar@gmail.com","pass", new HashSet<>()));
			userService.saveUser(new User("9890325433", "omkar1", "omkar1@gmail.com","pass", new HashSet<>()));
			userService.saveUser(new User("9890325433", "omkar2", "omkar2@gmail.com","pass", new HashSet<>()));

			userService.addUser("omkar@gmail.com", "ROLE_USER");
			userService.addUser("omkar1@gmail.com", "ROLE_ADMIN");
			userService.addUser("omkar2@gmail.com", "ROLE_MANAGER");
		};
	}
}
