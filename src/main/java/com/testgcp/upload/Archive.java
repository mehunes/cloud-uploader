package com.testgcp.upload;

import com.google.api.client.util.Lists;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Archive {


    public static final String DOWNLOADS_130_MB_FILE = "130MB_FILE";
    public static final String DOWNLOADS_1_GB_FILE = "1GB_FILE";

    private Bucket bucket;


    private void createTestBlobObject(Bucket bucket, String bucketTextValue, String blobName) {
        byte[] bytes = bucketTextValue.getBytes(UTF_8);
        Blob blob = bucket.create(blobName, bytes);

        System.out.println("Created blob: " + blob.getName());
    }

    private List<String> getAllBlobs() {
        List<String> blobsAsText = Lists.newArrayList();
        Page<Blob> blobs = bucket.list();
        for (Blob blob : blobs.getValues()) {
            blobsAsText.add(new String(blob.getContent()));
        }
        return blobsAsText;

    }

    private String getBlobByName(String name) {
        Page<Blob> blobs = bucket.list();
        for (Blob blob : blobs.getValues()) {
            if (name.equals(blob.getName())) {
                return new String(blob.getContent());
            }
        }
        return "Blob not found";
    }


    private byte[] readFileUsingFC(File fileToRead) throws IOException {
        try (RandomAccessFile reader = new RandomAccessFile(fileToRead, "r");
             FileChannel channel = reader.getChannel();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            int bufferSize = 32*1024*8;
            if (bufferSize > channel.size()) {
                bufferSize = (int) channel.size();
            }
            ByteBuffer buff = ByteBuffer.allocate(bufferSize);

            while (channel.read(buff) > 0) {
                out.write(buff.array(), 0, buff.position());
                buff.clear();
            }

            return out.toByteArray();
        }
    }

    private byte[] readFile(String path){
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
