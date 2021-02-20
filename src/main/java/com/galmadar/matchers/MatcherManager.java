package com.galmadar.matchers;

import com.galmadar.textFinder.TextSearchFinished;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatcherManager implements MatchersFinished {
    private final Logger logger = LogManager.getLogger(getClass());

    private final List<String> stringsToFind;
    private final TextSearchFinished textSearchFinished;
    private final Map<Integer, Matcher> matchers = new HashMap<>();
    private final List<WordsLocationsCollection> allWordsLocationCollections = new ArrayList<>();
    private boolean finishedText = false;

    public MatcherManager(List<String> stringsToFind, TextSearchFinished textSearchFinished) {
        this.stringsToFind = stringsToFind;
        this.textSearchFinished = textSearchFinished;
    }

    public void startNewMatcher(int offset, StringBuilder partOfText) {
        logger.debug("Start finding words from offset {}", offset);
        Matcher newMatcher = new Matcher(offset, partOfText, stringsToFind, this);
        matchers.put(offset, newMatcher);
        logger.debug("Start new matcher (currently there are {} matchers)", matchers.size());
        new Thread(newMatcher).start();
    }

    public void notifyIterateOverFileFinished() {
        logger.debug("Notified that Iterating over (big) file is DONE");
        this.finishedText = true;
    }

    @Override
    public void handleFinished(WordsLocationsCollection wordsLocationsCollection, int lineOffset) {
        logger.debug("Matcher of offset {} has finished and found {} words", lineOffset, wordsLocationsCollection.numberOfWords());
        allWordsLocationCollections.add(wordsLocationsCollection);
        logger.debug("Remove Matcher for 'lineOffset' {}", lineOffset);
        matchers.remove(lineOffset);
        logger.debug("Currently there are {} matchers", matchers.size());
        if (finishedText && matchers.size() <= 0) {
            logger.info("'finishedText' is true, and there are no matchers. Calling to 'handleSearchResults'");
            textSearchFinished.handleSearchResults(allWordsLocationCollections);
        }
    }
}
