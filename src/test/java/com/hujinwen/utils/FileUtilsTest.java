package com.hujinwen.utils;

import org.junit.jupiter.api.Test;

import java.io.File;

class FileUtilsTest {

    @Test
    void replaceCharNotAllowInWinFilename() {
        final String filename = "\\/*ffff.mp4";
        final String newFilename = FileUtils.replaceCharNotAllowInWinFilename(filename);
        System.out.println();
    }


    @Test
    void getResourceFile() {
        final File resourceFile = FileUtils.getResourceFile("log4j2.xml");
        System.out.println();
    }

}