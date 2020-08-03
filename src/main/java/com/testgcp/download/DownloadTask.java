package com.testgcp.download;

import com.testgcp.Main;

import java.util.concurrent.Callable;

public class DownloadTask implements Callable<String> {

    private String path;

    public DownloadTask(String path) {
        this.path = path;
    }

    @Override
    public String call() throws Exception {
        System.out.println("Downloading started for path "+ path);
        FileDownloader fileDownloader = new FileDownloader(Main.getBucket(), Main.getStorage());
        fileDownloader.uploadFileUsingBlobWriter(path);
        return "com.testgcp.download.DownloadTask's execution finished for path: "+path;
    }
}
