package com.example.cswbooks.models;

public class Book {
    private String title;
    private String author;
    private String sellerName;
    private double price;
    private int copiesAvailable;
    private int coverResId;

    public Book(String title, String author, String sellerName,
                double price, int copiesAvailable, int coverResId) {
        this.title = title;
        this.author = author;
        this.sellerName = sellerName;
        this.price = price;
        this.copiesAvailable = copiesAvailable;
        this.coverResId = coverResId;
    }

    public String getTitle()         { return title; }
    public String getAuthor()        { return author; }
    public String getSellerName()    { return sellerName; }
    public double getPrice()         { return price; }
    public int getCopiesAvailable()  { return copiesAvailable; }
    public int getCoverResId()       { return coverResId; }
}