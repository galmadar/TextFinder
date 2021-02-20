package com.galmadar.textFinder;

import com.galmadar.matchers.WordsLocationsCollection;

import java.util.List;

public interface TextSearchFinished {
    void handleSearchResults(List<WordsLocationsCollection> wordsWithLocations);
}
