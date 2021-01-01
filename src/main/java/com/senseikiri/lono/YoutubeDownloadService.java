package com.senseikiri.lono;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioFormat;
import com.github.kiulian.downloader.model.formats.Format;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;

/**
 * It will provide methods for downloading youtube videos.
 */
@Slf4j
public class YoutubeDownloadService {

    private final YoutubeDownloader downloader;

    public YoutubeDownloadService(){
        downloader = new YoutubeDownloader();
    }

    public void downloadYoutubeVideo(String videoId) {
        downloader.setParserRetryOnFailure(1);

        try {
            YoutubeVideo video = downloader.getVideo(videoId);
            Format format = video.formats().stream()
                    .filter(localFormat -> localFormat instanceof AudioFormat)
                    .max(Comparator.comparing(Format::bitrate))
                    .orElseThrow();
            video.download(format, new File("myVideo"));
        } catch (YoutubeException e) {
            log.error("Could not get video for id {}",videoId);
        } catch (IOException e) {
            log.error("Could not download video for id {}", videoId);
        }
    }
}