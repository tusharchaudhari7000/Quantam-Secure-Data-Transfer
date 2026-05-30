package com.dypcoe.qsdta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class QsdtaApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(QsdtaApplication.class, args);
		System.out.println("Server is running");
	}
}



