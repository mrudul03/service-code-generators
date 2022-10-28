package com.cnatives.ms.file.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//@Slf4j
@Service
public class StorageService {

//	private final String PATH_SEPARATOR = "/";
	
	@Value("${gcp.project.id}")
	private String projectId;
	
	@Value("${file.archive.bucketname}")
	private String bucketName;
	
//	public void uploadFile(
//			File file, String filepath, String filename){
//
//		try {
//			log.info("bucketName::"+bucketName);
//			log.info("projectId::"+projectId);
//	        BlobId blobId = BlobId.of(bucketName, filename);
//	        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
//	        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
//	        // to convert multipartFile to File
//	        Blob blob = storage.create(blobInfo, Files.readAllBytes(file.toPath()));
//	        
//	        log.info("Uploaded file name::"+blob.getSelfLink());
//		}
//		catch(IOException e) {
//			log.error("IOException", e);
//			throw new StorageException("IOException::"+e.getMessage());
//		}
//		log.info("Object upload complete");
//	}
//	
//	public String generatePresignedGetUrl(String projectId,
//			String bucketName, String filepath, String filename) {
//		Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
//		//BlobId blobId = BlobId.of(bucketName, filepath);
//		
//		BlobId blobId = null;
//		if(this.isValidField(filepath)) {
//			blobId = BlobId.of(bucketName, filepath+PATH_SEPARATOR+filename);
//		}
//		else {
//			blobId = BlobId.of(bucketName, filename);
//		}
//		
//		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
//		
//	    URL url =
//	            storage.signUrl(blobInfo, 15, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature());
//	    log.info("pre-signed URL for GET operation has been generated.");
//		String presignedUrl = url.toString();
//		return presignedUrl;
//	}
//	
//	public void deleteDocument(String projectId,
//			String bucketName, String filepath, String filename) {
//		
//		String folderfilepath = "";
//		if(this.isValidField(filepath)) {
//			folderfilepath = filepath+PATH_SEPARATOR+filename;
//		}
//		else {
//			folderfilepath = filename;
//		}
//		
//		Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
//	    storage.delete(bucketName, folderfilepath);
//	    log.info("Deleted bucket object::"+folderfilepath);
//	}
//	
//	private boolean isValidField(String field) {
//		boolean bValid = true;
//		if(null == field || field.isBlank() || field.isEmpty()) {
//			bValid = false;
//		}
//		return bValid;
//	}
}
