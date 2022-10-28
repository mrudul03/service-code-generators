package com.code.generator.ms.application;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.code.generator.ms.input.Configurations;
import com.code.generator.ms.output.Template;
import com.hubspot.jinjava.Jinjava;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Component
public class YamlFile {
	
//	@Autowired
//	private Template template;
	
	private String servicePort;
	private String serviceName;
	private String dbUrl;
	private String dbusername;
	private String dbpassword;
	private String dbDriver;
	private String schemaName;
	
	private String applicationTemplate;
	private String bootstrapTemplate;
	
	protected String templateDir;
	protected String templateName;
	protected String codeGenDirPath;
	private String fileExtension;
	
	public void initialize(final Configurations configurations) {
		this.fileExtension = ".yaml";
		this.servicePort = configurations.getServicePort();
		this.serviceName = configurations.getServiceName();
		this.dbUrl = configurations.getDbUrl();
		this.dbusername = configurations.getDbusername();
		this.dbpassword = configurations.getDbpassword();
		this.dbDriver = configurations.getDbDriver();
		this.schemaName = configurations.getSchemaName();
		this.applicationTemplate = "applicationYaml.template";
		this.bootstrapTemplate = "bootstrapYaml.template";
	}
	
	private Map<String, YamlFile> getModelBinding() {
		Map<String, YamlFile> bindings = new HashMap<>();
		bindings.put("configuration", this);
		return bindings;
	}
	
	public void generateCode(final Template template) {
		try {
			Jinjava jinjava = new Jinjava();
			Map<String, YamlFile> bindings = this.getModelBinding();
			
			//Template appTemplate = new Template(this.applicationTemplate, this.templateDir);
			final String content = template.getTemplateContent(this.applicationTemplate);
			String fileContent = jinjava.render(content, bindings);
			this.createFile(codeGenDirPath, "application", fileContent, this.fileExtension);
			
			//Template bootstrapTemplate = new Template(this.bootstrapTemplate, this.templateDir);
			final String bootstrapContent = template.getTemplateContent(this.bootstrapTemplate);
			String bootstrapFileContent = jinjava.render(bootstrapContent, bindings);
			this.createFile(codeGenDirPath, "bootstrap", bootstrapFileContent, this.fileExtension);
		}
		catch(Exception e) {
			//e.printStackTrace();
			log.error("BaseModel Error:", e);
		}
	}
	
	protected void createFile(String filePath, String fileName, String fileContent, String fileExtension) {
		
		try {
			Path path = Paths.get(filePath+fileName+fileExtension);
			Files.createDirectories(path.getParent());
			Files.write(path, fileContent.getBytes());
		}
		catch(Exception e) {
			//e.printStackTrace();
			log.error("BaseMode Error creating YAML file:", e);
		}
		
	}
}
