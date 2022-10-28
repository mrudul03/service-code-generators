package com.code.generator.ms.input;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class ValidationResult {
	
	@Getter
	private boolean isValid;
	
	@Getter
	private List<String> errors = new ArrayList<>();
	
	public ValidationResult() {}
	
	public ValidationResult(final boolean isValid, final List<String> errors) {
		this.isValid = isValid;
		this.errors = errors;
	}
	
	public void add(final String error) {
		this.errors.add(error);
	}
	
	public void addAll(final List<String> errors) {
		this.errors.addAll(errors);
	}
	
	public String getErrorsAsString() {
		return String.join("\n", this.errors);
	}
	
}
