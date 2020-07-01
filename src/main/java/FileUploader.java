import com.google.cloud.WriteChannel;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUploader {

    private Bucket bucket;
    private Storage storage;
    private static final int BUFFER_FIXED_SIZE = 32 * 1024;

    public FileUploader(Bucket bucket, Storage storage) {

        this.bucket = bucket;
        this.storage = storage;

    }

    public void uploadFileUsingBlobWriter(String filePathAsString) throws IOException {
        Path filePath = Paths.get(filePathAsString);
        String filenameAsString = filePath.getFileName().toString();

        System.out.println("File: " + filePath);
        System.out.println("Uploading file has started...");

        long start0 = System.nanoTime();
        useBlobWriter(storage, filePath, BlobInfo.newBuilder(bucket.getName(), filenameAsString).build());
        long time0 = System.nanoTime() - start0;
        System.out.printf("Took %.3f ms to upload %,d MB File%n", time0 / 1e6, Files.size(filePath) / 1024 / 1024);
    }


    private synchronized void useBlobWriter(Storage storage, Path filePath, BlobInfo blobInfo) {
        try (WriteChannel writer = storage.writer(blobInfo)) {
            byte[] buffer = new byte[BUFFER_FIXED_SIZE];

            try (InputStream input = Files.newInputStream(filePath)) {
                int limit;
                while ((limit = input.read(buffer)) >= 0) {
                    writer.write(ByteBuffer.wrap(buffer, 0, limit));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
