package com.release.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ReleaseTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReleaseTrackerApplication.class, args);
	}

}
