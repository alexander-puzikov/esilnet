package ru.puzikov;

import java.nio.file.Path;

public class Main {
    private String esilnetPlaylistUrl = "data.esilnet.com/Manual/iptv/newtv.m3u";
    private String protocol = "http://";
    private static String DEFAULT_CODE = "68TTK";


    public static void main(String[] args) {
        if (args.length == 0) {
            new Main();
        } else {
            new Main(args[0]);
        }
    }

    private Main() {
        this(DEFAULT_CODE);
    }

    private Main(String key) {
        Path playlistFile = new PlaylistDownloader().getByUrl(protocol + esilnetPlaylistUrl);
        new SSIPTVPlaylistUploader().uploadFileWithKey(playlistFile, key);
    }
}
