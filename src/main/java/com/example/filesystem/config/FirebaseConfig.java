package com.example.filesystem.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

@Configuration
public class FirebaseConfig {

    //Una volta configurato firebase

/*    @Value("${firebase.config.path}")
    private String firebaseConfigPath;

    @Value("${firebase.bucket}")
    private String firebaseBucket;

    @PostConstruct
    public void init() throws Exception {
        FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(firebaseBucket)
                .build();

        FirebaseApp.initializeApp(options);
    }*/
}

