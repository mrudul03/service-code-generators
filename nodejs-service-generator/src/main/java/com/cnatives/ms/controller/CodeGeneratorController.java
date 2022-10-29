package com.cnatives.ms.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.cnatives.ms.contract.CodeGenerationRequest;
import com.cnatives.ms.contract.FileInfo;
import com.cnatives.ms.contract.FilesResponse;
import com.cnatives.ms.domain.CodeGeneratorService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/ms/genrator")
@Slf4j
public class CodeGeneratorController {

private CodeGeneratorService codeGeneratorService;
	
	public CodeGeneratorController(CodeGeneratorService codeGeneratorService) {
		this.codeGeneratorService = codeGeneratorService;
	}
	
	@PostMapping(value="/{customerId}/generate", consumes=APPLICATION_JSON_VALUE, 
			produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<FilesResponse> generateFiles(
			@PathVariable String customerId,
			@RequestBody CodeGenerationRequest request){
		
		FilesResponse filesResponse = new FilesResponse();
		log.info("Received request for code generation.....");
		List<FileInfo> fileInfos = new ArrayList<>();
		try {
			codeGeneratorService.generateCode(request, customerId);
			
			log.info("Generated code and zipped:");
			
			fileInfos = codeGeneratorService.loadAll(customerId).map(path -> {
				String filename = path.getFileName().toString();
				String url = MvcUriComponentsBuilder
						.fromMethodName(CodeGeneratorController.class, "getFile", 
								customerId, path.getFileName().toString()).build().toString();

				return new FileInfo(filename, url);
			}).collect(Collectors.toList());
			
			filesResponse.setFiles(fileInfos);
			filesResponse.setMessage("Microservice code generated successfully.");
			return ResponseEntity.status(HttpStatus.OK).body(filesResponse);
		}
		catch (Exception e) {
			filesResponse.setMessage("Fail to generate code!");
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK).body(filesResponse);
		}
		//return ResponseEntity.status(HttpStatus.OK).body(new CodeGenerationResponse("success !!!"));
	}
	
	@GetMapping("/archives/{customerId}")
	public ResponseEntity<List<FileInfo>> getListFiles(@PathVariable String customerId) {
		List<FileInfo> fileInfos = codeGeneratorService.loadAll(customerId).map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(CodeGeneratorController.class, "getFile", 
							customerId, path.getFileName().toString()).build().toString();

			return new FileInfo(filename, url);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}
	
	@GetMapping("/files/{customerId}/{filename}")
	public ResponseEntity<Resource> getFile(@PathVariable String customerId, @PathVariable String filename) {
		System.out.println("Get file with name:"+filename);
		Resource file = codeGeneratorService.load(filename, customerId);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
}
