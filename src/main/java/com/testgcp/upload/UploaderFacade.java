package com.testgcp.upload;

import com.testgcp.upload.UploadExecutor;

import java.util.Arrays;
import java.util.List;

public class UploaderFacade {

    private static String [] paths=  {
            "1MB_FILE.pdf"
    };



    public void uploadFiles() throws InterruptedException {

        List<String> pathsToUpload = Arrays.asList(paths);
        UploadExecutor uploadExecutor = new UploadExecutor();
        uploadExecutor.execute(pathsToUpload);
    }
}
