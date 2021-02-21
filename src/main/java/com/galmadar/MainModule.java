package com.galmadar;

import com.galmadar.textFinder.TextFinder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class MainModule {

    private static final Logger logger = LogManager.getLogger(MainModule.class);

    public static void main(String[] args) {
        try {
            String bigTxtFile = MainModule.class.getResource("/big.txt").getFile().replaceAll("%20", " ");
            String stringsToFindFile = MainModule.class.getResource("/textsToFind.txt").getFile().replaceAll("%20", " ");
            int numberOfLinesPerMatcher = 5000;
            logger.info("Looking for all words from file {}", stringsToFindFile);
            logger.info("Searching in {}", bigTxtFile);
            logger.info("Create Matcher for {} lines", numberOfLinesPerMatcher);
            TextFinder tf = new TextFinder(bigTxtFile, stringsToFindFile, numberOfLinesPerMatcher);
            tf.startWork();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

