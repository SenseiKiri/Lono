package com.senseikiri.lono.services;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioFormat;
import com.github.kiulian.downloader.model.formats.Format;
import com.senseikiri.lono.entities.YoutubeMedia;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;

/**
 * It will provide methods for downloading youtube videos.
 */
@Slf4j
@Component
public class YoutubeDownloadService {

    private final YoutubeDownloader downloader;

    public YoutubeDownloadService() {
        downloader = new YoutubeDownloader();
        downloader.setParserRetryOnFailure(1);
    }

    public YoutubeMedia downloadYoutubeVideo(String videoId) {
        try {
            YoutubeVideo video = downloader.getVideo(videoId);
            File videoFile = downloadHighesAudioBitrate(video);
            YoutubeMedia youtubeMedia = YoutubeMedia.builder()
                    .filename(videoFile.getName())
                    .data(Files.readAllBytes(videoFile.toPath()))
                    .build();
            deleteYoutubeFolder(videoFile);
            return youtubeMedia;
        } catch (YoutubeException e) {
            log.error("Could not get video for id {}", videoId);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("Could not download video for id {}", videoId);
            throw new RuntimeException(e);
        }
    }

    private void deleteYoutubeFolder(File videoFile) {
        boolean isDeleted = videoFile.delete();
        if (!isDeleted) {
            log.error("Youtube folder could not be deleted ({})", videoFile.getName());
        }
    }

    private File downloadHighesAudioBitrate(YoutubeVideo video) throws IOException, YoutubeException {
        Format format = video.formats().stream()
                .filter(localFormat -> localFormat instanceof AudioFormat)
                .max(Comparator.comparing(Format::bitrate))
                .orElseThrow();
        return video.download(format, new File("ytvideo"));
    }
}
