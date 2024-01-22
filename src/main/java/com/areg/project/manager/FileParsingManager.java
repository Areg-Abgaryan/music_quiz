/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.manager;

import com.areg.project.model.MusicAlbum;
import com.areg.project.builder.MusicDatabaseBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class FileParsingManager {

    private static final Logger logger = LoggerFactory.getLogger(FileParsingManager.class);
    private static final Gson gson = new GsonBuilder().create();
    private final MusicDatabaseBuilder musicDatabaseBuilder = new MusicDatabaseBuilder();

    public void parseDataFiles(String dataFilesPath) {

        if (! isValid(dataFilesPath)) {
            return;
        }

        final File[] filesInDirectory = new File(dataFilesPath).listFiles();
        if (filesInDirectory == null || filesInDirectory.length == 0) {
            logger.error("Error : No data files found in the directory !");
            return;
        }

        final long startTime = System.currentTimeMillis();
        final Set<MusicAlbum> albums = parallelDataFileParsing(filesInDirectory);

        logger.info("parseDataFiles takes {} milliseconds.", System.currentTimeMillis() - startTime);
        musicDatabaseBuilder.buildMusicDatabase(albums);
    }

    public String getDataFilesDirectory() {
        return System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" +
                File.separator + "resources" + File.separator + "data_files" + File.separator;
    }

    private void parseAlbumFromFile(File file, Set<MusicAlbum> albums) {
        try (final var reader = new FileReader(file)) {
            if (isJsonFile(file)) {
                albums.add(gson.fromJson(reader, MusicAlbum.class));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<MusicAlbum> parallelDataFileParsing(File[] filesInDirectory) {

        final Set<MusicAlbum> albums = new HashSet<>();
        final int numThreads = Runtime.getRuntime().availableProcessors();
        final ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (File file : filesInDirectory) {
            final Runnable runnable = () -> parseAlbumFromFile(file, albums);
            executorService.execute(runnable);
        }

        // Shutdown the executor service when all tasks are done
        executorService.shutdown();
        return albums;
    }

    private boolean isValid(String dataFilesPath) {
        if (dataFilesPath.isEmpty()) {
            logger.error("Error: Wrong path provided {}.", dataFilesPath);
            return false;
        }

        final var directory = new File(dataFilesPath);
        if (! directory.isDirectory()) {
            logger.error("Error: Data files directory is wrong !");
            return false;
        }
        return true;
    }

    private boolean isJsonFile(File file) {
        if (! file.getName().endsWith(".json")) {
            logger.info("{} is not a .json file.", file.getName());
            return false;
        }
        return true;
    }
}
