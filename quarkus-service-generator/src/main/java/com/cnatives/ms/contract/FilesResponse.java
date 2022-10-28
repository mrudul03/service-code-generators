package com.cnatives.ms.contract;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FilesResponse {
	
	private List<FileInfo> files = new ArrayList<>();
	private String message;

}
