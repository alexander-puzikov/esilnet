package ru.puzikov;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class PlaylistDownloaderTest {
    private String dirName = "./tmp/";
    private String protocol = "http://";
    private String esilnetPlaylistUrl = "data.esilnet.com/Manual/iptv/newtv.m3u";
    PlaylistDownloader downloader;

    @Before
    public void before() throws IOException {
        Path dir = Paths.get(dirName);
        downloader = new PlaylistDownloader(dirName);
        Files.walkFileTree(dir, downloader.new Deleter());
        Files.deleteIfExists(dir);
        Files.createDirectory(dir);
    }

    @Test
    public void getFileFromEsilnetDotCom() throws Exception {
        Path file = downloader.getByUrl(protocol + esilnetPlaylistUrl);
        assertThat("Correct file type", file.getFileName().toString(), endsWith(".m3u"));
        assertThat("Correct file size", Files.size(file), greaterThan(0l));
    }

    @Test
    public void createRandomFilesAndCheckFilesInCache() throws Exception {
        int filesCount = fillDirectoryWithRandomFiles(dirName);
        assertThat("Files are the same count as created", Files.list(Paths.get(dirName)).count(), equalTo((long) filesCount));
    }


    @Test
    public void createAndCheckFilesClearInCache() throws Exception {
        fillDirectoryWithRandomFiles(dirName);
        downloader.cleanCacheFolder();
        assertThat("tmp folder empty", Files.list(Paths.get(dirName)).count(), equalTo(0l));
    }


    private int fillDirectoryWithRandomFiles(String directory) throws IOException {
        Random r = new Random();
        int fileCount = r.nextInt(15) + 1;
        for (int i = 0; i < fileCount; i++) {
            Files.createFile(Paths.get(directory + UUID.randomUUID().toString().substring(10)));
        }
        return fileCount;

    }

}