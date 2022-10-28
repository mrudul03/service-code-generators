package com.cnatives.ms.generator.mscontroller;

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

import com.cnatives.ms.generator.msservice.CodeGeneratorService;
import com.cnatives.ms.generator.storage.GoogleStorageClientAdapter;
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
	
//	@PostMapping(value="/{customerId}/generate", consumes=APPLICATION_JSON_VALUE, 
//			produces = APPLICATION_JSON_VALUE)
//	public ResponseEntity<FilesResponse> generateFiles(
//			@PathVariable String customerId,
//			@RequestBody CodeGenerationRequest request){
//		
//		FilesResponse filesResponse = new FilesResponse();
//		log.info("Received request for code generation.....");
//		List<FileInfo> fileInfos = new ArrayList<>();
//		try {
//			codeGeneratorService.generateCode(request, customerId);
//			
//			log.info("Generated code and zipped:");
//			
//			fileInfos = codeGeneratorService.loadAll(customerId).map(path -> {
//				String filename = path.getFileName().toString();
//				String url = MvcUriComponentsBuilder
//						.fromMethodName(CodeGeneratorController.class, "getFile", 
//								customerId, path.getFileName().toString()).build().toString();
//
//				return new FileInfo(filename, url);
//			}).collect(Collectors.toList());
//			
//			filesResponse.setFiles(fileInfos);
//			filesResponse.setMessage("Microservice code generated successfully.");
//			return ResponseEntity.status(HttpStatus.OK).body(filesResponse);
//		}
//		catch (Exception e) {
//			filesResponse.setMessage("Fail to generate code!");
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.OK).body(filesResponse);
//		}
//		//return ResponseEntity.status(HttpStatus.OK).body(new CodeGenerationResponse("success !!!"));
//	}
	
	@PostMapping(value="/{customerId}/generate", consumes=APPLICATION_JSON_VALUE, 
			produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<FileInfo> generateFiles(
			@PathVariable String customerId,
			@RequestBody CodeGenerationRequest request){
		
		FilesResponse filesResponse = new FilesResponse();
		log.info("Received request for code generation.....");
		try {
			FileInfo fileinfo = codeGeneratorService.generateCode(request, customerId);
			return ResponseEntity.status(HttpStatus.OK).body(fileinfo);
		}
		catch (Exception e) {
			filesResponse.setMessage("Fail to generate code!");
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK).body(new FileInfo("", ""));
		}
	}
	
//	@GetMapping("/archives/{customerId}")
//	public ResponseEntity<List<FileInfo>> getListFiles(@PathVariable String customerId) {
//		List<FileInfo> fileInfos = codeGeneratorService.loadAll(customerId).map(path -> {
//			String filename = path.getFileName().toString();
//			String url = MvcUriComponentsBuilder
//					.fromMethodName(CodeGeneratorController.class, "getFile", 
//							customerId, path.getFileName().toString()).build().toString();
//
//			return new FileInfo(filename, url);
//		}).collect(Collectors.toList());
//
//		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
//	}
	
//	@GetMapping("/files/{customerId}/{filename}")
//	public ResponseEntity<Resource> getFile(@PathVariable String customerId, @PathVariable String filename) {
//		System.out.println("Get file with name:"+filename);
//		Resource file = codeGeneratorService.load(filename, customerId);
//		return ResponseEntity.ok()
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
//				.body(file);
//	}
	
	@GetMapping("/files/{customerId}/{filename}")
	public ResponseEntity<ByteArrayResource> getFile(
			@PathVariable String customerId, @PathVariable String filename) {
		
		try {
			System.out.println("Get file with name:"+filename);
			//FileExport fileExport = codeGeneratorService.load(filename, customerId);
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
