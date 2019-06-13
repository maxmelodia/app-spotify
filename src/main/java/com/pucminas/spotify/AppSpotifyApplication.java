package com.pucminas.spotify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class AppSpotifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppSpotifyApplication.class, args);
	}

}
