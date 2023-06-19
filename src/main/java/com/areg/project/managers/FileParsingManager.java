/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.models.MusicAlbum;
import com.areg.project.builders.MusicDatabaseBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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

        final Set<MusicAlbum> albums = new HashSet<>();
        final long startTime = System.currentTimeMillis();

        for (var file : filesInDirectory) {
            try (var reader = new FileReader(file)) {
                if (! isJsonFile(file)) {
                    continue;
                }
                albums.add(gson.fromJson(reader, MusicAlbum.class));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        logger.debug("parseDataFiles takes {} milliseconds.", + (System.currentTimeMillis() - startTime));
        musicDatabaseBuilder.buildMusicDatabase(albums);
    }

    public String getDataFilesDirectory() {
        return System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" +
                File.separator + "resources" + File.separator + "data_files" + File.separator;
    }

    private boolean isValid(String dataFilesPath){
        if (dataFilesPath.isEmpty()) {
            logger.error("Error : Invalid path provided {}.", dataFilesPath);
            return false;
        }

        if (! new File(dataFilesPath).isDirectory()) {
            logger.error("Error : Data files directory is wrong !");
            return false;
        }
        return true;
    }

    private boolean isJsonFile(File file) {
        if (! file.getName().endsWith(".json")) {
            logger.debug("{} is not a .json file.", file.getName());
            return false;
        }
        return true;
    }
}
