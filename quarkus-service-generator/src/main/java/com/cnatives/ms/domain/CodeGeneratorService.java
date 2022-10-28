package com.cnatives.ms.domain;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.cnatives.ms.contract.CodeGenerationRequest;
import com.cnatives.ms.contract.Configurations;
import com.cnatives.ms.contract.DomainModel;
import com.cnatives.ms.contract.FileInfo;
import com.cnatives.ms.generator.base.GeneratorMetaData;
import com.cnatives.ms.generator.base.MainGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CodeGeneratorService {

	private MainGenerator mainGenerator;
	//private StorageService storageService;
	
	private final String CODEGENERATION_DIR = "codegeneration";
	private final String ARCHIVES_DIR = "archives";
	private final String PATH_SEPARATOR = "/";
	private final Path archiveRoot = Paths.get("");
	
	//private final Path archiveRoot = Paths.get(ARCHIVES_DIR);
	
	public CodeGeneratorService(MainGenerator mainGenerator) {
		this.mainGenerator = mainGenerator;
		//this.storageService = storageService;
	}
	
	public FileInfo generateCode(CodeGenerationRequest request, String customerId) {
		log.info("logging from service.....");
		
		Configurations configurations = request.getConfigurations();
		configurations.initialize();
		
		List<DomainModel> domainModels = request.getDomains();
		
		GeneratorMetaData metaData = new GeneratorMetaData();
		metaData.initialize(configurations, domainModels, customerId);
		
		mainGenerator.generateServiceCode(request, customerId);
		String sourceDirName = metaData.getServiceGenerationDir();
		String archiveDestName = metaData.getServiceArchiveFilename();
		
		FileArchiveService fileArchiveService = new FileArchiveService();
		fileArchiveService.archiveFiles(customerId, sourceDirName, archiveDestName, this.CODEGENERATION_DIR);
		String filename = configurations.getServiceName()+"-archive.zip";
		//this.uploadFile(archiveDestName, filename);
		FileInfo fileinfo = new FileInfo(customerId, filename);
		
		log.info("generated code.....");
		return fileinfo;
	}
	
//	private void uploadFile(final String zipDestinationName, String filename) {
//		Path path = archiveRoot.resolve(zipDestinationName);
//		log.info("Got file path");
//		File file = path.toFile();
//		log.info("Got file::");
//		//String filename = serviceName+"-archive.zip";
//		//storageService.uploadFile(file, "", filename);
//	}
	
	public Stream<Path> loadAll(String customerId) {
		try {
			Path archivePath = Paths.get(ARCHIVES_DIR+PATH_SEPARATOR+customerId);
			
			return Files.walk(archivePath, 10).
					filter(path -> !path.equals(archivePath)).map(archivePath::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}
	
	public Resource load(String filename, String customerId) {
		try {
			Path file = archiveRoot.resolve(customerId+PATH_SEPARATOR+filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				log.info("Returning file:" + filename);
				return resource;
			} else {
				log.info("Could not read the file");
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			log.error("MalformedURLException", e);
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
}
