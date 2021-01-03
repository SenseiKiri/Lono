package com.senseikiri.lono.rest.controller;

import com.senseikiri.lono.entities.YoutubeMedia;
import com.senseikiri.lono.services.YoutubeDownloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class YoutubeDownloaderController {

    private final YoutubeDownloadService youtubeDownloadService;

    @GetMapping(path = "/download/youtube/{videoId}")
    public ResponseEntity<byte[]> getYoutubeVideo(@PathVariable String videoId) {
        YoutubeMedia youtubeMedia = youtubeDownloadService.downloadYoutubeVideo(videoId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", youtubeMedia.getFilename());
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(youtubeMedia.getData());
    }
}
