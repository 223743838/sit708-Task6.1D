package com.example.learning.model;

// HistoryItem.java
public class HistoryItem {
    private String title;
    private String date;
    private int score;

    public HistoryItem(String title, String date, int score) {
        this.title = title;
        this.date = date;
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public int getScore() {
        return score;
    }
}


