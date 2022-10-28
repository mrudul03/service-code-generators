package com.code.generator.ms.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.code.generator.ms.input.Configurations;
import com.code.generator.ms.input.MsMetaData;
import com.code.generator.ms.output.CodeGenerator;
import com.code.generator.ms.output.Template;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BuildFileGenerator {
	
	@Autowired
	private CodeGenerator codeGenerator;
	
	@Autowired
	private Template template;
	
	public void generate(final MsMetaData metaData, final Configurations configurations) {
		this.generatePom(metaData, configurations);
		this.generateYamlFiles(metaData, configurations);
		this.generateApplication(metaData);
	}
	
	private void generatePom(final MsMetaData metaData, final Configurations configurations) {
		Pom pom = new Pom.Builder(metaData.getResourceName())
				.withCodeGenDirPath(metaData.getBasePath()+configurations.getServiceName()+"/")
				.withTemplateDir("templates")
				.withTemplateName("pom.template")
				.withServiceName(configurations.getServiceName())
				.withDatabaseType(metaData.getDatabaseType())
				.build();
		codeGenerator.generateCode(pom);
		log.info("Generated POM");
	}
	
	private void generateYamlFiles(final MsMetaData metaData, Configurations configurations) {
		YamlFile yamlFile = new YamlFile();
		yamlFile.initialize(configurations);
		yamlFile.setCodeGenDirPath(metaData.getPropertyFilePath());
		yamlFile.setTemplateDir("templates");
		yamlFile.generateCode(template);
		log.info("Generated YAML");
	}
	
	private void generateApplication(final MsMetaData metaData) {
		Application application = new Application.Builder(metaData.getResourceName())
				.withCodeGenDirPath(metaData.getCodeGenDirPath())
				.withPackageName("com.service."+metaData.getResourceName().toLowerCase())
				.withTemplateDir("templates")
				.withTemplateName("Application.template")
				.build();
		codeGenerator.generateCode(application);
		log.info("Generated Application");
	}
	
	

}
