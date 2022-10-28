package com.cnatives.ms.generator.msservice.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cnatives.ms.generator.mscontroller.DomainModelOperation;
import com.cnatives.ms.generator.mscontroller.OperationParam;
import com.cnatives.ms.generator.mscontroller.Response;
import com.cnatives.ms.generator.msservice.base.CommonUtil;

import lombok.Getter;

//@Slf4j
public class ControllerOperation {
	
	private final static String METHOD_GET = "GET";
	private final static String METHOD_POST = "POST";
	private final static String METHOD_PUT = "PUT";
	private final static String METHOD_DELETE = "DELETE";
	
	private final static String MAPPING_GET = "GetMapping";
	private final static String MAPPING_POST = "PostMapping";
	private final static String MAPPING_PUT = "PutMapping";
	private final static String MAPPING_DELETE = "DeleteMapping";
	
	@Getter
	private String method;
	
	@Getter
	private String operationName;
	
	@Getter
	private String description;
	
	@Getter
	private String mapping;
	
	@Getter
	private String path;
	
	@Getter
	private String response;
	
	@Getter
	private String name;
	
	@Getter
	private String returnresponse;
	
	@Getter
	private List<OperationParameter> parameters  = new ArrayList<>();
	
	@Getter
	private String parametersAsString;
	
	private void updateOperationDetails(DomainModelOperation dOperation) {
		this.method = dOperation.getMethod();
		this.operationName = dOperation.getName();
		this.description = dOperation.getDescription();
		this.mapping = mapMapping(dOperation.getMethod());
		this.path = dOperation.getPath();
		this.response = this.mapResponse(dOperation.getMethod(), dOperation.getResponses());
		this.returnresponse = this.mapReturnResponse(dOperation.getMethod(), dOperation.getResponses());
		this.parameters = this.getParameters(dOperation);
		this.parametersAsString = this.createParameterString(this.parameters);
	}
	
	private String mapMapping(String method) {
		if(method.equalsIgnoreCase(METHOD_GET)) {
			return MAPPING_GET;
		}
		else if(method.equalsIgnoreCase(METHOD_PUT)) {
			return MAPPING_PUT;
		}
		else if(method.equalsIgnoreCase(METHOD_POST)) {
			return MAPPING_POST;
		}
		else if(method.equalsIgnoreCase(METHOD_DELETE)) {
			return MAPPING_DELETE;
		}
		else {
			return null;
		}
	}
	
	private String mapResponse(String method, Map<String, Response> responses) {
		Response response = null;
		if(null != responses.get("201")) {
			response = responses.get("201");
		}
		else {
			response = responses.get("200");
		}
		if(null != response.getType() && !response.getType().isBlank()) {
			return response.getType();
		}
		else {
			return "Void";
		}
	}
	
	private String mapReturnResponse(String method, Map<String, Response> responses) {
		Response response = null;
		if(null != responses.get("201")) {
			response = responses.get("201");
		}
		else {
			response = responses.get("200");
		}
		if(null != response.getType() && !response.getType().isBlank()) {
			return "new "+response.getType()+" ()";
		}
		else {
			return "null";
		}
	}
	
	private List<OperationParameter> getParameters(DomainModelOperation dOperation){
		List<OperationParameter> parameters  = new ArrayList<>();
		if(null != dOperation.getBody()) {
			OperationParameter parameter = new OperationParameter(
					"@RequestBody", dOperation.getBody().getType(), "request");
			parameters.add(parameter);
		}
		if(null != dOperation.getPathparams()) {
			for(OperationParam pathParam: dOperation.getPathparams()) {
				OperationParameter parameter = new OperationParameter(
					//"@PathVariable", StringUtils.capitalize(pathParam.getType()), pathParam.getName());
					"@PathVariable", CommonUtil.mapDatatype(pathParam.getType()), pathParam.getName());
				parameters.add(parameter);
			}
		}
		if(null != dOperation.getRequestparams()) {
			for(OperationParam requestParam: dOperation.getRequestparams()) {
				
				OperationParameter parameter = new OperationParameter(
						"@RequestParam(name=\""+requestParam.getName()+"\", required="+requestParam.isRequired()+")", 
						//StringUtils.capitalize(requestParam.getType()),
						CommonUtil.mapDatatype(requestParam.getType()),
						requestParam.getName());
				parameters.add(parameter);
			}
		}
		return parameters;
	}
	
	private String createParameterString(List<OperationParameter> parameters) {
		StringBuilder parameterBuilder = new StringBuilder();
		
		for(OperationParameter opParam: parameters) {
			parameterBuilder.append(opParam.getParamtype()+" "+opParam.getParamname()+" "+ opParam.getParamvariable()+",");
		}
		String parameterString = "";
		if(parameterBuilder.length() > 0) {
			parameterString = parameterBuilder.substring(0, parameterBuilder.length()-1);
		}
		
		return parameterString;
	}
	
	public static ControllerOperationBuilder builder() {
		return new ControllerOperationBuilder();
	}
	
	public static class ControllerOperationBuilder {
		
		public ControllerOperation buildFrom(DomainModelOperation dOperation) {
			ControllerOperation cOperation = new ControllerOperation();
			cOperation.updateOperationDetails(dOperation);
			return cOperation;
		}
	}
	
	
}
