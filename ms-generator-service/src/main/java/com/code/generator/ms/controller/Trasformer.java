package com.code.generator.ms.controller;

import java.util.List;

import com.code.generator.ms.base.BaseClass;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Trasformer extends BaseClass {
	
	private List<TransformModel> transformModels;
	private TransformModel transformModel;
	private String imports;
	
	private Trasformer() {}
	
	public List<TransformModel> getTransformModels(){
		return this.transformModels;
	}
	
	public TransformModel getTransformModel(){
		return this.transformModel;
	}
	
	public String getImports() {
		return this.imports;
	}
	
	public static class Builder {
		private String resourceName;
		private String className;
		
		private String codeGenDirPath;
		private String packageName;
		
		private String templateDir;
		private String templateName;
		
		private String fileExtension;
		private List<TransformModel> tModels;
		private TransformModel transformModel;
		
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
		
		public Builder withTransformModels(List<TransformModel> tModels) {
			this.tModels = tModels;
			return this;
		}
		
		public Builder withTransformModel(TransformModel transformModel) {
			this.transformModel = transformModel;
			return this;
		}
		
		public Trasformer build() {
			Trasformer transformer = new Trasformer();
			
			if(null == this.className) {
				this.withClassName(resourceName);
			}
			if(null == this.templateDir) {
				this.withTemplateDir("templates");
			}
			
			if(null == this.templateName) {
				this.withTemplateName("Model.template");
			}
			
			transformer.className = this.className;
			transformer.templateDir = this.templateDir;
			transformer.templateName = this.templateName;
			transformer.codeGenDirPath = this.codeGenDirPath;
			transformer.packageName = this.packageName;
			transformer.transformModels = this.tModels;
			transformer.transformModel = this.transformModel;
			
			transformer.fileExtension = this.fileExtension;
			transformer.imports = "import com.service."+this.resourceName.toLowerCase()+".model.*;\n"
					+"import com.service."+this.resourceName.toLowerCase()+".contract.*;";
			
			transformer.bindingName = "transformer";
			log.info("Built Transform");
			return transformer;
		}
	}

}
