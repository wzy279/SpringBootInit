package com.wzyy.springbootinit.job.once;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

/**
 * 只执行一次
 *
 */
// todo 取消注释开启任务
//@Component
@Slf4j
public class FullSyncPostToEs implements CommandLineRunner {


    @Override
    public void run(String... args) {
    }
}
