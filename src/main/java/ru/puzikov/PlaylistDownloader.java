package ru.puzikov;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;

class PlaylistDownloader {
    private String folder = "./tmp/";

    PlaylistDownloader(String folder) {
        this.folder = folder;
    }

    PlaylistDownloader() {
    }

    Path getByUrl(String url) {
        if (!cleanCacheFolder()) {
            throw new RuntimeException("Inner problems, contact with author");
        }
        cleanCacheFolder();
        try {
            URI uri = new URI(url);
            String filename = UUID.randomUUID().toString().substring(10);
            BufferedReader reader = new BufferedReader(new InputStreamReader(uri.toURL().openStream()));
            String fullName = folder + filename + ".m3u";
            BufferedWriter writer = (new BufferedWriter(new FileWriter(fullName)));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                writer.write(tempString + System.lineSeparator());
            }
            writer.close();
            reader.close();
            return Paths.get(fullName);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Inner problems, contact with author" + e.getMessage());
        }
    }

    boolean cleanCacheFolder() {
        Path path = Paths.get(folder);
        try {
            if (Files.exists(path)) {
                Files.walkFileTree(path, new Deleter());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected class Deleter extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }
    }
}
