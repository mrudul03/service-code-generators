package com.cnatives.ms.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class FileArchiveException extends RuntimeException{

	private static final long serialVersionUID = -3151013225438315792L;
	private String message;
	private String details;
	private String hint;
	private String nextActions;
	private String support;
	
	public FileArchiveException() {}
	
	public FileArchiveException(final String message) {
		this.message = message;
	}
	
	public FileArchiveException(final String message,
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
