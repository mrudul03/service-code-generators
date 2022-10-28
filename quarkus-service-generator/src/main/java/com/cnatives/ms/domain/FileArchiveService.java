package com.cnatives.ms.domain;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.util.FileSystemUtils;

import com.cnatives.ms.exception.FileArchiveException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileArchiveService {

	private List<File> fileList = new ArrayList<File>();
	final int BUFFER = 1024;
	private final String ARCHIVES_DIR = "archives";
	private final String PATH_SEPARATOR = "/";
	
	public void archiveFiles(final String customerId,
			final String sourceDirectoryName, final String zipDestinationName,
			final String codeGenerationDirName) {
		
		log.info("sourceDirectoryName:"+sourceDirectoryName);
		log.info("zipDestinationName:"+zipDestinationName);
		
		this.deleteExisitngFile(zipDestinationName);
		this.createArchiveDir(customerId);
		this.createArchive(customerId, sourceDirectoryName, zipDestinationName, codeGenerationDirName);
	}
	
	private void deleteExisitngFile(final String zipDestinationName) {
		try {
			Path path = Paths.get(zipDestinationName);
			boolean result = FileSystemUtils.deleteRecursively(path);
			log.info("File deleted:"+result);
		} 
		catch(NoSuchFileException e) {
			log.error("NoSuchFileException::File does not exist");
		}
		catch (IOException ioExp) {
			log.error("IOException::Error while deleting archive file " + ioExp);
			throw new FileArchiveException("Error while deleting archiving file");
		}
	}
	
	private String getFileName(final String customerId, String filePath, String codeGenerationDirName) {
		String name = filePath.substring((codeGenerationDirName+"/"+customerId).length() + 1, filePath.length());
		return name;
	}
	
	private List<File> getFileList(File source) {

		if (source.isFile()) {
			fileList.add(source);
		} else if (source.isDirectory()) {
			String[] subList = source.list();
			// This condition checks for empty directory
			if (subList.length == 0) {
				log.info("path -- " + source.getAbsolutePath());
				fileList.add(new File(source.getAbsolutePath()));
			}
			for (String child : subList) {
				getFileList(new File(source, child));
			}
		}
		return fileList;
	}
	
	private void createArchiveDir(String customerId) {
		Path path = Paths.get(ARCHIVES_DIR+PATH_SEPARATOR+customerId);
		try {
			boolean isDirExist = Files.exists(path, new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
			if(!isDirExist) {
				Files.createDirectory(path);
				log.info("Created archive dir");
			}
			else {
				log.info("Archive dir already exists");
			}
		}
		catch(FileAlreadyExistsException e){
		    // the directory already exists.
			log.error("FileAlreadyExistsException", e);
			throw new FileArchiveException("Archived file already exists ");
		} catch (IOException e) {
		    //something else went wrong
			log.error("IOException: Error while creating archive directory " + e);
			throw new FileArchiveException("Error while creating archive directory");
		}
	}
	
	private void createArchive(final String customerId,
			final String sourceDirectoryName, 
			final String zipDestinationName,
			final String codeGenerationDirName) {
		try {
			
			List<File> fileList = this.getFileList(new File(sourceDirectoryName));
			
			// Creating ZipOutputStream - Using input name to create output name
			FileOutputStream fos = new FileOutputStream(zipDestinationName);
			ZipOutputStream zos = new ZipOutputStream(fos);
			// looping through all the files
			for (File file : fileList) {
				// To handle empty directory
				if (file.isDirectory()) {
					// ZipEntry --- Here file name can be created using the source file
					ZipEntry ze = new ZipEntry(getFileName(customerId, file.toString(), codeGenerationDirName) + "/");
					// Putting zipentry in zipoutputstream
					zos.putNextEntry(ze);
					zos.closeEntry();
				} else {
					FileInputStream fis = new FileInputStream(file);
					BufferedInputStream bis = new BufferedInputStream(fis, BUFFER);
					// ZipEntry --- Here file name can be created using the source file
					ZipEntry ze = new ZipEntry(getFileName(customerId, file.toString(), codeGenerationDirName));
					// Putting zipentry in zipoutputstream
					zos.putNextEntry(ze);
					byte data[] = new byte[BUFFER];
					int count;
					while ((count = bis.read(data, 0, BUFFER)) != -1) {
						zos.write(data, 0, count);
					}
					bis.close();
					zos.closeEntry();
				}
			}
			zos.close();
		} catch (IOException ioExp) {
			log.error("Error while archiving " + ioExp);
			throw new FileArchiveException("Error while archiving file");
		}
	}
}
