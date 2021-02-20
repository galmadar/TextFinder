# TextFinder

Java program to find specific strings in a large text.

Inputs:
- Large text file
- Text file with the words to find

Main Module:
1. Reads the large text file and divide it into parts.
2. Each part of the file "goes" into the `MatcherManger` which creates new `Matcher`. Each `Mathcer` starts to search on a separate thread.
3. After all of the `Matcher`s finished, the `Aggregator` "sum up" all the words and their locations on the large file.
