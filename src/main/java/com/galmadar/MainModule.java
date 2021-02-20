package com.galmadar;

import com.galmadar.mainFinder.TextFinder;

import java.io.IOException;

public class MainModule {

    public static void main(String[] args) {
        try {
            TextFinder tf = new TextFinder("/big.txt", "/textsToFind.txt");
            tf.startWork();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

