package com.galmadar.matchers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Matcher implements Runnable {

    private final Logger logger = LogManager.getLogger(getClass());
    private final StringBuilder strSearch;
    private final int lineOffset;
    private final List<String> wordsToSearch;
    private final MatchersFinished matchersFinished;
    private final WordsLocationsCollection wordsLocationsCollection = new WordsLocationsCollection();

    public Matcher(int lineOffset, StringBuilder strSearch, List<String> wordsToSearch, MatchersFinished matchersFinished) {
        this.strSearch = strSearch;
        this.lineOffset = lineOffset;
        this.wordsToSearch = wordsToSearch;
        this.matchersFinished = matchersFinished;
        logger.debug("Matcher Created for offset {}. Searching for {} words in {} characters", lineOffset, wordsToSearch.size(), strSearch.length());
    }

    public WordsLocationsCollection startSearch() {
        String[] allLines = this.strSearch.toString().split("\n");
        logger.debug("Matcher of offset {}, searching in {} lines", lineOffset, allLines.length);

        for (int i = 0; i < allLines.length; i++) {
            String currentLine = allLines[i];
            for (String currentWordToSearch : wordsToSearch) {
                int indexOfWord = 0;
                while ((indexOfWord = currentLine.indexOf(currentWordToSearch, indexOfWord)) >= 0) {
                    WordLocation wordLocation = new WordLocation(this.lineOffset + i, indexOfWord);
                    wordsLocationsCollection.addLocationForWord(currentWordToSearch, wordLocation);
                    indexOfWord++;
                }
            }
        }

        logger.debug("found {} words", wordsLocationsCollection.numberOfWords());
        return wordsLocationsCollection;
    }

    @Override
    public void run() {
        logger.debug("Running Matcher for offset {}", lineOffset);
        WordsLocationsCollection wordsLocationsCollection = startSearch();

        logger.debug("Calling to 'handleFinished', for offset {}", lineOffset);
        matchersFinished.handleFinished(wordsLocationsCollection, lineOffset);
    }
}

