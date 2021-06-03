package com.hujinwen.entity.annotations.quartz;

import java.lang.annotation.*;

/**
 * Created by hu-jinwen on 2021/5/26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Scheduled {
    String cron();
}
