package com.cnatives.ms.generator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cnatives.ms.generator.msservice.output.Template;

@SpringBootApplication
public class CodeGeneratorApplication implements CommandLineRunner{
	
	private CodeGeneratorStarter codeGeneratorStarter;
	private Template template;
	
	public CodeGeneratorApplication(CodeGeneratorStarter codeGeneratorStarter,
			Template template) {
		
		this.codeGeneratorStarter = codeGeneratorStarter;
		this.template = template;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(CodeGeneratorApplication.class, args);
	}
	
	@Override
	public void run(String... arg) throws Exception {
		codeGeneratorStarter.deleteAll();
		codeGeneratorStarter.init();
		template.loadTemplates();
	}

}
