package com.galmadar.mainFinder;

import com.galmadar.aggregator.Aggregator;
import com.galmadar.matchers.MatcherManager;
import com.galmadar.matchers.WordsAndLocations;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;

public class TextFinder implements FindFinished {

    private final Logger logger = LogManager.getLogger(getClass());

    private final String bigTextFileName;
    private final Integer numberOfLinesForMatcher;
    private final MatcherManager matcherManager;
    private final Aggregator aggregator = new Aggregator();

    public TextFinder(String bigTextFileName, String stringsToFind) throws IOException {
        this(bigTextFileName, stringsToFind, 5000);
    }

    public TextFinder(String bigText, String stringsToFind, Integer numberOfLinesForMatcher) throws IOException {
        this.numberOfLinesForMatcher = numberOfLinesForMatcher;
        this.bigTextFileName = bigText;
        List<String> wordsToFind = IOUtils.readLines(getClass().getResource(stringsToFind).openStream(), Charset.defaultCharset());
        this.matcherManager = new MatcherManager(wordsToFind, this);
    }

    public void startWork() {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            logger.info("Get a BufferedReader for the file to search in");
            String fileName = getClass().getResource(this.bigTextFileName).getFile();
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);

            logger.info("Prepare StringBuilder, and indexes variables");
            Integer lastOffset = 1, lineNumber = 1;
            String currentLine;
            StringBuilder sb = new StringBuilder();

            logger.info("Iterate over the file and prepare part of the texts for the matchers");
            while ((currentLine = bufferedReader.readLine()) != null) {
                sb.append(currentLine);
                lineNumber++;
                if (lineNumber % this.numberOfLinesForMatcher == 0) {
                    matcherManager.findWords(lastOffset, sb);
                    sb = new StringBuilder();
                    lastOffset = lineNumber;
                } else {
                    sb.append("\n");
                }
            }

            /* Call for matcher for the last buffer of text */
            if (sb.length() > 0) {
                matcherManager.findWords(lastOffset, sb);
            }

            matcherManager.finishedText();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeReader(bufferedReader);
            closeReader(fileReader);
        }
    }

    private void closeReader(Reader r) {
        if (r != null) {
            try {
                r.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void finished(List<WordsAndLocations> wordsWithLocations) {
        aggregator.aggregateWords(wordsWithLocations);
        System.out.println(aggregator);
    }
}