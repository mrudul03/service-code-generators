package com.cnatives.ms.contract;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class DomainModelOperation {
	
	private String method;
	private String name;
	private String description;
	private String path;
	private Body body;
	private Map<String, Response> responses;
	private List<OperationParam> pathparams;
	private List<OperationParam> requestparams;

}
