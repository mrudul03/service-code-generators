package com.cnatives.ms.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StorageException extends RuntimeException {
	
	private static final long serialVersionUID = 4681038762984083256L;
	
	private String message;
	private String details;
	private String hint;
	private String nextActions;
	private String support;
	
	public StorageException() {}
	
	public StorageException(final String message) {
		this.message = message;
	}
	
	public StorageException(final String message,
			final String details,
			final String hint,
			final String nextAction,
			final String support) {
		
		this.message = message;
		this.details = details;
		this.hint = hint;
		this.nextActions = nextAction;
		this.support = support;
	}
}
