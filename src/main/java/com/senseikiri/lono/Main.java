package com.senseikiri.lono;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        YoutubeDownloadService service = new YoutubeDownloadService();
        service.downloadYoutubeVideo("6hhjTUImkmY");
    }
}
