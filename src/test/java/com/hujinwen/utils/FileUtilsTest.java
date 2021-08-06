package com.hujinwen.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class FileUtilsTest {

    @Test
    void replaceCharNotAllowInWinFilename() {
        final String filename = "\\/*ffff.mp4";
        final String newFilename = FileUtils.replaceCharNotAllowInWinFilename(filename);
        System.out.println();
    }


    @Test
    void getResourceFile() {
        try {
            final File resourceFile = FileUtils.getResourceFile("log4j2.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

}