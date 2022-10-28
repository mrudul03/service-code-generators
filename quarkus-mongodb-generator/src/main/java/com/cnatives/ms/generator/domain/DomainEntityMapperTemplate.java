package com.cnatives.ms.generator.domain;

import java.util.ArrayList;
import java.util.List;

import com.cnatives.ms.generator.base.BaseClass;
import com.cnatives.ms.generator.base.GeneratorMetaData;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper=true)
public class DomainEntityMapperTemplate extends BaseClass{
	
	private static final String DOMAINMAPPER_TEMPLATE = "domainentitymapper.template";
	//private static final String DOMAIN = "domain";
	private static final String FILE_EXTENSION = ".java";
	
	@Getter
	private DomainTemplate domainTemplate;
	
	@Getter
	private EntityTemplate domainEntityTemplate;
	
	@Getter
	private String dateDeclared;
	
	@Getter
	private String setDeclared;
	
	@Getter
	private String listDeclared;
	
	@Getter
	private String classNameVariable;
	
	@Getter
	private String entityType;
	
	@Getter
	private List<ClassField> entityClassFields = new ArrayList<>();
	
	@Getter
	private List<ClassField> domainClassFields = new ArrayList<>();
	
	public String getDomainClassName() {
		return this.domainTemplate.getClassName();
	}
	
	public String getDomainEntityClassName() {
		return this.domainEntityTemplate.getClassName();
	}
	
	private void updateTemplateDetails( 
			final DomainTemplate domainTemplate,
			final EntityTemplate domainEntityTemplate,
			final GeneratorMetaData metaData) {
		
		this.templateName = DOMAINMAPPER_TEMPLATE;
		//this.codeGenDirPath = metaData.getServiceCodeGenDirPath(domainModel.getDomainName())+DOMAIN+"/";
		this.codeGenDirPath = domainTemplate.getCodeGenDirPath();
		this.fileExtension = FILE_EXTENSION;
		this.bindingName = "domainentitymapper";
	}
	
	private void updateClassDetails(
			final DomainTemplate domainTemplate,
			final EntityTemplate domainEntityTemplate,
			final GeneratorMetaData metaData) {
		
		this.domainTemplate = domainTemplate;
		this.domainClassFields = domainTemplate.getClassfields();
		this.domainEntityTemplate = domainEntityTemplate;
		this.entityClassFields = domainEntityTemplate.getClassfields();
		this.packageName = domainTemplate.getPackageName();
		this.className = domainTemplate.getClassName()+"Mapper";
		this.classNameVariable = this.getClassNameVariable(domainTemplate.getClassName()+"Mapper");
		this.entityType = domainTemplate.getEntityType();
		
		this.dateDeclared = this.getDateDeclared(domainTemplate.getClassfields());
		this.setDeclared = this.getSetDeclared(domainTemplate.getClassfields());
		this.listDeclared = this.getListDeclared(domainTemplate.getClassfields());
	}
	
	private String getDateDeclared(List<ClassField> fields) {
		String dateDeclared = null;
		for(ClassField classField:fields) {
			if(classField.getDatatype().equalsIgnoreCase("Date")) {
				dateDeclared = "Date";
				break;
			}
		}
		return dateDeclared;
	}
	private String getSetDeclared(List<ClassField> fields) {
		String setDeclared = null;
		for(ClassField classField:fields) {
			if(classField.getDatatypeClassName().contains("Set")) {
				setDeclared = "Set";
				break;
			}
		}
		return setDeclared;
	}
	private String getListDeclared(List<ClassField> fields) {
		String setDeclared = null;
		for(ClassField classField:fields) {
			if(classField.getDatatypeClassName().contains("List")) {
				setDeclared = "List";
				break;
			}
		}
		return setDeclared;
	}
	
	private String getClassNameVariable(String className) {
        String result = "";
        if(null != className && className.length() > 0) {
        	// Append first character(in lower case)
            char c = className.charAt(0);
            result = Character.toLowerCase(c)+ className.substring(1);
        }
        else {
        	result = "model";
        }
        //log.info("ClassNameVariable as::"+result);
        return result;
	}
	
	public static DomainEntityMapperTemplateBuilder builder() {
		return new DomainEntityMapperTemplateBuilder();
	}
	
	public static class DomainEntityMapperTemplateBuilder{
		
		public DomainEntityMapperTemplate buildFrom(
				final DomainTemplate domainTemplate,
				final EntityTemplate domainEntityTemplate,
				final GeneratorMetaData metaData) {
			
			DomainEntityMapperTemplate template = new DomainEntityMapperTemplate();
			template.updateClassDetails(domainTemplate, domainEntityTemplate, metaData);
			template.updateTemplateDetails(domainTemplate, domainEntityTemplate, metaData);
			return template;
		}
	}
}
