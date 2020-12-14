package com.hujinwen.utils;


import com.hujinwen.core.AnimatedGifEncoder.AnimatedGifEncoder;
import com.hujinwen.core.GifSequenceWriter.GifSequenceWriter;
import org.apache.commons.io.IOUtils;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by hu-jinwen on 2020/2/27
 * <p>
 * 图片相关工具
 */
public class ImageUtils {

    public static void imagesToGif(List<BufferedImage> bufferedImages, String target) throws IOException {

        try (ImageOutputStream output = new FileImageOutputStream(new File(target));
             GifSequenceWriter writer = new GifSequenceWriter(output, bufferedImages.get(0).getType(), 1000, false)) {
            bufferedImages.forEach(s -> {
                try {
                    writer.writeToSequence(s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 图片转gif
     */
    public static byte[] imagesToGif(List<BufferedImage> images) throws IOException {
        try (
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                final ImageOutputStream ImageOutputStream = new MemoryCacheImageOutputStream(byteArrayOutputStream);
                final GifSequenceWriter writer = new GifSequenceWriter(ImageOutputStream, images.get(0).getType(), 500, false)
        ) {
            for (BufferedImage img : images) {
                writer.writeToSequence(img);
            }
            ImageOutputStream.flush();
            byteArrayOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        }
    }

    /**
     * 图片转gif
     */
    public static byte[] imagesToGif2(List<BufferedImage> images) throws IOException {
        try (
                final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ) {
            AnimatedGifEncoder encoder = new AnimatedGifEncoder();
            encoder.setRepeat(0);
            encoder.start(outputStream);
            for (BufferedImage img : images) {
                encoder.addFrame(img);
                encoder.setDelay(500);
            }
            encoder.finish();
            outputStream.flush();
            return outputStream.toByteArray();
        }
    }


    public static byte[] readGifFile(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }

    public static void bytesToFile(byte[] bytes, String target) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             FileOutputStream fo = new FileOutputStream(new File(target))) {
            IOUtils.copy(bis, fo);
        }

    }


}