package com.cnatives.ms.generator.application;

import com.cnatives.ms.generator.base.BaseClass;
import com.cnatives.ms.generator.base.GeneratorMetaData;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
public class ExceptionTemplate extends BaseClass{
	
	private static final String TEMPLATE_NAME = "invalidinputexception.template";
	private static final String EXCEPTION = "exception";
	private static final String FILE_EXTENSION = ".java";
	
	private ExceptionTemplate() {}
	
	private void updateTemplateDetails(GeneratorMetaData metaData) {
		this.templateName = TEMPLATE_NAME;
		this.codeGenDirPath = metaData.getCodeGenDirPath()+EXCEPTION+"/";
		this.fileExtension = FILE_EXTENSION;
		this.bindingName = EXCEPTION;
	}
	
	private void updateClassDetails(GeneratorMetaData metaData) {
		this.packageName = metaData.getBasePackageName()+"."+metaData.getDomainName().toLowerCase()+"."+EXCEPTION;
		this.className = "";
		
		this.templateNames.put("InvalidInputExceptionHandler", "invalidinputexceptionhandler.template");
		this.templateNames.put("NoDataFoundExceptionHandler", "nodatafoundexceptionhandler.template");
		this.templateNames.put("InvalidInputException", "invalidinputexception.template");
		this.templateNames.put("NoDataFoundException", "nodatafoundexception.template");
	}
	
	public static ExceptionTemplateBuilder builder() {
		return new ExceptionTemplateBuilder();
	}
	
	public static class ExceptionTemplateBuilder{
		
		public ExceptionTemplate buildFrom(
				final GeneratorMetaData metaData
				) {
			
			ExceptionTemplate template = new ExceptionTemplate();
			template.updateClassDetails(metaData);
			template.updateTemplateDetails(metaData);
			return template;
		}
	}

}
