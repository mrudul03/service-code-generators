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

}
