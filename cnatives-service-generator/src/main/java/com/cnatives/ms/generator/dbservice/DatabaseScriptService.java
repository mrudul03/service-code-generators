package com.cnatives.ms.generator.dbservice;

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

import com.cnatives.ms.generator.archive.service.FileArchiveService;
import com.cnatives.ms.generator.dbcontroller.DbGenerationRequest;
import com.cnatives.ms.generator.mscontroller.Configurations;
import com.cnatives.ms.generator.mscontroller.DomainModel;
import com.cnatives.ms.generator.mscontroller.FileInfo;
import com.cnatives.ms.generator.msservice.base.GeneratorMetaData;
import com.cnatives.ms.generator.storage.StorageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DatabaseScriptService {
	
	private final String DBSCRIPT_DIR = "dbgeneration";
	private final String ARCHIVES_DIR = "archives";
	private final String PATH_SEPARATOR = "/";
	
	private DbModelGenerator dbGenerator;
	//private StorageService storageService;
	
	private final Path archiveRoot = Paths.get("");
	
	public DatabaseScriptService(DbModelGenerator dbGenerator,
			StorageService storageService) {
		
		this.dbGenerator = dbGenerator;
		//this.storageService = storageService;
	}
	
	public FileInfo generateDbScript(DbGenerationRequest request, String customerId) {
		log.info("logging from service.....");
		Configurations configurations = request.getConfigurations();
		configurations.initialize();
		List<DomainModel> domainModels = request.getDomains();
		
		GeneratorMetaData metaData = new GeneratorMetaData();
		metaData.initialize(configurations, domainModels, customerId);
		dbGenerator.generateDbModel(metaData, request);
		log.info("generated code.....");
		
		String sourceDirName = metaData.getDbscriptGenerationDir();
		String archiveDestName = metaData.getDbscriptArchiveFilename();
		log.info("generated code at::"+sourceDirName);
		
		FileArchiveService fileArchiveService = new FileArchiveService();
		fileArchiveService.archiveFiles(customerId, sourceDirName, archiveDestName, this.DBSCRIPT_DIR);
		String filename = configurations.getServiceName()+"-dbscript.zip";
		//this.uploadFile(archiveDestName, filename);
		FileInfo fileinfo = new FileInfo(customerId, filename);
		
		log.info("generated db script.....");
		return fileinfo;
	}
	
//	private void uploadFile(final String zipDestinationName, String filename) {
//		Path path = archiveRoot.resolve(zipDestinationName);
//		log.info("Got file path");
//		File file = path.toFile();
//		log.info("Got file::");
//		//String filename = serviceName+"-archive.zip";
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
