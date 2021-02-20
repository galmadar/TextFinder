package com.galmadar.matchers;

import com.galmadar.mainFinder.FindFinished;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatcherManager implements MatchersFinished {
    private final Logger logger = LogManager.getLogger(getClass());

    private final List<String> stringsToFind;
    private final FindFinished findFinished;
    private final Map<Integer, Matcher> matchers = new HashMap<>();
    private final List<WordsAndLocations> allWordsAndLocations = new ArrayList<>();
    private boolean finishedText = false;

    public MatcherManager(List<String> stringsToFind, FindFinished findFinished) {
        this.stringsToFind = stringsToFind;
        this.findFinished = findFinished;
    }

    public void findWords(Integer offset, StringBuilder partOfText) {
        logger.info("Start finding words from offset {}", offset);
        Matcher newMatcher = new Matcher(offset, partOfText, stringsToFind, this);
        matchers.put(offset, newMatcher);
        Thread t = new Thread(newMatcher);
        t.start();
    }

    public void finishedText() {
        this.finishedText = true;
    }

    @Override
    public void handleFinished(WordsAndLocations wordsAndLocations, Integer lineOffset) {
        allWordsAndLocations.add(wordsAndLocations);
        matchers.remove(lineOffset);
        if (this.finishedText && matchers.size() <= 0) {
            this.findFinished.finished(allWordsAndLocations);
        }
    }
}
