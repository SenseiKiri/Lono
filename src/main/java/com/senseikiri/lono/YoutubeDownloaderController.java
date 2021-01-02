package com.senseikiri.lono;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequiredArgsConstructor
@Slf4j
public class YoutubeDownloaderController {

    private final YoutubeDownloadService youtubeDownloadService;

    @GetMapping(path = "/download/youtube/{videoId}")
    public ResponseEntity<byte[]> getYoutubeVideo(@PathVariable String videoId) {
        File videoFile = youtubeDownloadService.downloadYoutubeVideo(videoId);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", videoFile.getName());
            byte[] videoBytes = Files.readAllBytes(videoFile.toPath());
            videoFile.delete();
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(videoBytes);
        } catch (IOException e) {
            log.error("There is no such File");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
