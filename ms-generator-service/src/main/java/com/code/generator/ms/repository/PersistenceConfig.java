package com.code.generator.ms.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.code.generator.ms.base.BaseClass;

public class PersistenceConfig extends BaseClass {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceConfig.class);
	
	private String generatedIdPkgName;
	
	public String getGeneratedIdPkgName() {
		return this.generatedIdPkgName;
	}
	
	public static class Builder {
		private String resourceName;
		private String className;
		
		private String codeGenDirPath;
		private String packageName;
		
		private String templateDir;
		private String templateName;
		
		private String fileExtension;
		private String generatedIdPkgName;
		
		public Builder(final String modelName) {
			this.resourceName = modelName;
			this.fileExtension = ".java";
		}
		
		public Builder withPackageName(final String packageName) {
			this.packageName = packageName;
			return this;
		}
		
		public Builder withCodeGenDirPath(final String codeGenDirPath) {
			this.codeGenDirPath = codeGenDirPath;
			return this;
		}
		
		public Builder withTemplateDir(String templateDir) {
			this.templateDir = templateDir;
			return this;
		}
		
		public Builder withTemplateName(String templateName) {
			this.templateName = templateName;
			return this;
		}
		
		public Builder withClassName(String className){
			this.className = className;
			return this;
		}
		
		public Builder withGeneratedIdPkgName(String generatedIdPkgName){
			this.generatedIdPkgName = generatedIdPkgName;
			return this;
		}
		
		public PersistenceConfig build() {
			PersistenceConfig config = new PersistenceConfig();
			
			if(null == this.className) {
				this.withClassName(resourceName);
			}
			if(null == this.templateDir) {
				this.withTemplateDir("templates");
			}
			
			if(null == this.templateName) {
				this.withTemplateName("Config.template");
			}
			
			config.className = this.className;
			config.templateDir = this.templateDir;
			config.templateName = this.templateName;
			config.codeGenDirPath = this.codeGenDirPath;
			config.packageName = this.packageName;
			config.fileExtension = this.fileExtension;
			config.bindingName = "config";
			config.generatedIdPkgName = generatedIdPkgName;
			
			LOGGER.info("Built Config");
			return config;
		}
	}

}
