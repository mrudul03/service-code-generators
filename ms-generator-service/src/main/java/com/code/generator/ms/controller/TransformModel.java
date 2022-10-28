package com.code.generator.ms.controller;

import org.apache.commons.lang3.StringUtils;

import com.code.generator.ms.model.ClassField;
import com.code.generator.ms.model.Model;
import com.code.generator.ms.util.Constants;

import lombok.Data;
import lombok.Getter;;

@Data
public class TransformModel {
	
	@Getter
	private Model model;
	
	@Getter
	private Contract contract;
	
	
	public TransformModel(final Model model, final Contract contract) {
		this.model = model;
		this.contract = contract;
	}
	
	public String getModelName() {
		return this.model.getClassName();
	}
	
	public String getContractName() {
		return this.contract.getClassName();
	}
	
	public String getModelToContract() {
		//System.out.println("Model name.........:"+this.model.getClassName());
		StringBuilder sbFieldList = new StringBuilder();
		sbFieldList.append(this.contract.getClassName()+" contract = new "+this.contract.getClassName()+"();\n");
		ClassField<?> idField = this.model.getIdField();
		if(null != idField) {
			sbFieldList.append("					contract."+this.setMethodString(
					idField.getName())+"(model."+this.getMethodString(idField.getName())+"());\n");
		}
		
		for(ClassField<?> modelField : this.model.getClassfields()) {
			if(this.isPrimitiveDataType(modelField.getDatatype())) {
				sbFieldList.append("					contract."+this.setMethodString(
						modelField.getName())+"(model."+this.getMethodString(modelField.getName())+"());\n");
			}
			else if(this.isCollectionDataType(modelField)) {
				String collectionTransformation = this.getContractCollectionTransfrom(modelField);
				sbFieldList.append(collectionTransformation);
			}
			else {
				String contractTransformation = this.getContractTransfrom(modelField);
				sbFieldList.append(contractTransformation);
			}
		}
		sbFieldList.append("					return contract;");
		return sbFieldList.toString();
	}
	
	public String getContractToModel() {
		//System.out.println("Contract name.........:"+this.contract.getClassName());
		StringBuilder sbFieldList = new StringBuilder();
		sbFieldList.append(this.model.getClassName()+" model = new "+this.model.getClassName()+"();\n");
		ClassField<?> idField = this.contract.getIdField();
		if(null != idField) {
			sbFieldList.append("					model."+this.setMethodString(
					idField.getName())+"(contract."+this.getMethodString(idField.getName())+"());\n");
		}
		
		for(ClassField<?> field : this.contract.getClassfields()) {
			
			if(this.isPrimitiveDataType(field.getDatatype())) {
				sbFieldList.append("					model."+this.setMethodString(
						field.getName())+"(contract."+this.getMethodString(field.getName())+"());\n");
			}
			else if(this.isCollectionDataType(field)) {
				String collectionTransformation = this.getModelCollectionTransfrom(field);
				sbFieldList.append(collectionTransformation);
			}
			else {
				String modelTransformation = this.getModelTransfrom(field);
				sbFieldList.append(modelTransformation);
			}
		}
		sbFieldList.append("					return model;");
		return sbFieldList.toString();
	}
	
	private String setMethodString(final String name) {
		return "set"+StringUtils.capitalize(name);
	}
	
	private String getMethodString(final String name) {
		return "get"+StringUtils.capitalize(name);
	}
	
	public boolean isPrimitiveDataType(final String datatype) {
		boolean isPrimititve = Constants.primitiveDatatypes.contains(datatype.toLowerCase());
		return isPrimititve;
	}
	
	private boolean isCollectionDataType(final ClassField<?> field) {
		String datatype = field.getDatatypeClass().getSimpleName();
		boolean isCollection = Constants.collectionTypes.contains(datatype);
		return isCollection;
	}
	
