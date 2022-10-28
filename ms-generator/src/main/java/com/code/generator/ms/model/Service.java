package com.code.generator.ms.model;

import com.code.generator.ms.base.BaseClass;

//@Slf4j
public class Service extends BaseClass {
	
	private String contractImport;
	private String respositoryImport;
	//private String exceptionImport;
	private String noDataFoundExceptionImport;
	private String invalidDataExceptionImport;
	
	private String contractName;
	private String modelName;
	private String pkDatatype;
	private String idFieldName;
	
	public String getContractImport() {
		return contractImport;
	}
	public String getRespositoryImport() {
		return respositoryImport;
	}
//	public String getExceptionImport() {
//		return exceptionImport;
//	}
	public String getNoDataFoundExceptionImport() {
		return noDataFoundExceptionImport;
	}
	public String getInvalidDataExceptionImport() {
		return invalidDataExceptionImport;
	}
	public String getContractName() {
		return contractName;
	}
	public String getModelName() {
		return modelName;
	}
	public String getRespositoryName() {
		return this.modelName+"Repository";
	}
	public String getPkDatatype() {
		return pkDatatype;
	}
	public String getIdFieldName() {
		return this.idFieldName;
	}

	public static class Builder {
		private String resourceName;
		private String templateDir;
		private String templateName;
		private String codeGenDirPath;
		private String packageName;
		
		private String className;
		private String fileExtension;
		private String pkDatatype;
		
		private String modelName;
		private String idFieldName;
		
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
		public Builder withPkDatatype(String pkDatatype){
			this.pkDatatype = pkDatatype;
			return this;
		}
		public Builder withIdFieldName(String idFieldName){
			this.idFieldName = idFieldName;
			return this;
		}
		
		
		public Service build() {
			Service service = new Service();
			
			if(null == this.className) {
				this.withClassName(modelName+"Service");
			} 
			if(null == this.templateDir) {
				this.withTemplateDir("templates");
			}
			if(null == this.templateName) {
				this.withTemplateName("Service.template");
			}
			
			service.className = this.className;
			service.codeGenDirPath = this.codeGenDirPath;
			service.templateDir = this.templateDir;
			service.templateName = this.templateName;
			service.packageName = this.packageName;
			service.fileExtension = this.fileExtension;
			
			service.contractImport = "com.service."+this.resourceName.toLowerCase()+".contract.*";;
			service.respositoryImport = "com.service."+this.resourceName.toLowerCase()+".repository.*";
//			service.exceptionImport = "com.service."+this.resourceName.toLowerCase()+".exception.NoDataFoundException; \n"
//					+ "import com.service."+this.resourceName.toLowerCase()+".exception.InvalidInputException;";
			
			service.noDataFoundExceptionImport = "com.service."+this.resourceName.toLowerCase()+".exception.NoDataFoundException";
			service.invalidDataExceptionImport = "com.service."+this.resourceName.toLowerCase()+".exception.InvalidInputException";
			
			service.contractName = this.modelName+"Request";
			
			service.modelName = this.modelName;
			service.bindingName = "service";
			service.pkDatatype = this.pkDatatype;
			service.idFieldName = this.idFieldName;
			
			return service;
		}
	}
}
