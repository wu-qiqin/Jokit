package com.hujinwen.tools;

import java.io.IOException;
import java.io.RandomAccessFile;

class RandomAccessFilePlusTest {

    void test1() throws IOException {
        final RandomAccessFilePlus accessFilePlus = new RandomAccessFilePlus("", "");
        final RandomAccessFile randomAccessFile = new RandomAccessFile("", "");
        final String line = randomAccessFile.readLine();
        final String s = accessFilePlus.readLine("");

    }

}