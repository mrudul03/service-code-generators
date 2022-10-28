package com.cnatives.ms.generator.domain;

import java.util.ArrayList;
import java.util.List;

import com.cnatives.ms.contract.DomainModel;
import com.cnatives.ms.contract.DomainModelOperation;
import com.cnatives.ms.contract.OperationParam;
import com.cnatives.ms.generator.base.CommonUtil;

import lombok.Getter;

public class DomainOperation {
	
	private final static String METHOD_GET = "GET";
	private final static String METHOD_POST = "POST";
	private final static String METHOD_PUT = "PUT";
	private final static String METHOD_DELETE = "DELETE";
	
	@Getter
	private String method;
	
	@Getter
	private String operationName;
	
	@Getter
	private String name;
	
	@Getter
	private String get;
	
	@Getter
	private String getwithid;
	
	@Getter
	private String post;
	
	@Getter
	private String put;
	
	@Getter
	private String putwithbody;
	
	@Getter
	private String delete;
	
	@Getter
	private String response;
	
	@Getter
	private String responseVariable;
	
	@Getter
	private String idParam;
	
	@Getter
	private String idParamVariable;
	
	@Getter
	private String saveRequest;
	
	@Getter
	private String saveRequestVariable;
	
	@Getter
	private List<DomainOperationParam> parameters  = new ArrayList<>();
	
	@Getter
	private String parametersAsString;
	
	private DomainOperation() {}
	
	private void updateDomainOperation(DomainModel domainModel, 
			DomainModelOperation modelOperation) {
		this.method = modelOperation.getMethod();
		this.operationName = modelOperation.getName();
		this.parameters = getParameters(domainModel.getName(), modelOperation);
		this.parametersAsString = this.createParameterString(this.parameters);
		
		this.setOperationType(modelOperation);
		this.setReponseType(domainModel);
		this.setIdType(domainModel);
		this.setSaveParamType(modelOperation);
	}
	
	private String createParameterString(List<DomainOperationParam> parameters) {
		StringBuilder parameterBuilder = new StringBuilder();
		
		for(DomainOperationParam opParam: parameters) {
			parameterBuilder.append(opParam.getParamtype()+" "+ opParam.getParamvariable()+",");
		}
		String parameterString = "";
		if(parameterBuilder.length() > 0) {
			parameterString = parameterBuilder.substring(0, parameterBuilder.length()-1);
		}
		
		return parameterString;
	}
	
	private void setOperationType(DomainModelOperation modelOperation) {
		if(modelOperation.getMethod().equalsIgnoreCase(DomainOperation.METHOD_POST)) {
			this.post = DomainOperation.METHOD_POST;
		}
		else if(modelOperation.getMethod().equalsIgnoreCase(DomainOperation.METHOD_PUT)) {
			this.put = DomainOperation.METHOD_PUT;
			if(null != modelOperation.getBody()) {
				this.putwithbody = DomainOperation.METHOD_PUT;
			}
		}
		else if(modelOperation.getMethod().equalsIgnoreCase(DomainOperation.METHOD_GET)) {
			this.get = DomainOperation.METHOD_GET;
			if(null != modelOperation.getPathparams()) {
				this.getwithid = DomainOperation.METHOD_GET;
			}
		}
		else if(modelOperation.getMethod().equalsIgnoreCase(DomainOperation.METHOD_DELETE)) {
			this.delete = DomainOperation.METHOD_DELETE;
		}
	}
	
	private void setReponseType(DomainModel domainModel) {
		this.response = domainModel.getName();
		this.responseVariable = this.getVariableName(domainModel.getName());
	}
	
	private void setIdType(DomainModel domainModel) {
		this.idParam = "Long";
		this.idParamVariable = this.getIdVariableName(domainModel.getName());
	}
	
