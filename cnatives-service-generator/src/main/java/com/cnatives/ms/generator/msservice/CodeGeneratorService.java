package com.cnatives.ms.generator.msservice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.cnatives.ms.generator.archive.service.FileArchiveService;
import com.cnatives.ms.generator.mscontroller.CodeGenerationRequest;
import com.cnatives.ms.generator.mscontroller.Configurations;
import com.cnatives.ms.generator.mscontroller.DomainModel;
import com.cnatives.ms.generator.mscontroller.FileInfo;
import com.cnatives.ms.generator.msservice.base.GeneratorMetaData;
import com.cnatives.ms.generator.msservice.base.MainGenerator;
import com.cnatives.ms.generator.storage.StorageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CodeGeneratorService {
	
	private MainGenerator mainGenerator;
	private final String CODEGENERATION_DIR = "codegeneration";
	private final String ARCHIVES_DIR = "archives";
	private final String PATH_SEPARATOR = "/";
	
	//private final Path archiveRoot = Paths.get("");
	//private StorageService storageService;
	
	public CodeGeneratorService(MainGenerator mainGenerator, 
			StorageService storageService) {
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
		log.info("archiveDestName::"+archiveDestName);
		
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
//		storageService.uploadFile(file, "", filename);
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
	
//	public Resource load(String filename, String customerId) {
//		try {
//			Path path = archiveRoot.resolve(customerId+PATH_SEPARATOR+filename);
//			File file = path.toFile();
//			
//			Resource resource = new UrlResource(path.toUri());
//
//			if (resource.exists() || resource.isReadable()) {
//				log.info("Returning file:" + filename);
//				return resource;
//			} else {
//				log.info("Could not read the file");
//				throw new RuntimeException("Could not read the file!");
//			}
//		} catch (MalformedURLException e) {
//			log.error("MalformedURLException", e);
//			throw new RuntimeException("Error: " + e.getMessage());
//		}
//	}
	
//	public FileExport load(String filename, String customerId) {
//		FileInputStream inputStream = null;
//		try {
//			Path path = archiveRoot.resolve(customerId+PATH_SEPARATOR+filename);
//			File file = path.toFile();
//			inputStream = new FileInputStream(file);
//			
//			FileExport fileExport = new FileExport();
//			fileExport.setFileContent(IOUtils.toByteArray(inputStream));
//            fileExport.setFileName(filename);
//            return fileExport;
//			
//		} catch (FileNotFoundException e) {
//			log.error("FileNotFoundException", e);
//			throw new RuntimeException("Error: " + e.getMessage());
//		} catch (IOException e) {
//			log.error("IOException", e);
//			throw new RuntimeException("Error: " + e.getMessage());
//		}
//	}

}
