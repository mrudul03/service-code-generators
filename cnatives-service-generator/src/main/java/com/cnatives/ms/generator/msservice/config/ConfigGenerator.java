package com.cnatives.ms.generator.msservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnatives.ms.generator.mscontroller.Configurations;
import com.cnatives.ms.generator.msservice.base.GeneratorMetaData;
import com.cnatives.ms.generator.msservice.output.CodeGenerator;
import com.cnatives.ms.generator.msservice.output.Template;

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
		this.generateYamlFiles(metaData, configurations);
	}
	
	
	private void generatePom(GeneratorMetaData metaData, final Configurations configurations) {
		PomTemplate pomTemplate = PomTemplate.builder().buildFrom(metaData);
		codeGenerator.generateCode(pomTemplate);
		log.info("Generated POM");
	}
	
	private void generateYamlFiles(final GeneratorMetaData metaData, Configurations configurations) {
		YamlTemplate yamlFile = new YamlTemplate();
		yamlFile.initialize(configurations);
		yamlFile.setCodeGenDirPath(metaData.getPropertyFilePath());
		yamlFile.setTemplateDir("templates");
		yamlFile.generateCode(template);
		log.info("Generated YAML");
	}

}
