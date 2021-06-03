package com.hujinwen.jobs;

import com.hujinwen.entity.annotations.quartz.QuartzJob;
import com.hujinwen.entity.annotations.quartz.Scheduled;
import org.quartz.JobExecutionContext;

/**
 * Created by hu-jinwen on 2021/6/1
 * <p>
 * 测试 job 类
 */
@QuartzJob
public class TestJob {

    @Scheduled(cron = "0/2 * * * * ?")
    public void job1(JobExecutionContext jobExecutionContext) {
        System.out.println("执行了 job1");
    }

    @Scheduled(cron = "0/2 * * * * ?")
    public void job2() {
        System.out.println("执行了 job2");
    }


}
