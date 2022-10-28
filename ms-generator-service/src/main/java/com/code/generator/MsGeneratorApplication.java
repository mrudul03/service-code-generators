package com.code.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.code.generator.model.MsGeneratorService;
import com.code.generator.ms.output.Template;

@SpringBootApplication
public class MsGeneratorApplication implements CommandLineRunner{
	
	@Autowired
	MsGeneratorService msGeneratorService;
	
	@Autowired
	private Template template;
	
	public static void main(String[] args) {
		SpringApplication.run(MsGeneratorApplication.class, args);
	}
	
	@Override
	public void run(String... arg) throws Exception {
		msGeneratorService.deleteAll();
		msGeneratorService.init();
		template.loadTemplates();
	}
}
