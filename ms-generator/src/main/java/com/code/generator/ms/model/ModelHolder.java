package com.code.generator.ms.model;

import com.code.generator.ms.controller.Contract;
import com.code.generator.ms.util.Constants;

import lombok.Data;
import lombok.Getter;;

@Data
public class ModelHolder {
	
	@Getter
	private Model model;
	
	@Getter
	private Contract contractRequest;
	
	@Getter
	private Contract contractResponse;
	
	
	public ModelHolder(final Model model, final Contract contractRequest, final Contract contractResponse) {
		this.model = model;
		this.contractRequest = contractRequest;
		this.contractResponse = contractResponse;
	}
	
	public String getModelName() {
		return this.model.getClassName();
	}
	
	public String getContractRequestName() {
		return this.contractRequest.getClassName();
	}
	
	public String getContractResponseName() {
		return this.contractResponse.getClassName();
	}
	
	public boolean isPrimitiveDataType(final String datatype) {
		boolean isPrimititve = Constants.primitiveDatatypes.contains(datatype.toLowerCase());
		return isPrimititve;
	}
}
