package com.code.generator.model;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileUploadService {
	
	private final String UPLOADS_DIR = "uploads";
	private final String PATH_SEPARATOR = "/";
	
	public List<String> uploadFiles(MultipartFile[] files, String customerId) {
		List<String> fileNames = new ArrayList<>();
		Path path = Paths.get(UPLOADS_DIR+PATH_SEPARATOR+customerId);
		try {
			boolean isDirExist = Files.exists(path, new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
			
			if(!isDirExist) {
				Path newDir = Files.createDirectory(path);
				Arrays.asList(files).stream().forEach(file -> {
					this.uploadFile(file, newDir);
					fileNames.add(file.getOriginalFilename());
				});
			}
			else {
				//Path newDir = path;
				Arrays.asList(files).stream().forEach(file -> {
					this.uploadFile(file, path);
					fileNames.add(file.getOriginalFilename());
				});
			}
		}
		catch(FileAlreadyExistsException e){
		    // the directory already exists.
			log.error("FileAlreadyExistsException", e);
		} catch (IOException e) {
		    //something else went wrong
		    //e.printStackTrace();
			log.error("IOException", e);
		}
		return fileNames;
	}
	
	private void uploadFile(MultipartFile file, Path newDir) {
		try {
			Files.copy(file.getInputStream(), newDir.resolve(file.getOriginalFilename()), 
					StandardCopyOption.REPLACE_EXISTING);
			log.info("Saved file");

		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}

}
