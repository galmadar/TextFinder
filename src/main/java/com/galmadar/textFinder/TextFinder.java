package com.galmadar.textFinder;

import com.galmadar.aggregator.Aggregator;
import com.galmadar.matchers.MatcherManager;
import com.galmadar.matchers.WordsLocationsCollection;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class TextFinder implements TextSearchFinished {

    private final Logger logger = LogManager.getLogger(getClass());

    private final String bigTextFileName;
    private final int numberOfLinesForMatcher;
    private final MatcherManager matcherManager;
    private final Aggregator aggregator = new Aggregator();

    public TextFinder(String bigText, String stringsToFind, int numberOfLinesForMatcher) throws IOException {
        this.numberOfLinesForMatcher = numberOfLinesForMatcher;
        this.bigTextFileName = bigText;
        FileInputStream fileInputStream = FileUtils.openInputStream(new File(stringsToFind));
        List<String> wordsToFind = IOUtils.readLines(fileInputStream, Charset.defaultCharset());
        this.matcherManager = new MatcherManager(wordsToFind, this);
    }

    public void startWork() {
        LineIterator lineIterator = null;
        try {
            logger.debug("Prepare indexes and StringBuilder for iterating over the (big) file");
            int newMatcherOffset = 1, currentLineNumber = 1;
            StringBuilder sb = new StringBuilder();

            File file = new File(bigTextFileName);
            logger.debug("Create LineIterator for file {}", file.getAbsolutePath());
            lineIterator = FileUtils.lineIterator(file);

            logger.debug("Iterate over the file and prepare parts of texts for the matchers");
            while (lineIterator.hasNext()) {
                sb.append(lineIterator.next());
                currentLineNumber++;
                if (currentLineNumber % this.numberOfLinesForMatcher == 0) {
                    logger.debug("Start new Matcher for lines {} to {}", newMatcherOffset, currentLineNumber);
                    matcherManager.startNewMatcher(newMatcherOffset, sb);

                    logger.trace("Init new StringBuilder. Set 'newMatcherOffset' to {}", currentLineNumber);
                    sb = new StringBuilder();
                    newMatcherOffset = currentLineNumber;
                } else {
                    sb.append("\n");
                }
            }

            /* Call for matcher for the last buffer of text */
            if (sb.length() > 0) {
                logger.debug("Create new Matcher for lines {} till the end ({})", newMatcherOffset, currentLineNumber);
                matcherManager.startNewMatcher(newMatcherOffset, sb);
            }

            logger.debug("Finished reading big file. call to 'notifyIterateOverFileFinished'");
            matcherManager.notifyIterateOverFileFinished();
        } catch (Exception e) {
            logger.error("Failed on 'startWork'", e);
        } finally {
            if (lineIterator != null) {
                try {
                    lineIterator.close();
                } catch (IOException e) {
                    logger.error("Failed to close 'lineIterator'", e);
                }
            }
        }
    }

    @Override
    public void handleSearchResults(List<WordsLocationsCollection> wordsWithLocations) {
        logger.debug("On 'handleSearchResults' with {} 'wordsWithLocations'. Call to 'aggregateWords'", wordsWithLocations.size());
        aggregator.aggregateWords(wordsWithLocations);
        logger.info("Aggregator results:\n{}", aggregator.toString());
    }
}