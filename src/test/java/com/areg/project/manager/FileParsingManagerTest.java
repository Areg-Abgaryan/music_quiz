/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.manager;

import com.areg.project.builder.MusicDatabaseBuilder;
import org.apache.commons.lang3.StringUtils;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;

public class FileParsingManagerTest {

    private FileParsingManager fileParsingManager;
    private MusicDatabaseBuilder musicDatabaseBuilder;

    @BeforeTest
    void setUp() {
        fileParsingManager = new FileParsingManager();
        musicDatabaseBuilder = Mockito.spy(MusicDatabaseBuilder.class);
    }

    @Test
    void testData() {
        //  FIXME !! Add data checks for all types of models
    }

    @Test
    void testGetDataFilesDirectory() {
        final String dataFilesDirectory = fileParsingManager.getDataFilesDirectory();
        Assert.assertFalse(StringUtils.isBlank(dataFilesDirectory));
        Assert.assertTrue(dataFilesDirectory.contains("/src/main/resources/data_files"));
    }

    @Test
    void testParseDataFilesWhenPathIsEmpty() {
        final String dataFilesPath = "";
        fileParsingManager.parseDataFiles(dataFilesPath);
        Mockito.verify(musicDatabaseBuilder, Mockito.times(0)).buildMusicDatabase(any());
    }
}
