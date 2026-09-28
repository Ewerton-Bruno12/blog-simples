package com.ewerton.blog_simples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BlogSimplesApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogSimplesApplication.class, args);
	}

}
