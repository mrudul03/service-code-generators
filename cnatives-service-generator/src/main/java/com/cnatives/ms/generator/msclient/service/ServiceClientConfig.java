package com.cnatives.ms.generator.msclient.service;

import com.cnatives.ms.generator.msservice.base.BaseClass;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
public class ServiceClientConfig extends BaseClass {
	
	private static final String TEMPLATE_DIR = "templates";
	private static final String BINDING_NAME = "serviceclientconfig";
	private static final String TEMPLATE = "serviceclientconfig.template";
	private static final String FILE_EXTENSION = ".java";
	
	public ServiceClientConfig(String packageName, String codeGenDirPath) {
		this.packageName = packageName;
		this.codeGenDirPath = codeGenDirPath;
		this.templateDir = TEMPLATE_DIR;
		this.templateName = TEMPLATE;
		this.bindingName = BINDING_NAME;
		this.fileExtension = FILE_EXTENSION;
		this.className ="CustomFeignConfig";
		
	}

}
