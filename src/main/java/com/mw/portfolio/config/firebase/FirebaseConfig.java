package com.mw.portfolio.config.firebase;

import static java.lang.Runtime.getRuntime;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.ThreadManager;
import com.google.firebase.auth.FirebaseAuth;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;

@Configuration
public class FirebaseConfig {

  private static final String FIREBASE_EXECUTOR_NAME = "firebase-exec";
  private static final String FIREBASE_THREAD_PREFIX = "firebase-custom";

  @Bean
  public ThreadPoolTaskExecutor firebaseThreadPoolExecutor() {
    val executor = new ThreadPoolTaskExecutor();
    val processorCount = getRuntime().availableProcessors();
    val corePoolSize = processorCount <= 1 ? processorCount : processorCount >> 1;

    executor.initialize();
    executor.setCorePoolSize(corePoolSize);
    executor.setThreadGroupName(FIREBASE_EXECUTOR_NAME);
    executor.setThreadNamePrefix(FIREBASE_THREAD_PREFIX);

    return executor;
  }

  @Bean
  public FirebaseApp firebaseApp(ThreadManager customThreadManager) throws IOException {
    return FirebaseApp.initializeApp(FirebaseOptions.builder()
        .setThreadManager(customThreadManager)
        .setProjectId("warrenburg-portfolio")
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .build());
  }

  @Bean
  public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
    return FirebaseAuth.getInstance(firebaseApp);
  }
}
