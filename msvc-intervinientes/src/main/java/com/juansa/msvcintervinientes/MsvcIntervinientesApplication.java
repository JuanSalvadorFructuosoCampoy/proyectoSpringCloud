package com.juansa.msvcintervinientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients

@SpringBootApplication
public class MsvcIntervinientesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcIntervinientesApplication.class, args);
	}

}
