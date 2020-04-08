package com.hujinwen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by joe on 2020/4/8
 */
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
        logger.fatal("fatal");
        System.out.println();
    }

}
