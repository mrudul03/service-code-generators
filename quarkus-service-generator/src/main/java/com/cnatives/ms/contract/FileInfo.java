package com.cnatives.ms.contract;

import lombok.Data;

@Data
public class FileInfo {
	private String name;
	private String fileName;
	private String message;
	
	public FileInfo() {}
	public FileInfo(String name, String fileName) {
		this.name = name;
		this.fileName = fileName;
	}
}
