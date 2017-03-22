package com.pinterest.cmp.job;

import com.pinterest.cmp.config.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *  A Job exector that runs function in a while loop
 */
public class ForeverRepeatedJobExecutor implements JobExecutor {

  private static final Logger logger = LoggerFactory.getLogger(ForeverRepeatedJobExecutor.class);
  private static final ScheduledExecutorService execService = Executors.newScheduledThreadPool(
      Configuration.getProperties().getInt("num_subscriber", 1));
  private long interval;
  private Runnable runnable;

  public ForeverRepeatedJobExecutor(long interval, Runnable runnable) {
    this.setInterval(interval);
    this.setRunnable(runnable);
  }

  public long getInterval() {
    return interval;
  }

  protected void setInterval(long interval) {
    this.interval = interval;
  }

  public Runnable getRunnable() {
    return runnable;
  }

  protected void setRunnable(Runnable runnable) {
    this.runnable = runnable;
  }

  @Override
  public void execute() {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.submit(() -> {
      execService.scheduleAtFixedRate(getRunnable(), 0, getInterval(), TimeUnit.MILLISECONDS);
    });
  }

  protected void runOnce() {
    this.getRunnable().run();
  }

}