package com.hujinwen.utils;

import org.junit.jupiter.api.Test;

class FileUtilsTest {

    @Test
    void replaceCharNotAllowInWinFilename() {
        final String filename = "\\/*ffff.mp4";
        final String newFilename = FileUtils.replaceCharNotAllowInWinFilename(filename);
        System.out.println();
    }

}