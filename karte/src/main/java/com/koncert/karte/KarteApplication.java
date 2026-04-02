package com.koncert.karte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class KarteApplication {

	public static void main(String[] args) {
		SpringApplication.run(KarteApplication.class, args);
	}

}
