package com.cnatives.ms.generator.msclient.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cnatives.ms.generator.archive.service.FileArchiveService;
import com.cnatives.ms.generator.mscontroller.CodeGenerationRequest;
import com.cnatives.ms.generator.mscontroller.Configurations;
import com.cnatives.ms.generator.mscontroller.DomainModel;
import com.cnatives.ms.generator.mscontroller.FileInfo;
import com.cnatives.ms.generator.msservice.base.GeneratorMetaData;
import com.cnatives.ms.generator.storage.StorageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MsClientService {

	private MsClientGenerator clientGenerator;
	//private StorageService storageService;
	
	//private final String PATH_SEPARATOR = "/";
	private final String CLIENT_DIR = "clientgeneration";
	//private final Path archiveRoot = Paths.get("");
	
	public MsClientService(MsClientGenerator clientGenerator, StorageService storageService) {
		this.clientGenerator = clientGenerator;
		//this.storageService = storageService;
	}	
	
	public FileInfo generateCode(CodeGenerationRequest request, String customerId) {
		log.info("logging from service.....");
		
		Configurations configurations = request.getConfigurations();
		configurations.initialize();
		List<DomainModel> domainModels = request.getDomains();
		
		GeneratorMetaData metaData = new GeneratorMetaData();
		metaData.initialize(configurations, domainModels, customerId);
		
		clientGenerator.generateClient(request, configurations, metaData);
		log.info("generated code.....");
		
		//String sourceDirName = metaData.getClientbasePath()+metaData.getServiceName()+"-client";
		//String archiveDestName = ARCHIVES_DIR+PATH_SEPARATOR+customerId+PATH_SEPARATOR+metaData.getServiceName()+"-clientarchive.zip";
		
		String sourceDirName = metaData.getClientGenerationDir();
		String archiveDestName = metaData.getClientArchiveFilename();
		log.info("generated code at::"+sourceDirName);
		
		FileArchiveService fileArchiveService = new FileArchiveService();
		fileArchiveService.archiveFiles(customerId, sourceDirName, archiveDestName, this.CLIENT_DIR);
		String filename = configurations.getServiceName()+"-client.zip";
		//this.uploadFile(archiveDestName, filename);
		FileInfo fileInfo = new FileInfo(customerId, filename);
		return fileInfo;
	}
	
//	private void uploadFile(final String zipDestinationName, String filename) {
//		Path path = archiveRoot.resolve(zipDestinationName);
//		log.info("Got file path");
//		File file = path.toFile();
//		log.info("Got file::");
//		//String filename = serviceName+"-archive.zip";
//		storageService.uploadFile(file, "", filename);
//	}
	
//	public Stream<Path> loadAll(String customerId) {
//		try {
//			Path archivePath = Paths.get(ARCHIVES_DIR+PATH_SEPARATOR+customerId);
//			
//			return Files.walk(archivePath, 10).
//					filter(path -> !path.equals(archivePath)).map(archivePath::relativize);
//		} catch (IOException e) {
//			throw new RuntimeException("Could not load the files!");
//		}
//	}
//	
//	public Resource load(String filename, String customerId) {
//		try {
//			Path file = archiveRoot.resolve(customerId+PATH_SEPARATOR+filename);
//			Resource resource = new UrlResource(file.toUri());
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
}
