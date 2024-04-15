package com.juansa.msvcprocedimientos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcProcedimientosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcProcedimientosApplication.class, args);
	}

}
