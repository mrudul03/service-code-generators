package com.cnatives.ms.generator.msservice.output;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cnatives.ms.generator.msservice.base.MainGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Template {
	
	private Map<String, String> templateMap = new HashMap<>();
	private final String tenplateDir = "templates";
	private String[] templateArray = {
			"controller.template",
			"contract.template",
			"domain.template",
			"service.template",
			"repository.template",
			"application.template",
			"exceptionhelper.template",
			"invalidinputexception.template",
			"nodatafoundexception.template",
			"pom.template",
			"applicationYaml.template",
			"bootstrapYaml.template",
			"V1.0__create_table.template",
			"serviceclient.template",
			"serviceclientconfig.template"
			};
	
	public void loadTemplates() {
		for(String templateName: templateArray) {
			String content = this.getResourceFileAsString(templateName);
			templateMap.put(templateName, content);
		}
		log.info("Loaded  templates");
	}
	
	private String getResourceFileAsString(String fileName) {
		//log.info("FileName:"+tenplateDir+"/"+fileName);
	    InputStream is = getResourceFileAsInputStream(tenplateDir+"/"+fileName);
	    if (is != null) {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        return (String)reader.lines().collect(Collectors.joining(System.lineSeparator()));
	    } else {
	        throw new RuntimeException("resource not found");
	    }
	}

	private InputStream getResourceFileAsInputStream(String fileName) {
	    ClassLoader classLoader = MainGenerator.class.getClassLoader();
	    return classLoader.getResourceAsStream(fileName);
	}
	
	public String getTemplateContent(String templateName) {
		return templateMap.get(templateName);
	}

}
