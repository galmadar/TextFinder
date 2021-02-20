package com.galmadar.matchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordsLocationsCollection {

    public Map<String, List<WordLocation>> mapOfWords = new HashMap<>();

    public void addLocationForWord(String word, List<WordLocation> wordLocation) {
        if (!mapOfWords.containsKey(word)) {
            mapOfWords.put(word, new ArrayList<>());
        }
        mapOfWords.get(word).addAll(wordLocation);
    }

    public void addLocationForWord(String word, WordLocation wordLocation) {
        ArrayList<WordLocation> objects = new ArrayList<>();
        objects.add(wordLocation);
        addLocationForWord(word, objects);
    }

    public int numberOfWords() {
        return mapOfWords.size();
    }
}
