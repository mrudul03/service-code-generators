package com.code.generator.model;

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
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.code.generator.ms.input.Configurations;
import com.code.generator.ms.input.DomainModel;
import com.code.generator.ms.input.Models;
import com.code.generator.ms.input.MsGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MsGeneratorService {
	
	private final String UPLOADS_DIR = "uploads";
	private final String CODEGENERATION_DIR = "codegeneration";
	private final String ARCHIVES_DIR = "archives";
	private final String PATH_SEPARATOR = "/";

	private final Path root = Paths.get(UPLOADS_DIR);
	private final Path codeGenerationRoot = Paths.get(CODEGENERATION_DIR);
	private final Path archiveRoot = Paths.get(ARCHIVES_DIR);
	
	final int BUFFER = 1024;

	private MsGenerator microserviceGenerator;
	private FileUploadService fileUploadService;
	private FileProcessService fileProcessService;

	public MsGeneratorService(final MsGenerator microserviceGenerator,
			final FileUploadService fileUploadService,
			final FileProcessService fileProcessService) {
		this.microserviceGenerator = microserviceGenerator;
		this.fileUploadService = fileUploadService;
		this.fileProcessService = fileProcessService;
	}

	public void init() {
		try {
			Files.createDirectory(root);
			Files.createDirectory(codeGenerationRoot);
			Files.createDirectory(archiveRoot);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
		FileSystemUtils.deleteRecursively(codeGenerationRoot.toFile());
		FileSystemUtils.deleteRecursively(archiveRoot.toFile());
	}
	
	public void generateMsCode(Configurations configurations, 
			List<DomainModel> domainModels,
			String customerId) {
		
		Models models = new Models();
		models.setModels(domainModels);
		
		String serviceDirectoryName = microserviceGenerator.generateServiceCode(
				configurations, models, customerId);
		log.info("serviceDirectoryName:"+serviceDirectoryName);
		
		String sourceDirName = CODEGENERATION_DIR+PATH_SEPARATOR+customerId+PATH_SEPARATOR+serviceDirectoryName;
		String archiveDestName = ARCHIVES_DIR+PATH_SEPARATOR+customerId+PATH_SEPARATOR+serviceDirectoryName+"-archive.zip";
		
		FileArchiveService fileArchiveService = new FileArchiveService();
		fileArchiveService.archiveFiles(customerId, sourceDirName, archiveDestName);
		
	}
	
	public void generateMsCode(MultipartFile[] files, String customerId) {
		// upload files (models and configurations)
		List<String> fileNames = fileUploadService.uploadFiles(files, customerId);
		
		// process uploaded files to get list of models and configuration
		FileProcessResponse fileProcessResponse = fileProcessService.processInputFiles(customerId, fileNames);
		
		// generate service code
		String serviceDirectoryName = microserviceGenerator.generateServiceCode(
				fileProcessResponse.getConfigurations(), fileProcessResponse.getModels(), customerId);
		log.info("serviceDirectoryName:"+serviceDirectoryName);
		
		String sourceDirName = CODEGENERATION_DIR+PATH_SEPARATOR+customerId+PATH_SEPARATOR+serviceDirectoryName;
		String archiveDestName = ARCHIVES_DIR+PATH_SEPARATOR+customerId+PATH_SEPARATOR+serviceDirectoryName+"-archive.zip";
		
		FileArchiveService fileArchiveService = new FileArchiveService();
		fileArchiveService.archiveFiles(customerId, sourceDirName, archiveDestName);
	}
	
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
