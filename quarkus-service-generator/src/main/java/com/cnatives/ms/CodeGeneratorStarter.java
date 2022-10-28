package com.cnatives.ms;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

@Component
public class CodeGeneratorStarter {
	
	private final String CODEGENERATION_DIR = "codegeneration";
	private final String ARCHIVES_DIR = "archives";
	private final String DBSCRIPT_DIR = "dbgeneration";
	private final String CLIENT_DIR = "clientgeneration";

	private final Path codeGenerationRoot = Paths.get(CODEGENERATION_DIR);
	private final Path archiveRoot = Paths.get(ARCHIVES_DIR);
	private final Path dbScriptRoot = Paths.get(DBSCRIPT_DIR);
	private final Path clientRoot = Paths.get(CLIENT_DIR);
	
	public void init() {
		try {
			Files.createDirectory(codeGenerationRoot);
			Files.createDirectory(archiveRoot);
			Files.createDirectory(dbScriptRoot);
			Files.createDirectory(clientRoot);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(codeGenerationRoot.toFile());
		FileSystemUtils.deleteRecursively(archiveRoot.toFile());
		FileSystemUtils.deleteRecursively(dbScriptRoot.toFile());
		FileSystemUtils.deleteRecursively(clientRoot.toFile());
	}

}
