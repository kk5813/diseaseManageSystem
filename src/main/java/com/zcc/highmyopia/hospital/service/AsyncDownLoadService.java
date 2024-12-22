package com.zcc.highmyopia.hospital.service;

import com.zcc.highmyopia.hospital.repository.ISaveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @Author zcc
 * @Date 2024/12/19
 * @Description
 */
@Slf4j
@Service
public class AsyncDownLoadService{

    @Async
    public CompletableFuture<String> downloadReportImage() {
        // 模拟下载图像的任务
        try {
            Thread.sleep(50000);  // 假设下载需要50秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture("Download completed");
    }

    @Async
    public CompletableFuture<String> callDeepLearningModel() {
        // 模拟模型预测的任务
        try {
            Thread.sleep(50000);  // 假设预测需要50秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture("Model prediction completed");
    }
}
