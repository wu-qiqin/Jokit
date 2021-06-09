package com.hujinwen.tools;

import org.junit.jupiter.api.Test;
import org.quartz.SchedulerException;

class QuartzJobSchedulerTest {

    @Test
    void test() throws SchedulerException {
        QuartzJobScheduler.schedule();
        System.out.println();
    }

    public static void main(String[] args) throws SchedulerException {
        QuartzJobScheduler.schedule();
        System.out.println();
    }

}