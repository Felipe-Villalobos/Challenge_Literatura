package com.alura.challenge.literatura;
import com.alura.challenge.literatura.Principal.Principal;
import com.alura.challenge.literatura.repository.iAutorRepository;
import com.alura.challenge.literatura.repository.iLibroRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class LiteraturaApplication implements CommandLineRunner {


	@Autowired
	private iLibroRepository libroRepository;
	@Autowired
	private iAutorRepository autorRepository;		   
	public static void main(String[] args) {
		SpringApplication.run(LiteraturaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal(libroRepository, autorRepository);
		principal.consumo();

	}
}
