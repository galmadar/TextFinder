package com.galmadar.aggregator;

import com.galmadar.matchers.WordLocation;
import com.galmadar.matchers.WordsLocationsCollection;

import java.util.List;

public class Aggregator {
    private final WordsLocationsCollection aggregatedWordsLocationsCollection = new WordsLocationsCollection();

    public void aggregateWords(List<WordsLocationsCollection> wordsWithLocations) {
        wordsWithLocations.forEach(wordsAndLocations -> {
            wordsAndLocations.mapOfWords.forEach(aggregatedWordsLocationsCollection::addLocationForWord);
        });
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        aggregatedWordsLocationsCollection.mapOfWords.forEach((s, wordLocations) -> {
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
