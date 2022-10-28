package com.cnatives.ms.generator.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.http.GenericUrl;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.model.StorageObject;

@Component
public class GoogleStorageClientAdapter {

	Storage storage;
    String bucketName = "dev-generated-code";
    
    public GoogleStorageClientAdapter(@Autowired Storage storage) {
        
    	this.storage = storage;
    }
    
    public StorageObject download(String fileName) throws IOException {
        StorageObject object = storage.objects().get(bucketName, fileName).execute();
        File file = new File(fileName);
        FileOutputStream os = new FileOutputStream(file);

        storage.getRequestFactory()
                .buildGetRequest(new GenericUrl(object.getMediaLink()))
                .execute()
                .download(os);
        object.set("file", file);
        return object;
    }
}
