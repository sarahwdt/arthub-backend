package org.sarahwdt.arthub;

import org.springframework.boot.SpringApplication;

public class TestArthubApplication {

	public static void main(String[] args) {
		SpringApplication.from(ArthubApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
