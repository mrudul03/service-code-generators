package com.code.generator.ms.controller;

import com.code.generator.ms.base.BaseClass;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Controller extends BaseClass {
	
	private String requestMapping;
	private String pathMapping;
	
	private String modelImport;
	private String contractImport;
	private String respositoryImport;
	private String transformImport;
	private String exceptionImport;
	
	private String contractName;
	private String modelName;
	
	private String pathMappingGet;
	private String pathMappingPost;
	private String pathMappingPut;
	private String pkDatatype;
	
	private Controller() {}
	
	public String getRequestMapping() {
		return requestMapping;
	}
	public String getPathMapping() {
		return pathMapping;
	}
	public String getModelImport() {
		return modelImport;
	}
	public String getContractImport() {
		return contractImport;
	}
	public String getRespositoryImport() {
		return respositoryImport;
	}
	public String getTransformImport() {
		return transformImport;
	}
	public String getExceptionImport() {
		return exceptionImport;
	}
	public String getContractName() {
		return contractName;
	}
	public String getModelName() {
		return modelName;
	}
	public String getPathMappingGet() {
		return pathMappingGet;
	}
	public String getPathMappingPost() {
		return pathMappingPost;
	}
	public String getPathMappingPut() {
		return pathMappingPut;
	}
	public String getRespositoryName() {
		return this.modelName+"Repository";
	}
	public String getTransformerName() {
		return this.modelName+"Transformer";
	}
	public String getPkDatatype() {
		return pkDatatype;
	}

	public static class Builder {
		private String resourceName;
		private String templateDir;
		private String templateName;
		private String codeGenDirPath;
		private String packageName;
		
		private String className;
		private String requestMapping;
		private String pathMapping;
		
		private String fileExtension;
		private String pkDatatype;
		
		private String modelName;
		
		public Builder(final String resourceName, final String modelName) {
			this.resourceName = resourceName;
			this.modelName = modelName;
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
		
		public Builder withRequestMapping(String requestMapping){
			this.requestMapping = requestMapping;
			return this;
		}
		
		public Builder withPathMapping(String pathMapping) {
			this.pathMapping = pathMapping;
			return this;
		}
		public Builder withPkDatatype(String pkDatatype){
			this.pkDatatype = pkDatatype;
			return this;
		}
		
		
		public Controller build() {
			Controller controller = new Controller();
			controller.resourceName = this.resourceName;
			
			if(null == this.className) {
				this.withClassName(modelName+"Controller");
			} 
			if(null == this.templateDir) {
				this.withTemplateDir("templates");
			}
			if(null == this.templateName) {
				this.withTemplateName("Controller.template");
			}
			if(null == this.pathMapping) {
				this.withPathMapping("{name}");
			}
			
			controller.className = this.className;
			controller.requestMapping = this.requestMapping;
			controller.pathMapping = this.pathMapping;
			controller.codeGenDirPath = this.codeGenDirPath;
			controller.templateDir = this.templateDir;
			controller.templateName = this.templateName;
			controller.packageName = this.packageName;
			controller.fileExtension = this.fileExtension;
			
			controller.modelImport = "com.service."+this.resourceName.toLowerCase()+".model.*";
			controller.contractImport = "com.service."+this.resourceName.toLowerCase()+".contract.*";;
			controller.respositoryImport = "com.service."+this.resourceName.toLowerCase()+".repository.*";
			controller.transformImport = "com.service."+this.resourceName.toLowerCase()+".transform.*";
			controller.exceptionImport = "com.service."+this.resourceName.toLowerCase()+".exception.*";
			
			//controller.contractName = this.resourceName+"Contract";
			controller.contractName = this.modelName+"Contract";
			
			controller.modelName = this.modelName;
			controller.pathMappingGet = this.modelName.toLowerCase()+"s/{id}";
			controller.pathMappingPost = this.modelName.toLowerCase()+"s";
			controller.pathMappingPut = this.modelName.toLowerCase()+"s/{id}";
			controller.bindingName = "controller";
			controller.pkDatatype = this.pkDatatype;
			
			log.info("Built Controller");
			return controller;
		}
	}
	
}
