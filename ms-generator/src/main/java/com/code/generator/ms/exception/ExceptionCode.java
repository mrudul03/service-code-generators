package com.code.generator.ms.exception;

import java.util.HashMap;
import java.util.Map;

import com.code.generator.ms.base.BaseClass;

//@Slf4j
public class ExceptionCode extends BaseClass {
	
	public static class Builder {
		private String resourceName;
		private String templateDir;
		private String templateName;
		private Map<String, String> templateNames = new HashMap<>();
		private String codeGenDirPath;
		private String packageName;
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
		
		public Builder withTemplateDir(String templateDir) {
			this.templateDir = templateDir;
			return this;
		}
		
		public Builder withTemplateName(String templateName) {
			this.templateName = templateName;
			return this;
		}
		
		public Builder withTemplateNames(Map<String, String> templateNames) {
			this.templateNames = templateNames;
			return this;
		}
		
		public ExceptionCode build() {
			ExceptionCode exGenerator = new ExceptionCode();
			exGenerator.resourceName = this.resourceName;
			
			if(null == this.templateDir) {
				this.withTemplateDir("templates");
			}
			if(null == this.templateName || this.templateNames.isEmpty()) {
				this.withTemplateName("ExceptionHelper.template");
				Map<String, String> templateNames = new HashMap<>();
				templateNames.put("ExceptionHelper", "ExceptionHelper.template");
				templateNames.put("InvalidInputException", "InvalidInputException.template");
				templateNames.put("NoDataFoundException", "NoDataFoundException.template");
				this.withTemplateNames(templateNames);
			}
			
			exGenerator.codeGenDirPath = this.codeGenDirPath;
			exGenerator.templateDir = this.templateDir;
			exGenerator.templateName = this.templateName;
			exGenerator.packageName = this.packageName;
			exGenerator.fileExtension = this.fileExtension;
			exGenerator.templateNames = this.templateNames;
			exGenerator.bindingName = "exception";
			
			return exGenerator;
		}
	}

}
