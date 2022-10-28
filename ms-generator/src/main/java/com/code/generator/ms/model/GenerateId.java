package com.code.generator.ms.model;

import com.code.generator.ms.base.BaseClass;

public class GenerateId extends BaseClass {

	public static class Builder {
		private String resourceName;
		private String className;
		
		private String codeGenDirPath;
		private String packageName;
		
		private String templateDir;
		private String templateName;
		
		private String fileExtension;
		
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
		
		public GenerateId build() {
			GenerateId generateId = new GenerateId();
			
			if(null == this.className) {
				this.withClassName(resourceName);
			}
			if(null == this.templateDir) {
				this.withTemplateDir("templates");
			}
			
			if(null == this.templateName) {
				this.withTemplateName("GeneratedId.template");
			}
			
			generateId.className = this.className;
			generateId.templateDir = this.templateDir;
			generateId.templateName = this.templateName;
			generateId.codeGenDirPath = this.codeGenDirPath;
			generateId.packageName = this.packageName;
			generateId.fileExtension = this.fileExtension;
			generateId.bindingName = "generateId";
			
			return generateId;
		}
	}

}
