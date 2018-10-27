package com.example.chahyunbin.cwapp;

public class BibleItem {

    String chapther;
    String paragraph;
    String sentence;

    public BibleItem(String chapther, String paragraph, String sentence) {
        this.chapther = chapther;
        this.paragraph = paragraph;
        this.sentence = sentence;
    }

    public String getChapther() {
        return chapther;
    }

    public void setChapther(String chapther) {
        this.chapther = chapther;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
