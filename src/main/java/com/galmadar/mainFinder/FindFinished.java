package com.galmadar.mainFinder;

import com.galmadar.matchers.WordsAndLocations;

import java.util.List;

public interface FindFinished {
    void finished(List<WordsAndLocations> wordsWithLocations);
}
