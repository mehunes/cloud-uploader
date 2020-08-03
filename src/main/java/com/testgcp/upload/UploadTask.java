package com.testgcp.upload;

import com.testgcp.Main;

import java.util.concurrent.Callable;

public class UploadTask implements Callable<String> {

    private String path;

    public UploadTask(String path) {
        this.path = path;
    }

    @Override
    public String call() throws Exception {
        System.out.println("Uploading started for path "+ path);
        FileUploader fileUploader = new FileUploader(Main.getBucket(), Main.getStorage());
        fileUploader.uploadFileUsingBlobWriter(path);
        return "com.testgcp.upload.UploadTask's execution finished for path: "+path;
    }
}