	private void setSaveParamType(DomainModelOperation modelOperation) {
		if(null != modelOperation.getBody()) {
			this.saveRequest = modelOperation.getBody().getType();
			this.saveRequestVariable = this.getVariableName(modelOperation.getBody().getType());
		}
	}
	
	private List<DomainOperationParam> getParameters(
			String domainModelName, DomainModelOperation modelOperation){
		
		List<DomainOperationParam> parameters  = new ArrayList<>();
		if(null != modelOperation.getBody()) {
			DomainOperationParam parameter = new DomainOperationParam(
					modelOperation.getBody().getType(), 
					this.getVariableName(modelOperation.getBody().getType()));
			parameters.add(parameter);
		}
		if(null != modelOperation.getPathparams()) {
			for(OperationParam pathParam: modelOperation.getPathparams()) {
				
				DomainOperationParam parameter = new DomainOperationParam(
						CommonUtil.mapDatatype(pathParam.getType()), pathParam.getName());
				parameters.add(parameter);
			}
		}
		if(null != modelOperation.getRequestparams()) {
			for(OperationParam pathParam: modelOperation.getRequestparams()) {
				
				DomainOperationParam parameter = new DomainOperationParam(
						//StringUtils.capitalize(pathParam.getType()), 
						CommonUtil.mapDatatype(pathParam.getType()),
						pathParam.getName());
				parameters.add(parameter);
			}
		}
		return parameters;
	}
	
	private String getIdVariableName(String domainModelName) {
		String idName = this.getVariableName(domainModelName)+"Id";
		return idName;
	}
	
	private String getVariableName(String name) {
        String result = "";
        if(null != name && name.length() > 0) {
            char c = name.charAt(0);
            result = Character.toLowerCase(c)+ name.substring(1);
        }
        else {
        	result = name;
        }
        return result;
	}
	
	public static DomainOperationBuilder builder() {
		return new DomainOperationBuilder();
	}
	
	public static class DomainOperationBuilder{
		
//		private String method;
//		private String operationName;
//		
//		private String get;
//		private String getwithid;
//		private String post;
//		private String put;
//		private String putwithbody;
//		private String delete;
//		
//		private String response;
//		private String responseVariable;
//		private String idParam;
//		private String idParamVariable;
//		private String saveRequest;
//		private String saveRequestVariable;
//		
//		private List<DomainOperationParam> parameters  = new ArrayList<>();
//		private String parametersAsString;
		
		public DomainOperation buildFrom(DomainModel domainModel, 
				DomainModelOperation modelOperation) {
			
			DomainOperation operation = new DomainOperation();
			operation.updateDomainOperation(domainModel, modelOperation);
			return operation;
			
//			this.method = modelOperation.getMethod();
//			this.operationName = modelOperation.getName();
//			this.parameters = getParameters(domainModel.getName(), modelOperation);
//			this.parametersAsString = this.createParameterString(this.parameters);
//			
//			this.setOperationType(modelOperation);
//			this.setReponseType(domainModel);
//			this.setIdType(domainModel);
//			this.setSaveParamType(modelOperation);
//			
//			return this;
		}
		
//		public DomainOperation build() {
//			DomainOperation operation = new DomainOperation();
//			
//			operation.method = (this.method);
//			operation.operationName = (this.operationName);
//			operation.parameters = (this.parameters);
//			operation.parametersAsString = this.parametersAsString;
//			
//			operation.post = (this.post);
//			operation.get = (this.get);
//			operation.getwithid = (this.getwithid);
//			operation.put =(this.put);
//			operation.putwithbody = (this.putwithbody);
//			operation.delete = (this.delete);
//			
//			operation.idParam =  (this.idParam);
//			operation.idParamVariable = (this.idParamVariable);
//			operation.response = (this.response);
//			operation.responseVariable = (this.responseVariable);
//			operation.saveRequest = this.saveRequest;
//			operation.saveRequestVariable = this.saveRequestVariable;
//			
//			return operation;
//		}
		
		
	}
	
}
