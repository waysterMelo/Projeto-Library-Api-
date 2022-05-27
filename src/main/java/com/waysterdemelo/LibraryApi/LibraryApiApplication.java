package com.waysterdemelo.LibraryApi;

import com.waysterdemelo.LibraryApi.service.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class LibraryApiApplication {

	@Autowired
	private EmailService emailService;

	@Bean
	public CommandLineRunner runner(){
		return args -> {
			List<String> mails = Arrays.asList("736b2591be-38f6a6@inbox.mailtrap.io");
			emailService.sendEmail("TESTANDO SERVIÇO DE MENSAGEM", mails);
			System.out.println("Email enviado com sucesso");
		};
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Scheduled(cron = "0 57 18 1/1 * ?")
	public void testeAgendamentosDeTarefas(){
		System.out.println("Agendamento de tarefas funcionando com sucesso");
	}

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}

}
