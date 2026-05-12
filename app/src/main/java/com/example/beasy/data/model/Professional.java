package com.example.beasy.data.model;

public class Professional {
    private final String name;
    private final String category;
    private final float  rating;
    private final int    reviewCount;
    private final String price;

    public Professional(String name, String category, float rating, int reviewCount, String price) {
        this.name        = name;
        this.category    = category;
        this.rating      = rating;
        this.reviewCount = reviewCount;
        this.price       = price;
    }

    public String getName()        { return name;        }
    public String getCategory()    { return category;    }
    public float  getRating()      { return rating;      }
    public int    getReviewCount() { return reviewCount; }
    public String getPrice()       { return price;       }
}

