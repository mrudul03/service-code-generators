package com.code.generator.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.code.generator.model.FileInfo;
import com.code.generator.model.FilesResponse;
import com.code.generator.model.MsGeneratorService;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/ms/genrator")
@Slf4j
public class MsGeneratorController {

	private MsGeneratorService msGeneratorService;

	public MsGeneratorController(final MsGeneratorService msGeneratorService) {
		this.msGeneratorService = msGeneratorService;
	}
	
	@PostMapping(value="/{customerId}/generate", consumes=APPLICATION_JSON_VALUE, 
			produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<FilesResponse> generateFiles(
			@PathVariable String customerId,
			@RequestBody GenerateServiceContract contract){
		
		FilesResponse filesResponse = new FilesResponse();
		log.info("Received contract:"+contract.getConfigurations().toString());
		log.info("Received contract:"+contract.getModels().size());
		
		List<FileInfo> fileInfos = new ArrayList<>();
		try {
			msGeneratorService.generateMsCode(contract.getConfigurations(), 
					contract.getModels(),
					customerId);
			log.info("Generated code and zipped:");
			
			fileInfos = msGeneratorService.loadAll(customerId).map(path -> {
				String filename = path.getFileName().toString();
				String url = MvcUriComponentsBuilder
						.fromMethodName(MsGeneratorController.class, "getFile", 
								customerId, path.getFileName().toString()).build().toString();

				return new FileInfo(filename, url);
			}).collect(Collectors.toList());
			
			filesResponse.setFiles(fileInfos);
			filesResponse.setMessage("Microservice code generated successfully.");
			return ResponseEntity.status(HttpStatus.OK).body(filesResponse);
		} catch (Exception e) {
			filesResponse.setMessage("Fail to generate code!");
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK).body(filesResponse);
		}
	}

	@PostMapping("/{customerId}/upload")
	public ResponseEntity<FilesResponse> uploadFiles(
			@RequestParam("files") MultipartFile[] files,
			@PathVariable String customerId) {
		
		FilesResponse filesResponse = new FilesResponse();
		List<FileInfo> fileInfos = new ArrayList<>();
		try {
			msGeneratorService.generateMsCode(files, customerId);
			log.info("Generated code and zipped:");
			
			fileInfos = msGeneratorService.loadAll(customerId).map(path -> {
				String filename = path.getFileName().toString();
				String url = MvcUriComponentsBuilder
						.fromMethodName(MsGeneratorController.class, "getFile", 
								customerId, path.getFileName().toString()).build().toString();

				return new FileInfo(filename, url);
			}).collect(Collectors.toList());
			
			filesResponse.setFiles(fileInfos);
			filesResponse.setMessage("Microservice code generated successfully.");
			return ResponseEntity.status(HttpStatus.OK).body(filesResponse);
		} catch (Exception e) {
			filesResponse.setMessage("Fail to generate code!");
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK).body(filesResponse);
		}
	}
	
	@GetMapping("/archives/{customerId}")
	public ResponseEntity<List<FileInfo>> getListFiles(@PathVariable String customerId) {
		List<FileInfo> fileInfos = msGeneratorService.loadAll(customerId).map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(MsGeneratorController.class, "getFile", 
							customerId, path.getFileName().toString()).build().toString();

			return new FileInfo(filename, url);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}
	
	@GetMapping("/files/{customerId}/{filename}")
	public ResponseEntity<Resource> getFile(@PathVariable String customerId, @PathVariable String filename) {
		System.out.println("Get file with name:"+filename);
		Resource file = msGeneratorService.load(filename, customerId);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
//	@GetMapping("/archives/{filename}")
//	public void getFile(@PathVariable String filename, HttpServletResponse response) {
//		log.info("Get file with name:"+filename);
//		Path archiveRoot = Paths.get("archives");
//		Path archivedFilePath = archiveRoot.resolve(filename);
//		
//		FileSystemResource resource = new FileSystemResource(archivedFilePath); 
//	    response.setContentType("application/zip");
//	    response.setHeader("Content-Disposition", "attachment; filename="+filename);
//	    
//	    try (ZipOutputStream zippedOut = new ZipOutputStream(response.getOutputStream())) {
//	        ZipEntry e = new ZipEntry(filename);
//	        // Configure the zip entry, the properties of the file
//	        e.setSize(resource.contentLength());
//	        e.setTime(System.currentTimeMillis());
//	        // etc.
//	        zippedOut.putNextEntry(e);
//	        // And the content of the resource:
//	        StreamUtils.copy(resource.getInputStream(), zippedOut);
//	        zippedOut.closeEntry();
//	        zippedOut.finish();
//	    } catch (Exception e) {
//	        // Do something with Exception
//	    	log.error("Error reading file:"+e);
//	    } 
//	}
}
