package com.cnatives.ms.generator.output;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.cnatives.ms.generator.base.BaseClass;
import com.hubspot.jinjava.Jinjava;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CodeGenerator {
	
	private Template template;
	
	public CodeGenerator(final Template template) {
		this.template = template;
	}
	
	public void generateCode(final BaseClass baseClass) {
		String templateName = baseClass.getTemplateName();
		try {
			final String content = template.getTemplateContent(templateName);
			
			Jinjava jinjava = new Jinjava();
			Map<String, ? extends BaseClass> bindings = this.getModelBinding(baseClass);
			
			String fileContent = jinjava.render(content, bindings);
			this.createFile(baseClass.getCodeGenDirPath(), 
					baseClass.getClassName(), fileContent, baseClass.getFileExtension());
		}
		catch(Exception e) {
			log.error("Error Generating Code:", e);
		}
	}
	
	public void generateCodeForTemplates(final BaseClass baseClass) {
		Map<String, String> templatesMap = baseClass.getTemplateNames();
		try {
			Set<String> classNames = templatesMap.keySet();
			for(String className: classNames) {
				String templateName = templatesMap.get(className);
				final String content = template.getTemplateContent(templateName);
				Jinjava jinjava = new Jinjava();
				Map<String, ? extends BaseClass> bindings = this.getModelBinding(baseClass);
				
				String fileContent = jinjava.render(content, bindings);
				this.createFile(baseClass.getCodeGenDirPath(), 
						className, fileContent, baseClass.getFileExtension());
			}
		}
		catch(Exception e) {
			log.error("Error Generating Code:", e);
		}
	}
	
	protected Map<String, ? extends BaseClass> getModelBinding(final BaseClass baseClass){
		
		Map<String, BaseClass> bindings = new HashMap<>();
		bindings.put(baseClass.getBindingName(), baseClass);
		return bindings;
	}	 
	
	protected void createFile(String filePath, String fileName, String fileContent, String fileExtension) {
		try {
			Path path = Paths.get(filePath+fileName+fileExtension);
			Files.createDirectories(path.getParent());
			Files.write(path, fileContent.getBytes());
		}
		catch(Exception e) {
			log.error("Error writing file:", e);
		}
	}
}
