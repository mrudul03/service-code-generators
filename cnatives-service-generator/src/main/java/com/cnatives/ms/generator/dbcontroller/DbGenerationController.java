package com.cnatives.ms.generator.dbcontroller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

import com.cnatives.ms.generator.dbservice.DatabaseScriptService;
import com.cnatives.ms.generator.mscontroller.FileInfo;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/ms/genrator")
@Slf4j
public class DbGenerationController {
	
	private DatabaseScriptService databaseScriptService;
	
	public DbGenerationController(DatabaseScriptService databaseScriptService) {
		this.databaseScriptService = databaseScriptService;
	}
	
	@PostMapping(value="/{customerId}/dbgenerate", consumes=APPLICATION_JSON_VALUE, 
			produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<FileInfo> generateFiles(
			@PathVariable String customerId,
			@RequestBody DbGenerationRequest request){
		
		FileInfo fileInfo = new FileInfo();
		log.info("Received request for code generation.....");
		try {
			log.info("Received request for code generation.....");
			fileInfo = databaseScriptService.generateDbScript(request, customerId);
			return ResponseEntity.status(HttpStatus.OK).body(fileInfo);
		}
		catch (Exception e) {
			fileInfo.setMessage("Fail to generate code!");
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK).body(fileInfo);
		}
	}
	
	@GetMapping("/archives/dbscript/{customerId}")
	public ResponseEntity<List<FileInfo>> getListFiles(@PathVariable String customerId) {
		List<FileInfo> fileInfos = databaseScriptService.loadAll(customerId).map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(DbGenerationController.class, "getArchiveFile", 
							customerId, path.getFileName().toString()).build().toString();

			return new FileInfo(filename, url);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}
	
	@GetMapping("/files/dbscript/{customerId}/{filename}")
	public ResponseEntity<Resource> getArchiveFile(@PathVariable String customerId, @PathVariable String filename) {
		System.out.println("Get file with name:"+filename);
		Resource file = databaseScriptService.load(filename, customerId);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
}
