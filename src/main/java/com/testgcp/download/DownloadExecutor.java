package com.testgcp.download;

import com.google.api.client.util.Lists;

import java.util.List;
import java.util.concurrent.*;

public class DownloadExecutor {


    public void execute(List<String> paths) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(paths.size());

        List<Callable<String>> callableTasks = Lists.newArrayList();
        paths.stream().map(DownloadTask::new).forEach(callableTasks::add);

        long start0 = System.nanoTime();
        List<Future<String>> futures = executorService.invokeAll(callableTasks);
        futures.parallelStream().forEach(stringFuture -> {
            try {
                System.out.println(stringFuture.get(200, TimeUnit.SECONDS));
            } catch (InterruptedException | TimeoutException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        long time0 = System.nanoTime() - start0;
        System.out.println("+++The whole process of downloading took around "+time0 / 1e9+"s" );


        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

    }

}
