package com.galmadar.matchers;

import java.util.List;

public class Matcher implements Runnable {
    private final StringBuilder strSearch;
    private final Integer lineOffset;
    private final List<String> wordsToSearch;
    private final MatchersFinished matchersFinished;
    private final WordsAndLocations wordsAndLocations = new WordsAndLocations();

    public Matcher(Integer lineOffset, StringBuilder strSearch, List<String> wordsToSearch, MatchersFinished matchersFinished) {
        this.strSearch = strSearch;
        this.lineOffset = lineOffset;
        this.wordsToSearch = wordsToSearch;
        this.matchersFinished = matchersFinished;
    }

    public WordsAndLocations startSearch() {
        String[] split = this.strSearch.toString().split("\n");

        for (Integer i = 0; i < split.length; i++) {
            String line = split[i];
            for (String toSearch : wordsToSearch) {
                int indexOfWord = line.indexOf(toSearch);
                if (indexOfWord >= 0) {
                    WordLocation wordLocation = new WordLocation(this.lineOffset + i, indexOfWord);
                    wordsAndLocations.addLocationForWord(toSearch, wordLocation);
                }
            }
        }

        return wordsAndLocations;
    }

    @Override
    public void run() {
        WordsAndLocations wordsAndLocations = startSearch();
        matchersFinished.handleFinished(wordsAndLocations, lineOffset);
    }
}

