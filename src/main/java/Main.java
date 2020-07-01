import com.google.api.client.util.Lists;
import com.google.api.gax.paging.Page;
import com.google.api.gax.retrying.RetryingExecutor;
import com.google.api.gax.retrying.RetryingExecutorWithContext;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class Main {

    private static final String JSON_WITH_KEY_LOCATION_PATH = "here_credentials.json";
    private static final String CLOUD_PROJECT_ID = "project-id-here";
    private static final String BUCKET_NAME = "bucket-name-here";


    private static Bucket bucket;
    private static Storage storage;

            private static String [] paths=  {"32MB_FILE1",
                                    "32MB_FILE2"
            };

    public static void main(String[] args) throws IOException, InterruptedException {
//        Storage storage = StorageOptions.getDefaultInstance().getService();

        Credentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(JSON_WITH_KEY_LOCATION_PATH));
        storage = StorageOptions.newBuilder().setCredentials(credentials)
                .setProjectId(CLOUD_PROJECT_ID).build().getService();
        bucket = storage.get(BUCKET_NAME);


        List<String> pathsToUpload = Arrays.asList(paths);
        UploadExecutor uploadExecutor = new UploadExecutor();
        uploadExecutor.execute(pathsToUpload);
        //main.createTestBlobObject(bucket, "Text inside my blob", "test-blob-2");
        //main.createTestBlobObject(bucket, "Text inside my blob 2", "test-blob-3");





        //List<String> blobsByName = main.getAllBlobs();
        //System.out.println("DONE - All blobs content as list of strings: \n" + blobsByName);

    }


    public static Bucket getBucket() {
        return bucket;
    }

    public static Storage getStorage() {
        return storage;
    }

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
