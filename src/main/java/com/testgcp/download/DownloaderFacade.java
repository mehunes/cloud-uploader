package com.testgcp.download;

import com.google.api.client.util.Lists;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.testgcp.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public class DownloaderFacade {


    public void downloadFiles() throws IOException {
        List<String> allBlobs = getAllBlobs();
    }

    private List<String> getAllBlobs() throws IOException {
        List<String> blobsAsText = Lists.newArrayList();
        Bucket bucket = Main.getBucket();

        Path filePath = Paths.get("/.");

        Page<Blob> blobs = bucket.list();
        Iterable<Blob> blobIterable = blobs.iterateAll();
        for (Blob blob : blobIterable) {
            System.out.println("Downloading: "+blob.getName());
            blob.downloadTo(filePath);
        }


        return blobsAsText;
    }
}
