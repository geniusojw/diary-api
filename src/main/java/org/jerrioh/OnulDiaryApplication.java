package org.jerrioh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OnulDiaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnulDiaryApplication.class, args);
	}

}