	private String getContractTransfrom(final ClassField<?> modelField) {
		StringBuilder contractTransform = new StringBuilder();
		//Model name:emails
		//Model datatype:Email
		String datatype = modelField.getDatatype();
		//String name = modelField.getName();
		String contractFunctionName = this.getContractFunctionName(datatype);
		
		//model.setAddress(transformAddressContractToModel(contract.getAddress()));
		contractTransform.append("					contract."+this.setMethodString(datatype)+"(");
		contractTransform.append(contractFunctionName+"(");
		contractTransform.append("model."+this.getMethodString(datatype)+"()));\n");
		
		return contractTransform.toString();
	}
	
	private String getModelTransfrom(final ClassField<?> contractField) {
		StringBuilder modelTransform = new StringBuilder();
		//Model name:emails
		//Model datatype:Email
		String datatype = contractField.getDatatype();
		String modelFunctionName = this.getModelFunctionName(datatype);
		
		//contract.setAddress(transformAddressToContract(model.getAddress()));
		modelTransform.append("					model."+this.setMethodString(datatype)+"(");
		modelTransform.append(modelFunctionName+"(");
		modelTransform.append("contract."+this.getMethodString(datatype)+"()));\n");
		
		return modelTransform.toString();
	}
	
	private String getContractCollectionTransfrom(
			final ClassField<?> modelField) {
		
		/**for(Email entity:model.getEmails()) {
			EmailContract email = transformEmailToContract(entity);
			contract.addItem(email);
		}*/
		String datatype = modelField.getDatatype();
		String name = modelField.getName();
		String contractTransformFunctionName = this.getContractFunctionName(datatype);
		String modelFunctionName = "model."+this.getMethodString(name)+"()";
		String contractName = StringUtils.capitalize(datatype)+"Contract";
		String contractVariableName = name.toLowerCase()+"Contract";
		
		StringBuilder collectionTransform = new StringBuilder();
		
		collectionTransform.append("					"+"for("+StringUtils.capitalize(datatype)+ " entity:"+modelFunctionName+") {"+"\n");
		collectionTransform.append("					   "+contractName+ " "+contractVariableName+ "= "+contractTransformFunctionName+"(entity);"+"\n");
		collectionTransform.append("					   "+"contract.addItem("+contractVariableName+");"+"\n");
		collectionTransform.append("					"+"}"+"\n");
		
		
		return collectionTransform.toString();
	}
	
	private String getModelCollectionTransfrom(
			final ClassField<?> contractField) {
		
		/**
		for(EmailContract emailContract:contract.getEmails()) {
			Email email = transformEmailContractToModel(emailContract);
			model.addItem(email);
		}
		*/
		
		StringBuilder collectionTransform = new StringBuilder();
		
		String datatype = contractField.getDatatype();
		String name = contractField.getName();
		String contractName = StringUtils.capitalize(datatype)+"Contract";
		String contractVariableName = name.toLowerCase()+"Contract";
		String contractFunctionName = "contract."+this.getMethodString(name)+"()";
		String modelTransformFunctionName = this.getModelFunctionName(datatype);
		
		collectionTransform.append("					"+"for("+contractName+ " "+contractVariableName+":"+contractFunctionName+") {"+"\n");
		collectionTransform.append("						"+StringUtils.capitalize(datatype)+ " "+name.toLowerCase()+ " = "+modelTransformFunctionName+"("+contractVariableName+");"+"\n");
		collectionTransform.append("						"+"model.addItem("+name.toLowerCase()+");"+"\n");
		collectionTransform.append("					"+"}"+"\n");
		
		return collectionTransform.toString();
	}
	
	private String getContractFunctionName(final String name) {
		StringBuilder functionName = new StringBuilder();
		functionName.append("transform"+StringUtils.capitalize(name)+"ToContract");
		return functionName.toString();
	}
	
	private String getModelFunctionName(final String name) {
		StringBuilder functionName = new StringBuilder();
		functionName.append("transform"+StringUtils.capitalize(name)+"ContractToModel");
		return functionName.toString();
	}

}
