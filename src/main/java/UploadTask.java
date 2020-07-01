import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;

import java.util.concurrent.Callable;

public class UploadTask implements Callable<String> {

    private String path;

    public UploadTask(String path) {
        this.path = path;
    }

    @Override
    public String call() throws Exception {
        System.out.println("Uploading started for path "+ path);
        FileUploader fileUploader = new FileUploader(Main.getBucket(),Main.getStorage());
        fileUploader.uploadFileUsingBlobWriter(path);
        return "UploadTask's execution finished for path: "+path;
    }
}
