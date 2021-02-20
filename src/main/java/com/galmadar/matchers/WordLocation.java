package com.galmadar.matchers;

public class WordLocation {

    private Integer line;
    private Integer column;

    WordLocation(Integer line, Integer column) {
        setLine(line);
        setColumn(column);
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "WordLocation{" +
                "line=" + line +
                ", column=" + column +
                '}';
    }
}
