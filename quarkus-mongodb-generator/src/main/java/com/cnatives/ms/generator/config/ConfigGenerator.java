package com.cnatives.ms.generator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnatives.ms.contract.Configurations;
import com.cnatives.ms.generator.base.GeneratorMetaData;
import com.cnatives.ms.generator.output.CodeGenerator;
import com.cnatives.ms.generator.output.Template;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConfigGenerator {
	
	@Autowired
	private CodeGenerator codeGenerator;
	
	@Autowired
	private Template template;
	
	public void generateOtherfiles(GeneratorMetaData metaData, final Configurations configurations) {
		this.generatePom(metaData, configurations);
		//this.generateYamlFiles(metaData, configurations);
		this.generatePropertiesFiles(metaData, configurations);
	}
	
	private void generatePom(GeneratorMetaData metaData, final Configurations configurations) {
		PomTemplate pomTemplate = PomTemplate.builder().buildFrom(metaData);
		codeGenerator.generateCode(pomTemplate);
		log.info("Generated POM");
	}
	
	private void generatePropertiesFiles(final GeneratorMetaData metaData, Configurations configurations) {
		PropertiesTemplate propertiesFile = new PropertiesTemplate();
		propertiesFile.initialize(configurations);
		propertiesFile.setCodeGenDirPath(metaData.getPropertyFilePath());
		propertiesFile.setTemplateDir("templates");
		propertiesFile.generateCode(template);
		log.info("Generated Properties");
	}

}
