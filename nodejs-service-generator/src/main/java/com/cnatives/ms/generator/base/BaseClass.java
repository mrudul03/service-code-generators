package com.cnatives.ms.generator.base;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class BaseClass {
	
	@Getter
	protected String templateDir;
	
	@Getter
	protected String templateName;
	
	@Getter
	protected String codeGenDirPath;
	
	@Getter
	protected String className;
	
	@Getter
	protected String fileExtension;
	
	@Getter
	protected String packageName;
	
	@Getter
	protected String bindingName;
	
	@Getter
	protected String domainName;
	
	@Getter
	protected String serviceName;
	
	@Getter
	protected Map<String, String> templateNames = new HashMap<>();
	
	protected String createVariable(String name) {
		String result = "";
		if (null != name && name.length() > 0) {
			// Append first character(in lower case)
			char c = name.charAt(0);
			result = Character.toLowerCase(c) + name.substring(1);

		} else {
			result = "";
		}
		return result;
	}

}
