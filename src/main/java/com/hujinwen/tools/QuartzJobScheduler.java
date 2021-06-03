package com.hujinwen.tools;

import com.hujinwen.entity.annotations.quartz.QuartzJob;
import com.hujinwen.entity.annotations.quartz.Scheduled;
import com.hujinwen.exceptions.quartz.InstanceInitializeException;
import com.hujinwen.utils.ClassUtils;
import com.hujinwen.utils.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hu-jinwen on 2021/6/2
 * <p>
 * quartz 任务调度装载
 */
public class QuartzJobScheduler {
    private static final Logger logger = LoggerFactory.getLogger(QuartzJobScheduler.class);

    private static final String JOB_METHOD_KEY = "JOB_METHOD";

    /**
     * job class -> 实例 映射
     */
    private static final Map<Class<?>, Object> JOB_CLASS_INSTANCE_MAP = new HashMap<>();

    private static final Scheduler DEFAULT_SCHEDULER;

    static {
        try {
            DEFAULT_SCHEDULER = StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            throw new RuntimeException("StdSchedulerFactory.getDefaultScheduler failed!!!");
        }
    }

    /**
     * 开始装载
     */
    public static void schedule() throws SchedulerException {
        final Class<?>[] jobClasses = ClassUtils.scanClass(clazz -> clazz.getAnnotation(QuartzJob.class) != null);

        for (Class<?> clazz : jobClasses) {
            try {
                final Constructor<?> constructor = clazz.getConstructor();
                JOB_CLASS_INSTANCE_MAP.put(clazz, constructor.newInstance());
            } catch (Exception e) {
                throw new InstanceInitializeException(e);
            }

            final Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                final Scheduled scheduled = method.getAnnotation(Scheduled.class);
                if (scheduled == null) {
                    continue;
                }
                final String cron = scheduled.cron();
                if (StringUtils.isBlank(cron)) {
                    logger.error("Cron must not be None! {}", method);
                    continue;
                }

                final CronTrigger cronTrigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
                final JobDetail jobDetail = JobBuilder.newJob(DefaultJob.class).setJobData(new JobDataMap() {{
                    put(JOB_METHOD_KEY, method);
                }}).build();

                DEFAULT_SCHEDULER.scheduleJob(jobDetail, cronTrigger);
            }
        }
        DEFAULT_SCHEDULER.start();
    }

    public static void shutdown() throws SchedulerException {
        DEFAULT_SCHEDULER.shutdown();
    }

    public static class DefaultJob implements org.quartz.Job {
        private static final Logger logger = LoggerFactory.getLogger(DefaultJob.class);

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            final Method method = (Method) jobExecutionContext.getMergedJobDataMap().get(JOB_METHOD_KEY);

            // 构造执行参数
            final Class<?>[] paramTypes = method.getParameterTypes();
            final Object[] params = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                final Class<?> paramType = paramTypes[i];
                if (paramType == JobExecutionContext.class) {
                    params[i] = jobExecutionContext;
                }
            }

            final Object instance = JOB_CLASS_INSTANCE_MAP.get(method.getDeclaringClass());
            try {
                method.invoke(instance, params);
            } catch (Exception e) {
                logger.error("Method invoke failed!", e);
            }
        }
    }


}
