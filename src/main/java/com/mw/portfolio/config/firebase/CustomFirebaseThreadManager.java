package com.mw.portfolio.config.firebase;

import com.google.firebase.FirebaseApp;
import com.google.firebase.ThreadManager;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Component
@AllArgsConstructor
public class CustomFirebaseThreadManager extends ThreadManager {

  private final ThreadPoolTaskExecutor executor;

  @PreDestroy
  public void cleanup() {
    executor.shutdown();
  }

  @Override
  protected ExecutorService getExecutor(FirebaseApp app) {
    executor.setCorePoolSize(executor.getPoolSize() + 1);
    return executor.getThreadPoolExecutor();
  }

  @Override
  protected void releaseExecutor(FirebaseApp app, ExecutorService executor) {
    app.delete();
    executor.shutdown();
  }

  @Override
  protected ThreadFactory getThreadFactory() {
    return Executors.defaultThreadFactory();
  }
}
