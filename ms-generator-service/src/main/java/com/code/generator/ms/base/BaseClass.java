package com.code.generator.ms.base;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class BaseClass {
	
	protected String templateDir;
	protected String templateName;
	protected String codeGenDirPath;
	protected String className;
	protected String name;
	protected String fileExtension;
	protected String packageName;
	protected String resourceName;
	protected String serviceName;
	protected String bindingName;
	protected Map<String, String> templateNames = new HashMap<>();
	
//	public String getTemplateDir() {
//		return templateDir;
//	}
//	public String getTemplateName() {
//		return templateName;
//	}
//	public String getCodeGenDirPath() {
//		return codeGenDirPath;
//	}
//	public String getClassName() {
//		return className;
//	}
//	public String getName() {
//		return name;
//	}
//	public String getFileExtension() {
//		return fileExtension;
//	}
//	public String getPackageName() {
//		return packageName;
//	}
//	public String getResourceName() {
//		return resourceName;
//	}
//	public String getServiceName() {
//		return serviceName;
//	}
//	public String getBindingName() {
//		return bindingName;
//	}

}
