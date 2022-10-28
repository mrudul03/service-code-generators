package com.code.generator.ms.test;

import java.util.List;

import com.code.generator.ms.base.BaseClass;
import com.code.generator.ms.model.ModelHolder;

public class ServiceTest extends BaseClass {

    private String respositoryImport;
    private String noDataFoundExceptionImport;
    private String invalidDataExceptionImport;
    private String serviceName;
    private String modelName;
    private String pkDatatype;
    private String repositoryName;
    private List<ModelHolder> testModels;
    
    public String getRespositoryImport() {
		return respositoryImport;
	}
	public String getNoDataFoundExceptionImport() {
		return noDataFoundExceptionImport;
	}
	public String getInvalidDataExceptionImport() {
		return invalidDataExceptionImport;
	}
	public String getModelName() {
		return modelName;
	}
	public String getPkDatatype() {
		return pkDatatype;
	}
	public String getServiceName() {
		return serviceName;
	}
	public String getRepositoryName() {
		return repositoryName;
	}
	public List<ModelHolder> getTestModels() {
		return testModels;
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
        private String serviceName;
        private String repositoryName;
        
        private List<ModelHolder> testModels;

        public Builder(final String resourceName, final String modelName) {
            this.resourceName = resourceName;
            this.modelName = modelName;
            this.fileExtension = ".java";
        }
        public ServiceTest.Builder withPackageName(final String packageName) {
            this.packageName = packageName;
            return this;
        }
        public ServiceTest.Builder withCodeGenDirPath(final String codeGenDirPath) {
            this.codeGenDirPath = codeGenDirPath;
            return this;
        }
        public ServiceTest.Builder withClassName(String className){
            this.className = className;
            return this;
        }
        public ServiceTest.Builder withTemplateDir(String templateDir) {
            this.templateDir = templateDir;
            return this;
        }
        public ServiceTest.Builder withTemplateName(String templateName) {
            this.templateName = templateName;
            return this;
        }
        public ServiceTest.Builder withPkDatatype(String pkDatatype){
            this.pkDatatype = pkDatatype;
            return this;
        }
        public ServiceTest.Builder withServiceName(String serviceName){
            this.serviceName = serviceName;
            return this;
        }
        public ServiceTest.Builder withRepositoryName(String repositoryName){
            this.repositoryName = repositoryName;
            return this;
        }
        public ServiceTest.Builder withModelHolders(List<ModelHolder> testModels){
            this.testModels = testModels;
            return this;
        }

        public ServiceTest build(){
            ServiceTest serviceTest = new ServiceTest();
            if(null == this.className) {
                this.withClassName(modelName+"ServiceTest");
            }
            if(null == this.templateDir) {
                this.withTemplateDir("templates");
            }
            if(null == this.templateName) {
                this.withTemplateName("ServiceTest.template");
            }
            serviceTest.className = this.className;
            serviceTest.codeGenDirPath = this.codeGenDirPath;
            serviceTest.templateDir = this.templateDir;
            serviceTest.templateName = this.templateName;
            serviceTest.packageName = this.packageName;
            serviceTest.fileExtension = this.fileExtension;

            serviceTest.respositoryImport = "com.service."+this.resourceName.toLowerCase()+".repository.*";
            serviceTest.noDataFoundExceptionImport = "com.service."+this.resourceName.toLowerCase()+".exception.NoDataFoundException";
            serviceTest.invalidDataExceptionImport = "com.service."+this.resourceName.toLowerCase()+".exception.InvalidInputException";

            serviceTest.bindingName = "servicetest";
            
            serviceTest.modelName = this.modelName;
            serviceTest.pkDatatype = this.pkDatatype;
            serviceTest.serviceName = this.serviceName;
            serviceTest.repositoryName = this.repositoryName;
            serviceTest.testModels = this.testModels;

            return serviceTest;
        }

    }



}
