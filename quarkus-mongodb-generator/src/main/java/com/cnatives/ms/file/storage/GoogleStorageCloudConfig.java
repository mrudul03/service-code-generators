package com.cnatives.ms.file.storage;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.storage.Storage;

@Configuration
public class GoogleStorageCloudConfig {

	@Bean
    Storage configStorageClient() throws GeneralSecurityException, IOException {

        Storage storage = new Storage(GoogleNetHttpTransport.newTrustedTransport(),
                new GsonFactory(), new GoogleHttpRequestInitializer());
        return storage;
    }
}
