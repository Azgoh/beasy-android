package com.example.beasy.data.model;

public class Category {
    private final String name;
    private final String emoji;

    public Category(String name, String emoji) {
        this.name  = name;
        this.emoji = emoji;
    }

    public String getName()  { return name;  }
    public String getEmoji() { return emoji; }
}

