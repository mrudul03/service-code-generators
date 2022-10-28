package com.code.generator.ms.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.code.generator.ms.base.BaseClass;

public class Repository extends BaseClass {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Repository.class);
	
	private String modelImport;
	private String modelClassName;
	private String pkDatatype;
	
	private String packageName;
	
	public String getPackageName() {
		return packageName;
	}

	public String getClassName() {
		return className;
	}
	
	public String getModelImport() {
		return modelImport;
	}

	public String getModelClassName() {
		return modelClassName;
	}

	public String getPkDatatype() {
		return pkDatatype;
	}
	
	public static class Builder {
		private String resourceName;
		private String className;
		
		private String codeGenDirPath;
		private String packageName;
		
		private String templateDir;
		private String templateName;
		
		private String fileExtension;
		private String modelImport;
		private String modelClassName;
		private String pkDatatype;
		
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
			this.className = this.resourceName+"Repository";
			return this;
		}
		
		public Builder withModelImport(String modelImport){
			this.modelImport = modelImport;
			return this;
		}
		public Builder withModelClassName(String modelClassName){
			this.modelClassName = modelClassName;
			return this;
		}
		public Builder withPkDatatype(String pkDatatype){
			this.pkDatatype = pkDatatype;
			return this;
		}
		
		public Repository build() {
			Repository repository = new Repository();
			if(null == this.className) {
				this.withClassName(resourceName);
			}
			if(null == this.templateDir) {
				this.withTemplateDir("templates");
			}
			
			if(null == this.templateName) {
				this.withTemplateName("Model.template");
			}
			
			repository.className = this.className;
			repository.templateDir = this.templateDir;
			repository.templateName = this.templateName;
			repository.codeGenDirPath = this.codeGenDirPath;
			repository.packageName = this.packageName;
			repository.fileExtension = this.fileExtension;
			repository.modelImport = this.modelImport;
			repository.modelClassName = this.modelClassName;
			repository.pkDatatype = this.pkDatatype;
			repository.bindingName = "repository";
			LOGGER.info("Built Repository");
			return repository;
		}
	}

//	@Override
//	protected Map<String, ? extends BaseModel> getModelBinding() {
//		LOGGER.info("rendering repository");
//		Map<String, Repository> bindings = new HashMap<>();
//		bindings.put("repository", this);
//		return bindings;
//	}

}
