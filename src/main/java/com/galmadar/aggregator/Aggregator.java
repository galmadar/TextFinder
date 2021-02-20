package com.galmadar.aggregator;

import com.galmadar.matchers.WordLocation;
import com.galmadar.matchers.WordsAndLocations;

import java.util.List;

public class Aggregator {
    private WordsAndLocations aggregatedWordsAndLocations = new WordsAndLocations();

    public void aggregateWords(List<WordsAndLocations> wordsWithLocations) {
        wordsWithLocations.forEach(wordsAndLocations -> {
            wordsAndLocations.mapOfWords.forEach((s, wordLocations) -> {
                aggregatedWordsAndLocations.addLocationForWord(s, wordLocations);
            });
        });
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        aggregatedWordsAndLocations.mapOfWords.forEach((s, wordLocations) -> {
            sb.append(String.format("%s --> ", s));
            sb.append("[");
            for (int i = 0; i < wordLocations.size(); i++) {
                WordLocation wordLocation = wordLocations.get(i);
                sb.append(String.format("[lineOffset=%d, charOffset=%d]", wordLocation.getLine(), wordLocation.getColumn()));
                if (i + 1 < wordLocations.size()) {
                    sb.append(", ");
                }
            }
            sb.append("]\n");
        });
        return sb.toString();
    }
}
