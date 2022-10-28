package com.cnatives.ms.generator.mscontroller;

import lombok.Data;

@Data
public class FileExport {

	private String fileName;
	private byte[] fileContent;
}
