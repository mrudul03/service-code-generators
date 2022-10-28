package com.cnatives.ms.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cnatives.ms.contract.CodeGenerationRequest;
import com.cnatives.ms.contract.FileInfo;
import com.cnatives.ms.domain.CodeGeneratorService;
import com.cnatives.ms.file.storage.GoogleStorageClientAdapter;
import com.google.api.services.storage.model.StorageObject;
import com.google.common.io.Files;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/ms/genrator")
@Slf4j
public class CodeGeneratorController {

	private CodeGeneratorService codeGeneratorService;
	private GoogleStorageClientAdapter googleStorageClientAdapter;
	
	public CodeGeneratorController(CodeGeneratorService codeGeneratorService,
			GoogleStorageClientAdapter googleStorageClientAdapter) {
		
		this.codeGeneratorService = codeGeneratorService;
		this.googleStorageClientAdapter = googleStorageClientAdapter;
	}
	
	@PostMapping(value="/{customerId}/generate", consumes=APPLICATION_JSON_VALUE, 
			produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<FileInfo> generateFiles(
			@PathVariable String customerId,
			@RequestBody CodeGenerationRequest request){
		
		log.info("Received request for code generation.....");
		FileInfo fileInfo = new FileInfo();
		try {
			fileInfo = codeGeneratorService.generateCode(request, customerId);
			log.info("Generated code and zipped:");
			fileInfo.setMessage("Generated code and zipped");
			
			return ResponseEntity.status(HttpStatus.OK).body(fileInfo);
		}
		catch (Exception e) {
			fileInfo.setMessage("Failed to generate code!");
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK).body(fileInfo);
		}
	}
	
	@GetMapping("/files/{customerId}/{filename}")
	public ResponseEntity<ByteArrayResource> getFile(
			@PathVariable String customerId, @PathVariable String filename) {
		
		try {
			System.out.println("Get file with name:"+filename);
			StorageObject object = googleStorageClientAdapter.download(filename);
			byte[] res = Files.toByteArray((File) object.get("file"));
	        ByteArrayResource resource = new ByteArrayResource(res);
	        return ResponseEntity
	                .ok()
	                .contentLength(res.length)
	                .header("Content-type", "application/octet-stream")
	                .header("Content-disposition", "attachment; filename="+filename)
	                .body(resource);
		}catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No such file or directory");
        }
	}	
}
