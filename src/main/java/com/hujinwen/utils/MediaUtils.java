package com.hujinwen.utils;

import com.coremedia.iso.IsoFile;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by hu-jinwen on 2020/6/23
 * <p>
 * 媒体文件相关
 * <p>
 * 参考文档 https://yq.aliyun.com/articles/695538
 */
public class MediaUtils {
    private static final Logger logger = LogManager.getLogger(MediaUtils.class);


    /**
     * 得到语音或视频文件时长,单位秒
     *
     * @param filePath 文件路径
     * @return 时长字符串
     * @throws IOException
     */
    public static String getDuration(String filePath) throws IOException {
        final String format = getMediaFormat(filePath);
        return getDuration(filePath, format);
    }

    /**
     * 得到语音或视频文件时长,单位秒
     *
     * @param filePath 文件路径
     * @return 时长字符串
     * @throws IOException
     */
    public static String getDuration(String filePath, String format) throws IOException {
        float result = 0;
        if ("wav".equals(format)) {
            result = getDurationByFilePath(filePath);
        } else if ("mp3".equals(format)) {
            result = getMp3Duration(filePath);
        } else if ("m4a".equals(format)) {
            result = getMp4Duration(filePath);
        } else if ("mov".equals(format)) {
            result = getMp4Duration(filePath);
        } else if ("mp4".equals(format)) {
            result = getMp4Duration(filePath);
        }
        return TimeUtils.convertTime(String.valueOf(result));
    }


    /**
     * 获取视频文件的播放长度(mp4、mov格式)
     *
     * @param videoPath 视频文件位置
     * @return 单位为毫秒
     */
    private static long getMp4Duration(String videoPath) throws IOException {
        IsoFile isoFile = new IsoFile(videoPath);
        return isoFile.getMovieBox().getMovieHeaderBox().getDuration() /
                isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
    }

    /**
     * 得到pcm文件的毫秒数
     * <p>
     * pcm文件音频时长计算
     * 同图像bmp文件一样，pcm文件保存的是未压缩的音频信息。 16bits 编码是指，每次采样的音频信息用2个字节保存。可以对比下bmp文件用分别用2个字节保存RGB颜色的信息。 16000采样率 是指 1秒钟采样 16000次。常见的音频是44100HZ，即一秒采样44100次。 单声道： 只有一个声道。
     * <p>
     * 根据这些信息，我们可以计算： 1秒的16000采样率音频文件大小是 2*16000 = 32000字节 ，约为32K 1秒的8000采样率音频文件大小是 2*8000 = 16000字节 ，约为 16K
     * <p>
     * 如果已知录音时长，可以根据文件的大小计算采样率是否正常。
     *
     * @param filePath 文件路径
     * @return duration
     */
    private static long getPCMDurationMilliSecond(String filePath) {
        File file = new File(filePath);

        //得到多少秒
        long second = file.length() / 32000;

        long milliSecond = Math.round((file.length() % 32000) / 32000.0 * 1000);

        return second * 1000 + milliSecond;
    }

    /**
     * 获取mp3语音文件播放时长(秒)
     *
     * @param mp3FilePath mp3文件路径
     * @return 时长
     */
    private static int getMp3Duration(String mp3FilePath) {
        try {
            File mp3File = new File(mp3FilePath);
            MP3File f = (MP3File) AudioFileIO.read(mp3File);
            MP3AudioHeader audioHeader = (MP3AudioHeader) f.getAudioHeader();
            return audioHeader.getTrackLength();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return -1;
    }

    /**
     * 获取语音文件播放时长(秒) 支持wav 格式
     *
     * @param filePath
     * @return
     */
    private static Float getDurationByFilePath(String filePath) {
        try {
            File destFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(destFile);
            AudioFormat format = audioInputStream.getFormat();
            long audioFileLength = destFile.length();
            int frameSize = format.getFrameSize();
            float frameRate = format.getFrameRate();
            return (audioFileLength / (frameSize * frameRate));
        } catch (Exception e) {
            e.printStackTrace();
            return 0f;
        }
    }


    /**
     * 获取mp3文件时长
     *
     * @param url 媒体文件url
     * @deprecated 失败率较高，经常出现获取到的时长不准确的情况。
     */
    @Deprecated
    public static float getMp3DurationByUrl(String url) {
        float duration = -1;

        InputStream inputStream = null;
        Bitstream bitstream = null;
        try {
            final URLConnection connection = new URL(url).openConnection();
            final int contentLength = connection.getContentLength();
            inputStream = connection.getInputStream();
            // 计算时长
            bitstream = new Bitstream(inputStream);
            Header header = bitstream.readFrame();
            for (int i = 0; i < 3; i++) {
                if (header != null) {
                    break;
                }
                header = bitstream.readFrame();
            }
            if (header == null) {
                return duration;
            }

            duration = header.total_ms(contentLength) / 1000;
            logger.info("calculate duration successfully! url -> {}, duration -> {}", url, duration);
        } catch (BitstreamException | IOException e) {
            e.printStackTrace();
        } finally {
            if (bitstream != null) {
                try {
                    bitstream.close();
                } catch (BitstreamException ignored) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
        return duration;
    }

    /**
     * 得到文件格式
     *
     * @param filePath 路径
     * @return 后缀
     */
    private static String getMediaFormat(String filePath) {
        return filePath.toLowerCase().substring(filePath.toLowerCase().lastIndexOf(".") + 1);
    }

}
