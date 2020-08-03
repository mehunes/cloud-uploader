package com.testgcp;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.testgcp.download.DownloaderFacade;
import com.testgcp.upload.UploaderFacade;

import java.io.*;

public final class Main {

    private static final String JSON_WITH_KEY_LOCATION_PATH = "here_credentials.json";
    private static final String CLOUD_PROJECT_ID = "project-id-here";
    private static final String BUCKET_NAME = "bucket-name-here";


    private static Bucket bucket;
    private static Storage storage;


    public static Bucket getBucket() {
        return bucket;
    }

    public static Storage getStorage() {
        return storage;
    }




    public static void main(String[] args) throws IOException, InterruptedException {

        Credentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(JSON_WITH_KEY_LOCATION_PATH));
        storage = StorageOptions.newBuilder().setCredentials(credentials)
                .setProjectId(CLOUD_PROJECT_ID).build().getService();
        bucket = storage.get(BUCKET_NAME);


        //new UploaderFacade().uploadFiles();

        new DownloaderFacade().downloadFiles();
    }



}
