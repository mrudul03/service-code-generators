package com.cnatives.ms.generator.msclient.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cnatives.ms.generator.msclient.service.MsClientService;
import com.cnatives.ms.generator.mscontroller.CodeGenerationRequest;
import com.cnatives.ms.generator.mscontroller.FileInfo;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/ms/genrator")
@Slf4j
public class ServiceClientController {
	
	private MsClientService msClientService;
	
	public ServiceClientController( MsClientService msClientService) {
		this.msClientService = msClientService;
	}
	
	@PostMapping(value="/{customerId}/generateclient", consumes=APPLICATION_JSON_VALUE, 
			produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<FileInfo> generateFiles(
			@PathVariable String customerId,
			@RequestBody CodeGenerationRequest request){
		
		FileInfo fileInfo = new FileInfo();
		log.info("Received request for code generation.....");
		try {
			log.info("Received request for code generation.....");
			fileInfo = msClientService.generateCode(request, customerId);
			
			return ResponseEntity.status(HttpStatus.OK).body(fileInfo);
		}
		catch (Exception e) {
			fileInfo.setMessage("Fail to generate Service client code!");
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK).body(fileInfo);
		}
	}
	
//	@GetMapping("/archives/client/{customerId}")
//	public ResponseEntity<List<FileInfo>> getListFiles(@PathVariable String customerId) {
//		List<FileInfo> fileInfos = msClientService.loadAll(customerId).map(path -> {
//			String filename = path.getFileName().toString();
//			String url = MvcUriComponentsBuilder
//					.fromMethodName(ServiceClientController.class, "getClientArchiveFile", 
//							customerId, path.getFileName().toString()).build().toString();
//
//			return new FileInfo(filename, url);
//		}).collect(Collectors.toList());
//
//		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
//	}
	
//	@GetMapping("/files/client/{customerId}/{filename}")
//	public ResponseEntity<Resource> getClientArchiveFile(@PathVariable String customerId, @PathVariable String filename) {
//		System.out.println("Get file with name:"+filename);
//		Resource file = msClientService.load(filename, customerId);
//		return ResponseEntity.ok()
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
//				.body(file);
//	}

}
