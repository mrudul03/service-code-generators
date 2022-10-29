package com.cnatives.ms.generator.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.cnatives.ms.contract.Configurations;
import com.cnatives.ms.generator.output.Template;
import com.hubspot.jinjava.Jinjava;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class EnvTemplate {
	
	private String dbUrl;
	private String dbusername;
	private String dbpassword;
	private String dbDriver;
	private String schemaName;
	
	private String templateDir;
	private String templateName;
	private String codeGenDirPath;
	private String fileExtension;
	private String template;
	
	public void initialize(final Configurations configurations) {
		this.fileExtension = ".js";
		this.dbUrl = configurations.getDbUrl();
		this.dbusername = configurations.getDbusername();
		this.dbpassword = configurations.getDbpassword();
		this.dbDriver = configurations.getDbDriver();
		this.schemaName = configurations.getSchemaName();
		this.template = "env.template";
	}
	
	private Map<String, EnvTemplate> getModelBinding() {
		Map<String, EnvTemplate> bindings = new HashMap<>();
		bindings.put("env", this);
		return bindings;
	}
	
	public void generateCode(final Template template) {
		try {
			Jinjava jinjava = new Jinjava();
			Map<String, EnvTemplate> bindings = this.getModelBinding();
			
			final String content = template.getTemplateContent(this.template);
			String fileContent = jinjava.render(content, bindings);
			this.createFile(codeGenDirPath, "env", fileContent, this.fileExtension);
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
			log.error("BaseMode Error creating DB Config file:", e);
		}
	}

}
