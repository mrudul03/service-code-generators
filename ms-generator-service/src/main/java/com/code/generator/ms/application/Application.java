package com.code.generator.ms.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.code.generator.ms.base.BaseClass;

public class Application extends BaseClass {
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
	
	private Application() {
	}

	public static class Builder {
		private String resourceName;
		private String templateDir;
		private String templateName;
		private String packageName;
		private String codeGenDirPath;
		
		private String className;
		private String fileExtension;
		
		public Builder(final String resourceName) {
			this.resourceName = resourceName;
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
		
		public Builder withClassName(String className){
			this.className = className;
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
		
		
		public Application build() {
			
			Application application = new Application();
			application.resourceName = this.resourceName;
			
			if(null == this.className) {
				this.withClassName(resourceName+"Application");
			} 
			if(null == this.templateDir) {
				this.withTemplateDir("templates");
			}
			
			if(null == this.templateName) {
				this.withTemplateName("Application.template");
			}
			
			application.className = this.className;
			application.codeGenDirPath = this.codeGenDirPath;
			application.templateDir = this.templateDir;
			application.templateName = this.templateName;
			application.packageName = this.packageName;
			application.fileExtension = this.fileExtension;
			application.bindingName = "application";
			LOGGER.info("Built Application");
			return application;
		}

	}
	
//	protected Map<String, ? extends BaseModel> getModelBinding(){
//		LOGGER.info("rendering Application");
//		Map<String, Application> bindings = new HashMap<>();
//		bindings.put("application", this);
//		return bindings;
//	}
	
}
